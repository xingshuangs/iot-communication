package com.github.xingshuangs.iot.protocol.s7.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 数据返回Transport size in data Transport size (variable Type) 变量的类型和长度
 *
 * @author xingshuang
 */
public enum EDataVariableType {

    /**
     * 无
     */
    NULL((byte) 0x00),

    /**
     * bit access, len is in bits
     */
    BIT((byte) 0x03),

    /**
     * byte/word/dword access, len is in bits
     */
    BYTE_WORD_DWORD((byte) 0x04),

    /**
     * integer access, len is in bits
     */
    INTEGER((byte) 0x05),

    /**
     * integer access, len is in bytes
     */
    DINTEGER((byte) 0x06),

    /**
     * real access, len is in bytes
     */
    REAL((byte) 0x07),

    /**
     * octet string, len is in bytes
     */
    OCTET_STRING((byte) 0x09),
    ;

    private static final Map<Byte, EDataVariableType> map = new HashMap<>();

    static {
        for (EDataVariableType item : EDataVariableType.values()) {
            map.put(item.code, item);
        }
    }

    public static EDataVariableType from(byte data) {
        return map.get(data);
    }

    private final byte code;

    EDataVariableType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
