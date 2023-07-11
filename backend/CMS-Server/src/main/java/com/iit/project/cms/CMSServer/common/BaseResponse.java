package com.iit.project.cms.CMSServer.common;


import com.google.gson.Gson;

public class BaseResponse {
    private String code;

    private String message;

    private Object data;

    public BaseResponse() {
    }

    public BaseResponse(IBaseError errorInfo) {
        this.code = errorInfo.getResultCode();
        this.message = errorInfo.getResultMessage();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static BaseResponse success() {
        return success(null);
    }

    public static BaseResponse success(Object data) {
        BaseResponse rb = new BaseResponse();
        rb.setCode(ExceptionEnum.SUCCESS.getResultCode());
        rb.setMessage(ExceptionEnum.SUCCESS.getResultMessage());
        rb.setData(data);
        return rb;
    }

    public static BaseResponse error(IBaseError errorInfo) {
        BaseResponse rb = new BaseResponse();
        rb.setCode(errorInfo.getResultCode());
        rb.setMessage(errorInfo.getResultMessage());
        rb.setData(null);
        return rb;
    }

    public static BaseResponse error(String code, String message) {
        BaseResponse rb = new BaseResponse();
        rb.setCode(code);
        rb.setMessage(message);
        rb.setData(null);
        return rb;
    }

    public static BaseResponse error(String message) {
        BaseResponse rb = new BaseResponse();
        rb.setCode("-1");
        rb.setMessage(message);
        rb.setData(null);
        return rb;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
