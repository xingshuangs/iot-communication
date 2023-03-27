package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.exceptions.ModbusCommException;
import com.github.xingshuangs.iot.net.client.TcpClientBasic;
import com.github.xingshuangs.iot.protocol.modbus.model.*;

import java.util.function.Consumer;

/**
 * plc的网络通信
 *
 * @author xingshuang
 */
public class ModbusNetwork extends TcpClientBasic {

    /**
     * 从站编号
     */
    private int unitId = 0;

    /**
     * 锁
     */
    private final Object objLock = new Object();

    /**
     * 通信回调
     */
    private Consumer<byte[]> comCallback;

    public void setComCallback(Consumer<byte[]> comCallback) {
        this.comCallback = comCallback;
    }

    public ModbusNetwork() {
        super();
    }

    public ModbusNetwork(int unitId, String host, int port) {
        super(host, port);
        this.unitId = unitId;
    }

    //region 底层数据通信部分

    /**
     * 从服务器读取数据
     *
     * @param req modbus协议数据
     * @return modbus协议数据
     */
    protected MbTcpResponse readFromServer(MbTcpRequest req) {
        if (this.comCallback != null) {
            this.comCallback.accept(req.toByteArray());
        }
        MbapHeader header;
        int len;
        byte[] remain;
        synchronized (this.objLock) {
            this.write(req.toByteArray());

            byte[] data = new byte[MbapHeader.BYTE_LENGTH];
            len = this.read(data);
            if (len < MbapHeader.BYTE_LENGTH) {
                throw new ModbusCommException(" MbapHeader 无效，读取长度不一致");
            }
            header = MbapHeader.fromBytes(data);
            remain = new byte[header.getLength() - 1];
            len = this.read(remain);
        }
        if (len < remain.length) {
            throw new ModbusCommException(" MbapHeader后面的数据长度，长度不一致");
        }
        MbTcpResponse ack = MbTcpResponse.fromBytes(header, remain);
        if (this.comCallback != null) {
            this.comCallback.accept(ack.toByteArray());
        }
        this.checkResult(req, ack);
        return ack;
    }

    /**
     * 校验请求数据和响应数据
     *
     * @param req 请求数据
     * @param ack 响应数据
     */
    private void checkResult(MbTcpRequest req, MbTcpResponse ack) {
        if (ack.getPdu() == null) {
            throw new ModbusCommException("PDU数据为null");
        }
        if (req.getHeader().getTransactionId() != ack.getHeader().getTransactionId()) {
            throw new ModbusCommException("事务元标识符Id不一致");
        }
        if (ack.getPdu().getFunctionCode().getCode() == (req.getPdu().getFunctionCode().getCode() | (byte) 0x80)) {
            MbErrorResponse response = (MbErrorResponse) ack.getPdu();
            throw new ModbusCommException("响应返回异常，异常码:" + response.getErrorCode().getDescription());
        }
        if (ack.getPdu().getFunctionCode().getCode() != req.getPdu().getFunctionCode().getCode()) {
            MbErrorResponse response = (MbErrorResponse) ack.getPdu();
            throw new ModbusCommException("返回功能码和发送功能码不一致，异常码:" + response.getErrorCode().getDescription());
        }
    }

    //endregion

    /**
     * 读取modbus数据
     *
     * @param reqPdu 请求对象
     * @return 响应结果
     */
    protected MbPdu readModbusData(MbPdu reqPdu) {
        MbTcpRequest request = MbTcpRequest.createDefault();
        request.getHeader().setUnitId(this.unitId);
        request.setPdu(reqPdu);
        request.selfCheck();
        MbTcpResponse response = this.readFromServer(request);
        return response.getPdu();
    }
}
