package com.github.xingshuangs.iot.protocol.melsec.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 软元件代码  MELSEC-Q/L系列用
 *
 * @author xingshuang
 */
public enum EMcDeviceCodeQL {
    /**
     * 特殊继电器
     */
    SM("SM", 10, "SM", (byte) 0x91),

    /**
     * 特殊寄存器
     */
    SD("SD", 10, "SD", (byte) 0xA9),

    /**
     * 输入
     */
    X("X", 16, "X*", (byte) 0x9C),

    /**
     * 输出
     */
    Y("Y", 16, "Y*", (byte) 0x9D),

    /**
     * 内部继电器
     */
    M("M", 10, "M*", (byte) 0x90),

    /**
     * 锁存继电器
     */
    L("L", 10, "L*", (byte) 0x92),

    /**
     * 报警器
     */
    F("F", 10, "F*", (byte) 0x93),

    /**
     * 变址继电器
     */
    V("V", 10, "V*", (byte) 0x94),

    /**
     * 链接继电器
     */
    B("B", 16, "B*", (byte) 0xA0),

    /**
     * 数据寄存器
     */
    D("D", 10, "D*", (byte) 0xA8),

    /**
     * 链接寄存器
     */
    W("W", 16, "W*", (byte) 0xB4),

    /**
     * 定时器触点
     */
    TS("TS", 10, "TS", (byte) 0xC1),

    /**
     * 定时器线圈
     */
    TC("TC", 10, "TC", (byte) 0xC0),

    /**
     * 定时器当前值
     */
    TN("TN", 10, "TN", (byte) 0xC2),
//    LTS("LTC", 10, "", (byte) 0x00),
//    LTC("LTC", 10, "", (byte) 0x00),
//    LTN("LTN", 10, "", (byte) 0x00),

    /**
     * 累计定时器触点
     */
    STS("STS", 10, "SS", (byte) 0xC7),

    /**
     * 累计定时器线圈
     */
    STC("STC", 10, "SC", (byte) 0xC6),

    /**
     * 累计定时器当前值
     */
    STN("STN", 10, "SN", (byte) 0xC8),
//    LSTS("LSTS", 10, "", (byte) 0x00),
//    LSTC("LSTC", 10, "", (byte) 0x00),
//    LSTN("LSTN", 10, "", (byte) 0x00),

    /**
     * 计数器触点
     */
    CS("CS", 10, "CS", (byte) 0xC4),

    /**
     * 计数器线圈
     */
    CC("CC", 10, "CC", (byte) 0xC3),

    /**
     * 计数器当前值
     */
    CN("CN", 10, "CN", (byte) 0xC5),
//    LCS("LCS", 10, "", (byte) 0x00),
//    LCC("LCC", 10, "", (byte) 0x00),
//    LCN("LCN", 10, "", (byte) 0x00),

    /**
     * 链接特殊继电器
     */
    SB("SB", 16, "SB", (byte) 0xA1),

    /**
     * 链接特殊寄存器
     */
    SW("SW", 16, "SW", (byte) 0xB5),

    /**
     * 直接访问输入
     */
    DX("DX", 16, "DX", (byte) 0xA2),

    /**
     * 直接访问输出
     */
    DY("DY", 16, "DY", (byte) 0xA3),

    /**
     * 变址寄存器
     */
    Z("Z", 10, "Z*", (byte) 0xCC),
//    LZ("LZ", 10, "", (byte) 0x00),

    /**
     * 文件寄存器 块切换方式
     */
    R("R", 10, "R*", (byte) 0xAF),

    /**
     * 文件寄存器 连号访问方式
     */
    ZR("ZR", 16, "ZR", (byte) 0xB0),
//    RD("RD", 10, "", (byte) 0x00),
    ;

    private static Map<String, EMcDeviceCodeQL> map;

    public static EMcDeviceCodeQL from(String data) {
        if (map == null) {
            map = new HashMap<>();
            for (EMcDeviceCodeQL item : EMcDeviceCodeQL.values()) {
                map.put(item.symbol, item);
            }
        }
        return map.get(data);
    }

    /**
     * 符号
     */
    private final String symbol;

    /**
     * 表记，10进制和16进制
     */
    private final int notation;

    /**
     * 二进制参数，1个字节
     */
    private final byte binaryCode;

    /**
     * ascii参数
     */
    private final String asciiCode;

    EMcDeviceCodeQL(String symbol, int notation, String asciiCode, byte binaryCode) {
        this.symbol = symbol;
        this.notation = notation;
        this.asciiCode = asciiCode;
        this.binaryCode = binaryCode;
    }

    public byte getBinaryCode() {
        return binaryCode;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getNotation() {
        return notation;
    }

    public String getAsciiCode() {
        return asciiCode;
    }
}
