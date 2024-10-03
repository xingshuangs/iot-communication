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


import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上传参数
 *
 * @author xingshuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UploadParameter extends UploadAckParameter {

    /**
     * 未知字节，2个字节
     */
    protected byte[] errorCode = new byte[]{0x00, 0x00};

    /**
     * 下载的Id，4个字节（没用）
     */
    protected long id = 0x00000000;

    public UploadParameter() {
        this.functionCode = EFunctionCode.UPLOAD;
    }

    @Override
    public int byteArrayLength() {
        return 8;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(8)
                .putByte(this.functionCode.getCode())
                .putByte((byte) (BooleanUtil.setBit(0, this.moreDataFollowing) | BooleanUtil.setBit(1, this.errorStatus)))
                .putBytes(this.errorCode)
                .putInteger(this.id)
                .getData();
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data byte array
     * @return UploadParameter
     */
    public static UploadParameter fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data   byte array
     * @param offset index offset
     * @return UploadParameter
     */
    public static UploadParameter fromBytes(final byte[] data, final int offset) {
        if (data.length < 8) {
            throw new IndexOutOfBoundsException("UploadParameter时, data length < 8");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        UploadParameter res = new UploadParameter();
        res.functionCode = EFunctionCode.from(buff.getByte());
        byte b = buff.getByte();
        res.moreDataFollowing = BooleanUtil.getValue(b, 0);
        res.errorStatus = BooleanUtil.getValue(b, 1);
        res.errorCode = buff.getBytes(2);
        res.id = buff.getUInt32();
        return res;
    }

    /**
     * 创建上传参数
     *
     * @param uploadId 上传Id
     * @return UploadParameter
     */
    public static UploadParameter createDefault(long uploadId) {
        UploadParameter parameter = new UploadParameter();
        parameter.id = uploadId;
        return parameter;
    }
}
