package com.unis.crk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors() //开启跨域
                .and().csrf().disable() // 取消跨站请求伪造防护
                .authorizeRequests()
                .antMatchers("/**").permitAll() // 所有地址都不拦截，开发使用
                .anyRequest() // 任何请求
                .authenticated() // 都需要身份认证
                .and().formLogin()
                .loginProcessingUrl("/login").permitAll()
                .and().logout().logoutUrl("/logout").permitAll();
    }

    /**
     * 重写该方法，添加自定义用户
     * */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("ADMIN");
    }


}
