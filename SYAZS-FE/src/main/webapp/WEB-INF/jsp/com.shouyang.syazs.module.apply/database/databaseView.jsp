<%@page import="org.owasp.esapi.ESAPI"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>
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
		var url = "<c:url value = '/'/>crud/apply.database.click.action?entity.serNo="
				+ serNo;
		window.open(url);
	}
</script>
<div id="main_b_box">
	<!-- 內容開始 -->
	<div class="detail">
		<div>
			<a class="btn_01" href="javascript:goBack();">回 上 一 層</a>
		</div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="table_03">
			<tr>
				<td class="t_01">題名</td>
				<td class="t_02"><esapi:encodeForHTML>${entity.dbTitle }</esapi:encodeForHTML></td>
			</tr>
			<tr>
				<td class="t_01">URL</td>
				<td class="t_02"><a onclick="link('${entity.serNo }');">${entity.url }</a></td>
			</tr>
			<c:if test="${not empty entity.languages}">
				<tr>
					<td class="t_01">語文</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.languages }</esapi:encodeForHTML></td>
				</tr>
			</c:if>
			<c:if test="${not empty entity.includedSpecies}">
				<tr>
					<td class="t_01">類型</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.includedSpecies }</esapi:encodeForHTML></td>
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
			<c:if test="${not empty entity.content}">
				<tr>
					<td class="t_01">內容描述</td>
					<td class="t_02"><s:textarea id="content"
							value="%{entity.content}" readonly="true" /></td>
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
			<c:if test="${not empty entity.embargo }">
				<tr>
					<td class="t_01">全文取得授權刊期(embargo period)</td>
					<td class="t_02"><esapi:encodeForHTML>${entity.embargo }</esapi:encodeForHTML></td>
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
			<tr>
				<td class="t_01">資源種類</td>
				<td class="t_02"><s:property value="entity.type" /></td>
			</tr>
			<tr>
				<td class="t_01">開放近用</td>
				<td class="t_02"><c:choose>
						<c:when test="${true eq entity.openAccess}">是</c:when>
						<c:otherwise>否</c:otherwise>
					</c:choose></td>
			</tr>
		</table>

		<div>
			<a class="btn_01" href="javascript:goBack();">回 上 一 層</a>
		</div>
	</div>
	<!-- 內容結束 -->
</div>