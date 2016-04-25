<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>碩陽電子資源檢索</title>
<script type="text/javascript"
	src="<c:url value = '/resources/js/jquery-1.7.2.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value = '/resources/js/jquery.history.js'/>"></script>
<jsp:include page="/WEB-INF/jsp/layout/css.jsp" />
<script type="text/javascript">
	$(document).ready(function() {
		goURL("<c:url value = '/page/query.action'/>");
	});

	$(document).ready(function() {
		var url = "<c:url value = '/'/>";
		if (typeof (history.pushState) == "undefined") {
			History.pushState({
				"page" : url,
				"data" : ""
			}, url, url);
		} else {
			history.pushState({
				page : url,
				data : ""
			}, url, url);
		}
	});

	if (typeof (history.pushState) == "undefined") {
		window.onhashchange = function() {
			history.forward();
		};
	} else {
		window.addEventListener("popstate", function(e) {
			history.forward();
		});
	}
</script>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/WEB-INF/jsp/layout/top.jsp" />
		<jsp:include page="/WEB-INF/jsp/layout/header.jsp" />
		<div id="container"></div>
		<jsp:include page="/WEB-INF/jsp/layout/footer.jsp" />
	</div>
</body>
</html>
