package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;

import java.util.Collections;
import java.util.List;

/**
 * NCK请求构建器
 *
 * @author xingshuang
 */
public class NckRequestBuilder {

    private NckRequestBuilder() {
        // NOOP
    }

    public static S7Data creatNckRequest(RequestNckItem item) {
        return creatNckRequest(Collections.singletonList(item));
    }

    public static S7Data creatNckRequest(List<RequestNckItem> items) {
        S7Data s7Data = new S7Data();
        s7Data.setTpkt(new TPKT());
        s7Data.setCotp(COTPData.createDefault());
        s7Data.setHeader(Header.createDefault());
        s7Data.setParameter(ReadWriteParameter.createReqParameter(EFunctionCode.READ_VARIABLE, items));
        s7Data.selfCheck();
        return s7Data;
    }
}
