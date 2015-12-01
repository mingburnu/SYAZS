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
				+ "<esapi:encodeForJavaScript>${ds.entity.indexTerm}</esapi:encodeForJavaScript>".replace(/\&/g, "%26")
				+ "&entity.refSerNo=" + "${ds.entity.refSerNo}"
				+ "&pager.recordPoint=" + "${ds.pager.recordPoint}"
				+ "&pager.recordPerPage=" + "${ds.pager.recordPerPage}";
		$.ajax({
			url : url,
			data : data,
			success : function(result) {
				$("#container").html(result);
			}
		});
	}
	
	function link(serNo){
		var url="<%=request.getContextPath()%>"+"/crud/apply.database.click.action?entity.serNo="+serNo;
		window.open(url);
	}
</script>
<div id="main_b_box">
	<!-- 內容開始 -->
	<div class="detail">

		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="table_03">
			<tr>
				<td class="t_01">題名</td>
				<td class="t_02"><esapi:encodeForHTML>${entity.dbTitle }</esapi:encodeForHTML></td>
			</tr>
			<tr>
				<td class="t_01">URL</td>
				<td class="t_02"><a onclick="link(${entity.serNo });">${entity.url }</a></td>
			</tr>
			<c:if test="${not empty entity.includedSpecies}">
				<tr>
					<td class="t_01">類型</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.includedSpecies }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.content}">
				<tr>
					<td class="t_01">內容描述</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.content }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.publishName}">
				<tr>
					<td class="t_01">出版社</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.publishName }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.indexedYears }">
				<tr>
					<td class="t_01">收錄年代</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.indexedYears }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if
				test="${(not empty entity.resourcesBuyers.startDate)||(not empty entity.resourcesBuyers.maturityDate)}">
				<tr>
					<td class="t_01">起訂日期</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.resourcesBuyers.startDate }</esapi:encodeForHTML>~<esapi:encodeForHTML>${entity.resourcesBuyers.maturityDate }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.topic}">
				<tr>
					<td class="t_01">主題</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.topic }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.classification}">
				<tr>
					<td class="t_01">分類</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.classification }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<tr>
				<td class="t_01">館藏</td>
				<td class="t_02"><c:forEach items="${entity.referenceOwners }"
						var="owner">${owner.name }</c:forEach></td>
			</tr>
		</table>

		<div align="center">
			<a class="btn_01" href="javascript:goBack();">回 上 一 頁</a>
		</div>
	</div>
	<!-- 內容結束 -->
</div>