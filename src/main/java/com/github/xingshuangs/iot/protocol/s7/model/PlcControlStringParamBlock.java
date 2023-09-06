package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * PLC控制参数块，字符串格式
 *
 * @author xingshuang
 */
@Data
public class PlcControlStringParamBlock extends PlcControlParamBlock {

    private String paramBlock = "";

    public PlcControlStringParamBlock() {
    }

    public PlcControlStringParamBlock(String paramBlock) {
        this.paramBlock = paramBlock;
    }

    @Override
    public int byteArrayLength() {
        return this.paramBlock.length();
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.paramBlock.length())
                .putString(this.paramBlock)
                .getData();
    }
}
