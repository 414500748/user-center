package com.wenqi.usercenter.exception;

import com.wenqi.usercenter.common.ErrorCode;
import com.wenqi.usercenter.common.Result;
import com.wenqi.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //用于统一处理控制器层异常及添加一些通用的前置、后置逻辑的注解
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result businessExceptionHandler(BusinessException e) {
        log.error("business error :" + e.getMessage(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result runtimeExceptionHandler(BusinessException e) {
        log.error("runtime error :", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), e.getDescription());
    }
}
