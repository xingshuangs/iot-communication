package com.github.xingshuangs.iot.exceptions;

/**
 * Modbus通信异常
 *
 * @author xingshuang
 */
public class ModbusCommException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ModbusCommException() {
        super();
    }

    public ModbusCommException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModbusCommException(String message) {
        super(message);
    }

    public ModbusCommException(Throwable cause) {
        super(cause);
    }
}
