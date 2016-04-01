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
	function goSearch() {
		goMain("<c:url value = '/'/>crud/apply.accountNumber.list.action",
				"#apply_accountNumber_list", "");
	}

	//新增
	function goAdd() {
		goDetail("<c:url value = '/'/>crud/apply.accountNumber.add.action",
				'帳戶-新增');
	}

	//失效多筆資料之函式
	function goDeauthorize() {
		//檢查資料是否已被勾選
		var IsSelected = false;
		for (var i = 0; i < $(".checkbox").length; i++) {
			if ($(".checkbox").get(i).checked) {
				IsSelected = true;
				break;
			}
		}
		//進行失效動作
		if (IsSelected) {
			var f = {
				trueText : '是',
				trueFunc : function() {
					var url = "<c:url value = '/'/>crud/apply.accountNumber.deauthorize.action";
					var data = $('#apply_accountNumber_list').serialize()
							+ '&pager.currentPage=' + '${ds.pager.currentPage}';
					goMain(url, '', data);
				},
				falseText : '否',
				falseFunc : function() {
					//不進行刪除...
				}
			};
			goAlert('提醒', '您確定要失效所勾選的帳戶嗎?', f);
		} else {
			goAlert("提醒", "請選擇一筆或一筆以上的資料");
		}
	}

	//生效多筆資料之函式
	function goAuthorize() {
		//檢查資料是否已被勾選
		var IsSelected = false;
		for (var i = 0; i < $(".checkbox").length; i++) {
			if ($(".checkbox").get(i).checked) {
				IsSelected = true;
				break;
			}
		}
		//進行生效動作
		if (IsSelected) {
			var f = {
				trueText : '是',
				trueFunc : function() {
					var url = "<c:url value = '/'/>crud/apply.accountNumber.authorize.action";
					var data = $('#apply_accountNumber_list').serialize()
							+ '&pager.currentPage=' + '${ds.pager.currentPage}';
					goMain(url, '', data);
				},
				falseText : '否',
				falseFunc : function() {
					//不進行刪除...
				}
			};
			goAlert('提醒', '您確定要生效所勾選的帳戶嗎?', f);
		} else {
			goAlert("提醒", "請選擇一筆或一筆以上的資料");
		}
	}

	//資料檢視
	function goView(serNo) {
		var isNum = /^\d+$/.test(serNo);
		if (isNum && parseInt(serNo) > 0) {
			var url = "<c:url value = '/'/>crud/apply.accountNumber.view.action";
			var data = 'entity.serNo=' + serNo;
			goDetail(url, '帳戶-檢視', data);
		}
	}

	//更新資料
	function goUpdate(serNo) {
		var isNum = /^\d+$/.test(serNo);
		if (isNum && parseInt(serNo) > 0) {
			goDetail(
					"<c:url value = '/'/>crud/apply.accountNumber.edit.action?"
							+ 'entity.serNo=' + serNo, '帳戶-修改');
		}
	}

	//單筆刪除
	function goDel(serNo) {
		var f = {
			trueText : '是',
			trueFunc : function() {
				var url = '<c:url value = "/crud/apply.accountNumber.delete.action"/>';
				var data = $('#apply_accountNumber_list').serialize()
						+ '&pager.currentPage=' + '${ds.pager.currentPage}'
						+ '&entity.serNo=' + serNo;
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

	//批次匯入
	function goImport() {
		goDetail("<c:url value = '/'/>crud/apply.accountNumber.imports.action",
				'帳戶-匯入');
	}
</script>
</head>
<body>
	<s:form action="apply.accountNumber.list" namespace="/crud"
		method="post" onsubmit="return false;">
		<div class="tabs-box">
			<div>
				<a id="tabs-items_A" class="tabs-items-hover"><span
					class="tabs-items-hover-span">查詢</span></a>
			</div>
			<div id="TabsContain_A" class="tabs-contain">
				<table border="0" cellspacing="4" cellpadding="0">

					<tbody>
						<tr>
							<td align="left">用戶代碼</td>
							<td align="left"></td>
						</tr>
						<tr>
							<td align="left"><s:textfield name="entity.userId"
									id="search" cssClass="input_text" /></td>
							<td align="left"><a class="state-default"
								onclick="goSearch();">查詢</a></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div id="div_nav">
			目前位置：<span>帳戶管理</span> &gt; <span>帳戶設定</span>
		</div>
		<div class="list-box">
			<div class="list-buttons">
				<a class="state-default" onclick="goAdd();">新增</a>
			</div>
			<table cellspacing="1" class="list-table">
				<tbody>
					<tr>
						<td colspan="8" class="topic">基本設定</td>
					</tr>
					<tr>
						<th>用戶代碼</th>
						<th>用戶姓名</th>
						<th>客戶名稱</th>
						<th>Email</th>
						<th>權限</th>
						<th>狀態</th>
						<th>操作</th>
					</tr>
					<c:forEach var="item" items="${ds.results}" varStatus="status">
						<tr>
							<td>${item.userId }</td>
							<td align="center"><esapi:encodeForHTML>${item.userName }</esapi:encodeForHTML></td>
							<td>${item.customer.name }</td>
							<td align="center">${item.email }</td>
							<td>${item.role.role }</td>
							<td align="center">${item.status.status }</td>
							<td align="center"><a class="state-default2"
								onclick="goUpdate('${item.serNo}')"><span
									class="icon-default icon-edit"></span>修改</a> <c:if
									test="${9 ne item.serNo }">
									<a class="state-default2" onclick="goDel('${item.serNo}')"><span
										class="icon-default icon-delete"></span>刪除</a>
								</c:if></td>
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
										<jsp:param name="action" value="apply.accountNumber.list" />
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
									max="${totalPage }" onchange="gotoPage(this.value)"> 頁,
									共<span class="totalNum">${totalPage }</span>頁
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
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>