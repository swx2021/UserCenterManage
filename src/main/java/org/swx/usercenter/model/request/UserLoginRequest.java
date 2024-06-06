package org.swx.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录请求体
 * @author swx
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userAccount;
    private String userPassword;
}
