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

package com.github.xingshuangs.iot.protocol.rtsp.constant;


/**
 * key名
 *
 * @author xingshuang
 */
public class RtspCommonKey {

    private RtspCommonKey() {
        // NOOP
    }

    /**
     * 换行
     */
//    public static final String CRLF = System.getProperty("line.separator");
    public static final String CRLF = "\r\n";

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
     * 分号
     */
    public static final String SEMICOLON = ";";

    /**
     * 等号
     */
    public static final String EQUAL = "=";

    /**
     * 序列号
     */
    public static final String C_SEQ = "CSeq";

    /**
     * 序列号
     */
    public static final String C_SEQ1 = "Cseq";

    /**
     * 会话
     */
    public static final String SESSION = "Session";

    /**
     * 通道
     */
    public static final String TRANSPORT = "Transport";

}
