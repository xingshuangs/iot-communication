package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import lombok.Data;

/**
 * 上传参数
 *
 * @author xingshuang
 */
@Data
public class UploadParameter extends UploadAckParameter {

    /**
     * 未知字节，2个字节
     */
    protected byte[] errorCode = new byte[]{0x00, 0x00};

    /**
     * 下载的Id，4个字节（没用）
     */
    protected long id = 0x00000000;

    public UploadParameter() {
        this.functionCode = EFunctionCode.UPLOAD;
    }

    @Override
    public int byteArrayLength() {
        return 8;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(8)
                .putByte(this.functionCode.getCode())
                .putByte((byte) (BooleanUtil.setBit(0, this.moreDataFollowing) | BooleanUtil.setBit(1, this.errorStatus)))
                .putBytes(this.errorCode)
                .putInteger(this.id)
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return UploadParameter
     */
    public static UploadParameter fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return UploadParameter
     */
    public static UploadParameter fromBytes(final byte[] data, final int offset) {
        if (data.length < 8) {
            throw new IndexOutOfBoundsException("解析UploadParameter时，字节数组长度不够");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        UploadParameter res = new UploadParameter();
        res.functionCode = EFunctionCode.from(buff.getByte());
        byte b = buff.getByte();
        res.moreDataFollowing = BooleanUtil.getValue(b, 0);
        res.errorStatus = BooleanUtil.getValue(b, 1);
        res.errorCode = buff.getBytes(2);
        res.id = buff.getUInt32();
        return res;
    }

    /**
     * 创建上传参数
     *
     * @param uploadId 上传Id
     * @return UploadParameter
     */
    public static UploadParameter createDefault(long uploadId) {
        UploadParameter parameter = new UploadParameter();
        parameter.id = uploadId;
        return parameter;
    }
}
