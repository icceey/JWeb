package com.icceey.jweb.beans;

import com.icceey.jweb.constants.ResponseStatus;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class BaseResponse {
    private Integer code;
    private String message;
    private Map<String, Object> data;

    private BaseResponse(){}

    private BaseResponse(int code, String message){
        this.code = code;
        this.message = message;
        this.data = new HashMap<>();
    }

    public BaseResponse put(String str, Object obj) {
        data.put(str, obj);
        return this;
    }

    public static BaseResponse success() {
        return BaseResponse.success("处理成功");
    }

    public static BaseResponse success(String msg) {
        return new BaseResponse(ResponseStatus.SUCCESS, msg);
    }

    public static BaseResponse fail() {
        return BaseResponse.fail("处理失败");
    }

    public static BaseResponse fail(String msg) {
        return new BaseResponse(ResponseStatus.FAIL, msg);
    }

    public static BaseResponse sessionExpires() {
        return new BaseResponse(ResponseStatus.SESSION_EXPIRE, "session expires");
    }

    public static BaseResponse exception() {
        return BaseResponse.exception("服务器异常");
    }

    public static BaseResponse exception(String msg) {
        return new BaseResponse(ResponseStatus.EXCEPTION, msg);
    }



}
