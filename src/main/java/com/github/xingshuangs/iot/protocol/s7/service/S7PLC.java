package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.enums.EArea;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.model.DataItem;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;
import com.github.xingshuangs.iot.utils.ShortUtil;

import java.io.IOException;

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

    public byte[] readBytes() throws IOException {
        RequestItem item = new RequestItem();
        item.setCount(2);
        item.setDbNumber(1);
        item.setArea(EArea.DATA_BLOCKS);
        item.setByteAddress(2);
        DataItem dataItem = this.readS7Data(item);
        return dataItem.getData();
    }

    public int readUInt16(String var) throws IOException {
        RequestItem item = new RequestItem();
        item.setCount(2);
        item.setDbNumber(1);
        item.setArea(EArea.DATA_BLOCKS);
        item.setByteAddress(2);
        DataItem dataItem = this.readS7Data(item);
        return ShortUtil.toUInt16(dataItem.getData());
    }

    public int readUInt32(String var) {
        return 0;
    }
}
