package com.lc.springioinit.springinitdemo.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Java mail
 *
 * @auther lichi
 * @create 2017-10-26 23:08
 */
public class Mail implements Serializable {

    // SMTP server
    private String smtpService;

    // Port
    private String smtpPort;

    // Send mail address
    private String fromMailAddress;

    // Send mail password
    private String fromMailSmtpPwd;

    // Mail title
    private String title;

    // Mail content
    private String content;

    // Mail content type
    private String contentType;

    // To list
    private List<String> toList = new ArrayList<>();

    public String getSmtpService() {
        return smtpService;
    }

    public void setSmtpService(String smtpService) {
        this.smtpService = smtpService;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getFromMailAddress() {
        return fromMailAddress;
    }

    public void setFromMailAddress(String fromMailAddress) {
        this.fromMailAddress = fromMailAddress;
    }

    public String getFromMailSmtpPwd() {
        return fromMailSmtpPwd;
    }

    public void setFromMailSmtpPwd(String fromMailSmtpPwd) {
        this.fromMailSmtpPwd = fromMailSmtpPwd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<String> getToList() {
        return toList;
    }

    public void setToList(List<String> toList) {
        this.toList = toList;
    }
}
