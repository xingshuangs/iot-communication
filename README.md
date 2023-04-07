# IOT-COMMUNICATION

![Maven-v1.2.7](https://img.shields.io/badge/Maven-v1.2.7-brightgreen)
![Language-java8](https://img.shields.io/badge/Language-java8-blue)
![Idea-2022.02.03](https://img.shields.io/badge/Idea-2022.02.03-lightgrey)
![CopyRight-Oscura](https://img.shields.io/badge/CopyRight-Oscura-yellow)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](./LICENSE)

**English(英文) | [Chinese(中文)](./README-CN.md )**

## Overview

**If this helps you, please give the author a :star: .**<br>

Now, it is a tool for iot communication, it includes

- Siemens S7 protocol, it can access to S1500, S1200, S200smart.
- ModbusTCP protocol.
- Parse byte array data.

## Getting Started

### 1. How to get

Add the dependency to pom.xml in the JAVA project

```xml
<dependency>
    <groupId>com.github.xingshuangs</groupId>
    <artifactId>iot-communication</artifactId>
    <version>1.2.7</version>
</dependency>
```

### 2. Basic examples

```java
class Demo {
    public static void main(String[] args) {
        // create PLC instance
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        // read and write boolean
        s7PLC.writeBoolean("DB1.2.0", true);
        boolean boolData = s7PLC.readBoolean("DB1.2.0");
        // read and write byte
        s7PLC.writeByte("DB2.1", (byte) 0x11);
        byte byteData = s7PLC.readByte("DB2.1");
        // close
        s7PLC.close();
    }
}
```

## Tutorial

- [Siemens S7 Protocol Tutorial](./tutorial/README-S7-EN.md)
- [ModbusTCP Protocol Tutorial](./tutorial/README-Modbus-EN.md)
- [Tool for Byte Array Data Parse Tutorial](./tutorial/README-ByteArray-EN.md)

## Contact

If you have any questions, please contact the author in the following ways, and the author will give answers in his
spare time.

- QQ Group: **759101350**
- QQ Private: **837820457**
- Email：**xingshuang_cool@163.com**

## License

Distributed under the MIT License. See [`LICENSE`](./LICENSE) for more information.<br>
@2019 - 2099 Oscura, All Rights Reserved <br>

## Sponsor

**WeChat** (Please note your name)<br>
![微信](https://i.postimg.cc/brBG5vx8/image.png)
