package com.github.xingshuangs.iot.protocol.s7.serializer;


import com.github.xingshuangs.iot.protocol.common.enums.EDataType;
import com.github.xingshuangs.iot.protocol.s7.model.DataItem;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * S7解析数据
 *
 * @author xingshuang
 */
@Data
public class S7ParseData {

    /**
     * 数据类型
     */
    private EDataType dataType;

    /**
     * 数据个数
     */
    private int count;

    /**
     * 字段参数
     */
    private Field field;

    /**
     * 请求项
     */
    private RequestItem requestItem;

    /**
     * 数据项
     */
    private DataItem dataItem;
}
