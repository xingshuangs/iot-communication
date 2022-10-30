package com.github.xingshuangs.iot.protocol.common.serializer;


import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author xingshuang
 */
@Data
public class ByteArrayParseData {

    /**
     * 字节数组注解参数
     */
    ByteArrayVariable variable;

    /**
     * 字段内容
     */
    Field field;

    public ByteArrayParseData() {
    }

    public ByteArrayParseData(ByteArrayVariable variable, Field field) {
        this.variable = variable;
        this.field = field;
    }
}
