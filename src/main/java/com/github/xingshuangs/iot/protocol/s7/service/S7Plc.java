package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.net.socket.PLCNetwork;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.model.COTP;
import com.github.xingshuangs.iot.protocol.s7.model.S7Data;
import com.github.xingshuangs.iot.protocol.s7.model.TPKT;

import java.io.IOException;
import java.util.Arrays;

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
        this.read(data);
        TPKT tpkt = TPKT.fromBytes(data);
        byte[] remain = new byte[tpkt.getLength() - TPKT.BYTE_LENGTH];
        this.read(remain);
        COTP cotp = COTP.fromBytes(remain);
        byte[] s7ComBytes = Arrays.copyOfRange(remain, cotp.byteArrayLength(), remain.length);
        if (s7ComBytes.length > 0) {

        }
        return null;
    }
}
