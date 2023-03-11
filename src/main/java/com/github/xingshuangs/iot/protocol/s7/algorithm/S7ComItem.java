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
     * 原始数据大小
     */
    private int rawSize;

    /**
     * 分割点，即数据偏移索引
     */
    private int splitOffset;

    /**
     * 分割后的数据大小
     */
    private int ripeSize;

    /**
     * 额外需要的数据大小
     */
    private int extraSize;

    /**
     * 阀值
     */
    private int threshold = 0;

    /**
     * 整个长度
     *
     * @return 整个长度
     */
    public int getTotalLength() {
        if (this.ripeSize + this.extraSize > this.threshold) {
            return this.ripeSize + this.extraSize;
        } else {
            return this.threshold;
        }
    }
}
