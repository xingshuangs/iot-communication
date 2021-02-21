/*
  Copyright (C), 2009-2021, 江苏汇博机器人技术股份有限公司
  FileName: HexException
  Author:   ShuangPC
  Date:     2021/2/18
  Description:
  History:
  <author>         <time>          <version>          <desc>
  作者姓名         修改时间           版本号             描述
 */

package com.github.xingshuangs.iot.exceptions;

/**
 * 16进制解析异常
 *
 * @author ShuangPC
 */

public class HexParseException extends RuntimeException {

    public HexParseException() {
        super();
    }

    public HexParseException(String message) {
        super(message);
    }

    public HexParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public HexParseException(Throwable cause) {
        super(cause);
    }

    protected HexParseException(String message, Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
