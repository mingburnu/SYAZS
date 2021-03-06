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
						"form#apply_database_list input#listForm_currentPageHeader")
						.val() != null) {
					gotoPage($(
							"form#apply_database_list input#listForm_currentPageHeader")
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
						"form#apply_database_list input#listForm_currentPageHeader")
						.val() != null) {
					gotoPage($(
							"form#apply_database_list input#listForm_currentPageHeader")
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
			goDetail("<c:url value = '/'/>crud/apply.database.paginate.action?pager.currentPage=${pager.currentPage}&pager.recordPerPage=${pager.recordPerPage}");
		}
	</script>
</c:if>
</head>
<body>
	<c:choose>
		<c:when test="${empty successCount }">
			<script type="text/javascript">
				var observe;
				if (window.attachEvent) {
					observe = function(element, event, handler) {
						element.attachEvent('on' + event, handler);
					};
				} else {
					observe = function(element, event, handler) {
						element.addEventListener(event, handler, false);
					};
				}

				$(document).ready(function() {
					var text = document.getElementById('content');
					function resize() {
						text.style.height = 'auto';
						text.style.height = text.scrollHeight + 'px';
					}
					/* 0-timeout to get the already changed text */
					function delayedResize() {
						window.setTimeout(resize, 0);
					}
					observe(text, 'change', resize);
					observe(text, 'cut', delayedResize);
					observe(text, 'paste', delayedResize);
					observe(text, 'drop', delayedResize);
					observe(text, 'keydown', delayedResize);

					text.focus();
					resize();
				});
			</script>
			<table cellspacing="1" class="detail-table">
				<tbody>
					<tr>
						<th width="130">資料庫題名<span class="required">(&#8226;)</span></th>
						<td><esapi:encodeForHTML>${entity.dbTitle }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">UUID</th>
						<td>${entity.uuIdentifier }</td>
					</tr>
					<tr>
						<th width="130">出版社<span class="required">(&#8226;)</span></th>
						<td><esapi:encodeForHTML>${entity.publishName }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">語文</th>
						<td><esapi:encodeForHTML>${entity.languages }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">收錄種類</th>
						<td><esapi:encodeForHTML>${entity.includedSpecies }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">開放近用</th>
						<td><c:choose>
								<c:when test="${true eq entity.openAccess}">是</c:when>
								<c:otherwise>否</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<th width="130">URL<span class="required">(&#8226;)</span></th>
						<td><a href="${entity.url }" target="_blank">${entity.url }</a></td>
					</tr>
					<tr>
						<th width="130">出版時間差(embargo period)</th>
						<td><esapi:encodeForHTML>${entity.embargo }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">主題</th>
						<td><esapi:encodeForHTML>${entity.topic }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">分類</th>
						<td><esapi:encodeForHTML>${entity.classification }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">收錄年代</th>
						<td><esapi:encodeForHTML>${entity.indexedYears }</esapi:encodeForHTML></td>
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
					<tr>
						<th width="130">資源種類</th>
						<td>${entity.type}</td>
					</tr>
					<tr>
						<th width="130">內容</th>
						<td height="auto"><s:textarea id="content"
								value="%{entity.content}" readonly="true" /></td>
					</tr>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>成功筆數:${successCount}</c:otherwise>
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