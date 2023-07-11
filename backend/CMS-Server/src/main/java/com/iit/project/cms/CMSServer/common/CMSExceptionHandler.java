package com.iit.project.cms.CMSServer.common;

public class CMSExceptionHandler extends RuntimeException {
    private static final long serialVersionID = 1L;
    protected String errorCode;
    protected String errorMsg;

    public CMSExceptionHandler() {
        super();
    }

    public CMSExceptionHandler(IBaseError error) {
        super(error.getResultCode());
        this.errorCode = error.getResultCode();
        this.errorMsg = error.getResultMessage();
    }

    public CMSExceptionHandler(IBaseError error, Throwable cause) {
        super(error.getResultCode(), cause);
        this.errorCode = error.getResultCode();
        this.errorMsg = error.getResultMessage();
    }

    public CMSExceptionHandler(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public CMSExceptionHandler(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public CMSExceptionHandler(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
