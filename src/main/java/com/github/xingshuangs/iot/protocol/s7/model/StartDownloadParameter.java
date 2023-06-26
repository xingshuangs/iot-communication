package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
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
     * 装载长度，6个字节
     */
    private String loadMemoryLength = "000000";

    /**
     * MC代码长度，6个字节
     */
    private String mC7CodeLength = "000000";


    public StartDownloadParameter() {
        this.functionCode = EFunctionCode.START_UPLOAD;
    }

    @Override
    public int byteArrayLength() {
        return 9 + this.filenameLength+part2Length;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.functionCode.getCode())
                .putByte((byte) (BooleanUtil.setBit(0, this.moreDataFollowing) & BooleanUtil.setBit(1, this.errorStatus)))
                .putBytes(this.unknownBytes)
                .putInteger(this.uploadId)
                .putByte(this.filenameLength)
                .putString(this.fileIdentifier)
                .putBytes(this.blockType.getByteArray())
                .putString(this.blockNumber)
                .putByte(this.destinationFileSystem.getCode())
                .putByte(this.part2Length)
                .putString(this.unknownChar)
                .putString(this.loadMemoryLength)
                .putString(this.mC7CodeLength)
                .getData();
    }
}
