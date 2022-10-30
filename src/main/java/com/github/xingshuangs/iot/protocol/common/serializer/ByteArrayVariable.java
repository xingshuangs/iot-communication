package com.github.xingshuangs.iot.protocol.common.serializer;


import com.github.xingshuangs.iot.protocol.common.buff.EByteBuffFormat;
import com.github.xingshuangs.iot.protocol.common.enums.EDataType;

import java.lang.annotation.*;

/**
 * 字节数组变量参数
 */
@Target(value = { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ByteArrayVariable {

    /**
     * 字节偏移量
     */
    int byteOffset() default 0;

    /**
     * 位偏移量
     */
    int bitOffset() default 0;

    /**
     * 数量，数量大于1的时候对应的数据必须使用list
     */
    int count() default 1;

    /**
     * 类型
     */
    EDataType type() default EDataType.BYTE;

    /**
     * 数据格式
     */
    EByteBuffFormat format() default EByteBuffFormat.DC_BA;
}
