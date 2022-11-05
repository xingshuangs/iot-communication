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
     *  1,50,65,200,   322,    99,500,        44
     * |1,50,65,90|110,106|216|99,117|221|162,44|
     *  第一      第二      第三 第四   第五 第六
     *
     * @param src          数据源
     * @param targetNumber 目标值
     * @param extraNumber  每个数据额外占用的数据大小
     * @return 分组结果
     */
    public static List<S7ComGroup> recombination(List<Integer> src, int targetNumber, int extraNumber) {
        List<S7ComGroup> groupList = new LinkedList<>();
        S7ComGroup group = new S7ComGroup();
        int sum = group.total();
        groupList.add(group);
        for (int i = 0; i < src.size(); i++) {
            int number = src.get(i);
            int offset = 0;
            while (number > 0) {
                S7ComItem s7ComItem = new S7ComItem(i, src.get(i), offset, 0, extraNumber);
                if (sum + number + extraNumber > targetNumber) {
                    s7ComItem.setRipeData(targetNumber - sum - extraNumber);
                } else {
                    s7ComItem.setRipeData(number);
                }
                number -= s7ComItem.getRipeData();
                offset += s7ComItem.getRipeData();
                sum += s7ComItem.getTotalLength();
                group.add(s7ComItem);
                if (sum >= targetNumber - extraNumber) {
                    group = new S7ComGroup();
                    groupList.add(group);
                    sum = group.total();
                }
            }
        }
        return groupList;
    }
}
