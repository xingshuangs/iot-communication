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


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.EReturnCode;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * 返回项
 *
 * @author xingshuang
 */
@Data
public class ReturnItem implements IObjectByteArray {

    /**
     * 返回码 <br>
     * 字节大小：1 <br>
     * 字节序数：0
     */
    protected EReturnCode returnCode = EReturnCode.SUCCESS;

    @Override
    public int byteArrayLength() {
        return 1;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(returnCode.getCode())
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return ReturnItem
     */
    public static ReturnItem fromBytes(final byte[] data) {
        ReturnItem returnItem = new ReturnItem();
        returnItem.returnCode = EReturnCode.from(data[0]);
        return returnItem;
    }

    /**
     * 获取默认数据返回
     *
     * @param returnCode 返回码
     * @return 返回数据
     */
    public static ReturnItem createDefault(EReturnCode returnCode) {
        ReturnItem item = new ReturnItem();
        item.returnCode = returnCode;
        return item;
    }
}
