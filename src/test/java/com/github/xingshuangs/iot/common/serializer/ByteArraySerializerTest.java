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
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class ByteArraySerializerTest {

    @Test
    public void toObject() {
        byte[] src = new byte[]{(byte) 0x81,
                // 0, 25689
                (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x59,
                // 523975585
                (byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1, (byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1,
                // 33.16f, -15.62f
                (byte) 0x42, (byte) 0x04, (byte) 0xA3, (byte) 0xD7, (byte) 0xC1, (byte) 0x79, (byte) 0xEB, (byte) 0x85,
                // 156665.35455556
                (byte) 0x41, (byte) 0x03, (byte) 0x1F, (byte) 0xCA, (byte) 0xD6, (byte) 0x21, (byte) 0x39, (byte) 0xB7,
                // -56516.66664
                (byte) 0xC0, (byte) 0xEB, (byte) 0x98, (byte) 0x95, (byte) 0x55, (byte) 0x1D, (byte) 0x68, (byte) 0xC7,
                // 23A
                (byte) 0x32, (byte) 0x33, (byte) 0x41};

        ByteArraySerializer serializer = ByteArraySerializer.newInstance();
        ByteArrayBean bean = serializer.toObject(ByteArrayBean.class, src);
        assertTrue(bean.getBoolData());
        assertEquals((byte) 0x81, bean.getByteData().byteValue());
        assertEquals(25689, bean.getUint16Data().intValue());
        assertEquals((short) 25689, bean.getInt16Data().shortValue());
        assertEquals((long) 523975585, bean.getUint32Data().longValue());
        assertEquals(523975585, bean.getInt32Data().intValue());
        assertEquals(33.16, bean.getFloat32Data(), 0.0001);
        assertEquals(156665.35455556, bean.getFloat64Data(), 0.00000000001);
        assertEquals("23A", bean.getStringData());

        ByteArrayListBean listBean = serializer.toObject(ByteArrayListBean.class, src);
        assertArrayEquals(new Boolean[]{true, false, false, false, false, false, false, true}, listBean.getBoolData().toArray(new Boolean[0]));
        assertArrayEquals(new Byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x59}, listBean.getByteData().toArray(new Byte[0]));
        assertArrayEquals(new Integer[]{0, 25689}, listBean.getUint16Data().toArray(new Integer[0]));
        assertArrayEquals(new Short[]{25689, 7995}, listBean.getInt16Data().toArray(new Short[0]));
        assertArrayEquals(new Long[]{523975585L, 523975585L}, listBean.getUint32Data().toArray(new Long[0]));
        assertArrayEquals(new Integer[]{523975585, 523975585}, listBean.getInt32Data().toArray(new Integer[0]));
        assertArrayEquals(new Float[]{33.16f, -15.62f}, listBean.getFloat32Data().toArray(new Float[0]));
        assertArrayEquals(new Double[]{156665.35455556, -56516.66664}, listBean.getFloat64Data().toArray(new Double[0]));
        assertEquals("23A", listBean.getStringData());
    }

    @Test
    public void toObject1() {
        byte[] src = new byte[]{(byte) 0x81,
                // 0, 25689
                (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x59,
                // 523975585
                (byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1, (byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1,
                // 33.16f, -15.62f
                (byte) 0x42, (byte) 0x04, (byte) 0xA3, (byte) 0xD7, (byte) 0xC1, (byte) 0x79, (byte) 0xEB, (byte) 0x85,
                // 156665.35455556
                (byte) 0x41, (byte) 0x03, (byte) 0x1F, (byte) 0xCA, (byte) 0xD6, (byte) 0x21, (byte) 0x39, (byte) 0xB7,
                // -56516.66664
                (byte) 0xC0, (byte) 0xEB, (byte) 0x98, (byte) 0x95, (byte) 0x55, (byte) 0x1D, (byte) 0x68, (byte) 0xC7,
                // 23A
                (byte) 0x32, (byte) 0x33, (byte) 0x41};

        List<ByteArrayParameter> list = new ArrayList<>();
        list.add(new ByteArrayParameter(0, 0, 1, EDataType.BOOL));
        list.add(new ByteArrayParameter(0, 0, 1, EDataType.BYTE));
        list.add(new ByteArrayParameter(3, 0, 1, EDataType.UINT16));
        list.add(new ByteArrayParameter(3, 0, 1, EDataType.INT16));
        list.add(new ByteArrayParameter(5, 0, 1, EDataType.UINT32));
        list.add(new ByteArrayParameter(9, 0, 1, EDataType.INT32));
        list.add(new ByteArrayParameter(13, 0, 1, EDataType.FLOAT32));
        list.add(new ByteArrayParameter(21, 0, 1, EDataType.FLOAT64));
        list.add(new ByteArrayParameter(37, 0, 3, EDataType.STRING));

        ByteArraySerializer serializer = ByteArraySerializer.newInstance();
        List<ByteArrayParameter> bean = serializer.extractParameter(list, src);
        assertTrue((Boolean) bean.get(0).getValue());
        assertEquals((byte) 0x81, bean.get(1).getValue());
        assertEquals(25689, bean.get(2).getValue());
        assertEquals((short) 25689, bean.get(3).getValue());
        assertEquals((long) 523975585, bean.get(4).getValue());
        assertEquals(523975585, bean.get(5).getValue());
        assertEquals(33.16, (Float) bean.get(6).getValue(), 0.0001);
        assertEquals(156665.35455556, (Double) bean.get(7).getValue(), 0.00000000001);
        assertEquals("23A", bean.get(8).getValue());

        ByteArrayParameter parameter = serializer.extractParameter(list.get(2), src);
        assertEquals(25689, parameter.getValue());

        list = new ArrayList<>();
        list.add(new ByteArrayParameter(0, 0, 8, EDataType.BOOL));
        list.add(new ByteArrayParameter(1, 0, 4, EDataType.BYTE));
        list.add(new ByteArrayParameter(1, 0, 2, EDataType.UINT16));
        list.add(new ByteArrayParameter(3, 0, 2, EDataType.INT16));
        list.add(new ByteArrayParameter(5, 0, 2, EDataType.UINT32));
        list.add(new ByteArrayParameter(5, 0, 2, EDataType.INT32));
        list.add(new ByteArrayParameter(13, 0, 2, EDataType.FLOAT32));
        list.add(new ByteArrayParameter(21, 0, 2, EDataType.FLOAT64));
        list.add(new ByteArrayParameter(37, 0, 3, EDataType.STRING));
        List<ByteArrayParameter> listBean = serializer.extractParameter(list, src);
        assertArrayEquals(new Boolean[]{true, false, false, false, false, false, false, true}, ((List<Boolean>) listBean.get(0).getValue()).toArray(new Boolean[0]));
        assertArrayEquals(new Byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x59}, ((List<Byte>) listBean.get(1).getValue()).toArray(new Byte[0]));
        assertArrayEquals(new Integer[]{0, 25689}, ((List<Integer>) listBean.get(2).getValue()).toArray(new Integer[0]));
        assertArrayEquals(new Short[]{25689, 7995}, ((List<Short>) listBean.get(3).getValue()).toArray(new Short[0]));
        assertArrayEquals(new Long[]{523975585L, 523975585L}, ((List<Long>) listBean.get(4).getValue()).toArray(new Long[0]));
        assertArrayEquals(new Integer[]{523975585, 523975585}, ((List<Integer>) listBean.get(5).getValue()).toArray(new Integer[0]));
        assertArrayEquals(new Float[]{33.16f, -15.62f}, ((List<Float>) listBean.get(6).getValue()).toArray(new Float[0]));
        assertArrayEquals(new Double[]{156665.35455556, -56516.66664}, ((List<Double>) listBean.get(7).getValue()).toArray(new Double[0]));
        assertEquals("23A", listBean.get(8).getValue());
    }

    @Test
    public void toByteArrayList() {
        byte[] expect = new byte[]{(byte) 0x81,
                // 0, 25689
                (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x59,
                // 523975585
                (byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1, (byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1,
                // 33.16f, -15.62f
                (byte) 0x42, (byte) 0x04, (byte) 0xA3, (byte) 0xD7, (byte) 0xC1, (byte) 0x79, (byte) 0xEB, (byte) 0x85,
                // 156665.35455556
                (byte) 0x41, (byte) 0x03, (byte) 0x1F, (byte) 0xCA, (byte) 0xD6, (byte) 0x21, (byte) 0x39, (byte) 0xB7,
                // -56516.66664
                (byte) 0xC0, (byte) 0xEB, (byte) 0x98, (byte) 0x95, (byte) 0x55, (byte) 0x1D, (byte) 0x68, (byte) 0xC7,
                // 23A
                (byte) 0x32, (byte) 0x33, (byte) 0x41};

        ByteArraySerializer serializer = ByteArraySerializer.newInstance();
        ByteArrayListBean listBean = serializer.toObject(ByteArrayListBean.class, expect);
        byte[] actual = serializer.toByteArray(listBean);
        assertArrayEquals(expect, actual);
    }

    @Test
    public void toByteArray() {
        ByteArraySerializer serializer = ByteArraySerializer.newInstance();
        byte[] expect = new byte[]{(byte) 0x01,
                // 0, 25689
                (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x59,
                // 523975585
                (byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1, (byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1,
                // 33.16f, -15.62f
                (byte) 0x42, (byte) 0x04, (byte) 0xA3, (byte) 0xD7, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                // 156665.35455556
                (byte) 0x41, (byte) 0x03, (byte) 0x1F, (byte) 0xCA, (byte) 0xD6, (byte) 0x21, (byte) 0x39, (byte) 0xB7,
                // -56516.66664
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                // 23A
                (byte) 0x32, (byte) 0x33, (byte) 0x41};

        ByteArrayBean bean = serializer.toObject(ByteArrayBean.class, expect);
        byte[] actual = serializer.toByteArray(bean);
        assertArrayEquals(expect, actual);

        expect = new byte[]{(byte) 0x81,
                // 0, 25689
                (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x59,
                // 523975585
                (byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1, (byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1,
                // 33.16f, -15.62f
                (byte) 0x42, (byte) 0x04, (byte) 0xA3, (byte) 0xD7, (byte) 0xC1, (byte) 0x79, (byte) 0xEB, (byte) 0x85,
                // 156665.35455556
                (byte) 0x41, (byte) 0x03, (byte) 0x1F, (byte) 0xCA, (byte) 0xD6, (byte) 0x21, (byte) 0x39, (byte) 0xB7,
                // -56516.66664
                (byte) 0xC0, (byte) 0xEB, (byte) 0x98, (byte) 0x95, (byte) 0x55, (byte) 0x1D, (byte) 0x68, (byte) 0xC7,
                // 23A
                (byte) 0x32, (byte) 0x33, (byte) 0x41};

        ByteArrayListBean listBean = serializer.toObject(ByteArrayListBean.class, expect);
        actual = serializer.toByteArray(listBean);
        assertArrayEquals(expect, actual);
    }

    @Test
    public void toByteArray1() {
        ByteArraySerializer serializer = ByteArraySerializer.newInstance();
        byte[] expect = new byte[]{(byte) 0x01,
                // 0, 25689
                (byte) 0x59, (byte) 0x64, (byte) 0x00, (byte) 0x00,
                // 523975585
                (byte) 0xA1, (byte) 0x3B, (byte) 0x3B, (byte) 0x1F, (byte) 0xA1, (byte) 0x3B, (byte) 0x3B, (byte) 0x1F,
                // 33.16f, -15.62f
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xD7, (byte) 0xA3, (byte) 0x04, (byte) 0x42,
                // 156665.35455556
                (byte) 0xB7, (byte) 0x39, (byte) 0x21, (byte) 0xD6, (byte) 0xCA, (byte) 0x1F, (byte) 0x03, (byte) 0x41,
                // -56516.66664
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                // 23A
                (byte) 0x32, (byte) 0x33, (byte) 0x41};

        ByteArrayLittleEndianBean bean = serializer.toObject(ByteArrayLittleEndianBean.class, expect);
        byte[] actual = serializer.toByteArray(bean);
        assertArrayEquals(expect, actual);
    }

    @Test
    public void toByteArray2() {
        ByteArraySerializer serializer = ByteArraySerializer.newInstance();
        byte[] expect = new byte[]{(byte) 0x01,
                // 0, 25689
                (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x59,
                // 523975585
                (byte) 0x3B, (byte) 0xA1, (byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1, (byte) 0x1F, (byte) 0x3B,
                // 33.16f, -15.62f
                (byte) 0xA3, (byte) 0xD7, (byte) 0x42, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                // 156665.35455556
                (byte) 0xD6, (byte) 0x21, (byte) 0x39, (byte) 0xB7, (byte) 0x41, (byte) 0x03, (byte) 0x1F, (byte) 0xCA,
                // -56516.66664
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                // 23A
                (byte) 0x32, (byte) 0x33, (byte) 0x41};

        ByteFormatBean bean = serializer.toObject(ByteFormatBean.class, expect);
        byte[] actual = serializer.toByteArray(bean);
        assertArrayEquals(expect, actual);
        bean.getInt32Data().byteValue();
    }
}