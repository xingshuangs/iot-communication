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
import com.github.xingshuangs.iot.protocol.s7.enums.ENckArea;
import com.github.xingshuangs.iot.protocol.s7.enums.ENckModule;
import com.github.xingshuangs.iot.protocol.s7.enums.ESyntaxID;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * NCK请求项
 *
 * @author xingshuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RequestNckItem extends RequestBaseItem {

    public static final int BYTE_LENGTH = 10;

    public RequestNckItem() {
        this.specificationType = (byte) 0x12;
        this.lengthOfFollowing = 0x08;
        this.syntaxId = ESyntaxID.NCK;
    }

    public RequestNckItem(ENckArea area, int unit, int columnNumber, int lineNumber, ENckModule module, int lineCount) {
        this.specificationType = (byte) 0x12;
        this.lengthOfFollowing = 0x08;
        this.syntaxId = ESyntaxID.NCK;

        this.area = area;
        this.unit = unit;
        this.columnNumber = columnNumber;
        this.lineNumber = lineNumber;
        this.module = module;
        this.lineCount = lineCount;
    }

    /**
     * NCK区域 <br>
     * 字节大小：1 <br>
     * 字节序数：3
     */
    private ENckArea area = ENckArea.N_NCK;

    /**
     * 通道编号 <br>
     * 字节大小：1 <br>
     * 字节序数：4
     */
    private int unit = 0x0000;

    /**
     * 列编号 <br>
     * 字节大小：2 <br>
     * 字节序数：5
     */
    private int columnNumber = 0x0000;

    /**
     * 行编号 <br>
     * 字节大小：2 <br>
     * 字节序数：7
     */
    private int lineNumber = 0x0000;

    /**
     * 模块名 <br>
     * 字节大小：1 <br>
     * 字节序数：8
     */
    private ENckModule module = ENckModule.S;

    /**
     * 行个数 <br>
     * 字节大小：1 <br>
     * 字节序数：9
     */
    private int lineCount = 1;

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        byte areaAndUint = (byte) (((this.area.getCode() << 5) & (byte) 0xE0)
                | (this.unit & (byte) 0x1F));
        return ByteWriteBuff.newInstance(BYTE_LENGTH)
                .putByte(this.specificationType)
                .putByte(this.lengthOfFollowing)
                .putByte(this.syntaxId.getCode())
                .putByte(areaAndUint)
                .putShort(this.columnNumber)
                .putShort(this.lineNumber)
                .putByte(this.module.getCode())
                .putByte(this.lineCount)
                .getData();
    }

    /**
     * 复制一个新对象
     *
     * @return requestItem
     */
    public RequestNckItem copy() {
        RequestNckItem requestItem = new RequestNckItem();
        requestItem.specificationType = this.specificationType;
        requestItem.lengthOfFollowing = this.lengthOfFollowing;
        requestItem.syntaxId = this.syntaxId;
        requestItem.area = this.area;
        requestItem.unit = this.unit;
        requestItem.columnNumber = this.columnNumber;
        requestItem.lineNumber = this.lineNumber;
        requestItem.module = this.module;
        requestItem.lineCount = this.lineCount;
        return requestItem;
    }

    public static RequestNckItem fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RequestItem
     */
    public static RequestNckItem fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        RequestNckItem requestItem = new RequestNckItem();
        requestItem.specificationType = buff.getByte();
        requestItem.lengthOfFollowing = buff.getByteToInt();
        requestItem.syntaxId = ESyntaxID.from(buff.getByte());
        byte areaAndUnit = buff.getByte();
        requestItem.area = ENckArea.from((byte) ((areaAndUnit & (byte) 0xE0) >> 5));
        requestItem.unit = areaAndUnit & (byte) 0x1F;
        requestItem.columnNumber = buff.getUInt16();
        requestItem.lineNumber = buff.getUInt16();
        requestItem.module = ENckModule.from(buff.getByte());
        requestItem.lineCount = buff.getByteToInt();
        return requestItem;
    }


    public static RequestNckItem createByParams(ENckArea area, int unit, int columnNumber, int lineNumber, ENckModule module, int lineCount) {
        RequestNckItem requestItem = new RequestNckItem();
        requestItem.area = area;
        requestItem.unit = unit;
        requestItem.columnNumber = columnNumber;
        requestItem.lineNumber = lineNumber;
        requestItem.module = module;
        requestItem.lineCount = lineCount;
        return requestItem;
    }
}
