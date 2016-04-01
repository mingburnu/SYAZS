<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>
<c:if test="${login.role.role == '管理員'}">
	<%
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	%>
</c:if>
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
<c:if test="${entity.option != 'done'}">
	<script type="text/javascript">
		$(document).ready(function() {
			goURL("<c:url value = '/'/>crud/apply.customer.list.action");
		});
	</script>
</c:if>
<style type="text/css">
.tabs-box .tabs-items-hover-span {
	display: none;
}

.tabs-box .tabs-items-hover {
	background: none;
	background-color: transparent;
}
</style>
<script type="text/javascript">
	function goSearch() {
		goMain("<c:url value = '/'/>crud/apply.customer.list.action",
				"#apply_customer_list", "");
	}

	//新增
	function goAdd() {
		goDetail("<c:url value = '/'/>crud/apply.customer.add.action", '客戶-新增');
	}

	//刪除多筆資料之函式
	function goDelete() {
		//檢查資料是否已被勾選
		var IsSelected = false;
		for (var i = 0; i < $(".checkbox").length; i++) {
			if ($(".checkbox").get(i).checked) {
				IsSelected = true;
				break;
			}
		}

		//進行刪除動作
		if (IsSelected) {
			var f = {
				trueText : '是',
				trueFunc : function() {
					var url = '<c:url value="/crud/apply.customer.delete.action"/>';
					var data = $('#apply_customer_list').serialize()
							+ '&pager.currentPage=' + '${ds.pager.currentPage}';
					goMain(url, '', data);
				},
				falseText : '否',
				falseFunc : function() {
					//不進行刪除...
				}
			};
			goAlert('提醒', '您確定要刪除所勾選的資料嗎?', f);
		} else {
			goAlert("提醒", "請選擇一筆或一筆以上的資料");
		}
	}

	//資料檢視
	function goView(serNo) {
		var isNum = /^\d+$/.test(serNo);
		if (isNum && parseInt(serNo) > 0) {
			var url = "<c:url value = '/'/>crud/apply.customer.view.action";
			var data = 'entity.serNo=' + serNo;
			goDetail(url, '用戶-檢視', data);
		}
	}

	//更新資料
	function goUpdate(serNo) {
		var isNum = /^\d+$/.test(serNo);
		if (isNum && parseInt(serNo) > 0) {
			goDetail("<c:url value = '/'/>crud/apply.customer.edit.action?"
					+ "entity.serNo=" + serNo, '客戶-修改');
		}
	}

	//單筆刪除
	function goDel(serNo) {
		var f = {
			trueText : '是',
			trueFunc : function() {
				var url = '<c:url value = "/crud/apply.customer.delete.action"/>';
				var data = $('#apply_customer_list').serialize()
						+ '&pager.currentPage=' + '${ds.pager.currentPage}'
						+ '&entity.checkItem=' + serNo;
				goMain(url, '', data);
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
	}

	//IP Range管理
	function goIpRangeManager(serNo) {
		var isNum = /^\d+$/.test(serNo);
		if (isNum && parseInt(serNo) > 0) {
			var url = '<c:url value = '/'/>crud/apply.ipRange.list.action';
			var data = 'entity.customer.serNo=' + serNo;
			goDetail(url, '客戶-IP Range管理', data);
		}
	}

	//群組管理
	function goGroupManager(serNo) {
		var isNum = /^\d+$/.test(serNo);
		if (isNum && parseInt(serNo) > 0) {
			var url = '<c:url value = '/'/>crud/apply.group.list.action';
			var data = 'entity.customer.serNo=' + serNo;
			goDetail(url, '客戶-群組管理', data);
		}
	}

	//批次匯入
	function goImport() {
		goDetail("<c:url value = '/'/>crud/apply.customer.imports.action?",
				'客戶-匯入');
	}
</script>
</head>
<body>
	<s:form action="apply.customer.list" namespace="/crud" method="post"
		onsubmit="return false;">
		<div class="tabs-box">
			<div>
				<a id="tabs-items_A" class="tabs-items-hover"><span
					class="tabs-items-hover-span">查詢</span></a>
			</div>
		</div>
		<div id="div_nav">
			目前位置：<span>客戶管理</span> &gt; <span>基本設定</span>
		</div>
		<div class="list-box">
			<table cellspacing="1" class="list-table">
				<tbody>
					<tr>
						<td colspan="5" class="topic">基本設定</td>
					</tr>
					<tr>
						<th>用戶名稱</th>
						<th>電話</th>
						<th>地址</th>
						<th>操作</th>
					</tr>
					<c:forEach var="item" items="${ds.results}" varStatus="status">
						<tr>
							<td>${item.name}</td>
							<td align="center">${item.tel }</td>
							<td><esapi:encodeForHTML>${item.address }</esapi:encodeForHTML></td>
							<td align="center"><a class="state-default2"
								onclick="goUpdate('${item.serNo}');"><span
									class="icon-default icon-edit"></span>修改</a></td>
					</c:forEach>
				</tbody>
			</table>
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
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>