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

package com.github.xingshuangs.iot.protocol.s7.enums;


/**
 * PLC类型
 *
 * @author xingshuang
 */
public enum EPlcType {
    S200(0, 1, 240),
    S200_SMART(0, 1, 240),
    S300(0, 2, 240),
    S400(0, 3, 480),
    S1200(0, 1, 240),
    S1500(0, 1, 960),
    SINUMERIK_828D(0, 1, 240),

    ;

    /**
     * PLC机架号
     */
    private final int rack;

    /**
     * PLC槽号
     */
    private final int slot;

    /**
     * PDU最大允许长度
     */
    private final int pduLength;

    EPlcType(int rack, int slot, int pduLength) {
        this.rack = rack;
        this.slot = slot;
        this.pduLength = pduLength;
    }

    public int getRack() {
        return rack;
    }

    public int getSlot() {
        return slot;
    }

    public int getPduLength() {
        return pduLength;
    }
}
