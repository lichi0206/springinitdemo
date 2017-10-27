package com.lc.springioinit.springinitdemo.utils;

/**
 * Mail content type enum
 *
 * @auther lichi
 * @create 2017-10-26 23:16
 */
public enum MailContentTypeEnum {

    HTML("text/html;charset=UTF-8"),
    TEXT("text");

    private String value;

    MailContentTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
