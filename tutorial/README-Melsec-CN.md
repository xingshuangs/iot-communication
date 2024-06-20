# 三菱MELSEC(MC)通信协议教程

[返回主页](../README-CN.md)

## 前言

> 注意点

- 软元件分为位软元件和字软元件
- **1** 个字软元件地址 = **2** 个字节；
- 采用小端模式，**4 字节**数据的编码格式 = **AB_CD**； (大端模式 = **DC_BA**，小端模式 = **AB_CD**)
- 只支持二进制方式读写，不支持ASCII方式读写
- 采用TCP的方式，不支持串口
- 支持三菱PLC: iQ-R系列, Q/L系列, QnA系列, A系列, 目前测试了L系列（L02CPU），FX系列（FX5U-32M）
- 支持自动重连

> PLC系列

|  系列  | 帧类型 | 连接  |  型号  |
|:----:|:---:|-----|:----:|
|  A   | 1E  | 以太网 | FX3U |
| QnA  | 3E  | 以太网 | FX5U |
| Q/L  | 3E  | 以太网 | Q/L  |
| IQ-R |  -  | 以太网 |  -   |
| IQ-L |  -  | 以太网 |  -   |

> 地址格式，兼容大小写

|  简写   | 软元件名   | 符号  |  地址   |  类型  | 地址的进制    |
|:-----:|:-------|:---:|:-----:|:----:|:---------|
| SM10  | 特殊继电器  | SM  |  10   | BIT  | 10进制     |
| SD12  | 特殊寄存器  | SD  |  12   | WORD | 10进制     |
|  X2F  | 输入     |  X  | 0x2F  | BIT  | **16进制** |
| Y12F  | 输出     |  Y  | 0x12F | BIT  | **16进制** |
| M100  | 内部继电器  |  M  |  100  | BIT  | 10进制     |
|  L10  | 锁存继电器  |  L  |  10   | BIT  | 10进制     |
|  F10  | 报警器    |  F  |  10   | BIT  | 10进制     |
|  V9   | 变址继电器  |  V  |   9   | BIT  | 10进制     |
|  B2F  | 链接继电器  |  B  | 0x2F  | BIT  | **16进制** |
| D100  | 数据寄存器  |  D  |  100  | WORD | 10进制     |
|  W1F  | 链接寄存器  |  W  | 0x1F  | WORD | **16进制** |
| TN100 | 定时器当前值 | TN  |  100  | WORD | 10进制     |
| CN100 | 计数器当前值 | CN  |  100  | WORD | 10进制     |

> 快捷访问接口

| 序号  | 方法           | 软元件数量 | 字节大小 | 位大小 | 含义        |
|:---:|:-------------|:-----:|:----:|:---:|:----------|
|  1  | readBoolean  |   1   | 1/8  |  1  | 读取boolean |
|  2  | readInt16    |   1   |  2   | 16  | 读取Int16   |
|  3  | readUInt16   |   1   |  2   | 16  | 读取UInt16  |
|  4  | readInt32    |   2   |  4   | 32  | 读取Int32   |
|  5  | readUInt32   |   2   |  4   | 32  | 读取UInt32  |
|  6  | readFloat32  |   2   |  4   | 32  | 读取Float32 |
|  7  | readFloat64  |   4   |  8   | 64  | 读取Float64 |
|  8  | readString   |   n   |  2n  | 16n | 读取字符串     |
|  9  | writeBoolean |   1   | 1/8  |  1  | 写入boolean |
| 10  | writeInt16   |   1   |  2   | 16  | 写入Int16   |
| 11  | writeUInt16  |   1   |  2   | 16  | 写入UInt16  |
| 12  | writeInt32   |   2   |  4   | 32  | 写入Int32   |
| 13  | writeUInt32  |   2   |  4   | 32  | 写入UInt32  |
| 14  | writeFloat32 |   2   |  4   | 32  | 写入Float32 |
| 15  | writeFloat64 |   4   |  8   | 64  | 写入Float32 |
| 16  | writeString  |   n   |  2n  | 16n | 写入字符串     |

## 打印报文

如果想知道通信过程中的实际输入输出报文内容，可以添加报文信息打印

```java
class Demo {
    public static void main(String[] args) {
        McPLC mcPLC = new McPLC(EMcSeries.QnA, "127.0.0.1", 6000);
        // optional
        mcPLC.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));
        // read boolean
        boolean booleanData = mcPLC.readBoolean("M100");
        mcPLC.close();
    }
}
```

## 通信连接

- 默认采用长连接的方式，不用的时候需要手动关闭；
- 若需要短连接，则需要手动设置；

