package com.github.xingshuangs.iot.protocol.s7.service;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地址的包装类
 *
 * @author xingshuang
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressWrapper {

    /**
     * 地址 例如：DB15.2.0
     */
    private String address;

    /**
     * 数量
     */
    private int count;
}
