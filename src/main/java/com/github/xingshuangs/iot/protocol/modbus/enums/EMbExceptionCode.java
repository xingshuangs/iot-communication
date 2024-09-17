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

package com.github.xingshuangs.iot.protocol.modbus.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * Exception code enum.
 * 异常码枚举
 *
 * @author xingshuang
 */
public enum EMbExceptionCode {

    /**
     * Illegal function.
     * 非法功能码<br>
     * 对于服务器(或从站)来说，询问中接收到的功能码是不可允许的操作。这也许
     * 是因为功能码仅仅适用于新设备而在被选单元中是不可实现的。同时，还指出
     * 服务器(或从站)在错误状态中处理这种请求，例如：因为它是未配置的，并且
     * 要求返回寄存器值。
     */
    ILLEGAL_FUNCTION((byte) 0x01, "illegal function"),

    /**
     * Illegal data address.
     * 非法数据地址<br>
     * 对于服务器(或从站)来说，询问中接收到的数据地址是不可允许的地址。特别
     * 是，参考号和传输长度的组合是无效的。对于带有 100 个寄存器的控制器来说，
     * 带有偏移量 96 和长度 4 的请求会成功，带有偏移量 96 和长度 5 的请求将产生
     * 异常码 02。
     */
    ILLEGAL_DATA_ADDRESS((byte) 0x02, "illegal data address"),

    /**
     * Illegal data value.
     * 非法数据值<br>
     * 对于服务器(或从站)来说，询问中包括的值是不可允许的值。这个值指示了组
     * 合请求剩余结构中的故障，例如：隐含长度是不正确的。并不意味着，因为
     * MODBUS 协议不知道任何特殊寄存器的任何特殊值的重要意义，寄存器中被
     * 提交存储的数据项有一个应用程序期望之外的值。
     */
    ILLEGAL_DATA_VALUE((byte) 0x03, "illegal data value"),

    /**
     * Slave device failure.
     * 从站设备失败<br>
     * 当服务器(或从站)正在设法执行请求的操作时，产生不可重新获得的差错。
     */
    SLAVE_DEVICE_FAILURE((byte) 0x04, "slave device failure"),

    /**
     * Acknowledge
     * 确认<br>
     * 与编程命令一起使用。服务器(或从站)已经接受请求，并切正在处理这个请求，
     * 但是需要长的持续时间进行这些操作。返回这个响应防止在客户机(或主站)中
     * 发生超时错误。客户机(或主站)可以继续发送轮询程序完成报文来确定是否完
     * 成处理。
     */
    ACKNOWLEDGE((byte) 0x05, "acknowledge"),

    /**
     * Slave device busy.
     * 从属设备忙<br>
     * 与编程命令一起使用。服务器(或从站)正在处理长持续时间的程序命令。张服
     * 务器(或从站)空闲时，用户(或主站)应该稍后重新传输报文
     */
    SLAVE_DEVICE_BUSY((byte) 0x06, "slave device busy"),

    /**
     * Memory parity error.
     * 存储奇偶性差错<br>
     * 与功能码 20 和 21 以及参考类型 6 一起使用，指示扩展文件区不能通过一致性
     * 校验。
     * 服务器(或从站)设法读取记录文件，但是在存储器中发现一个奇偶校验错误。
     * 客户机(或主方)可以重新发送请求，但可以在服务器(或从站)设备上要求服务。
     */
    MEMORY_PARITY_ERROR((byte) 0x08, "memory parity error"),

    /**
     * Gateway path unavailable.
     * 不可用网关路径<br>
     * 与网关一起使用，指示网关不能为处理请求分配输入端口至输出端口的内部通
     * 信路径。通常意味着网关是错误配置的或过载的。
     */
    GATEWAY_PATH_UNAVAILABLE((byte) 0x0A, "gateway path unavailable"),

    /**
     * Gateway target device failed to respond.
     * 网关目标设备响应失败<br>
     * 与网关一起使用，指示没有从目标设备中获得响应。通常意味着设备未在网络
     * 中。
     */
    GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND((byte) 0x0B, "gateway target device failed to respond"),

    ;

    private static Map<Byte, EMbExceptionCode> map;

    public static EMbExceptionCode from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EMbExceptionCode item : EMbExceptionCode.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    private final String description;

    EMbExceptionCode(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
