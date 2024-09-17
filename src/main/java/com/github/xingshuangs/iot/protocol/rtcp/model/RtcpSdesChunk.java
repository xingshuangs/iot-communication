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

package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.rtcp.enums.ERtcpSdesItemType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xingshuang
 */
@Data
public class RtcpSdesChunk implements IObjectByteArray {

    /**
     * SSRC/CSRC
     */
    private long sourceId;

    /**
     * 所有项
     */
    private List<RtcpSdesItem> sdesItems = new ArrayList<>();

    public void addRtcpSdesItem(RtcpSdesItem item) {
        this.sdesItems.add(item);
    }

    public RtcpSdesChunk() {
    }

    public RtcpSdesChunk(long sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public int byteArrayLength() {
        // item结束的时候会多一个字节
        int sum = this.sdesItems.stream().mapToInt(RtcpSdesItem::byteArrayLength).sum() + 1;
        // 保持偶数个字节
        sum = sum % 2 == 0 ? sum : sum + 1;
        return 4 + sum;
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength())
                .putInteger(this.sourceId);
        for (RtcpSdesItem item : this.sdesItems) {
            buff.putBytes(item.toByteArray());
        }
        return buff.getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data byte array
     * @return RtcpHeader
     */
    public static RtcpSdesChunk fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   byte array
     * @param offset index offset
     * @return RtcpHeader
     */
    public static RtcpSdesChunk fromBytes(final byte[] data, final int offset) {
        if (data.length < 4) {
            throw new IndexOutOfBoundsException("RtcpSdesChunk, data length < 4");
        }
        int len = offset;
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        RtcpSdesChunk res = new RtcpSdesChunk();
        res.sourceId = buff.getUInt32();
        len += 4;

        while (data.length > len + 2) {
            // 判定是否结束
            ERtcpSdesItemType type = ERtcpSdesItemType.from(buff.getByte(len));
            if (type == ERtcpSdesItemType.END) {
                break;
            }
            RtcpSdesItem item = RtcpSdesItem.fromBytes(data, len);
            res.sdesItems.add(item);
            len += item.byteArrayLength();
        }
        return res;
    }
}
