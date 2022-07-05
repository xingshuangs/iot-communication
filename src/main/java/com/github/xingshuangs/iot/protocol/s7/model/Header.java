package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.IByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;
import com.github.xingshuangs.iot.utils.ByteReadBuff;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 头部
 *
 * @author xingshuang
 */
@Data
public class Header implements IByteArray {

    private static final AtomicInteger index = new AtomicInteger();

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
     * pdu的参考–由主站生成，每次新传输递增，大端 <br>
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

    /**
     * 获取新的pduNumber
     *
     * @return 编号
     */
    public static int getNewPduNumber() {
        int res = index.getAndIncrement();
        if (res >= 65536) {
            index.set(0);
            res = 0;
        }
        return res;
    }

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(BYTE_LENGTH)
                .putByte(this.protocolId)
                .putByte(this.messageType.getCode())
                .putShort(this.reserved)
                .putShort(this.pduReference)
                .putShort(this.parameterLength)
                .putShort(this.dataLength)
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return Header
     */
    public static Header fromBytes(final byte[] data) {
        if (data.length < BYTE_LENGTH) {
            throw new IndexOutOfBoundsException("解析header时，字节数组长度不够");
        }
        ByteReadBuff buff = new ByteReadBuff(data);
        Header header = new Header();
        header.protocolId = buff.getByte();
        header.messageType = EMessageType.from(buff.getByte());
        header.reserved = buff.getUInt16();
        header.pduReference = buff.getUInt16();
        header.parameterLength = buff.getUInt16();
        header.dataLength = buff.getUInt16();
        return header;
    }

    /**
     * 创建默认的头header
     *
     * @return Header对象
     */
    public static Header createDefault() {
        Header header = new Header();
        header.protocolId = (byte) 0x32;
        header.messageType = EMessageType.JOB;
        header.reserved = 0x0000;
        header.pduReference = getNewPduNumber();
        header.parameterLength = 0;
        header.dataLength = 0;
        return header;
    }
}
