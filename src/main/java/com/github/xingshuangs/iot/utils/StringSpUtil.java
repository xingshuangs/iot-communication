package com.github.xingshuangs.iot.utils;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 字符串分割工具
 *
 * @author xingshuang
 */
public class StringSpUtil {

    private StringSpUtil() {
        // NOOP
    }

    /**
     * 两步分割，按行分割并转化为Map
     *
     * @param src      字符串
     * @param lineChar 行字符串分割字符
     * @param midChar  中间字符串分割字符
     * @return 分割结果Map
     */
    public static Map<String, String> splitTwoStepByLine(String src, String lineChar, String midChar) {
        String[] data = src.split(lineChar);
        Map<String, String> res = new LinkedHashMap<>();
        for (String item : data) {
            int index = item.indexOf(midChar);
            if (index >= 0) {
                res.put(item.substring(0, index).trim(), item.substring(index + 1).trim());
            }
        }
        return res;
    }

    /**
     * 一步分割，按行分割转化为List
     *
     * @param src      字符串
     * @param lineChar 行字符串分割符
     * @return 分割结果List
     */
    public static List<String> splitOneStepByLine(String src, String lineChar) {
        String[] data = src.split(lineChar);
        List<String> res = new ArrayList<>();
        for (String item : data) {
            String tmp = item.trim();
            if (!tmp.equals("")) {
                res.add(tmp);
            }
        }
        return res;
    }

    /**
     * 获取字符串标记的所有索引
     *
     * @param src  数据源字符串
     * @param flag 标记字符串
     * @return 索引列表
     */
    public static List<Integer> findFlagAllIndexes(String src, String flag) {
        List<Integer> res = new ArrayList<>();
        int index = 0;
        while ((index = src.indexOf(flag)) >= 0) {
            res.add(index);
        }
        return res;
    }
}
