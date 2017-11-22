package com.lc.springioinit.springinitdemo.jpa;

import com.lc.springioinit.springinitdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationEvent;
import java.io.Serializable;
import java.util.List;

public interface UserJPA extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>, Serializable {

    /**
     * 根据年龄查询用户
     * @param age
     * @return
     */
    @Query(value = "select * from user where age > ?1", nativeQuery = true)
    public List<User> nativeQuery(int age);

    /**
     * 根据用户名密码删除一条数据
     * @param userName
     * @param pwd
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM USER WHERE username=?1 AND password=?2", nativeQuery = true)
    public void deleteQuery(String userName, String pwd);

}
