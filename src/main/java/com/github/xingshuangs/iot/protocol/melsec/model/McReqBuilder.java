package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSubHeader;

/**
 * MC请求构建器
 *
 * @author xingshuang
 */
public class McReqBuilder {

    private McReqBuilder() {
        // NOOP
    }

    /**
     * 创建4E帧的请求头
     *
     * @param accessRoute 访问路径
     * @param timer       监控时间，单位毫秒
     * @return McHeaderReq
     */
    public static McHeaderReq createMcHeaderReq4E(McAccessRoute accessRoute, int timer) {
        McHeaderReq req = new McHeaderReq();
        req.setSubHeader(EMcSubHeader.REQ_4E.getCode());
        req.setAccessRoute(accessRoute);
        req.setMonitoringTimer(timer / 250);
        return req;
    }
}
