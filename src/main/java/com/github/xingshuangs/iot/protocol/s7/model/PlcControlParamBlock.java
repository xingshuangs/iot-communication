package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;

/**
 * PLC控制参数块
 *
 * @author xingshuang
 */
public class PlcControlParamBlock implements IObjectByteArray {

    @Override
    public int byteArrayLength() {
        return 0;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
