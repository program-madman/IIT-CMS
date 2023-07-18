package com.iit.project.cms.CMSServer.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.iit.project.cms.CMSServer.common.BaseResponse;
import com.iit.project.cms.CMSServer.common.HttpServletRequestParameterWrapper;
import com.iit.project.cms.CMSServer.entity.User;
import com.iit.project.cms.CMSServer.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

        //跨域
        cross(response);

        //login
        if (request.getRequestURI().equals(URL_NO_AUTH_LOGIN) ||
                request.getRequestURI().equals(URL_NO_AUTH_REGISTER)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //token
        HttpServletRequestParameterWrapper requestParameterWrapper = new HttpServletRequestParameterWrapper(request);
        String tokenInHeader = requestParameterWrapper.getHeader(KEY_HEADER_TOKEN);
        if (tokenInHeader == null) {
            authFailed(response);
            return;
        }
        if (!TokenUtil.isValidJwt(tokenInHeader)) {
            authFailed(response);
            return;
        }

        // update custom header and dispatch
        User user = extractUserFromToken(tokenInHeader);
        requestParameterWrapper.setAttribute(KEY_HEADER_USER_ID, user.getUserId());
        filterChain.doFilter(requestParameterWrapper, servletResponse);
    }

    private static void cross(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST， GET， DELETE， OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
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
        log.info("user : " + user);
        return new Gson().fromJson(user, User.class);
    }
}
