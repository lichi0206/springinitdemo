package com.lc.springioinit.springinitdemo.base;

import java.io.Serializable;

/**
 * @auther lichi
 * @create 2017-11-04 19:01
 */
public class BaseEntity implements Serializable {

    /**
     * <p>
     *     分页页码，默认页码为1
     * </p>
     */
    protected int page = 1;

    /**
     * <p>
     *     每页数量，默认20条
     * </p>
     */
    protected int size = 20;

    /**
     * <p>
     *     排序列名称，默认为“id”
     * </p>
     */
    protected String sidx = "id";

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }
}
