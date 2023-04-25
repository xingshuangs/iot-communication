package com.github.xingshuangs.iot.protocol.rtp.payload;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.rtp.enums.EH264NaluType;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import lombok.Data;

/**
 * Nalu的头
 *
 * @author xingshuang
 */
@Data
public class H264NaluHeader implements IObjectByteArray {

    /**
     * H264 规范要求该位为 0
     */
    private boolean forbiddenZeroBit;

    /**
     * 取值 0~3，指示该 NALU 重要性，对于 NRI=0 的 NALU 解码器可以丢弃它而不影响图像的回放，该值越大说明该 NALU 越重要，
     * 需要受到保护。如果当前 NALU 属于参考帧的数据，或者是序列参数集，图像参数集等重要信息，则该值必须大于 0
     */
    private int nri;

    /**
     * 表示 NALU 数据类型，该字段占 5 位，取值 0 ~ 31
     */
    private EH264NaluType type;

    @Override
    public int byteArrayLength() {
        return 1;
    }

    @Override
    public byte[] toByteArray() {
        byte res = (byte) (BooleanUtil.setBit(7, this.forbiddenZeroBit)
                | ((this.nri << 5) & 0x60)
                | (this.type.getCode() & 0x1F));
        return new byte[]{res};
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static H264NaluHeader fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static H264NaluHeader fromBytes(final byte[] data, final int offset) {
        if (data.length < 1) {
            throw new IndexOutOfBoundsException("解析header时，字节数组长度不够");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        H264NaluHeader res = new H264NaluHeader();
        byte aByte = buff.getByte();
        res.forbiddenZeroBit = BooleanUtil.getValue(aByte, 7);
        res.nri = (aByte >> 5) & 0x03;
        res.type = EH264NaluType.from(aByte & 0x1F);
        return res;
    }
}
