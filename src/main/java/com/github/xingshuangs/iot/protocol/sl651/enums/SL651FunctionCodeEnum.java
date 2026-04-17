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
package com.github.xingshuangs.iot.protocol.sl651.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * SL651帧功能码枚举
 *
 * @author wangqingpan
 */
@Getter
public enum SL651FunctionCodeEnum {

    HEART_BEAT(new byte[]{0x2F}, "链路维持"),
    TEST_REPORT(new byte[]{0x30}, "查询上报"),
    HYDROLOGICAL_REPORT(new byte[]{0x31}, "均匀时段水文信息上报"),
    REGULAR_REPORT(new byte[]{0x32}, "定时上报"),
    URGENT_REPORT(new byte[]{0x33}, "加报上报"),
    HOUR_REPORT(new byte[]{0x34}, "小时上报"),
    MANUAL_SET_DATA(new byte[]{0x35}, "人工置数"),
    IMAGE_REPORT(new byte[]{0x36}, "图片报"),
    REAL_TIME_DATA(new byte[]{0x37}, "实时数据"),
    QUERY_PERIOD_DATA(new byte[]{0x38}, "查询时段数据"),
    QUERY_MANUAL_SET_DATA(new byte[]{0x39}, "查询人工置数"),
    QUERY_SPECIFIED_ELEMENT_REAL_TIME_DATA(new byte[]{0x3A}, "查询指定要素实时数据"),
    MODIFY_STATION_BASIC_CONFIG(new byte[]{0x40}, "修改遥测站基本配置"),
    READ_STATION_BASIC_CONFIG(new byte[]{0x41}, "读取遥测站基本配置"),
    MODIFY_STATION_OPERATION_PARAM_CONFIG(new byte[]{0x42}, "修改遥测站运行参数配置"),
    READ_STATION_OPERATION_PARAM_CONFIG(new byte[]{0x43}, "读取遥测站运行参数配置"),
    QUERY_PUMP_MOTOR_REAL_TIME_WORK_DATA(new byte[]{0x44}, "查询水泵电机实时工作数据"),
    QUERY_STATION_SOFTWARE_VERSION(new byte[]{0x45}, "查询遥测站软件版本"),
    QUERY_STATION_STATUS_AND_ALARM_INFO(new byte[]{0x46}, "查询遥测站状态和报警信息"),
    INITIALIZE_SOLID_STATE_STORAGE_DATA(new byte[]{0x47}, "初始化固态存储数据"),
    RESTORE_STATION_FACTORY_SETTINGS(new byte[]{0x48}, "恢复遥测站出厂设置"),
    MODIFY_PASSWORD(new byte[]{0x49}, "修改密码"),
    SET_STATION_CLOCK(new byte[]{0x4A}, "设置遥测站时钟"),
    SET_STATION_IC_CARD_STATUS(new byte[]{0x4B}, "设置遥测站IC卡状态"),
    CONTROL_PUMP_SWITCH_COMMAND(new byte[]{0x4C}, "控制水泵开关命令"),
    CONTROL_VALVE_SWITCH_COMMAND(new byte[]{0x4D}, "控制阀门开关命令"),
    CONTROL_GATE_SWITCH_COMMAND(new byte[]{0x4E}, "控制闸门开关命令"),
    WATER_QUANTITY_FIXED_VALUE_CONTROL_COMMAND(new byte[]{0x4F}, "水量定值控制命令"),
    QUERY_STATION_EVENT_RECORD(new byte[]{0x50}, "查询遥测站事件记录"),
    QUERY_STATION_CLOCK(new byte[]{0x51}, "查询遥测站时钟");

    /**
     * 功能码
     */
    private byte[] code;

    /**
     * 功能码描述
     */
    private String desc;

    SL651FunctionCodeEnum(byte[] code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据功能码获取枚举
     *
     * @param code 功能码
     * @return 枚举
     */
    public static SL651FunctionCodeEnum getSL651FunctionCodeEnum(byte[] code) {
        for (SL651FunctionCodeEnum funCodeEnum : SL651FunctionCodeEnum.values()) {
            if (Arrays.equals(funCodeEnum.code, code)) {
                return funCodeEnum;
            }
        }
        return null;
    }

}

