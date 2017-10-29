package com.lc.springioinit.springinitdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @auther lichi
 * @create 2017-10-29 22:15
 */
@Configuration
public class StaticPathConfiguration extends WebMvcConfigurerAdapter{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("lc/resources/**").addResourceLocations("classpath:/static/");
    }
}
