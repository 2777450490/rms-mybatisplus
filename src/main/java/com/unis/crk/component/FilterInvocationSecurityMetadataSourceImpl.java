//package com.ls.server.component;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.ls.server.entity.Resource;
//import com.ls.server.entity.Role;
//import com.ls.server.service.IResourceService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.access.SecurityConfig;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.util.StringUtils;
//
//import java.util.Collection;
//import java.util.List;
//
///**
// * 拦截获取用户请求的URL所需要的角色放到SecurityConfig中
// * @author pengxl
// * @version 1.0
// * 创建时间: 2019/05/21 14:19
// */
//@Component
//public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {
//
//    private final static Logger LOGGER = LoggerFactory.getLogger(FilterInvocationSecurityMetadataSourceImpl.class);
//
//    @Autowired
//    private UncurbedProperties uncurbedProperties;
//
//    @Autowired
//    private IResourceService resourceService;
//
//
//    AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//    @Override
//    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
//        // FIXME: 2019/5/22 得到用户的请求地址 URL地址待处理
//        String requestUrl = ((FilterInvocation) o).getRequestUrl();
//        LOGGER.info("用户请求的地址是:{}",requestUrl);
//        // 如果是登录页面就不需要权限
//        if (uncurbedProperties.getUrl().equals(requestUrl)) {
//            return null;
//        }
//        List<Resource> resources = resourceService.list(new QueryWrapper<Resource>().ne("is_delete",1));
//        for (Resource menu : resources) {
//            if (antPathMatcher.match(menu.getResourceUrl(), requestUrl)) {
//                // 强制加载懒加载数据
////                Hibernate.initialize(menu.getRoles());
//                List<Role> roles = menu.getRoles();
//                int size = roles.size();
//                String[] values = new String[size];
//                for (int i = 0; i < size; i++) {
//                    values[i] = roles.get(i).getId();
//                }
//                LOGGER.info("用户所拥有的角色ID:{}", StringUtils.arrayToCommaDelimitedString(values));
//                return SecurityConfig.createList(values);
//            }
//        }
//        //没有匹配上的资源，都是登录访问
//        return SecurityConfig.createList(uncurbedProperties.getRole());
//    }
//
//    @Override
//    public Collection<ConfigAttribute> getAllConfigAttributes() {
//        return null;
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return FilterInvocation.class.isAssignableFrom(aClass);
//    }
//}
