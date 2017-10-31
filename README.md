**A spring boot demo by spring boot initializr**
==
Project integration
--
* Maven
* JPA + MySQL
* JSP
* Servlet support
* Upload File & Files
* Custom LogBack

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

乱码问题
--
####`@Controller`返回值乱码问题：

在`produce`参数中加入编码声明`charset=UTF-8`，示例代码如下：
```java
@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
```

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

Upload File/Files
--
使用`MultipartFile`对象内置的方法`transferTo()`就可以实现JSP页面上传到`request`内的文件对象直接存储到指定文件`File`对象内，以此来完成上传文件的存储。代码如下：
```java
private void executeUpload(String uploadDir, MultipartFile file) throws Exception {
    // 文件后缀名
    String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
    // 上传文件名，使用UUID防止重名
    String fileName = UUID.randomUUID() + suffix;
    // 服务器端保存对象，并存入服务器文件夹中
    File serverFile = new File(uploadDir + fileName);
    file.transferTo(serverFile);
}
```
`SpringBoot`上传文件时限制了我们上传文件最大容量为：`1048576 bytes`，那我们需要上传超过配置的文件时修改怎么做呢？
打开`application.properties`配置文件，加入`spring.http.multipart.max-file-size`以及`spring.http.multipart.max-request-size`配置信息
```properties
spring.http.multipart.max-file-size=1024Mb
spring.http.multipart.max-request-size=2048Mb
```
Custom LogBack
--
spring boot内部使用`Commons Logging`来记录日志，但也保留外部接口可以让一些日志框架来进行实现，例如`Java Util Logging`,`Log4J2`还有`Logback`。如果你想用某一种日志框架来进行实现的话，就必须先配置，默认情况下，spring boot使用Logback作为日志实现的框架。
SpringBoot内部集成了`LogBack`日志依赖，SpringBoot默认使用LogBack记录日志信息，默认根据`base.xml`配置内容来输出到控制台和文件之中。

####LogBack读取配置文件的步骤
- 尝试classpath下查找文件`logback-test.xml`
- 如果文件不存在，尝试查找`logback.xml`
- 如果两个文件都不存在，LogBack用`BasicConfiguration`自动对自己进行最小化配置，这样既实现了上面我们不需要添加任何配置就可以输出到控制台日志信息。

