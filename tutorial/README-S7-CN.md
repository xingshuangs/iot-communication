# 西门子S7通信协议教程

[返回主页](../README-CN.md)

## 前言

1. 如果你不熟悉S7协议可以查看这个[地址](https://blog.csdn.net/XS_YOUYOU/article/details/124870209)
2. 对于200smartPLC的V区，就是DB1.X，例如，**V1=DB1.1，V100=DB1.100**

## 知识点

> 知识点1：地址的格式以及对应含义，兼容大小写

| 简写      |  区域  | 字节索引 | 位索引 | PLC类型    |
|---------|:----:|:----:|:---:|----------|
| DB1.1.2 | DB1区 |  1   |  2  | 1200     |
| DB2     | DB2区 |  0   |  0  | 1200     |
| DB3.3   | DB3区 |  3   |  0  | 1200     |
| D1.1.2  | DB1区 |  1   |  2  | 1200     |
| Q1.6    |  Q区  |  1   |  6  | 1200     |
| Q1      |  Q区  |  1   |  0  | 1200     |
| I2.5    |  I区  |  2   |  5  | 1200     |
| I2      |  I区  |  2   |  0  | 1200     |
| M3.2    |  M区  |  3   |  2  | 1200     |
| M3      |  M区  |  3   |  0  | 1200     |
| V2.1    |  V区  |  2   |  1  | 200Smart |
| V2      |  V区  |  2   |  0  | 200Smart |

> 知识点2：访问数据类型与JAVA数据类型和PLC数据类型对应关系

| 访问数据类型    | 数据类型名称   | 数据大小[位] | 数据大小[字节] | JAVA数据类型  | PLC数据类型     | 示例         |
|-----------|----------|:-------:|:--------:|-----------|-------------|------------|
| boolean   | 布尔类型     |    1    |   1/8    | Boolean   | BOOL        | true       |
| byte      | 字节类型     |    8    |    1     | Byte      | BYTE        | 0x11       |
| uint16    | 无符号2字节整型 |   16    |    2     | Integer   | WORD/UINT   | 65535      |
| int16     | 有符号2字节整型 |   16    |    2     | Short     | WORD/INT    | -32760     |
| uint32    | 无符号4字节整型 |   32    |    4     | Long      | DWORD/UDINT | 70000      |
| int32     | 有符号4字节整型 |   32    |    4     | Integer   | DWORD/DINT  | -70000     |
| float32   | 4字节浮点型   |   32    |    4     | Float     | REAL        | 3.14       |
| float64   | 8字节浮点型   |   64    |    8     | Double    | LREAL       | 3.14       |
| string    | 字符型      |    8    |    1     | String    | String      | ABC        |
| time      | 时间/耗时    |   32    |    4     | Long      | Time        | 100ms      |
| date      | 日期       |   16    |    2     | LocalDate | Date        | 2023-04-03 |
| timeOfDay | 一天中的时间   |   32    |    4     | LocalTime | TimeOfDay   | 10:22:11   |

## 通信连接

- PLC默认采用长连接的方式，不用的时候需要手动关闭；
- 若需要短连接，则需要手动设置；

### 1. 长连接方式

```java
class Demo {
    public static void main(String[] args) {
        // 长连接方式，即持久化为true
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        s7PLC.writeByte("DB2.1", (byte) 0x11);
        s7PLC.readByte("DB2.1");
        // 需要手动关闭
        s7PLC.close();
    }
}
```

### 2. 短连接方式

```java
class Demo {
    public static void main(String[] args) {
        // 短连接
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        // 设置短连接模式，即持久化为false
        s7PLC.setPersistence(false);
        s7PLC.writeByte("DB2.1", (byte) 0x11);
        s7PLC.readByte("DB2.1");
    }
}
```

## 客户端教程

### 1. 直接方式读写

#### 1.1 读取数据

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
        String strData1 = s7PLC.readString("DB14.4", 10);

        // read time
        long timeData = s7PLC.readTime("DB1.0");
        // read date
        LocalDate localDateData = s7PLC.readDate("DB1.0");
        // read time of day
        LocalTime localTimeOfDayData = s7PLC.readTimeOfDay("DB1.0");

        // read multi address
        MultiAddressRead addressRead = new MultiAddressRead();
        addressRead.addData("DB1.0", 1)
                .addData("DB1.2", 3)
                .addData("DB1.3", 5);
        List<byte[]> multiByte = s7PLC.readMultiByte(addressRead);

        s7PLC.close();
    }
}
```

#### 1.2 写入数据

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

        // write time
        s7PLC.writeTime("DB1.0", 1000);
        // write date
        s7PLC.writeDate("DB1.0", LocalDate.now());
        // write time of day
        s7PLC.writeTimeOfDay("DB1.0", LocalTime.now());

        // write multi address
        MultiAddressWrite addressWrite = new MultiAddressWrite();
        addressWrite.addByte("DB2.0", (byte) 0x11)
                .addUInt16("DB2.2", 88)
                .addBoolean("DB2.1.0", true);
        s7PLC.writeMultiData(addressWrite);

        s7PLC.close();
    }
}
```

