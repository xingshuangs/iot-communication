package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;
import com.github.xingshuangs.iot.protocol.s7.utils.AddressUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址的包装类
 *
 * @author xingshuang
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MultiAddressRead {

    /**
     * 请求项列表
     */
    List<RequestItem> requestItems = new ArrayList<>();

    public MultiAddressRead addData(String address, int count){
        this.requestItems.add(AddressUtil.parseByte(address, count));
        return this;
    }
}
