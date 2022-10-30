package com.github.xingshuangs.iot.protocol.common.serializer;

import org.junit.Test;

import static org.junit.Assert.*;


public class ByteArraySerializerTest {

    private ByteArraySerializer serializer = new ByteArraySerializer();

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

        ByteArrayBean bean = serializer.toObject(ByteArrayBean.class, src);
//        System.out.println(bean.toString());
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
//        System.out.println(listBean);
        assertArrayEquals(new Boolean[]{true, false, false, false, false, false, false, true}, listBean.getBoolData().toArray(new Boolean[0]));
        assertArrayEquals(new Byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x59}, listBean.getByteData().toArray(new Byte[0]));
        assertArrayEquals(new Integer[]{0, 25689}, listBean.getUint16Data().toArray(new Integer[0]));
        assertArrayEquals(new Short[]{25689, 7995}, listBean.getInt16Data().toArray(new Short[0]));
        assertArrayEquals(new Long[]{523975585L, 523975585L}, listBean.getUint32Data().toArray(new Long[0]));
        assertArrayEquals(new Integer[]{523975585, 523975585}, listBean.getInt32Data().toArray(new Integer[0]));
        assertArrayEquals(new Float[]{33.16f, -15.62f}, listBean.getFloat32Data().toArray(new Float[0]));
        assertArrayEquals(new Double[]{156665.35455556, -56516.66664}, listBean.getFloat64Data().toArray(new Double[0]));
        assertEquals("23A", bean.getStringData());
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

        ByteArrayListBean listBean = serializer.toObject(ByteArrayListBean.class, expect);
        byte[] actual = serializer.toByteArray(listBean);
        assertArrayEquals(expect, actual);
    }

    @Test
    public void toByteArray() {
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

        ByteArrayBean listBean = serializer.toObject(ByteArrayBean.class, expect);
        byte[] actual = serializer.toByteArray(listBean);
        assertArrayEquals(expect, actual);
    }
}