#### 1.3 控制指令

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

        s7PLC.close();
    }
}
```

### 2. 自定义方式读写

```java
class Demo {
    public static void main(String[] args) {
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        // bit数据读写
        byte[] expect = new byte[]{(byte) 0x00};
        this.s7PLC.writeRaw(EParamVariableType.BIT, 1, EArea.DATA_BLOCKS, 1, 0, 3,
                EDataVariableType.BIT, expect);
        byte[] actual = this.s7PLC.readRaw(EParamVariableType.BIT, 1, EArea.DATA_BLOCKS, 1, 0, 3);
        // byte数据读写
        expect = new byte[]{(byte) 0x02, (byte) 0x03};
        this.s7PLC.writeRaw(EParamVariableType.BYTE, 2, EArea.DATA_BLOCKS, 1, 1, 0,
                EDataVariableType.BYTE_WORD_DWORD, expect);
        actual = this.s7PLC.readRaw(EParamVariableType.BYTE, 2, EArea.DATA_BLOCKS, 1, 1, 0);
        s7PLC.close();
    }
}
```

### 3. 序列化方式读写

支持BOOL UINT16 INT16 UINT32 INT32 FLOAT32 FLOAT64 STRING等数据类型读写

构建数据量比较小的数据类

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

    @S7Variable(address = "DB1.31", type = EDataType.STRING, count = 10)
    private String stringData;
}
```

对于大数据量建议采用字节数组的方式，后续采用字节数据解析

构建数据量比较大的数据类

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

数据读写

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
        bean.setStringData("1234567890");
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
        s7PLC.close();
    }
}
```

## 服务端教程

- 服务端支持默认支持I区，Q区，M区，T区，C区以及DB1区，每个区都包含65536个字节；
- 服务端可以自定义DB区，随意添加；
- 目前只支持读写操作；

### 1. 初始化

```java
class Demo {
    public static void main(String[] args) {
        // 创建服务端
        S7PLCServer server = new S7PLCServer();
        // 添加DB2，DB3，DB4
        server.addDBArea(2, 3, 4);
        // 服务端启动
        server.start();
        // 服务端停止
        server.stop();
    }
}
```

### 2. 读写数据

```java
class Demo {
    public static void main(String[] args) {
        // 创建服务端
        S7PLCServer server = new S7PLCServer();
        server.addDBArea(2, 3, 4);
        server.start();

        // 创建客户端
        S7PLC s7PLC = new S7PLC(EPlcType.S1200);
        s7PLC.writeByte("DB2.0", (byte) 0x01);
        byte b = s7PLC.readByte("DB2.0");

        // 关闭
        s7PLC.close();
        server.stop();
    }
}
```

## 西门子机床教程

```java
class Demo {
    public static void main(String[] args) {
        S7PLC s7PLC = new S7PLC(EPlcType.SINUMERIK_828D, "127.0.0.1");
        
        String cncId = s7PLC.readCncId();
        String cncVersion = s7PLC.readCncVersion();
        String cncType = s7PLC.readCncType();
        String cncManufactureDate = s7PLC.readCncManufactureDate();
        List<Double> machinePosition = s7PLC.readMachinePosition();
        List<Double> readRelativePosition = s7PLC.readRelativePosition();
        List<Double> readRemainPosition = s7PLC.readRemainPosition();
        List<Double> tWorkPiecePosition = s7PLC.readTWorkPiecePosition();
        int toolRadiusCompensationNumber = s7PLC.readToolRadiusCompensationNumber();
        int toolNumber = s7PLC.readToolNumber();
        double actSpindleSpeed = s7PLC.readActSpindleSpeed();
        double feedRate = s7PLC.readFeedRate();
        int workMode = s7PLC.readWorkMode();
        double runTime = s7PLC.readRunTime();
        double remainTime = s7PLC.readRemainTime();
        String programName = s7PLC.readProgramName();
        int alarmNumber = s7PLC.readAlarmNumber();
        
        s7PLC.close();
    }
}
```
