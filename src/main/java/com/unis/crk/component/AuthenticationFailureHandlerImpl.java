//package com.ls.server.component;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import ResultUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * 处理登录验证失败的类
// * @author pengxl
// * @version 1.0
// * 创建时间: 2019/05/20 17:42
// */
//@Component
//public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
//
//    private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationFailureHandlerImpl.class);
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        AuthenticationException exception) throws IOException, ServletException {
//        LOGGER.error("登录验证失败");
//        ResultUtil<User> result = new ResultUtil<>();
//        result.failed(exception.getMessage());
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(objectMapper.writeValueAsString(result));
//    }
//}
