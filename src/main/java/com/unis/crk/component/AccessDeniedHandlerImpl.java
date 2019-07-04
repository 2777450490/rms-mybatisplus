//package com.ls.server.component;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import ResultUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * 处理没有权限的类
// * @author pengxl
// * @version 1.0
// * 创建时间: 2019/05/20 17:48
// */
//@Component
//public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
//
//    private final static Logger LOGGER = LoggerFactory.getLogger(LogoutSuccessHandlerImpl.class);
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
//        LOGGER.error("用户没有权限");
//        ResultUtil<User> result = new ResultUtil<>();
//        result.noAuth(e.getMessage());
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(objectMapper.writeValueAsString(result));
//    }
//}
