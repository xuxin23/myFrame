<!-- 定义jsp页面 -->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- 根路径 -->
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":"
            + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- 根路径 -->
<base href="<%=basePath%>">
<title>自定义Struts框架登录页面</title>
</head>
<body>
	<form action="login" method="post">
		账号：<input type="text" name="user.id"/><br/>
		密码：<input type="text" name="user.password"/><br/>
		<input type="submit" value="登录"/>
	</form>
</body>
</html>