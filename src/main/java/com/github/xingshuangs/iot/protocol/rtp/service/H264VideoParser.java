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

package com.github.xingshuangs.iot.protocol.rtp.service;


import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.exceptions.RtpCommException;
import com.github.xingshuangs.iot.protocol.rtp.enums.EH264NaluType;
import com.github.xingshuangs.iot.protocol.rtp.enums.EH264SliceType;
import com.github.xingshuangs.iot.protocol.rtp.model.RtpPackage;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.H264VideoFrame;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.RawFrame;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * H264的视频数据解析器
 *
 * @author xingshuang
 */
@Slf4j
public class H264VideoParser implements IPayloadParser {

    /**
     * 负载编号
     */
    private final Integer payloadNumber;

    /**
     * 基准时间戳
     */
    private long baseTimestamp = 0;

    /**
     * Last Decoding Time Stamp
     * (上一次解码时间戳)
     */
    private H264VideoFrame lastFrame;

    /**
     * Cache list of H264 video frame.
     * 缓存帧列表
     */
    private final List<H264VideoFrame> cacheFrameList = new ArrayList<>();

    /**
     * Frame handle.
     * （帧处理事件）
     */
    private Consumer<RawFrame> frameHandle;

    /**
     * Cache list of H264 Nalu FuA.
     * H264的FuA单元的列表
     */
    private final List<H264NaluFuA> naluFuADataBuffers = new ArrayList<>();

    /**
     * 单Nalu的缓存
     */
    private final List<H264NaluSingle> naluBuffers = new ArrayList<>();

    /**
     * 是否有B帧
     */
    private boolean hasBFrame;

    public H264VideoParser(Integer payloadNumber) {
        this.payloadNumber = payloadNumber;
    }

    private void resetBuffers() {
        this.naluFuADataBuffers.clear();
        this.naluBuffers.clear();
    }

    /**
     * 执行Nalu的缓存，打包成一帧
     *
     * @param timestamp 时间戳
     * @return H264VideoFrame
     */
    private H264VideoFrame doNaluBuffers(long timestamp) {
        if (this.naluBuffers.isEmpty()) {
            throw new RtpCommException("the number of buffers is 0");
        }
        H264NaluSingle single = this.naluBuffers.get(0);
        int sum = this.naluBuffers.stream().mapToInt(H264NaluSingle::byteArrayLength).sum();
        ByteWriteBuff buff = new ByteWriteBuff(sum);
        naluBuffers.forEach(x -> buff.putBytes(x.toByteArray()));
        naluBuffers.clear();

        return new H264VideoFrame(single.getHeader().getType(), timestamp - this.baseTimestamp, buff.getData());
    }

    /**
     * 执行FuA的缓存，将FuA打包成一个单Nalu
     *
     * @return H264NaluSingle
     */
    private H264NaluSingle doFuABuffers() {
        if (this.naluFuADataBuffers.isEmpty()) {
            throw new RtpCommException("FuA the number of buffers is 0");
        }

        H264NaluFuA naluFuA = this.naluFuADataBuffers.get(0);
        int sum = this.naluFuADataBuffers.stream().mapToInt(x -> x.getPayload().length).sum();
        ByteWriteBuff buff = new ByteWriteBuff(sum);
        naluFuADataBuffers.forEach(x -> buff.putBytes(x.getPayload()));
        naluFuADataBuffers.clear();

        H264NaluSingle single = new H264NaluSingle();
        single.getHeader().setForbiddenZeroBit(naluFuA.getHeader().isForbiddenZeroBit());
        single.getHeader().setNri(naluFuA.getHeader().getNri());
        single.getHeader().setType(naluFuA.getFuHeader().getType());
        single.setPayload(buff.getData());
        return single;
    }

