package com.github.xingshuangs.iot.net.netty.client;

import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import com.github.xingshuangs.iot.net.netty.handler.ClientHeartbeatHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xingshuang
 */
@Slf4j
public class NettyClient extends Service {

    /**
     * 是否正在断线重连
     */
    private AtomicBoolean reConnecting = new AtomicBoolean(false);

    /**
     * 建立连接超时时间(毫秒), 默认3000
     */
    protected int connectTimeout = 3000;

    /**
     * 同步调用默认超时时间(毫秒)
     */
    protected int syncInvokeTimeout = 3000;

    /**
     * 当前连接Server
     */
    protected SocketAddress server;

    /**
     * 通道
     */
    protected Channel channel;

    /**
     * netty的EventLoopGroup
     */
    protected EventLoopGroup group;

    /**
     * netty的启动配置对象
     */
    protected Bootstrap bootstrap;

    public NettyClient() {
        super();
    }

    @Override
    protected void init() {
        super.init();
        // 将一些handler放在这里初始化是为了防止多例的产生。
        if (checkHeartbeat) {
            idleStateHandler = new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
            heartbeatHandler = new ClientHeartbeatHandler();
        }

        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
    }

    /**
     * 同步连接
     *
     * @return ChannelFuture
     */
    public ChannelFuture connect() {
        return this.connect(new InetSocketAddress(this.ip, this.port), true);
    }

    /**
     * 同步连接
     *
     * @param socketAddress 服务器地址
     * @return ChannelFuture
     */
    public ChannelFuture connect(final SocketAddress socketAddress) {
        return this.connect(socketAddress, true);
    }

    /**
     * 连接
     *
     * @param socketAddress 服务器地址
     * @param sync          是否同步
     * @return ChannelFuture
     */
    public ChannelFuture connect(final SocketAddress socketAddress, boolean sync) {
        init();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, this.keepAlive)
                .option(ChannelOption.TCP_NODELAY, this.tcpNoDelay);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();

                // 注册各种自定义Handler
                LinkedHashMap<String, ChannelHandler> handlers = getHandlers();
                for (String key : handlers.keySet()) {
                    pipeline.addLast(key, handlers.get(key));
                }

                if (checkHeartbeat) {
                    pipeline.addLast("idleStateHandler", idleStateHandler);
                    pipeline.addLast("heartbeatHandler", heartbeatHandler);
                }
            }
        });

        return doConnect(socketAddress, sync);
    }

    /**
     * 执行连接
     *
     * @param socketAddress 服务器地址
     * @param sync          是否同步
     * @return ChannelFuture
     */
    protected ChannelFuture doConnect(final SocketAddress socketAddress, boolean sync) {
        // 连接的server
        this.server = socketAddress;
        try {
            ChannelFuture future = bootstrap.connect(socketAddress).sync();
            Semaphore semaphore = new Semaphore(0);
            future.addListener((ChannelFutureListener) ch -> {
                ch.await();
                if (ch.isSuccess()) {
                    channel = ch.channel();
                    log.debug("Netty客户端连接 '{}' 成功.", socketAddress);
                } else {
                    log.error("Netty客户端连接 '{}' 失败, 原因: '{}'.", socketAddress, ch.cause());
                }

                semaphore.release(Integer.MAX_VALUE - semaphore.availablePermits());
            });
            future.channel().closeFuture();

            if (sync) {
                Throwable cause = null;

                if (!semaphore.tryAcquire(connectTimeout, TimeUnit.MILLISECONDS)) {
                    cause = new SocketTimeoutException("time out.");
                }

                if (cause != null) {
                    throw new SocketRuntimeException(cause);
                }
            }
            return future;
        } catch (Exception ex) {
            throw new SocketRuntimeException(ex);
        }
    }

    /**
     * 断线重连
     */
    private boolean reConnect() {
        try {
            if (!reConnecting.get()) {
                reConnecting.set(true);

                // 重连三次当前server
                for (int i = 1; i <= 3; i++) {
                    try {
                        ChannelFuture future = this.doConnect(this.server, true);
                        future.await();
                        if (future.isSuccess() && future.cause() == null) {
                            log.info("尝试第 {} 次重连到 '{}' 成功!", i, this.server);
                            return true;
                        } else {
                            throw new SocketRuntimeException(future.cause());
                        }
                    } catch (Exception ex) {
                        log.error("尝试第 {} 次重连到 '{}' 失败!", i, this.server, ex);
                    }
                }
                // 只要成功就不会走到这一步
                return false;
            } else {
                return false;
            }
        } finally {
            reConnecting.set(false);
        }
    }

    /**
     * 关闭
     */
    @Override
    public void shutdown() {
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    /**
     * 发送数据
     *
     * @param message 消息内容
     * @return ChannelFuture
     */
    public ChannelFuture send(String message) {
        return this.send(Unpooled.wrappedBuffer(message.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 发送数据
     *
     * @param message 消息内容
     * @return ChannelFuture
     */
    public ChannelFuture send(byte[] message) {
        return this.send(Unpooled.wrappedBuffer(message));
    }

    /**
     * 发送数据
     *
     * @param message 消息内容
     * @return ChannelFuture
     */
    public ChannelFuture send(ByteBuf message) {
        if (channel == null || !channel.isActive()) {
            this.reConnect();
        }
        return channel.writeAndFlush(message);
    }

//    public Response sendWithSync(Request message) {
//        if (channel == null) {
//            throw new SocketRuntimeException("channel have not connect.");
//        }
//        return channel.sendSync(message, syncInvokeTimeout);
//    }
//
//    public Response sendWithSync(Request message, int timeout) {
//        if (channel == null) {
//            throw new SocketRuntimeException("channel have not connect.");
//        }
//        return channel.sendSync(message, timeout);
//    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSyncInvokeTimeout() {
        return syncInvokeTimeout;
    }

    public void setSyncInvokeTimeout(int syncInvokeTimeout) {
        this.syncInvokeTimeout = syncInvokeTimeout;
    }

    public SocketAddress getServer() {
        return server;
    }

    public void setServer(SocketAddress server) {
        this.server = server;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
