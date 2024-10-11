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


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.ESyntaxID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Read and write parameter.
 * 读写参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReadWriteParameter extends Parameter implements IObjectByteArray {

    /**
     * Item count.
     * Request Item结构的数量 <br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    private int itemCount = 0x00;

    /**
     * Request items.
     * (可重复的请求项)
     */
    private List<RequestBaseItem> requestItems = new ArrayList<>();

    /**
     * Add item.
     * (添加请求项)
     *
     * @param item request item
     */
    public void addItem(RequestBaseItem item) {
        this.requestItems.add(item);
        this.itemCount = this.requestItems.size();
    }

    /**
     * Add item list.
     * (添加请求项列表)
     *
     * @param items request item list
     */
    public void addItem(Collection<? extends RequestBaseItem> items) {
        this.requestItems.addAll(items);
        this.itemCount = this.requestItems.size();
    }

    @Override
    public int byteArrayLength() {
        return 2 + this.requestItems.stream().mapToInt(RequestBaseItem::byteArrayLength).sum();
    }

    @Override
    public byte[] toByteArray() {
        int length = 2 + this.requestItems.stream().mapToInt(RequestBaseItem::byteArrayLength).sum();
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length)
                .putByte(this.functionCode.getCode())
                .putByte(this.itemCount);
        for (RequestBaseItem requestItem : this.requestItems) {
            buff.putBytes(requestItem.toByteArray());
        }
        return buff.getData();
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data byte array
     * @return ReadWriteParameter
     */
    public static ReadWriteParameter fromBytes(final byte[] data) {
        if (data.length < 2) {
            // Parameter解析有误，parameter字节数组长度 < 2
            throw new S7CommException("Parameter parsing error, parameter byte array length < 2");
        }
        ByteReadBuff buff = new ByteReadBuff(data);
        ReadWriteParameter readWriteParameter = new ReadWriteParameter();
        readWriteParameter.functionCode = EFunctionCode.from(buff.getByte());
        readWriteParameter.itemCount = buff.getByteToInt();
        if (readWriteParameter.itemCount == 0) {
            return readWriteParameter;
        }
        // 读写返回时，只有功能码和个数
        if (data.length == 2) {
            return readWriteParameter;
        }
        int off = 2;
        for (int i = 0; i < readWriteParameter.itemCount; i++) {
            RequestBaseItem item = parserItem(data, off);
            readWriteParameter.requestItems.add(item);
            off += item.byteArrayLength();
        }
        return readWriteParameter;
    }

    /**
     * Parses byte array and converts it to object.
     * (解析字节数组数据)
     *
     * @param data   byte array
     * @param offset index offset
     * @return RequestBaseItem
     */
    public static RequestBaseItem parserItem(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        byte aByte = buff.getByte(2 + offset);
        ESyntaxID syntaxID = ESyntaxID.from(aByte);
        switch (syntaxID) {
            case S7ANY:
                return RequestItem.fromBytes(data, offset);
            case NCK:
                return RequestNckItem.fromBytes(data, offset);
            default:
                // 无法解析RequestBaseItem对应的类型
                throw new S7CommException("Unable to resolve the corresponding type of RequestBaseItem");
        }
    }

    /**
     * Create request parameter.
     * (创建默认的请求参数)
     *
     * @param functionCode function code
     * @param requestItems request items
     * @return ReadWriteParameter
     */
    public static ReadWriteParameter createReqParameter(EFunctionCode functionCode, Collection<? extends RequestBaseItem> requestItems) {
        ReadWriteParameter parameter = new ReadWriteParameter();
        parameter.functionCode = functionCode;
        parameter.addItem(requestItems);
        return parameter;
    }

    /**
     * Create ack parameter
     * (创建响应参数)
     *
     * @param request request parameter
     * @return new read and write parameter
     */
    public static ReadWriteParameter createAckParameter(ReadWriteParameter request) {
        ReadWriteParameter parameter = new ReadWriteParameter();
        parameter.functionCode = request.functionCode;
        parameter.itemCount = request.itemCount;
        return parameter;
    }
}
