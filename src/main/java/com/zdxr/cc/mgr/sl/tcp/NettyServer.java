package com.zdxr.cc.mgr.sl.tcp;

import com.zdxr.cc.mgr.sl.common.SpringContextUtils;
import com.zdxr.cc.mgr.sl.data.SlDeviceClientVoData;
import com.zdxr.cc.mgr.sl.service.ISlDeviceClientService;
import com.zdxr.cc.mgr.sl.tcp.bean.ClientInfo;
import com.zdxr.cc.mgr.sl.tcp.bean.Message;
import com.zdxr.cc.mgr.sl.tcp.bean.RegistMsg;
import com.zdxr.cc.mgr.sl.tcp.bean.RegistResMsg;
import com.zdxr.cc.mgr.sl.tcp.bean.type.MsgType;
import com.zdxr.cc.mgr.sl.tcp.util.GsonUtil;
import com.zdxr.cc.mgr.sl.tcp.util.MsgBuilder;
import com.zdxr.cc.mgr.sl.tcp.util.UUIDUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class NettyServer {
    Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    //当前客户端连接数量
    private AtomicInteger clientCount = new AtomicInteger();

    //key ：hashCode
    private Map<Integer, ClientInfo> connCacheMap = Collections.synchronizedMap(new HashMap<Integer, ClientInfo>());

    public NettyServer() {
    }

    /***
     * NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器，
     * Netty提供了许多不同的EventLoopGroup的实现用来处理不同传输协议。 在这个例子中我们实现了一个服务端的应用，
     * 因此会有2个NioEventLoopGroup会被使用。 第一个经常被叫做‘boss’，用来接收进来的连接。
     * 第二个经常被叫做‘worker’，用来处理已经被接收的连接， 一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上。
     * 如何知道多少个线程已经被使用，如何映射到已经创建的Channels上都需要依赖于EventLoopGroup的实现，
     * 并且可以通过构造函数来配置他们的关系。
     */
    @PostConstruct
    public void run() throws Exception {
        bossGroup = new NioEventLoopGroup(5);
        workerGroup = new NioEventLoopGroup(5);
        Integer port = 9093;
        try {
            System.out.println("准备运行端口：" + port);

            ServerBootstrap b = new ServerBootstrap();
            b = b.group(bossGroup, workerGroup);

            //ServerSocketChannel以NIO的selector为基础进行实现的，用来接收新的连接,这里告诉Channel如何获取新的连接.
            b = b.channel(NioServerSocketChannel.class);

            //handler：是发生在初始化的时候，childHandler()是发生在客户端连接之后
            b = b.handler(new LoggingHandler(LogLevel.INFO));

            b = b.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    final String uuid = UUIDUtil.getUUid();
                    final int channelCode = ch.hashCode();
                    final String remoteIp = ch.remoteAddress().getHostString();
                    final int remotePort = ch.remoteAddress().getPort();
                    logger.info("[uuid:{}]client connect IP:{},port:{}:hashCode:{}", uuid, remoteIp, remotePort, channelCode);

                    // 连接关闭监听
                    ch.closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            logger.info("[uuid:{}]客户端连接断开 IP:{},port:{}:hashCode:{}", uuid, remoteIp, remotePort, channelCode);
                            clientCount.addAndGet(-1);
                            connCacheMap.remove(channelCode);
                            logger.info("当前连接数{}", clientCount.get());
                        }
                    });

                    if (clientCount.get() >= ServerConfig.MAX_CONNECTION) {//超出最大连接断开
                        logger.info("[uuid:{}]超过最大连接数:hashcode{},remoteIP：{}", uuid, channelCode, ch.remoteAddress());
                        ch.close();
                    }

                    clientCount.addAndGet(1);
                    logger.info("当前连接数{}", clientCount.get());
                    ch.pipeline().addLast(new MsgDecoder());//outboundHandler 按住注册先后书序的逆序执行
                    ch.pipeline().addLast(new MsgEncoder());//InboundHandler按住注册的先后顺序执行
                    ch.pipeline().addLast("registHandler", new ChannelInboundHandlerAdapter() {//连接后验证连接信息
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object objMsg) throws Exception {
                            //super.channelRead(ctx, objMsg);
                            logger.info("registHandler:成功读取客户端发送的消息,hashCode:{},content:{}", ctx.channel().hashCode(), objMsg);
                            if (!(objMsg instanceof Message)) {
                                return;
                            }
                            Message msg = (Message) objMsg;

                            if (!msg.getType().equals(MsgType.REGISTER)) {
                                logger.info("不处理，传递到下一个InboundHandler处理");
                                ctx.fireChannelRead(objMsg);//转到下一个InboundHandler处理
                                return;
                            }

                            ClientInfo clientInfo = connCacheMap.get(ctx.channel().hashCode());
                            clientInfo.getRegistDisposable().dispose();//取消因未收到注册信息关闭连接
                            clientInfo.setRegistDisposable(null);
                            RegistMsg regMsg = GsonUtil.getGosn().fromJson(msg.getContent(), RegistMsg.class);
                            Message responseMsg = MsgBuilder.buildResponseMsg(msg);
                            responseMsg.setContent(new RegistResMsg("1"));
                            clientInfo.setUserId(regMsg.getUserId());
                            clientInfo.setToken(regMsg.getToken());
                            clientInfo.setClientName(regMsg.getClientName());
                            clientInfo.setClientCode(regMsg.getClientCode());

                            logger.info("下发后客户端返回消息：" + msg.getContent());

                            SlDeviceClientVoData clientVoData = new SlDeviceClientVoData();
                            clientVoData.setClientCode(clientInfo.getClientCode());
                            clientVoData.setClientName(clientInfo.getClientName());
                            ISlDeviceClientService clientService = SpringContextUtils.getBean(ISlDeviceClientService.class);
                            clientService.saveDeviceClient(clientVoData);
                            logger.info("更新操作端code:" + clientVoData.getClientCode() + ",name:" + clientVoData.getClientName());

                            logger.info("回执消息,responseMsg：{}", responseMsg);
                            ctx.channel().writeAndFlush(responseMsg);
                            ScheduledFuture<?> scheduledFuture = ctx.executor().schedule(new Runnable() {
                                public void run() {
                                    logger.info("未收到心跳消息，close");
                                    clientInfo.getChannel().close();
                                }
                            }, ServerConfig.HEARTBEAT_CHECK_TIME, TimeUnit.SECONDS);
                            clientInfo.setHeartbeatCheckScheduledFuture(scheduledFuture);
                            //此处可添加踢人操作
                        }
                    });

                    ClientInfo clientInfo = new ClientInfo();
                    clientInfo.setChannel(ch);
                    //这里不用担心每个连接都会创建一个线程，这里是用的线程池
                    Disposable disposable = Observable.timer(10, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long t) throws Exception {
                            ClientInfo clientInfo = connCacheMap.get(ch.hashCode());
                            if (clientInfo.getUserId() == null || clientInfo.getUserId().trim().equals("")) {
                                logger.info("未收到连接注册消息，close");
                                clientInfo.getChannel().close();
                            }
                        }
                    });
                    clientInfo.setRegistDisposable(disposable);
                    connCacheMap.put(ch.hashCode(), clientInfo);
                    ch.pipeline().addLast(new MessageHandler(clientInfo));
                }
            });

            /***
             * 你可以设置这里指定的通道实现的配置参数。 我们正在写一个TCP/IP的服务端，
             * 因此我们被允许设置socket的参数选项比如tcpNoDelay和keepAlive。
             * 请参考ChannelOption和详细的ChannelConfig实现的接口文档以此可以对ChannelOptions的有一个大概的认识。
             */
            b = b.option(ChannelOption.SO_BACKLOG, 128);

            /***
             * option()是提供给NioServerSocketChannel用来接收进来的连接。
             * childOption()是提供给由父管道ServerChannel接收到的连接，
             * 在这个例子中也是NioServerSocketChannel。
             */
            b = b.childOption(ChannelOption.SO_KEEPALIVE, true);
            b.localAddress(new InetSocketAddress(port));
            ChannelFuture future = b.bind().sync();
            if (future.isSuccess()) {
                logger.info("启动 Netty Server");
            }

        } catch (Exception e) {
            logger.info("启动Netty失败");
        }
    }

    @PreDestroy
    public void destory() throws InterruptedException {
        bossGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
        logger.info("关闭Netty");
    }

    public Map<Integer, ClientInfo> getConnCacheMap() {
        return connCacheMap;
    }
}
