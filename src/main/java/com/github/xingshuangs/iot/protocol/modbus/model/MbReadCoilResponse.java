package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import com.github.xingshuangs.iot.utils.ByteReadBuff;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
import lombok.Data;

/**
 * 响应读取线圈
 *
 * @author xingshuang
 */
@Data
public final class MbReadCoilResponse extends MbPdu {

    /**
     * 字节数<br>
     * 字节大小：1个字节
     */
    private int count;

    /**
     * 线圈状态N 或 N+1，N＝输出数量/8，如果余数不等于 0，那么N = N+1
     * 字节大小：N个字节
     */
    private byte[] coilStatus;

    @Override
    public int byteArrayLength() {
        return super.byteArrayLength() + 1 + this.coilStatus.length;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.functionCode.getCode())
                .putByte(this.count)
                .putBytes(this.coilStatus)
                .getData();
    }

    public static MbReadCoilResponse fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    public static MbReadCoilResponse fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbReadCoilResponse res = new MbReadCoilResponse();
        res.functionCode = EMbFunctionCode.from(buff.getByte());
        res.count = buff.getByteToInt();
        res.coilStatus = buff.getBytes(res.count);
        return res;
    }
}
