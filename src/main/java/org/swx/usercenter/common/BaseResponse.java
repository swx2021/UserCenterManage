package org.swx.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @param <T>
 * 成功200
 * @author swx
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;

    private T data;

    private String msg;

    private String detail;

    /**
     *
     * 操作成功时，返回对象
     */
    public BaseResponse(int code, T data, String msg,String detail) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.detail = detail;
    }

    public BaseResponse(int code, T data,String message) {
        this(code, data, message,null);
    }

    public BaseResponse(int code, T data) {
        this(code, data, null,null);
    }

    /**
     * 操作失败时返回对象
     * @param errorCode
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null,errorCode.getMsg(),errorCode.getDetail());
    }
}
