package com.wenqi.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类型
 *
 *
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    private String description;

    public Result(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public Result(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Result(int code, T data) {
        this(code, data, "", "");
    }

    public Result(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
