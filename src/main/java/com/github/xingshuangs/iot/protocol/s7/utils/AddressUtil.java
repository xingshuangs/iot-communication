package com.github.xingshuangs.iot.protocol.s7.utils;


import com.github.xingshuangs.iot.protocol.s7.enums.EArea;
import com.github.xingshuangs.iot.protocol.s7.enums.EParamVariableType;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;
import org.apache.commons.lang3.StringUtils;

/**
 * S7协议地址解析工具  DB15.20.1
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
        if (StringUtils.isEmpty(address)) {
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
        item.setArea(parseArea(addList[0].substring(0, 1)));

        if (addList[0].contains("DB") || addList[0].contains("D")) {
            int dbNumber = addList[0].contains("DB") ? Integer.valueOf(addList[0].substring(2)) : Integer.valueOf(addList[0].substring(1));
            item.setDbNumber(dbNumber);
            item.setByteAddress(addList.length >= 2 ? Integer.valueOf(addList[1]) : 0);
            // 只有是bit数据类型的时候，才能将bit地址进行赋值，不然都是0
            int bitAddress = addList.length >= 3 && variableType == EParamVariableType.BIT ? Integer.valueOf(addList[2]) : 0;
            item.setBitAddress(bitAddress);
        } else {
            item.setByteAddress(Integer.valueOf(addList[0].substring(1)));
            // 只有是bit数据类型的时候，才能将bit地址进行赋值，不然都是0
            int bitAddress = addList.length >= 2 && variableType == EParamVariableType.BIT ? Integer.valueOf(addList[1]) : 0;
            item.setBitAddress(bitAddress);
        }
        if (item.getBitAddress() > 7) {
            throw new IllegalArgumentException("address地址信息格式错误，位索引只能[0-7]");
        }
        return item;
    }

    /**
     * 区域解析
     *
     * @param area 区域
     * @return 区域地址，枚举类型
     */
    private static EArea parseArea(String area) {
        switch (area) {
            case "I":
                return EArea.INPUTS;
            case "Q":
                return EArea.OUTPUTS;
            case "M":
                return EArea.FLAGS;
            case "D":
            case "DB":
                return EArea.DATA_BLOCKS;
            case "T":
                return EArea.S7_TIMERS;
            case "C":
                return EArea.S7_COUNTERS;
            default:
                throw new IllegalArgumentException("传入的参数有误，无法解析Area");
        }
    }
}
