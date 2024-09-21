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

package com.github.xingshuangs.iot.utils;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * String splitting tool.
 * (字符串分割工具)
 *
 * @author xingshuang
 */
public class StringSpUtil {

    private StringSpUtil() {
        // NOOP
    }

    /**
     * Two-step segmentation, divided by row and converted to Map.
     * (两步分割，按行分割并转化为Map)
     *
     * @param src     string
     * @param rowChar row string split character
     * @param midChar middle string split character
     * @return map
     */
    public static Map<String, String> splitTwoStepByLine(String src, String rowChar, String midChar) {
        String[] data = src.split(rowChar);
        Map<String, String> res = new LinkedHashMap<>();
        for (String item : data) {
            int index = item.indexOf(midChar);
            if (index >= 0) {
                res.putIfAbsent(item.substring(0, index).trim(), item.substring(index + 1).trim());
            }
        }
        return res;
    }

    /**
     * One-step split, split by row converted to List.
     * (一步分割，按行分割转化为List)
     *
     * @param src     string
     * @param rowChar row string split character
     * @return list
     */
    public static List<String> splitOneStepByLine(String src, String rowChar) {
        String[] data = src.split(rowChar);
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
     * Gets all indexes of string tags.
     * (获取字符串标记的所有索引)
     *
     * @param src string
     * @param tag tag string
     * @return index list
     */
    public static List<Integer> findFlagAllIndexes(String src, String tag) {
        List<Integer> res = new ArrayList<>();
        int index = src.indexOf(tag, 0);
        while (index >= 0) {
            res.add(index);
            index = src.indexOf(tag, index + tag.length());
        }
        return res;
    }
}
