package com.wang.confing;

import com.wang.interceptors.Logininterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 WebMvcConfigurer 是 Spring MVC 提供的一个配置接口，允许你在不覆盖默认配置的前提下，灵活添加或修改 MVC 功能（如拦截器、静态资源路径、跨域支持等）。
 类比理解：
 默认配置是 “毛坯房”，实现 WebMvcConfigurer 相当于 “装修”：
 保留原有结构（默认配置），按需添加家具（拦截器）、调整门窗（跨域规则）、规划储物空间（静态资源路径）。
 若用 @EnableWebMvc 则相当于 “推到重建”，需完全自己设计整套房。
 核心方法：
 addInterceptors()：添加拦截器（如权限校验）。
 addResourceHandlers()：配置静态资源路径（如 /static/**）。
 addCorsMappings()：设置跨域规则。
 addArgumentResolvers()：自定义参数解析逻辑。
 * **/
@Configuration
public class WebConfing  implements WebMvcConfigurer {
    @Autowired
    private Logininterceptors logininterceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logininterceptors).excludePathPatterns("/user/login", "/user/register");


    }
}