接下来我们在`resources`目录下创建名叫`logback.xml`的文件，并且添加日志配置输出到文件内按天存储到不同的文件之中。
- 彩色日志
- 专为 Hibernate 定制
- 日志异步到数据库
```xml
<configuration debug="false">
    <!-- 定义日志文件的存放地址，请勿在 logBack 配置中使用相对路径 -->
    <property name="LOG_HOME" value="./logs"/>

    <!-- 彩色日志 -->
    <!-- 彩色日志依赖的渲染类 -->
    <conversionRule conversionWord="clr" converterClass="org.springframework.boot.logging.logback.ColorConverter" />
    <conversionRule conversionWord="wex" converterClass="org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter" />
    <conversionRule conversionWord="wEx" converterClass="org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter" />
    <!-- 彩色日志格式 -->
    <property name="CONSOLE_LOG_PATTERN" value="${CONSOLE_LOG_PATTERN:-%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}" />
    <!-- Console 输出设置 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
            <charset>utf8</charset>
        </encoder>
    </appender>

    <!--&lt;!&ndash; 不带彩色日志 &ndash;&gt;
    &lt;!&ndash; Console输出设置 &ndash;&gt;
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            &lt;!&ndash; 格式化输出：%d表示日期，%thread表示线程名，%-5level级别从左显示五个字符宽度，%msg表示消息，%n换行&ndash;&gt;
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} -%msg %n</pattern>
        </encoder>
    </appender>-->

    <!-- 按照每天生成日志文件 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 日志文件输出名 -->
            <FileNamePattern>${LOG_HOME}/runtime.log.%d{yyyy-MM-dd}.log</FileNamePattern>
            <!-- 最大保留天数 -->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!-- 格式化输出：%d表示日期，%thread表示线程名，%-5level级别从左显示五个字符宽度，%msg表示消息，%n换行-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} -%msg %n</pattern>
        </encoder>
        <!-- 日志文件最大大小 -->
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>10MB</MaxFileSize>
        </triggeringPolicy>
    </appender>

    <!-- show parameters for hibernate sql 专为 Hibernate 定制 -->
    <logger name="org.hibernate.type.descriptor.sql.BasicBinder"  level="TRACE" />
    <logger name="org.hibernate.type.descriptor.sql.BasicExtractor"  level="DEBUG" />
    <logger name="org.hibernate.SQL" level="DEBUG" />
    <logger name="org.hibernate.engine.QueryParameters" level="DEBUG" />
    <logger name="org.hibernate.engine.query.HQLQueryPlan" level="DEBUG" />

    <!--myibatis log configure-->
    <logger name="com.apache.ibatis" level="TRACE"/>
    <logger name="java.sql.Connection" level="DEBUG"/>
    <logger name="java.sql.Statement" level="DEBUG"/>
    <logger name="java.sql.PreparedStatement" level="DEBUG"/>

    <!-- 日志输出级别 -->
    <root level="INFO">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="FILE"/>
    </root>

    <!--日志异步到数据库
    <appender name="DB" class="ch.qos.logback.classic.db.DBAppender">
        &lt;!&ndash;日志异步到数据库 &ndash;&gt;
        <connectionSource class="ch.qos.logback.core.db.DriverManagerConnectionSource">
            &lt;!&ndash;连接池 &ndash;&gt;
            <dataSource class="com.mchange.v2.c3p0.ComboPooledDataSource">
                <driverClass>com.mysql.jdbc.Driver</driverClass>
                <url>jdbc:mysql://127.0.0.1:3306/databaseName</url>
                <user>root</user>
                <password>123456</password>
            </dataSource>
        </connectionSource>
    </appender>-->
</configuration>
```
####LogBack取代log4j的理由：
    1、更快的实现  Logback的内核重写了，在一些关键执行路径上性能提升10倍以上。而且logback不仅性能提升了，初始化内存加载也更小了。
    2、非常充分的测试  Logback经过了几年，数不清小时的测试。Logback的测试完全不同级别的。在作者的观点，这是简单重要的原因选择logback而不是log4j。=
    3、Logback-classic非常自然实现了SLF4j    Logback-classic实现了 SLF4j。在使用SLF4j中，你都感觉不到logback-classic。而且因为logback-classic非常自然地实现了SLF4J，  所 以切换到log4j或者其他，非常容易，只需要提供成另一个jar包就OK，根本不需要去动那些通过SLF4JAPI实现的代码。
    4、非常充分的文档  官方网站有两百多页的文档。
    5、自动重新加载配置文件  当配置文件修改了，Logback-classic能自动重新加载配置文件。扫描过程快且安全，它并不需要另外创建一个扫描线程。这个技术充分保证了应用程序能跑得很欢在JEE环境里面。
    6、Lilith   Lilith是log事件的观察者，和log4j的chainsaw类似。而lilith还能处理大数量的log数据 。
    7、谨慎的模式和非常友好的恢复  在谨慎模式下，多个FileAppender实例跑在多个JVM下，能 够安全地写道同一个日志文件。RollingFileAppender会有些限制。Logback的FileAppender和它的子类包括 RollingFileAppender能够非常友好地从I/O异常中恢复。
    8、配置文件可以处理不同的情况   开发人员经常需要判断不同的Logback配置文件在不同的环境下（开发，测试，生产）。而这些配置文件仅仅只有一些很小的不同，可以通过,和来实现，这样一个配置文件就可以适应多个环境。
    9、Filters（过滤器）  有些时候，需要诊断一个问题，需要打出日志。在log4j，只有降低日志级别，不过这样会打出大量的日志，会影响应用性能。在Logback，你可以继续 保持那个日志级别而除掉某种特殊情况，如alice这个用户登录，她的日志将打在DEBUG级别而其他用户可以继续打在WARN级别。要实现这个功能只需 加4行XML配置。可以参考MDCFIlter 。
    10、SiftingAppender（一个非常多功能的Appender）  它可以用来分割日志文件根据任何一个给定的运行参数。如，SiftingAppender能够区别日志事件跟进用户的Session，然后每个用户会有一个日志文件。
    11、自动压缩已经打出来的log  RollingFileAppender在产生新文件的时候，会自动压缩已经打出来的日志文件。压缩是个异步过程，所以甚至对于大的日志文件，在压缩过程中应用不会受任何影响。
    12、堆栈树带有包版本  Logback在打出堆栈树日志时，会带上包的数据。
    13、自动去除旧的日志文件  通过设置TimeBasedRollingPolicy或者SizeAndTimeBasedFNATP的maxHistory属性，你可以控制已经产生日志文件的最大数量。如果设置maxHistory 12，那那些log文件超过12个月的都会被自动移除。
