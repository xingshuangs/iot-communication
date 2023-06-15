package com.github.xingshuangs.iot.protocol.rtp.service;


import com.github.xingshuangs.iot.protocol.rtp.model.RtpPackage;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.RawFrame;

import java.util.function.Consumer;

/**
 * 负载解析器
 *
 * @author xingshuang
 */
public interface IPayloadParser {
    /**
     * 处理数据包
     *
     * @param rtp         rtp数据包
     */
    void processPackage(RtpPackage rtp);

    /**
     * 设置帧处理事件
     *
     * @param frameHandle 处理事件
     */
    void setFrameHandle(Consumer<RawFrame> frameHandle);
}
