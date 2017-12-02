package com.lc.springioinit.springinitdemo.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

/**
 * @auther lichi
 * @create 2017-11-23 21:02
 */
@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    private Long id;

    @Column(name = "r_name")
    private String roleName;

     /**
     * mappedBy:
     *   表示当前所在表和User的关系是定义在User表中的roles成员变量上面，
     *   他表示此表是多对多关系中的从表，也就是关系在User表中维护，
     *   User表是关系的维护者，有主导权，他有个外键指向Roles
     * NotFound：
     *   意思是找不到引用的外键数据时忽略，NotFound默认是Exception
     */
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
