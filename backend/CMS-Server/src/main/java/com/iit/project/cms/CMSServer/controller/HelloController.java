package com.iit.project.cms.CMSServer.controller;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.common.ExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class HelloController {
  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  public String sayHello(HttpServletRequest request) {
    log.info("req ip : " + request.getRemoteAddr());
    return "Greeting!  this is IIT  Project demo";
  }


  @RequestMapping(value = "/showerror")
  public BaseResponse showError() {
    return BaseResponse.error(ExceptionEnum.AUTH_FAILED);
  }
}