### 1. 长连接方式

```java
class Demo {
    public static void main(String[] args) {
        // 长连接方式，即持久化为true
        McPLC mcPLC = new McPLC(EMcSeries.QnA, "127.0.0.1", 6000);
        boolean booleanData = mcPLC.readBoolean("M100");
        // 需要手动关闭
        plc.close();
    }
}
```

### 2. 短连接方式

```java
class Demo {
    public static void main(String[] args) {
        // 短连接
        McPLC mcPLC = new McPLC(EMcSeries.QnA, "127.0.0.1", 6000);
        // 设置短连接模式，即持久化为false
        mcPLC.setPersistence(false);
        boolean booleanData = mcPLC.readBoolean("M100");
    }
}
```

## 常规读写

### 1. 读取数据

```java
class Demo {
    public static void main(String[] args) {
        McPLC mcPLC = new McPLC(EMcSeries.QnA, "127.0.0.1", 6000);

        // optional
        mcPLC.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));

        // read boolean
        boolean booleanData = mcPLC.readBoolean("M100");

        // read boolean list
        List<Boolean> booleanList = mcPLC.readBoolean("M100", 10);

        // read one byte
        byte byteData = mcPLC.readByte("D100");

        // read multi byte
        byte[] bytes = mcPLC.readBytes("D100", 10);

        // read int16
        short int16Data = mcPLC.readInt16("D100");

        // read int16 list
        List<Integer> int16List = mcPLC.readUInt16("D100", "D108", "D130");

        // read uint16
        int uint16Data = mcPLC.readUInt16("D100");

        // read uint16 list
        List<Integer> uint16List = mcPLC.readUInt16("D100", "D108", "D130");

        // read int32
        int int32Data = mcPLC.readInt32("D100");

        // read int32 list
        List<Integer> int32List = mcPLC.readInt32("D100", "D108", "D130");

        // read uint32
        long uint32Data = mcPLC.readUInt32("D100");

        // read uint32 list
        List<Long> uint32List = mcPLC.readUInt32("D100", "D108", "D130");

        // read float32
        float float32Data = mcPLC.readFloat32("D100");

        // read float32 list
        List<Float> float32List = mcPLC.readFloat32("D100", "D108", "D130");

        // read float64
        double float64Data = mcPLC.readFloat64("D100");

        // read string
        String stringData = mcPLC.readString("D100", 6);

        // read multi address, only support word and dword
        McMultiAddressRead addressRead = new McMultiAddressRead();
        addressRead.addWordData("D100");
        addressRead.addWordData("D150");
        addressRead.addWordData("D130");
        addressRead.addWordData("D110");
        addressRead.addDWordData("D112");
        addressRead.addDWordData("D116");
        addressRead.addDWordData("D126");
        List<McDeviceContent> contentList = mcPLC.readMultiAddress(addressRead);

        mcPLC.close();
    }
}
```

### 2. 写入数据

```java
class Demo {
    public static void main(String[] args) {
        McPLC mcPLC = new McPLC(EMcSeries.QnA, "127.0.0.1", 6000);

        // optional
        mcPLC.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));

        // write one boolean
        mcPLC.writeBoolean("M100", true);

        // write multi boolean
        mcPLC.writeBoolean("M100", true, false, true);

        // write one byte
        mcPLC.writeByte("D100", (byte) 0x01);

        // write multi byte
        mcPLC.writeBytes("D100", new byte[]{0x01, 0x02, 0x03});

        // write one int16
        mcPLC.writeInt16("D100", (short) 16);

        // write multi int16
        mcPLC.writeInt16("D100", (short) 16, (short) 17, (short) 118);

        // write one uint16
        mcPLC.writeUInt16("D100", 16);

        // write multi uint16
        mcPLC.writeUInt16("D100", 16, 17, 18);

        // write one int32
        mcPLC.writeInt32("D100", 55);

        // write multi int32
        mcPLC.writeInt32("D100", 55, 66, 77);

        // write one uint32
        mcPLC.writeUInt32("D100", 55);

        // write multi uint32
        mcPLC.writeUInt32("D100", 55L, 66L, 77L);

        // write one float32
        mcPLC.writeFloat32("D100", 0.123f);

        // write multi float32
        mcPLC.writeFloat32("D100", 0.123f, 145.56f, 88.12f);

        // write one float64
        mcPLC.writeFloat64("D100", 0.123);

        // write multi float64
        mcPLC.writeFloat64("D100", 0.123, 145.56, 88.12);

        // write string
        mcPLC.writeString("D100", "1234567890");

        // multi write, only support int16 uint16 int32 uint32 float32
        McMultiAddressWrite addressWrite = new McMultiAddressWrite();
        addressWrite.addInt16("D100", 110);
        addressWrite.addUInt16("D101", 110);
        addressWrite.addInt32("D102", 110);
        addressWrite.addUInt32("D104", 110);
        addressWrite.addFloat32("D106", 110);
        mcPLC.writeMultiAddress(addressWrite);

        mcPLC.close();
    }
}
```

