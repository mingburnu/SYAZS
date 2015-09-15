<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>跨司署電子資料庫索引查詢平台</title>
</head>
<body>
	<c:if test="${empty login.userId}">
		<%
			response.sendRedirect(request.getContextPath()
						+ "/authorization/userEntry.action");
		%>
	</c:if>
	<c:if test="${not empty login.userId}">
		<%
			response.sendRedirect(request.getContextPath()
						+ "/page/index.action");
		%>
	</c:if>
</body>
</html>