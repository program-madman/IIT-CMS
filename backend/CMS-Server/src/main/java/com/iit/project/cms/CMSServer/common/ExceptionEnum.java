package com.iit.project.cms.CMSServer.common;

public enum ExceptionEnum implements IBaseError {
    SUCCESS("200", "Success!"),
    AUTH_FAILED("1000", "Auth Failed!");

    private final String resultCode;
    private final String resultMessage;

    ExceptionEnum(String code, String message) {
        this.resultCode = code;
        this.resultMessage = message;
    }

    @Override
    public String getResultCode() {
        return this.resultCode;
    }

    @Override
    public String getResultMessage() {
        return this.resultMessage;
    }
}
