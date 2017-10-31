package com.lc.springioinit.springinitdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller: index
 *
 * @auther lichi
 * @create 2017-10-21 19:30
 */
@Controller
@RequestMapping(value = "/user")
public class IndexController {

    @RequestMapping(value = "/login_view", method = RequestMethod.GET)
    public String login_view() {
        return "login";
    }

    @RequestMapping(value = "/index2", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
