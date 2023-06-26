package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.EDestinationFileSystem;
import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 下载参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DownloadParameter extends DownloadAckParameter implements IObjectByteArray {

    /**
     * 未知字节，2个字节
     */
    protected byte[] unknownBytes = new byte[2];

    /**
     * 上传的Id，4个字节
     */
    protected int uploadId = 0x00000000;

    /**
     * 文件名长度，1个字节
     */
    protected int filenameLength = 9;

    /**
     * 文件id，1个字节
     */
    protected String fileIdentifier = "_";

    /**
     * 数据块类型，2个字节
     */
    protected EFileBlockType blockType = EFileBlockType.DB;

    /**
     * 数据块编号，5个字节
     */
    protected String blockNumber = "";

    /**
     * 目标文件系统，1个字节
     */
    protected EDestinationFileSystem destinationFileSystem = EDestinationFileSystem.P;


    public DownloadParameter() {
        this.functionCode = EFunctionCode.DOWNLOAD;
    }

    @Override
    public int byteArrayLength() {
        return 9 + this.filenameLength;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(9 + this.filenameLength)
                .putByte(this.functionCode.getCode())
                .putByte((byte) (BooleanUtil.setBit(0, this.moreDataFollowing) & BooleanUtil.setBit(1, this.errorStatus)))
                .putBytes(this.unknownBytes)
                .putInteger(this.uploadId)
                .putByte(this.filenameLength)
                .putString(this.fileIdentifier)
                .putBytes(this.blockType.getByteArray())
                .putString(this.blockNumber)
                .putByte(this.destinationFileSystem.getCode())
                .getData();
    }
}
