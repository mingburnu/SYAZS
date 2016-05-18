<%@page import="org.owasp.esapi.ESAPI"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="title">
	<%=ESAPI.encoder().encodeForHTML(
					request.getParameter("title"))%>
</div>
<div class="query_list">
	<a class="btn_04" onclick="queryByPrefix('0-9')">0-9</a>
	<c:forEach begin="65" end="90" varStatus="loop">
		<a class="btn_04" onclick="queryByPrefix('${'&#'}${loop.index };')">${'&#'}${loop.index };</a>
	</c:forEach>
</div>
<div class="query_list">
	<c:forEach begin="12549" end="12585" varStatus="loop">
		<a class="btn_04" onclick="queryByPrefix('${'&#'}${loop.index };')">${'&#'}${loop.index };</a>
	</c:forEach>
	<a class="btn_04" onclick="queryByPrefix('其他')">其他</a>
</div>