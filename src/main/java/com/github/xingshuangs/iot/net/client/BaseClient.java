package com.github.xingshuangs.iot.net.client;

import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import com.github.xingshuangs.iot.net.Service;
import com.github.xingshuangs.iot.net.SocketType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 16:23
 */
public class BaseClient extends Service {
    private static final Logger logger = LoggerFactory.getLogger(BaseClient.class);

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
     * 一个线程同步的辅助类，可以维护当前访问自身的线程个数，并提供了同步机制
     */
    protected Semaphore semaphore = new Semaphore(0);

    /**
     * netty的EventLoopGroup
     */
    protected EventLoopGroup group;

    /**
     * netty的启动配置对象
     */
    protected Bootstrap bootstrap;

    public BaseClient() {
        super();
    }

    @Override
    protected void init() {
        super.init();
        // 将一些handler放在这里初始化是为了防止多例的产生。
        if (checkHeartbeat) {
            timeoutHandler = new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
            heartbeatHandler = new ClientHeartbeatHandler();
        }

        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
    }

    /**
     * 执行连接
     *
     * @param socketAddress 服务器地址
     * @param sync          是否同步
     * @return ChannelFuture
     */
    protected ChannelFuture doConnect(final SocketAddress socketAddress, boolean sync) {
        // 连接server
        server = socketAddress;
        try {
            ChannelFuture future = bootstrap.connect(socketAddress).sync();
            future.addListener((ChannelFutureListener) ch -> {
                ch.await();
                if (ch.isSuccess()) {
                    channel = ch.channel();
                    if (logger.isDebugEnabled()) {
                        logger.debug("Connect to '{}' success.", socketAddress);
                    }
                } else {
                    logger.error("Failed to connect to '{}', caused by: '{}'.", socketAddress, ch.cause());
                }

                semaphore.release(Integer.MAX_VALUE - semaphore.availablePermits());
            });
            future.channel().closeFuture();

            if (sync) {
                Throwable cause = null;
                try {
                    if (!semaphore.tryAcquire(connectTimeout, TimeUnit.MILLISECONDS)) {
                        cause = new SocketTimeoutException("time out.");
                    }
                } catch (InterruptedException ex) {
                    throw new SocketTimeoutException(ex.getMessage());
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
     * 同步连接
     *
     * @param socketAddress 服务器地址
     * @return ChannelFuture
     */
    public ChannelFuture connect(final SocketAddress socketAddress) {
        return connect(socketAddress, true);
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
        bootstrap.option(ChannelOption.SO_KEEPALIVE, keepAlive);
        bootstrap.option(ChannelOption.TCP_NODELAY, tcpNoDelay);
        bootstrap.group(group).channel(NioSocketChannel.class);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();

                // 注册各种自定义Handler
                LinkedHashMap<String, ChannelHandler> handlers = getHandlers();
                for (String key : handlers.keySet()) {
                    pipeline.addLast(key, handlers.get(key));
                }

                if (socketType.equals(SocketType.MQTT_WS)) {
                    pipeline.addFirst("httpRequestDecoder", new HttpRequestDecoder());
                    pipeline.addFirst("httpObjectAggregator", new HttpObjectAggregator(65536));
                    pipeline.addFirst("httpResponseEncoder", new HttpResponseEncoder());
                }

                if (checkHeartbeat) {
                    pipeline.addLast("timeout", timeoutHandler);
                    pipeline.addLast("idleHandler", heartbeatHandler);
                }
            }
        });

        return doConnect(socketAddress, sync);
    }

    @Override
    public void shutdown() {
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    public ChannelFuture send(Object message) {
        if (channel == null) {
            throw new SocketRuntimeException("channel have not connect.");
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
