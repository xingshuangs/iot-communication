package com.github.xingshuangs.iot.exceptions;

/**
 * RTCP的运行异常
 *
 * @author xingshuang
 */
public class RtpCommException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RtpCommException() {
        super();
    }

    public RtpCommException(String message, Throwable cause) {
        super(message, cause);
    }

    public RtpCommException(String message) {
        super(message);
    }

    public RtpCommException(Throwable cause) {
        super(cause);
    }

}
