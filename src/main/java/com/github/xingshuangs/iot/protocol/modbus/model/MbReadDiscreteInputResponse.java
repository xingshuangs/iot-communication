package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import com.github.xingshuangs.iot.utils.ByteReadBuff;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
import lombok.Data;

/**
 * 响应读离散量输入
 *
 * @author xingshuang
 */
@Data
public final class MbReadDiscreteInputResponse extends MbPdu {

    /**
     * 字节数<br>
     * 字节大小：1个字节
     */
    private int count;

    /**
     * 线圈状态N 或 N+1，N＝输出数量/8，如果余数不等于 0，那么N = N+1
     * 字节大小：N个字节
     */
    private byte[] inputStatus;

    @Override
    public int byteArrayLength() {
        return super.byteArrayLength() + 1 + this.inputStatus.length;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.functionCode.getCode())
                .putByte(this.count)
                .putBytes(this.inputStatus)
                .getData();
    }

    public static MbReadDiscreteInputResponse fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    public static MbReadDiscreteInputResponse fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        MbReadDiscreteInputResponse res = new MbReadDiscreteInputResponse();
        res.functionCode = EMbFunctionCode.from(buff.getByte());
        res.count = buff.getByteToInt();
        res.inputStatus = buff.getBytes(res.count);
        return res;
    }
}
