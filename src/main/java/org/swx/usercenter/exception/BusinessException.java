package org.swx.usercenter.exception;

import org.swx.usercenter.common.ErrorCode;

/**
 * 自定义异常类
 *给原本的RuntimeException封装了两个字段
 * 自定义构造函数，更加灵活
 * @author swx
 */

public class BusinessException extends RuntimeException {
    private  final int code;

    private final String detail;

    public BusinessException(String message, int code, String detail) {
        super(message);
        this.code = code;
        this.detail = detail;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.detail = errorCode.getDetail();
    }

    public BusinessException(ErrorCode errorCode, String detail) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.detail = detail;
    }

    public int getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }
}
