<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<c:set var="pageFactor"
	value="${ds.pager.totalRecord/ds.pager.recordPerPage}" />
<c:set var="totalPage">
	<fmt:formatNumber type="number" pattern="#"
		value="${pageFactor+(1-(pageFactor%1))%1}" />
</c:set>
<script type="text/javascript">
	<c:choose>
	<c:when test="${not empty keywords }">
	function goSearch() {
		goMain("<c:url value = '/'/>crud/apply.feLogs.list.action",
				"#apply_feLogs_list", "");
	}

	//匯出
	function goExport() {
		var data = $("#apply_feLogs_list").serialize();
		var url = '<c:url value = '/'/>crud/apply.feLogs.exportKeyword.action?'
				+ data;
		if ($("input#customerSerNo").attr("checked")) {
			var customerSerNo = $("input#customerSerNo").val();
			if (customerSerNo != null && customerSerNo > 0) {
				$.fileDownload(url, {
					failCallback : function(html, url) {
						goAlert("訊息", "請正確填寫機構名稱");
					}
				});
			} else {
				goAlert("訊息", "請正確填寫機構名稱");
			}
		} else {
			$.fileDownload(url, {
				failCallback : function(html, url) {
					goAlert("訊息", "請正確填寫機構名稱");
				}
			});
		}
	}
	</c:when>
	<c:when test="${not empty clicks }">
	function goSearch() {
		goMain("<c:url value = '/'/>crud/apply.feLogs.link.action",
				"#apply_feLogs_link", "");
	}

	//匯出
	function goExport() {
		var data = $("#apply_feLogs_link").serialize();
		var url = "<c:url value = '/'/>crud/apply.feLogs.exportClick.action?"
				+ data;

		if ($("input#customerSerNo").attr("checked")) {
			var customerSerNo = $("input#customerSerNo").val();
			if (customerSerNo != null && customerSerNo > 0) {
				$.fileDownload(url, {
					failCallback : function(html, url) {
						goAlert("訊息", "請正確填寫機構名稱");
					}
				});
			} else {
				goAlert("訊息", "請正確填寫機構名稱");
			}
		} else {
			$.fileDownload(url, {
				failCallback : function(html, url) {
					goAlert("訊息", "請正確填寫機構名稱");
				}
			});
		}
	}
	</c:when>
	</c:choose>
