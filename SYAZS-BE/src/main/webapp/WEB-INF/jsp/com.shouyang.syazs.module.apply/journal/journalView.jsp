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
</head>
<body>
	<c:choose>
		<c:when test="${empty successCount }">
			<table cellspacing="1" class="detail-table">
				<tbody>
					<tr>
						<th width="130">中文刊名</th>
						<td><esapi:encodeForHTML>${entity.chineseTitle }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">英文刊名<span class="required">(&#8226;)</span></th>
						<td><esapi:encodeForHTML>${entity.englishTitle }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">英文縮寫刊名</th>
						<td><esapi:encodeForHTML>${entity.abbreviationTitle }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">ISSN<span class="required">(&#8226;)</span></th>
						<td>${entity.issn }</td>
					</tr>
					<tr>
						<th width="130">語文</th>
						<td><esapi:encodeForHTML>${entity.languages }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">出版項</th>
						<td><esapi:encodeForHTML>${entity.publishName }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">出版年</th>
						<td><esapi:encodeForHTML>${entity.publishYear }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">刊別</th>
						<td><esapi:encodeForHTML>${entity.publication }</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">國會分類號</th>
						<td>${entity.congressClassification }</td>
					</tr>
					<tr>
						<th width="130">起始日</th>
						<td><esapi:encodeForHTML>${entity.resourcesBuyers.startDate}</esapi:encodeForHTML></td>
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
						<th width="130">資源種類</th>
						<td>${entity.resourcesBuyers.type}</td>
					</tr>
					<tr>
						<th width="130">資料庫中文題名</th>
						<td><esapi:encodeForHTML>${entity.resourcesBuyers.dbChtTitle}</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">資料庫英文題名</th>
						<td><esapi:encodeForHTML>${entity.resourcesBuyers.dbEngTitle}</esapi:encodeForHTML></td>
					</tr>
					<tr>
						<th width="130">購買單位名稱</th>
						<td><c:forEach var="item" items="${entity.customers}"
								varStatus="status">
								<div>${item.name}</div>
							</c:forEach></td>
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
	<s:if test="hasActionMessages()">
		<script language="javascript" type="text/javascript">
			var msg = "";
			<s:iterator value="actionMessages">msg += '<s:property escape="true"/>\n';
			</s:iterator>;
			goAlert('訊息', msg);
		</script>
	</s:if>
</body>
</html>