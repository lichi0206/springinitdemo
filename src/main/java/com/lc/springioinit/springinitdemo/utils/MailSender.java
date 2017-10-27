package com.lc.springioinit.springinitdemo.utils;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
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
            throw new Exception("邮件标题未设置，请设置（SetTitle()）");
        if (mail.getContent() == null || mail.getContent().trim().length() == 0)
            throw new Exception("邮件内容未设置，请设置");
        if (mail.getToList().size() == 0)
            throw new Exception("收件人列表未设置，请设置");
        // 读取/resources/mail_zh_CN.properties文件内容
        final PropertiesUtil propertiesUtil = new PropertiesUtil("mail");
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
        // 构建授权信息，用于进行SMTP身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                // user name & password
                String userName = properties.getProperty("mail.user");
                String password = properties.getProperty("mail.password");
                return new javax.mail.PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(properties, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        String nickName = MimeUtility.encodeText(propertiesUtil.getValue("mail.from.nickName"));
        InternetAddress from = new InternetAddress(nickName + "<" + properties.getProperty("mail.user") + ">");
        message.setFrom(from);
        // 设置邮件标题
        message.setSubject(mail.getTitle());
        // HTML发送邮件
        if (mail.getContentType().equals(MailContentTypeEnum.HTML.getValue()))
            message.setContent(mail.getContent(), mail.getContentType());
            // 文本发送邮件
        else if (mail.getContentType().equals(MailContentTypeEnum.TEXT.getValue()))
            message.setText(mail.getContent());
        // 发送邮箱地址
        List<String> targets = mail.getToList();
        for (String target : targets) {
            try {
                // 设置收件人邮箱
                InternetAddress to = new InternetAddress(target);
                message.setRecipient(Message.RecipientType.TO, to);
            } catch (Exception e) {
                continue;
            }
        }
        // 发送邮件
        Transport.send(message);
    }

}
