**A spring boot demo by spring boot initializr**
==
Project integration
--
* Maven
* JPA + MySQL
* JSP
* Servlet support

Third party integration
--
* Druid Connector pool
* FastJSON
* Interceptor
* JavaMail
* Spring Boot request logger
* Static resource path forwarding

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

Spring Boot Request Logger
--
通过设置拦截器，拦截所有请求的`Request`和`Response`，并将这些记录在数据库中，通过重写拦截器中的`preHandle方法`和`afterCompletion`方法分别将`Request`数据和
`Response`数据存储在`Logger Entity`中，最后将整个日志实体类存储到数据库中。

这里要注意一点，拦截器中无法通过`SpringBean`的方式注入`Logger实体`的JPA类，也就无法通过这种方式将最后的日志实体通过JPA持久化，所以通过另一种方式来获取（Filter比Bean先加载）。

获取方式：
`WebApplicationContextUtils`可以通过`HttpServletRequest`请求对象的上下文（`ServetCotext`）获取Spring管理的Bean，
通过`WebApplicationContextUtils`内部的`getRequiredWebApplicationContext`方法获取到
`BeanFactory`（实体工厂类），从而通过`BeanFactory`的`getBean()`方法就可以拿到`SpringDataJPA`为
我们管理的`Logger JPA持久化数据接口实例`，代码如下：
```java
private <T> T getDao(Class<T> clazz, HttpServletRequest request) {
    BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
    return factory.getBean(clazz);
}
```
创建日志请求，首先我们在`日志拦截器的 preHandle 方法中`创建`Logger Entity`，将一些必要的参数记录后，
将实体类写入`HttpServletRequest`中，接下来请求会进入对应的具体`Spring MVC 控制器方法`，在最后
渲染视图即将返回前台前会开始执行`日志拦截器中的 afterCompeletion 方法`，这个方法中记录了请求
状态码、请求时间戳、请求返回值等内容。

当然，最后，一定不要忘了将日志拦截器添加到Spring Boot项目中，使用`@Configuration`添加，示例代码如下：
```java
@Configuration
public class LoggerConfiguration extends WebMvcConfigurerAdapter{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor()).addPathPatterns("/**");
    }
}
```

Static resource path forwarding
--
Spring Boot默认配置了静态资源地址转发，我们只需要将资源文件放在`/resources/static/`目录下，
就可以直接通过网页访问这些静态资源了，但是这样会暴露项目结构，所以添加配置做一个静态资源路径转发：
```java
@Configuration
public class StaticPathConfiguration extends WebMvcConfigurerAdapter{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("lc/resources/**").addResourceLocations("classpath:/static/");
    }
}
```

Servlet support
--
Web开发使用`Controller`基本上可以完成大部分需求，但是我们还可能会用到`Servlet、Filter、Listener、Interceptor`等等。
当使用Spring Boot时，嵌入式Servlet容器通过扫描注解的方式注册`Servlet、Filter`和`Servlet规范的所有监听器`（如HttpSessionListener监听器）

在spring boot中添加自己的Servlet有两种方法，代码注册Servlet和注解自动注册（Filter和Listener也是如此）
1. 代码注册通过`ServletRegistrationBean`、`FilterRegistrationBean`和`ServletListenerRegistrationBean`获得控制。 
   也可以通过实现`ServletContextInitializer`接口直接注册。
2. 在`SpringBootApplication`上使用`@ServletComponentScan`注解后（或者通过`@configuration`配置`@ServeltComponentScan`），`Servlet、Filter、Listener`可以直接通过`@WebServlet、@WebFilter、@WebListener`注解自动注册，无需其他代码。代码如下：
```java
@WebServlet(urlPatterns = "/test")
public class TestServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        PrintWriter writer = resp.getWriter();
        writer.write("执行TestServlet中的doGet方法成功");
        writer.close();
    }
}
```
```java
@Configuration
@ServletComponentScan
public class ServletConfiguration {
}
```
