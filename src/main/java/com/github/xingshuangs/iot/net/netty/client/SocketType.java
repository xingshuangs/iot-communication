package com.github.xingshuangs.iot.net.netty.client;

/**
 * Socket类型
 *
 * @author ShuangPC
 */
public enum SocketType {
    /**
     * 普通socket
     */
    NORMAL,
    /**
     * MQTT
     */
    MQTT,

    /**
     * MQTT web socket
     */
    MQTT_WS
}
