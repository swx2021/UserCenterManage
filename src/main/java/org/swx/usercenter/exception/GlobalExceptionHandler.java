package org.swx.usercenter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.swx.usercenter.common.BaseResponse;
import org.swx.usercenter.common.ErrorCode;
import org.swx.usercenter.common.ResponseUtils;

/**
 * 全局异常处理器
 * @author swx
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException:"+e.getMessage(),e);
        return ResponseUtils.errot(e.getCode(),e.getMessage(),e.getDetail());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResponseUtils.errot(ErrorCode.SYSTEM_INTERNAL_ERROR,e.getMessage(),"");
    }
}
