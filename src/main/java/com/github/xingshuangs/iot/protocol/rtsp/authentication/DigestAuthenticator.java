package com.github.xingshuangs.iot.protocol.rtsp.authentication;


import com.github.xingshuangs.iot.exceptions.AuthenticationException;
import com.github.xingshuangs.iot.utils.MD5Util;
import lombok.Getter;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Digest认证
 *
 * @author xingshuang
 */
@Getter
public class DigestAuthenticator extends AbstractAuthenticator {

    private static final String DIGEST_NAME = "Digest";

    /**
     * 表示Web服务器中受保护文档的安全域（比如公司财务信息域和公司员工信息域），用来指示需要哪个域的用户名和密码
     */
    private String realm = "";

    /**
     * 保护质量，包含auth（默认的）和auth-int（增加了报文完整性检测）两种策略，（可以为空，但是）不推荐为空值
     */
    private String qop = "";

    /**
     * 服务端向客户端发送质询时附带的一个随机数，这个数会经常发生变化。客户端计算密码摘要时将其附加上去，
     * 使得多次生成同一用户的密码摘要各不相同，用来防止重放攻击
     */
    private String nonce = "";

    /**
     * nonce计数器，是一个16进制的数值，表示同一nonce下客户端发送出请求的数量。例如，在响应的第一个请求中，客户端将发送“nc=00000001”。
     * 这个指示值的目的是让服务器保持这个计数器的一个副本，以便检测重复的请求
     */
    private int nc = 0;

    /**
     * 客户端随机数，这是一个不透明的字符串值，由客户端提供，并且客户端和服务器都会使用，以避免用明文文本。
     * 这使得双方都可以查验对方的身份，并对消息的完整性提供一些保护
     */
    private String cnonce = "";

    /**
     * 当密码摘要使用的随机数过期时，服务器可以返回一个附带有新随机数的401响应，
     * 并指定stale=true，表示服务器在告知客户端用新的随机数来重试，而不再要求用户重新输入用户名和密码了
     */
    private boolean stale = false;

    /**
     * 访问地址
     */
    private String uri = "";

    /**
     * 对应方法
     */
    private String method = "";

    private byte[] entityBody = new byte[0];

    public DigestAuthenticator(UsernamePasswordCredential credential) {
        this.credential = credential;
        this.name = DIGEST_NAME;
    }


