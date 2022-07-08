# IOT-COMMUNICATION

![Maven-v1.2.0](https://img.shields.io/badge/Maven-v1.2.0-brightgreen)
![Language-java8](https://img.shields.io/badge/Language-java8-blue)
![Idea-2018.02.04](https://img.shields.io/badge/Idea-2018.02.04-lightgrey)
![CopyRight-Oscura](https://img.shields.io/badge/CopyRight-Oscura-yellow)

## CopyRight

@2019-2022 Oscura, All Rights Reserved

## How to get（如何获取）

```
<dependency>
    <groupId>com.github.xingshuangs</groupId>
    <artifactId>iot-communication</artifactId>
    <version>1.2.0</version>
</dependency>
```

## Description（简介）

Now, it is a tool for iot communication. 
1. It includes Siemens S7 protocol, it can access to S1200.（包含西门子S7协议，目前可以访问西门子1200）
2. It includes ModbusTCP protocol.（包含modbusTCP通信协议）
3. It can parse byte array data.（可以进行字节数组数据解析）


# Instance（示例）

## 1、Siemens S7 protocol（西门子S7协议）

> read data(读)
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
      
      // read byte
      byte byteData = s7PLC.readByte("DB14.0");
      byte[] byteDatas = s7PLC.readByte("DB14.0", 4);
      
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

> write data(写)
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
      
      // write UInt16
      s7PLC.writeUInt16("DB2.0", 0x2222);
      
      // write UInt32
      s7PLC.writeUInt32("DB2.0", 0x11111122);
      
      // write float32
      s7PLC.writeFloat32("DB2.0", 12);
      
      // write float64
      s7PLC.writeFloat64("DB2.0", 12.02);
      
      // write String
      s7PLC.writeString("DB14.4","demo");
      
      // write multi address
      MultiAddressWrite addressWrite = new MultiAddressWrite();
      addressWrite.addByte("DB2.0", (byte) 0x11)
                .addUInt16("DB2.2", 88)
                .addBoolean("DB2.1.0", true);
      s7PLC.writeMultiData(addressWrite);
    }
}
```

> control(控制)
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
---

## 2、ModbusTCP protocol（ModbusTCP协议）

> read data(读)
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

> write data(写)
```java
class Demo {
    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp(1, "127.0.0.1");

        // single write coil
        plc.writeCoil(0, true);

        // multiple write coil
        List<Boolean> booleans = new ArrayList<>();
        booleans.add(true);
        booleans.add(false);
        booleans.add(true);
        booleans.add(false);
        plc.writeCoil(0, booleans);

        // single write hold register
        plc.writeHoldRegister(0, 33);
        // multiple write hold register
        plc.writeHoldRegister(3, new byte[]{(byte) 0x11, (byte) 0x12});
        // multiple write hold register
        List<Integer> integers = new ArrayList<>();
        integers.add(11);
        integers.add(12);
        integers.add(13);
        integers.add(14);
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

## 3、Byte array parse（字节数据解析）

> Get single data
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
        list.add(new DataUnit<>(8,"float"));
        list.add(new DataUnit<>(12,"double"));
        list.add(new DataUnit<>(20, 9, "string"));
        parse.parseDataList(list);
        list.forEach(x-> System.out.println(x.getValue()));
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

> Get Array data
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
