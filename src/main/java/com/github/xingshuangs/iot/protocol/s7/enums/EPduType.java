package com.github.xingshuangs.iot.protocol.s7.enums;


/**
 * PDU类型（CRConnect Request 连接请求）
 *
 * @author xingshuang
 */
public enum EPduType {

    /**
     * 连接请求
     */
    CONNECT_REQUEST((byte)0x0E),

    /**
     * 连接确认
     */
    CONNECT_CONFIRM((byte)0x0D),

    /**
     * 断开请求
     */
    DISCONNECT_REQUEST((byte)0x08),

    /**
     * 断开确认
     */
    DISCONNECT_CONFIRM((byte)0x0C),

    /**
     * 拒绝
     */
    REJECT((byte)0x05),

    /**
     * 数据
     */
    DT_DATA((byte)0x0f),
    ;

    private byte code;

    private EPduType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
