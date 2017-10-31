package com.lc.springioinit.springinitdemo.controller;

import com.lc.springioinit.springinitdemo.jpa.UserJPA;
import com.lc.springioinit.springinitdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;

/**
 * Controller: login
 *
 * @auther lichi
 * @create 2017-10-21 19:08
 */
@RestController
@RequestMapping(value = "/user")
public class LoginController {

    @Autowired
    private UserJPA userJPA;

    @RequestMapping(value = "/login")
    public ModelAndView login(User user, HttpServletRequest request) {
        boolean flag = true;
        String page = "fileupload";

        // Query whether user exist
        User userIsExist = userJPA.findOne(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                criteriaQuery.where(criteriaBuilder.equal(root.get("userNo"), user.getUserNo()));
                return null;
            }
        });

        if (userIsExist == null) {
            flag = false;
            page = "User doesn't exist, login failed!";
        } else if (!userIsExist.getPassword().equals(user.getPassword())) {
            flag = false;
            page = "Password is not correct, please retry!";
        }

        // Login success, inject session
        if (flag)
            request.getSession().setAttribute("_session_user", userIsExist);

        return new ModelAndView(page);
    }
}
