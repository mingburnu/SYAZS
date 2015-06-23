<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
input:-webkit-autofill {
	-webkit-box-shadow: 0 0 0px 1000px #ffffff inset;
}

input#listForm_currentPageHeader,input#listForm_currentRowHeader {
	width: 11.6%;
}

<%
String browser = request.getHeader ("User-Agent");
if (browser.contains ("MSIE") || browser.contains ("Trident")
			 || browser.contains ("Firefox")) {
		out.println(".page-box table tbody td{width: 287px;}");
		out.println(".page-box table tbody td a {position: relative; right: -148px;}");
}
if (browser.contains ("MSIE 7.0")) {
	out.println(".page-box table tbody td a {position:relative; right: -26px;}");
}
%>
</style>
