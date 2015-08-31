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
				if ($("form#apply_ebook_list input#listForm_currentPageHeader")
						.val() != null) {
					gotoPage($(
							"form#apply_ebook_list input#listForm_currentPageHeader")
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
				if ($("form#apply_ebook_list input#listForm_currentPageHeader")
						.val() != null) {
					gotoPage($(
							"form#apply_ebook_list input#listForm_currentPageHeader")
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
						<th width="130">書名<span class="required">(&#8226;)</span></th>
						<td><esapi:encodeForHTML>${entity.bookName }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">ISBN/13碼<span class="required">(&#8226;)</span></th>
						<td>${entity.isbn }</td>
					</tr>
					<tr>
						<th width="130">出版社</th>
						<td><esapi:encodeForHTML>${entity.publishName }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">第一作者</th>
						<td><esapi:encodeForHTML>${entity.autherName }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">次要作者</th>
						<td><esapi:encodeForHTML>${entity.authers }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">系列叢書名</th>
						<td><esapi:encodeForHTML>${entity.uppeName }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">出版日期</th>
						<td><esapi:encodeForHTML>${entity.pubDate }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">語文</th>
						<td><esapi:encodeForHTML>${entity.languages }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">版本</th>
						<td><esapi:encodeForHTML>${entity.version }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">中國圖書分類法</th>
						<td>${entity.cnClassBzStr }</td>
					</tr>
					<tr>
						<th width="130">杜威十進分類法</th>
						<td>${entity.bookInfoIntegral }</td>
					</tr>
					<tr>
						<th width="130">URL</th>
						<td><a href="${entity.url }" target="_blank">${entity.url }</a></td>
					</tr>
					<tr>
						<th width="130">公開資源</th>
						<td><c:choose>
								<c:when test="${true eq entity.openAccess}">是</c:when>
								<c:otherwise>否</c:otherwise>
							</c:choose></td>
					</tr>
					<c:choose>
						<c:when test="${empty entity.database }">
							<tr>
								<th width="130">起始日</th>
								<td><esapi:encodeForHTML>${entity.resourcesBuyers.startDate}</esapi:encodeForHTML>
								</td>
							</tr>
							<tr>
								<th width="130">到期日</th>
								<td><esapi:encodeForHTML>${entity.resourcesBuyers.maturityDate}</esapi:encodeForHTML></td>
							</tr>
							<tr>
								<th width="130">資源類型</th>
								<td>${entity.resourcesBuyers.category}</td>
							</tr>
							<tr>
								<th width="130">購買單位名稱</th>
								<td><c:forEach var="item" items="${entity.referenceOwners}">
										<div>${item.name}</div>
									</c:forEach></td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<th width="130">資料庫題名</th>
								<td><a
									href='<esapi:encodeForHTMLAttribute>${entity.database.url }</esapi:encodeForHTMLAttribute>'
									target="_blank">${entity.database.dbTitle}</a></td>
							</tr>
							<tr>
								<th width="130">起始日</th>
								<td><esapi:encodeForHTML>${entity.database.resourcesBuyers.startDate}</esapi:encodeForHTML>
								</td>
							</tr>
							<tr>
								<th width="130">到期日</th>
								<td><esapi:encodeForHTML>${entity.database.resourcesBuyers.maturityDate}</esapi:encodeForHTML></td>
							</tr>
							<tr>
								<th width="130">資源類型</th>
								<td>${entity.database.resourcesBuyers.category}</td>
							</tr>
							<tr>
								<th width="130">購買單位名稱</th>
								<td><c:forEach var="item"
										items="${entity.database.referenceOwners}">
										<div>${item.name}</div>
									</c:forEach></td>
							</tr>
						</c:otherwise>
					</c:choose>
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