package org.swx.usercenter.common;

/**
 * 错误码
 * @author swx
 */
public enum ErrorCode {

    SUCCESS(200,"OK",""),
    PARAMS_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40001,"请求数据为空",""),
    NO_LOGIN(40100,"未登录",""),
    NO_AUTH(40101,"无权限",""),
    NO_REGISTER(40102,"无法注册",""),
    SYSTEM_INTERNAL_ERROR(50000,"系统内部异常","");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态码信息
     */
    private final String msg;

    /**
     * 状态码详情
     */
    private final String detail;

    ErrorCode(int code, String msg, String detail) {
        this.code = code;
        this.msg = msg;
        this.detail = detail;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDetail() {
        return detail;
    }
}
