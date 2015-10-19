<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>
<script type="text/javascript">
	function owner(url) {
		$.ajax({
			url : url,
			success : function(result) {
				$("#container").html(result);
			}
		});
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

		<c:if test="${not empty list}">
			<div class="pager">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td align="left" class="p_01"><s:form
								action="apply.referenceOwner.list.action">
								共 <strong>${ds.pager.totalRecord}</strong>
											筆記錄， 每頁顯示筆數 <select name="pager.recordPerPage"
									id="apply_referenceOwner_list_action_recordPerPage"
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
									<jsp:param name="action" value="apply.referenceOwner.list" />
									<jsp:param name="pager" value="${ds.pager}" />
								</jsp:include></c:if></td>
					</tr>
				</table>
			</div>
		</c:if>

		<div class="list">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr valign="top">
					<th width="40">序號</th>
					<th width="327">單位名稱 / 聯絡人</th>
					<th width="292">電話</th>
					<th width="67">資料庫</th>
					<th width="67">電子書</th>
					<th width="47">期刊</th>
				</tr>
				<c:forEach var="item" items="${ds.results}" varStatus="status">
					<c:set var="num" scope="session" value="${(status.index+1)%2}" />
					<c:set var="orderInt" scope="session"
						value="${ds.pager.offset+(status.index+1)}" />
					<c:set var="ownJorunal">
						<s:url namespace="/crud" action="apply.journal.owner">
							<s:param name="entity.refSerNo">${item.serNo}</s:param>
						</s:url>
					</c:set>
					<c:set var="ownDb">
						<s:url namespace="/crud" action="apply.database.owner">
							<s:param name="entity.refSerNo">${item.serNo}</s:param>
						</s:url>
					</c:set>
					<c:set var="ownEbook">
						<s:url namespace="/crud" action="apply.ebook.owner">
							<s:param name="entity.refSerNo">${item.serNo}</s:param>
						</s:url>
					</c:set>
					<c:set var="ebookAmount" value="${fn:length(item.ebooks)}" />
					<c:set var="journalAmount" value="${fn:length(item.journals)}" />
					<c:forEach items="${item.databases }" var="database">
						<c:set var="ebookAmount"
							value="${ebookAmount +  fn:length(database.ebooks)}" />
						<c:set var="journalAmount"
							value="${journalAmount +  fn:length(database.journals)}" />
					</c:forEach>
					<c:choose>
						<c:when test="${num > 0}">
							<tr valign="top">
								<td>${orderInt}</td>
								<td><div>
										<esapi:encodeForHTML>${item.name}</esapi:encodeForHTML>
									</div>
									<div>
										<esapi:encodeForHTML>${item.contactUserName}</esapi:encodeForHTML>
									</div></td>
								<td><div>${item.tel}</div></td>
								<td><c:choose>
										<c:when test="${fn:length(item.databases) > 0 }">
											<a onclick="owner('${ownDb}')">${fn:length(item.databases)}</a>
										</c:when>
										<c:otherwise>${fn:length(item.databases)}</c:otherwise>
									</c:choose></td>
								<td><c:choose>
										<c:when test="${ebookAmount > 0 }">
											<a onclick="owner('${ownEbook}')">${ebookAmount }</a>
										</c:when>
										<c:otherwise>${ebookAmount}</c:otherwise>
									</c:choose></td>
								<td><c:choose>
										<c:when test="${journalAmount > 0 }">
											<a onclick="owner('${ownJorunal}')">${journalAmount}</a>
										</c:when>
										<c:otherwise>${journalAmount}</c:otherwise>
									</c:choose></td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr valign="top" class="odd">
								<td>${orderInt}</td>
								<td><div>
										<esapi:encodeForHTML>${item.name}</esapi:encodeForHTML>
									</div>
									<div>
										<esapi:encodeForHTML>${item.contactUserName}</esapi:encodeForHTML>
									</div></td>
								<td><div>${item.tel}</div></td>
								<td><c:choose>
										<c:when test="${fn:length(item.databases) > 0 }">
											<a onclick="owner('${ownDb}')">${fn:length(item.databases)}</a>
										</c:when>
										<c:otherwise>${fn:length(item.databases)}</c:otherwise>
									</c:choose></td>
								<td><c:choose>
										<c:when test="${ebookAmount > 0 }">
											<a onclick="owner('${ownEbook}')">${ebookAmount }</a>
										</c:when>
										<c:otherwise>${ebookAmount}</c:otherwise>
									</c:choose></td>
								<td><c:choose>
										<c:when test="${journalAmount > 0 }">
											<a onclick="owner('${ownJorunal}')">${journalAmount}</a>
										</c:when>
										<c:otherwise>${journalAmount}</c:otherwise>
									</c:choose></td>
							</tr>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</table>
		</div>

		<c:if test="${not empty list}">
			<div class="pager">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td align="left" class="p_01"><s:form
								action="apply.referenceOwner.list.action">
								共 <strong>${ds.pager.totalRecord}</strong>
											筆記錄， 每頁顯示筆數 <select name="pager.recordPerPage"
									id="apply_referenceOwner_list_action_recordPerPage"
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
									<jsp:param name="action" value="apply.referenceOwner.action" />
									<jsp:param name="pager" value="${ds.pager}" />
								</jsp:include></c:if></td>
					</tr>
				</table>
			</div>
		</c:if>

	</div>
	<!-- 內容結束 -->
</div>