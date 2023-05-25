package com.github.xingshuangs.iot.protocol.rtp.model.payload;


import com.github.xingshuangs.iot.exceptions.RtpCommException;

/**
 * @author xingshuang
 */
public class H264NaluBuilder {

    private H264NaluBuilder() {
        // NOOP
    }

    public static H264NaluBase parsePackage(final byte[] data) {
        return parsePackage(data,0);
    }

    public static H264NaluBase parsePackage(final byte[] data, final int offset) {
        H264NaluHeader header = H264NaluHeader.fromBytes(data, offset);
        switch (header.getType()) {
            case SEI:
            case SPS:
            case PPS:
            case NON_IDR_SLICE:
            case IDR_SLICE:
                return H264NaluSingle.fromBytes(data, offset);
            case STAP_A:
                return H264NaluStapA.fromBytes(data, offset);
            case STAP_B:
                return H264NaluStapB.fromBytes(data, offset);
            case FU_A:
                return H264NaluFuA.fromBytes(data, offset);
            case FU_B:
                return H264NaluFuB.fromBytes(data, offset);
            case MTAP16:
                return H264NaluMtap16.fromBytes(data, offset);
            case MTAP24:
                return H264NaluMtap24.fromBytes(data, offset);
            default:
                throw new RtpCommException("无法识别类型");
        }
    }
}
