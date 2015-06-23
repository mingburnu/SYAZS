<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
	<title>Error!</title>
</head>

<body>
	<h2>發生錯誤 please check the console's log!</h2>
	<div class="alert alert-error">
		<s:property value="%{exception.message}"/><br/>
		<s:property value="%{exceptionStack}"/>
	</div>
</body>
</html>
