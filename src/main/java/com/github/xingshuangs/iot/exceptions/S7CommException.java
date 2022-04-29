package com.github.xingshuangs.iot.exceptions;

/**
 * S7通信异常
 *
 * @author xingshuang
 */
public class S7CommException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public S7CommException() {
        super();
    }

    public S7CommException(String message, Throwable cause) {
        super(message, cause);
    }

    public S7CommException(String message) {
        super(message);
    }

    public S7CommException(Throwable cause) {
        super(cause);
    }
}
