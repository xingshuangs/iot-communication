package com.github.xingshuangs.iot.protocol.rtsp.authentication;


/**
 * 认证器
 *
 * @author xingshuang
 */
public abstract class AbstractAuthenticator {

    protected String name;

    protected UsernamePasswordCredential credential;

    public abstract String createResponse();
}
