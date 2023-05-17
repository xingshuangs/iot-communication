# ModbusTCP通信协议教程

[返回主页](../README-CN.md)

## 前言

> 注意点

- **1** 个线圈 = **1** 位
- **1** 个寄存器地址 = **2** 个字节；
- [ 寄存器起始地址 = **0001** ] 相当于 [ 访问地址 = **0** ]，存在 **1** 位地址偏移；
- **4 字节**数据的编码格式 = **BA_DC**； (大端模式 = **DC_BA**，小端模式 = **AB_CD**)

> 存储区

| 区号  | 名称    | 读写      |    地址范围     | 对应方法                                 |
|:---:|:------|:--------|:-----------:|:-------------------------------------|
| 0区  | 输出线圈  | 可读可写布尔量 | 00001-09999 | readCoil / writeCoil                 |
| 1区  | 输入线圈  | 只读布尔量   | 10001-19999 | readDiscreteInput                    |
| 3区  | 输入寄存器 | 只读寄存器   | 30001-39999 | readInputRegister                    |
| 4区  | 保持寄存器 | 可读可写寄存器 | 40001-49999 | readHoldRegister / writeHoldRegister |

> 功能码

| 功能码 | 功能说明    | 对应方法              |
|:---:|:--------|:------------------|
| 01H | 读取输出线圈  | readCoil          |
| 02H | 读取输入线圈  | readDiscreteInput |
| 03H | 读取保持寄存器 | readHoldRegister  |
| 04H | 读取输入寄存器 | readInputRegister |
| 05H | 写入单线圈   | writeCoil         |
| 06H | 写入单寄存器  | writeHoldRegister |
| 0FH | 写入多线圈   | writeCoil         |
| 10H | 写入多寄存器  | writeHoldRegister |

> 保持寄存器快捷访问

| 方法           | 寄存器数量 | 字节大小 | 位大小 | 含义        |  寄存器  |
|:-------------|:-----:|:----:|:---:|:----------|:-----:|
| readBoolean  |   1   | 1/8  |  1  | 读取boolean | 保持寄存器 |
| readInt16    |   1   |  2   | 16  | 读取Int16   | 保持寄存器 |
| readUInt16   |   1   |  2   | 16  | 读取UInt16  | 保持寄存器 |
| readInt32    |   2   |  4   | 32  | 读取Int32   | 保持寄存器 |
| readUInt32   |   2   |  4   | 32  | 读取UInt32  | 保持寄存器 |
| readFloat32  |   2   |  4   | 32  | 读取Float32 | 保持寄存器 |
| readFloat64  |   4   |  8   | 64  | 读取Float64 | 保持寄存器 |
| readString   |   n   |  2n  | 16n | 读取字符串     | 保持寄存器 |
| writeInt16   |   1   |  2   | 16  | 写入Int16   | 保持寄存器 |
| writeUInt16  |   1   |  2   | 16  | 写入UInt16  | 保持寄存器 |
| writeInt32   |   2   |  4   | 32  | 写入Int32   | 保持寄存器 |
| writeUInt32  |   2   |  4   | 32  | 写入UInt32  | 保持寄存器 |
| writeFloat32 |   2   |  4   | 32  | 写入Float32 | 保持寄存器 |
| writeFloat64 |   4   |  8   | 64  | 写入Float32 | 保持寄存器 |
| writeString  |   n   |  2n  | 16n | 写入字符串     | 保持寄存器 |

## 打印报文

如果想知道通信过程中的实际输入输出报文内容，可以添加报文信息打印

```java
class Demo {
    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp(1, "127.0.0.1");
        // 报文输出设置
        plc.setComCallback(x -> System.out.printf("长度[%d]:%s%n", x.length, HexUtil.toHexString(x)));
        plc.writeInt16(2, (short) 10);
        plc.close();
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
        ModbusTcp plc = new ModbusTcp(1, "127.0.0.1");
        plc.writeInt16(2, (short) 10);
        short data = plc.readInt16(2);
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
        ModbusTcp plc = new ModbusTcp(1, "127.0.0.1");
        // 设置短连接模式，即持久化为false
        plc.setPersistence(false);
        plc.writeInt16(2, (short) 10);
        short data = plc.readInt16(2);
    }
}
```

## 读取数据

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

        plc.close();
    }
}
```

## 写入数据

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

        // hold register write String，偶数长度
        plc.writeString(2, "1234");

        plc.close();
    }
}
```