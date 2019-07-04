package com.unis.crk.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取配置文件属性
 * @author pengxl
 * @version 1.0
 * 创建时间: 2019/05/21 14:30
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix="project.uncurbed")
public class UncurbedProperties {
    /**
     * 配置未被权限控制的URL
     */
    private String url;

    /**
     * 配置未被权限控制的角色
     */
    private String role;

}
