<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>
<script type="text/javascript">
	function goBack() {
		var url = "${entity.backURL}";
		var data = "entity.option="
				+ "<esapi:encodeForJavaScript>${ds.entity.option}</esapi:encodeForJavaScript>"
				+ "&entity.indexTerm="
				+ "<esapi:encodeForJavaScript>${ds.entity.indexTerm}</esapi:encodeForJavaScript>"
				+ "&entity.cusSerNo=" + "${ds.entity.cusSerNo}"
				+ "&pager.recordPoint=" + "${ds.pager.recordPoint}"
				+ "&pager.recordPerPage=" + "${ds.pager.recordPerPage}"
				+ "&pager.offset=" + "${ds.pager.offset}";
		$.ajax({
			url : url,
			data : data,
			success : function(result) {
				$("#container").html(result);
			}
		});
	}
</script>
<div id="main_b_box">
	<!-- 內容開始 -->
	<div class="detail">

		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="table_03">
			<c:if test="${not empty entity.bookName}">
				<tr>
					<td class="t_01">題名</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.bookName}</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if
				test="${(not empty entity.autherName) || (not empty entity.authers) }">
				<tr>
					<td class="t_01">作者</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.autherName}</esapi:encodeForHTML>,<esapi:encodeForHTML>${entity.authers}</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.publishName}">
				<tr>
					<td class="t_01">出版社</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.publishName}</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${entity.version > 0}">
				<tr>
					<td class="t_01">版本</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.version}</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.isbn}">
				<tr>
					<td class="t_01">ISBN</td>
					<td class="t_02">${entity.isbn}</td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.style}">
				<tr>
					<td class="t_01">類型</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.style}</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.publication}">
				<tr>
					<td class="t_01">出版地</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.publication}</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.pubDate}">
				<tr>
					<td class="t_01">出版日期</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.pubDate}</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.languages}">
				<tr>
					<td class="t_01">語文</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.languages}</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.bookInfoIntegral}">
				<tr>
					<td class="t_01">美國國家<BR />圖書館分類
					</td>
					<td class="t_02">${entity.bookInfoIntegral}</td>
				</tr>
			</c:if>
			<c:if test="${not empty ownerNames}">
				<tr>
					<td class="t_01">館藏</td>
					<td class="t_02">${ownerNames }</td>
				</tr>
			</c:if>
		</table>

		<div align="center">
			<a class="btn_01" href="javascript:goBack();">回 上 一 頁</a>
		</div>
	</div>
	<!-- 內容結束 -->
</div>