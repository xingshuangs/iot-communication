# IOT-COMMUNICATION

[![Maven-v1.5.2](https://img.shields.io/badge/Maven-v1.5.2-brightgreen)](https://mvnrepository.com/artifact/com.github.xingshuangs/iot-communication)
![Language-java8](https://img.shields.io/badge/Language-java8-blue)
![Idea-2022.02.03](https://img.shields.io/badge/Idea-2022.02.03-lightgrey)
![CopyRight-Oscura](https://img.shields.io/badge/CopyRight-Oscura-yellow)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](./LICENSE)

**[英文](./README.md) | 中文**

## 简述

**如果这个库对你有帮助，请给作者一颗** ⭐ <br>

目前它只是一个物联网通信的工具，主要用于相关协议的学习和开发，包含

- 西门子S7通信协议，支持西门子S1500，S1200，S400，S300，S200Smart，西门子数控机床828D，
  [S7Client DEMO](https://github.com/xingshuangs/SiemensWindowProgram)
- Modbus通信协议，支持ModbusTcp, ModbusRtuOverTcp, ModbusAsciiOverTcp, ModbusTcpServer
- 三菱Melsec（MC）通信协议，支持PLC iQ-R系列, Q/L系列, QnA系列, A系列,  目前只测试了L系列和FX5U
- RTSP, RTCP, RTP, H264, MP4 (FMP4)协议，RTSP + H264 + FMP4 + WebSocket + MSE +
  WEB，[WEB Monitor DEMO](https://github.com/xingshuangs/rtsp-websocket-server)
- 基础字节数组解析转换工具

## 使用指南

在JAVA工程的pom.xml文件中添加该依赖即可

```xml

<dependency>
    <groupId>com.github.xingshuangs</groupId>
    <artifactId>iot-communication</artifactId>
    <version>1.5.2</version>
</dependency>
```

## 教程链接

- [西门子S7通信协议](./tutorial/README-S7-CN.md)
- [Modbus通信协议](./tutorial/README-Modbus-CN.md)
- [三菱Melsec(MC)通信协议](./tutorial/README-Melsec-CN.md)
- [RTSP/RTCP/RTP/H264/FMP4协议](./tutorial/README-RTSP-CN.md)
- [WEB视频监控](./tutorial/README-WebVideo-CN.md)
- [字节数据解析转换工具](./tutorial/README-ByteArray-CN.md)

## 联系方式

如果有任何问题，可以通过以下方式联系作者，作者在空余时间会做解答。<br>
[更新日志](./tips/changeLog.md)

- QQ群：**759101350**
- QQ私人：**837820457**
- 邮件：**xingshuang_cool@163.com**

## 许可证

根据MIT许可证发布，更多信息请参见[`LICENSE`](./LICENSE)。<br>
@2019 - 2099 Oscura (xingshuang) 版权所有。<br>

❗❗❗ **请各位使用者严格遵守MIT协议，使用时添加作者的版权许可声明。**

## 免责声明

本项目所使用的依赖如下：

| 序号  | 使用范围     | 依赖                                               | 版本      |   协议    |    时间     | 版权             |
|:---:|----------|:-------------------------------------------------|---------|:-------:|:---------:|:---------------|
|  1  | provided | [lombok](https://projectlombok.org/)             | 1.18.26 |   MIT   | 2009-2021 | Lombok Authors |
|  2  | test     | [junit](https://junit.org/junit4/)               | 4.13.2  | EPL-1.0 | 2002-2021 | junit          |
|  3  | test     | [slf4j-simple](https://www.slf4j.org/index.html) | 2.0.6   |   MIT   | 2004-2023 | QOS.ch         |
|  4  | compile  | [slf4j-api](https://www.slf4j.org/index.html)    | 2.0.6   |   MIT   | 2004-2023 | QOS.ch         |

## 赞助

一杯奶茶足矣<br>
**微信** (请备注上你的姓名)<br>
![微信](https://i.postimg.cc/brBG5vx8/image.png)

