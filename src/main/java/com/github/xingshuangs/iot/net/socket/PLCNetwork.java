package com.github.xingshuangs.iot.net.socket;


/**
 * plc的网络通信
 *
 * @author xingshuang
 */
public class PLCNetwork extends SocketBasic {

    public PLCNetwork() {
        super();
    }

    public PLCNetwork(String host, int port) {
        super(host, port);
    }
}
