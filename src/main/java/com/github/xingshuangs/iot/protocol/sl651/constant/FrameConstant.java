/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>, 王清攀
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
package com.github.xingshuangs.iot.protocol.sl651.constant;

/**
 * SL651-2014 通用常量
 *
 * @author wangqingpan
 */
public class FrameConstant {

    /**
     * 帧起始符
     * 字节大小: 2个字节
     */
    public static final byte[] HEADER_START_HEX = new byte[]{0x7E, 0x7E};

    /**
     * M1/M2/M4 模式传输正文起始标记STX
     * 字节大小: 1个字节
     */
    public static final byte[] HEADER_RSP_BODY_END_STX_HEX = new byte[]{0x02};

    /**
     * M3 模式报文起始符SYN
     * 字节大小: 1个字节
     */
    public static final byte[] HEADER_START_BODY_M3_FLAG_HEX = new byte[]{0x16};

    /**
     * 报文控制符EOT，传输结束退出通信
     * 字节大小: 1个字节
     * 结束符：
     * 03  表示传输完成，等待退出通信
     * 17  表示传输未完成，不可退出通信
     * 05  作为下行查询及控制命令帧的报文结束符
     * 04  表示可以退出通信
     * 06  作为有后续报文帧的“确认帧”报文结束符
     * 15  用于要求对方重发某数据包的报文结束符
     * 1B  要求终端在线。保持在线 10 分钟内若没有接收到中心站命令，终端退回原先设定的工作状态
     */
    public static final byte[] HEADER_RSP_BODY_END_EOT_HEX = new byte[]{0x1B};

    /**
     * 下行报文标识
     * 字节大小: 1个字节
     * 高4位用作上下行标识：0000表示上行，1000表示下行
     * 低12位表示报文正文长度（0001～4095）
     * 将长度转换成12位的二进制，在拼上前四位1000，转成十六进制后前8位 10000000 = 0x80
     */
    public static final byte[] HEADER_RSP_DOWN_SYMBOL_AND_ZERO_LEN_HEX = new byte[]{(byte) 0x80};
}
