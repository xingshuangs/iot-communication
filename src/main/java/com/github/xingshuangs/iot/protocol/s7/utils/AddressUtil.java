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

    public static RequestItem parse(String address, int count, EParamVariableType variableType) {
        if (StringUtils.isEmpty(address)) {
            throw new IllegalArgumentException("address不能为空");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("count个数必须为正数");
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
        item.setCount(count);
        item.setByteAddress(Integer.valueOf(addList[1]));

        // 只有是bit数据类型的时候，才能将bit地址进行赋值，不然都是0
        if (addList.length == 3 && variableType == EParamVariableType.BIT) {
            item.setBitAddress(Integer.valueOf(addList[2]));
            if (item.getBitAddress() > 7) {
                throw new IllegalArgumentException("address地址信息格式错误，位索引只能[0-7]");
            }
        }
        int dbNumber = addList[0].contains("DB") ? Integer.valueOf(addList[0].substring(2)) : Integer.valueOf(addList[0].substring(1));
        item.setDbNumber(dbNumber);
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