####logBack配置介绍
    1、Logger、appender及layout
       Logger作为日志的记录器，把它关联到应用的对应的context上后，主要用于存放日志对象，也可以定义日志类型、级别。
    Appender主要用于指定日志输出的目的地，目的地可以是控制台、文件、远程套接字服务器、 MySQL、 PostreSQL、 Oracle和其他数据库、 JMS和远程UNIX Syslog守护进程等。 
    Layout 负责把事件转换成字符串，格式化的日志信息的输出。
    2、logger context
       各个logger 都被关联到一个 LoggerContext，LoggerContext负责制造logger，也负责以树结构排列各 logger。其他所有logger也通过org.slf4j.LoggerFactory 类的静态方法getLogger取得。 getLogger方法以 logger 名称为参数。用同一名字调用LoggerFactory.getLogger 方法所得到的永远都是同一个logger对象的引用。
    3、有效级别及级别的继承
       Logger 可以被分配级别。级别包括：TRACE、DEBUG、INFO、WARN 和 ERROR，定义于 ch.qos.logback.classic.Level类。如果 logger没有被分配级别，那么它将从有被分配级别的最近的祖先那里继承级别。root logger 默认级别是 DEBUG。
    4、打印方法与基本的选择规则
       打印方法决定记录请求的级别。例如，如果 L 是一个 logger 实例，那么，语句 L.info("..")是一条级别为 INFO 的记录语句。记录请求的级别在高于或等于其 logger 的有效级别时被称为被启用，否则，称为被禁用。记录请求级别为 p，其logger的有效级别为 q，只有则当 p>=q时，该请求才会被执行。
    **该规则是 logback 的核心。级别排序为： TRACE < DEBUG < INFO < WARN < ERROR。**
