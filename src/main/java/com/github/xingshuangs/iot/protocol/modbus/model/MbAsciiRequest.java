package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.exceptions.ModbusCommException;
import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.utils.LRCUtil;
import lombok.Data;

/**
 * ASCII的modbus请求
 *
 * @author xingshuang
 */
@Data
public class MbAsciiRequest implements IObjectByteArray {

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

    public MbAsciiRequest() {
    }

    public MbAsciiRequest(int unitId, MbPdu pdu) {
        this.unitId = unitId;
        this.pdu = pdu;
        this.selfCheck();
    }

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
}
