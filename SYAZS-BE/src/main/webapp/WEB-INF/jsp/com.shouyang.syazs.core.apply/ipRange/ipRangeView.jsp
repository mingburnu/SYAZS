<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	//關閉並更新上一層資料
	function closeDetail_ToQuery() {
		$("#div_Detail_2").hide();
		UI_Resize();
		gotoPage_detail($(
				"form#apply_ipRange_list input#listForm_currentPageHeader")
				.val());
		resetCloseDetail_2();
	}

	function closeDetail_2() {
		$("#div_Detail_2").hide();
		UI_Resize();
		gotoPage_detail($(
				"form#apply_ipRange_list input#listForm_currentPageHeader")
				.val());
		resetCloseDetail_2();
	}
</script>
</head>
<body>
	<table cellspacing="1" class="detail-table">
		<tbody>
			<c:if test="${entity.listNo > 0}">
				<tr>
					<th>ID</th>
					<td>${entity.listNo}</td>
				</tr>
			</c:if>
			<tr>
				<th>IP Range<span class="required">(&#8226;)</span></th>
				<td>${entity.ipRangeStart }~${entity.ipRangeEnd}</td>
			</tr>
		</tbody>
	</table>
	<div class="detail-func-button">
		<a class="state-default" onclick="closeDetail_ToQuery();">關閉</a>
	</div>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>