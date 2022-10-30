package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * 参数
 *
 * @author xingshuang
 */
@Data
public class Parameter implements IObjectByteArray {

    /**
     * 功能码 <br>
     * 字节大小：1 <br>
     * 字节序数：0
     */
    protected EFunctionCode functionCode = EFunctionCode.READ_VARIABLE;

    public Parameter() {
    }

    public Parameter(EFunctionCode functionCode) {
        this.functionCode = functionCode;
    }

    @Override
    public int byteArrayLength() {
        return 1;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(1)
                .putByte(this.functionCode.getCode())
                .getData();
    }
}
