package com.github.xingshuangs.iot.protocol.melsec.service;


import com.github.xingshuangs.iot.exceptions.McCommException;
import com.github.xingshuangs.iot.net.client.TcpClientBasic;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSubHeader;
import com.github.xingshuangs.iot.protocol.melsec.model.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * plc的网络通信
 *
 * @author xingshuang
 */
@Slf4j
@Data
public class McNetwork extends TcpClientBasic {

    /**
     * 锁
     */
    private final Object objLock = new Object();

    /**
     * 通信回调
     */
    private Consumer<byte[]> comCallback;

    /**
     * 是否持久化，默认是持久化，对应长连接，true：长连接，false：短连接
     */
    private boolean persistence = true;

    /**
     * 访问路径，默认4E，3E帧访问路径
     */
    private McAccessRoute accessRoute = Mc4E3EFrameAccessRoute.createDefault();

    /**
     * 监视定时器，默认：3000ms，设置读取及写入的处理完成之前的等待时间。设置连接站E71向访问目标发出处理请求之后到返回响应为止的等待时间。
     */
    private int monitoringTimer = 3000;

    public McNetwork() {
        super();
    }

    public McNetwork(String host, int port) {
        super(host, port);
    }

    @Override
    public void connect() {
        try {
            super.connect();
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    //region 底层数据通信部分

    /**
     * 从服务器读取数据
     *
     * @param req modbus协议数据
     * @return modbus协议数据
     */
    protected McMessageAck readFromServer(McMessageReq req) {
        if (this.comCallback != null) {
            this.comCallback.accept(req.toByteArray());
        }
        McHeader header;
        int len;
        byte[] total;
        synchronized (this.objLock) {
            this.write(req.toByteArray());

            byte[] data = new byte[9];
            len = this.read(data);
            if (len < 9) {
                throw new McCommException(" McHeader 无效，读取长度不一致");
            }
            header = McHeader.fromBytes(data);
            total = new byte[9 + header.getDataLength()];
            System.arraycopy(data, 0, total, 0, data.length);
            len = this.read(total, data.length, header.getDataLength());
        }
        if (len < header.getDataLength()) {
            throw new McCommException(" McHeader后面的数据长度，长度不一致");
        }
        if (this.comCallback != null) {
            this.comCallback.accept(total);
        }
        McMessageAck ack = McMessageAck.fromBytes(total);
        this.checkResult(req, ack);
        return ack;
    }

    /**
     * 校验请求数据和响应数据
     *
     * @param req 请求数据
     * @param ack 响应数据
     */
    protected void checkResult(McMessageReq req, McMessageAck ack) {
        if (req.getHeader().getSubHeader() == EMcSubHeader.REQ_4E.getCode()
                && ack.getHeader().getSubHeader() != EMcSubHeader.ACK_4E.getCode()) {
            throw new McCommException("响应副帧头和请求副帧头不一致，请求副帧头：" + req.getHeader().getSubHeader()
                    + "，响应副帧头：" + ack.getHeader().getEndCode());
        }
        if (req.getHeader().getSubHeader() == EMcSubHeader.REQ_3E.getCode()
                && ack.getHeader().getSubHeader() != EMcSubHeader.ACK_3E.getCode()) {
            throw new McCommException("响应副帧头和请求副帧头不一致，请求副帧头：" + req.getHeader().getSubHeader()
                    + "，响应副帧头：" + ack.getHeader().getEndCode());
        }
        if (ack.getHeader().getEndCode() != 0) {
            throw new McCommException("响应返回异常，异常码:" + ack.getHeader().getEndCode());
        }
    }

    //endregion


    public byte[] readDeviceBatchInWord(McDeviceAddress deviceAddress) {
        McMessageReq req = McReqBuilder.createReadDeviceBatchInWordReq(this.accessRoute, this.monitoringTimer, deviceAddress);
        McMessageAck ack = this.readFromServer(req);
        McAckData ackData = (McAckData) ack.getData();
        return ackData.getData();
    }

    public void writeDeviceBatchInWord(McDeviceContent deviceContent) {
        McMessageReq req = McReqBuilder.createWriteDeviceBatchInWordReq(this.accessRoute, this.monitoringTimer, deviceContent);
        this.readFromServer(req);
    }

    public byte[] readDeviceBatchInBit(McDeviceAddress deviceAddress) {
        McMessageReq req = McReqBuilder.createReadDeviceBatchInBitReq(this.accessRoute, this.monitoringTimer, deviceAddress);
        McMessageAck ack = this.readFromServer(req);
        McAckData ackData = (McAckData) ack.getData();
        return ackData.getData();
    }

    public void writeDeviceBatchInBit(McDeviceContent deviceContent) {
        McMessageReq req = McReqBuilder.createWriteDeviceBatchInBitReq(this.accessRoute, this.monitoringTimer, deviceContent);
        this.readFromServer(req);
    }
}
