package com.github.xingshuangs.iot.protocol.rtsp.sdp.attribute;


import com.github.xingshuangs.iot.exceptions.RtspCommException;
import lombok.Data;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.COMMA;

/**
 * 附加信息：维度
 * x-dimensions:2048,1536
 *
 * @author xingshuang
 */
@Data
public class RtspSdpMediaAttrDimension {

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;

    public static RtspSdpMediaAttrDimension fromString(String src) {
        if (src == null || src.equals("")) {
            throw new IllegalArgumentException("SDP解析MediaAttrDimension部分数据源错误");
        }
        RtspSdpMediaAttrDimension dimension = new RtspSdpMediaAttrDimension();
        String[] split = src.split(COMMA);
        if (split.length != 2) {
            throw new RtspCommException("RtspSdpMediaAttrDimension数据有误，无法解析");
        }
        dimension.width = Integer.parseInt(split[0]);
        dimension.height = Integer.parseInt(split[1]);
        return dimension;
    }
}
