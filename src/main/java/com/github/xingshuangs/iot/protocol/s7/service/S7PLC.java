package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.s7.enums.EArea;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.enums.EReturnCode;
import com.github.xingshuangs.iot.protocol.s7.model.DataItem;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;
import com.github.xingshuangs.iot.protocol.s7.utils.S7AddressUtil;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import com.github.xingshuangs.iot.utils.ShortUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xingshuang
 */
public class S7PLC extends PLCNetwork {

    public static final int PORT = 102;
    public static final String IP = "127.0.0.1";

    private EPlcType plcType = EPlcType.S1200;

    public S7PLC() {
        this(EPlcType.S1200, IP, PORT);
    }

    public S7PLC(EPlcType plcType) {
        this(plcType, IP, PORT);
    }

    public S7PLC(EPlcType plcType, String ip) {
        this(plcType, ip, PORT);
    }

    public S7PLC(EPlcType plcType, String ip, int port) {
        super(ip, port);
        this.plcType = plcType;
    }

    public byte[] readBytes(EArea area, int dbNumber, int count, int byteAddress) throws IOException {
        RequestItem item = new RequestItem();
        item.setCount(count);
        item.setDbNumber(dbNumber);
        item.setArea(area);
        item.setByteAddress(byteAddress);
        DataItem dataItem = this.readS7Data(item);
        return dataItem.getData();
    }

    public boolean readBoolean(String address) throws IOException {
        DataItem dataItem = this.readS7Data(S7AddressUtil.parseBit(address));
        if (dataItem.getReturnCode() == EReturnCode.SUCCESS) {
            return BooleanUtil.getValue(dataItem.getData()[0], 0);
        } else {
            throw new S7CommException(String.format("readBoolean读取失败，原因：%s", dataItem.getReturnCode().getDescription()));
        }
    }

    public int readUInt16(String address) throws IOException {
        RequestItem item = new RequestItem();
        item.setCount(2);
        item.setDbNumber(1);
        item.setArea(EArea.DATA_BLOCKS);
        item.setByteAddress(2);
        DataItem dataItem = this.readS7Data(item);
        return ShortUtil.toUInt16(dataItem.getData());
    }

    public List<Integer> readUInt32(String var) throws IOException {
        List<RequestItem> list = new ArrayList<>();
        RequestItem item = new RequestItem();
        item.setCount(2);
        item.setDbNumber(1);
        item.setArea(EArea.DATA_BLOCKS);
        item.setByteAddress(2);
        list.add(item);
        RequestItem item1 = new RequestItem();
        item1.setCount(2);
        item1.setDbNumber(2);
        item1.setArea(EArea.DATA_BLOCKS);
        item1.setByteAddress(2);
        list.add(item1);
        List<DataItem> dataItems = this.readS7Data(list);

        return dataItems.stream().map(x -> ShortUtil.toUInt16(x.getData())).collect(Collectors.toList());
    }
}
