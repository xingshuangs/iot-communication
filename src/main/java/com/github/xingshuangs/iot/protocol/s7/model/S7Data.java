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

    private TPKT tpkt;

    private COTP cotp;

    private Header header;

    private Parameter parameter;

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

    public void selfCheck() {
        if (this.datum != null && this.header != null) {
            this.header.setDataLength(this.datum.byteArrayLength());
        }
        if (this.parameter != null && this.header != null) {
            this.header.setParameterLength(this.parameter.byteArrayLength());
        }
        if (this.tpkt != null) {
            this.tpkt.setLength(this.byteArrayLength());
        }
    }

    public static S7Data fromBytes(final byte[] data) {
        byte[] tpktBytes = Arrays.copyOfRange(data, 0, TPKT.BYTE_LENGTH);
        TPKT tpkt = TPKT.fromBytes(tpktBytes);
        byte[] remainBytes = Arrays.copyOfRange(data, TPKT.BYTE_LENGTH, data.length);
        return fromBytes(tpkt, remainBytes);
    }

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
//        byte[] headerBytes = Arrays.copyOfRange(lastBytes, 0, Header.BYTE_LENGTH);
        Header header = HeaderBuilder.fromBytes(lastBytes);
        s7Data.header = header;
        if (header == null) {
            return s7Data;
        }
        // parameter
        if (header.getParameterLength() > 0) {
            byte[] parameterBytes = Arrays.copyOfRange(lastBytes, header.byteArrayLength(), header.byteArrayLength() + header.getParameterLength());
            s7Data.parameter = ParameterBuilder.fromBytes(parameterBytes);
        }
        // datum
        if (header.getDataLength() > 0) {
            byte[] dataBytes = Arrays.copyOfRange(lastBytes, header.byteArrayLength(), header.byteArrayLength() + header.getParameterLength() + header.getDataLength());
            s7Data.datum = Datum.fromBytes(dataBytes);
        }
        return s7Data;
    }
}
