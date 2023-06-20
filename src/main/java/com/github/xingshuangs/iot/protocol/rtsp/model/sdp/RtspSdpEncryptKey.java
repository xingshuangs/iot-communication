package com.github.xingshuangs.iot.protocol.rtsp.model.sdp;


import lombok.Data;

/**
 * 加密秘钥
 *
 * @author xingshuang
 */
@Data
public class RtspSdpEncryptKey {

    /**
     * 方法名
     */
    private String name;

    /**
     * 秘钥
     */
    private String value;
}
