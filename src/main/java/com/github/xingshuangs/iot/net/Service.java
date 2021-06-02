package com.github.xingshuangs.iot.net;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateHandler;
import javafx.event.EventDispatcher;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.*;


public abstract class Service {
    /**
     * socket类型
     */
    protected SocketType socketType = SocketType.NORMAL;

    /**
     * 绑定端口，默认为8000
     */
    protected int port = 8000;

    /**
     * 多IP情况下绑定指定的IP(可以不设置)
     */
    protected String ip;

    /**
     * 是否启用keepAlive
     */
    protected boolean keepAlive = true;

    /**
     * 是否启用tcpNoDelay
     */
    protected boolean tcpNoDelay = true;

    /**
     * 设置是否心跳检查
     */
    protected boolean checkHeartbeat = true;

    /**
     * 心跳检查时的读空闲时间
     */
    protected int readerIdleTimeSeconds = 0;

    /**
     * 心跳检查时的写空闲时间
     */
    protected int writerIdleTimeSeconds = 0;

    /**
     * 心跳检查时的读写空闲时间
     */
    protected int allIdleTimeSeconds = 90;

    /**
     * 超时的处理对象
     */
    protected IdleStateHandler timeoutHandler;

    /**
     * 心跳的处理对象
     */
    protected ChannelInboundHandlerAdapter heartbeatHandler;

    /**
     * 自定义的处理对象
     */
    protected LinkedHashMap<String, ChannelHandler> handlers = new LinkedHashMap<>();

    protected List<EventListener> eventListeners = new ArrayList<>();

    protected EventDispatcher eventDispatcher;

    public Service() {
        //添加JVM关闭时的勾子
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
    }

    protected void init() {
    }

    /**
     * shutdown
     */
    public abstract void shutdown();

    public void addEventListener(EventListener listener) {
        this.eventListeners.add(listener);
    }

    public void addChannelHandler(String key, ChannelHandler handler) {
        this.handlers.put(key, handler);
    }

    public LinkedHashMap<String, ChannelHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(LinkedHashMap<String, ChannelHandler> handlers) {
        this.handlers = handlers;
    }

    public List<EventListener> getEventListeners() {
        return eventListeners;
    }

    public void setListeners(List<EventListener> listeners) {
        if (listeners == null) {
            listeners = new ArrayList<EventListener>();
        }
        eventListeners = listeners;
    }

    public SocketType getSocketType() {
        return socketType;
    }

    public void setSocketType(SocketType socketType) {
        this.socketType = socketType;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public boolean isCheckHeartbeat() {
        return checkHeartbeat;
    }

    public void setCheckHeartbeat(boolean checkHeartbeat) {
        this.checkHeartbeat = checkHeartbeat;
    }

    public int getReaderIdleTimeSeconds() {
        return readerIdleTimeSeconds;
    }

    public void setReaderIdleTimeSeconds(int readerIdleTimeSeconds) {
        this.readerIdleTimeSeconds = readerIdleTimeSeconds;
    }

    public int getWriterIdleTimeSeconds() {
        return writerIdleTimeSeconds;
    }

    public void setWriterIdleTimeSeconds(int writerIdleTimeSeconds) {
        this.writerIdleTimeSeconds = writerIdleTimeSeconds;
    }

    public int getAllIdleTimeSeconds() {
        return allIdleTimeSeconds;
    }

    public void setAllIdleTimeSeconds(int allIdleTimeSeconds) {
        this.allIdleTimeSeconds = allIdleTimeSeconds;
    }

    /**
     * 关闭的钩子
     */
    class ShutdownHook extends Thread {
        private Service service;

        ShutdownHook(Service service) {
            this.service = service;
        }

        @Override
        public void run() {
            service.shutdown();
        }
    }
}
