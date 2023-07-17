package com.iit.project.cms.CMSServer.filter;

import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.common.CMSExceptionHandler;
import com.iit.project.cms.CMSServer.common.HttpServletRequestParameterWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST， GET， DELETE， OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

        HttpServletRequestParameterWrapper requestParameterWrapper = new HttpServletRequestParameterWrapper(request);
        requestParameterWrapper.addParameter("login_ip", request.getRemoteAddr());


        String requestURI = requestParameterWrapper.getRequestURI();
        log.info("RequestURI : " + requestURI);
        String id = requestParameterWrapper.getParameter("id");
        log.info("User ID : " + id);
        String token = requestParameterWrapper.getParameter("token");
        if (token == null) {
            reject(response, "-1111", "token is null");
        } else {
            filterChain.doFilter(requestParameterWrapper, servletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private void reject(ServletResponse response, String code, String message) {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        BaseResponse error = BaseResponse.error(code, message);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(error.toString().getBytes());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
