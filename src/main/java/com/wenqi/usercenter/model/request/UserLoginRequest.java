package com.wenqi.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 8120898600126286691L;

    private String userAccount;

    private String userPassword;
}
