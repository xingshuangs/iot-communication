/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.common.serializer;


import com.github.xingshuangs.iot.common.enums.EDataType;

import java.lang.annotation.*;

/**
 * 字节数组变量参数
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ByteArrayVariable {

    /**
     * 字节偏移量
     *
     * @return 字节偏移量
     */
    int byteOffset() default 0;

    /**
     * 位偏移量
     *
     * @return 位偏移量
     */
    int bitOffset() default 0;

    /**
     * 数量，数量大于1的时候对应的数据必须使用list
     *
     * @return 数量
     */
    int count() default 1;

    /**
     * 类型
     *
     * @return 类型
     */
    EDataType type() default EDataType.BYTE;

    /**
     * 是否小端模式
     *
     * @return 是否小端模式
     */
    boolean littleEndian() default false;

//    /**
//     * 数据格式
//     *
//     * @return 数据格式
//     */
//    EByteBuffFormat format() default EByteBuffFormat.DC_BA;
}
