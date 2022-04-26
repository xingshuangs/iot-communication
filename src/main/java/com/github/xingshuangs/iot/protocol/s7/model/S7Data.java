package com.github.xingshuangs.iot.protocol.s7.model;


import lombok.Data;

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
    public int getByteArrayLength() {
        int length = 0;
        length += this.tpkt != null ? this.tpkt.getByteArrayLength() : 0;
        length += this.cotp != null ? this.cotp.getByteArrayLength() : 0;
        length += this.header != null ? this.header.getByteArrayLength() : 0;
        length += this.parameter != null ? this.parameter.getByteArrayLength() : 0;
        length += this.datum != null ? this.datum.getByteArrayLength() : 0;
        return length;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[this.getByteArrayLength()];
        int count = 0;
        if (this.tpkt != null) {
            byte[] tpktBytes = this.tpkt.toByteArray();
            System.arraycopy(tpktBytes, 0, res, count, tpktBytes.length);
            count += tpktBytes.length;
        }
        if(this.cotp!=null){
            byte[] cotpBytes = this.cotp.toByteArray();
            System.arraycopy(cotpBytes, 0, res, count, cotpBytes.length);
            count += cotpBytes.length;
        }
        if(this.header!=null){
            byte[] headerBytes = this.header.toByteArray();
            System.arraycopy(headerBytes, 0, res, count, headerBytes.length);
            count += headerBytes.length;
        }
        if(this.parameter!=null){
            byte[] parameterBytes = this.parameter.toByteArray();
            System.arraycopy(parameterBytes, 0, res, count, parameterBytes.length);
            count += parameterBytes.length;
        }
        if(this.datum!=null){
            byte[] datumBytes = this.datum.toByteArray();
            System.arraycopy(datumBytes, 0, res, count, datumBytes.length);
        }
        return res;
    }
}
