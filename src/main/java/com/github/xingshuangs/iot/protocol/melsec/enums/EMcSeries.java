/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.protocol.melsec.enums;


import lombok.Getter;

/**
 * PLC系列
 *
 * @author xingshuang
 */
@Getter
public enum EMcSeries {
    /**
     * QnA系列
     */
    QnA(1, 3, 480, 3584,
            96, 960, 94,
            120, 4, 960),

    /**
     * MELSEC-Q/L系列
     */
    Q_L(1, 3, 960, 7168,
            192, 1920, 188,
            120, 4, 960),

    /**
     * MELSEC iQ-R系列
     */
    IQ_R(2, 4, 960, 7168,
            96, 960, 94,
            60, 9, 960),
    ;

    EMcSeries(int deviceCodeByteLength,
              int headDeviceNumberByteLength,
              int deviceBatchInWordPointsCount,
              int deviceBatchInBitPointsCount,
              int deviceRandomReadInWordPointsCount,
              int deviceRandomWriteInWordPointsCount,
              int deviceRandomWriteInBitPointsCount,
              int deviceBlocksBlocksCount,
              int deviceBlocksWritePointsSize,
              int deviceBlocksWritePointsCount) {
        this.deviceCodeByteLength = deviceCodeByteLength;
        this.headDeviceNumberByteLength = headDeviceNumberByteLength;
        this.deviceBatchInWordPointsCount = deviceBatchInWordPointsCount;
        this.deviceBatchInBitPointsCount = deviceBatchInBitPointsCount;
        this.deviceRandomReadInWordPointsCount = deviceRandomReadInWordPointsCount;
        this.deviceRandomWriteInWordPointsCount = deviceRandomWriteInWordPointsCount;
        this.deviceRandomWriteInBitPointsCount = deviceRandomWriteInBitPointsCount;
        this.deviceBlocksBlocksCount = deviceBlocksBlocksCount;
        this.deviceBlocksWritePointsSize = deviceBlocksWritePointsSize;
        this.deviceBlocksWritePointsCount = deviceBlocksWritePointsCount;
    }

    private final int deviceCodeByteLength;

    private final int headDeviceNumberByteLength;

    private final int deviceBatchInWordPointsCount;

    private final int deviceBatchInBitPointsCount;

    private final int deviceRandomReadInWordPointsCount;

    private final int deviceRandomWriteInWordPointsCount;

    private final int deviceRandomWriteInBitPointsCount;

    private final int deviceBlocksBlocksCount;

    private final int deviceBlocksWritePointsSize;

    private final int deviceBlocksWritePointsCount;


}
