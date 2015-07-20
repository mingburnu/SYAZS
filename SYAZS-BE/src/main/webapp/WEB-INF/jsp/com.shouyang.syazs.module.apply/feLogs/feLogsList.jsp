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
<c:if test="${login.role.role != '管理員'}">
	<script type="text/javascript">
	$(document).ready(function() {
		formSetCSS();
		state_hover();
		initAutoComplete("<%=request.getContextPath()%>/crud/apply.customer.json.action",
				'#customerSerNo', '#customerName');
		});
	</script>
</c:if>
<script type="text/javascript">
	//IE press Enter GoPage
	$(document).ready(function() {
		$("input#listForm_currentPageHeader").keyup(function(e) {
			if (e.keyCode == 13) {
				gotoPage($(this).val());
			}
		});
	});

	$(document).ready(
			function() {
				$('input#customerName').click(
						function() {
							$('input[name="entity.customer.serNo"]:eq(0)')
									.attr("checked", '');
							$('input[name="entity.customer.serNo"]:eq(1)')
									.attr("checked", true);
						});
			});
	
	function goSearch(){
		if($("input#customerSerNo").attr("checked")){
		var customerSerNo=$("input#customerSerNo").val();
		if(customerSerNo!=null&&customerSerNo>0){
			goMain("<%=request.getContextPath()%>/crud/apply.feLogs.list.action",
					"#apply_feLogs_list", "");
		}else{
			goAlert("訊息", "請正確填寫機構名稱");
		}
		}else{
			goMain("<%=request.getContextPath()%>/crud/apply.feLogs.list.action",
						"#apply_feLogs_list", "");
			}
		}
		
	//GoPage
	function gotoPage(page) {
		var isNum = /^\d+$/.test(page);
		var totalPage = $("span.totalNum:eq(0)").html();

		if (!isNum) {
			page = "${ds.pager.currentPage}";
		} else {
			if (parseInt(page) < 1) {
				page = 1;
			}

			if (parseInt(page) > parseInt(totalPage)) {
				page = totalPage;
			}
		}
		goMain('<c:url value = '/'/>crud/apply.feLogs.list.action',
				'#apply_feLogs_list', '&pager.currentPage=' + page);
	}

	//變更顯示筆數
	function chagePageSize() {
		goMain('<c:url value = '/'/>crud/apply.feLogs.list.action',
				'#apply_feLogs_list', '&pager.recordPoint='
						+ '${ds.pager.recordPoint }');
	}

	//匯出
	function goExport(){
		var data=$("#apply_feLogs_list").serialize();
		var url='<%=request.getContextPath()%>/crud/apply.feLogs.exports.action?'
					+ data;

			if ($("input#customerSerNo").attr("checked")) {
				var customerSerNo = $("input#customerSerNo").val();
				if (customerSerNo != null && customerSerNo > 0) {
					window.open(url, "exports");
				} else {
					goAlert("訊息", "請正確填寫機構名稱");
				}
			} else {
				window.open(url, "exports");
			}
		}		
</script>
</head>
<body>
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
									</script> 至&nbsp;&nbsp;<s:textfield name="entity.end" class="input_text"
									id="cal-field2" /> <script type="text/javascript">
										Calendar.setup({
											inputField : "cal-field2"
										});
									</script></td>
						</tr>
						<c:if test="${login.role.role != '管理員'}">
							<tr>
								<th align="right">全部：</th>
								<td><c:choose>
										<c:when test="${0 eq entity.customer.serNo}">
											<input type="radio" name="entity.customer.serNo" value="0"
												checked>
										</c:when>
										<c:otherwise>
											<input type="radio" name="entity.customer.serNo" value="0">
										</c:otherwise>
									</c:choose></td>
							</tr>
						</c:if>
						<c:choose>
							<c:when test="${login.role.role != '管理員'}">
								<tr>
									<th align="right">用戶名稱：</th>
									<td><c:choose>
											<c:when test="${0 eq entity.customer.serNo}">
												<input type="radio" name="entity.customer.serNo"
													id="customerSerNo" value="0" />
											</c:when>
											<c:otherwise>
												<input type="radio" name="entity.customer.serNo"
													id="customerSerNo" value="${entity.customer.serNo }"
													checked />
											</c:otherwise>
										</c:choose><input type="text" id="customerName" class="input_text"
										name="entity.customer.name" value="${entity.customer.name }">
										<a class="state-default" onclick="goSearch()">查詢</a></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<th align="right">用戶名稱：</th>
									<td><input type="radio" name="entity.customer.serNo"
										id="customerSerNo" value="${login.customer.serNo}"
										disabled="disabled" checked="checked"> <input
										type="text" id="customerName" class="input_text"
										value="${login.customer.name}" disabled="disabled"> <a
										class="state-default" onclick="goSearch()">查詢</a></td>
								</tr>
							</c:otherwise>
						</c:choose>
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
						<th>客戶名稱</th>
					</tr>
					<c:forEach var="item" items="${ds.results}" varStatus="status">
						<tr>
							<td><s:property value="entity.start" />~<s:property
									value="entity.end" /></td>
							<td align="center">${item.rank }</td>
							<td><esapi:encodeForHTML>${item.keyword }</esapi:encodeForHTML></td>
							<td>${item.count }</td>
							<td><c:out value="${item.customer.name }" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="page-box" align="right">
				<table border="0" cellspacing="0" cellpadding="0">
					<tbody>

						<c:if test="${ds.pager.totalRecord > 0 }">
							<tr>
								<td><jsp:include page="/WEB-INF/jsp/layout/pagination.jsp">
										<jsp:param name="namespace" value="/crud" />
										<jsp:param name="action" value="apply.feLogs.paginate" />
										<jsp:param name="pager" value="${ds.pager}" />
										<jsp:param name="recordPerPage"
											value="${ds.pager.recordPerPage}" />
										<jsp:param name="detail" value="0" />
									</jsp:include></td>
								<td><c:set var="pageFactor"
										value="${ds.pager.totalRecord/ds.pager.recordPerPage}" /> <c:set
										var="totalPage">
										<fmt:formatNumber type="number" pattern="#"
											value="${pageFactor+(1-(pageFactor%1))%1}" />
									</c:set> 每頁顯示 <select id="listForm_pageSize" name="pager.recordPerPage"
									onchange="chagePageSize()">
										<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
										<option value="5">5</option>
										<option value="10">10</option>
										<option value="20">20</option>
										<option value="50">50</option>
								</select> 筆紀錄, 第 <input id="listForm_currentPageHeader"
									value="${ds.pager.currentPage }" type="number" min="1"
									max="${totalPage }" onchange="gotoPage(this.value)"> 頁,
									共<span class="totalNum">${totalPage }</span>頁</td>
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
	<iframe name="exports" style="display: none;"></iframe>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>