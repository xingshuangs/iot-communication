package com.github.xingshuangs.iot.exceptions;

/**
 * socket的运行异常
 *
 * @author xingshuang
 */
public class SocketRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SocketRuntimeException() {
        super();
    }

    public SocketRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketRuntimeException(String message) {
        super(message);
    }

    public SocketRuntimeException(Throwable cause) {
        super(cause);
    }

}
