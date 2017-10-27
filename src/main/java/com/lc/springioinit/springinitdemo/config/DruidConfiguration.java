package com.lc.springioinit.springinitdemo.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid Configuration
 *
 * @auther lichi
 * @create 2017-10-21 17:58
 */
@Configuration
public class DruidConfiguration {


    /**
     * Config servlet
     *
     * @return servlet registration Bean
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        // Create servlet registration bean
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // If "allow" and "deny" exist simultaneously, "deny" takes precedence over "allow"
        // IP white list
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        // IP black list
        servletRegistrationBean.addInitParameter("deny", "192.168.0.19");
        // Admin user(console)
        servletRegistrationBean.addInitParameter("loginUsername", "druid");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        // Whether have the permission to reset data
        servletRegistrationBean.addInitParameter("resetEnable", "false");

        return servletRegistrationBean;
    }

    /**
     * Config filter
     *
     * @return filter registration Bean
     */
    @Bean
    public FilterRegistrationBean statFilter() {
        // Create filter registration bean
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // THE URL of the filter
        filterRegistrationBean.addUrlPatterns("/*");
        // Ignore file types
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");

        return filterRegistrationBean;
    }
}
