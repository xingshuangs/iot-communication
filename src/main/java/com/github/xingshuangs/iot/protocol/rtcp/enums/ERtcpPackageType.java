package com.github.xingshuangs.iot.protocol.rtcp.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 数据包的类型
 *
 * @author xingshuang
 */
public enum ERtcpPackageType {

    /**
     * 发送端报告,200
     */
    SR((byte) 0xC8),

    /**
     * 接收端报告,201
     */
    RR((byte) 0xC9),

    /**
     * 源点描述,202
     */
    SDES((byte) 0xCA),

    /**
     * 结束传输,203
     */
    BYE((byte) 0xCB),

    /**
     * 特定应用,204
     */
    APP((byte) 0xCE),
    ;

    private static final Map<Byte, ERtcpPackageType> map = new HashMap<>();

    static {
        for (ERtcpPackageType item : ERtcpPackageType.values()) {
            map.put(item.code, item);
        }
    }

    public static ERtcpPackageType from(byte data) {
        return map.get(data);
    }

    private final byte code;

    ERtcpPackageType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
