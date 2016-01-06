<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cfn" uri="http://java.sy.com/jsp/jstl/cfn"%>
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
	function allBox() {
		if ($("input.all.box").attr("checked")) {
			addAllBox();
		} else {
			removeAllBox();
		}
	}

	function addAllBox() {
		$(".checkbox.queue:visible").each(function() {
			$(this).attr("checked", "checked");
		});

		$
				.ajax({
					type : "POST",
					url : "<c:url value = '/'/>crud/apply.accountNumber.addAllItem.action",
					dataType : "html",
					success : function(message) {

					}
				});
	}

	function removeAllBox() {
		$(".checkbox.queue:visible").each(function() {
			$(this).attr("checked", false);
		});

		$
				.ajax({
					type : "POST",
					url : "<c:url value = '/'/>crud/apply.accountNumber.removeAllItem.action",
					dataType : "html",
					success : function(message) {

					}
				});
	}

	function allRow(action) {
		var importItem = "";
		if (action == 1) {
			$(".checkbox.queue:visible").each(
					function() {
						$(this).attr("checked", "checked");
						importItem = importItem + "entity.importItem="
								+ $(this).val() + "&";
					});

			$
					.ajax({
						type : "POST",
						url : "<c:url value = '/'/>crud/apply.accountNumber.allCheckedItem.action",
						dataType : "html",
						data : importItem.slice(0, importItem.length - 1),
						success : function(message) {

						}
					});
		} else {
			$(".checkbox.queue:visible").each(
					function() {
						$(this).removeAttr("checked");
						importItem = importItem + "entity.importItem="
								+ $(this).val() + "&";
					});

			$
					.ajax({
						type : "POST",
						url : "<c:url value = '/'/>crud/apply.accountNumber.allUncheckedItem.action",
						dataType : "html",
						data : importItem.slice(0, importItem.length - 1),
						success : function(message) {

						}
					});
		}
	}

	function getCheckedItem(index) {
		$
				.ajax({
					type : "POST",
					url : "<c:url value = '/'/>crud/apply.accountNumber.getCheckedItem.action",
					dataType : "html",
					data : "entity.importItem=" + index,
					success : function(message) {

					}
				});
	}

	function getErrors() {
		var url = "<c:url value = '/'/>crud/apply.accountNumber.backErrors.action";
		window.open(url, "_top");
	}

	function checkData() {
		//檢查資料是否已被勾選
		//進行動作
		if ($("input.checkbox.queue:checked").length > 0) {
			goDetail_Main(
					'<c:url value = '/'/>crud/apply.accountNumber.importData.action',
					'#apply_accountNumber_paginate', '&pager.currentPage='
							+ '${ds.pager.currentPage}');
		} else {
			goAlert("訊息", "請選擇一筆或一筆以上的資料");
		}
	}
</script>
</head>
<body>
	<s:form namespace="/crud" action="apply.accountNumber.paginate"
		method="post" onsubmit="return false;">
		<table cellspacing="1" class="list-table queue">
			<tbody>
				<tr>
					<th><c:choose>
							<c:when test="${allChecked }">
								<input type="checkbox" class="all box" onclick="allBox()"
									checked="checked">
							</c:when>
							<c:otherwise>
								<input type="checkbox" class="all box" onclick="allBox()">
							</c:otherwise>
						</c:choose></th>
					<c:forEach var="item" items="${cellNames}" varStatus="status">
						<c:if test="${1 ne status.index }">
							<th><esapi:encodeForHTMLAttribute>${item}</esapi:encodeForHTMLAttribute></th>
						</c:if>
					</c:forEach>
					<th></th>
				</tr>
				<c:forEach var="item" items="${ds.results}" varStatus="status">
					<tr>
						<td><c:choose>
								<c:when test="${item.dataStatus=='正常'}">
									<c:choose>
										<c:when
											test="${cfn:containsInt(checkItemSet,(ds.pager.currentPage-1) * ds.pager.recordPerPage + status.index) }">
											<input type="checkbox" class="checkbox queue"
												name="checkItem"
												value="${(ds.pager.currentPage-1) * ds.pager.recordPerPage + status.index }"
												onclick="getCheckedItem(this.value)" checked="checked">
										</c:when>
										<c:otherwise>
											<input type="checkbox" class="checkbox queue"
												name="checkItem"
												value="${(ds.pager.currentPage-1) * ds.pager.recordPerPage + status.index }"
												onclick="getCheckedItem(this.value)">
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<input type="checkbox" disabled="disabled">
								</c:otherwise>
							</c:choose></td>
						<td><esapi:encodeForHTML>${item.userId }</esapi:encodeForHTML></td>
						<td><esapi:encodeForHTML>${item.userName }</esapi:encodeForHTML></td>
						<td><esapi:encodeForHTML>${item.customer.name }</esapi:encodeForHTML></td>
						<td><esapi:encodeForHTML>${item.email }</esapi:encodeForHTML></td>
						<td>${item.role.role }</td>
						<td>${item.status.status }</td>
						<td align="center">${fn:replace(item.dataStatus, ',', '<br>')}</td>
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
									<jsp:param name="action" value="apply.accountNumber.paginate" />
									<jsp:param name="pager" value="${ds.pager}" />
									<jsp:param name="detail" value="1" />
								</jsp:include></td>
							<td>每頁顯示 <select id="listForm_pageSize"
								name="pager.recordPerPage" onchange="changePageSize_detail()">
									<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
									<option value="5">5</option>
									<option value="10">10</option>
									<option value="20">20</option>
									<option value="50">50</option>
							</select> 筆紀錄, 第 <input id="listForm_currentPageHeader"
								value="${ds.pager.currentPage }" type="number" min="1"
								max="${totalPage }" onchange="gotoPage_detail(this.value)">
								頁, 共<span class="totalNum">${totalPage }</span>頁
							</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
		<div class="button_box">
			<div class="detail-func-button">
				<a class="state-default" onclick="allRow(1)">全選</a> <a
					class="state-default" onclick="allRow(0)">重置</a> <a
					class="state-default" onclick="closeDetail();">關閉</a> <a
					class="state-default" onclick="checkData()">確認</a>
			</div>
		</div>
		<div class="detail_note">
			<div class="detail_note_title">Note</div>
			<div class="detail_note_content">
				共${total }筆記錄(正常筆數 : ${normal } ;異常筆數 :
				<c:choose>
					<c:when test="${total-normal>0 }">
						<a class="error number" onclick="getErrors()">${total-normal }</a>
					</c:when>
					<c:otherwise>0</c:otherwise>
				</c:choose>
				;已匯入 : ${insert })
			</div>
		</div>
	</s:form>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>