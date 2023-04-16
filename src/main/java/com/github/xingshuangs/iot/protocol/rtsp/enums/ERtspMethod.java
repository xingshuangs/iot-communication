package com.github.xingshuangs.iot.protocol.rtsp.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * ERtspMethod方法
 *
 * @author xingshuang
 */
public enum ERtspMethod {

    /**
     * 广播
     */
    ANNOUNCE("ANNOUNCE"),

    /**
     * 重定向
     */
    REDIRECT("REDIRECT"),


    /**
     * 选项
     */
    OPTIONS("OPTIONS"),

    /**
     * 描述
     */
    DESCRIBE("DESCRIBE"),

    /**
     * 设置
     */
    SETUP("SETUP"),

    /**
     * 暂停
     */
    PAUSE("PAUSE"),

    /**
     * 播放
     */
    PLAY("PLAY"),

    /**
     * 录入
     */
    RECORD("RECORD"),

    /**
     * 销毁
     */
    TEARDOWN("TEARDOWN"),

    /**
     * 获取参数
     */
    GET_PARAMETER("GET_PARAMETER"),

    /**
     * 设置参数
     */
    SET_PARAMETER("SET_PARAMETER"),

    ;

    private static Map<String, ERtspMethod> map;

    public static ERtspMethod from(String data) {
        if (map == null) {
            map = new HashMap<>();
            for (ERtspMethod item : ERtspMethod.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final String code;

    ERtspMethod(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
