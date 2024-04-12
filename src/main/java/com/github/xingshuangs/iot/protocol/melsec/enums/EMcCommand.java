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


import java.util.HashMap;
import java.util.Map;

/**
 * 指令
 *
 * @author xingshuang
 */
public enum EMcCommand {

    //region 1E帧
    /**
     * 软元件访问，批量读取，位单位，适用于1E帧
     */
    DEVICE_ACCESS_BATCH_READ_IN_BIT(0x00),

    /**
     * 软元件访问，批量读取，字单位，适用于1E帧
     */
    DEVICE_ACCESS_BATCH_READ_IN_WORD(0x01),

    /**
     * 软元件访问，批量写入，位单位，适用于1E帧
     */
    DEVICE_ACCESS_BATCH_WRITE_IN_BIT(0x02),

    /**
     * 软元件访问，批量写入，字单位，适用于1E帧
     */
    DEVICE_ACCESS_BATCH_WRITE_IN_WORD(0x03),

    /**
     * 软元件访问，随机写入，位单位，适用于1E帧
     */
    DEVICE_ACCESS_RANDOM_WRITE_IN_BIT(0X04),

    /**
     * 软元件访问，随机写入，字单位，适用于1E帧
     */
    DEVICE_ACCESS_RANDOM_WRITE_IN_WORD(0X05),
    //endregion

    //region 3E帧和4E帧

    /**
     * 软元件访问，批量读取
     */
    DEVICE_ACCESS_BATCH_READ_IN_UNITS(0x0401),

    /**
     * 软元件访问，批量写入
     */
    DEVICE_ACCESS_BATCH_WRITE_IN_UNITS(0x1401),

    /**
     * 软元件访问，随机读取
     */
    DEVICE_ACCESS_RANDOM_READ_IN_UNITS(0x0403),

    /**
     * 软元件访问，随机写入
     */
    DEVICE_ACCESS_RANDOM_WRITE_IN_UNITS(0X1402),

    /**
     * 软元件访问，多个块批量读取
     */
    DEVICE_ACCESS_BATCH_READ_MULTIPLE_BLOCKS(0x0406),

    /**
     * 软元件访问，多个块批量写入
     */
    DEVICE_ACCESS_BATCH_WRITE_MULTIPLE_BLOCKS(0x1406),

    /**
     * 软元件访问，监视数据登录
     */
    DEVICE_ACCESS_REGISTER_MONITOR_DATA(0x0801),

    /**
     * 软元件访问，监视
     */
    DEVICE_ACCESS_MONITOR(0x0802),

    /**
     * 标签访问，排列型标签的批量读取
     */
    LABEL_ACCESS_BATCH_READ_ARRAY_TYPE_LABELS(0x041A),

    /**
     * 标签访问，排列型标签的批量写入
     */
    LABEL_ACCESS_BATCH_WRITE_ARRAY_TYPE_LABELS(0x141A),

    /**
     * 标签访问，标签的随机读取
     */
    LABEL_ACCESS_RANDOM_READ_LABELS(0x041C),

    /**
     * 标签访问，标签的随机写入
     */
    LABEL_ACCESS_RANDOM_WRITE_LABELS(0x141B),

    /**
     * 缓冲存储器访问，缓冲存储器批量读取
     */
    BUFFER_MEMORY_ACCESS_BATCH_READ(0x0613),

    /**
     * 缓冲存储器访问，缓冲存储器批量写入
     */
    BUFFER_MEMORY_ACCESS_BATCH_WRITE(0x1613),

    /**
     * 缓冲存储器访问，智能功能模块批量读取
     */
    BUFFER_MEMORY_ACCESS_INTELLIGENT_FUNCTION_MODULE_BATCH_READ(0x0601),

    /**
     * 缓冲存储器访问，智能功能模块批量写入
     */
    BUFFER_MEMORY_ACCESS_INTELLIGENT_FUNCTION_MODULE_BATCH_WRITE(0x1601),

    /**
     * 模块控制，远程RUN
     */
    MODULE_CONTROL_REMOTE_RUN(0x1001),

    /**
     * 模块控制，远程STOP
     */
    MODULE_CONTROL_REMOTE_STOP(0x1002),

    /**
     * 模块控制，远程PAUSE
     */
    MODULE_CONTROL_REMOTE_PAUSE(0x1003),

    /**
     * 模块控制，远程锁存清除
     */
    MODULE_CONTROL_REMOTE_LATCH_CLEAR(0x1005),

    /**
     * 模块控制，远程RESET
     */
    MODULE_CONTROL_REMOTE_RESET(0x1006),

    /**
     * 模块控制，CPU型号读取
     */
    MODULE_CONTROL_READ_CPU_MODEL_NAME(0x0101),

    /**
     * 模块控制，远程口令解锁
     */
    MODULE_CONTROL_PASSWORD_UNLOCK(0x1630),

    /**
     * 模块控制，远程口令锁定
     */
    MODULE_CONTROL_PASSWORD_LOCK(0x1631),

    /**
     * 模块控制，反复测试
     */
    MODULE_CONTROL_LOOPBACK_TEST(0x0619),

    /**
     * 模块控制，出错信息的清除
     */
    MODULE_CONTROL_CLEAR_ERROR_INFORMATION(0x1617),
    //endregion
    ;

    private static Map<Integer, EMcCommand> map;

    public static EMcCommand from(int data) {
        if (map == null) {
            map = new HashMap<>();
            for (EMcCommand item : EMcCommand.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final int code;

    EMcCommand(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
