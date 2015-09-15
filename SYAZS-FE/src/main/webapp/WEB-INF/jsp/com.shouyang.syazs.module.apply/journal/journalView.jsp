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
			<c:if test="${not empty entity.englishTitle}">
				<tr>
					<td class="t_01">刊名</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.englishTitle }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.abbreviationTitle}">
				<tr>
					<td class="t_01">縮寫刊名</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.abbreviationTitle }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.issn}">
				<tr>
					<td class="t_01">ISSN</td>
					<td class="t_02">${entity.issn }</td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.publishName}">
				<tr>
					<td class="t_01">出版項</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.publishName }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if
				test="${(not empty entity.resourcesBuyers.dbChtTitle) || (not empty entity.resourcesBuyers.dbEngTitle)}">
				<tr>
					<td class="t_01">資料庫</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.resourcesBuyers.dbEngTitle }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${entity.version > 0 }">
				<tr>
					<td class="t_01">版本</td>
					<td class="t_02">${entity.version }</td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.languages}">
				<tr>
					<td class="t_01">語文</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.languages }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.publishYear}">
				<tr>
					<td class="t_01">出版年</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.publishYear }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.publication}">
				<tr>
					<td class="t_01">刊別</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.publication }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.congressClassification}">
				<tr>
					<td class="t_01">國會分類號</td>
					<td class="t_02">${entity.congressClassification }</td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.caption}">
				<tr>
					<td class="t_01">標題</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.caption }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.numB}">
				<tr>
					<td class="t_01">編號</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.numB }</esapi:encodeForHTML></td>
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