package com.wenqi.usercenter.common;

/**
 * 返回工具类
 *
 *
 */
public class ResultUtils {
    /**
     * 成功
     *
     * @param data
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(0, data, "OK");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static Result error(ErrorCode errorCode) {
        return new Result<>(errorCode);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static Result error(ErrorCode errorCode, String message, String description) {
        return new Result<>(errorCode.getCode(), null, message, description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param description
     * @return
     */
    public static Result error(ErrorCode errorCode, String description) {
        return new Result<>(errorCode.getCode(), null, errorCode.getMessage(), description);
    }
}
