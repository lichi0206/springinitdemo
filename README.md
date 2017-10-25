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
* 如果使用Tomcat容器的话，打包方式改为war方式（Jar方式不推荐也不支持）：
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
* 添加JSP解析引擎：
```XML
<!-- spring boot tomcat jsp support init -->
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
    <version>8.5.6</version>
</dependency>
```
以及JSTL（JSP标准标签库）支持：
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
* 添加servlet-api
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

