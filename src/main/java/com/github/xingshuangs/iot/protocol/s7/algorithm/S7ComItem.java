package com.github.xingshuangs.iot.protocol.s7.algorithm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 合并项
 *
 * @author xingshuang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class S7ComItem {

    /**
     * 数据索引
     */
    private int index;

    /**
     * 原始数据
     */
    private int rawData;

    /**
     * 分割点
     */
    private int splitOffset;

    /**
     * 分割后的数据
     */
    private int ripeData;

    /**
     * 额外需要的数据
     */
    private int extraData;

    /**
     * 整个长度
     *
     * @return 整个长度
     */
    public int getTotalLength() {
        return this.ripeData + this.extraData;
    }
}
