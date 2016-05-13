<%@ page import="com.shouyang.syazs.module.apply.ebook.ISBN_Validator"%>
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
						.replace(/\&/g, "%26") + "&pager.recordPoint="
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
		var url = "<c:url value = '/'/>crud/apply.ebook.click.action?entity.serNo="
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
				<td class="t_01">題名</td>
				<td class="t_02"><a onclick="link('${entity.serNo }')"><esapi:encodeForHTML>${entity.bookName}</esapi:encodeForHTML></a></td>
			</tr>
			<c:if
				test="${(not empty entity.autherName) || (not empty entity.authers) }">
				<tr>
					<td class="t_01">作者</td>
					<td class="t_02"><c:choose>
							<c:when
								test="${(not empty entity.autherName) && (not empty entity.authers) }">
								<esapi:encodeForHTML>${entity.autherName}</esapi:encodeForHTML>,<esapi:encodeForHTML>${entity.authers}</esapi:encodeForHTML>
							</c:when>
							<c:otherwise>
								<esapi:encodeForHTML>${entity.autherName}</esapi:encodeForHTML>
								<esapi:encodeForHTML>${entity.authers}</esapi:encodeForHTML>
							</c:otherwise>
						</c:choose></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.publishName}">
				<tr>
					<td class="t_01">出版社</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.publishName}</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.uppeName}">
				<tr>
					<td class="t_01">系列叢書名</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.uppeName}</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.version}">
				<tr>
					<td class="t_01">版本</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.version}</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.isbn}">
				<tr>
					<td class="t_01">ISBN</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.isbn }</esapi:encodeForHTML></td>
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
					<td class="t_02"><s:property value="entity.pubDate" /></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.languages}">
				<tr>
					<td class="t_01">語文</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.languages}</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.classification}">
				<tr>
					<td class="t_01">${entity.classification.classname }</td>
					<td class="t_02">${entity.lcsCode}</td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.database}">
				<tr>
					<td class="t_01">資料庫</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.database.dbTitle }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<tr>
				<td class="t_01">資源類型</td>
				<td class="t_02">${entity.resourcesBuyers.category}</td>
			</tr>
			<tr>
				<td class="t_01">開放近用</td>
				<td class="t_02"><c:choose>
						<c:when test="${true eq entity.openAccess}">是</c:when>
						<c:otherwise>否</c:otherwise>
					</c:choose></td>
			</tr>
		</table>

		<div align="center">
			<a class="btn_01" href="javascript:goBack();">回 上 一 頁</a>
		</div>
	</div>
	<!-- 內容結束 -->
</div>