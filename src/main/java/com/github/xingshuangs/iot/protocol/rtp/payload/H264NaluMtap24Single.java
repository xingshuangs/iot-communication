package com.github.xingshuangs.iot.protocol.rtp.payload;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.utils.IntegerUtil;

/**
 * @author xingshuang
 * 0                   1                   2                   3
 * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                          RTP Header                           |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |MTAP24 NAL HDR |  decoding order number base   | NALU 1 Size   |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |  NALU 1 Size  |  NALU 1 DOND  |       NALU 1 TS offs          |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |NALU 1 TS offs |  NALU 1 HDR   |  NALU 1 DATA                  |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                               +
 * :                                                               :
 * +               +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |               | NALU 2 SIZE                   |  NALU 2 DOND  |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |       NALU 2 TS offset                        |  NALU 2 HDR   |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |  NALU 2 DATA                                                  |
 * :                                                               :
 * |                               +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                               :...OPTIONAL RTP padding        |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 */
public class H264NaluMtap24Single extends H264NaluSingle {

    private int size;

    private int dond;

    private int tsOffset;

    @Override
    public int byteArrayLength() {
        return this.header.byteArrayLength() + 2 + this.payload.length;
    }

    @Override
    public byte[] toByteArray() {
        byte[] tsOffsetBytes = IntegerUtil.toCustomByteArray(this.tsOffset, 1, 3);
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putShort(this.size)
                .putByte(this.dond)
                .putBytes(tsOffsetBytes)
                .putBytes(this.header.toByteArray())
                .putBytes(this.payload)
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static H264NaluMtap24Single fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static H264NaluMtap24Single fromBytes(final byte[] data, final int offset) {
        if (data.length < 3) {
            throw new IndexOutOfBoundsException("解析H264NaluStapSingle时，字节数组长度不够");
        }
        int index = offset;
        ByteReadBuff buff = ByteReadBuff.newInstance(data, index);
        H264NaluMtap24Single res = new H264NaluMtap24Single();
        res.size = buff.getUInt16();
        res.dond = buff.getByteToInt();
        res.tsOffset = IntegerUtil.toInt32In3Bytes(buff.getBytes(3),0);
        index += 6;

        res.header = H264NaluHeader.fromBytes(data, index);
        index += res.header.byteArrayLength();

        res.payload = ByteReadBuff.newInstance(data, index).getBytes(res.size);
        return res;
    }
}