####采用滚动记录并将INFO、ERROR、DEBUG信息分别记录在不同文件中的示例
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!--
       说明：
       1、日志级别及文件
           日志记录采用分级记录，级别与日志文件名相对应，不同级别的日志信息记录到不同的日志文件中
           例如：error级别记录到log_error_xxx.log或log_error.log（该文件为当前记录的日志文件），而log_error_xxx.log为归档日志，
           日志文件按日期记录，同一天内，若日志文件大小等于或大于2M，则按0、1、2...顺序分别命名
           例如log-level-2013-12-21.0.log
           其它级别的日志也是如此。
       2、文件路径
           若开发、测试用，在Eclipse中运行项目，则到Eclipse的安装路径查找logs文件夹，以相对路径../logs。
           若部署到Tomcat下，则在Tomcat下的logs文件中
       3、Appender
           FILEERROR对应error级别，文件名以log-error-xxx.log形式命名
           FILEWARN对应warn级别，文件名以log-warn-xxx.log形式命名
           FILEINFO对应info级别，文件名以log-info-xxx.log形式命名
           FILEDEBUG对应debug级别，文件名以log-debug-xxx.log形式命名
           stdout将日志信息输出到控制上，为方便开发测试使用
    -->
    <contextName>SpringBootDemo</contextName>
    <property name="LOG_PATH" value="D:\\JavaWebLogs" />
    <!--设置系统日志目录-->
    <property name="APPDIR" value="SpringBootDemo" />

    <!-- 日志记录器，日期滚动记录 -->
    <appender name="FILEERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 正在记录的日志文件的路径及文件名 -->
        <file>${LOG_PATH}/${APPDIR}/log_error.log</file>
        <!-- 日志记录器的滚动策略，按日期，按大小记录 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 归档的日志文件的路径，例如今天是2013-12-21日志，当前写的日志文件路径为file节点指定，可以将此文件与file指定文件路径设置为不同路径，从而将当前日志文件或归档日志文件置不同的目录。
            而2013-12-21的日志文件在由fileNamePattern指定。%d{yyyy-MM-dd}指定日期格式，%i指定索引 -->
            <fileNamePattern>${LOG_PATH}/${APPDIR}/error/log-error-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <!-- 除按日志记录之外，还配置了日志文件不能超过2M，若超过2M，日志文件会以索引0开始，
            命名日志文件，例如log-error-2013-12-21.0.log -->
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>2MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <!-- 追加方式记录日志 -->
        <append>true</append>
        <!-- 日志文件的格式 -->
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>===%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger Line:%-3L - %msg%n</pattern>
            <charset>utf-8</charset>
        </encoder>
        <!-- 此日志文件只记录info级别的 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>error</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- 日志记录器，日期滚动记录 -->
    <appender name="FILEWARN" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 正在记录的日志文件的路径及文件名 -->
        <file>${LOG_PATH}/${APPDIR}/log_warn.log</file>
        <!-- 日志记录器的滚动策略，按日期，按大小记录 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 归档的日志文件的路径，例如今天是2013-12-21日志，当前写的日志文件路径为file节点指定，可以将此文件与file指定文件路径设置为不同路径，从而将当前日志文件或归档日志文件置不同的目录。
            而2013-12-21的日志文件在由fileNamePattern指定。%d{yyyy-MM-dd}指定日期格式，%i指定索引 -->
            <fileNamePattern>${LOG_PATH}/${APPDIR}/warn/log-warn-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <!-- 除按日志记录之外，还配置了日志文件不能超过2M，若超过2M，日志文件会以索引0开始，
            命名日志文件，例如log-error-2013-12-21.0.log -->
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>2MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <!-- 追加方式记录日志 -->
        <append>true</append>
        <!-- 日志文件的格式 -->
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>===%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger Line:%-3L - %msg%n</pattern>
            <charset>utf-8</charset>
        </encoder>
        <!-- 此日志文件只记录info级别的 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>warn</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- 日志记录器，日期滚动记录 -->
    <appender name="FILEINFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 正在记录的日志文件的路径及文件名 -->
        <file>${LOG_PATH}/${APPDIR}/log_info.log</file>
        <!-- 日志记录器的滚动策略，按日期，按大小记录 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 归档的日志文件的路径，例如今天是2013-12-21日志，当前写的日志文件路径为file节点指定，可以将此文件与file指定文件路径设置为不同路径，从而将当前日志文件或归档日志文件置不同的目录。
            而2013-12-21的日志文件在由fileNamePattern指定。%d{yyyy-MM-dd}指定日期格式，%i指定索引 -->
            <fileNamePattern>${LOG_PATH}/${APPDIR}/info/log-info-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <!-- 除按日志记录之外，还配置了日志文件不能超过2M，若超过2M，日志文件会以索引0开始，
            命名日志文件，例如log-error-2013-12-21.0.log -->
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>2MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <!-- 追加方式记录日志 -->
        <append>true</append>
        <!-- 日志文件的格式 -->
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>===%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger Line:%-3L - %msg%n</pattern>
            <charset>utf-8</charset>
        </encoder>
        <!-- 此日志文件只记录info级别的 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>info</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <!--encoder 默认配置为PatternLayoutEncoder-->
        <encoder>
            <pattern>===%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger Line:%-3L - %msg%n</pattern>
            <charset>utf-8</charset>
        </encoder>
        <!--此日志appender是为开发使用，只配置最底级别，控制台输出的日志级别是大于或等于此级别的日志信息-->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>debug</level>
        </filter>
    </appender>

    <logger name="org.springframework" level="WARN" />
    <logger name="org.hibernate" level="WARN" />

    <!-- 生产环境下，将此级别配置为适合的级别，以免日志文件太多或影响程序性能 -->
    <root level="INFO">
        <appender-ref ref="FILEERROR" />
        <appender-ref ref="FILEWARN" />
        <appender-ref ref="FILEINFO" />

        <!-- 生产环境将请stdout,testfile去掉 -->
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```