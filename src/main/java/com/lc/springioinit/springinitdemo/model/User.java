package com.lc.springioinit.springinitdemo.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Model: User
 *
 * @auther lichi
 * @create 2017-10-21 0:02
 */

@Entity(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "userid")
    private int userId;

    @Column(name = "user_no")
    private String userNo;

    @Column(name = "username")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "valid")
    private String valid;

    @Column(name = "age")
    private int age;

    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
