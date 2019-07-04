//package com.ls.server.component;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.service.IService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//
//import java.util.Date;
//
///**
// * 初始化admin用户
// * @author pengxl
// * @version 1.0
// * 创建时间: 2019/05/20 15:29
// */
//@Component
//public class InitUserComponent implements ApplicationRunner {
//
//    @Autowired
//    private IService<User> userService;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        User user = this.userService.getOne(new QueryWrapper<User>().eq("login_name","admin"));
//        if (!ObjectUtils.isEmpty(user)){
//            return;
//        }
//        user = new User();
//        user.setLoginName("admin");
//        user.setPwd("123456");
//        user.setName("admin");
//        user.setSex(1);
//        user.setBirthday(new Date());
//        this.userService.save(user);
//    }
//}
