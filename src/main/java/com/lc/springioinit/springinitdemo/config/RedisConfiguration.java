package com.lc.springioinit.springinitdemo.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;

/**
 * Redis configuration
 *
 * @auther lichi
 * @create 2017-11-22 21:26
 */
@Configuration
@EnableCaching
public class RedisConfiguration extends JCacheConfigurerSupport{

    /**
     * 自定义key生成规则
     * @return
     */
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                StringBuffer sb = new StringBuffer();
                // 追加类名
                sb.append(o.getClass().getName());
                // 追加方法名
                sb.append(method.getName());
                // 遍历参数并追加
                for (Object object : objects) {
                    sb.append(object.toString());
                }
                System.out.println("调用Redis缓存key：" + sb.toString());
                return sb.toString();
            }
        };
    }

    /**
     * 采用redisCacheManager作为缓存管理器
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }

}
