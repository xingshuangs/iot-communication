package com.github.xingshuangs.iot.exceptions;

/**
 * socket执行超时异常
 *
 * @author xingshuang
 */
public class SocketTimeoutException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SocketTimeoutException() {
        super();
    }

    public SocketTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketTimeoutException(String message) {
        super(message);
    }

    public SocketTimeoutException(Throwable cause) {
        super(cause);
    }
}
