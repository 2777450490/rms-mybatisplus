//package com.ls.server.component;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ls.server.entity.User;
//import ResultUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * 处理登录验证成功的类
// * @author pengxl
// * @version 1.0
// * 创建时间: 2019/05/20 17:32
// */
//@Component
//public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
//
//    private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessHandlerImpl.class);
//
//    /**Json转化工具*/
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//        User user = (User) authentication.getPrincipal();
//        LOGGER.info("登录用户为:{}",user.getUsername());
//        ResultUtil<User> result = new ResultUtil<>();
//        result.success("登录成功",user);
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(objectMapper.writeValueAsString(result));
//    }
//}
