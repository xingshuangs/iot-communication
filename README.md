# IOT-COMMUNICATION

![Maven-v1.0.2](https://img.shields.io/badge/Maven-v1.0.2-brightgreen)
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
    <version>1.0.2</version>
</dependency>
```

## Description（简介）

Now, it is a tool for iot communication. 
1. It includes Siemens S7 protocol, it can access to S1200.（包含西门子S7协议，目前可以访问西门子1200）
2. It can parse byte array data.（可以进行字节数组数据解析）


# Instance（示例）

## 1、Siemens S7 protocol（西门子S7协议）

> read data(读)
```java
class Demo{
    public static void main(String[] args){
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
class Demo{
    public static void main(String[] args){
      S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
      // write boolean
      s7PLC.writeBoolean("DB2.0.7", true);
      s7PLC.writeBoolean("I0.5", true);
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
class Demo{
    public static void main(String[] args){
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

## 2、Byte array parse（字节数据解析）

> Get single data
```java
/*********************************** EXAMPLE1 ***********************************/
class Demo{
    public static void main(String[] args){
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
class Demo{
    public static void main(String[] args){
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