## 自定义读写

```java
class Demo {
    public static void main(String[] args) {
        McPLC mcPLC = new McPLC(EMcSeries.QnA, "127.0.0.1", 6000);

        // optional
        mcPLC.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));

        // ------------------ read and write device batch in word ------------------------
        byte[] expect = new byte[]{0x34, 0x12, 0x02, 0x00};
        McDeviceContent reqContent = McDeviceContent.createBy("D110", 2, expect);
        mcPLC.writeDeviceBatchInWord(reqContent);
        McDeviceAddress address = McDeviceAddress.createBy("D110", 2);
        McDeviceContent ackContent = mcPLC.readDeviceBatchInWord(address);

        // ------------------ read and write device batch in bit --------------------------
        expect = new byte[]{0x11, 0x00, 0x01, 0x10};
        reqContent = McDeviceContent.createBy("M110", 8, expect);
        mcPLC.writeDeviceBatchInBit(reqContent);
        address = McDeviceAddress.createBy("M110", 8);
        ackContent = mcPLC.readDeviceBatchInBit(address);

        // ------------------ read and write device random in word ------------------------
        List<McDeviceContent> writeWord = new ArrayList<>();
        writeWord.add(McDeviceContent.createBy("D110", new byte[]{0x50, 0x05}));
        writeWord.add(McDeviceContent.createBy("D111", new byte[]{0x75, 0x05}));
        writeWord.add(McDeviceContent.createBy("M110", new byte[]{0x40, 0x05}));
        List<McDeviceContent> writeDWord = new ArrayList<>();
        writeDWord.add(McDeviceContent.createBy("D120", new byte[]{0x02, 0x12, 0x39, 0x04}));
        writeDWord.add(McDeviceContent.createBy("M130", new byte[]{0x75, 0x04, 0x25, 0x04}));
        mcPLC.writeDeviceRandomInWord(writeWord, writeDWord);

        List<McDeviceAddress> readWord = new ArrayList<>();
        readWord.add(McDeviceAddress.createBy("D110"));
        readWord.add(McDeviceAddress.createBy("D111"));
        readWord.add(McDeviceAddress.createBy("M110"));
        List<McDeviceAddress> readDWord = new ArrayList<>();
        readDWord.add(McDeviceAddress.createBy("D120"));
        readDWord.add(McDeviceAddress.createBy("M130"));
        List<McDeviceContent> mcDeviceContents = mcPLC.readDeviceRandomInWord(readWord, readDWord);

        // ------------------------ write device random in bit ---------------------------
        List<McDeviceContent> contents = new ArrayList<>();
        contents.add(McDeviceContent.createBy("M110", new byte[]{0x01}));
        contents.add(McDeviceContent.createBy("M112", new byte[]{0x01}));
        contents.add(McDeviceContent.createBy("M113", new byte[]{0x01}));
        mcPLC.writeDeviceRandomInBit(contents);

        // ---------- read and write device multi blocks (test failed, not get reason) ----
        List<McDeviceContent> wordContents = new ArrayList<>();
        wordContents.add(McDeviceContent.createBy("D110", 2, new byte[]{0x01, 0x02, 0x03, 0x04}));
        wordContents.add(McDeviceContent.createBy("D0", 1, new byte[]{0x08, 0x07}));
        List<McDeviceContent> bitContents = new ArrayList<>();
        bitContents.add(McDeviceContent.createBy("M110", 1, new byte[]{0x03, 0x04}));
        mcPLC.writeDeviceBatchMultiBlocks(wordContents, bitContents);

        List<McDeviceAddress> wordAddresses = new ArrayList<>();
        wordAddresses.add(McDeviceAddress.createBy("D110", 2));
        wordAddresses.add(McDeviceAddress.createBy("D114", 1));
        List<McDeviceAddress> bitAddresses = new ArrayList<>();
        bitAddresses.add(McDeviceAddress.createBy("M110", 1));
        mcDeviceContents = mcPLC.readDeviceBatchMultiBlocks(wordAddresses, bitAddresses);

        mcPLC.close();
    }
}
```