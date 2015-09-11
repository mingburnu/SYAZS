<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="nl.bitwalker.useragentutils.*"%>
<style>
input:-webkit-autofill {
	-webkit-box-shadow: 0 0 0px 1000px #ffffff inset;
}

input#listForm_currentPageHeader,input#listForm_currentRowHeader {
	width: 11.6%;
}

span#span-num-tip,span#span-title-name-tip, span#span-tip {
	color: red;
}

textarea#content {
	border: 0px;
	background-color: white;
	color: black;
	padding: 0px;
	width: 100%;
	resize: none;
}

<%
UserAgent userAgent = UserAgent.parseUserAgentString (request.getHeader
	("User-Agent"));
if (userAgent.getBrowser ().getGroup ().equals (Browser.MOZILLA )
		 || userAgent.getBrowser ().getGroup ().equals (Browser.FIREFOX )
		 || userAgent.getBrowser ().getGroup ().equals (Browser.IE )) {
	out.println(".page-box table tbody td {width:287px;}");
    out.println (".page-box table tbody td a {	position: relative;right: -148px;}");
}
if (userAgent.getBrowser ().getGroup ().equals (Browser.IE ) 
		 && userAgent.getBrowserVersion ().getVersion ().equals ("7.0 ")){
	out.println(".page-box table tbody td a {position:relative;right: -48px;}");
}
%>
</style>
