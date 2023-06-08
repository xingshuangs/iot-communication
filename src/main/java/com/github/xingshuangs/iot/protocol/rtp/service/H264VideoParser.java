package com.github.xingshuangs.iot.protocol.rtp.service;


import com.github.xingshuangs.iot.exceptions.RtpCommException;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.rtp.enums.EH264NaluType;
import com.github.xingshuangs.iot.protocol.rtp.model.RtpPackage;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.H264VideoFrame;
import com.github.xingshuangs.iot.protocol.rtp.model.frame.RawFrame;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.H264NaluBase;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.H264NaluBuilder;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.H264NaluFuA;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.H264NaluSingle;
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

    private final List<H264NaluFuA> buffers = new ArrayList<>();

    private void resetBuffers() {
        this.buffers.clear();
    }

    private H264VideoFrame doBuffers(long timestamp) {
        if (this.buffers.isEmpty()) {
            throw new RtpCommException("buffer缓存数量为0");
        }
        H264NaluFuA naluFuA = this.buffers.get(0);
        int sum = this.buffers.stream().mapToInt(x -> x.getPayload().length).sum();
        ByteWriteBuff buff = new ByteWriteBuff(sum);
        buffers.forEach(x -> buff.putBytes(x.getPayload()));
        buffers.clear();

        H264NaluSingle single = new H264NaluSingle();
        single.getHeader().setForbiddenZeroBit(naluFuA.getHeader().isForbiddenZeroBit());
        single.getHeader().setNri(naluFuA.getHeader().getNri());
        single.getHeader().setType(naluFuA.getFuHeader().getType());
        single.setPayload(buff.getData());

        return new H264VideoFrame(single.getHeader().getType(), timestamp, single.toByteArray());
    }

    /**
     * 处理RTP包
     *
     * @param rtp rtp数据包
     */
    public void processPackage(RtpPackage rtp, Consumer<RawFrame> frameHandle) {
        H264NaluBase h264Nalu = H264NaluBuilder.parsePackage(rtp.getPayload());
        EH264NaluType naluType = h264Nalu.getHeader().getType();
        H264VideoFrame frame = null;
        switch (naluType) {
            case SEI:
            case PPS:
            case SPS:
            case NON_IDR_SLICE:
            case IDR_SLICE:
                H264NaluSingle naluSingle = (H264NaluSingle) h264Nalu;
//                log.debug("Mark[{}], Type[{}]", rtp.getHeader().isMarker(), naluType);
                frame = new H264VideoFrame(naluType, rtp.getHeader().getTimestamp(), naluSingle.toByteArray());
                break;
            case FU_A:
                H264NaluFuA naluFuA = (H264NaluFuA) h264Nalu;
//                log.debug("Mark[{}], Type[{}], start[{}], end[{}], unitType[{}]", rtp.getHeader().isMarker(), naluType,
//                        naluFuA.getFuHeader().isStart(), naluFuA.getFuHeader().isEnd(), naluFuA.getFuHeader().getType());

                if (naluFuA.getFuHeader().isStart()) {
                    this.resetBuffers();
//                    log.debug("FuA开始：Mark[{}], Type[{}], start[{}], end[{}], unitType[{}], 时间戳[{}]", rtp.getHeader().isMarker(), naluType,
//                            naluFuA.getFuHeader().isStart(), naluFuA.getFuHeader().isEnd(), naluFuA.getFuHeader().getType(), rtp.getHeader().getTimestamp());
                }
                this.buffers.add(naluFuA);
                if (naluFuA.getFuHeader().isEnd()) {
//                    log.debug("FuA结束：Mark[{}], Type[{}], start[{}], end[{}], unitType[{}], 时间戳[{}]", rtp.getHeader().isMarker(), naluType,
//                            naluFuA.getFuHeader().isStart(), naluFuA.getFuHeader().isEnd(), naluFuA.getFuHeader().getType(), rtp.getHeader().getTimestamp());
                    frame = this.doBuffers(rtp.getHeader().getTimestamp());
                }
                break;
        }
        if (frameHandle != null && frame != null && frame.getFrameSegment().length > 0) {
            frameHandle.accept(frame);
        }
    }
}
