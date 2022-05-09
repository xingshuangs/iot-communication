package com.github.xingshuangs.iot.protocol.s7.utils;


import com.github.xingshuangs.iot.protocol.s7.enums.EArea;
import com.github.xingshuangs.iot.protocol.s7.enums.EParamVariableType;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;

/**
 * S7协议地址解析工具  DB15.20.1
 *
 * @author xingshuang
 */
public class S7AddressUtil {

    public static RequestItem parseByte(String address, int length) {
        return parse(address, length, EParamVariableType.BYTE);
    }

    public static RequestItem parseBit(String address) {
        return parse(address, 1, EParamVariableType.BIT);
    }

    public static RequestItem parse(String address, int length, EParamVariableType variableType) {
        if (length <= 0) {
            throw new IllegalArgumentException("length长度必须为正数");
        }
        // 转换为大写
        address = address.toUpperCase();
        RequestItem item = new RequestItem();
        String[] addList = address.split("\\.");
        if (addList.length < 2) {
            throw new IllegalArgumentException("address地址信息格式错误");
        }

        item.setVariableType(variableType);
        item.setArea(parseArea(addList[0].substring(0, 1)));
        item.setCount(length);
        item.setByteAddress(Integer.valueOf(addList[1]));

        // 只有是bit数据类型的时候，才能将bit地址进行赋值，不然都是0
        if (addList.length == 3 && variableType == EParamVariableType.BIT) {
            item.setBitAddress(Integer.valueOf(addList[2]));
            if (item.getBitAddress() > 7) {
                throw new IllegalArgumentException("address地址信息格式错误，位索引只能[0-7]");
            }
        }
        if (addList[0].contains("DB")) {
            item.setDbNumber(Integer.valueOf(addList[0].substring(2)));
        } else {
            item.setDbNumber(Integer.valueOf(addList[0].substring(1)));
        }
        return item;
    }

    private static EArea parseArea(String str) {
        switch (str) {
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
