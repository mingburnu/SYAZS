<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	$(document).ready(function() {
		goURL("<c:url value = '/'/>crud/apply.classification.list.action");
	});
	closeDetail();
</script>
</head>
<body>
	<table cellspacing="1" class="detail-table">
		<tbody>
			<tr>
				<th width="130">用戶名稱</th>
				<td>${entity.classname }</td>
			</tr>
		</tbody>
	</table>

	<div class="detail-func-button">
		<a class="state-default" onclick="closeDetail_ToQuery();">關閉</a>&nbsp;
	</div>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>