package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.exceptions.ModbusCommException;
import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.utils.LRCUtil;
import lombok.Data;

/**
 * ASCII的modbus响应
 *
 * @author xingshuang
 */
@Data
public class MbAsciiResponse implements IObjectByteArray {

    /**
     * 单元标识符<br>
     * 字节大小：1个字节
     * 字节序数：0
     */
    private int unitId = 1;

    /**
     * 协议数据单元
     */
    private MbPdu pdu;

    /**
     * 纵向冗余校验，最后1个字节
     */
    private byte lrc;

    @Override
    public int byteArrayLength() {
        return 2 + this.pdu.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.unitId)
                .putBytes(this.pdu.toByteArray())
                .putByte(this.lrc)
                .getData();
    }

    /**
     * 自我数据校验
     */
    public void selfCheck() {
        if (this.pdu == null) {
            throw new ModbusCommException("pdu不能为null");
        }
        byte[] data = ByteWriteBuff.newInstance(1 + this.pdu.byteArrayLength())
                .putByte(this.unitId)
                .putBytes(this.pdu.toByteArray())
                .getData();
        this.lrc = LRCUtil.lrc(data);
    }

    /**
     * 校验CRC
     *
     * @return true：校验成功，false：校验失败
     */
    public boolean checkLrc() {
        if (this.pdu == null) {
            throw new ModbusCommException("pdu不能为null");
        }
        byte[] data = ByteWriteBuff.newInstance(1 + this.pdu.byteArrayLength())
                .putByte(this.unitId)
                .putBytes(this.pdu.toByteArray())
                .getData();
        return this.lrc == LRCUtil.lrc(data);
    }


    /**
     * 解析字节数组数据
     *
     * @param data 字节数组数据
     * @return MbRtuResponse
     */
    public static MbAsciiResponse fromBytes(byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 解析字节数组数据
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return MbRtuResponse
     */
    public static MbAsciiResponse fromBytes(byte[] data, int offset) {
        MbAsciiResponse response = new MbAsciiResponse();
        ByteReadBuff buff = ByteReadBuff.newInstance(data, offset);
        response.unitId = buff.getByteToInt(0);
        response.pdu = MbPdu.fromBytes(data, 1);
        response.lrc = buff.getByte(1 + response.pdu.byteArrayLength());
        return response;
    }
}
