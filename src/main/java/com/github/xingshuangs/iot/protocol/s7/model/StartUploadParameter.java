package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.IByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 开始上传参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StartUploadParameter extends Parameter implements IByteArray {

    /**
     * 后续是否还有更多数据
     */
    private boolean moreDataFollowing = false;

    /**
     * 错误状态
     */
    private boolean errorStatus = false;

    /**
     * 未知字节
     */
    private byte[] unknownBytes = new byte[2];

    /**
     * 上传的Id
     */
    private int uploadId = 0x00000000;

    /**
     * 文件名长度
     */
    private int filenameLength = 0;

    /**
     * 文件id
     */
    private String fileIdentifier = "";

    /**
     * 数据块类型
     */
    private String blockType = "";

    /**
     * 数据块编号
     */
    private String blockNumber = "";

    /**
     * 目标文件系统
     */
    private String destinationFileSystem = "";

    public StartUploadParameter() {
        this.functionCode = EFunctionCode.START_UPLOAD;
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
                .putString(this.blockType)
                .putString(this.blockNumber)
                .putString(this.destinationFileSystem)
                .getData();

//        byte[] res = new byte[this.byteArrayLength()];
//        int offset = 0;
//        res[offset++] = this.functionCode.getCode();
//        res[offset++] = (byte) (BooleanUtil.setBit(0, this.moreDataFollowing) & BooleanUtil.setBit(1, this.errorStatus));
//
//        System.arraycopy(this.unknownBytes, 0, res, offset, this.unknownBytes.length);
//        offset += this.unknownBytes.length;
//
//        byte[] uploadIdBytes = IntegerUtil.toByteArray(this.uploadId);
//        System.arraycopy(uploadIdBytes, 0, res, offset, uploadIdBytes.length);
//        offset += uploadIdBytes.length;
//
//        res[offset++] = ByteUtil.toByte(this.filenameLength);
//
//        byte[] fileNameBytes = (this.fileIdentifier + this.blockType + this.blockNumber + this.destinationFileSystem).getBytes(StandardCharsets.US_ASCII);
//        System.arraycopy(fileNameBytes, 0, res, offset, fileNameBytes.length);
//        return res;
    }
}
