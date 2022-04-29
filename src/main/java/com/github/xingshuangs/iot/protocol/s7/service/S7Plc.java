package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.net.socket.PLCNetwork;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.model.S7Data;
import com.github.xingshuangs.iot.protocol.s7.model.TPKT;

import java.io.IOException;

/**
 * @author xingshuang
 */
public class S7Plc extends PLCNetwork {

    public static final int PORT = 102;

    private EPlcType plcType = EPlcType.S1200;

    public S7Plc(EPlcType plcType) {
        this(plcType, "127.0.0.1", PORT);
    }

    public S7Plc(EPlcType plcType, String ip) {
        this(plcType, ip, PORT);
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
        int len = this.read(data);
        if (len < TPKT.BYTE_LENGTH) {
            throw new S7CommException(" TPKT 无效，长度不一致");
        }
        TPKT tpkt = TPKT.fromBytes(data);
        byte[] remain = new byte[tpkt.getLength() - TPKT.BYTE_LENGTH];
        len = this.read(remain);
        if (len < remain.length) {
            throw new S7CommException(" TPKT后面的数据长度，长度不一致");
        }
        return S7Data.fromBytes(tpkt, remain);
    }
}