    /**
     * 处理RTP包
     *
     * @param rtp rtp数据包
     */
    @Override
    public void processPackage(RtpPackage rtp) {
        // 过滤负载编号不一致的rtp
        if (rtp.getHeader().getPayloadType() != this.payloadNumber) {
            log.warn("payload numbers are inconsistent, expect[{}], actual[{}]", this.payloadNumber, rtp.getHeader().getPayloadType());
            return;
        }
        // 第一次更新时间
        if (this.baseTimestamp == 0) {
            this.baseTimestamp = rtp.getHeader().getTimestamp();
        }
        H264NaluBase h264Nalu = H264NaluBuilder.parsePackage(rtp.getPayload());
        EH264NaluType naluType = h264Nalu.getHeader().getType();
        H264VideoFrame frame = null;
        switch (naluType) {
            case AUD:
                this.resetBuffers();
                break;
            case SEI:
            case PPS:
            case SPS:
                H264NaluSingle single = (H264NaluSingle) h264Nalu;
                frame = new H264VideoFrame(single.getHeader().getType(), rtp.getHeader().getTimestamp() - this.baseTimestamp, single.toByteArray());
                this.videoFrameHandle(frame);
                break;
            case NON_IDR_SLICE:
            case IDR_SLICE:
                H264NaluSingle naluSingle = (H264NaluSingle) h264Nalu;
                this.naluBuffers.add(naluSingle);
                if (rtp.getHeader().isMarker()) {
                    frame = this.doNaluBuffers(rtp.getHeader().getTimestamp());
                    this.videoFrameHandle(frame);
                }
                break;
            case FU_A:
                H264NaluFuA naluFuA = (H264NaluFuA) h264Nalu;
                if (naluFuA.getFuHeader().isStart()) {
                    this.naluFuADataBuffers.clear();
                }
                this.naluFuADataBuffers.add(naluFuA);
                if (naluFuA.getFuHeader().isEnd()) {
                    H264NaluSingle naluSingle1 = this.doFuABuffers();
                    this.naluBuffers.add(naluSingle1);
                }
                if (rtp.getHeader().isMarker()) {
                    frame = this.doNaluBuffers(rtp.getHeader().getTimestamp());
                    this.videoFrameHandle(frame);
                }
                break;
            case STAP_A:
                // TODO: 待确认
//                H264NaluStapA naluStapA = (H264NaluStapA) h264Nalu;
//                List<H264VideoFrame> collect = naluStapA.getNaluSingles().stream()
//                        .filter(x -> x.getHeader().getType() == EH264NaluType.NON_IDR_SLICE)
//                        .map(x -> new H264VideoFrame(x.getHeader().getType(), rtp.getHeader().getTimestamp() - this.baseTimestamp, x.toByteArray()))
//                        .filter(x -> x.getSliceType() == EH264SliceType.I || x.getSliceType() == EH264SliceType.P || x.getSliceType() == EH264SliceType.B)
//                        .collect(Collectors.toList());
//                collect.forEach(this::videoFrameHandle);
                break;
            default:
                log.error("RTP parsing unknown data type [{}], timestamp [{}]", naluType, rtp.getHeader().getTimestamp());
                break;
        }
    }

    @Override
    public void onFrameHandle(Consumer<RawFrame> frameHandle) {
        this.frameHandle = frameHandle;
    }

    private void videoFrameHandle(H264VideoFrame frame) {
        if (this.frameHandle == null || frame == null || frame.getPts() < 0) {
            return;
        }
        H264VideoFrame h264VideoFrame;
        if (frame.getNaluType() == EH264NaluType.IDR_SLICE || frame.getNaluType() == EH264NaluType.NON_IDR_SLICE) {
            // 只处理I帧，P帧，B帧的数据
            h264VideoFrame = this.dtsHandle(frame);
            this.addLastFrame(frame);
            if (h264VideoFrame == null) {
                return;
            }
        } else {
            // 处理SPS, PPS等其他
            h264VideoFrame = frame;
        }

        try {
            this.frameHandle.accept(h264VideoFrame);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Handle DTS of frame.
     *
     * @param frame video frame
     * @return last frame
     */
    private H264VideoFrame dtsHandle(H264VideoFrame frame) {
        if (frame.getSliceType() == EH264SliceType.B && !this.hasBFrame) {
            this.hasBFrame = true;
        }
        if (this.lastFrame == null) {
            return null;
        }
        if (this.hasBFrame && frame.getSliceType() != EH264SliceType.I && this.cacheFrameList.size() >= 5) {
            long delta = (this.cacheFrameList.get(4).getPts() - this.cacheFrameList.get(0).getPts()) / 4;
            frame.setDts(this.lastFrame.getDts() + delta);
        }
        this.lastFrame.setDuration((int) (frame.getDts() - this.lastFrame.getDts()));
        if (this.lastFrame.getDuration() < 0) {
            this.lastFrame.setDuration(0);
            if (frame.getSliceType() != EH264SliceType.I) {
                frame.setDts(this.lastFrame.getDts());
            } else {
                this.lastFrame.setDts(frame.getDts());
            }
        }
        return this.lastFrame;
    }

    /**
     * Add last frame for cache
     *
     * @param frame h264 video frame
     */
    private void addLastFrame(H264VideoFrame frame) {
        this.lastFrame = frame;
        this.cacheFrameList.add(frame);
        this.cacheFrameList.sort((a, b) -> (int) (a.getTimestamp() - b.getTimestamp()));
        if (this.cacheFrameList.size() > 10) {
            this.cacheFrameList.remove(0);
        }
    }
}
