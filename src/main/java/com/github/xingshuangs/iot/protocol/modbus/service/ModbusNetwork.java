package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.exceptions.ModbusCommException;
import com.github.xingshuangs.iot.net.socket.SocketBasic;
import com.github.xingshuangs.iot.protocol.modbus.model.MbErrorResponse;
import com.github.xingshuangs.iot.protocol.modbus.model.MbTcpRequest;
import com.github.xingshuangs.iot.protocol.modbus.model.MbTcpResponse;
import com.github.xingshuangs.iot.protocol.modbus.model.MbapHeader;

/**
 * plc的网络通信
 *
 * @author xingshuang
 */
public class ModbusNetwork extends SocketBasic {

    /**
     * 锁
     */
    private final Object objLock = new Object();

    /**
     * PLC机架号
     */
    protected int rack = 0;

    /**
     * PLC槽号
     */
    protected int slot = 0;

    public ModbusNetwork() {
        super();
    }

    public ModbusNetwork(String host, int port) {
        super(host, port);
    }

    //region 底层数据通信部分

    /**
     * 从服务器读取数据
     *
     * @param req modbus协议数据
     * @return modbus协议数据
     */
    protected MbTcpResponse readFromServer(MbTcpRequest req) {
        byte[] sendData = req.toByteArray();
        MbapHeader header;
        int len;
        byte[] remain;
        synchronized (this.objLock) {
            this.writeCycle(sendData);

            byte[] data = new byte[MbapHeader.BYTE_LENGTH];
            len = this.readCycle(data);
            if (len < MbapHeader.BYTE_LENGTH) {
                throw new ModbusCommException(" MbapHeader 无效，读取长度不一致");
            }
            header = MbapHeader.fromBytes(data);
            remain = new byte[header.getLength() - 1];
            len = this.readCycle(remain);
        }
        if (len < remain.length) {
            throw new ModbusCommException(" MbapHeader后面的数据长度，长度不一致");
        }
        MbTcpResponse ack = MbTcpResponse.fromBytes(header, remain);
        this.checkResult(req, ack);
        return ack;
    }

    private void checkResult(MbTcpRequest req, MbTcpResponse ack) {
        if (ack.getPdu() == null) {
            throw new ModbusCommException("PDU数据为null");
        }
        if (req.getHeader().getTransactionId() != ack.getHeader().getTransactionId()) {
            throw new ModbusCommException("事务元标识符Id不一致");
        }
        if (ack.getPdu().getFunctionCode().getCode() == req.getPdu().getFunctionCode().getCode() + 0x80) {
            MbErrorResponse response = (MbErrorResponse) ack.getPdu();
            throw new ModbusCommException("响应返回异常，异常码:" + response.getErrorCode());
        }
    }
    //endregion
}
