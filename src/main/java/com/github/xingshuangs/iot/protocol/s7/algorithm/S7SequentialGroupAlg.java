package com.github.xingshuangs.iot.protocol.s7.algorithm;


import java.util.LinkedList;
import java.util.List;

/**
 * 顺序分组算法
 *
 * @author xingshuang
 */
public class S7SequentialGroupAlg {

    private S7SequentialGroupAlg() {
        // NOOP
    }

    /**
     * 重组，按照目标最大值进行顺序分组，超过最大值则进行分割
     * 示例：
     * 目标值：226 ，额外数据大小：5
     * 1,50,65,200,   322,    99,500,        44
     * |1,50,65,90|110,106|216|99,117|221|162,44|
     * 第一      第二      第三 第四   第五 第六
     *
     * @param src        数据源
     * @param targetSize 目标值
     * @param extraSize  每个数据额外占用的数据大小
     * @return 分组结果
     */
    public static List<S7ComGroup> writeRecombination(List<Integer> src, int targetSize, int extraSize) {
        List<S7ComGroup> groupList = new LinkedList<>();
        S7ComGroup group = new S7ComGroup();
        groupList.add(group);
        int sum = 0;
        for (int i = 0; i < src.size(); i++) {
            int number = src.get(i);
            int offset = 0;
            while (number > 0) {
                S7ComItem item = new S7ComItem(i, src.get(i), offset, 0, extraSize, 0);
                if (sum + number + item.getExtraSize() > targetSize) {
                    item.setRipeSize(targetSize - sum - item.getExtraSize());
                } else {
                    item.setRipeSize(number);
                }
                number -= item.getRipeSize();
                offset += item.getRipeSize();
                sum += item.getTotalLength();
                group.add(item);
                if (sum + extraSize >= targetSize) {
                    group = new S7ComGroup();
                    groupList.add(group);
                    sum = 0;
                }
            }
        }
        return groupList;
    }

    /**
     * 重组，按照目标最大值进行顺序分组，超过最大值则进行分割
     * 示例：
     * 目标值：226 ，额外数据大小：5，阀值数据大小：12
     * 1, 9, 102, 33, 2, 4, 8, 326, 2, 2, 2, 2, 2,         400, 2, 2, 2, 2, 2, 2, 2, 2,        2, 2, 2, 99
     * 1, 9, 102, 33, 2, 4, 8, 13| 221| 92, 2, 2, 2, 2, 2, 64|221|115, 2, 2, 2, 2, 2, 2, 2, 2| 2, 2, 2, 99
     *
     * @param src        数据源
     * @param targetSize 目标值
     * @param extraSize  每个数据额外占用的数据大小
     * @param threshold  阀值
     * @return 分组结果
     */
    public static List<S7ComGroup> readRecombination(List<Integer> src, int targetSize, int extraSize, int threshold) {
        List<S7ComGroup> groupList = new LinkedList<>();
        S7ComGroup group = new S7ComGroup();
        groupList.add(group);
        int sum = 0;
        for (int i = 0; i < src.size(); i++) {
            int number = src.get(i);
            int offset = 0;
            while (number > 0) {
                S7ComItem item = new S7ComItem(i, src.get(i), offset, 0, extraSize, threshold);
                if (sum + number + item.getExtraSize() > targetSize) {
                    item.setRipeSize(targetSize - sum - item.getExtraSize());
                } else {
                    item.setRipeSize(number);
                }
                sum += item.getTotalLength();
                number -= item.getRipeSize();
                offset += item.getRipeSize();
                group.add(item);
                if (sum + threshold >= targetSize) {
                    group = new S7ComGroup();
                    groupList.add(group);
                    sum = 0;
                }
            }
        }
        return groupList;
    }
}
