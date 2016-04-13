<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
						"form#apply_journal_list input#listForm_currentPageHeader")
						.val() != null) {
					gotoPage($(
							"form#apply_journal_list input#listForm_currentPageHeader")
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
						"form#apply_journal_list input#listForm_currentPageHeader")
						.val() != null) {
					gotoPage($(
							"form#apply_journal_list input#listForm_currentPageHeader")
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
			goDetail("<c:url value = '/'/>crud/apply.journal.paginate.action?pager.currentPage=${pager.currentPage}&pager.recordPerPage=${pager.recordPerPage}");
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
						<th width="130">刊名<span class="required">(&#8226;)</span></th>
						<td><esapi:encodeForHTML>${entity.title }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">英文縮寫刊名</th>
						<td><esapi:encodeForHTML>${entity.abbreviationTitle }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">ISSN<span class="required">(&#8226;)</span></th>
						<td>${fn:substring(entity.issn, 0, 4)}${fn:substring(entity.issn, 4, 8)}</td>
					</tr>
					<tr>
						<th width="130">語文</th>
						<td><esapi:encodeForHTML>${entity.languages }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">出版社<span class="required">(&#8226;)</span></th>
						<td><esapi:encodeForHTML>${entity.publishName }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">出版年</th>
						<td><esapi:encodeForHTML>${entity.publishYear }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">標題</th>
						<td><esapi:encodeForHTML>${entity.caption }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">編號</th>
						<td><esapi:encodeForHTML>${entity.numB }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">刊別</th>
						<td><esapi:encodeForHTML>${entity.publication }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">分類法</th>
						<td>${entity.classification.classname }</td>
					</tr>
					<tr>
						<th width="130">分類碼</th>
						<td>${entity.lcsCode }</td>
					</tr>
					<tr>
						<th width="130">版本</th>
						<td><esapi:encodeForHTML>${entity.version }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">全文取得授權刊期(embargo period)</th>
						<td><esapi:encodeForHTML>${entity.embargo }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">URL<span class="required">(&#8226;)</span></th>
						<td><a href="${entity.url }" target="_blank">${entity.url }</a></td>
					</tr>
					<tr>
						<th width="130">開放近用</th>
						<td><c:choose>
								<c:when test="${true eq entity.openAccess}">是</c:when>
								<c:otherwise>否</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<th width="130">資料庫題名</th>
						<td><a
							href='<esapi:encodeForHTMLAttribute>${entity.database.url }</esapi:encodeForHTMLAttribute>'
							target="_blank">${entity.database.dbTitle}</a></td>
					</tr>
					<tr>
						<th width="130">起始日</th>
						<td><s:property value="entity.startDate" /></td>
					</tr>
					<tr>
						<th width="130">到期日</th>
						<td><s:property value="entity.maturityDate" /></td>
					</tr>
					<tr>
						<th width="130">資源類型</th>
						<td>${entity.resourcesBuyers.category}</td>
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
			<a class="state-default" onclick="reimport();">繼續匯入</a>
		</c:if>
	</div>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>