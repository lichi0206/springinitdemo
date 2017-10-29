package com.lc.springioinit.springinitdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.lc.springioinit.springinitdemo.jpa.UserJPA;
import com.lc.springioinit.springinitdemo.model.User;
import com.lc.springioinit.springinitdemo.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controller: User
 *
 * @auther lichi
 * @create 2017-10-21 0:07
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserJPA userJPA;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<User> list(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        obj.put("msg", "查询成功");
        request.setAttribute(LoggerUtils.LOGGER_RETURN, obj);
        return userJPA.findAll();
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public User save(User user) {
        return userJPA.save(user);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public List<User> delete(Long id) {
        userJPA.delete(id);
        return userJPA.findAll();
    }
}
