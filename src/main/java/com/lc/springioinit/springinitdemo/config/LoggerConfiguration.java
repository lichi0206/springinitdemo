package com.lc.springioinit.springinitdemo.config;

import com.lc.springioinit.springinitdemo.interceptor.LoggerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @auther lichi
 * @create 2017-10-29 21:36
 */
@Configuration
public class LoggerConfiguration extends WebMvcConfigurerAdapter{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor()).addPathPatterns("/**");
    }
}
