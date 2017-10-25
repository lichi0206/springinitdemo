package com.lc.springioinit.springinitdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller: Hello
 *
 * @auther lichi
 * @create 2017-10-20 21:25
 */

@Controller
//@RestController
public class HelloController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Map<String, Object> map) {
        map.put("name", "Hello Controller");
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello world!";
    }
}
