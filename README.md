**A spring boot demo by spring boot initializr**
==
Project integration
--
* Maven
* JPA + MySQL
* JSP

Third party integration
--
* Druid Connector pool
* FastJSON
* Interceptor
* JavaMail

Add JSP support
--
* **打包方式改为war方式**：
```xml
<package>war</package>
```
官方文档说明：
>When running a Spring Boot application that uses an embedded servlet container (and is packaged as an executable archive), there are some limitations in the JSP support.
>* With Tomcat it should work if you use war packaging, i.e. an executable war will work, and will also be deployable to a standard container (not limited to, but including Tomcat). An executable jar will not work because of a hard coded file pattern in Tomcat.
>* With Jetty it should work if you use war packaging, i.e. an executable war will work, and will also be deployable to any standard container.
>* Undertow does not support JSPs.
>* Creating a custom error.jsp page won’t override the default view for error handling, custom error pages should be used instead.

[参考Developing web applications](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html#boot-features-jsp-limitations)

* **添加JSP解析引擎以及JSTL：**
```XML
<!-- spring boot tomcat jsp support init -->
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
    <version>8.5.6</version>
</dependency>
```
JSTL（JSP标准标签库）支持：
```xml
<!-- jstl support init -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2</version>
</dependency>
```
上述代码一定要注意，不要在`<dependency>`标签里面添加`<scope>provide</scope>`，否则会访问不到JSP页面
不需要加下述`<dependency>`
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
</dependency>
```
如果非要加上述代码，也不要在`<dependency>`标签里面添加`<scope>provide</scope>`
这两个`<dependency>`加不加`<scope>provide</scope>`一共有四种组合，目前都不加是可以实现访问JSP页面的，
`embed`添加`provide`肯定不能访问到，其他组合没有试过

* **添加servlet-api**
```xml
<!-- servlet support init -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
</dependency>
```
添加`servlet-api`支持之后，`maven`编译阶段才会正常编译通过，但是如果你不用`maven`内置的`tomcat`，打好`war`包之后，
放到单独的`Tomcat`容器里面去运行，有可能会产生包重复的错误，因为`Tomcat`容器中是默认有`Servlet`包的，所以项目中的
`Servlet-api`一定要指定好范围，最好加上：`<scope>provide</scope>`

* **创建`JSP`存放目录并在配置文件中指定**

创建`JSP`存放目录（与java/resources目录齐平）:
```
/webapp/WEB-INF/view/*.jsp
```
修改`/resources/application.properties`文件使`JSP`目录生效
```
# 页面文件存放目录
spring.mvc.view.prefix=/WEB-INF/view/
# 页面文件后缀
spring.mvc.view.suffix=.jsp
```

`@RestController` 与 `@Controller`
-----------------------------
`Spring Boot`基于`Spring MVC`改编，将`@Controller`与`ResponseBody`进行合并为一个新的注解：`@RestController`
`@RestController`中返回视图：
```java
@RequestMapping(value="/tologin", method=RequestMethod.GET)
public ModelAndView login(){
    ModelAndView mv = new ModelAndView("index");
    return mv;
}
```
正常`Return "Stirng"`将会返回字符串或者JSON数据（需要在`@RequestMapping`中指定：`produces = "application/json; charset=UTF-8"`）

Java Mail
--
构建发送过程：
1. 构建一个继承自`javax.mail.Authenticator`的具体类，并重写里面的`getPasswordAuthentication()`方法。此类是用作登录校验的，以确保你对该邮箱有发送邮件的权利。
2. 构建一个`properties`文件，该文件中存放`SMTP`服务器地址等参数。
3. 通过构建的`properties`文件和`javax.mail.Authenticator`具体类来创建一个`javax.mail.Session`（`Session`的创建，就相当于登录邮箱一样）。
4. 构建邮件内容，一般是`javax.mail.internet.MimeMessage`对象，并指定发送人，收信人，主题，内容等等。
5. 使用`javax.mail.Transport`工具类发送邮件。

发送邮箱需要确认：
1. POP3/SMTP服务已开启
2. 获取授权码（非独立密码）

有些邮箱需要使用SSL发送，这类邮箱注意添加：
```java
properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
properties.put("mail.smtp.socketFactory.port", "465");
```