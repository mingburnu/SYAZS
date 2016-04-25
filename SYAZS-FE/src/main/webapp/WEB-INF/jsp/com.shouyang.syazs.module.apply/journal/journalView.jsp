<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>
<script type="text/javascript">
	function goBack() {
		var url = "${entity.backURL}";
		var data = "entity.option="
				+ "<esapi:encodeForJavaScript>${ds.entity.option}</esapi:encodeForJavaScript>"
				+ "&entity.indexTerm="
				+ "<esapi:encodeForJavaScript>${ds.entity.indexTerm}</esapi:encodeForJavaScript>"
						.replace(/\&/g, "%26") + "&entity.refSerNo="
				+ "${ds.entity.refSerNo}" + "&pager.recordPoint="
				+ "${ds.pager.recordPoint}" + "&pager.recordPerPage="
				+ "${ds.pager.recordPerPage}";
		$.ajax({
			url : url,
			data : data,
			success : function(result) {
				$("#container").html(result);
			}
		});
	}

	function link(serNo) {
		var url = "<c:url value = '/'/>crud/apply.journal.click.action?entity.serNo="
				+ serNo;
		window.open(url);
	}
</script>
<div id="main_b_box">
	<!-- 內容開始 -->
	<div class="detail">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="table_03">
			<tr>
				<td class="t_01">刊名</td>
				<td class="t_02"><a onclick="link('${entity.serNo }')"><esapi:encodeForHTML>${entity.title }</esapi:encodeForHTML></a></td>
			</tr>
			<c:if test="${not empty entity.abbreviationTitle}">
				<tr>
					<td class="t_01">縮寫刊名</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.abbreviationTitle }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.issn}">
				<tr>
					<td class="t_01">ISSN</td>
					<td class="t_02">${fn:substring(entity.issn, 0, 4)}-${fn:substring(entity.issn, 4, 8)}</td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.publishName}">
				<tr>
					<td class="t_01">出版項</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.publishName }</esapi:encodeForHTML></td>
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
			<c:if test="${not empty entity.classification}">
				<tr>
					<td class="t_01">${entity.classification.classname }</td>
					<td class="t_02">${entity.lcsCode}</td>
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
			<c:if test="${not empty entity.database}">
				<tr>
					<td class="t_01">資料庫</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.database.dbTitle }</esapi:encodeForHTML></td>
				</tr>

			</c:if>
			<c:if
				test="${(not empty entity.startDate)||(not empty entity.maturityDate)}">
				<tr>
					<td class="t_01">起訂日期</td>
					<td class="t_02"><s:property value="entity.startDate" />~<s:property
							value="entity.maturityDate" /></td>
				</tr>
			</c:if>
			<tr>
				<td class="t_01">資源類型</td>
				<td class="t_02">${entity.resourcesBuyers.category}</td>
			</tr>
		</table>

		<div align="center">
			<a class="btn_01" href="javascript:goBack();">回 上 一 頁</a>
		</div>
	</div>
	<!-- 內容結束 -->
</div>