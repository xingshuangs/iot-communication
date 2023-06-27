package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
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
    private int part2Length;

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
        this.functionCode = EFunctionCode.START_UPLOAD;
    }

    @Override
    public int byteArrayLength() {
        return 32;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(32)
                .putByte(this.functionCode.getCode())
                .putByte((byte) (BooleanUtil.setBit(0, this.moreDataFollowing) & BooleanUtil.setBit(1, this.errorStatus)))
                .putBytes(this.unknownBytes)
                .putInteger(this.downloadId)
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
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return StartDownloadParameter
     */
    public static StartDownloadParameter fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return StartDownloadParameter
     */
    public static StartDownloadParameter fromBytes(final byte[] data, final int offset) {
        if (data.length < 32) {
            throw new IndexOutOfBoundsException("解析StartDownloadParameter时，字节数组长度不够");
        }
        StartDownloadParameter res = new StartDownloadParameter();
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        res.functionCode = EFunctionCode.from(buff.getByte());
        byte b = buff.getByte();
        res.moreDataFollowing = BooleanUtil.getValue(b, 0);
        res.errorStatus = BooleanUtil.getValue(b, 1);
        res.unknownBytes = buff.getBytes(2);
        res.downloadId = buff.getUInt32();
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
}
