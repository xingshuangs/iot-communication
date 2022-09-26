package com.github.xingshuangs.iot.protocol.common.buff;


/**
 * 字节缓存格式
 *
 * @author xingshuang
 */
public enum EByteBuffFormat {
    /**
     * 按照原始顺序排列
     */
    AB_CD,

    /**
     * 按照单字节反转
     */
    BA_DC,

    /**
     * 按照双字节反转
     */
    CD_AB,

    /**
     * 按照倒序排列
     */
    DC_BA
}
