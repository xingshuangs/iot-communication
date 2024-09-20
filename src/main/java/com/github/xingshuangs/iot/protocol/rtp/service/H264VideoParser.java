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
import com.github.xingshuangs.iot.protocol.rtp.enums.EH264NaluType;
import com.github.xingshuangs.iot.protocol.rtp.enums.EH264SliceType;
import com.github.xingshuangs.iot.protocol.rtp.model.RtpPackage;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.H264VideoFrame;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.RawFrame;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
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
     * RTP package cache list.
     */
    private final List<RtpPackage> rtpPackageList = new ArrayList<>();

    /**
     * 单Nalu的缓存
     */
    private final List<RtpPackage> naluBuffers = new ArrayList<>();

    private RtpPackage lastRtpPackage;

    /**
     * 是否有B帧
     */
    private boolean hasBFrame;

    public H264VideoParser(Integer payloadNumber) {
        this.payloadNumber = payloadNumber;
    }

    private void resetBuffers() {
        this.naluBuffers.clear();
    }

    /**
     * 执行Nalu的缓存，打包成一帧
     *
     * @return H264VideoFrame
     */
    private H264VideoFrame doRtpNaluSingleBuffers() {
        if (this.naluBuffers.isEmpty()) {
            return null;
        }
        try {
            this.naluBuffers.sort(Comparator.comparingInt(a -> a.getHeader().getSequenceNumber()));
//            boolean match = this.checkMatchLostNumber();
//            if (!match) {
//                return null;
//            }
            RtpPackage rtp = this.naluBuffers.get(0);
            EH264NaluType currentNaluType = this.queryNaluType();
            List<byte[]> naluSingleBytes = this.createNaluSingleBytes();
            if (naluSingleBytes.isEmpty()) {
                return null;
            }

            int sum = naluSingleBytes.stream().mapToInt(x -> x.length).sum() + (naluSingleBytes.size() - 1) * 4;
            ByteWriteBuff buff = new ByteWriteBuff(sum);
            for (int i = 0; i < naluSingleBytes.size() - 1; i++) {
                buff.putBytes(naluSingleBytes.get(i));
                // 多slice的NAL拼装需要添加分隔符
                buff.putBytes(new byte[]{0x00, 0x00, 0x00, 0x01});
            }
            buff.putBytes(naluSingleBytes.get(naluSingleBytes.size() - 1));

            return new H264VideoFrame(currentNaluType, rtp.getHeader().getTimestamp() - this.baseTimestamp, buff.getData());
        } finally {
            this.naluBuffers.clear();
        }
    }

    private EH264NaluType queryNaluType() {
        for (RtpPackage rtpPackage : this.naluBuffers) {
            H264NaluHeader h264NaluHeader = H264NaluHeader.fromBytes(rtpPackage.getPayload());
            if (h264NaluHeader.getType() == EH264NaluType.NON_IDR_SLICE || h264NaluHeader.getType() == EH264NaluType.IDR_SLICE) {
                return h264NaluHeader.getType();
            } else if (h264NaluHeader.getType() == EH264NaluType.FU_A) {
                H264NaluFuA h264NaluFuA = (H264NaluFuA) H264NaluBuilder.parsePackage(rtpPackage.getPayload());
                return h264NaluFuA.getFuHeader().getType();
            }
        }
        return EH264NaluType.NON_IDR_SLICE;
    }

    private List<byte[]> createNaluSingleBytes() {
        List<byte[]> naluSingleBytes = new ArrayList<>();
        List<H264NaluFuA> naluFuAList = new ArrayList<>();
        for (RtpPackage rtpPackage : this.naluBuffers) {
            H264NaluHeader h264NaluHeader = H264NaluHeader.fromBytes(rtpPackage.getPayload());
            if (h264NaluHeader.getType() == EH264NaluType.NON_IDR_SLICE || h264NaluHeader.getType() == EH264NaluType.IDR_SLICE) {
                naluSingleBytes.add(rtpPackage.getPayload());
            } else if (h264NaluHeader.getType() == EH264NaluType.FU_A) {
                H264NaluFuA h264NaluFuA = (H264NaluFuA) H264NaluBuilder.parsePackage(rtpPackage.getPayload());
                if (h264NaluFuA.getFuHeader().isStart()) {
                    naluFuAList.clear();
                }
                naluFuAList.add(h264NaluFuA);
                if (h264NaluFuA.getFuHeader().isEnd()) {
                    int sum = naluFuAList.stream().mapToInt(x -> x.getPayload().length).sum();
                    ByteWriteBuff buff = new ByteWriteBuff(sum);
                    naluFuAList.forEach(x -> buff.putBytes(x.getPayload()));
                    H264NaluSingle single = new H264NaluSingle();
                    single.getHeader().setForbiddenZeroBit(h264NaluFuA.getHeader().isForbiddenZeroBit());
                    single.getHeader().setNri(h264NaluFuA.getHeader().getNri());
                    single.getHeader().setType(h264NaluFuA.getFuHeader().getType());
                    single.setPayload(buff.getData());
                    naluSingleBytes.add(single.toByteArray());
                }
            }
        }
        return naluSingleBytes;
    }

    private boolean checkMatchLostNumber() {
        int lostNumber = 0;
        for (int i = 1; i < this.naluBuffers.size(); i++) {
            if (this.naluBuffers.get(i).getHeader().getSequenceNumber() - this.naluBuffers.get(i - 1).getHeader().getSequenceNumber() != 1) {
                lostNumber++;
            }
        }
        int idrSliceMinNumber = 1;
        int nonIdrSliceMinNumber = 3;
        RtpPackage rtp = this.naluBuffers.get(0);
        H264NaluHeader naluHeader = H264NaluHeader.fromBytes(rtp.getPayload());
        if (naluHeader.getType() == EH264NaluType.IDR_SLICE && lostNumber > idrSliceMinNumber) {
            log.debug("处理Single的NALU时发生数据序列号不连续，导致关键帧数据因丢包导致数据丢失，总数量[{}]，丢失数量[{}]超过[{}]，丢弃该帧数据", this.naluBuffers.size(), lostNumber, idrSliceMinNumber);
            return false;
        } else if (naluHeader.getType() == EH264NaluType.NON_IDR_SLICE && lostNumber > nonIdrSliceMinNumber) {
            log.debug("处理Single的NALU时发生数据序列号不连续，导致非关键帧数据因丢包导致数据丢失，总数量[{}]，丢失数量[{}]超过[{}]，丢弃该帧数据", this.naluBuffers.size(), lostNumber, nonIdrSliceMinNumber);
            return false;
        } else if (naluHeader.getType() == EH264NaluType.FU_A) {
            H264NaluFuHeader naluFuHeader = H264NaluFuHeader.fromBytes(rtp.getPayload(), 1);
            if (naluFuHeader.getType() == EH264NaluType.IDR_SLICE && lostNumber > idrSliceMinNumber) {
                log.debug("处理FUA的NALU时发生数据序列号不连续，导致关键帧数据因丢包导致数据丢失，总数量[{}]，丢失数量[{}]超过[{}]，丢弃该帧数据", this.naluBuffers.size(), lostNumber, idrSliceMinNumber);
                return false;
            } else if (naluFuHeader.getType() == EH264NaluType.NON_IDR_SLICE && lostNumber > nonIdrSliceMinNumber) {
                log.debug("处理FUA的NALU时发生数据序列号不连续，导致非关键帧帧数据因丢包导致数据丢失，总数量[{}]，丢失数量[{}]超过[{}]，丢弃该帧数据", this.naluBuffers.size(), lostNumber, nonIdrSliceMinNumber);
                return false;
            }
        } else if (naluHeader.getType() != EH264NaluType.IDR_SLICE && naluHeader.getType() != EH264NaluType.NON_IDR_SLICE) {
            log.error("无法识别RTP中NALU的数据类型，丢弃该帧数据，帧类型[{}]", naluHeader.getType());
            return false;
        }
        return true;
    }

    /**
     * 处理RTP包
     *
     * @param rtpPackage rtp数据包
     */
    @Override
    public void processPackage(RtpPackage rtpPackage) {
        // 过滤负载编号不一致的rtp
        if (rtpPackage.getHeader().getPayloadType() != this.payloadNumber) {
            log.warn("payload numbers are inconsistent, expect[{}], actual[{}], ignore this message.", this.payloadNumber, rtpPackage.getHeader().getPayloadType());
            return;
        }
        RtpPackage rtp = this.addLastRtpPackage(rtpPackage);
        if (rtp == null) {
            return;
        }
        // 第一次更新时间
        if (this.baseTimestamp == 0) {
            this.baseTimestamp = rtp.getHeader().getTimestamp();
        }
        H264NaluHeader header = H264NaluHeader.fromBytes(rtp.getPayload());
        H264VideoFrame frame;
        switch (header.getType()) {
            case AUD:
                this.resetBuffers();
                break;
            case SEI:
            case PPS:
            case SPS:
                frame = new H264VideoFrame(header.getType(), rtp.getHeader().getTimestamp() - this.baseTimestamp, rtp.getPayload());
                this.videoFrameHandle(frame);
                break;
            case NON_IDR_SLICE:
            case IDR_SLICE:
            case FU_A:
                this.naluBuffers.add(rtp);
                if (rtp.getHeader().isMarker()) {
                    frame = this.doRtpNaluSingleBuffers();
                    this.videoFrameHandle(frame);
                }
                break;
            case STAP_A:
            case STAP_B:
                break;
            default:
                log.error("RTP parsing unknown data type [{}], timestamp [{}]", header.getType(), rtp.getHeader().getTimestamp());
                break;
        }
    }

    private RtpPackage addLastRtpPackage(RtpPackage rtp) {
        RtpPackage currentRtp = null;
        this.rtpPackageList.add(rtp);
        this.rtpPackageList.sort(Comparator.comparingInt(a -> a.getHeader().getSequenceNumber()));
        if (this.rtpPackageList.size() > 60) {
            currentRtp = this.rtpPackageList.remove(0);
        }
        if (currentRtp != null
                && this.lastRtpPackage != null
                && this.lastRtpPackage.getHeader().getSequenceNumber() > currentRtp.getHeader().getSequenceNumber()) {
            log.warn("The sequence number is not always ascending when receiving RTP data");
        }
        this.lastRtpPackage = currentRtp;
        return currentRtp;
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
     * Add last frame for cache, use for calculating dts.
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
