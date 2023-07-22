package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.EDestinationFileSystem;
import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 开始上传参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StartUploadParameter extends DownloadParameter {

    public StartUploadParameter() {
        this.functionCode = EFunctionCode.START_UPLOAD;
        this.errorCode = new byte[]{0x00, 0x00};
    }

    public static StartUploadParameter createDefault(EFileBlockType blockType,
                                                     int blockNumber,
                                                     EDestinationFileSystem destinationFileSystem) {
        StartUploadParameter parameter = new StartUploadParameter();
        parameter.blockType = blockType;
        parameter.blockNumber = blockNumber;
        parameter.destinationFileSystem = destinationFileSystem;
        return parameter;
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return StartUploadParameter
     */
    public static StartUploadParameter fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return StartUploadParameter
     */
    public static StartUploadParameter fromBytes(final byte[] data, final int offset) {
        if (data.length < 18) {
            throw new IndexOutOfBoundsException("解析DownloadParameter时，字节数组长度不够");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        StartUploadParameter res = new StartUploadParameter();
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
        return res;
    }
}
