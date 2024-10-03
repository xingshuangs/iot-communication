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
import com.github.xingshuangs.iot.protocol.s7.enums.EDestinationFileSystem;
import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 开始下载参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StartDownloadParameter extends DownloadParameter implements IObjectByteArray {

    /**
     * 第二部分字符串长度，1个字节
     */
    private int part2Length = 13;

    /**
     * 未知字符，1个字节
     */
    private String unknownChar = "1";

    /**
     * 装载长度，6个字节，范围000000-999999
     */
    private int loadMemoryLength = 0;

    /**
     * MC代码长度，6个字节，范围000000-999999
     */
    private int mC7CodeLength = 0;


    public StartDownloadParameter() {
        this.functionCode = EFunctionCode.START_DOWNLOAD;
    }

    @Override
    public int byteArrayLength() {
        return 32;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(32)
                .putByte(this.functionCode.getCode())
                .putByte((byte) (BooleanUtil.setBit(0, this.moreDataFollowing) | BooleanUtil.setBit(1, this.errorStatus)))
                .putBytes(this.errorCode)
                .putInteger(this.id)
                .putByte(this.fileNameLength)
                .putString(this.fileIdentifier)
                .putBytes(this.blockType.getByteArray())
                .putString(String.format("%05d", this.blockNumber))
                .putByte(this.destinationFileSystem.getCode())
                .putByte(this.part2Length)
                .putString(this.unknownChar)
                .putString(String.format("%06d", this.loadMemoryLength))
                .putString(String.format("%06d", this.mC7CodeLength))
                .getData();
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data byte array
     * @return StartDownloadParameter
     */
    public static StartDownloadParameter fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * Parses byte array and converts it to object.
     *
     * @param data   byte array
     * @param offset index offset
     * @return StartDownloadParameter
     */
    public static StartDownloadParameter fromBytes(final byte[] data, final int offset) {
        if (data.length < 32) {
            throw new IndexOutOfBoundsException("StartDownloadParameter, data length < 32");
        }
        StartDownloadParameter res = new StartDownloadParameter();
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        res.functionCode = EFunctionCode.from(buff.getByte());
        byte b = buff.getByte();
        res.moreDataFollowing = BooleanUtil.getValue(b, 0);
        res.errorStatus = BooleanUtil.getValue(b, 1);
        res.errorCode = buff.getBytes(2);
        res.id = buff.getUInt32();
        res.fileNameLength = buff.getByteToInt();
        res.fileIdentifier = buff.getString(1);
        res.blockType = EFileBlockType.from(buff.getString(2));
        res.blockNumber = Integer.parseInt(buff.getString(5));
        res.destinationFileSystem = EDestinationFileSystem.from(buff.getByte());
        res.part2Length = buff.getByteToInt();
        res.unknownChar = buff.getString(1);
        res.loadMemoryLength = Integer.parseInt(buff.getString(6));
        res.mC7CodeLength = Integer.parseInt(buff.getString(6));
        return res;
    }

    public static StartDownloadParameter createDefault(EFileBlockType blockType,
                                                       int blockNumber,
                                                       EDestinationFileSystem destinationFileSystem,
                                                       int loadMemoryLength,
                                                       int mC7CodeLength){
        StartDownloadParameter parameter = new StartDownloadParameter();
        parameter.blockType = blockType;
        parameter.blockNumber = blockNumber;
        parameter.destinationFileSystem = destinationFileSystem;
        parameter.loadMemoryLength = loadMemoryLength;
        parameter.mC7CodeLength = mC7CodeLength;
        return parameter;
    }
}
