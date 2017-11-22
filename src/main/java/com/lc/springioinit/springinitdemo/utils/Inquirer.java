package com.lc.springioinit.springinitdemo.utils;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 自定义查询实体类
 *
 * @auther lichi
 * @create 2017-11-14 22:30
 */
public class Inquirer {

    // 查询条件集合
    private List<BooleanExpression> expressions;
    public Inquirer() {
        this.expressions = new ArrayList<>();
    }

    /**
     * 添加查询条件到查询集合内
     * @param expression 查询条件继承BooleanExpression抽象对象得具体实体对象，querydsl已经封装
     * @return
     */
    public Inquirer putExpression(BooleanExpression expression) {
        expressions.add(expression);
        return this;
    }

    /**
     * 构建出查询findall函数使用得Predicate接口查询对象
     * 调用buildQuery可直接作为findall参数查询条件使用
     * @return
     */
    public Predicate buildQuery() {
        BooleanExpression be = null;
        for (int i = 0; i < expressions.size(); i++) {
            if (i == 0)
                be = expressions.get(i);
            else
                be = be.and(expressions.get(i));
        }

        return be;
    }

    /**
     * 将Iterator集合转化为Array List集合
     * @param iterable
     * @param <T>
     * @return
     */
    public <T> List<T> iteartorToList(Iterable<T> iterable) {
        List<T> returnList = new ArrayList<>();
        Iterator<T> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            returnList.add(iterator.next());
        }

        return returnList;
    }
}
