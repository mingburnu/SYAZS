<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>
<script type="text/javascript">
	function view(serNo) {
		var data = $("form:eq(0)").serialize() + "&entity.backURL="
				+ "${list}${prefix}";
		var url = "<c:url value = '/'/>crud/apply.database.view.action?entity.serNo="
				+ serNo + "&pager.recordPoint=" + "${ds.pager.recordPoint}";
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

	function goBack() {
		goURL("<c:url value='/' />page/database.action");
	}
</script>
<style>
.list td a:hover {
	cursor: pointer;
}
</style>
<div id="main_b_box">
	<!-- 內容開始 -->
	<div class="result">
		<div>
			<a class="btn_02" href="javascript:goBack();"><span>回 上 一
					層</span></a>
		</div>
		<c:choose>
			<c:when test="${not empty list}">
				<div class="pager">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" class="p_01"><s:form
									action="apply.database.list.action">
									共 <strong>${ds.pager.totalRecord}</strong>
									筆記錄， 每頁顯示筆數 <select name="pager.recordPerPage"
										id="apply_database_list_action_recordPerPage"
										onchange="upperChangeSize(this.value);">
										<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
										<option value="5">5</option>
										<option value="10">10</option>
										<option value="20">20</option>
										<option value="50">50</option>
										<option value="100">100</option>
									</select>
									<s:hidden name="entity.option" />
									<s:hidden name="entity.indexTerm" />
								</s:form></td>
							<td align="right" class="p_02"><jsp:include
									page="/WEB-INF/jsp/layout/pagination.jsp">
									<jsp:param name="namespace" value="/crud" />
									<jsp:param name="action" value="apply.database.list" />
									<jsp:param name="pager" value="${ds.pager}" />
								</jsp:include></td>
						</tr>
					</table>
				</div>
			</c:when>
			<c:when test="${not empty prefix}">
				<div class="pager">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" class="p_01"><s:form
									action="apply.database.prefix.action">
									共 <strong>${ds.pager.totalRecord}</strong>
									筆記錄， 每頁顯示筆數 <select name="pager.recordPerPage"
										id="apply_database_prefix_action_recordPerPage"
										onchange="upperChangeSize(this.value);">
										<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
										<option value="5">5</option>
										<option value="10">10</option>
										<option value="20">20</option>
										<option value="50">50</option>
										<option value="100">100</option>
									</select>
									<s:hidden name="entity.option" />
								</s:form></td>
							<td align="right" class="p_02"><jsp:include
									page="/WEB-INF/jsp/layout/pagination.jsp">
									<jsp:param name="namespace" value="/crud" />
									<jsp:param name="action" value="apply.database.prefix" />
									<jsp:param name="pager" value="${ds.pager}" />
								</jsp:include></td>
						</tr>
					</table>
				</div>
			</c:when>
		</c:choose>

		<div class="list">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr valign="top">
					<th>標題</th>
					<th>OA</th>
					<th width="38">詳細</th>
				</tr>
				<c:forEach var="item" items="${ds.results}" varStatus="status">
					<c:set var="num" scope="session" value="${(status.index+1)%2}" />
					<c:set var="orderInt" scope="session"
						value="${ds.pager.offset+(status.index+1)}" />
					<c:choose>
						<c:when test="${num > 0}">
							<tr valign="top">
								<td><a onclick="link('${item.serNo }')"><esapi:encodeForHTML>${item.dbTitle}</esapi:encodeForHTML></a></td>
								<td><c:if test="${item.openAccess }">V</c:if></td>
								<td><a onclick="view('${item.serNo }')">檢視</a></td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr valign="top" class="odd">
								<td><a onclick="link('${item.serNo }')"><esapi:encodeForHTML>${item.dbTitle}</esapi:encodeForHTML></a></td>
								<td><c:if test="${item.openAccess }">V</c:if></td>
								<td><a onclick="view('${item.serNo }')">檢視</a></td>
							</tr>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</table>
		</div>

		<c:choose>
			<c:when test="${not empty list}">
				<div class="pager">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" class="p_01"><s:form
									action="apply.database.list.action">
									共 <strong>${ds.pager.totalRecord}</strong>
									筆記錄， 每頁顯示筆數 <select name="pager.recordPerPage"
										id="apply_database_list_action_recordPerPage"
										onchange="bottomChangeSize(this.value);">
										<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
										<option value="5">5</option>
										<option value="10">10</option>
										<option value="20">20</option>
										<option value="50">50</option>
										<option value="100">100</option>
									</select>
									<s:hidden name="entity.option" />
									<s:hidden name="entity.indexTerm" />
								</s:form></td>
							<td align="right" class="p_02"><jsp:include
									page="/WEB-INF/jsp/layout/pagination.jsp">
									<jsp:param name="namespace" value="/crud" />
									<jsp:param name="action" value="apply.database.list" />
									<jsp:param name="pager" value="${ds.pager}" />
								</jsp:include></td>
						</tr>
					</table>
				</div>
			</c:when>
			<c:when test="${not empty prefix}">
				<div class="pager">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" class="p_01"><s:form
									action="apply.database.prefix.action">
									共 <strong>${ds.pager.totalRecord}</strong>
									筆記錄， 每頁顯示筆數 <select name="pager.recordPerPage"
										id="apply_database_prefix_action_recordPerPage"
										onchange="bottomChangeSize(this.value);">
										<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
										<option value="5">5</option>
										<option value="10">10</option>
										<option value="20">20</option>
										<option value="50">50</option>
										<option value="100">100</option>
									</select>
									<s:hidden name="entity.option" />
								</s:form></td>
							<td align="right" class="p_02"><jsp:include
									page="/WEB-INF/jsp/layout/pagination.jsp">
									<jsp:param name="namespace" value="/crud" />
									<jsp:param name="action" value="apply.database.prefix" />
									<jsp:param name="pager" value="${ds.pager}" />
								</jsp:include></td>
						</tr>
					</table>
				</div>
			</c:when>
		</c:choose>
		<div>
			<a class="btn_02" href="javascript:goBack();"><span>回 上 一
					層</span></a>
		</div>
	</div>
	<!-- 內容結束 -->
</div>