    public String createResponse() {
        if (this.realm == null || this.realm.equals("")) {
            throw new AuthenticationException("realm为空");
        }
        if (this.nonce == null || this.nonce.equals("")) {
            throw new AuthenticationException("nonce为空");
        }
        if (this.uri == null || this.uri.equals("")) {
            throw new AuthenticationException("uri为空");
        }
        if (this.method == null || this.method.equals("")) {
            throw new AuthenticationException("method为空");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(DIGEST_NAME).append(" ")
                .append("username=\"").append(this.credential.getUsername()).append("\", ")
                .append("realm=\"").append(this.realm).append("\", ")
                .append("nonce=\"").append(this.nonce).append("\", ")
                .append("uri=\"").append(this.uri).append("\", ");
        if (this.qop == null || this.qop.equals("")) {
            sb.append("response=\"").append(this.calculateResponseString()).append("\"");
        } else {
            sb.append("response=\"").append(this.calculateResponseString()).append("\", ")
                    .append("qop=").append(this.qop).append(", ")
                    .append("nc=").append(String.format("%08X", this.nc)).append(", ")
                    .append("cnonce=\"").append(this.cnonce).append("\"");
        }
        return sb.toString();
    }

    /**
     * 1）nonce 由后台生成传给浏览器的，浏览器会在 Authorization 请求头中带回；
     * 2）Authorization 请求头中nc的含义：nonce计数器，是一个16进制的数值，表示同一nonce下客户端发送出请求的数量，用来防重复攻击；
     * 3）生成response的算法：response = MD5(MD5(username:realm:password):nonce:nc:cnonce:qop:MD5(<request-method>:url))
     * 　- 浏览器不发送 password 的值，而是发送 response，可以防止密码在传输过程中被窃取；
     * ---
     * Digest算法
     * A1 = username:realm:password
     * A2 = mthod:uri
     * ---
     * HA1 = MD5(A1)
     * 如果 qop 值为“auth”或未指定，那么 HA2 为
     * HA2 = MD5(A2)=MD5(method:uri)
     * 如果 qop 值为“auth-int”，那么 HA2 为
     * HA2 = MD5(A2)=MD5(method:uri:MD5(entityBody))
     * ----
     * 如果 qop 值为“auth”或“auth-int”，那么如下计算 response：
     * response = MD5(HA1:nonce:nc:cnonce:qop:HA2)
     * ---
     * 如果 qop 未指定，那么如下计算 response：
     * response = MD5(HA1:nonce:HA2)
     * 上面的算法，是不是把你绕晕了，下面，用实例介绍一下，便于你的理解
     *
     * @return 字符串
     */
    private String calculateResponseString() {
        try {
            this.nc++;
            String a1 = String.format("%s:%s:%s", this.credential.getUsername(), this.realm, this.credential.getPassword());
            String ha1 = MD5Util.encode(a1);

            String a2 = String.format("%s:%s", this.method, this.uri);
            if ("auth-int".equals(this.qop)) {
                a2 += String.format(":%s", MD5Util.encode(this.entityBody));
            }
            String ha2 = MD5Util.encode(a2);

            if (this.qop == null || this.qop.equals("")) {
                String response = String.format("%s:%s:%s", ha1, this.nonce, ha2);
                return MD5Util.encode(response);
            } else {
                String ncStr = String.format("%08X", this.nc);
                String response = String.format("%s:%s:%s:%s:%s:%s", ha1, this.nonce, ncStr, this.cnonce, this.qop, ha2);
                return MD5Util.encode(response);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new AuthenticationException(e);
        }
    }

    /**
     * 添加服务端的信息
     *
     * @param realm 表示Web服务器中受保护文档的安全域（比如公司财务信息域和公司员工信息域），用来指示需要哪个域的用户名和密码
     * @param qop   保护质量，包含auth（默认的）和auth-int（增加了报文完整性检测）两种策略，（可以为空，但是）不推荐为空值
     * @param nonce 服务端向客户端发送质询时附带的一个随机数，这个数会经常发生变化
     * @param stale 当密码摘要使用的随机数过期时，服务器可以返回一个附带有新随机数的401响应，并指定stale=true，表示服务器在告知客户端用新的随机数来重试，而不再要求用户重新输入用户名和密码了
     */
    public void addServerInfo(String realm, String qop, String nonce, boolean stale) {
        this.realm = realm;
        this.qop = qop;
        this.nonce = nonce;
        this.stale = stale;
        this.nc = 0;
    }

    /**
     * 根据字符串添加服务端反馈的信息
     *
     * @param src 服务端反馈的信息
     */
    public void addServerInfoByString(String src) {
        // Digest realm="IP Camera(10789)", nonce="6b9a455aec675b8db81a9ceb802e4eb8", stale="FALSE"
        // Digest realm="testrealm@host.com", qop="auth", nonce="dcd98b7102dd2f0e8b11d0f600bfb0c093"
        int i = src.indexOf(" ");
        if (i < 0) {
            throw new AuthenticationException("传入的服务端Digest数据格式错误");
        }
        Map<String, String> map = new HashMap<>();
        String content = src.substring(i + 1);
        String[] contentSplit = content.split(",");
        for (String item : contentSplit) {
            int index = item.indexOf("=");
            if (index >= 0) {
                map.put(item.substring(0, index).trim(), item.substring(index + 1).replace("\"", "").trim());
            }
        }

        this.realm = map.getOrDefault("realm", "").trim();
        this.qop = map.getOrDefault("qop", "").trim();
        this.nonce = map.getOrDefault("nonce", "").trim();
        this.stale = map.containsKey("stale") && map.get("stale").equals("FALSE");
        this.nc = 0;
    }

    /**
     * 添加客户端信息
     *
     * @param uri    地址
     * @param method 方法
     */
    public void addClientInfo(String uri, String method) {
        String randomStr = UUID.randomUUID().toString().replace("-", "");
        this.addClientInfo(uri, method, randomStr, new byte[0]);
    }

    /**
     * 添加客户端信息
     *
     * @param uri    地址
     * @param method 方法
     * @param cnonce 客户端的随机数
     */
    public void addClientInfo(String uri, String method, String cnonce) {
        this.addClientInfo(uri, method, cnonce, new byte[0]);
    }

    /**
     * 添加客户端信息
     *
     * @param uri        地址
     * @param method     方法
     * @param cnonce     客户端的随机数
     * @param entityBody 实体内容
     */
    public void addClientInfo(String uri, String method, String cnonce, byte[] entityBody) {
        this.uri = uri;
        this.cnonce = cnonce;
        this.method = method;
        this.entityBody = entityBody;
    }


}
