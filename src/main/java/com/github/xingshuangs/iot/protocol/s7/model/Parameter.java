package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import lombok.Data;

/**
 * 参数
 *
 * @author xingshuang
 */
@Data
public class Parameter implements IByteArray {

    /**
     * 功能码 <br>
     * 字节大小：1 <br>
     * 字节序数：0
     */
    private EFunctionCode functionCode = EFunctionCode.READ_VARIABLE;

    @Override
    public int getByteArrayLength() {
        return 1;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[]{this.functionCode.getCode()};
    }
}
