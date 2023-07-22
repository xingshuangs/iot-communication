package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
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
public class StartUploadAckParameter extends UploadParameter {

    /**
     * 即自此之后的数据长度，1个字节
     */
    private int blockLengthStringLength = 7;

    /**
     * 到尾完整上传快的长度（以字节为单位）、可以拆分为多个PDU，7个字节
     */
    private int blockLength = 0;

    public StartUploadAckParameter() {
        this.functionCode = EFunctionCode.START_UPLOAD;
        this.errorCode = new byte[]{0x01, 0x00};
    }

    @Override
    public int byteArrayLength() {
        return 16;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(16)
                .putByte(this.functionCode.getCode())
                .putByte((byte) (BooleanUtil.setBit(0, this.moreDataFollowing) & BooleanUtil.setBit(1, this.errorStatus)))
                .putBytes(this.errorCode)
                .putInteger(this.id)
                .putByte(this.blockLengthStringLength)
                .putString(String.format("%07d", this.blockLength))
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return StartUploadAckParameter
     */
    public static StartUploadAckParameter fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return StartUploadAckParameter
     */
    public static StartUploadAckParameter fromBytes(final byte[] data, final int offset) {
        if (data.length < 8) {
            throw new IndexOutOfBoundsException("解析StartUploadAckParameter时，字节数组长度不够");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        StartUploadAckParameter res = new StartUploadAckParameter();
        res.functionCode = EFunctionCode.from(buff.getByte());
        byte b = buff.getByte();
        res.moreDataFollowing = BooleanUtil.getValue(b, 0);
        res.errorStatus = BooleanUtil.getValue(b, 1);
        res.errorCode = buff.getBytes(2);
        res.id = buff.getUInt32();
        res.blockLengthStringLength = buff.getByteToInt();
        res.blockLength = Integer.parseInt(buff.getString(res.blockLengthStringLength));
        return res;
    }
}
