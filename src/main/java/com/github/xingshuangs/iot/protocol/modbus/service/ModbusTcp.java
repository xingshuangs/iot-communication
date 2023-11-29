/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
public class ModbusTcp extends ModbusNetwork<MbTcpRequest, MbTcpResponse> {

    public ModbusTcp() {
        this(1, IP, PORT);
    }

    public ModbusTcp(String ip) {
        this(1, ip, PORT);
    }

    public ModbusTcp(String ip, int port) {
        this(1, ip, port);
    }

    public ModbusTcp(int unitId) {
        this(unitId, IP, PORT);
    }

    public ModbusTcp(int unitId, String ip) {
        this(unitId, ip, PORT);
    }

    public ModbusTcp(int unitId, String ip, int port) {
        super(unitId, ip, port);
        this.tag = "ModbusTcp";
    }

    //region 底层数据通信部分

    /**
     * 从服务器读取数据
     *
     * @param req modbus协议数据
     * @return modbus协议数据
     */
    @Override
    protected MbTcpResponse readFromServer(MbTcpRequest req) {
        if (this.comCallback != null) {
            this.comCallback.accept(req.toByteArray());
        }
        MbapHeader header;
        int len;
        byte[] total;
        synchronized (this.objLock) {
            this.write(req.toByteArray());

            byte[] data = new byte[MbapHeader.BYTE_LENGTH];
            len = this.read(data);
            if (len < MbapHeader.BYTE_LENGTH) {
                throw new ModbusCommException(" MbapHeader 无效，读取长度不一致");
            }
            header = MbapHeader.fromBytes(data);
            total = new byte[data.length + header.getLength() - 1];
            System.arraycopy(data, 0, total, 0, data.length);
            len = this.read(total, data.length, header.getLength() - 1);
        }
        if (len < header.getLength() - 1) {
            throw new ModbusCommException(" MbapHeader后面的数据长度，长度不一致");
        }
        if (this.comCallback != null) {
            this.comCallback.accept(total);
        }
        MbTcpResponse ack = MbTcpResponse.fromBytes(total);
        this.checkResult(req, ack);
        return ack;
    }

    /**
     * 校验请求数据和响应数据
     *
     * @param req 请求数据
     * @param ack 响应数据
     */
    @Override
    protected void checkResult(MbTcpRequest req, MbTcpResponse ack) {
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
    @Override
    protected MbPdu readModbusData(int unitId, MbPdu reqPdu) {
        MbTcpRequest request = MbTcpRequest.createDefault();
        request.getHeader().setUnitId(unitId);
        request.setPdu(reqPdu);
        request.selfCheck();
        try {
            MbTcpResponse response = this.readFromServer(request);
            return response.getPdu();
        } finally {
            if (!this.persistence) {
                log.debug("由于短连接方式，通信完毕触发关闭连接通道，服务端IP[{}]", this.socketAddress);
                this.close();
            }
        }
    }
}
