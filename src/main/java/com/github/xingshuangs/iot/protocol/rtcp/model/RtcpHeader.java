package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.rtcp.enums.ERtcpPackageType;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import lombok.Data;

/**
 * 头
 *
 * @author xingshuang
 */
@Data
public class RtcpHeader implements IObjectByteArray {

    /**
     * 版本（V）：2比特
     */
    protected int version;

    /**
     * 填充（P）：1比特，如果该位置为1，则该RTCP包的尾部就包含附加的填充字节。
     */
    protected boolean padding;

    /**
     * 接收报告计数器（RC）：5比特，该SR包中的接收报告块的数目，可以为零。
     */
    protected int receptionCount;

    /**
     * 包类型（PT）：8比特，SR包是200。
     */
    protected ERtcpPackageType packageType;

    /**
     * 长度域（Length）：16比特，RTCP包的长度，包括填充的内容。长度代表整个数据包的大小（协议头+荷载+填充）
     */
    protected int length;

    @Override
    public int byteArrayLength() {
        return 4;
    }

    @Override
    public byte[] toByteArray() {
        byte res = (byte) ((this.version << 6)
                & (BooleanUtil.setBit(6, this.padding) & 0xFF)
                & this.receptionCount);
        return ByteWriteBuff.newInstance(4)
                .putByte(res)
                .putByte(this.packageType.getCode())
                .putShort(this.length)
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static RtcpHeader fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static RtcpHeader fromBytes(final byte[] data, final int offset) {
        if (data.length < 4) {
            throw new IndexOutOfBoundsException("解析header时，字节数组长度不够");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        RtcpHeader res = new RtcpHeader();
        byte aByte = buff.getByte();
        res.version = aByte >> 6;
        res.padding = BooleanUtil.getValue(aByte, 5);
        res.receptionCount = aByte & 0x1F;
        res.packageType = ERtcpPackageType.from(buff.getByte());
        res.length = buff.getUInt16();
        return res;
    }
}
