package com.iit.project.cms.CMSServer.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = CMSExceptionHandler.class)
    @ResponseBody
    public BaseResponse globalExceptionHandler(HttpServletRequest req, CMSExceptionHandler e) {
        logger.error("GlobalException: {}", e.getMessage());
        return BaseResponse.error(e.getErrorCode(), e.getErrorMessage());
    }

}
