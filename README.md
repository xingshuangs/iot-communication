# IOT-COMMUNICATION

[![Maven-v1.5.1](https://img.shields.io/badge/Maven-v1.5.1-brightgreen)](https://mvnrepository.com/artifact/com.github.xingshuangs/iot-communication)
![Language-java8](https://img.shields.io/badge/Language-java8-blue)
![Idea-2022.02.03](https://img.shields.io/badge/Idea-2022.02.03-lightgrey)
![CopyRight-Oscura](https://img.shields.io/badge/CopyRight-Oscura-yellow)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](./LICENSE)

**English(英文) | [Chinese(中文)](./README-CN.md )**

## Overview

**If this project helps you, please give the author a** ⭐ .<br>

Now, it is a tool for iot communication, used for the learning and development of protocols, it includes

- Siemens S7 protocol, support PLC S1500, S1200, S400, S300, S200Smart, Siemens SINUMERIK
  828D. [S7Client DEMO](https://github.com/xingshuangs/SiemensWindowProgram).
- Modbus protocol, support ModbusTcp, ModbusRtuOverTcp, ModbusAsciiOverTcp.
- Mitsubishi Melsec(MC) Protocol, support PLC iQ-R series, Q/L series, QnA series, Only the L Series and FX5U has been
  tested so far.
- RTSP, RTCP, RTP, H264, MP4 (FMP4) protocol, RTSP + H264 + FMP4 + WebSocket + MSE +
  WEB. [WEB Monitor DEMO](https://github.com/xingshuangs/rtsp-websocket-server).
- Parse byte array data.

## Getting Started

Add the dependency to pom.xml in the JAVA project.

```xml

<dependency>
    <groupId>com.github.xingshuangs</groupId>
    <artifactId>iot-communication</artifactId>
    <version>1.5.1</version>
</dependency>
```

## Tutorial

- [Siemens S7 Protocol Tutorial.](./tutorial/README-S7-EN.md)
- [Modbus Protocol Tutorial.](./tutorial/README-Modbus-EN.md)
- [Mitsubishi Melsec(MC) Protocol Tutorial.](./tutorial/README-Melsec-EN.md)
- [RTSP/RTCP/RTP/H264/FMP4 Protocol Tutorial](./tutorial/README-RTSP-EN.md)
- [WEB Video Monitor Tutorial](./tutorial/README-WebVideo-EN.md)
- [Tool for Byte Array Data Parse Tutorial.](./tutorial/README-ByteArray-EN.md)

## Contact

If you have any questions, please contact the author in the following ways, and the author will give answers in his
spare time. <br>
[UPDATE LOG](./tips/changeLog.md)

- QQ Group: **759101350**
- QQ Private: **837820457**
- Email：**xingshuang_cool@163.com**

## License

Distributed under the MIT License. See [`LICENSE`](./LICENSE) for more information.<br>
@2019 - 2099 Oscura (xingshuang), All Rights Reserved. <br>

❗❗❗ **Please strictly abide by the MIT agreement and add the author's copyright license notice when using.**

## Disclaimer

The dependencies used in this project are as follows:

| Number | Scope    | Dependency                                       | Version | License |   Date    | Copyright      |
|:------:|----------|:-------------------------------------------------|---------|:-------:|:---------:|:---------------|
|   1    | provided | [lombok](https://projectlombok.org/)             | 1.18.26 |   MIT   | 2009-2021 | Lombok Authors |
|   2    | test     | [junit](https://junit.org/junit4/)               | 4.13.2  | EPL-1.0 | 2002-2021 | junit          |
|   3    | test     | [slf4j-simple](https://www.slf4j.org/index.html) | 2.0.6   |   MIT   | 2004-2023 | QOS.ch         |
|   4    | compile  | [slf4j-api](https://www.slf4j.org/index.html)    | 2.0.6   |   MIT   | 2004-2023 | QOS.ch         |

## Sponsor

Buy me a cup of coffee. <br>
**WeChat** (Please note your name)<br>
![wechat](https://i.postimg.cc/brBG5vx8/image.png)
