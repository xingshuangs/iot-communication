package com.github.xingshuangs.iot.protocol.rtsp.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 状态码
 *
 * @author xingshuang
 */
public enum ERtspStatusCode {
    CONTINUE(100, "Continue"),

    OK(200, "OK"),
    CREATED(201, "Created"),
    LOW_ON_STORAGE_SPACE(250, "Low on Storage Space"),

    MULTIPLE_CHOICES(300, "Multiple Choices"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    MOVED_TEMPORARILY(302, "Moved Temporarily"),
    SEE_OTHER(303, "See Other"),
    NOT_MODIFIED(304, "Not Modified"),
    USE_PROXY(305, "Use Proxy"),

    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408, "Request Time-out"),
    GONE(410, "Gone"),
    LENGTH_REQUIRED(411, "Length Required"),
    PRECONDITION_FAILED(412, "Precondition Failed"),
    REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
    REQUEST_URI_TOO_LARGE(414, "Request-URI Too Large"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    PARAMETER_NOT_UNDERSTOOD(451, "Parameter Not Understood"),
    CONFERENCE_NOT_FOUND(452, "Conference Not Found"),
    NOT_ENOUGH_BANDWIDTH(453, "Not Enough Bandwidth"),
    SESSION_NOT_FOUND(454, "Session Not Found"),
    METHOD_NOT_VALID_IN_THIS_STATE(455, "Method Not Valid in This State"),
    HEADER_FIELD_NOT_VALID_FOR_RESOURCE(456, "Header Field Not Valid for Resource"),
    INVALID_RANGE(457, "Invalid Range"),
    PARAMETER_IS_READ_ONLY(458, "Parameter Is Read-Only"),
    AGGREGATE_OPERATION_NOT_ALLOWED(459, "Aggregate operation not allowed"),
    ONLY_AGGREGATE_OPERATION_ALLOWED(460, "Only aggregate operation allowed"),
    UNSUPPORTED_TRANSPORT(461, "Unsupported transport"),
    DESTINATION_UNREACHABLE(462, "Destination unreachable"),
    DESTINATION_PROHIBITED(463, "Destination prohibited"),
    DATA_TRANSPORT_NOT_READY_YET(464, "Data transport not ready yet"),
    NOTIFICATION_REASON_UNKNOWN(465, "Notification reason unknown"),
    KEY_MANAGEMENT_ERROR(466, "Key management error"),

    CONNECTION_AUTHORIZATION_REQUIRED(470, "Connection authorization required"),
    CONNECTION_CREDENTIALS_NOT_ACCEPT(471, "Connection credentials not accept"),
    FAILURE_TO_ESTABLISH_SECURE_CONNECTION(472, "Failure to establish secure connection"),

    INTERNAL_SERVERERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Time-out"),
    RTSP_VERSION_NOT_SUPPORTED(505, "RTSP Version not supported"),
    OPTION_NOT_SUPPORTED(551, "Option not supported"),

    ;

    private static Map<Integer, ERtspStatusCode> map;

    public static ERtspStatusCode from(Integer data) {
        if (map == null) {
            map = new HashMap<>();
            for (ERtspStatusCode item : ERtspStatusCode.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final Integer code;

    private final String description;

    ERtspStatusCode(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
