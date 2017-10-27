package com.lc.springioinit.springinitdemo.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Fast JSON configuration
 *
 * @auther lichi
 * @create 2017-10-21 18:37
 */
@Configuration
public class FastJsonConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Call configuration of the parent
        super.configureMessageConverters(converters);
        // Create FastJSON message converter
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        // Create FastJSON configurator
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // Modify Configuration: Return context filtering
        fastJsonConfig.setSerializerFeatures(
                // Eliminating the problem of circular references to the same object, default is false.
                // If not configured, it may go into the dead cycle
                SerializerFeature.DisableCircularReferenceDetect,
                // WriteMapNullValue: whether output the fields which the value are null(not empty, is NULL!), default is false.
                SerializerFeature.WriteMapNullValue,
                // WriteNullStringAsEmpty: If String field is null, will output "", but not null
                SerializerFeature.WriteNullStringAsEmpty
                // WriteNullListAsEmpty: If List field is null, will output [], but not null
                // WriteNullBooleanAsEmpty: If boolean field is null, will output false, but not null
        );
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        // Add the fastJSON converter to HttpMessageConverters list
        converters.add(fastJsonHttpMessageConverter);
    }
}
