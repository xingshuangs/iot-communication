package com.github.xingshuangs.iot.utils;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * 序号工具
 *
 * @author xingshuang
 */
public class SequenceNumberUtil {

    private static final AtomicInteger index = new AtomicInteger();

    private SequenceNumberUtil() {
        // NOOP
    }

    /**
     * 获取2字节大小的最新序号，0-65536
     *
     * @return 序号
     */
    public static int getUint16Number() {
        return getNumber(65536);
    }

    /**
     * 获取1字节大小的最新序号，0-256
     *
     * @return 序号
     */
    public static int getUint8Number() {
        return getNumber(256);
    }

    /**
     * 自定义最大值的最新序号
     *
     * @return 序号
     */
    public static int getNumber(int max) {
        int res = index.getAndIncrement();
        if (res >= max) {
            index.set(0);
            res = 0;
        }
        return res;
    }
}
