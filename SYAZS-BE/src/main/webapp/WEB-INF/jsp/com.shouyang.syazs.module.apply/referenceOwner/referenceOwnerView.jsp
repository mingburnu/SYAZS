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
<c:choose>
	<c:when test="${empty viewSerNo}">
		<script type="text/javascript">
			//關閉並更新上一層資料
			function closeDetail_ToQuery() {
				$("#div_Detail").hide();
				UI_Resize();
				if ($(
						"form#apply_referenceOwner_list input#listForm_currentPageHeader")
						.val() != null) {
					gotoPage($(
							"form#apply_referenceOwner_list input#listForm_currentPageHeader")
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
						"form#apply_referenceOwner_list input#listForm_currentPageHeader")
						.val() != null) {
					gotoPage($(
							"form#apply_referenceOwner_list input#listForm_currentPageHeader")
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
<c:if test="${empty entity.serNo}">
	<script type="text/javascript">
		function reimport() {
			goDetail(
					"<c:url value = '/'/>crud/apply.referenceOwner.paginate.action?pager.currentPage=${pager.currentPage}&pager.recordPerPage=${pager.recordPerPage}",
					"擁有者-匯入");
		}
	</script>
</c:if>
</head>
<body>
	<c:choose>
		<c:when test="${empty successCount }">
			<table cellspacing="1" class="detail-table">
				<tbody>
					<tr>
						<th width="130">用戶名稱</th>
						<td>${entity.name }</td>
					</tr>
					<tr>
						<th width="130">用戶英文名稱</th>
						<td><esapi:encodeForHTML>${entity.engName }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">聯絡人</th>
						<td><esapi:encodeForHTML>${entity.contactUserName }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">地址</th>
						<td><esapi:encodeForHTML>${entity.address }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">電話</th>
						<td>${entity.tel }</td>
					</tr>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
	成功筆數:${successCount}
	</c:otherwise>
	</c:choose>
	<div class="detail-func-button">
		<a class="state-default" onclick="closeDetail_ToQuery();">關閉</a>&nbsp;
		<c:if test="${empty entity.serNo}">
			<a class="state-default" onclick="reImport();">繼續匯入</a>
		</c:if>
	</div>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>