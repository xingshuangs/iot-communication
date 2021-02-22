package com.github.xingshuangs.iot.parse.hex;

import com.github.xingshuangs.iot.utils.HexUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HexParseTest {

    private HexParse hexParse;

    @Before
    public void init() {
        this.hexParse = new HexParse(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x81, (byte) 0x00,
                (byte) 0x00, (byte) 0x64, (byte) 0x59, (byte) 0xC1, (byte) 0x79, (byte) 0xEB, (byte) 0x85,
                (byte) 0xC0, (byte) 0xEB, (byte) 0x98, (byte) 0x95, (byte) 0x55, (byte) 0x1D, (byte) 0x68, (byte) 0xC7,
                (byte) 0xE5, (byte) 0xA4, (byte) 0xA9, (byte) 0xE6, (byte) 0xB0, (byte) 0x94, (byte) 0xE5, (byte) 0xA5, (byte) 0xBD,
                (byte) 0x32, (byte) 0x33, (byte) 0x41});
    }

    @Test
    public void toBoolean() {
        List<Boolean> list = this.hexParse.toBoolean(3, 0, 1);
        assertArrayEquals(new Boolean[]{true}, list.toArray(new Boolean[0]));
        list = this.hexParse.toBoolean(3, 0, 3);
        assertArrayEquals(new Boolean[]{true, false, false}, list.toArray(new Boolean[0]));
        list = this.hexParse.toBoolean(3, 6, 4);
        assertArrayEquals(new Boolean[]{false, true, false, false}, list.toArray(new Boolean[0]));
    }

    @Test
    public void toInt8() {
        List<Byte> list = this.hexParse.toInt8(1, 2);
        assertArrayEquals(new Byte[]{(byte) 0xFF, (byte) 0xFF}, list.toArray(new Byte[0]));
        list = this.hexParse.toInt8(2, 3);
        assertArrayEquals(new Byte[]{(byte) 0xFF, (byte) 0x81, (byte) 0x00}, list.toArray(new Byte[0]));
    }

    @Test
    public void toUInt8() {
        List<Integer> list = this.hexParse.toUInt8(1, 2);
        assertArrayEquals(new Integer[]{0xFF, 0xFF}, list.toArray(new Integer[0]));
        list = this.hexParse.toUInt8(2, 3);
        assertArrayEquals(new Integer[]{0xFF, 0x81, 0x00}, list.toArray(new Integer[0]));
    }

    @Test
    public void toInt16() {
        List<Short> list = this.hexParse.toInt16(0, 2, false);
        assertArrayEquals(new Short[]{-1, (short) -127}, list.toArray(new Short[0]));
        list = this.hexParse.toInt16(4, 2, false);
        assertArrayEquals(new Short[]{0, (short) 25689}, list.toArray(new Short[0]));
    }

    @Test
    public void toUInt16() {
        List<Integer> list = this.hexParse.toUInt16(0, 2, false);
        assertArrayEquals(new Integer[]{65535, 65409}, list.toArray(new Integer[0]));
        list = this.hexParse.toUInt16(4, 2, false);
        assertArrayEquals(new Integer[]{0, 25689}, list.toArray(new Integer[0]));
    }

    @Test
    public void toInt32() {
        List<Integer> list = this.hexParse.toInt32(0, 1, false);
        assertArrayEquals(new Integer[]{-127}, list.toArray(new Integer[0]));
    }

    @Test
    public void toUInt32() {
        List<Long> list = this.hexParse.toUInt32(0, 1, false);
        assertArrayEquals(new Long[]{4294967169L}, list.toArray(new Long[0]));
    }

    @Test
    public void toFloat32() {
        List<Float> list = this.hexParse.toFloat32(8, 1, false);
        assertArrayEquals(new Float[]{-15.62f}, list.toArray(new Float[0]));
    }

    @Test
    public void toFloat64() {
        List<Double> list = this.hexParse.toFloat64(12, 1, false);
        assertArrayEquals(new Double[]{-56516.66664}, list.toArray(new Double[0]));
    }

    @Test
    public void toStringUtf8() {
        String actual = this.hexParse.toStringUtf8(20, 9);
        assertEquals("天气好", actual);
        actual = this.hexParse.toStringUtf8(29, 3);
        assertEquals("23A", actual);
    }

    @Test
    public void parseData() {
        DataUnit<Boolean> boolUnit = new DataUnit<>(3, "bool");
        this.hexParse.parseData(boolUnit);
        assertEquals(true, boolUnit.getValue());

        DataUnit<List<Boolean>> boolListUnit = new DataUnit<>(3, 6, 4, "bool");
        this.hexParse.parseData(boolListUnit);
        assertArrayEquals(new Boolean[]{false, true, false, false}, boolListUnit.getValue().toArray(new Boolean[0]));

        DataUnit<Byte> byteUnit = new DataUnit<>(3, "byte");
        this.hexParse.parseData(byteUnit);
        assertEquals((byte) 0x81, byteUnit.getValue().byteValue());

        DataUnit<List<Byte>> byteListUnit = new DataUnit<>(3, 2, "byte");
        this.hexParse.parseData(byteListUnit);
        assertArrayEquals(new Byte[]{(byte) 0x81, (byte) 0x00}, byteListUnit.getValue().toArray(new Byte[0]));

        DataUnit<Integer> ubyteUnit = new DataUnit<>(3, "ubyte");
        this.hexParse.parseData(ubyteUnit);
        assertEquals(0x81, ubyteUnit.getValue().intValue());

        DataUnit<List<Integer>> ubyteListUnit = new DataUnit<>(3, 2, "ubyte");
        this.hexParse.parseData(ubyteListUnit);
        assertArrayEquals(new Integer[]{0x81, 0x00}, ubyteListUnit.getValue().toArray(new Integer[0]));

        DataUnit<Short> shortUnit = new DataUnit<>(6, "short");
        this.hexParse.parseData(shortUnit);
        assertEquals((short) 25689, shortUnit.getValue().shortValue());

        DataUnit<List<Short>> shortListUnit = new DataUnit<>(4, 2, "short");
        this.hexParse.parseData(shortListUnit);
        assertArrayEquals(new Short[]{0, (short) 25689}, shortListUnit.getValue().toArray(new Short[0]));

        DataUnit<Integer> intUnit = new DataUnit<>(0, "int");
        this.hexParse.parseData(intUnit);
        assertEquals(-127, intUnit.getValue().intValue());

        DataUnit<List<Integer>> intListUnit = new DataUnit<>(0, 2, "int");
        this.hexParse.parseData(intListUnit);
        assertArrayEquals(new Integer[]{-127, 25689}, intListUnit.getValue().toArray(new Integer[0]));

        DataUnit<Long> longUnit = new DataUnit<>(0, "uint");
        this.hexParse.parseData(longUnit);
        assertEquals(4294967169L, longUnit.getValue().longValue());

        DataUnit<List<Long>> longListUnit = new DataUnit<>(0, 2, "uint");
        this.hexParse.parseData(longListUnit);
        assertArrayEquals(new Long[]{4294967169L, 25689L}, longListUnit.getValue().toArray(new Long[0]));

        DataUnit<Float> floatUnit = new DataUnit<>(8,"float");
        this.hexParse.parseData(floatUnit);
        assertEquals(-15.62f, floatUnit.getValue(), 0.01);

        DataUnit<Double> doubleUnit = new DataUnit<>(12,"double");
        this.hexParse.parseData(doubleUnit);
        assertEquals(-56516.66664, doubleUnit.getValue(), 0.00001);

        DataUnit<String> stringUnit = new DataUnit<>(20, 9, "string");
        this.hexParse.parseData(stringUnit);
        assertEquals("天气好", stringUnit.getValue());
    }

    @Test
    public void parseDataList() {
        String src = "FFFFFF8100006459C179EB85C0EB9895551D68C7E5A4A9E6B094E5A5BD323341";
        HexParse parse = new HexParse(HexUtil.toHexArray(src));
        List<DataUnit> list = new ArrayList<>();
        list.add(new DataUnit<>(3, "bool"));
        list.add(new DataUnit<>(3, "byte"));
        list.add(new DataUnit<>(3, "ubyte"));
        list.add(new DataUnit<>(6, "short"));
        list.add(new DataUnit<>(0, "int"));
        list.add(new DataUnit<>(0, "uint"));
        list.add(new DataUnit<>(8,"float"));
        list.add(new DataUnit<>(12,"double"));
        list.add(new DataUnit<>(20, 9, "string"));
        parse.parseDataList(list);
//        list.forEach(x-> System.out.println(x.getValue()));
        /*
            true
            -127
            129
            25689
            -127
            4294967169
            -15.62
            -56516.66664
            天气好
        */
        List<DataUnit> listArray = new ArrayList<>();
        listArray.add(new DataUnit<Boolean>(3, 6, 4, "bool"));
        listArray.add(new DataUnit<>(3, 2, "byte"));
        listArray.add(new DataUnit<>(3, 2, "ubyte"));
        listArray.add(new DataUnit<>(4, 2, "short"));
        listArray.add(new DataUnit<>(0, 2, "int"));
        listArray.add(new DataUnit<>(0, 2, "uint"));
        parse.parseDataList(listArray);
//        listArray.forEach(System.out::println);
        /*
        DataUnit{name='', description='', unit='', bytes=[], value=[false, true, false, false], byteOffset=3, bitOffset=6, count=4, dataType='bool', littleEndian=false}
        DataUnit{name='', description='', unit='', bytes=[], value=[-127, 0], byteOffset=3, bitOffset=0, count=2, dataType='byte', littleEndian=false}
        DataUnit{name='', description='', unit='', bytes=[], value=[129, 0], byteOffset=3, bitOffset=0, count=2, dataType='ubyte', littleEndian=false}
        DataUnit{name='', description='', unit='', bytes=[], value=[0, 25689], byteOffset=4, bitOffset=0, count=2, dataType='short', littleEndian=false}
        DataUnit{name='', description='', unit='', bytes=[], value=[-127, 25689], byteOffset=0, bitOffset=0, count=2, dataType='int', littleEndian=false}
        DataUnit{name='', description='', unit='', bytes=[], value=[4294967169, 25689], byteOffset=0, bitOffset=0, count=2, dataType='uint', littleEndian=false}
        */
    }
}
