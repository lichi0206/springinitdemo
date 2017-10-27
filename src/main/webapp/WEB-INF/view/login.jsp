<%--
  Created by IntelliJ IDEA.
  User: lichi
  Date: 2017-10-21
  Time: 19:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<form method="get" , action="/user/login">
    Username: <input type="text" , name="userNo"/><br/>
    Password: <input type="text" , name="password"/><br/>
    <input type="submit" value="login"/>
</form>
</body>
</html>
