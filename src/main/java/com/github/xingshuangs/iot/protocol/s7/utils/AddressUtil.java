package com.github.xingshuangs.iot.protocol.s7.utils;


import com.github.xingshuangs.iot.protocol.s7.enums.EArea;
import com.github.xingshuangs.iot.protocol.s7.enums.EParamVariableType;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;

/**
 * S7协议地址解析工具
 * DB1.0.1、DB1.1
 * M1.1、M1
 * V1.1、V1
 * I0.1、I0
 * Q0.1、Q0
 *
 * @author xingshuang
 */
public class AddressUtil {

    private AddressUtil() {
        // NOOP
    }

    /**
     * 字节地址解析
     *
     * @param address 地址
     * @param count   个数
     * @return 请求项
     */
    public static RequestItem parseByte(String address, int count) {
        return parse(address, count, EParamVariableType.BYTE);
    }

    /**
     * 位地址解析
     *
     * @param address 地址
     * @return 请求项
     */
    public static RequestItem parseBit(String address) {
        return parse(address, 1, EParamVariableType.BIT);
    }

    /**
     * 解析请求内容
     *
     * @param address      地址
     * @param count        个数
     * @param variableType 参数类型
     * @return RequestItem请求项
     */
    public static RequestItem parse(String address, int count, EParamVariableType variableType) {
        if (address == null || address.length() == 0) {
            throw new IllegalArgumentException("address不能为空");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("count个数必须为正数");
        }
        // 转换为大写
        address = address.toUpperCase();
        String[] addList = address.split("\\.");

        RequestItem item = new RequestItem();
        item.setVariableType(variableType);
        item.setCount(count);
        item.setArea(parseArea(addList));
        item.setDbNumber(parseDbNumber(addList));
        item.setByteAddress(parseByteAddress(addList));
        item.setBitAddress(parseBitAddress(addList, variableType));
        if (item.getBitAddress() > 7) {
            throw new IllegalArgumentException("address地址信息格式错误，位索引只能[0-7]");
        }
        return item;
    }

    /**
     * 区域解析
     *
     * @param addList 地址信息
     * @return 区域地址，枚举类型
     */
    private static EArea parseArea(String[] addList) {
        switch (addList[0].substring(0, 1)) {
            case "I":
                return EArea.INPUTS;
            case "Q":
                return EArea.OUTPUTS;
            case "M":
                return EArea.FLAGS;
            case "D":
            case "V":
                //****************** 对于200smartPLC的V区，就是DB1.X，例如，V1=DB1.1，V100=DB1.100 **********************/
                return EArea.DATA_BLOCKS;
            case "T":
                return EArea.S7_TIMERS;
            case "C":
                return EArea.S7_COUNTERS;
            default:
                throw new IllegalArgumentException("传入的参数有误，无法解析Area");
        }
    }

    /**
     * DB块索引解析
     *
     * @param addList 地址信息
     * @return DB块索引
     */
    private static int parseDbNumber(String[] addList) {
        switch (addList[0].substring(0, 1)) {
            case "D":
                return addList[0].contains("DB") ?
                        Integer.valueOf(addList[0].substring(2))
                        : Integer.valueOf(addList[0].substring(1));
            case "V":
                //****************** 对于200smartPLC的V区，就是DB1.X，例如，V1=DB1.1，V100=DB1.100 **********************/
                return 1;
            default:
                return 0;
        }
    }

    /**
     * 字节索引解析
     *
     * @param addList 地址信息
     * @return 字节索引
     */
    private static int parseByteAddress(String[] addList) {
        switch (addList[0].substring(0, 1)) {
            case "D":
                return addList.length >= 2 ? Integer.parseInt(addList[1]) : 0;
            default:
                return Integer.parseInt(addList[0].substring(1));
        }
    }

    /**
     * 位索引解析
     *
     * @param addList 地址信息
     * @return 位索引
     */
    private static int parseBitAddress(String[] addList, EParamVariableType variableType) {
        switch (addList[0].substring(0, 1)) {
            case "D":
                // 只有是bit数据类型的时候，才能将bit地址进行赋值，不然都是0；本质上不是bit时，位索引是不是0都不受影响的
                return addList.length >= 3 && variableType == EParamVariableType.BIT ? Integer.parseInt(addList[2]) : 0;
            default:
                // 只有是bit数据类型的时候，才能将bit地址进行赋值，不然都是0；本质上不是bit时，位索引是不是0都不受影响的
                return addList.length >= 2 && variableType == EParamVariableType.BIT ? Integer.parseInt(addList[1]) : 0;
        }
    }
}
