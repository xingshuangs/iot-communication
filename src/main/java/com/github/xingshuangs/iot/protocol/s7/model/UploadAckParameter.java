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

package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上传响应参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UploadAckParameter extends Parameter implements IObjectByteArray {

    /**
     * 后续是否还有更多数据
     */
    protected boolean moreDataFollowing = false;

    /**
     * 错误状态
     */
    protected boolean errorStatus = false;


    public UploadAckParameter() {
        this.functionCode = EFunctionCode.UPLOAD;
    }

    @Override
    public int byteArrayLength() {
        return 2;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(2)
                .putByte(this.functionCode.getCode())
                .putByte((byte) (BooleanUtil.setBit(0, this.moreDataFollowing) | BooleanUtil.setBit(1, this.errorStatus)))
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data byte array
     * @return DownloadAckParameter
     */
    public static UploadAckParameter fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   byte array
     * @param offset index offset
     * @return DownloadAckParameter
     */
    public static UploadAckParameter fromBytes(final byte[] data, final int offset) {
        if (data.length < 2) {
            throw new IndexOutOfBoundsException("DownloadAckParameter, data length < 2");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        UploadAckParameter res = new UploadAckParameter();
        res.functionCode = EFunctionCode.from(buff.getByte(0));
        res.moreDataFollowing = buff.getBoolean(1, 0);
        res.errorStatus = buff.getBoolean(1, 1);
        return res;
    }
}
