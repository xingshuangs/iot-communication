package com.github.xingshuangs.iot.protocol.rtsp.authentication;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户密码凭证
 *
 * @author xingshuang
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsernamePasswordCredential {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
