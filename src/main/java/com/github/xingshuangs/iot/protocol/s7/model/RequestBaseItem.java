package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.ESyntaxID;
import lombok.Data;

/**
 * 请求基础项
 *
 * @author xingshuang
 */
@Data
public class RequestBaseItem implements IObjectByteArray {

    /**
     * 变量规范，对于读/写消息，它总是具有值0x12 <br>
     * 字节大小：1 <br>
     * 字节序数：0
     */
    protected byte specificationType = (byte) 0x12;

    /**
     * 其余部分的长度规范 <br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    protected int lengthOfFollowing = 0x0A;

    /**
     * 寻址模式和项结构其余部分的格式，它具有任意类型寻址的常量值0x10 <br>
     * 字节大小：1 <br>
     * 字节序数：2
     */
    protected ESyntaxID syntaxId = ESyntaxID.S7ANY;

    @Override
    public int byteArrayLength() {
        return 0;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
