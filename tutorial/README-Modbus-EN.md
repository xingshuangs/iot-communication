# Modbus Protocol Tutorial

[HOME BACK](../README.md)

## Foreword

> Tips

- **1** coil = **1** bit.
- **1** register address = **2** bytes.
- [ register start address = **0001** ] equal to [ access address = **0** ], there is a **1** address offset.
- The encoding format of **4 bytes** data = **BA_DC**. (big-endian mode = **DC_BA**，little-endian mode = **AB_CD**)
- Support automatic reconnection.

> Protocol type

| Modbus Protocol | Class                      | Remark             |
|:---------------:|:---------------------------|:-------------------|
|       Tcp       | ModbusTcp, ModbusTcpServer | Ethernet           |
|       Rtu       | ModbusRtuOverTcp           | Serial to Ethernet |
|      Ascii      | ModbusAsciiOverTcp         | Serial to Ethernet |

> Area

| Area Number | Name           | Read/Write | Address Range | Method                               |
|:-----------:|:---------------|:-----------|:-------------:|:-------------------------------------|
|   0 area    | output coil    | read/write |  00001-09999  | readCoil / writeCoil                 |
|   1 area    | input coil     | read       |  10001-19999  | readDiscreteInput                    |
|   3 area    | input register | read       |  30001-39999  | readInputRegister                    |
|   4 area    | hold register  | read/write |  40001-49999  | readHoldRegister / writeHoldRegister |

> Address

According to the Modbus communication protocol, the Modbus data addresses are 0xxxx, 1xxxx, 3xxxx, and 4xxxx.

| Case Address | Area           | Address Prefix | Address Remain | Method Address | Use Example              |
|:------------:|:---------------|:---------------|:--------------:|----------------|:-------------------------|
|    00001     | output coil    | 0              |      0001      | 0              | readCoil(0, 1)           |
|    10003     | input coil     | 1              |      0003      | 2              | readDiscreteInput(2, 1)  |
|    30020     | input register | 2              |      0020      | 19             | readInputRegister(19, 1) |
|    40100     | hold register  | 3              |      0100      | 99             | readHoldRegister(99, 1)  |

> Function Code

| number | Function Code | Description             | Method            |
|:------:|:-------------:|:------------------------|:------------------|
|   1    |      01H      | read output coil        | readCoil          |
|   2    |      02H      | read input coil         | readDiscreteInput |
|   3    |      03H      | read hold register      | readHoldRegister  |
|   4    |      04H      | read input register     | readInputRegister |
|   5    |      05H      | write single coil       | writeCoil         |
|   6    |      06H      | write single register   | writeHoldRegister |
|   7    |      0FH      | write multiple coil     | writeCoil         |
|   8    |      10H      | write multiple register | writeHoldRegister |

> Hold Register Quick Access

| number | Method       | Register Count | Size in Byte | Size in Bit |   Register    |
|:------:|:-------------|:--------------:|:------------:|:-----------:|:-------------:|
|   1    | readBoolean  |       1        |     1/8      |      1      | hold register |
|   2    | readInt16    |       1        |      2       |     16      | hold register |
|   3    | readUInt16   |       1        |      2       |     16      | hold register |
|   4    | readInt32    |       2        |      4       |     32      | hold register |
|   5    | readUInt32   |       2        |      4       |     32      | hold register |
|   6    | readFloat32  |       2        |      4       |     32      | hold register |
|   7    | readFloat64  |       4        |      8       |     64      | hold register |
|   8    | readString   |       n        |      2n      |     16n     | hold register |
|   9    | writeInt16   |       1        |      2       |     16      | hold register |
|   10   | writeUInt16  |       1        |      2       |     16      | hold register |
|   11   | writeInt32   |       2        |      4       |     32      | hold register |
|   12   | writeUInt32  |       2        |      4       |     32      | hold register |
|   13   | writeFloat32 |       2        |      4       |     32      | hold register |
|   14   | writeFloat64 |       4        |      8       |     64      | hold register |
|   15   | writeString  |       n        |      2n      |     16n     | hold register |

## Print Message

If you want to know the actual input and output of packets during communication, you can print packet information by
yourself.

```java
class Demo {
    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp("127.0.0.1");
        // print message
        plc.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));
        plc.writeInt16(2, (short) 10);
        plc.close();
    }
}
```

## Communication Connection

- By default, the long connection mode is adopted. You need to close connection manually when it is not in use.
- If a short connection is required, you need to set it manually.

### 1. Long Connection Mode