</script>
</head>
<body>
	<c:choose>
		<c:when test="${not empty keywords }">
			<s:form action="apply.feLogs.list" namespace="/crud" method="post"
				onsubmit="return false;">
				<div class="tabs-box">
					<div>
						<a id="tabs-items_A" class="tabs-items-hover"><span
							class="tabs-items-hover-span">查詢</span></a>
					</div>
					<div id="TabsContain_A" class="tabs-contain">
						<table cellspacing="4" cellpadding="0" border="0">
							<tbody>
								<tr>
									<th align="right">查詢統計範圍：</th>
									<td align="left"><s:textfield name="entity.start"
											class="input_text" id="cal-field1" /> <script
											type="text/javascript">
												Calendar.setup({
													inputField : "cal-field1"
												});
											</script> 至&nbsp;&nbsp;<s:textfield name="entity.end"
											class="input_text" id="cal-field2" /> <script
											type="text/javascript">
												Calendar.setup({
													inputField : "cal-field2"
												});
											</script><a class="state-default" onclick="goSearch()">查詢</a></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div id="div_nav">
					目前位置：<span>統計資訊</span> &gt; <span>關鍵字檢索統計</span>
				</div>
				<div class="list-box">
					<div class="list-buttons">
						<c:choose>
							<c:when
								test="${(not empty ds.pager.totalRecord)&& (0 ne ds.pager.totalRecord) }">
								<a class="state-default" onclick="goExport()">匯出</a>
							</c:when>

						</c:choose>
					</div>
					<table cellspacing="1" class="list-table">
						<tbody>
							<tr>
								<td colspan="8" class="topic">基本設定</td>
							</tr>
							<tr>
								<th>年月</th>
								<th>名次</th>
								<th>關鍵字</th>
								<th>次數</th>
							</tr>
							<c:forEach var="item" items="${ds.results}" varStatus="status">
								<tr>
									<td><s:property value="entity.start" />~<s:property
											value="entity.end" /></td>
									<td align="center">${item.rank }</td>
									<td><esapi:encodeForHTML>${item.keyword }</esapi:encodeForHTML></td>
									<td>${item.count }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="page-box" align="right">
						<table border="0" cellspacing="0" cellpadding="0">
							<tbody>

								<c:if test="${ds.pager.totalRecord > 0 }">
									<tr>
										<td><jsp:include
												page="/WEB-INF/jsp/layout/pagination.jsp">
												<jsp:param name="namespace" value="/crud" />
												<jsp:param name="action" value="apply.feLogs.list" />
												<jsp:param name="pager" value="${ds.pager}" />
												<jsp:param name="detail" value="0" />
											</jsp:include></td>
										<td>每頁顯示 <select id="listForm_pageSize"
											name="pager.recordPerPage"
											onchange="changePageSize(this.value)">
												<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
												<option value="5">5</option>
												<option value="10">10</option>
												<option value="20">20</option>
												<option value="50">50</option>
												<option value="100">100</option>
										</select> 筆紀錄, 第 <input id="listForm_currentPageHeader"
											value="${ds.pager.currentPage }" type="number" min="1"
											max="${totalPage }" onchange="gotoPage(this.value)">
											頁, 共<span class="totalNum">${totalPage }</span>頁
										</td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
					<div class="detail_note">
						<div class="detail_note_title">Note</div>
						<div class="detail_note_content">
							<c:if test="${0 eq ds.pager.totalRecord}">
								<span>查無資料</span>
							</c:if>
						</div>
					</div>
				</div>
			</s:form>
		</c:when>
		<c:when test="${not empty clicks }">
			<s:form action="apply.feLogs.link" namespace="/crud" method="post"
				onsubmit="return false;">
				<div class="tabs-box">
					<div>
						<a id="tabs-items_A" class="tabs-items-hover"><span
							class="tabs-items-hover-span">查詢</span></a>
					</div>
					<div id="TabsContain_A" class="tabs-contain">
						<table cellspacing="4" cellpadding="0" border="0">
							<tbody>
								<tr>
									<th align="right">查詢統計範圍：</th>
									<td align="left"><s:textfield name="entity.start"
											class="input_text" id="cal-field1" /> <script
											type="text/javascript">
												Calendar.setup({
													inputField : "cal-field1"
												});
											</script> 至&nbsp;&nbsp;<s:textfield name="entity.end"
											class="input_text" id="cal-field2" /> <script
											type="text/javascript">
												Calendar.setup({
													inputField : "cal-field2"
												});
											</script><a class="state-default" onclick="goSearch()">查詢</a></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div id="div_nav">
					目前位置：<span>統計資訊</span> &gt; <span>URL點擊次數統計</span>
				</div>
				<div class="list-box">
					<div class="list-buttons">
						<c:choose>
							<c:when
								test="${(not empty ds.pager.totalRecord)&& (0 ne ds.pager.totalRecord) }">
								<a class="state-default" onclick="goExport()">匯出</a>
							</c:when>

						</c:choose>
					</div>
					<table cellspacing="1" class="list-table">
						<tbody>
							<tr>
								<td colspan="8" class="topic">基本設定</td>
							</tr>
							<tr>
								<th>年月</th>
								<th>名次</th>
								<th>資源類型</th>
								<th>標題</th>
								<th>次數</th>
							</tr>
							<c:forEach var="item" items="${ds.results}" varStatus="status">
								<tr>
									<td><s:property value="entity.start" />~<s:property
											value="entity.end" /></td>
									<td align="center">${item.rank }</td>
									<td><c:choose>
											<c:when test="${not empty item.database }">資料庫</c:when>
											<c:when test="${not empty item.ebook }">電子書</c:when>
											<c:when test="${not empty item.journal }">期刊</c:when>
										</c:choose></td>
									<td><c:choose>
											<c:when test="${not empty item.database }">
												<esapi:encodeForHTML>${item.database.dbTitle }</esapi:encodeForHTML>
											</c:when>
											<c:when test="${not empty item.ebook }">
												<esapi:encodeForHTML>${item.ebook.bookName }</esapi:encodeForHTML>
											</c:when>
											<c:when test="${not empty item.journal }">
												<esapi:encodeForHTML>${item.journal.title }</esapi:encodeForHTML>
											</c:when>
										</c:choose></td>
									<td>${item.count }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="page-box" align="right">
						<table border="0" cellspacing="0" cellpadding="0">
							<tbody>

								<c:if test="${ds.pager.totalRecord > 0 }">
									<tr>
										<td><jsp:include
												page="/WEB-INF/jsp/layout/pagination.jsp">
												<jsp:param name="namespace" value="/crud" />
												<jsp:param name="action" value="apply.feLogs.link" />
												<jsp:param name="pager" value="${ds.pager}" />
												<jsp:param name="detail" value="0" />
											</jsp:include></td>
										<td>每頁顯示 <select id="listForm_pageSize"
											name="pager.recordPerPage"
											onchange="changePageSize(this.value)">
												<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
												<option value="5">5</option>
												<option value="10">10</option>
												<option value="20">20</option>
												<option value="50">50</option>
												<option value="100">100</option>
										</select> 筆紀錄, 第 <input id="listForm_currentPageHeader"
											value="${ds.pager.currentPage }" type="number" min="1"
											max="${totalPage }" onchange="gotoPage(this.value)">
											頁, 共<span class="totalNum">${totalPage }</span>頁
										</td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
					<div class="detail_note">
						<div class="detail_note_title">Note</div>
						<div class="detail_note_content">
							<c:if test="${0 eq ds.pager.totalRecord}">
								<span>查無資料</span>
							</c:if>
						</div>
					</div>
				</div>
			</s:form>
		</c:when>
	</c:choose>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>