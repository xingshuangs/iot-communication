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


import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 读写数据
 *
 * @author xingshuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReadWriteDatum extends Datum {

    /**
     * 数据项
     */
    private final List<ReturnItem> returnItems = new ArrayList<>();

    @Override
    public int byteArrayLength() {
        if (this.returnItems.isEmpty()) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < this.returnItems.size(); i++) {
            int length = this.returnItems.get(i).byteArrayLength();
            sum += length;
            // 当数据不是最后一个的时候，如果数据长度为奇数，S7协议会多填充一个字节，使其数量保持为偶数（最后一个奇数长度数据不需要填充）
            if (i != this.returnItems.size() - 1
                    && length % 2 == 1
                    && this.returnItems.get(i) instanceof DataItem) {
                sum++;
            }
        }
        return sum;
    }

    @Override
    public byte[] toByteArray() {
        if (this.returnItems.isEmpty()) {
            return new byte[0];
        }
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength());
        for (int i = 0; i < this.returnItems.size(); i++) {
            int length = this.returnItems.get(i).byteArrayLength();
            buff.putBytes(this.returnItems.get(i).toByteArray());
            // 当数据不是最后一个的时候，如果数据长度为奇数，S7协议会多填充一个字节，使其数量保持为偶数（最后一个奇数长度数据不需要填充）
            if (i != this.returnItems.size() - 1
                    && length % 2 == 1
                    && this.returnItems.get(i) instanceof DataItem) {
                buff.putByte(0x00);
            }
        }
        return buff.getData();
    }

    /**
     * 添加数据项
     *
     * @param item 项
     */
    public void addItem(ReturnItem item) {
        this.returnItems.add(item);
    }

    /**
     * 批量添加数据项
     *
     * @param items 数据项列表
     */
    public void addItem(Collection<? extends ReturnItem> items) {
        this.returnItems.addAll(items);
    }

    /**
     * 根据消息类型和功能码，对字节数组数据进行解析
     *
     * @param data         字节数组数据
     * @param messageType  头部的消息类型
     * @param functionCode 参数部分的功能码
     * @return ReadWriteDatum
     */
    public static ReadWriteDatum fromBytes(final byte[] data, EMessageType messageType, EFunctionCode functionCode) {
        ReadWriteDatum datum = new ReadWriteDatum();
        if (data.length == 0) {
            return datum;
        }
        int offset = 0;
        byte[] remain = data;
        while (true) {
            ReturnItem dataItem;
            // 对写操作的响应结果进行特殊处理
            if (EMessageType.ACK_DATA == messageType && EFunctionCode.WRITE_VARIABLE == functionCode) {
                dataItem = ReturnItem.fromBytes(remain);
                datum.returnItems.add(dataItem);
                offset += dataItem.byteArrayLength();
            } else {
                dataItem = DataItem.fromBytes(remain);
                datum.returnItems.add(dataItem);
                offset += dataItem.byteArrayLength();
                // 当数据不是最后一个的时候，如果数据长度为奇数，S7协议会多填充一个字节，使其数量保持为偶数（最后一个奇数长度数据不需要填充）
                if (dataItem.byteArrayLength() % 2 == 1) {
                    offset++;
                }
            }
            if (offset >= data.length) {
                break;
            }
            remain = Arrays.copyOfRange(data, offset, data.length);
        }
        return datum;
    }

    /**
     * 创建数据Datum
     *
     * @param dataItems 数据项
     * @return 数据对象Datum
     */
    public static ReadWriteDatum createDatum(Collection<? extends ReturnItem> dataItems) {
        ReadWriteDatum datum = new ReadWriteDatum();
        datum.addItem(dataItems);
        return datum;
    }
}
