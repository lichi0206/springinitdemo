package com.lc.springioinit.springinitdemo.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Good type
 *
 * @auther lichi
 * @create 2017-11-14 22:00
 */
@Entity
@Table(name = "good_types")
public class GoodTypeEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "tgt_id")
    private Long id;

    @Column(name = "tgt_name")
    private String name;

    @Column(name = "tgt_is_show")
    private int isShow;

    @Column(name = "tgt_order")
    private int order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
