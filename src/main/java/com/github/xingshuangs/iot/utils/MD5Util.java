package com.github.xingshuangs.iot.utils;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具
 *
 * @author xingshuang
 */
public class MD5Util {

    private MD5Util() {
        // NOOP
    }

    public static String encode(String src) throws NoSuchAlgorithmException {
        return encode(src, StandardCharsets.US_ASCII);
    }

    public static String encode(String src, Charset charsets) throws NoSuchAlgorithmException {
        byte[] bytes = src.getBytes(charsets);
        return encode(bytes);
    }

    public static String encode(byte[] src) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(src);
        return HexUtil.toHexString(bytes, "", false);
    }
}
