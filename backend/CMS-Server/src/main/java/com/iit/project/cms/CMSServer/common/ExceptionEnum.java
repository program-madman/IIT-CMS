package com.iit.project.cms.CMSServer.common;

public enum ExceptionEnum implements IBaseError {
    SUCCESS("200", "Success!"),
    AUTH_FAILED("1000", "Auth Failed!"),
    ACCOUNT_PASSWORD_REPEAT_DIFF("2000", "Password different from password repeat"),
    ACCOUNT_SAVE_USER_ERROR("2001", "Save user error"),
    ACCOUNT_USER_NAME_EXIST("2002", "Username exist."),
    ACCOUNT_LOGIN_FAILED("2003", "Login failed."),
    CREATE_ARTICLE_FAILED("3000", "Create article failed."),
    CREATE_COMMENT_FAILED("3001", "Create comment failed.");

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
