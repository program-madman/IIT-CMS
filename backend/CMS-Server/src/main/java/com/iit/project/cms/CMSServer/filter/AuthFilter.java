package com.iit.project.cms.CMSServer.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.common.CustomHttpServletRequestWrapper;
import com.iit.project.cms.CMSServer.entity.User;
import com.iit.project.cms.CMSServer.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

import static com.iit.project.cms.CMSServer.common.ExceptionEnum.AUTH_FAILED;
import static com.iit.project.cms.CMSServer.common.Symbol.*;

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

        //login & register
        if (request.getRequestURI().equals(URL_NO_AUTH_LOGIN) ||
                request.getRequestURI().equals(URL_NO_AUTH_REGISTER) ||
                request.getRequestURI().startsWith(URL_ACTUATOR)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 判断是否为预检请求（OPTIONS请求）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            // 设置允许的方法、头部和响应缓存时间
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, token");
            response.setHeader("Access-Control-Max-Age", "3600");
            // 设置允许的域名，可以设置为 * 或具体的前端地址，以便支持跨域请求
            response.setHeader("Access-Control-Allow-Origin", "*"); // 请将地址修改为您前端页面的实际地址
            // 返回响应
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        //token
        String tokenInHeader = request.getHeader(KEY_HEADER_TOKEN);

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
        }
        if (tokenInHeader == null) {
            authFailed(response);
            return;
        }


        if (!TokenUtil.isValidJwt(tokenInHeader)) {
            authFailed(response);
            return;
        }

        // 将自定义请求头添加到HttpServletRequest对象中
        User user = extractUserFromToken(tokenInHeader);
        CustomHttpServletRequestWrapper customRequest = new CustomHttpServletRequestWrapper(request);
        customRequest.addCustomHeader(KEY_HEADER_USER_ID, String.valueOf(user.getUserId()));
        filterChain.doFilter(customRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private void authFailed(ServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        BaseResponse error = BaseResponse.error(AUTH_FAILED);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(error.toString().getBytes());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private User extractUserFromToken(String token) {
        DecodedJWT decode = JWT.decode(token);
        String user = decode.getClaim(KEY_USER).asString();
        return new Gson().fromJson(user, User.class);
    }
}
