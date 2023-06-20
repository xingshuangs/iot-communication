package com.github.xingshuangs.iot.protocol.rtsp.model.base;


import lombok.Data;

import java.util.Map;

/**
 * 范围
 *
 * @author xingshuang
 */
@Data
public class RtspRangeNpt extends RtspRange {

    /**
     * 起始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    public RtspRangeNpt() {
        this(null, null);
    }

    public RtspRangeNpt(String startTime) {
        this(startTime, null);
    }

    public RtspRangeNpt(String startTime, String endTime) {
        this.type = "npt";
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        if ((this.startTime == null || this.startTime.equals("")) && (this.endTime == null || this.endTime.equals(""))) {
            return "";
        }
        if ((this.startTime != null && !this.startTime.equals("")) && (this.endTime == null || this.endTime.equals(""))) {
            return String.format("%s=%s-", this.type, this.startTime);
        }
        if ((this.startTime != null && !this.startTime.equals("")) && (this.endTime != null && !this.endTime.equals(""))) {
            return String.format("%s=&s-%s", this.type, this.startTime, this.endTime);
        }
        return "";
    }
}
