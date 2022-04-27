package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.net.socket.PLCNetwork;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.model.S7Data;
import com.github.xingshuangs.iot.protocol.s7.model.TPKT;

import java.io.IOException;

/**
 * @author xingshuang
 */
public class S7Plc extends PLCNetwork {

    private EPlcType plcType = EPlcType.S1200;

    public S7Plc(EPlcType plcType) {
        this(plcType, "127.0.0.1", 102);
    }

    public S7Plc(EPlcType plcType, String ip) {
        this(plcType, ip, 102);
    }

    public S7Plc(EPlcType plcType, String ip, int port) {
        super(ip, port);
        this.plcType = plcType;
    }

    /**
     * 连接成功之后要做的动作
     */
    @Override
    protected void doAfterConnected() {

    }

    private S7Data readFromServer(S7Data s7Data) throws IOException {
        this.write(s7Data.toByteArray());

        byte[] data = new byte[TPKT.BYTE_LENGTH];
        this.read(data);
        TPKT tpkt = TPKT.fromBytes(data);
        byte[] remain = new byte[tpkt.getLength() - TPKT.BYTE_LENGTH];
        this.read(remain);


        return null;
    }
}
