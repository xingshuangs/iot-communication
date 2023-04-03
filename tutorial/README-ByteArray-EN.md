# ByteArray Parse Tutorial

[HOME BACK](../README.md)

## 1. Serializer mode

Support BOOL UINT16 INT16 UINT32 INT32 FLOAT32 FLOAT64 STRING read-write

```java

@Data
public class ByteArrayBean {

    @ByteArrayVariable(byteOffset = 0, bitOffset = 0, count = 1, type = EDataType.BOOL)
    Boolean boolData;

    @ByteArrayVariable(byteOffset = 0, count = 1, type = EDataType.BYTE)
    Byte byteData;

    @ByteArrayVariable(byteOffset = 3, count = 1, type = EDataType.UINT16)
    Integer uint16Data;

    @ByteArrayVariable(byteOffset = 3, count = 1, type = EDataType.INT16)
    Short int16Data;

    @ByteArrayVariable(byteOffset = 5, count = 1, type = EDataType.UINT32)
    Long uint32Data;

    @ByteArrayVariable(byteOffset = 9, count = 1, type = EDataType.INT32)
    Integer int32Data;

    @ByteArrayVariable(byteOffset = 13, count = 1, type = EDataType.FLOAT32)
    Float float32Data;

    @ByteArrayVariable(byteOffset = 21, count = 1, type = EDataType.FLOAT64)
    Double float64Data;

    @ByteArrayVariable(byteOffset = 37, count = 3, type = EDataType.STRING)
    String stringData;
}

@Data
public class ByteArrayListBean {

    @ByteArrayVariable(byteOffset = 0, bitOffset = 0, count = 8, type = EDataType.BOOL)
    List<Boolean> boolData;

    @ByteArrayVariable(byteOffset = 1, count = 4, type = EDataType.BYTE)
    List<Byte> byteData;

    @ByteArrayVariable(byteOffset = 1, count = 2, type = EDataType.UINT16)
    List<Integer> uint16Data;

    @ByteArrayVariable(byteOffset = 3, count = 2, type = EDataType.INT16)
    List<Short> int16Data;

    @ByteArrayVariable(byteOffset = 5, count = 2, type = EDataType.UINT32)
    List<Long> uint32Data;

    @ByteArrayVariable(byteOffset = 5, count = 2, type = EDataType.INT32)
    List<Integer> int32Data;

    @ByteArrayVariable(byteOffset = 13, count = 2, type = EDataType.FLOAT32)
    List<Float> float32Data;

    @ByteArrayVariable(byteOffset = 21, count = 2, type = EDataType.FLOAT64)
    List<Double> float64Data;

    @ByteArrayVariable(byteOffset = 37, count = 3, type = EDataType.STRING)
    String stringData;
}

class Demo {
    public static void main(String[] args) {
        ByteArraySerializer serializer = ByteArraySerializer.newInstance();
        byte[] expect = new byte[]{(byte) 0x01,
                // 0, 25689
                (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x59,
                // 523975585
                (byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1, (byte) 0x1F, (byte) 0x3B, (byte) 0x3B, (byte) 0xA1,
                // 33.16f
                (byte) 0x42, (byte) 0x04, (byte) 0xA3, (byte) 0xD7, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                // 156665.35455556
                (byte) 0x41, (byte) 0x03, (byte) 0x1F, (byte) 0xCA, (byte) 0xD6, (byte) 0x21, (byte) 0x39, (byte) 0xB7,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                // 23A
                (byte) 0x32, (byte) 0x33, (byte) 0x41};

        ByteArrayBean bean = serializer.toObject(ByteArrayBean.class, expect);
        byte[] actual = serializer.toByteArray(bean);

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
    }
}
```

## 2. Previous version

### 2.1 Single Data

```java
/*********************************** EXAMPLE1 ***********************************/
class Demo {
    public static void main(String[] args) {
        String src = "FFFFFF8100006459C179EB85C0EB9895551D68C7E5A4A9E6B094E5A5BD323341";
        HexParse parse = new HexParse(HexUtil.toHexArray(src));
        List<DataUnit> list = new ArrayList<>();
        list.add(new DataUnit<>(3, "bool"));
        list.add(new DataUnit<>(3, "byte"));
        list.add(new DataUnit<>(3, "ubyte"));
        list.add(new DataUnit<>(6, "short"));
        list.add(new DataUnit<>(0, "int"));
        list.add(new DataUnit<>(0, "uint"));
        list.add(new DataUnit<>(8, "float"));
        list.add(new DataUnit<>(12, "double"));
        list.add(new DataUnit<>(20, 9, "string"));
        parse.parseDataList(list);
        list.forEach(x -> System.out.println(x.getValue()));
    }
}

/* result
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
```

### 2.2 Array Data

