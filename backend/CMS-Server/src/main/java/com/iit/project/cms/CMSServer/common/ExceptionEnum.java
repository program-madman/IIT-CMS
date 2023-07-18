package com.iit.project.cms.CMSServer.common;

public enum ExceptionEnum implements IBaseError {
    SUCCESS("200", "Success!"),
    AUTH_FAILED("1000", "Auth Failed!"),
    ACCOUNT_PASSWORD_REPEAT_DIFF("2000", "password different from password repeat"),
    ACCOUNT_SAVE_USER_ERROR("2001", "save user error"),
    ACCOUNT_USER_NAME_EXIST("2002", "username exist."),
    ACCOUNT_LOGIN_FAILED("2003", "login failed.");

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
