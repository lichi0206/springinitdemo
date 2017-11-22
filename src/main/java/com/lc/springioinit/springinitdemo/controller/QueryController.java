package com.lc.springioinit.springinitdemo.controller;

import com.lc.springioinit.springinitdemo.jpa.GoodJPA;
import com.lc.springioinit.springinitdemo.model.GoodEntity;
import com.lc.springioinit.springinitdemo.model.QGoodEntity;
import com.lc.springioinit.springinitdemo.utils.Inquirer;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * @auther lichi
 * @create 2017-11-14 22:05
 */
@RestController
public class QueryController {

    @Autowired
    private GoodJPA goodJPA;

    // 注入Entity Manager
    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping(value = "/query")
    public List<GoodEntity> list() {
        // querydsl查询实体
        QGoodEntity _good = QGoodEntity.goodEntity;
        // 构建JPA查询对象
        JPAQuery<GoodEntity> jpaQuery = new JPAQuery<>(entityManager);
        // 返回查询接口
        return jpaQuery
                // 查询字段
                .select(_good)
                // 查询表
                .from(_good)
                // 查询条件
                .where(_good.type.id.eq(Long.valueOf("1")))
                // 返回结果
                .fetch();
    }

    /**
     * Spring data jpa 整合querydsl完成查询
     * @return
     */
    @RequestMapping(value = "/join")
    public List<GoodEntity> join() {
        // DSL查询实体
        QGoodEntity _good = QGoodEntity.goodEntity;

        /**
         * 第一种实现方式
         */
        /*// 查询条件
        BooleanExpression expression = _good.type.id.eq(Long.valueOf("1"));
        // 执行查询
        Iterator<GoodEntity> iterable = goodJPA.findAll(expression).iterator();
        List<GoodEntity> goods = new ArrayList<>();

        while (iterable.hasNext()) {
            goods.add(iterable.next());
        }

        return goods;*/
        /**
         * 第二种实现方式
         */
        // 自定义查询对象
        Inquirer inquirer = new Inquirer();

        // 构建查询条件
        inquirer.putExpression(_good.type.id.eq(Long.valueOf("1")));

        // 返回查询结果
        return inquirer.iteartorToList(goodJPA.findAll(inquirer.buildQuery()));

    }

}