```java
class Demo {
    public static void main(String[] args) {
        // long connection mode, persistence = true
        ModbusTcp plc = new ModbusTcp("127.0.0.1");
        plc.writeInt16(2, (short) 10);
        short data = plc.readInt16(2);
        // close it manually
        plc.close();
    }
}
```

### 2. Short Connection Mode

```java
class Demo {
    public static void main(String[] args) {
        // short connection mode
        ModbusTcp plc = new ModbusTcp("127.0.0.1");
        // set short connection mode, persistence = false
        plc.setPersistence(false);
        plc.writeInt16(2, (short) 10);
        short data = plc.readInt16(2);
    }
}
```

## Fixed unitId mode (1-to-1)

### 1. Read data

```java
class Demo {
    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp("127.0.0.1");

        // read coil
        List<Boolean> readCoil = plc.readCoil(0, 2);

        // read discrete input
        List<Boolean> readDiscreteInput = plc.readDiscreteInput(0, 4);

        // read hold register
        byte[] readHoldRegister = plc.readHoldRegister(0, 4);

        // read input register
        byte[] readInputRegister = plc.readInputRegister(0, 2);

        // hold register read Boolean
        boolean readBoolean = plc.readBoolean(2, 1);

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

### 2. Write data

```java
class Demo {
    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp("127.0.0.1");

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

        // hold register write String, even length
        plc.writeString(2, "1234");

        plc.close();
    }
}
```

## Any unitId mode (1 to many)

### 1. Read data

```java
class Demo {
    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp("127.0.0.1");
        // optional
        plc.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));

        // read coil
        List<Boolean> readCoil = plc.readCoil(2, 0, 2);

        // read discrete input
        List<Boolean> readDiscreteInput = plc.readDiscreteInput(2, 0, 4);

        // read hold register
        byte[] readHoldRegister = plc.readHoldRegister(2, 0, 4);

        // read input register
        byte[] readInputRegister = plc.readInputRegister(2, 0, 2);

        // hold register read Boolean
        boolean readBoolean = plc.readBoolean(3, 2, 1);

        // hold register read Int16
        short readInt16 = plc.readInt16(3, 2, true);

        // hold register read UInt16
        int readUInt16 = plc.readUInt16(3, 2, false);

        // hold register read Int32
        int readInt32 = plc.readInt32(4, 2, EByteBuffFormat.AB_CD);

        // hold register read Int32
        long readUInt32 = plc.readUInt32(4, 2, EByteBuffFormat.AB_CD);

        // hold register read Float32
        float readFloat32 = plc.readFloat32(4, 2, EByteBuffFormat.AB_CD);

        // hold register read Float64
        double readFloat64 = plc.readFloat64(4, 2, EByteBuffFormat.AB_CD);

        // hold register read String
        String readString = plc.readString(5, 2, 4);

        plc.close();
    }
}
```

### 2. Write data

```java
class Demo {
    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp("127.0.0.1");
        // optional
        plc.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));

        // single write coil
        plc.writeCoil(2, 0, true);

        // multiple write coil
        List<Boolean> booleans = Arrays.asList(true, false, true, false);
        plc.writeCoil(2, 0, booleans);

        // single write hold register
        plc.writeHoldRegister(2, 0, 33);
        // multiple write hold register
        plc.writeHoldRegister(2, 3, new byte[]{(byte) 0x11, (byte) 0x12});
        // multiple write hold register
        List<Integer> integers = Arrays.asList(11, 12, 13, 14);
        plc.writeHoldRegister(2, 3, integers);

        // hold register write int16
        plc.writeInt16(3, 2, (short) 10, true);

        // hold register write uint16
        plc.writeUInt16(3, 2, 20, false);

        // hold register write int32
        plc.writeInt32(4, 2, 32, EByteBuffFormat.AB_CD);

        // hold register write uint32
        plc.writeUInt32(4, 2, 32L, EByteBuffFormat.AB_CD);

        // hold register write float32
        plc.writeFloat32(4, 2, 12.12f, EByteBuffFormat.AB_CD);

        // hold register write float64
        plc.writeFloat64(4, 2, 33.21, EByteBuffFormat.AB_CD);

        // hold register write String
        plc.writeString(5, 2, "1234");

        plc.close();
    }
}
```

## ModbusTcp Server(Slave)

```java
class Demo {
    public static void main(String[] args) {
        ModbusTcpServer server = new ModbusTcpServer();
        server.start();

        ModbusTcp modbusTcp = new ModbusTcp("127.0.0.1");
        // optional
        modbusTcp.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));

        modbusTcp.writeInt16(2, (short) 10);
        short data = modbusTcp.readInt16(2);

        modbusTcp.close();
        server.stop();
    }
}
```