```java
/*********************************** EXAMPLE2 ***********************************/
class Demo {
    public static void main(String[] args) {
        String src = "FFFFFF8100006459C179EB85C0EB9895551D68C7E5A4A9E6B094E5A5BD323341";
        HexParse parse = new HexParse(HexUtil.toHexArray(src));
        List<DataUnit> listArray = new ArrayList<>();
        listArray.add(new DataUnit<Boolean>(3, 6, 4, "bool"));
        listArray.add(new DataUnit<>(3, 2, "byte"));
        listArray.add(new DataUnit<>(3, 2, "ubyte"));
        listArray.add(new DataUnit<>(4, 2, "short"));
        listArray.add(new DataUnit<>(0, 2, "int"));
        listArray.add(new DataUnit<>(0, 2, "uint"));
        parse.parseDataList(listArray);
        listArray.forEach(System.out::println);
    }
}

/* result
DataUnit{name='', description='', unit='', bytes=[], value=[false, true, false, false], byteOffset=3, bitOffset=6, count=4, dataType='bool', littleEndian=false}
DataUnit{name='', description='', unit='', bytes=[], value=[-127, 0], byteOffset=3, bitOffset=0, count=2, dataType='byte', littleEndian=false}
DataUnit{name='', description='', unit='', bytes=[], value=[129, 0], byteOffset=3, bitOffset=0, count=2, dataType='ubyte', littleEndian=false}
DataUnit{name='', description='', unit='', bytes=[], value=[0, 25689], byteOffset=4, bitOffset=0, count=2, dataType='short', littleEndian=false}
DataUnit{name='', description='', unit='', bytes=[], value=[-127, 25689], byteOffset=0, bitOffset=0, count=2, dataType='int', littleEndian=false}
DataUnit{name='', description='', unit='', bytes=[], value=[4294967169, 25689], byteOffset=0, bitOffset=0, count=2, dataType='uint', littleEndian=false}
*/
```

## 3. Byte Parse Tool

You can use **ByteReadBuff** tool, when a large amount of byte array data is collected and needs to be converted into
the desired data.<br>
The tool adopts big-endian mode by default, and uses DCBA for 4-byte data parsing, which can be modified according to
requirements.

### 1. Boolean data type

```java
class Demo {
    public static void main(String[] args) {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x55});
        // fetch bool data, internal index automatically moved back
        boolean b1 = buff.getBoolean(0);
        // get data by specifies the byte index address
        boolean b2 = buff.getBoolean(0, 1);
    }
}
```

### 2. Byte data type

```java
class Demo {
    public static void main(String[] args) {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x55, (byte) 0x33, (byte) 0x22});
        // extract a byte
        buff.getByte();
        // extract two bytes
        byte[] actual = buff.getBytes(2);
        assertArrayEquals(new byte[]{(byte) 0x33, (byte) 0x22}, actual);

        buff = new ByteReadBuff(new byte[]{(byte) 0x55, (byte) 0x33, (byte) 0x22});
        // extract a byte
        buff.getByte();
        // extract all remaining bytes
        actual = buff.getBytes();
        assertArrayEquals(new byte[]{(byte) 0x33, (byte) 0x22}, actual);
    }
}
```

### 3. Uint16 data type

```java
class Demo {
    public static void main(String[] args) {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x5F, (byte) 0xF5});
        int actual = buff.getUInt16();
        assertEquals(24565, actual);
    }
}
```

### 4. Int16 data type

```java
class Demo {
    public static void main(String[] args) {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x5F, (byte) 0xF5});
        short actual = buff.getInt16();
        assertEquals(24565, actual);
    }
}
```

### 5. Uint32 data type

```java
class Demo {
    public static void main(String[] args) {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x00, (byte) 0x20, (byte) 0x37, (byte) 0x36});
        long actual = buff.getUInt32();
        assertEquals(2111286L, actual);
    }
}
```

### 6. Int32 data type

```java
class Demo {
    public static void main(String[] args) {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x00, (byte) 0x20, (byte) 0x37, (byte) 0x36});
        int actual = buff.getInt32();
        assertEquals(2111286, actual);
    }
}
```

### 7. Float32 data type

```java
class Demo {
    public static void main(String[] args) {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x42, (byte) 0x04, (byte) 0xA3, (byte) 0xD7});
        float actual = buff.getFloat32();
        assertEquals(33.16f, actual, 0.001);
    }
}
```

### 8. Float64 data type

```java
class Demo {
    public static void main(String[] args) {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x41, (byte) 0x03, (byte) 0x1F, (byte) 0xCA, (byte) 0xD6, (byte) 0x21, (byte) 0x39, (byte) 0xB7});
        double actual = buff.getFloat64();
        assertEquals(156665.35455556, actual, 0.001);
    }
}
```

### 9. String data type

```java
class Demo {
    public static void main(String[] args) {
        ByteReadBuff buff = new ByteReadBuff(new byte[]{(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33});
        String actual = buff.getString(4);
        assertEquals("0123", actual);
    }
}
```