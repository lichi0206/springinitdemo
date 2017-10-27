package com.lc.springioinit.springinitdemo.utils;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Read and write properties
 *
 * @auther lichi
 * @create 2017-10-26 23:25
 */
public class PropertiesUtil {

    private final ResourceBundle resource;
    private final String fileName;

    /**
     * 构造函数实例化部分对象，获取文件资源
     *
     * @param fileName
     */
    public PropertiesUtil(String fileName) {
        this.fileName = fileName;
        this.resource = ResourceBundle.getBundle(this.fileName);
    }

    /**
     * 根据传入的key获取对象的值
     *
     * @param key
     * @return key对应的值
     */
    public String getValue(String key) {
        String message = this.resource.getString(key);
        return message;
    }

    /**
     * 获取Properties文件中所有的key值
     *
     * @return
     */
    public Enumeration<String> getKey() {
        return resource.getKeys();
    }
}
