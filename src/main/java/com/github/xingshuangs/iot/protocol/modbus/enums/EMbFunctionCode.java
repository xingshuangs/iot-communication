package com.github.xingshuangs.iot.protocol.modbus.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 功能码枚举
 *
 * @author xingshuang
 */
public enum EMbFunctionCode {

    /**
     * 读线圈
     */
    READ_COIL((byte) 0x01, "读线圈"),

    /**
     * 读离散量输入
     */
    READ_DISCRETE_INPUT((byte) 0x02, "读离散量输入"),

    /**
     * 读保持寄存器
     */
    READ_HOLD_REGISTER((byte) 0x03, "读保持寄存器"),

    /**
     * 读输入寄存器
     */
    READ_INPUT_REGISTER((byte) 0x04, "读输入寄存器"),

    /**
     * 写单个线圈
     */
    WRITE_SINGLE_COIL((byte) 0x05, "写单个线圈"),

    /**
     * 写单个寄存器
     */
    WRITE_SINGLE_REGISTER((byte) 0x06, "写单个寄存器"),

    /**
     * 写多个线圈
     */
    WRITE_MULTIPLE_COIL((byte) 0x0F, "写多个线圈"),

    /**
     * 写多个寄存器
     */
    WRITE_MULTIPLE_REGISTER((byte) 0x10, "写多个寄存器"),

    /**
     * 读文件记录
     */
    READ_DOCUMENT_RECORD((byte) 0x14, "读文件记录"),

    /**
     * 屏蔽写寄存器
     */
    SHIELD_WRITE_REGISTER((byte) 0x16, "屏蔽写寄存器"),

    /**
     * 读/写多个寄存器
     */
    READ_WRITE_MULTIPLE_REGISTER((byte) 0x17, "读/写多个寄存器"),

    /**
     * 读设备识别码
     */
    READ_DEVICE_IDENTIFICATION_CODE((byte) 0x2B, "读设备识别码"),

    /**
     * 读线圈
     */
    ERROR_READ_COIL((byte) 0x81, "读线圈错误"),

    /**
     * 读离散量输入
     */
    ERROR_READ_DISCRETE_INPUT((byte) 0x82, "读离散量输入错误"),

    /**
     * 读保持寄存器
     */
    ERROR_READ_HOLD_REGISTER((byte) 0x83, "读保持寄存器错误"),

    /**
     * 读输入寄存器
     */
    ERROR_READ_INPUT_REGISTER((byte) 0x84, "读输入寄存器错误"),

    /**
     * 写单个线圈
     */
    ERROR_WRITE_SINGLE_COIL((byte) 0x85, "写单个线圈错误"),

    /**
     * 写单个寄存器
     */
    ERROR_WRITE_SINGLE_REGISTER((byte) 0x86, "写单个寄存器错误"),

    /**
     * 写多个线圈
     */
    ERROR_WRITE_MULTIPLE_COIL((byte) 0x8F, "写多个线圈错误"),

    /**
     * 写多个寄存器
     */
    ERROR_WRITE_MULTIPLE_REGISTER((byte) 0x90, "写多个寄存器错误"),

    /**
     * 读文件记录
     */
    ERROR_READ_DOCUMENT_RECORD((byte) 0x94, "读文件记录错误"),

    /**
     * 屏蔽写寄存器
     */
    ERROR_SHIELD_WRITE_REGISTER((byte) 0x96, "屏蔽写寄存器错误"),

    /**
     * 读/写多个寄存器
     */
    ERROR_READ_WRITE_MULTIPLE_REGISTER((byte) 0x97, "读/写多个寄存器错误"),
    ;

    private static Map<Byte, EMbFunctionCode> map;

    public static EMbFunctionCode from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (EMbFunctionCode item : EMbFunctionCode.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    private final String description;

    EMbFunctionCode(byte code, String description) {
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
