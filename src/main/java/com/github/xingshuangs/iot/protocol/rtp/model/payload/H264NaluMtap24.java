package com.github.xingshuangs.iot.protocol.rtp.model.payload;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;

import java.util.ArrayList;
import java.util.List;

/**
 * STAP_A
 * <p>
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
 *
 * @author xingshuang
 */
public class H264NaluMtap24 extends H264NaluBase {

    /**
     * 解码顺序编号
     */
    private int decodingOrderNumber;

    private List<H264NaluMtap24Single> naluSingles = new ArrayList<>();

    @Override
    public int byteArrayLength() {
        int sum = this.header.byteArrayLength();
        sum += 2;
        for (H264NaluMtap24Single item : this.naluSingles) {
            sum += item.byteArrayLength();
        }
        return sum;
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength());
        buff.putBytes(this.header.toByteArray());
        buff.putShort(this.decodingOrderNumber);
        for (H264NaluMtap24Single item : this.naluSingles) {
            buff.putBytes(item.toByteArray());
        }
        return buff.getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static H264NaluMtap24 fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static H264NaluMtap24 fromBytes(final byte[] data, final int offset) {
        if (data.length < 3) {
            throw new IndexOutOfBoundsException("解析H264NaluStapSingle时，字节数组长度不够");
        }
        int index = offset;
        H264NaluMtap24 res = new H264NaluMtap24();

        res.header = H264NaluHeader.fromBytes(data, index);
        index += res.header.byteArrayLength();

        res.decodingOrderNumber = ByteReadBuff.newInstance(data, index).getUInt16();
        index += 2;

        while (index < data.length) {
            H264NaluMtap24Single tmp = H264NaluMtap24Single.fromBytes(data, index);
            res.naluSingles.add(tmp);
            index += tmp.byteArrayLength();
        }
        return res;
    }
}
