package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EErrorClass;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
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
     * 错误码，本来是1个字节的，但本质上errorCode（真正） = errorClass + errorCode（原） <br>
     * 字节大小：2 <br>
     * 字节序数：10-11
     */
    private int errorCode = 0x0000;

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(BYTE_LENGTH)
                .putBytes(super.toByteArray())
                .putByte(errorClass.getCode())
                .putByte(this.errorCode)
                .getData();
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
        ByteReadBuff buff = new ByteReadBuff(data);
        AckHeader header = new AckHeader();
        header.protocolId = buff.getByte();
        header.messageType = EMessageType.from(buff.getByte());
        header.reserved = buff.getUInt16();
        header.pduReference = buff.getUInt16();
        header.parameterLength = buff.getUInt16();
        header.dataLength = buff.getUInt16();
        header.errorClass = EErrorClass.from(buff.getByte());
        header.errorCode = buff.getUInt16(10);
        return header;
    }

    /**
     * 创建默认的头header
     *
     * @return Header对象
     */
    public static AckHeader createDefault(Header request, EErrorClass errorClass, int errorCode) {
        AckHeader header = new AckHeader();
        header.protocolId = request.protocolId;
        header.messageType = EMessageType.ACK_DATA;
        header.reserved = request.reserved;
        header.pduReference = request.pduReference;
        header.parameterLength = request.parameterLength;
        header.dataLength = request.dataLength;
        header.errorClass = errorClass;
        header.errorCode = errorCode;
        return header;
    }
}
