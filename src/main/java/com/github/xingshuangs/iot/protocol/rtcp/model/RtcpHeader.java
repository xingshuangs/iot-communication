package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
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
     * 长度域（Length）：16比特，RTCP包的长度，包括填充的内容。
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
}
