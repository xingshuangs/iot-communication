package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.exceptions.ModbusCommException;
import com.github.xingshuangs.iot.protocol.modbus.model.*;
import lombok.extern.slf4j.Slf4j;

/**
 * modbus 1个寄存器占2个字节
 *
 * @author xingshuang
 */
@Slf4j
public class ModbusRtuOverTcp extends ModbusNetwork<MbRtuRequest, MbRtuResponse> {

    public ModbusRtuOverTcp() {
        this(0, IP, PORT);
    }

    public ModbusRtuOverTcp(int unitId) {
        this(unitId, IP, PORT);
    }

    public ModbusRtuOverTcp(int unitId, String ip) {
        this(unitId, ip, PORT);
    }

    public ModbusRtuOverTcp(int unitId, String ip, int port) {
        super(unitId, ip, port);
        this.tag = "ModbusRtu";
    }

    @Override
    protected MbRtuResponse readFromServer(MbRtuRequest req) {
        byte[] reqBytes = req.toByteArray();
        if (this.comCallback != null) {
            this.comCallback.accept(reqBytes);
        }
        int len;
        byte[] data = new byte[1024];
        synchronized (this.objLock) {
            this.write(reqBytes);
            len = this.read(data);
        }
        if (len <= 0) {
            throw new ModbusCommException(" Modbus数据读取长度有误");
        }
        byte[] total = new byte[len];
        System.arraycopy(data, 0, total, 0, len);
        if (this.comCallback != null) {
            this.comCallback.accept(total);
        }
        MbRtuResponse ack = MbRtuResponse.fromBytes(total);
        this.checkResult(req, ack);
        return ack;
    }

    @Override
    protected void checkResult(MbRtuRequest req, MbRtuResponse ack) {
        if (!ack.checkCrc()) {
            throw new ModbusCommException("响应数据CRC校验失败");
        }
        if (ack.getPdu() == null) {
            throw new ModbusCommException("PDU数据为null");
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

    @Override
    protected MbPdu readModbusData(MbPdu reqPdu) {
        MbRtuRequest request = new MbRtuRequest(this.unitId, reqPdu);
        try {
            MbRtuResponse response = this.readFromServer(request);
            return response.getPdu();
        } finally {
            if (!this.persistence) {
                log.debug("由于短连接方式，通信完毕触发关闭连接通道，服务端IP[{}]", this.socketAddress);
                this.close();
            }
        }
    }


}
