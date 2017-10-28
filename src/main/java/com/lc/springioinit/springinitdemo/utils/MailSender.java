package com.lc.springioinit.springinitdemo.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.List;
import java.util.Properties;

/**
 * Send mail
 *
 * @auther lichi
 * @create 2017-10-26 23:12
 */
public class MailSender {

    private static Mail mail = new Mail();

    /**
     * 设置邮件标题
     *
     * @param title
     * @return
     */
    public MailSender title(String title) {
        mail.setTitle(title);
        return this;
    }

    /**
     * 设置邮件内容
     *
     * @param content
     * @return
     */
    public MailSender content(String content) {
        mail.setContent(content);
        return this;
    }

    /**
     * 设置邮件格式
     *
     * @param typeEnum
     * @return
     */
    public MailSender contentType(MailContentTypeEnum typeEnum) {
        mail.setContentType(typeEnum.getValue());
        return this;
    }

    /**
     * 设置请求目标邮件地址
     *
     * @param targets
     * @return
     */
    public MailSender targets(List<String> targets) {
        mail.setToList(targets);
        return this;
    }

    /**
     * 执行发送邮件
     *
     * @throws Exception
     */
    public void send() throws Exception {
        // 默认使用HTML格式发送
        if (mail.getContentType() == null)
            mail.setContentType(MailContentTypeEnum.HTML.getValue());
        if (mail.getTitle() == null || mail.getTitle().trim().length() == 0)
            throw new Exception("邮件标题未设置，请设置");
        if (mail.getContent() == null || mail.getContent().trim().length() == 0)
            throw new Exception("邮件内容未设置，请设置");
        if (mail.getToList().size() == 0)
            throw new Exception("收件人列表未设置，请设置");

        // 读取/resources/mail.properties文件内容
        final PropertiesUtil propertiesUtil = new PropertiesUtil("mail");

        Properties properties = setMailProperties(propertiesUtil, true);

        // 创建邮件会话
        Session mailSession = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // user name & password
                String userName = properties.getProperty("mail.user");
                String password = properties.getProperty("mail.password");
                return new javax.mail.PasswordAuthentication(userName, password);
            }
        });
        mailSession.setDebug(true); // 查看邮件发送过程

        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);

        // 设置发件人
        String nickName = MimeUtility.encodeText(propertiesUtil.getValue("mail.from.nickName"));
        InternetAddress from = new InternetAddress(nickName + "<" + properties.getProperty("mail.user") + ">");
        message.setFrom(from);

        // 设置邮件标题
        message.setSubject(mail.getTitle());
        // HTML OR TEXT
        if (mail.getContentType().equals(MailContentTypeEnum.HTML.getValue()))
            message.setContent(mail.getContent(), mail.getContentType());
        else if (mail.getContentType().equals(MailContentTypeEnum.TEXT.getValue()))
            message.setText(mail.getContent());

        // 发送邮箱地址
        List<String> targets = mail.getToList();
        for (String target : targets) {
            try {
                InternetAddress to = new InternetAddress(target);
                message.addRecipient(Message.RecipientType.TO, to);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 发送邮件
        Transport.send(message);
    }

    /**
     * 设置邮箱基本属性
     * @return
     */
    public Properties setMailProperties(PropertiesUtil propertiesUtil, boolean ssl) {
        // 创建Properties类用来记录邮箱属性
        final Properties properties = new Properties();
        // 表示smtp发送邮件，必须进行身份验证
        properties.put("mail.smtp.auth", true);
        // smtp服务器
        properties.put("mail.smtp.host", propertiesUtil.getValue("mail.smtp.service"));
        // 设置端口号
        properties.put("mail.smtp.port", propertiesUtil.getValue("mail.smtp.port"));
        // 设置发送邮箱
        properties.put("mail.user", propertiesUtil.getValue("mail.from.address"));
        // 设置发送邮箱的16位SMTP口令
        properties.put("mail.password", propertiesUtil.getValue("mail.from.smtp.pwd"));

        // SSL发送
        if(ssl) {
            properties.put("mail.smtp.socketFactory.class", propertiesUtil.getValue("mail.smtp.socketFactory.class"));
            properties.put("mail.smtp.socketFactory.port", propertiesUtil.getValue("mail.smtp.socketFactory.port"));
            properties.put("mail.transport.protocol", propertiesUtil.getValue("mail.transport.protocol"));
        }

        return properties;
    }

}
