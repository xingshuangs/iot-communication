# Siemens S7 Protocol Tutorial

[HOME BACK](../README.md)

## Foreword

1. You can check this [address](https://blog.csdn.net/XS_YOUYOU/article/details/124870209) if
   you're not familiar with the S7 protocol. <br>
2. 200smartPLC, V Area == DB1.X. Example: **V1=DB1.1, V100=DB1.100**

## Tips

> Tips1: Format and meaning of the address, case compatible.

| Abbr    | Area | Byte Index | Bit Index | PLC Type |
|---------|:----:|:----------:|:---------:|----------|
| DB1.1.2 | DB1  |     1      |     2     | 1200     |
| DB2     | DB2  |     0      |     0     | 1200     |
| DB3.3   | DB3  |     3      |     0     | 1200     |
| D1.1.2  | DB1  |     1      |     2     | 1200     |
| Q1.6    |  Q   |     1      |     6     | 1200     |
| Q1      |  Q   |     1      |     0     | 1200     |
| I2.5    |  I   |     2      |     5     | 1200     |
| I2      |  I   |     2      |     0     | 1200     |
| M3.2    |  M   |     3      |     2     | 1200     |
| M3      |  M   |     3      |     0     | 1200     |
| V2.1    |  V   |     2      |     1     | 200Smart |
| V2      |  V   |     2      |     0     | 200Smart |

> Tips2: Access data types mapping to JAVA data types and PLC data types.

| Access Data Type | Data Size in Bit | Data Size in Byte | JAVA Data Type | PLC Data Type | Instance   |
|------------------|:----------------:|:-----------------:|----------------|---------------|------------|
| boolean          |        1         |        1/8        | Boolean        | BOOL          | true       |
| byte             |        8         |         1         | Byte           | BYTE          | 0x11       |
| uint16           |        16        |         2         | Integer        | WORD/UINT     | 65535      |
| int16            |        16        |         2         | Short          | WORD/INT      | -32760     |
| uint32           |        32        |         4         | Long           | DWORD/UDINT   | 70000      |
| int32            |        32        |         4         | Integer        | DWORD/DINT    | -70000     |
| float32          |        32        |         4         | Float          | REAL          | 3.14       |
| float64          |        64        |         8         | Double         | LREAL         | 3.14       |
| string           |        8         |         1         | String         | String        | ABC        |
| time             |        32        |         4         | Long           | Time          | 100ms      |
| date             |        16        |         2         | LocalDate      | Date          | 2023-04-03 |
| timeOfDay        |        32        |         4         | LocalTime      | TimeOfDay     | 10:22:11   |

## Communication Connection

- By default, the long connection mode is adopted. You need to close connection manually when it is not in use.
- If a short connection is required, you need to set it manually.

### 1. Long Connection Mode

```java
class Demo {
    public static void main(String[] args) {
        // long connection mode, persistence = true
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        s7PLC.writeByte("DB2.1", (byte) 0x11);
        s7PLC.readByte("DB2.1");
        // close it manually
        s7PLC.close();
    }
}
```

### 2. Short Connection Mode

```java
class Demo {
    public static void main(String[] args) {
        // short connection mode
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        // set short connection mode，persistence = false
        s7PLC.setPersistence(false);
        s7PLC.writeByte("DB2.1", (byte) 0x11);
        s7PLC.readByte("DB2.1");
    }
}
```

## Client Tutorial

### 1. Direct Mode Read-write

#### 1.1 Read Data

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

#### 1.2 Write Data

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
        s7PLC.writeByte("V1", (byte) 0x11); // 200smart

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

#### 1.3 Control Command

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

### 2. Custom Mode Read-write

```java
class Demo {
    public static void main(String[] args) {
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        // bit data read-write
        byte[] expect = new byte[]{(byte) 0x00};
        this.s7PLC.writeRaw(EParamVariableType.BIT, 1, EArea.DATA_BLOCKS, 1, 0, 3,
                EDataVariableType.BIT, expect);
        byte[] actual = this.s7PLC.readRaw(EParamVariableType.BIT, 1, EArea.DATA_BLOCKS, 1, 0, 3);
        // byte data read-write
        expect = new byte[]{(byte) 0x02, (byte) 0x03};
        this.s7PLC.writeRaw(EParamVariableType.BYTE, 2, EArea.DATA_BLOCKS, 1, 1, 0,
                EDataVariableType.BYTE_WORD_DWORD, expect);
        actual = this.s7PLC.readRaw(EParamVariableType.BYTE, 2, EArea.DATA_BLOCKS, 1, 1, 0);

        s7PLC.close();
    }
}
```

### 3. Serializer Mode Read-write

Support BOOL UINT16 INT16 UINT32 INT32 FLOAT32 FLOAT64 STRING read-write.

Create small size data class.

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

For large amounts of data, byte array is recommended.

Create big size data class.

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

Read and write

```java
class Demo {
    public static void main(String[] args) {
        // create PLC instance
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        // create S7Serializer instance
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);

        // small size data read-write
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

        // big size data read-write
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

## Server Tutorial

- By default, the server supports area I, Q, M, T, C and DB1, each includes 65536 bytes.
- The server can customize the DB area and add it at will.
- Currently, only read and write operations are supported.

### 1. Initialization

```java
class Demo {
    public static void main(String[] args) {
        // create server
        S7PLCServer server = new S7PLCServer();
        // add DB2，DB3，DB4
        server.addDBArea(2, 3, 4);
        // server start
        server.start();
        // server stop
        server.stop();
    }
}
```

### 2. Read-write Data

```java
class Demo {
    public static void main(String[] args) {
        // create server
        S7PLCServer server = new S7PLCServer();
        server.addDBArea(2, 3, 4);
        server.start();

        // create client
        S7PLC s7PLC = new S7PLC(EPlcType.S1200);
        s7PLC.writeByte("DB2.0", (byte) 0x01);
        byte b = s7PLC.readByte("DB2.0");

        // close
        s7PLC.close();
        server.stop();
    }
}
```