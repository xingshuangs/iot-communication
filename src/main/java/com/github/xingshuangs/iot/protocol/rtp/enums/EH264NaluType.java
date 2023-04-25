package com.github.xingshuangs.iot.protocol.rtp.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * H264的Nalu类别
 *
 * @author xingshuang
 */
public enum EH264NaluType {

    /**
     * 未指定
     */
    NONE(0),

    /**
     * 非 IDR 图像的片
     */
    NON_IDR(1),

    /**
     * IDR 图像的片
     */
    IDR(5),

    /**
     * SEI（辅助增强信息）
     */
    SEI(6),

    /**
     * SPS（序列参数集）
     */
    SPS(7),

    /**
     * PPS（图像参数集）
     */
    PPS(8),

    /**
     * STAP-A（单一时间组合包模式 A，用于一个 RTP 包荷载多个 NALU）
     */
    STAP_A(24),

    /**
     * STAP-B（单一时间组合包模式 B）
     */
    STAP_B(25),

    /**
     * MTAP16（多个时间的组合包模式 A）
     */
    MTAP16(26),

    /**
     * MTAP24（多个时间的组合包模式 B）
     */
    MTAP24(27),

    /**
     * FU-A（分片模式 A，用于将单个 NALU 分到多个 RTP 包）
     */
    FU_A(28),

    /**
     * FU-B（分片模式 B）
     */
    FU_B(29),

    ;

    private static Map<Integer, EH264NaluType> map;

    public static EH264NaluType from(int data) {
        if (map == null) {
            map = new HashMap<>();
            for (EH264NaluType item : EH264NaluType.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final int code;

    EH264NaluType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
