package com.github.xingshuangs.iot.protocol.rtsp.authentication;

import com.github.xingshuangs.iot.utils.HexUtil;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.Assert.*;


public class BasicAuthenticatorTest {

    @Test
    public void createResponse() {
        String expect = "Basic YWRtaW46MTIzNDU2";
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "123456");
        BasicAuthenticator authenticator = new BasicAuthenticator(credential);
        String actual = authenticator.createResponse();
        assertEquals(expect, actual);
    }

    @Test
    public void base64() {
        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();
        String expect = "admin:123456";
        byte[] encode = encoder.encode(expect.getBytes(StandardCharsets.ISO_8859_1));
        byte[] decode = decoder.decode(encode);
        String actual = new String(decode, StandardCharsets.ISO_8859_1);
        assertEquals(expect, actual);

        // fmtp:96 profile-level-id=420029; packetization-mode=1; sprop-parameter-sets=Z00AH5Y1QKALdNwEBAQI,aO48gA==
        String src = "Z00AH5Y1QKALdNwEBAQI";
        byte[] sps = decoder.decode(src);
        String spsStr = HexUtil.toHexString(sps);
//        System.out.println(spsStr);

        src = "aO48gA==";
        byte[] pps = decoder.decode(src);
        String ppsStr = HexUtil.toHexString(pps);
//        System.out.println(ppsStr);
    }
}