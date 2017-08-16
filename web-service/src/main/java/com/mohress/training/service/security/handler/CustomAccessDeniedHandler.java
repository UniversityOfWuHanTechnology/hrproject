package com.mohress.training.service.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问拒绝处理器
 *
 * Created by youtao.wan on 2017/8/16.
 */
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private AuthorizationFailHandler handler = new AuthorizationFailHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        handler.handle(request, response, e);
    }
}