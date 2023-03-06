# IOT-COMMUNICATION

![Maven-v1.2.6](https://img.shields.io/badge/Maven-v1.2.6-brightgreen)
![Language-java8](https://img.shields.io/badge/Language-java8-blue)
![Idea-2022.02.03](https://img.shields.io/badge/Idea-2022.02.03-lightgrey)
![CopyRight-Oscura](https://img.shields.io/badge/CopyRight-Oscura-yellow)

## CopyRight

**If this helps you, please give the author a :star: .**<br>
**如果这个库对你有帮助，请给作者一颗星星**<br>

You can add QQ group( **759101350** ) or send email( **xingshuang_cool@163.com** ) if you have questions, the author will give
answers in his spare time. <br>
如果有任何问题，可以添加QQ群( **759101350** ) 或者发送邮件( **xingshuang_cool@163.com** )
，作者在空余时间会做解答。<br>

@2019 - 9999 Oscura, All Rights Reserved <br>

## How to get（如何获取）

```
<dependency>
    <groupId>com.github.xingshuangs</groupId>
    <artifactId>iot-communication</artifactId>
    <version>1.2.6</version>
</dependency>
```

## Description（简介）

Now, it is a tool for iot communication.

1. It includes Siemens S7 protocol, it can access to S1500, S1200, S200smart.（包含西门子S7协议，目前可以访问西门子S1500，S1200，S200smart）
2. It includes ModbusTCP protocol.（包含modbusTCP通信协议）
3. It can parse byte array data.（可以进行字节数组数据解析）

# Instance（示例）

## 1. Siemens S7 protocol（西门子S7协议）

You can check this [address](https://blog.csdn.net/XS_YOUYOU/article/details/124870209) if
you're not familiar with the S7 protocol. <br>(
如果你不熟悉S7协议可以查看这个[地址](https://blog.csdn.net/XS_YOUYOU/article/details/124870209)
)<br>
对于200smartPLC的V区，就是DB1.X，例如，**V1=DB1.1，V100=DB1.100**

> Tips1: Format and meaning of the address, case compatible. <br> 知识点1：地址的格式以及对应含义，兼容大小写

| Abbr<br/>(简写) | Area<br/>(区域) | Byte Index<br/>(字节索引) | Bit Index<br/>(位索引) | PLC Type<br/>(PLC类型) |
|---------------|:-------------:|:---------------------:|:-------------------:|----------------------|
| DB1.1.2       |     DB1区      |           1           |          2          | 1200                 |
| DB2           |     DB2区      |           0           |          0          | 1200                 |
| DB3.3         |     DB3区      |           3           |          0          | 1200                 |
| D1.1.2        |     DB1区      |           1           |          2          | 1200                 |
| Q1.6          |      Q区       |           1           |          6          | 1200                 |
| Q1            |      Q区       |           1           |          0          | 1200                 |
| I2.5          |      I区       |           2           |          5          | 1200                 |
| I2            |      I区       |           2           |          0          | 1200                 |
| M3.2          |      M区       |           3           |          2          | 1200                 |
| M3            |      M区       |           3           |          0          | 1200                 |
| V2.1          |      V区       |           2           |          1          | 200Smart             |
| V2            |      V区       |           2           |          0          | 200Smart             |

> Tips2: Access data types mapping to JAVA data types and PLC data types. <br> 知识点2：访问数据类型与JAVA数据类型和PLC数据类型对应关系

| Access Data Type<br/>(访问数据类型) | Data Type Name<br/>(数据类型名称) | Data Size in Bit<br/>(数据大小[位]) | Data Size in Byte<br/>(数据大小[字节]) | JAVA Data Type<br/>(JAVA数据类型) | PLC Data Type<br/>(PLC数据类型) | Instance<br/>(示例) |
|-------------------------------|-----------------------------|:------------------------------:|:--------------------------------:|-------------------------------|-----------------------------|-------------------|
| boolean                       | 布尔类型                        |               1                |               1/8                | Boolean                       | BOOL                        | true              |
| byte                          | 字节类型                        |               8                |                1                 | Byte                          | BYTE                        | 0x11              |
| uint16                        | 无符号2字节整型                    |               16               |                2                 | Integer                       | WORD/UINT                   | 65535             |
| int16                         | 有符号2字节整型                    |               16               |                2                 | Short                         | WORD/INT                    | -32760            |
| uint32                        | 无符号4字节整型                    |               32               |                4                 | Long                          | DWORD/UDINT                 | 70000             |
| int32                         | 有符号4字节整型                    |               32               |                4                 | Integer                       | DWORD/DINT                  | -70000            |
| float32                       | 4字节浮点型                      |               32               |                4                 | Float                         | REAL                        | 3.14              |
| float64                       | 8字节浮点型                      |               64               |                8                 | Double                        | LREAL                       | 3.14              |
| string                        | 字符型                         |               8                |                1                 | String                        | String                      | ABC               |

### 1.1 read data(读)

```java
class Demo {
    public static void main(String[] args) {
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        // read boolean
        boolean boolData = s7PLC.readBoolean("DB1.2.0");
        List<Boolean> boolDatas = s7PLC.readBoolean("DB1.2.0", "DB1.2.1", "DB1.2.7");
        List<Boolean> iDatas = s7PLC.readBoolean("I0.0", "I0.1", "I0.2", "I0.3", "I0.4", "I0.5");
        List<Boolean> qDatas = s7PLC.readBoolean("Q0.0", "Q0.1", "Q0.2", "Q0.3", "Q0.4", "Q0.5", "Q0.6", "Q0.7");
        List<Boolean> mDatas = s7PLC.readBoolean("M1.0", "M1.1", "M1.2", "M1.3", "M1.4", "M1.5", "M1.6", "M1.7");
        List<Boolean> vDatas = s7PLC.readBoolean("V1.0", "V1.1", "V1.2", "V1.3", "V1.4", "V1.5", "V1.6", "V1.7"); // 200smart有V区

        // read byte
        byte byteData = s7PLC.readByte("DB14.0");
        byte[] byteDatas = s7PLC.readByte("DB14.0", 4);
        byte iByteData = s7PLC.readByte("I0");
        byte qByteData = s7PLC.readByte("Q0");
        byte mByteData = s7PLC.readByte("M0");
        byte vByteData = s7PLC.readByte("V0"); // 200smart有V区

        // read UInt16
        int intData = s7PLC.readUInt16("DB14.0");
        List<Integer> intDatas = s7PLC.readUInt16("DB1.0", "DB1.2");

        // read UInt32
        long int32Data = s7PLC.readUInt32("DB1.0");
        List<Long> int32Datas = s7PLC.readUInt32("DB1.0", "DB1.4");

        // read float32
        float float32Data = s7PLC.readFloat32("DB1.0");
        List<Float> float32Datas = s7PLC.readFloat32("DB1.0", "DB1.4");

        // read float64
        double float64Data = s7PLC.readFloat64("DB1.0");
        List<Double> float64Datas = s7PLC.readFloat64("DB1.0", "DB1.4");

        // read String
        String strData = s7PLC.readString("DB14.4");

        // read multi address
        MultiAddressRead addressRead = new MultiAddressRead();
        addressRead.addData("DB1.0", 1)
                .addData("DB1.2", 3)
                .addData("DB1.3", 5);
        List<byte[]> multiByte = s7PLC.readMultiByte(addressRead);
    }
}
```

### 1.2 write data(写)

```java
class Demo {
    public static void main(String[] args) {
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        // write boolean
        s7PLC.writeBoolean("DB2.0.7", true);
        s7PLC.writeBoolean("Q0.7", true);
        s7PLC.writeBoolean("M1.4", true);

        // write byte
        s7PLC.writeByte("DB2.1", (byte) 0x11);
        s7PLC.writeByte("M1", (byte) 0x11);
        s7PLC.writeByte("V1", (byte) 0x11); // 200smart有V区

        // write UInt16
        s7PLC.writeUInt16("DB2.0", 0x2222);

        // write UInt32
        s7PLC.writeUInt32("DB2.0", 0x11111122);

        // write float32
        s7PLC.writeFloat32("DB2.0", 12);

        // write float64
        s7PLC.writeFloat64("DB2.0", 12.02);

        // write String
        s7PLC.writeString("DB14.4", "demo");

        // write multi address
        MultiAddressWrite addressWrite = new MultiAddressWrite();
        addressWrite.addByte("DB2.0", (byte) 0x11)
                .addUInt16("DB2.2", 88)
                .addBoolean("DB2.1.0", true);
        s7PLC.writeMultiData(addressWrite);
    }
}
```

### 1.3 control(控制)

```java
class Demo {
    public static void main(String[] args) {
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        // hot restart
        s7PLC.hotRestart();

        // cold restart
        s7PLC.coldRestart();

        // plc stop
        s7PLC.plcStop();

        // copy ram to rom
        s7PLC.copyRamToRom();

        // compress
        s7PLC.compress();
    }
}
```

### 1.4 serializer(序列化的方式)

create small size data class (构建数据量比较小的类型数据)

```java

@Data
public class DemoBean {

    @S7Variable(address = "DB1.0.1", type = EDataType.BOOL)
    private boolean bitData;

    @S7Variable(address = "DB1.4", type = EDataType.UINT16)
    private int uint16Data;

    @S7Variable(address = "DB1.6", type = EDataType.INT16)
    private short int16Data;

    @S7Variable(address = "DB1.8", type = EDataType.UINT32)
    private long uint32Data;

    @S7Variable(address = "DB1.12", type = EDataType.INT32)
    private int int32Data;

    @S7Variable(address = "DB1.16", type = EDataType.FLOAT32)
    private float float32Data;

    @S7Variable(address = "DB1.20", type = EDataType.FLOAT64)
    private double float64Data;

    @S7Variable(address = "DB1.28", type = EDataType.BYTE, count = 3)
    private byte[] byteData;
}
```

create big size data class (构建数据量比较大的数据类型)

```java

@Data
public class DemoLargeBean {

    @S7Variable(address = "DB1.0.1", type = EDataType.BOOL)
    private boolean bitData;

    @S7Variable(address = "DB1.10", type = EDataType.BYTE, count = 50)
    private byte[] byteData1;

    @S7Variable(address = "DB1.60", type = EDataType.BYTE, count = 65)
    private byte[] byteData2;

    @S7Variable(address = "DB1.125", type = EDataType.BYTE, count = 200)
    private byte[] byteData3;

    @S7Variable(address = "DB1.325", type = EDataType.BYTE, count = 322)
    private byte[] byteData4;

    @S7Variable(address = "DB1.647", type = EDataType.BYTE, count = 99)
    private byte[] byteData5;

    @S7Variable(address = "DB1.746", type = EDataType.BYTE, count = 500)
    private byte[] byteData6;

    @S7Variable(address = "DB1.1246", type = EDataType.BYTE, count = 44)
    private byte[] byteData7;
}
```

read and write (数据读写)

```java
class Demo {
    public static void main(String[] args) {
        // 构建PLC对象
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        // 构建序列化对象
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);

        // 小数据量的读写
        DemoBean bean = s7Serializer.read(DemoBean.class);
        bean.setBitData(true);
        bean.setUint16Data(42767);
        bean.setInt16Data((short) 32767);
        bean.setUint32Data(3147483647L);
        bean.setInt32Data(2147483647);
        bean.setFloat32Data(3.14f);
        bean.setFloat64Data(4.15);
        bean.setByteData(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        s7Serializer.write(bean);

        // 大数据量的读写
        DemoLargeBean largeBean = s7Serializer.read(DemoLargeBean.class);
        largeBean.getByteData2()[0] = (byte) 0x05;
        largeBean.getByteData3()[0] = (byte) 0x05;
        largeBean.getByteData4()[0] = (byte) 0x05;
        largeBean.getByteData5()[0] = (byte) 0x05;
        largeBean.getByteData6()[0] = (byte) 0x05;
        largeBean.getByteData7()[0] = (byte) 0x05;
        largeBean.getByteData2()[64] = (byte) 0x02;
        largeBean.getByteData3()[199] = (byte) 0x03;
        largeBean.getByteData4()[321] = (byte) 0x04;
        largeBean.getByteData5()[98] = (byte) 0x05;
        largeBean.getByteData6()[499] = (byte) 0x06;
        largeBean.getByteData7()[43] = (byte) 0x07;
        s7Serializer.write(bean);
    }
}
```

---

## 2. ModbusTCP protocol（ModbusTCP协议）

### 2.1 read data(读)

```java
class Demo {
    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp(1, "127.0.0.1");

        // read coil
        List<Boolean> readCoil = plc.readCoil(0, 2);

        // read discrete input
        List<Boolean> readDiscreteInput = plc.readDiscreteInput(0, 4);

        // read hold register
        byte[] readHoldRegister = plc.readHoldRegister(0, 4);

        // read input register
        byte[] readInputRegister = plc.readInputRegister(0, 2);

        // hold register read Int16
        short readInt16 = plc.readInt16(2);

        // hold register read UInt16
        int readUInt16 = plc.readUInt16(2);

        // hold register read Int32
        int readInt32 = plc.readInt32(2);

        // hold register read Int32
        long readUInt32 = plc.readUInt32(2);

        // hold register read Float32
        float readFloat32 = plc.readFloat32(2);

        // hold register read Float64
        double readFloat64 = plc.readFloat64(2);

        // hold register read String
        String readString = plc.readString(2, 4);
    }
}
```

### 2.2 write data(写)

```java
class Demo {
    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp(1, "127.0.0.1");

        // single write coil
        plc.writeCoil(0, true);

        // multiple write coil
        List<Boolean> booleans = Arrays.asList(true, false, true, false);
        plc.writeCoil(0, booleans);

        // single write hold register
        plc.writeHoldRegister(0, 33);
        // multiple write hold register
        plc.writeHoldRegister(3, new byte[]{(byte) 0x11, (byte) 0x12});
        // multiple write hold register
        List<Integer> integers = Arrays.asList(11, 12, 13, 14);
        plc.writeHoldRegister(3, integers);

        // hold register write int16
        plc.writeInt16(2, (short) 10);

        // hold register write uint16
        plc.writeUInt16(2, 20);

        // hold register write int32
        plc.writeInt32(2, 32);

        // hold register write uint32
        plc.writeUInt32(2, 32L);

        // hold register write float32
        plc.writeFloat32(2, 12.12f);

        // hold register write float64
        plc.writeFloat64(2, 33.21);

        // hold register write String
        plc.writeString(2, "1234");
    }
}
```

---

## 3. Byte array parse（字节数据解析）

### 3.1 serializer(序列化的方式)

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

### 3.2 Get single data

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

### 3.3 Get Array data

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
