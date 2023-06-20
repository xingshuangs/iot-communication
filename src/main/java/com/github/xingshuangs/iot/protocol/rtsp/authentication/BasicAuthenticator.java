package com.github.xingshuangs.iot.protocol.rtsp.authentication;


import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Basic64加密方式
 *
 * @author xingshuang
 */
public class BasicAuthenticator extends AbstractAuthenticator {

    public static final String NAME = "Basic";

    private final Base64.Encoder encoder;

    public BasicAuthenticator(UsernamePasswordCredential credential) {
        this.credential = credential;
        this.name = NAME;
        this.encoder = Base64.getEncoder();
    }

    public String createResponse() {
        String src = String.format("%s:%s", this.credential.getUsername(), this.credential.getPassword());
        return this.name + " " + this.encoder.encodeToString(src.getBytes(StandardCharsets.UTF_8));
    }
}
