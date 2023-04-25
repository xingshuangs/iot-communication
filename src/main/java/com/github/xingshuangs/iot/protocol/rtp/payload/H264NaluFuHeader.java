package com.github.xingshuangs.iot.protocol.rtp.payload;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.rtp.enums.EH264NaluType;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import lombok.Data;

/**
 * Nalu的FU头
 *
 * @author xingshuang
 */
@Data
public class H264NaluFuHeader implements IObjectByteArray {

    /**
     * 当设置成1,开始位指示分片NAL单元的开始。当跟随的FU荷载不是分片NAL单元荷载的开始，开始位设为0。
     */
    private boolean start;

    /**
     * 当设置成1, 结束位指示分片NAL单元的结束，即, 荷载的最后字节也是分片NAL单元的最后一个字节。
     * 当跟随的 FU荷载不是分片NAL单元的最后分片,结束位设置为0。
     */
    private boolean end;

    /**
     * 保留位必须设置为0，接收者必须忽略该位
     */
    private boolean reserve;

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
        byte res = (byte) (BooleanUtil.setBit(7, this.start)
                | BooleanUtil.setBit(6, this.end)
                | BooleanUtil.setBit(5, this.reserve)
                | (this.type.getCode() & 0x1F));
        return new byte[]{res};
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static H264NaluFuHeader fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static H264NaluFuHeader fromBytes(final byte[] data, final int offset) {
        if (data.length < 1) {
            throw new IndexOutOfBoundsException("解析H264NaluFuHeader时，字节数组长度不够");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        H264NaluFuHeader res = new H264NaluFuHeader();
        byte aByte = buff.getByte();
        res.start = BooleanUtil.getValue(aByte, 7);
        res.end = BooleanUtil.getValue(aByte, 6);
        res.reserve = BooleanUtil.getValue(aByte, 5);
        res.type = EH264NaluType.from(aByte & 0x1F);
        return res;
    }
}
