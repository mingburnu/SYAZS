<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<c:choose>
	<c:when test="${empty viewSerNo}">
		<script type="text/javascript">
			//關閉並更新上一層資料
			function closeDetail_ToQuery() {
				$("#div_Detail").hide();
				UI_Resize();
				if ($(
						"form#apply_accountNumber_list input#listForm_currentPageHeader")
						.val() != null) {
					gotoPage($(
							"form#apply_accountNumber_list input#listForm_currentPageHeader")
							.val());
					resetCloseDetail();
				} else {
					goSearch();
					resetCloseDetail();
				}
			}

			function closeDetail() {
				$("#div_Detail").hide();
				UI_Resize();
				if ($(
						"form#apply_accountNumber_list input#listForm_currentPageHeader")
						.val() != null) {
					gotoPage($(
							"form#apply_accountNumber_list input#listForm_currentPageHeader")
							.val());
					resetCloseDetail();
				} else {
					goSearch();
					resetCloseDetail();
				}
			}
		</script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
			function closeDetail_ToQuery() {
				closeDetail();
			}
		</script>
	</c:otherwise>
</c:choose>
</head>
<body>
	<c:choose>
		<c:when test="${empty successCount }">
			<table cellspacing="1" class="detail-table">
				<tbody>
					<tr>
						<th width="130">用戶代碼</th>
						<td><c:out value="${entity.userId }" /></td>

					</tr>
					<tr>
						<th width="130">用戶姓名</th>
						<td><c:out value="${entity.userName }" /></td>
					</tr>
					<tr>
						<th width="130">客戶名稱</th>
						<td><c:out value="${entity.customer.name }" /></td>
					</tr>
					<tr>
						<th width="130">Email</th>
						<td>${entity.customer.email }</td>
					</tr>
					<tr>
						<th width="130">權限</th>
						<td>${entity.role.role }</td>
					</tr>
					<tr>
						<th width="130">狀態</th>
						<td>${entity.status }</td>
					</tr>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
	成功筆數:${successCount}
	</c:otherwise>
	</c:choose>
	<div class="detail-func-button">
		<a class="state-default" onclick="closeDetail_ToQuery();">關閉</a>
	</div>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>