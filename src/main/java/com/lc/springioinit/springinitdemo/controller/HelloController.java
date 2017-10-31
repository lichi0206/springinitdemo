package com.lc.springioinit.springinitdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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

    // LogBack
    private final static Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, Map<String, Object> map) {
        if(request.getSession().getAttribute("_session_user") == null)
            map.put("name", "No User Login!");
        else
            map.put("name", request.getSession().getAttribute("_session_user"));

        LOGGER.debug("记录了Debug方法");
        LOGGER.info("记录了index方法");
        LOGGER.error("记录了Error方法");

        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello world!";
    }
}
