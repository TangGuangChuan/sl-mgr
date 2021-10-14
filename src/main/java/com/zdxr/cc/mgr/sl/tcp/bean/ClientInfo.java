package com.zdxr.cc.mgr.sl.tcp.bean;

import io.netty.channel.socket.SocketChannel;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ScheduledFuture;

@Getter
@Setter
public class ClientInfo {

    private String clientCode;

    private String clientName;

    private SocketChannel channel;

    private String userId;

    private String token;

    //用于监听客户端连接后没有发送连接注册信息
    private Disposable registDisposable;

    //用于心跳检测
    private ScheduledFuture<?> heartbeatCheckScheduledFuture;

}
