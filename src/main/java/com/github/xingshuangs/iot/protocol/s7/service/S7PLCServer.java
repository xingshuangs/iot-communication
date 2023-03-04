package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.net.server.ServerSocketBasic;
import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import com.github.xingshuangs.iot.protocol.s7.model.COTPConnection;
import com.github.xingshuangs.iot.protocol.s7.model.COTPData;
import com.github.xingshuangs.iot.protocol.s7.model.S7Data;
import lombok.extern.slf4j.Slf4j;

import java.net.Socket;

/**
 * S7的PLC服务端
 *
 * @author xingshuang
 */
@Slf4j
public class S7PLCServer extends ServerSocketBasic {

    private S7Data readS7DataFromClient(Socket socket) {
        byte[] data = this.readClientData(socket);
        return S7Data.fromBytes(data);
    }

    @Override
    protected boolean checkHandshake(Socket socket) {
        // 校验connect request
        S7Data s7Data = this.readS7DataFromClient(socket);
        if (!(s7Data.getCotp() instanceof COTPConnection)
                || s7Data.getCotp().getPduType() != EPduType.CONNECT_REQUEST) {
            log.error("握手失败，不是连接请求");
            return false;
        }
        S7Data connectConfirm = S7Data.createConnectConfirm(s7Data);
        this.write(socket, connectConfirm.toByteArray());

        // 校验setup
        s7Data = this.readS7DataFromClient(socket);
        if (!(s7Data.getCotp() instanceof COTPData)) {
            log.error("握手失败，不是参数设置");
            return false;
        }
        S7Data connectAckDtData = S7Data.createConnectAckDtData(s7Data);
        this.write(socket, connectAckDtData.toByteArray());
        return true;
    }

    @Override
    protected void doClientHandle(Socket socket) {
        S7Data s7Data = this.readS7DataFromClient(socket);
    }
}
