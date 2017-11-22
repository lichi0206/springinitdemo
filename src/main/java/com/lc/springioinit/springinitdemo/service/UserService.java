package com.lc.springioinit.springinitdemo.service;

import com.lc.springioinit.springinitdemo.jpa.UserJPA;
import com.lc.springioinit.springinitdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * user service
 *
 * @auther lichi
 * @create 2017-11-22 21:57
 */
@Service
@CacheConfig(cacheNames = "user")
public class UserService {

    @Autowired
    private UserJPA userJPA;

    @Cacheable
    public List<User> list() {
        return userJPA.findAll();
    }
}
