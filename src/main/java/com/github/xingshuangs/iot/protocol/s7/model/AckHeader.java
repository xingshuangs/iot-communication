package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EErrorClass;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;
import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 响应头
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AckHeader extends Header {

    public static final int BYTE_LENGTH = 12;

    /**
     * 错误类型 <br>
     * 字节大小：1 <br>
     * 字节序数：10
     */
    private EErrorClass errorClass = EErrorClass.NO_ERROR;

    /**
     * 错误码 <br>
     * 字节大小：1 <br>
     * 字节序数：11
     */
    private byte errorCode = 0x00;

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[BYTE_LENGTH];
        byte[] headerBytes = super.toByteArray();
        System.arraycopy(headerBytes, 0, res, 0, headerBytes.length);
        res[10] = errorClass.getCode();
        res[11] = errorCode;
        return res;
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return AckHeader
     */
    public static AckHeader fromBytes(final byte[] data) {
        if (data.length < BYTE_LENGTH) {
            throw new IndexOutOfBoundsException("解析header时，字节数组长度不够");
        }
        AckHeader header = new AckHeader();
        header.protocolId = data[0];
        header.messageType = EMessageType.from(data[1]);
        header.reserved = ShortUtil.toUInt16(data, 2);
        header.pduReference = ShortUtil.toUInt16(data, 4);
        header.parameterLength = ShortUtil.toUInt16(data, 6);
        header.dataLength = ShortUtil.toUInt16(data, 8);
        header.errorClass = EErrorClass.from(data[10]);
        header.errorCode = data[11];
        return header;
    }
}
