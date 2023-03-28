package com.github.xingshuangs.iot.exceptions;

/**
 * RTSP的运行异常
 *
 * @author xingshuang
 */
public class RtspCommException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RtspCommException() {
        super();
    }

    public RtspCommException(String message, Throwable cause) {
        super(message, cause);
    }

    public RtspCommException(String message) {
        super(message);
    }

    public RtspCommException(Throwable cause) {
        super(cause);
    }

}
