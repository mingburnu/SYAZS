<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>
<script type="text/javascript">
function view(serNo){
	var data=$("form:eq(0)").serialize()+"&entity.backURL="+"${list}${all}${owner}";
	var url="<%=request.getContextPath()%>"+"/crud/apply.database.view.action?entity.serNo="+serNo+"&pager.recordPoint="+"${ds.pager.recordPoint}";
	$.ajax({
		url: url,
		data: data,
		success: function(result){
			$("#container").html(result);
		}
	});
}

function link(serNo){
	var url="<%=request.getContextPath()%>"+"/crud/apply.database.click.action?entity.serNo="+serNo;
	window.open(url);
}

function goBack() {
	<c:choose>
	<c:when test="${not empty list}">goURL("<c:url value='/' />page/query.action");</c:when>
	<c:otherwise>goURL("<c:url value='/' />crud/apply.referenceOwner.list.action?entity.indexTerm=<esapi:encodeForJavaScript>${entityRecord.indexTerm}</esapi:encodeForJavaScript>".replace(/\&/g, "%26")+"&pager.recordPoint=${pagerRecord.recordPoint}&pager.recordPerPage=${pagerRecord.recordPerPage}");</c:otherwise>
	</c:choose>
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
									<s:hidden name="entity.indexTerm" />
								</s:form></td>
							<td align="right" class="p_02"><c:if
									test="${ds.pager.totalRecord > 0 }"><jsp:include
										page="/WEB-INF/jsp/layout/pagination.jsp">
										<jsp:param name="namespace" value="/crud" />
										<jsp:param name="action" value="apply.database.list" />
										<jsp:param name="pager" value="${ds.pager}" />
									</jsp:include></c:if></td>
						</tr>
					</table>
				</div>
			</c:when>
			<c:when test="${not empty owner}">
				<div class="pager">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" class="p_01"><s:form
									action="apply.database.owner.action">
									共 <strong>${ds.pager.totalRecord}</strong>
												筆記錄， 每頁顯示筆數 <select name="pager.recordPerPage"
										id="apply_database_owner_action_recordPerPage"
										onchange="upperChangeSize(this.value);">
										<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
										<option value="5">5</option>
										<option value="10">10</option>
										<option value="20">20</option>
										<option value="50">50</option>
										<option value="100">100</option>
									</select>
									<s:hidden name="entity.refSerNo" value="%{ds.entity.refSerNo}" />
								</s:form></td>
							<td align="right" class="p_02"><c:if
									test="${ds.pager.totalRecord > 0 }"><jsp:include
										page="/WEB-INF/jsp/layout/pagination.jsp">
										<jsp:param name="namespace" value="/crud" />
										<jsp:param name="action" value="apply.database.owner" />
										<jsp:param name="pager" value="${ds.pager}" />
									</jsp:include></c:if></td>
						</tr>
					</table>
				</div>
			</c:when>
		</c:choose>

		<c:choose>
			<c:when test="${(not empty list) || (not empty owner)}">
				<div class="list">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr valign="top">
							<th width="38">序號</th>
							<th>題名</th>
							<th>OA</th>
							<th width="38">詳細</th>
							<th>出版社</th>
							<th>收錄年代</th>
						</tr>
						<c:forEach var="item" items="${ds.results}" varStatus="status">
							<c:set var="num" scope="session" value="${(status.index+1)%2}" />
							<c:set var="orderInt" scope="session"
								value="${ds.pager.offset+(status.index+1)}" />
							<c:choose>
								<c:when test="${num > 0}">
									<tr valign="top">
										<td>${orderInt}</td>
										<td><a onclick="link(${item.serNo})"><esapi:encodeForHTML>${item.dbTitle}</esapi:encodeForHTML></a></td>
										<td><c:if test="${item.openAccess }">V</c:if></td>
										<td><a onclick="view(${item.serNo})">檢視</a></td>
										<td><esapi:encodeForHTML>${item.publishName}</esapi:encodeForHTML></td>
										<td><c:choose>
												<c:when test="${not empty item.indexedYears }">
													<esapi:encodeForHTML>${item.indexedYears}</esapi:encodeForHTML>
												</c:when>
												<c:otherwise>N/A</c:otherwise>
											</c:choose></td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr valign="top" class="odd">
										<td>${orderInt}</td>
										<td><a onclick="link(${item.serNo})"><esapi:encodeForHTML>${item.dbTitle}</esapi:encodeForHTML></a></td>
										<td><c:if test="${item.openAccess }">V</c:if></td>
										<td><a onclick="view(${item.serNo})">檢視</a></td>
										<td><esapi:encodeForHTML>${item.publishName}</esapi:encodeForHTML></td>
										<td><c:choose>
												<c:when test="${not empty item.indexedYears }">
													<esapi:encodeForHTML>${item.indexedYears}</esapi:encodeForHTML>
												</c:when>
												<c:otherwise>N/A</c:otherwise>
											</c:choose></td>
									</tr>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</table>
				</div>
			</c:when>
			<c:otherwise>
				<div class="query_db_list">
					<c:forEach var="item" items="${ds.results}" varStatus="status">
						<c:set var="num" scope="session" value="${(status.index+1)%2}" />
						<c:set var="orderInt" scope="session"
							value="${ds.pager.offset+(status.index+1)}" />
						<c:choose>
							<c:when test="${num > 0}">
								<div>
									<esapi:encodeForHTML>${item.dbTitle}</esapi:encodeForHTML>
									<br> <a onclick="link(${item.serNo });">${item.url }</a>
								</div>
							</c:when>
							<c:otherwise>
								<div class="odd">
									<esapi:encodeForHTML>${item.dbTitle}</esapi:encodeForHTML>
									<br> <a onclick="link(${item.serNo });">${item.url }</a>
								</div>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>

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
									<s:hidden name="entity.indexTerm" />
								</s:form></td>
							<td align="right" class="p_02"><c:if
									test="${ds.pager.totalRecord > 0 }"><jsp:include
										page="/WEB-INF/jsp/layout/pagination.jsp">
										<jsp:param name="namespace" value="/crud" />
										<jsp:param name="action" value="apply.database.list" />
										<jsp:param name="pager" value="${ds.pager}" />
									</jsp:include></c:if></td>
						</tr>
					</table>
				</div>
			</c:when>
			<c:when test="${not empty owner}">
				<div class="pager">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" class="p_01"><s:form
									action="apply.database.owner.action">
									共 <strong>${ds.pager.totalRecord}</strong>
									筆記錄， 每頁顯示筆數 <select name="pager.recordPerPage"
										id="apply_database_owner_action_recordPerPage"
										onchange="bottomChangeSize(this.value);">
										<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
										<option value="5">5</option>
										<option value="10">10</option>
										<option value="20">20</option>
										<option value="50">50</option>
										<option value="100">100</option>
									</select>
									<s:hidden name="entity.refSerNo" value="%{ds.entity.refSerNo}" />
								</s:form></td>
							<td align="right" class="p_02"><c:if
									test="${ds.pager.totalRecord > 0 }"><jsp:include
										page="/WEB-INF/jsp/layout/pagination.jsp">
										<jsp:param name="namespace" value="/crud" />
										<jsp:param name="action" value="apply.database.owner" />
										<jsp:param name="pager" value="${ds.pager}" />
									</jsp:include></c:if></td>
						</tr>
					</table>
				</div>
			</c:when>
		</c:choose>
		<div class="bottom">
			<a class="btn_02" href="javascript:goBack();"><span>回 上 一
					頁</span></a>
		</div>
	</div>
	<!-- 內容結束 -->
</div>