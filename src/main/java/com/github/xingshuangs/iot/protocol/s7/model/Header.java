package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;
import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Data;

/**
 * 头部
 *
 * @author xingshuang
 */
@Data
public class Header implements IByteArray {

    public static final int BYTE_LENGTH = 10;
    /**
     * 协议id <br>
     * 字节大小：1 <br>
     * 字节序数：0
     */
    protected byte protocolId = (byte) 0x32;

    /**
     * pdu（协议数据单元（Protocol Data Unit））的类型 <br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    protected EMessageType messageType = EMessageType.JOB;

    /**
     * 保留 <br>
     * 字节大小：2 <br>
     * 字节序数：2-3
     */
    protected int reserved = 0x0000;

    /**
     * pdu的参考–由主站生成，每次新传输递增，Little-Endian <br>
     * 字节大小：2 <br>
     * 字节序数：4-5
     */
    protected int pduReference = 0x0000;

    /**
     * 参数的长度（大端） <br>
     * 字节大小：2 <br>
     * 字节序数：6-7
     */
    protected int parameterLength = 0x0000;

    /**
     * 数据的长度（大端） <br>
     * 字节大小：2 <br>
     * 字节序数：8-9
     */
    protected int dataLength = 0x0000;

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[BYTE_LENGTH];
        byte[] reservedBytes = ShortUtil.toByteArray((short) this.reserved);
        byte[] pduReferenceBytes = ShortUtil.toByteArray((short) this.pduReference, true);
        byte[] parameterLengthBytes = ShortUtil.toByteArray((short) this.parameterLength);
        byte[] dataLengthBytes = ShortUtil.toByteArray((short) this.dataLength);

        res[0] = this.protocolId;
        res[1] = this.messageType.getCode();
        res[2] = reservedBytes[0];
        res[3] = reservedBytes[1];
        res[4] = pduReferenceBytes[0];
        res[5] = pduReferenceBytes[1];
        res[6] = parameterLengthBytes[0];
        res[7] = parameterLengthBytes[1];
        res[8] = dataLengthBytes[0];
        res[9] = dataLengthBytes[1];
        return res;
    }

    public static Header fromBytes(final byte[] data) {
        if (data.length < BYTE_LENGTH) {
            throw new IndexOutOfBoundsException("解析header时，字节数组长度不够");
        }
        Header header = new Header();
        header.protocolId = data[0];
        header.messageType = EMessageType.from(data[1]);
        header.reserved = ShortUtil.toUInt16(data, 2);
        header.pduReference = ShortUtil.toUInt16(data, 4, true);
        header.parameterLength = ShortUtil.toUInt16(data, 6);
        header.dataLength = ShortUtil.toUInt16(data, 8);
        return header;
    }
}
