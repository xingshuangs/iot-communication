/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>, 王清攀
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.xingshuangs.iot.protocol.sl651.service;


import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import com.github.xingshuangs.iot.net.SocketUtils;
import com.github.xingshuangs.iot.net.server.TcpServerBasic;
import com.github.xingshuangs.iot.protocol.sl651.constant.FrameConstant;
import com.github.xingshuangs.iot.protocol.sl651.enums.SL651FunctionCodeEnum;
import com.github.xingshuangs.iot.protocol.sl651.event.ISl651ConnectStatusListener;
import com.github.xingshuangs.iot.protocol.sl651.event.ISl651MessageListener;
import com.github.xingshuangs.iot.protocol.sl651.model.SL651BodyResponse;
import com.github.xingshuangs.iot.protocol.sl651.model.SL651Response;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * SL651-2014 水文通信协议服务端
 *
 * @author wangqingpan
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class SL651Server extends TcpServerBasic {

    /**
     * Current connected client number.
     * (客户端当前的连接数量)
     */
    private AtomicInteger connectedNumber = new AtomicInteger();

    /**
     * Max available number of client.
     * (客户端连接的最大允许数量)
     */
    private Integer maxAvailableNumber;

    /**
     * Password.
     * (密码)
     */
    private String pwd;

    /**
     * Message listener.
     * (消息监听器)
     */
    private ISl651MessageListener messageListener;

    /**
     * Connection status listener.
     * (连接状态监听器)
     */
    private ISl651ConnectStatusListener connectStatusListener;

    public SL651Server(String pwd) {
        this.pwd = pwd;
        this.maxAvailableNumber = Runtime.getRuntime().availableProcessors() * 1000;
    }

    /**
     * Is the client valid.
     * (校验客户端是否允许连入)
     *
     * @param client client socket.
     * @return true: check success，false：check failed.
     */
    @Override
    protected boolean checkClientValid(Socket client) {
        return this.connectedNumber.get() < this.maxAvailableNumber;
    }

    /**
     * Client connected.
     * (客户端连入)
     *
     * @param socket client socket.
     */
    @Override
    protected void clientConnected(Socket socket) {
        try {
            socket.setSoTimeout(10 * 60 * 1000);
        } catch (SocketException e1) {
            log.info("设置Socket超时异常:{}", e1.getMessage());
        }
        if (connectStatusListener != null) {
            connectStatusListener.online(socket.getInetAddress().getHostAddress(), socket.getPort(), socket.getLocalPort());
        }
        this.connectedNumber.getAndIncrement();
    }

    /**
     * Client disconnected.
     * (客户端断开)
     *
     * @param socket client socket.
     */
    @Override
    protected void clientDisconnected(Socket socket) {
        if (connectStatusListener != null) {
            connectStatusListener.offline(socket.getInetAddress().getHostAddress(), socket.getPort(), socket.getLocalPort());
        }
        this.connectedNumber.getAndDecrement();
    }

    @Override
    protected void doClientHandle(Socket socket) {
        byte[] bytes = this.readClientData(socket);
        if (bytes == null || bytes.length == 0) {
            return;
        }
        try {
            SL651Response respParams = SL651MessageParser.parseRespParams(bytes);
            List<SL651BodyResponse> bodyParamsList = null;
            if (isReportMessage(respParams.getFunctionCode())) {
                bodyParamsList = SL651MessageParser.parseRespBodyParams(bytes);
                byte[] responseBytes = SL651ResponseBuilder.buildAckResponse(respParams);
                this.write(socket, responseBytes);
            }

            if (messageListener != null) {
                messageListener.onMessage(bytes, respParams, bodyParamsList);
            }
        } catch (Exception e) {
            log.error("处理SL651客户端数据异常: " + e.getMessage(), e);
        }
    }


    /**
     * Override read client data function.
     * (重写读取客户端数据，针对粘包粘包的数据处理)
     *
     * @param socket client socket.
     * @return byte array
     */
    @Override
    protected byte[] readClientData(Socket socket) {
        try {
            InputStream in = socket.getInputStream();
            int firstByte = in.read();
            if (firstByte == -1) {
                SocketUtils.close(socket);
                throw new SocketRuntimeException("The client is disconnected.");
            }

            byte[] header = new byte[2];
            header[0] = (byte) firstByte;
            this.read(socket, header, 1, 1, 1024, 0, true);

            boolean isStartSymbol = Arrays.equals(FrameConstant.HEADER_START_HEX, header);
            if (!isStartSymbol) {
                SocketUtils.close(socket);
                throw new SocketRuntimeException("非法报文");
            }

            byte[] headerBody = new byte[12];
            this.read(socket, headerBody, 0, 12, 1024, 0, true);

            int bodyLength = parseDataLength(new byte[]{headerBody[9], headerBody[10]});

            byte[] dataBytes = new byte[bodyLength];
            this.read(socket, dataBytes, 0, bodyLength, 1024, 0, true);

            boolean m3Equals = Arrays.equals(FrameConstant.HEADER_START_BODY_M3_FLAG_HEX, new byte[]{headerBody[11]});
            if (m3Equals) {
                SocketUtils.close(socket);
                throw new SocketRuntimeException("不支持的M3模式");
            }

            byte[] endByte = new byte[3];
            this.read(socket, endByte, 0, 3, 1024, 0, true);

            int totalLength = header.length + headerBody.length + dataBytes.length + endByte.length;
            byte[] result = new byte[totalLength];
            System.arraycopy(header, 0, result, 0, header.length);
            System.arraycopy(headerBody, 0, result, header.length, headerBody.length);
            System.arraycopy(dataBytes, 0, result, header.length + headerBody.length, dataBytes.length);
            System.arraycopy(endByte, 0, result, header.length + headerBody.length + dataBytes.length, endByte.length);
            return result;
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }

    /**
     * 检查功能码是否为上报消息
     *
     * @param functionCode 功能码
     * @return 是否为上报消息
     */
    private boolean isReportMessage(byte[] functionCode) {
        return Arrays.equals(functionCode, SL651FunctionCodeEnum.HOUR_REPORT.getCode()) ||
                Arrays.equals(functionCode, SL651FunctionCodeEnum.TEST_REPORT.getCode()) ||
                Arrays.equals(functionCode, SL651FunctionCodeEnum.REGULAR_REPORT.getCode()) ||
                Arrays.equals(functionCode, SL651FunctionCodeEnum.URGENT_REPORT.getCode());
    }

    /**
     * 解析数据长度
     * 数据长度 = 第一个字节的低 4 位 + 第二个字节的全部 8 位（共 12 位）
     *
     * @param bytes 字节
     * @return 数据长度
     */
    private int parseDataLength(byte[] bytes) {
        int lengthLowNibble = bytes[0] & 0x0F;
        int lengthHighByte = bytes[1] & 0xFF;
        return (lengthLowNibble << 8) | lengthHighByte;
    }
}
