package com.lc.springioinit.springinitdemo.config;

import com.lc.springioinit.springinitdemo.interceptor.SessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Session configuration
 *
 * @auther lichi
 * @create 2017-10-21 19:38
 */
@Configuration
public class SessionConiguration extends WebMvcConfigurerAdapter{

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**");
    }
}
