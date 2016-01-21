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
<title>自定义Struts框架成功页面</title>
</head>
<body>
	当前登录用户<a href="exit">注销</a><br/>
	账户：${user.id}<br/>
	昵称：${user.name}<br/>
	地址：${user.address}<br/><br/>
	
	新闻列表<br/>
	<table style="background-color:cyan">
		<tr>
			<th>标题</th>
			<th>内容</th>
			<th>日期</th>
		</tr>
		<c:forEach var="e" items="${newsList}">
			<tr>
				<td>${e.title}</td>
				<td>${e.content}</td>
				<td>${e.date}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>