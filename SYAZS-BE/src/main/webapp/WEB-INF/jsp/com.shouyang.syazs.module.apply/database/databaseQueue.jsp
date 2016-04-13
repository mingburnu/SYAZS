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

		$.ajax({
			type : "POST",
			url : "<c:url value = '/'/>crud/apply.database.addAllItem.action",
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
					url : "<c:url value = '/'/>crud/apply.database.removeAllItem.action",
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
						url : "<c:url value = '/'/>crud/apply.database.allCheckedItem.action",
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
						url : "<c:url value = '/'/>crud/apply.database.allUncheckedItem.action",
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
					url : "<c:url value = '/'/>crud/apply.database.getCheckedItem.action",
					dataType : "html",
					data : "entity.importItem=" + index,
					success : function(message) {

					}
				});
	}

	function checkData() {
		//檢查資料是否已被勾選
		//進行動作
		if ($("input.checkbox.queue:checked").length > 0) {

			goDetail_Main(
					'<c:url value = '/'/>crud/apply.database.importData.action',
					'#apply_database_paginate', '&pager.currentPage='
							+ '${ds.pager.currentPage}');
		} else {
			goAlert("訊息", "請選擇一筆或一筆以上的資料");
		}
	}

	function closeDetail() {
		$("#div_Detail").hide();
		UI_Resize();
		var insert = "${insert}";
		if (parseInt(insert) > 0) {
			if ($("form#apply_journal_list input#listForm_currentPageHeader")
					.val() != null) {
				gotoPage($(
						"form#apply_journal_list input#listForm_currentPageHeader")
						.val());
			} else {
				goSearch();
			}
		}

		resetCloseDetail();
	}
</script>
</head>
<body>
	<s:form namespace="/crud" action="apply.database.paginate"
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
					<th><esapi:encodeForHTML>${cellNames[0]}</esapi:encodeForHTML></th>
					<th><esapi:encodeForHTML>${cellNames[5]}</esapi:encodeForHTML></th>
					<th><esapi:encodeForHTML>${cellNames[9]}</esapi:encodeForHTML></th>
					<th><esapi:encodeForHTML>${cellNames[10]}</esapi:encodeForHTML></th>
					<th><esapi:encodeForHTML>${cellNames[11]}</esapi:encodeForHTML></th>
					<th><esapi:encodeForHTML>${cellNames[12]}</esapi:encodeForHTML></th>
					<th><esapi:encodeForHTML>${cellNames[13]}</esapi:encodeForHTML></th>
					<th><esapi:encodeForHTML>${cellNames[14]}</esapi:encodeForHTML></th>
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
						<td><esapi:encodeForHTML>${item.dbTitle }</esapi:encodeForHTML></td>
						<td>${item.topic }</td>
						<td>${item.type }</td>
						<td>${item.url }</td>
						<td><c:choose>
								<c:when test="${true eq item.openAccess}">是</c:when>
								<c:otherwise>否</c:otherwise>
							</c:choose></td>
						<td>${fn:split(item.startDate, 'T')[0] }</td>
						<td>${fn:split(item.maturityDate, 'T')[0] }</td>
						<td>${item.resourcesBuyers.category }</td>
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
									<jsp:param name="action" value="apply.database.paginate" />
									<jsp:param name="pager" value="${ds.pager}" />
									<jsp:param name="detail" value="1" />
								</jsp:include></td>
							<td>每頁顯示 <select id="listForm_pageSize"
								name="pager.recordPerPage"
								onchange="changePageSize_detail(this.value)">
									<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
									<option value="5">5</option>
									<option value="10">10</option>
									<option value="20">20</option>
									<option value="50">50</option>
									<option value="100">100</option>
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
			<div class="detail_note_content">共${total }筆記錄(正常筆數 : ${normal }
				;異常筆數 : ${total-normal } ;已匯入 : ${insert })</div>
		</div>
	</s:form>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>