package com.github.xingshuangs.iot.protocol.s7.model;


import lombok.Data;

import java.util.Arrays;

/**
 * S7数据结构
 *
 * @author xingshuang
 */
@Data
public class S7Data implements IByteArray {

    /**
     * TPKT
     */
    private TPKT tpkt;

    /**
     * COTP
     */
    private COTP cotp;

    /**
     * 头
     */
    private Header header;

    /**
     * 参数
     */
    private Parameter parameter;

    /**
     * 数据
     */
    private Datum datum;

    @Override
    public int byteArrayLength() {
        int length = 0;
        length += this.tpkt != null ? this.tpkt.byteArrayLength() : 0;
        length += this.cotp != null ? this.cotp.byteArrayLength() : 0;
        length += this.header != null ? this.header.byteArrayLength() : 0;
        length += this.parameter != null ? this.parameter.byteArrayLength() : 0;
        length += this.datum != null ? this.datum.byteArrayLength() : 0;
        return length;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[this.byteArrayLength()];
        int count = 0;
        if (this.tpkt != null) {
            byte[] tpktBytes = this.tpkt.toByteArray();
            System.arraycopy(tpktBytes, 0, res, count, tpktBytes.length);
            count += tpktBytes.length;
        }
        if (this.cotp != null) {
            byte[] cotpBytes = this.cotp.toByteArray();
            System.arraycopy(cotpBytes, 0, res, count, cotpBytes.length);
            count += cotpBytes.length;
        }
        if (this.header != null) {
            byte[] headerBytes = this.header.toByteArray();
            System.arraycopy(headerBytes, 0, res, count, headerBytes.length);
            count += headerBytes.length;
        }
        if (this.parameter != null) {
            byte[] parameterBytes = this.parameter.toByteArray();
            System.arraycopy(parameterBytes, 0, res, count, parameterBytes.length);
            count += parameterBytes.length;
        }
        if (this.datum != null) {
            byte[] datumBytes = this.datum.toByteArray();
            System.arraycopy(datumBytes, 0, res, count, datumBytes.length);
        }
        return res;
    }

    /**
     * 自我数据校验
     */
    public void selfCheck() {
        if (this.header != null) {
            this.header.setDataLength(0);
            this.header.setParameterLength(0);
        }
        if (this.parameter != null && this.header != null) {
            if (this.parameter instanceof ReadWriteParameter) {
                ReadWriteParameter p = (ReadWriteParameter) this.parameter;
                p.setItemCount(p.getRequestItems().size());
            }
            this.header.setParameterLength(this.parameter.byteArrayLength());
        }
        if (this.datum != null && this.header != null) {
            this.header.setDataLength(this.datum.byteArrayLength());
        }
        if (this.tpkt != null) {
            this.tpkt.setLength(this.byteArrayLength());
        }
    }

    /**
     * 根据字节数据解析S7协议数据
     *
     * @param data 数据字节
     * @return s7数据
     */
    public static S7Data fromBytes(final byte[] data) {
        byte[] tpktBytes = Arrays.copyOfRange(data, 0, TPKT.BYTE_LENGTH);
        TPKT tpkt = TPKT.fromBytes(tpktBytes);
        byte[] remainBytes = Arrays.copyOfRange(data, TPKT.BYTE_LENGTH, data.length);
        return fromBytes(tpkt, remainBytes);
    }

    /**
     * 根据字节数据解析S7协议数据
     *
     * @param tpkt   tpkt
     * @param remain 剩余字节数据
     * @return s7数据
     */
    public static S7Data fromBytes(TPKT tpkt, final byte[] remain) {
        // tpkt
        S7Data s7Data = new S7Data();
        s7Data.tpkt = tpkt;
        // cotp
        COTP cotp = COTPBuilder.fromBytes(remain);
        s7Data.cotp = cotp;
        if (cotp == null || remain.length <= cotp.byteArrayLength()) {
            return s7Data;
        }

        //-----------------------------S7通信部分的内容--------------------------------------------
        byte[] lastBytes = Arrays.copyOfRange(remain, cotp.byteArrayLength(), remain.length);
        // header
        Header header = HeaderBuilder.fromBytes(lastBytes);
        s7Data.header = header;
        if (header == null) {
            return s7Data;
        }
        // parameter
        if (header.getParameterLength() > 0) {
            byte[] parameterBytes = Arrays.copyOfRange(lastBytes, header.byteArrayLength(), header.byteArrayLength() + header.getParameterLength());
            s7Data.parameter = ParameterBuilder.fromBytes(parameterBytes, s7Data.header.getMessageType());
        }
        // datum
        if (header.getDataLength() > 0) {
            byte[] dataBytes = Arrays.copyOfRange(lastBytes, header.byteArrayLength() + header.getParameterLength(), header.byteArrayLength() + header.getParameterLength() + header.getDataLength());
            s7Data.datum = Datum.fromBytes(dataBytes, s7Data.header.getMessageType(), s7Data.parameter.getFunctionCode());
        }
        return s7Data;
    }

    /**
     * 创建连接请求
     *
     * @param local  本地参数
     * @param remote 远程参数
     * @return s7data数据
     */
    public static S7Data createConnectRequest(int local, int remote) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPConnection.crConnectRequest(local, remote);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * 创建连接setup
     *
     * @return s7data数据
     */
    public static S7Data createConnectDtData() {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = SetupComParameter.createDefault();
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * 创建默认读对象
     *
     * @return S7Data
     */
    public static S7Data createReadDefault() {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = ReadWriteParameter.createReadDefault();
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * 创建默认写对象
     *
     * @return S7Data
     */
    public static S7Data createWriteDefault() {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = ReadWriteParameter.createWriteDefault();
        s7Data.datum = new Datum();
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * 创建热启动
     *
     * @return S7Data
     */
    public static S7Data createHotRestart() {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = StartParameter.hotRestart();
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * 创建冷启动
     *
     * @return S7Data
     */
    public static S7Data createColdRestart() {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = StartParameter.coldRestart();
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * 创建PLC停止
     *
     * @return S7Data
     */
    public static S7Data createPlcStop() {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = StopParameter.createDefault();
        s7Data.selfCheck();
        return s7Data;
    }
}
