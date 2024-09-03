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


import com.github.xingshuangs.iot.common.buff.EByteBuffFormat;
import com.github.xingshuangs.iot.common.enums.EDataType;

import java.lang.annotation.*;

/**
 * Annotation of byte array variable.
 * (字节数组变量参数)
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ByteArrayVariable {

    /**
     * Byte offset.
     * (字节偏移量)
     *
     * @return byte offset
     */
    int byteOffset() default 0;

    /**
     * Bit offset.
     * (位偏移量)
     *
     * @return the bit offset
     */
    int bitOffset() default 0;

    /**
     * Count, if count bigger than 1, then must use list type.
     * (数量，数量大于1的时候对应的数据必须使用list)
     *
     * @return count
     */
    int count() default 1;

    /**
     * 类型
     *
     * @return type
     */
    EDataType type() default EDataType.BYTE;

    /**
     * Is little endian.
     * (是否小端模式)
     *
     * @return is little endian
     */
    boolean littleEndian() default false;

    /**
     * 4 - or 8-byte encoding format.
     * (数据格式)
     *
     * @return format
     */
    EByteBuffFormat format() default EByteBuffFormat.DC_BA;
}
