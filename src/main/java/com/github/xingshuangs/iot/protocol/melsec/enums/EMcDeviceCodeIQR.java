package com.github.xingshuangs.iot.protocol.melsec.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 软元件代码  MELSEC iQ-R系列用
 *
 * @author xingshuang
 */
public enum EMcDeviceCodeIQR {
    /**
     * 特殊继电器
     */
    SM("SM", 10, "SM**", 0x0091),

    /**
     * 特殊寄存器
     */
    SD("SD", 10, "SD**", 0x00A9),

    /**
     * 输入
     */
    X("X", 16, "X***", 0x009C),

    /**
     * 输出
     */
    Y("Y", 16, "Y***", 0x009D),

    /**
     * 内部继电器
     */
    M("M", 10, "M***", 0x0090),

    /**
     * 锁存继电器
     */
    L("L", 10, "L***", 0x0092),

    /**
     * 报警器
     */
    F("F", 10, "F***", 0x0093),

    /**
     * 变址继电器
     */
    V("V", 10, "V***", 0x0094),

    /**
     * 链接继电器
     */
    B("B", 16, "B***", 0x00A0),

    /**
     * 数据寄存器
     */
    D("D", 10, "D***", 0x00A8),

    /**
     * 链接寄存器
     */
    W("W", 16, "W***", 0x00B4),

    /**
     * 定时器触点
     */
    TS("TS", 10, "TS**", 0x00C1),

    /**
     * 定时器线圈
     */
    TC("TC", 10, "TC**", 0x00C0),

    /**
     * 定时器当前值
     */
    TN("TN", 10, "TN**", 0x00C2),

    /**
     * 长定时器触点
     */
    LTS("LTS", 10, "LTS*", 0x0051),

    /**
     * 长定时器线圈
     */
    LTC("LTC", 10, "LTC*", 0x0050),

    /**
     * 长定时器当前值
     */
    LTN("LTN", 10, "LTN*", 0x0052),

    /**
     * 累计定时器触点
     */
    STS("STS", 10, "STS*", 0x00C7),

    /**
     * 累计定时器线圈
     */
    STC("STC", 10, "STC*", 0x00C6),

    /**
     * 累计定时器当前值
     */
    STN("STN", 10, "STN*", 0x00C8),

    /**
     * 长累计定时器触点
     */
    LSTS("LSTS", 10, "LSTS", 0x0059),

    /**
     * 长累计定时器线圈
     */
    LSTC("LSTC", 10, "LSTC", 0x0058),

    /**
     * 长累计定时器当前值
     */
    LSTN("LSTN", 10, "LSTN", 0x005A),

    /**
     * 计数器触点
     */
    CS("CS", 10, "CS**", 0x00C4),

    /**
     * 计数器线圈
     */
    CC("CC", 10, "CC**", 0x00C3),

    /**
     * 计数器当前值
     */
    CN("CN", 10, "CN**", 0x00C5),

    /**
     * 长计数器触点
     */
    LCS("LCS", 10, "LCS*", 0x0055),

    /**
     * 长计数器线圈
     */
    LCC("LCC", 10, "LCC*", 0x0054),

    /**
     * 长计数器当前值
     */
    LCN("LCN", 10, "LCN*", 0x0056),

    /**
     * 链接特殊继电器
     */
    SB("SB", 16, "SB**", 0x00A1),

    /**
     * 链接特殊寄存器
     */
    SW("SW", 16, "SW**", 0x00B5),

    /**
     * 直接访问输入
     */
    DX("DX", 16, "DX**", 0x00A2),

    /**
     * 直接访问输出
     */
    DY("DY", 16, "DY**", 0x00A3),

    /**
     * 变址寄存器
     */
    Z("Z", 10, "Z***", 0x00CC),

    /**
     * 变址寄存器长变址寄存器
     */
    LZ("LZ", 10, "LZ**", 0x0062),

    /**
     * 文件寄存器 块切换方式
     */
    R("R", 10, "R***", 0x00AF),

    /**
     * 文件寄存器 连号访问方式
     */
    ZR("ZR", 16, "ZR**", 0x00B0),

    /**
     * 刷新数据寄存器
     */
    RD("RD", 10, "RD**", 0x002C),
    ;

    private static Map<String, EMcDeviceCodeIQR> map;

    public static EMcDeviceCodeIQR from(String data) {
        if (map == null) {
            map = new HashMap<>();
            for (EMcDeviceCodeIQR item : EMcDeviceCodeIQR.values()) {
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
     * 二进制参数，2个字节
     */
    private final int binaryCode;

    /**
     * ascii参数
     */
    private final String asciiCode;

    EMcDeviceCodeIQR(String symbol, int notation, String asciiCode, int binaryCode) {
        this.symbol = symbol;
        this.notation = notation;
        this.asciiCode = asciiCode;
        this.binaryCode = binaryCode;
    }

    public int getBinaryCode() {
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
