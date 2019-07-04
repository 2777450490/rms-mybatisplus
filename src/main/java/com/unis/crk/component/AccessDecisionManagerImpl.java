package com.unis.crk.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * 判断当前用户是否有访问当前url权限
 * @author pengxl
 * @version 1.0
 * 创建时间: 2019/05/22 14:01
 */
@Component
public class AccessDecisionManagerImpl implements AccessDecisionManager {

    @Autowired
    private UncurbedProperties uncurbedProperties;

    @Override
    public void decide(Authentication authentication, Object o,
                       Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        // 迭代器遍历目标url的权限列表
        Iterator<ConfigAttribute> iterator = collection.iterator();
        while (iterator.hasNext()) {
            ConfigAttribute ca = iterator.next();
            String needRole = ca.getAttribute();
            if (uncurbedProperties.getRole().equals(needRole)) {
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new BadCredentialsException("未登录");
                }else {
                    return;
                }
            }
            //遍历当前用户所具有的权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        //执行到这里说明没有匹配到应有权限
        throw new AccessDeniedException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
