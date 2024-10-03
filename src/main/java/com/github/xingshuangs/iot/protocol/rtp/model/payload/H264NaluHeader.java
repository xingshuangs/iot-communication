/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.protocol.rtp.model.payload;


import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.rtp.enums.EH264NaluType;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import lombok.Data;

/**
 * Nalu的头
 * +---------------+
 * |0|1|2|3|4|5|6|7|
 * +-+-+-+-+-+-+-+-+
 * |F|NRI|  Type   |
 * +---------------+
 *
 * @author xingshuang
 */
@Data
public class H264NaluHeader implements IObjectByteArray {

    /**
     * Forbidden zero bit.
     * (H264 规范要求该位为 0)
     */
    private boolean forbiddenZeroBit;

    /**
     * The value ranges from 0 to 3, indicating the importance of the NALU. For the NALU decoder with NRI=0,
     * it can be discarded without affecting the playback of the image. The larger the value, the more important the NALU is.
     * 取值 0~3，指示该 NALU 重要性，对于 NRI=0 的 NALU 解码器可以丢弃它而不影响图像的回放，该值越大说明该 NALU 越重要，
     * 需要受到保护。如果当前 NALU 属于参考帧的数据，或者是序列参数集，图像参数集等重要信息，则该值必须大于 0
     */
    private int nri;

    /**
     * Nalu type.
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
     * Parses byte array and converts it to object.
     *
     * @param data byte array
     * @return RtcpHeader
     */
    public static H264NaluHeader fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data   byte array
     * @param offset index offset
     * @return RtcpHeader
     */
    public static H264NaluHeader fromBytes(final byte[] data, final int offset) {
        if (data.length < 1) {
            throw new IndexOutOfBoundsException("header, data length < 1");
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
