package com.github.xingshuangs.iot.exceptions;

/**
 * MC communication runtime exception class.
 * (MC通信异常)
 *
 * @author xingshuang
 */
public class McCommException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public McCommException() {
        super();
    }

    public McCommException(String message, Throwable cause) {
        super(message, cause);
    }

    public McCommException(String message) {
        super(message);
    }

    public McCommException(Throwable cause) {
        super(cause);
    }
}
