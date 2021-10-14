package com.zdxr.cc.mgr.sl.tcp;

import com.google.gson.Gson;
import com.zdxr.cc.mgr.sl.common.SpringContextUtils;
import com.zdxr.cc.mgr.sl.data.DeleteDataVoData;
import com.zdxr.cc.mgr.sl.data.SlSendCallBackVoData;
import com.zdxr.cc.mgr.sl.service.ISlDeviceSendGroupService;
import com.zdxr.cc.mgr.sl.service.impl.TaskSendLock;
import com.zdxr.cc.mgr.sl.tcp.bean.ClientInfo;
import com.zdxr.cc.mgr.sl.tcp.bean.HeartbeatMsg;
import com.zdxr.cc.mgr.sl.tcp.bean.Message;
import com.zdxr.cc.mgr.sl.tcp.bean.type.MsgType;
import com.zdxr.cc.mgr.sl.tcp.util.GsonUtil;
import com.zdxr.cc.mgr.sl.tcp.util.MsgBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MessageHandler extends SimpleChannelInboundHandler<Message> {
    private ClientInfo clientInfo;

    public MessageHandler(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    static Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        final Channel channel = ctx.channel();
        System.out.println(this.hashCode());
        logger.info("成功读取客户端发送的消息,hashCode:{},content:{}", ctx.channel().hashCode(), msg);

        Message responseMsg = MsgBuilder.buildResponseMsg(msg);

        if (msg.getType().equals(MsgType.HEARTBEAT)) {
            HeartbeatMsg heartbeatMsg = GsonUtil.getGosn().fromJson(msg.getContent(), HeartbeatMsg.class);
            logger.info("收到心跳消息");

            if (clientInfo.getHeartbeatCheckScheduledFuture() != null) {
                clientInfo.getHeartbeatCheckScheduledFuture().cancel(true);
            }

            ScheduledFuture<?> scheduledFuture = ctx.executor().schedule(new Runnable() {
                public void run() {
                    logger.info("未检测到心跳消息，close");
                    closeChannel(clientInfo.getChannel());
                }
            }, ServerConfig.HEARTBEAT_CHECK_TIME, TimeUnit.SECONDS);

            clientInfo.setHeartbeatCheckScheduledFuture(scheduledFuture);
        }
        if (msg.getType().equals(MsgType.PUSH_RESP)) {
            logger.info("下发后客户端返回消息：" + msg.getContent());
            ISlDeviceSendGroupService groupService = SpringContextUtils.getBean(ISlDeviceSendGroupService.class);
            Gson gson = new Gson();
            SlSendCallBackVoData callBackVoData = gson.fromJson(msg.getContent(), SlSendCallBackVoData.class);
            groupService.sendGroupCallBack(callBackVoData.getGroupId());
        }
        if (msg.getType().equals(MsgType.ROBBINGORDER_RESP)) {
            logger.info("删除下发数据后客户端返回消息：" + msg.getContent());
            ISlDeviceSendGroupService sendGroupService = SpringContextUtils.getBean(ISlDeviceSendGroupService.class);
            Gson gson = new Gson();
            DeleteDataVoData callBackVoData = gson.fromJson(msg.getContent(), DeleteDataVoData.class);
            sendGroupService.deleteSendDataCallBack(callBackVoData);
            //-------------下发后修改装改
            TaskSendLock.over("group" + callBackVoData.getGroupId());
        }
        logger.info("回执消息,responseMsg：{}", responseMsg);
        channel.writeAndFlush(responseMsg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        printInfo("channelActive客户端连接到服务端后");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        printInfo("handlerAdded");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelRegistered(ctx);
        printInfo("channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelUnregistered(ctx);
        ctx.close();
        printInfo("channelUnregistered:" + ctx.channel().hashCode());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelInactive(ctx);
        printInfo("channelInactive");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelReadComplete(ctx);
        printInfo("channelReadComplete");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // TODO Auto-generated method stub
        super.userEventTriggered(ctx, evt);
        printInfo("userEventTriggered");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelWritabilityChanged(ctx);
        printInfo("channelWritabilityChanged");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // TODO Auto-generated method stub
        super.exceptionCaught(ctx, cause);
        printInfo("exceptionCaught");
    }

    private void printInfo(String str) {
        System.out.println(str);
        logger.info(str);
        ;
    }

    private void closeChannel(Channel channel) {
        if (channel != null) {
            logger.info("server  handler close client" + channel.hashCode());
            channel.close();
        }
    }


}
