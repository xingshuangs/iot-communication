package com.github.xingshuangs.iot.protocol.rtcp.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 资源项的类型
 *
 * @author xingshuang
 */
public enum ERtcpSdesItemType {

    /**
     * end of SDES list
     */
    END((byte) 0x00),

    /**
     * canonical name
     */
    CNAME((byte) 0x01),

    /**
     * username
     */
    NAME((byte) 0x02),

    /**
     * user's electronic mail address
     */
    EMAIL((byte) 0x03),

    /**
     * user's phone number
     */
    PHONE((byte) 0x04),

    /**
     * geographic user location
     */
    LOC((byte) 0x05),

    /**
     * name of application or tool
     */
    TOOL((byte) 0x06),

    /**
     * notice about the source
     */
    NOTE((byte) 0x07),

    /**
     * private extension
     */
    PRIV((byte) 0x08),
    ;

    private static Map<Byte, ERtcpSdesItemType> map;

    public static ERtcpSdesItemType from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (ERtcpSdesItemType item : ERtcpSdesItemType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    ERtcpSdesItemType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
