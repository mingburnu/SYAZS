<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
	<c:when test="${(not empty tip && not empty repeat)}">${tip }，${repeat }</c:when>
	<c:otherwise>${tip }${repeat }</c:otherwise>
</c:choose>
