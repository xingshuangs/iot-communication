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
     * @param frameHandle 帧回调事件
     */
    void processPackage(RtpPackage rtp, Consumer<RawFrame> frameHandle);
}
