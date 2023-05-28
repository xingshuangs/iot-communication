package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.ENckArea;
import com.github.xingshuangs.iot.protocol.s7.enums.ENckModule;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.model.DataItem;
import com.github.xingshuangs.iot.protocol.s7.model.RequestNckItem;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author xingshuang
 */
public class DemoS7Nck1 {

    public static void main(String[] args) {
        S7PLC s7PLC = new S7PLC(EPlcType.SINUMERIK_828D, "127.0.0.1");

        // single request
        RequestNckItem requestNckItem = new RequestNckItem(ENckArea.N_NCK, 1, 18040, 4, ENckModule.M, 1);
        DataItem dataItem = s7PLC.readS7NckData(requestNckItem);
        String cncType = ByteReadBuff.newInstance(dataItem.getData(), true).getString(dataItem.getCount()).trim();
        System.out.println(cncType);

        // multi request
        List<RequestNckItem> requestNckItems = IntStream.of(1, 2, 3, 4)
                .mapToObj(x -> new RequestNckItem(ENckArea.C_CHANNEL, 1, 2, x, ENckModule.SMA, 1))
                .collect(Collectors.toList());
        List<DataItem> dataItems = s7PLC.readS7NckData(requestNckItems);
        List<Double> positions = dataItems.stream().map(x -> ByteReadBuff.newInstance(x.getData(), true).getFloat64())
                .collect(Collectors.toList());
        positions.forEach(System.out::println);

        s7PLC.close();
    }
}
