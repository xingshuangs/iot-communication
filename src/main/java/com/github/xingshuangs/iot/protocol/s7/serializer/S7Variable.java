package com.github.xingshuangs.iot.protocol.s7.serializer;


import com.github.xingshuangs.iot.protocol.common.enums.EDataType;

import java.lang.annotation.*;

/**
 * S7变量参数
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface S7Variable {

    /**
     * 地址
     *
     * @return 地址
     */
    String address() default "";

    /**
     * 类型
     * 如果字节，对应是byte[]类型
     *
     * @return 类型
     */
    EDataType type() default EDataType.BYTE;

    /**
     * 数量
     * 除字节Byte和String类型外，其他类型对应的count必须为1
     *
     * @return 数量
     */
    int count() default 1;

}
