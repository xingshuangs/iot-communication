package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.BooleanUtil;

/**
 * 上传参数
 *
 * @author xingshuang
 */
public class EndUploadParameter extends UploadParameter {

    public EndUploadParameter() {
        this.functionCode = EFunctionCode.END_UPLOAD;
    }

    public static EndUploadParameter createDefault(long uploadId){
        EndUploadParameter parameter = new EndUploadParameter();
        parameter.id = uploadId;
        return parameter;
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return UploadParameter
     */
    public static EndUploadParameter fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return UploadParameter
     */
    public static EndUploadParameter fromBytes(final byte[] data, final int offset) {
        if (data.length < 8) {
            throw new IndexOutOfBoundsException("解析UploadParameter时，字节数组长度不够");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        EndUploadParameter res = new EndUploadParameter();
        res.functionCode = EFunctionCode.from(buff.getByte());
        byte b = buff.getByte();
        res.moreDataFollowing = BooleanUtil.getValue(b, 0);
        res.errorStatus = BooleanUtil.getValue(b, 1);
        res.errorCode = buff.getBytes(2);
        res.id = buff.getUInt32();
        return res;
    }
}
