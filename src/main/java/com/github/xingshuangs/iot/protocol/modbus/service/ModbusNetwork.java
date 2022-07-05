package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.net.socket.SocketBasic;
import com.github.xingshuangs.iot.protocol.s7.model.S7Data;
import com.github.xingshuangs.iot.protocol.s7.model.TPKT;

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

    /**
     * 最大的PDU长度
     */
    private int maxPduLength = 0;

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
     * @param req S7协议数据
     * @return S7协议数据
     */
    protected S7Data readFromServer(S7Data req) {
        byte[] sendData = req.toByteArray();
        if (this.maxPduLength > 0 && sendData.length > this.maxPduLength) {
            throw new S7CommException("发送请求的字节数过长，已经大于最大的PDU长度");
        }
        TPKT tpkt;
        int len;
        byte[] remain;
        synchronized (this.objLock) {
            this.writeWrapper(sendData);

            byte[] data = new byte[TPKT.BYTE_LENGTH];
            len = this.readWrapper(data);
            if (len < TPKT.BYTE_LENGTH) {
                throw new S7CommException(" TPKT 无效，长度不一致");
            }
            tpkt = TPKT.fromBytes(data);
            remain = new byte[tpkt.getLength() - TPKT.BYTE_LENGTH];
            len = this.readWrapper(remain);
        }
        if (len < remain.length) {
            throw new S7CommException(" TPKT后面的数据长度，长度不一致");
        }
        S7Data ack = S7Data.fromBytes(tpkt, remain);
//        this.checkResult(req, ack);
        return ack;
    }


    /**
     * 读取字节
     *
     * @param data 字节数组
     * @return 读取数量
     */
    private int readWrapper(final byte[] data) {
        int offset = 0;
        while (offset < data.length) {
            int length = this.maxPduLength <= 0 ? data.length - offset : Math.min(this.maxPduLength, data.length - offset);
            this.read(data, offset, length);
            offset += length;
        }
        return offset;
    }

    /**
     * 写入字节
     *
     * @param data 字节数组
     */
    private void writeWrapper(final byte[] data) {
        int offset = 0;
        while (offset < data.length) {
            int length = this.maxPduLength <= 0 ? data.length - offset : Math.min(this.maxPduLength, data.length - offset);
            this.write(data, offset, length);
            offset += length;
        }
    }
    //endregion
}
