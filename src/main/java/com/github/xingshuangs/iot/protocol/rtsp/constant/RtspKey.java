package com.github.xingshuangs.iot.protocol.rtsp.constant;


/**
 * key名
 *
 * @author xingshuang
 */
public class RtspKey {

    private RtspKey() {
        // NOOP
    }

    /**
     * 换行
     */
    public static final String CRLF = System.getProperty("line.separator");

    /**
     * 冒号
     */
    public static final String COLON = ":";

    /**
     * 逗号
     */
    public static final String COMMA = ",";

    /**
     * 空格
     */
    public static final String SP = " ";

    /**
     * 序列号
     */
    public static final String C_SEQ = "CSeq";

}
