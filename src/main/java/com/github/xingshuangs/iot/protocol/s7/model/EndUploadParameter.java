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
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.BooleanUtil;

/**
 * 上传参数
 *
 * @author xingshuang
 */
public class EndUploadParameter extends UploadParameter {

    public EndUploadParameter() {
        this.functionCode = EFunctionCode.END_UPLOAD;
    }

    public static EndUploadParameter createDefault(long uploadId){
        EndUploadParameter parameter = new EndUploadParameter();
        parameter.id = uploadId;
        return parameter;
    }

    /**
     * 字节数组数据解析
     *
     * @param data byte array
     * @return UploadParameter
     */
    public static EndUploadParameter fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   byte array
     * @param offset index offset
     * @return UploadParameter
     */
    public static EndUploadParameter fromBytes(final byte[] data, final int offset) {
        if (data.length < 8) {
            throw new IndexOutOfBoundsException("UploadParameter, data length < 8");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        EndUploadParameter res = new EndUploadParameter();
        res.functionCode = EFunctionCode.from(buff.getByte());
        byte b = buff.getByte();
        res.moreDataFollowing = BooleanUtil.getValue(b, 0);
        res.errorStatus = BooleanUtil.getValue(b, 1);
        res.errorCode = buff.getBytes(2);
        res.id = buff.getUInt32();
        return res;
    }
}
