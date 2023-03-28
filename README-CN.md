# IOT-COMMUNICATION

![Maven-v1.2.7](https://img.shields.io/badge/Maven-v1.2.7-brightgreen)
![Language-java8](https://img.shields.io/badge/Language-java8-blue)
![Idea-2022.02.03](https://img.shields.io/badge/Idea-2022.02.03-lightgrey)
![CopyRight-Oscura](https://img.shields.io/badge/CopyRight-Oscura-yellow)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](./LICENSE)

**[英文](./README.md) | 中文**

## 简述

**如果这个库对你有帮助，请给作者一颗:star:**<br>

目前它只是一个物联网通信的工具，包含

- 西门子S7通信协议(可以访问西门子S1500，S1200，S200smart);
- ModbusTCP通信协议;
- 基础字节数组解析转换工具;

## 使用指南

### 1. 如何获取

在JAVA工程的pom.xml文件中添加该依赖即可

```xml
<dependency>
    <groupId>com.github.xingshuangs</groupId>
    <artifactId>iot-communication</artifactId>
    <version>1.2.7</version>
</dependency>
```

### 2. 简单示例

```java
class Demo {
    public static void main(String[] args) {
        // 创建PLC对象
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        // 读写boolean
        s7PLC.writeBoolean("DB1.2.0", true);
        boolean boolData = s7PLC.readBoolean("DB1.2.0");
        // 读写字节
        s7PLC.writeByte("DB2.1", (byte) 0x11);
        byte byteData = s7PLC.readByte("DB2.1");
    }
}
```

## 教程链接

- [西门子S7通信协议](./tutorial/README-S7-CN.md)
- [ModbusTCP通信协议](./tutorial/README-Modbus-CN.md)
- [字节数据解析转换工具](./tutorial/README-ByteArray-CN.md)

## 联系方式

如果有任何问题，可以通过以下方式联系作者，作者在空余时间会做解答。

- QQ群：**759101350**
- 邮件：**xingshuang_cool@163.com**

## 许可证

根据MIT许可证发布，更多信息请参见[`LICENSE`](./LICENSE)。<br>
@2019 - 2099 Oscura版权所有。

## 赞助

**微信** (请备注上你的姓名)<br>
![微信](https://i.postimg.cc/brBG5vx8/image.png)

