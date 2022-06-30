package com.github.xingshuangs.iot.protocol.modbus.model;


import com.github.xingshuangs.iot.protocol.modbus.enums.EMbFunctionCode;
import lombok.Data;

/**
 * modbus的协议数据单元
 *
 * @author xingshuang
 */
@Data
public class MbPdu {

    /**
     * 功能码
     */
    private EMbFunctionCode functionCode;
}
