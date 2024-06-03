package org.swx.usercenter.common;

/**
 * 返回工具类
 * 由此类返回统一返回对象
 * @author swx
 */

public class ResponseUtils {
    /**
     * 成功
     * @param data 返回成功后的数据
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200,data,"ok","操作成功");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     * @param
     */
    public static BaseResponse errot(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static BaseResponse errot(ErrorCode errorCode,String message,String detail) {
        return new BaseResponse(errorCode.getCode(),null,message,detail);
    }

    public static BaseResponse errot(ErrorCode errorCode,String detail) {
        return new BaseResponse(errorCode.getCode(),null,errorCode.getMsg(),detail);
    }

    public static BaseResponse errot(int code,String message,String detail) {
        return new BaseResponse(code,null,message,detail);
    }

}
