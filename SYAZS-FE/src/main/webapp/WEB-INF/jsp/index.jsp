<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>碩陽電子資源檢索</title>
<jsp:include page="/WEB-INF/jsp/layout/css.jsp" />
<script type="text/javascript"
	src="<c:url value = '/'/>resources/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		goURL("<c:url value = '/'/>page/query.action");
	});
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
