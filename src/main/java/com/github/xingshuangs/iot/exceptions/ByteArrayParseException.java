package com.github.xingshuangs.iot.exceptions;

/**
 * 字节数组解析异常
 *
 * @author ShuangPC
 */
public class ByteArrayParseException extends RuntimeException {

    public ByteArrayParseException() {
        super();
    }

    public ByteArrayParseException(String message) {
        super(message);
    }

    public ByteArrayParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ByteArrayParseException(Throwable cause) {
        super(cause);
    }

    protected ByteArrayParseException(String message, Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
