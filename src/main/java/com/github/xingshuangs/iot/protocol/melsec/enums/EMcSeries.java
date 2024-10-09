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
 * PLC系列<br>
 * 通信帧命名规格<br>
 * 通信帧命名格式如下：<br>
 * xxx 兼容 n m 帧(示例: QnA 兼容 3C 帧、QnA 兼容 3E 帧)<br>
 * 1、xxx 用于表示与以前产品模块的指令兼容性的对象可编程控制器 CPU<br>
 * A : A 系列可编程控制器 CPU<br>
 * QnA : QnA 系列可编程控制器 CPU<br>
 * 2、n对应的以前产品模块的帧<br>
 * 1 : 兼容 A 系列的计算机链接模块、以太网接口模块支持的指令的通信帧<br>
 * 2 : 兼容 QnA 系列串行通信模块支持的 QnA 简易帧<br>
 * 3 : QnA 系列串行通信模块支持的 QnA 帧及兼容 QnA 系列以太网接口模块支持的通信帧<br>
 * 4 : 兼容 QnA 系列串行通信模块支持的 QnA 扩展帧<br>
 * 3、m是指相应帧进行数据通信的对象模块<br>
 * C : C24<br>
 * E : E71<br>
 * <p>
 * 通信方式<br>
 * 一般我们使用比较多的是以太网通信，<br>
 * 对于FX5U系列/Q系列/Qna系列/L系列的PLC，通常会使用QnA兼容3E帧，<br>
 * 对于FX3U系列，我们需要加以太网模块，采用A兼容1E帧。<br>
 * 对于串口设备，一般会使用QnA兼容2C帧和QnA兼容4C帧。<br>
 *
 * @author xingshuang
 */
@Getter
public enum EMcSeries {

    /**
     * A series.
     * A系列，批量读：位单位256点，字单位32个；批量写：位单位160点，字单位10个
     */
    A(EMcFrameType.FRAME_1E, 2, 4, 10, 160,
            0, 10, 80,
            0, 0, 0),

    /**
     * QnA series
     */
    QnA(EMcFrameType.FRAME_3E, 1, 3, 480, 3584,
            96, 960, 94,
            120, 4, 960),

    /**
     * MELSEC-Q/L series
     */
    Q_L(EMcFrameType.FRAME_3E, 1, 3, 960, 7168,
            192, 1920, 188,
            120, 4, 960),

    /**
     * MELSEC iQ-R series
     * TODO: 不是很确定IQ-R是否为3E帧类型
     */
    IQ_R(EMcFrameType.FRAME_3E, 2, 4, 960, 7168,
            96, 960, 94,
            60, 9, 960),
    ;

    EMcSeries(EMcFrameType frameType,
              int deviceCodeByteLength,
              int headDeviceNumberByteLength,
              int deviceBatchInWordPointsCount,
              int deviceBatchInBitPointsCount,
              int deviceRandomReadInWordPointsCount,
              int deviceRandomWriteInWordPointsCount,
              int deviceRandomWriteInBitPointsCount,
              int deviceBlocksBlocksCount,
              int deviceBlocksWritePointsSize,
              int deviceBlocksWritePointsCount) {
        this.frameType = frameType;
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

    /**
     * Frame type.
     * (对应帧类型)
     */
    private final EMcFrameType frameType;

    /**
     * Device code byte length.
     * (软元件代码字节长度)
     */
    private final int deviceCodeByteLength;

    /**
     * head device number byte length.
     * (起始软元件编号字节长度)
     */
    private final int headDeviceNumberByteLength;

    /**
     * device batch in word points count.
     * (软元件按字批量读写的点数)
     */
    private final int deviceBatchInWordPointsCount;

    /**
     * Device batch in bit points count.
     * (软元件按位批量读写的点数)
     */
    private final int deviceBatchInBitPointsCount;

    /**
     * Device random read in word points count.
     * (软元件按字随机读的点数)
     */
    private final int deviceRandomReadInWordPointsCount;

    /**
     * Device random write in word points count.
     * (软元件按字随机写的点数)
     */
    private final int deviceRandomWriteInWordPointsCount;

    /**
     * Device random write in bit points count.
     * (软元件按位随机写的点数)
     */
    private final int deviceRandomWriteInBitPointsCount;

    /**
     * Device blocks count.
     * (软元件按块批量读写的块数)
     */
    private final int deviceBlocksBlocksCount;

    /**
     * Device blocks write points size.
     * (软元件按块批量写的点大小)
     */
    private final int deviceBlocksWritePointsSize;

    /**
     * Device blocks write points count.
     * (软元件按块批量写的点数)
     */
    private final int deviceBlocksWritePointsCount;
}
