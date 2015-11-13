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
<script type="text/javascript">
//切換查詢項目
$(document).ready(function() {
	$("select#listForm_searchCondition").change(function() {
		$("input#search").attr("name", $(this).val());
	});	
});

function goSearch(){
	goMain("<%=request.getContextPath()%>/crud/apply.referenceOwner.list.action",	"#apply_referenceOwner_list", "");
}

//新增
function goAdd(){
	goDetail('<%=request.getContextPath()%>/crud/apply.referenceOwner.add.action','客戶-新增');
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
				trueText:'是',
				trueFunc:function(){
					var url = '<c:url value="/crud/apply.referenceOwner.delete.action"/>';    
					var data = $('#apply_referenceOwner_list').serialize()+'&pager.currentPage='+'${ds.pager.currentPage}';
					goMain(url,'',data);
                    },
                    falseText:'否',
                    falseFunc:function(){
                    	//不進行刪除...
                   }
		};
		goAlert('提醒','您確定要刪除所勾選的資料嗎?',f);
	} else {
		goAlert("提醒","請選擇一筆或一筆以上的資料");
	}
}
	
//資料檢視
function goView(serNo){
	var isNum = /^\d+$/.test(serNo);
	if (isNum && parseInt(serNo) > 0){
        var url = "<c:url value = '/'/>crud/apply.referenceOwner.view.action";
        var data = 'entity.serNo='+serNo;
        goDetail(url,'用戶-檢視',data);
	}
}
	
//更新資料
function goUpdate(serNo) {
	var isNum = /^\d+$/.test(serNo);
	if (isNum && parseInt(serNo) > 0){
		goDetail('<%=request.getContextPath()%>/crud/apply.referenceOwner.edit.action?'+'entity.serNo='+serNo,'客戶-修改');
	}
}
	
//單筆刪除
function goDel(serNo){
	var f = {
		trueText:'是',
		trueFunc:function(){
			var url = '<c:url value = "/crud/apply.referenceOwner.delete.action"/>';
			var data =$('#apply_referenceOwner_list').serialize()+'&pager.currentPage='+'${ds.pager.currentPage}'+'&entity.checkItem='+serNo;
			goMain(url,'',data);
			},
			falseText:'否',
			falseFunc:function(){
				//不進行刪除...
			}
		};
		
	var isNum = /^\d+$/.test(serNo);
	if (isNum && parseInt(serNo) > 0){
		goAlert('提醒','確定要刪除此筆資料嗎?',f);
	} else {
		goAlert('提醒','錯誤','');
	}
}

//批次匯入
function goImport(){
	goDetail('<%=request.getContextPath()%>/crud/apply.referenceOwner.imports.action?','客戶-匯入');
}
</script>
</head>
<body>
	<s:form action="apply.referenceOwner.list" namespace="/crud"
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
							<td align="left"><s:select name="entity.option"
									id="listForm_searchCondition"
									list="#{'entity.name':'用戶名稱','entity.engName':'英文名稱'}"></s:select>
							</td>
							<c:choose>
								<c:when test="${entity.option=='entity.engName' }">
									<td align="left"><s:textfield name="entity.engName"
											id="search" cssClass="input_text" /></td>
								</c:when>
								<c:otherwise>
									<td align="left"><s:textfield name="entity.name"
											id="search" cssClass="input_text" /></td>
								</c:otherwise>
							</c:choose>
							<td align="left"><a class="state-default"
								onclick="goSearch();">查詢</a></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div id="div_nav">
			目前位置：<span>書目資料管理</span> &gt; <span>擁有人</span>
		</div>
		<div class="list-box">
			<div class="list-buttons">
				<c:choose>
					<c:when
						test="${(not empty ds.pager.totalRecord)&& (0 ne ds.pager.totalRecord) }">
						<c:if test="${login.role =='系統管理員'}">
							<a class="state-default" onclick="allSelect(1);">全選</a>
							<a class="state-default" onclick="allSelect(0);">取消</a>
						</c:if>
						<a class="state-default" onclick="goAdd();">新增</a>
						<c:if test="${login.role =='系統管理員'}">
							<a class="state-default" onclick="goDelete();">刪除</a>
						</c:if>
						<a class="state-default" onclick="goImport()">匯入</a>
					</c:when>
					<c:otherwise>
						<a class="state-default" onclick="goAdd();">新增</a>
						<a class="state-default" onclick="goImport()">匯入</a>
					</c:otherwise>
				</c:choose>
			</div>
			<table cellspacing="1" class="list-table">
				<tbody>
					<tr>
						<td colspan="5" class="topic">基本設定</td>
					</tr>
					<tr>
						<c:if test="${login.role =='系統管理員'}">
							<th width="50" align="center">&nbsp;</th>
						</c:if>
						<th>用戶名稱</th>
						<th>電話</th>
						<th>地址</th>
						<th>操作</th>
					</tr>
					<c:forEach var="item" items="${ds.results}" varStatus="status">
						<tr>
							<c:if test="${login.role =='系統管理員'}">
								<td align="center" class="td_first" nowrap><input
									type="checkbox" class="checkbox" name="entity.checkItem"
									value="${item.serNo}"></td>
							</c:if>
							<td>${item.name}</td>
							<td align="center">${item.tel }</td>
							<td><esapi:encodeForHTML>${item.address }</esapi:encodeForHTML></td>
							<td align="center"><a class="state-default2"
								onclick="goView(${item.serNo });"><span
									class="icon-default icon-view"></span>檢視</a> <a
								class="state-default2" onclick="goUpdate(${item.serNo});"><span
									class="icon-default icon-edit"></span>修改</a> <c:if
									test="${login.role =='系統管理員'}">
									<a class="state-default2" onclick="goDel(${item.serNo});"><span
										class="icon-default icon-delete"></span>刪除</a>
								</c:if></td>
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
										<jsp:param name="action" value="apply.referenceOwner.list" />
										<jsp:param name="pager" value="${ds.pager}" />
										<jsp:param name="detail" value="0" />
									</jsp:include></td>
								<td>每頁顯示 <select id="listForm_pageSize"
									name="pager.recordPerPage" onchange="changePageSize()">
										<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
										<option value="5">5</option>
										<option value="10">10</option>
										<option value="20">20</option>
										<option value="50">50</option>
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