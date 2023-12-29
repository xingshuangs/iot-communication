/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
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

package com.github.xingshuangs.iot.protocol.rtsp.service;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import com.github.xingshuangs.iot.net.client.TcpClientBasic;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.rtcp.model.RtcpBasePackage;
import com.github.xingshuangs.iot.protocol.rtcp.model.RtcpPackageBuilder;
import com.github.xingshuangs.iot.protocol.rtcp.service.RtcpDataStatistics;
import com.github.xingshuangs.iot.protocol.rtp.model.RtpPackage;
import com.github.xingshuangs.iot.protocol.rtp.service.IPayloadParser;
import com.github.xingshuangs.iot.protocol.rtsp.model.interleaved.RtspInterleaved;
import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * 简单TCP示例
 *
 * @author xingshuang
 */
@Slf4j
public class RtspInterleavedClient implements IRtspDataStream {

    public static final Integer BUFFER_SIZE = 4096;

    /**
     * 是否终止线程
     */
    private boolean terminal = false;

    /**
     * 数据收发前自定义处理接口
     */
    private Consumer<byte[]> commCallback;

    /**
     * 负载解析器
     */
    private final IPayloadParser iPayloadParser;

    /**
     * RTP和RTCP的数据统计
     */
    private final RtcpDataStatistics statistics = new RtcpDataStatistics();

    /**
     * 视频rtp的通道编号
     */
    private int rtpVideoChannelNumber = 0;

    /**
     * 视频rtcp的通道编号
     */
    private int rtcpVideoChannelNumber = 1;

    /**
     * 连接对象
     */
    private final TcpClientBasic rtspClient;

    /**
     * 异步执行对象
     */
    private CompletableFuture<Void> future;

    /**
     * 线程池执行服务，单线程
     */
    private final ExecutorService executorService;

    public void setCommCallback(Consumer<byte[]> commCallback) {
        this.commCallback = commCallback;
    }

    public int getRtpVideoChannelNumber() {
        return rtpVideoChannelNumber;
    }

    public int getRtcpVideoChannelNumber() {
        return rtcpVideoChannelNumber;
    }

    public void setRtpVideoChannelNumber(int rtpVideoChannelNumber) {
        this.rtpVideoChannelNumber = rtpVideoChannelNumber;
    }

    public void setRtcpVideoChannelNumber(int rtcpVideoChannelNumber) {
        this.rtcpVideoChannelNumber = rtcpVideoChannelNumber;
    }

    public RtspInterleavedClient(IPayloadParser iPayloadParser, TcpClientBasic rtspClient) {
        this.iPayloadParser = iPayloadParser;
        this.rtspClient = rtspClient;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public CompletableFuture<Void> getFuture() {
        return this.future;
    }

    @Override
    public void close() {
        this.executorService.shutdown();
        if (!this.terminal) {
            // 发送byte
            byte[] receiverAndByteContent = this.statistics.createReceiverAndByteContent();
            this.sendData(receiverAndByteContent);
            this.terminal = true;
        }
    }


    @Override
    public void triggerReceive() {
        this.future = CompletableFuture.runAsync(this::waitForReceiveData, this.executorService);
    }

    @Override
    public void sendData(byte[] data) {
        if (this.commCallback != null) {
            this.commCallback.accept(data);
        }
        this.rtspClient.write(data);
    }

    /**
     * 接收数据线程
     */
    private void waitForReceiveData() {
        InetSocketAddress socketAddress = this.rtspClient.getSocketAddress();
        log.debug("[RTSP + TCP] Interleaved 开启异步接收数据线程，远程的IP[/{}:{}]",
                socketAddress.getAddress().getHostAddress(), socketAddress.getPort());
        while (!this.terminal) {
            try {
                if (!this.rtspClient.checkConnected()) {
                    this.terminal = true;
                    break;
                }
                byte[] data = this.readFromServer();
                if (this.commCallback != null) {
                    this.commCallback.accept(data);
                }
                RtspInterleaved interleaved = RtspInterleaved.fromBytes(data);

                if (interleaved.getChannelId() == this.rtpVideoChannelNumber) {
                    this.rtpVideoHandle(interleaved);
                } else if (interleaved.getChannelId() == this.rtcpVideoChannelNumber) {
                    this.rtcpVideoHandle(interleaved);
                }
            } catch (SocketRuntimeException e) {
                // SocketRuntimeException就是IO异常，网络断开了，结束线程
                log.error(e.getMessage());
                this.terminal = true;
                break;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        log.debug("[RTSP + TCP] Interleaved 关闭异步接收数据线程，远程的IP[/{}:{}]",
                socketAddress.getAddress().getHostAddress(), socketAddress.getPort());
    }

    /**
     * 获取接收的数据
     *
     * @return 字节数组
     */
    private byte[] readFromServer() {
        byte[] header = new byte[4];
        while (header[0] != RtspInterleaved.VERSION) {
            this.rtspClient.read(header, 0, 1, 1024, 0, true);
        }
        int readLength = this.rtspClient.read(header, 1, 3, 1024, 0, true);
        if (readLength != 3) {
            throw new RtspCommException("头读取长度有误");
        }
        int offset = 4;
        int length = ByteReadBuff.newInstance(header, 2).getUInt16();
        byte[] total = new byte[length + offset];
        System.arraycopy(header, 0, total, 0, header.length);
        // 存在分包的情况，循环读取，保证数据准确性
        int read = this.rtspClient.read(total, offset, length, 1024, 0, true);
        if (offset + read != total.length) {
            log.error(HexUtil.toHexString(total));
            throw new RtspCommException("数据体读取长度有误，原来长度[" + (total.length) + "], 现在长度[" + (offset + read) + "]");
        }
        return total;
    }

    /**
     * 处理视频的RTCP
     *
     * @param interleaved 数据包
     */
    private void rtcpVideoHandle(RtspInterleaved interleaved) {
        List<RtcpBasePackage> basePackages = RtcpPackageBuilder.fromBytes(interleaved.getPayload());
        this.statistics.processRtcpPackage(basePackages);
    }

    /**
     * 处理视频RTP
     *
     * @param interleaved 数据包
     */
    private void rtpVideoHandle(RtspInterleaved interleaved) {
        RtpPackage rtp = RtpPackage.fromBytes(interleaved.getPayload());
//        log.debug("数据长度[{}], 时间戳[{}], 序列号[{}]", rtp.byteArrayLength(), rtp.getHeader().getTimestamp(), rtp.getHeader().getSequenceNumber());
        this.iPayloadParser.processPackage(rtp);
        this.statistics.processRtpPackage(rtp, this::sendData);
    }
}
