package com.lc.springioinit.springinitdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.lc.springioinit.springinitdemo.jpa.UserJPA;
import com.lc.springioinit.springinitdemo.model.User;
import com.lc.springioinit.springinitdemo.service.UserService;
import com.lc.springioinit.springinitdemo.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    ///////////////////////////////////////////////////////////////////////////////////
    // 使用Redis缓存读取所有用户
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/listByCache")
    public List<User> listByCache() {
        return userService.list();
    }
    ///////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////
    // JPA默认CRUD
    /**
     * 添加查询记录，并使用JPA方法查询所有用户
     * @param request
     * @return
     */
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

    @RequestMapping(value = "/delete", produces = "text/plain;charset=utf-8")
    public String delete(Long id) {
        userJPA.delete(id);
        return "delete user: " + "id" + "successful";
    }

    @RequestMapping(value = "/add", produces = "text/plain;charset=utf-8")
    public String add() {
        User user = new User();
        user.setAge(20);
        user.setEmail("user@qq.com");
        user.setPassword("1212");
        user.setUserId(1212);
        user.setUserName("1212");
        user.setUserNo("1212");
        user.setValid("1");
        userJPA.save(user);
        return "用户信息添加成功";
    }
    ///////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////////////
    // 自定义查询于分页查询
    @RequestMapping(value = "/age")
    public List<User> age() {
        return userJPA.nativeQuery(20);
    }

    /**
     * 自定义查询
     *
     * @return
     */
    @RequestMapping(value = "/deleteWhere")
    public String deleteWhere() {
        userJPA.deleteQuery("1212", "1212");
        return "Delete user: " + "1212" + "successful";
    }

    /**
     * 分页查询
     * 排序+分页
     * @param page
     * @return
     */
    @RequestMapping(value = "/cutPage")
    public List<User> cutPage(int page) {
        User user = new User();
        user.setSize(2);
        user.setPage(page);
        // 获取排序对象
        Sort.Direction sort_direction = Sort.Direction.ASC.toString().equalsIgnoreCase(user.getSord()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        // 构建排序，参数
        Sort sort = new Sort(sort_direction, user.getSidx());
        // 构建分页对象
        PageRequest pageRequest = new PageRequest(user.getPage() - 1, user.getSize(), sort);
        // 执行分页查询
        return userJPA.findAll(pageRequest).getContent();
    }
    //////////////////////////////////////////////////////////////////////////////////
}
