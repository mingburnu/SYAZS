<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<c:set var="customerSerNo">
	<c:out value='<%=request.getParameter("entity.customer.serNo")%>'></c:out>
</c:set>
<c:set var="pageFactor"
	value="${ds.pager.totalRecord/ds.pager.recordPerPage}" />
<c:set var="totalPage">
	<fmt:formatNumber type="number" pattern="#"
		value="${pageFactor+(1-(pageFactor%1))%1}" />
</c:set>
<script type="text/javascript">
	//新增Group
	function goAdd_detail() {
		var url = "<c:url value = '/'/>/crud/apply.group.add.action";
		var data = 'entity.customer.serNo=' + '${customerSerNo }';
		goDetail_2(url, '客戶-群組新增', data);
	}

	//Group編輯
	function goUpdate_detail(listNo, serNo) {
		var isNum = /^\d+$/.test(serNo);
		if (isNum && parseInt(serNo) > 0) {
			var url = "<c:url value = '/'/>crud/apply.group.edit.action";
			var data = 'entity.serNo=' + serNo + '&entity.listNo=' + listNo
					+ '&entity.customer.serNo=' + '${customerSerNo }';
			goDetail_2(url, '客戶-群組修改', data);
		}
	}

	//單筆刪除
	function goDel_detail(serNo) {
		var f = {
			trueText : '是',
			trueFunc : function() {
				var url = "<c:url value = '/'/>crud/apply.group.delete.action";
				var data = $('#apply_group_list').serialize()
						+ '&entity.serNo=' + serNo + '&entity.customer.serNo='
						+ '${entity.customer.serNo }' + '&pager.currentPage='
						+ '${ds.pager.currentPage}';
				goDetail_Main(url, '', data);
			},
			falseText : '否',
			falseFunc : function() {
				//不進行刪除...
			}
		};

		var isNum = /^\d+$/.test(serNo);
		if (isNum && parseInt(serNo) > 0) {
			goAlert('提醒', '確定要刪除此筆資料嗎?', f);
		} else {
			goAlert('提醒', '錯誤', '');
		}

		var isNum = /^\d+$/.test(serNo);
		if (isNum && parseInt(serNo) > 0) {
			goAlert('提醒', '確定要刪除此筆資料嗎?', f);
		} else {
			goAlert('提醒', '錯誤', '');
		}
	}

	function closeDetail() {
		$("#div_Detail").hide();
		UI_Resize();
		$("#div_Detail .content > .header > .title").empty();
		$("#div_Detail .content > .contain").empty();
	}

	//批次匯入
	function goImport_detail() {
		goDetail_2("<c:url value = '/'/>crud/apply.group.imports.action?",
				'Group-匯入', 'entity.customer.serNo='
						+ '${entity.customer.serNo}');
	}
</script>
</head>
<body>
	<s:form action="apply.group.list" namespace="/crud" method="post">
		<input type="hidden" name="entity.customer.serNo"
			value="${customerSerNo }" />
		<div class="list-box">
			<div class="list-buttons">
				<c:choose>
					<c:when test="${9 eq entity.customer.serNo }">
						<c:if test="${login.role == '系統管理員' }">
							<a class="state-default" onclick="goAdd_detail();">新增</a>
							<a class="state-default" onclick="goImport_detail();">匯入</a>
						</c:if>
					</c:when>
					<c:otherwise>
						<a class="state-default" onclick="goAdd_detail();">新增</a>
						<a class="state-default" onclick="goImport_detail();">匯入</a>
					</c:otherwise>
				</c:choose>
			</div>
			<table cellspacing="1" class="list-table">
				<tbody>
					<tr>
						<td colspan="6" class="topic">Group管理</td>
					</tr>
					<tr>
						<th>NO.</th>
						<th>用戶名稱</th>
						<th>Group1</th>
						<th>Group2</th>
						<th>Group3</th>
						<th>操作</th>
					</tr>
					<c:forEach var="item" items="${ds.results}" varStatus="status">
						<c:set var="orderInt" scope="session"
							value="${ds.pager.offset+(status.index+1)}" />
						<tr>
							<td align="center">${orderInt}</td>
							<td align="center">${item.groupMapping.title}</td>
							<td align="center"><c:if
									test="${1 eq item.groupMapping.level }">${item.groupName}</c:if>
								<c:if test="${2 eq item.groupMapping.level }">${item.groupMapping.parentGroupMapping.group.groupName}</c:if>
								<c:if test="${3 eq item.groupMapping.level }">${item.groupMapping.parentGroupMapping.parentGroupMapping.group.groupName}</c:if>
							</td>
							<td align="center"><c:if
									test="${2 eq item.groupMapping.level }">${item.groupName}</c:if>
								<c:if test="${3 eq item.groupMapping.level }">${item.groupMapping.parentGroupMapping.group.groupName}</c:if>
							</td>
							<td align="center"><c:if
									test="${3 eq item.groupMapping.level }">${item.groupName}</c:if>
							</td>
							<td align="center"><c:choose>
									<c:when test="${9 eq item.customer.serNo }">
										<c:if test="${login.role == '系統管理員' }">
											<a class="state-default2"
												onclick="goUpdate_detail('${orderInt}','${item.serNo}');"><span
												class="icon-default icon-edit"></span>修改</a>
											<a class="state-default2"
												onclick="goDel_detail('${item.serNo}');"><span
												class="icon-default icon-delete"></span>刪除</a>
										</c:if>
									</c:when>
									<c:otherwise>
										<a class="state-default2"
											onclick="goUpdate_detail('${orderInt}','${item.serNo}');"><span
											class="icon-default icon-edit"></span>修改</a>
										<a class="state-default2"
											onclick="goDel_detail('${item.serNo}');"><span
											class="icon-default icon-delete"></span>刪除</a>
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="page-box" align="right">
				<table border="0" cellspacing="0" cellpadding="0">
					<tbody>
						<tr>
							<td align="left" class="p_02"><jsp:include
									page="/WEB-INF/jsp/layout/pagination.jsp">
									<jsp:param name="namespace" value="/crud" />
									<jsp:param name="action" value="apply.group.list" />
									<jsp:param name="pager" value="${ds.pager}" />
									<jsp:param name="detail" value="1" />
								</jsp:include></td>
							<td>每頁顯示 <select id="listForm_pageSize"
								name="pager.recordPerPage" onchange="changePageSize_detail(this.value)">
									<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
									<option value="5">5</option>
									<option value="10">10</option>
									<option value="20">20</option>
									<option value="50">50</option>
									<option value="=100">100</option>
							</select> 筆紀錄, 第 <input id="listForm_currentPageHeader"
								value="${ds.pager.currentPage }" type="number" min="1"
								max="${totalPage }" onchange="gotoPage_detail(this.value)">
								頁, 共<span class="totalNum">${totalPage }</span>頁
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="detail_note">
				<div class="detail_note_title">Note</div>
				<div class="detail_note_content"></div>
			</div>
		</div>
	</s:form>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>