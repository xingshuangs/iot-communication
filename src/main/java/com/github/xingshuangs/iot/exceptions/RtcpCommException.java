package com.github.xingshuangs.iot.exceptions;

/**
 * RTCP的运行异常
 *
 * @author xingshuang
 */
public class RtcpCommException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RtcpCommException() {
        super();
    }

    public RtcpCommException(String message, Throwable cause) {
        super(message, cause);
    }

    public RtcpCommException(String message) {
        super(message);
    }

    public RtcpCommException(Throwable cause) {
        super(cause);
    }

}
