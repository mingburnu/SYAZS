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
<script type="text/javascript">
//切換查詢項目
$(document).ready(function() {
	$("select#listForm_searchCondition").change(function() {
		$("input#search").attr("name", $(this).val());
	});
});

$(document).ready(function() {
	$("select#listForm_searchCondition").val('${option }');
});

//IE press Enter GoPage
$(document).ready(function() {
	$("input#listForm_currentPageHeader").keyup(function(e){
		if(e.keyCode == 13){gotoPage($(this).val());}
	});
});

function goSearch(){
    goMain("<%=request.getContextPath()%>/crud/apply.ebook.list.action",
			"#apply_ebook_list", "");
}

//新增
function goAdd(){
        goDetail('<%=request.getContextPath()%>/crud/apply.ebook.edit.action','電子書-新增');
}

//刪除多筆資料之函式
function goDelete() {
	//檢查資料是否已被勾選
	var IsSelected = false;
	var serNoStr = "";
	var checkbox_checked_num = 0;
	for (var i = 0; i < $(".checkbox").length; i++) {
		if ($(".checkbox").get(i).checked) {
			serNoStr = serNoStr + $(".checkbox").get(i).value + "；；";
			IsSelected = true;
			checkbox_checked_num++;
		}
	}
	
	//進行刪除動作
	if (IsSelected) {
		var f = {
                trueText:'是',
                trueFunc:function(){
                        var url = "<c:url value='/crud/apply.ebook.delete.action'/>";
                        var data = $('#apply_ebook_list').serialize()+'&pager.offset='+'${ds.pager.offset}'+'&pager.currentPage='+'${ds.pager.currentPage}'+'&pager.offsetPoint'+'${ds.pager.offset}';
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
        var url = "<c:url value = '/'/>crud/apply.ebook.view.action";
        var data = 'viewSerNo='+serNo;
        goDetail(url,'電子書-檢視',data);
	}
}

//更新資料
function goUpdate(serNo) {
	var isNum = /^\d+$/.test(serNo);
	if (isNum && parseInt(serNo) > 0){
		goDetail('<%=request.getContextPath()%>/crud/apply.ebook.edit.action?'+'entity.serNo='+serNo,'電子書-修改');
	}
}

//GoPage
function gotoPage(page){
	var isNum = /^\d+$/.test(page);
	var totalPage = $("span.totalNum:eq(0)").html();
	var recordPerPage="${ds.pager.recordPerPage}";
	var offset=parseInt(recordPerPage)*(parseInt(page)-1);
	
	if(!isNum){
		page="${ds.pager.currentPage}";
		offset=parseInt(recordPerPage)*(parseInt(page)-1);
	} else {
		if (parseInt(page) < 1){
			page=1;
			offset=parseInt(recordPerPage)*(parseInt(page)-1);
			}		
		
		if (parseInt(page) > parseInt(totalPage)){
			page=totalPage;
			offset=parseInt(recordPerPage)*(parseInt(page)-1);
			} 
		}
    goMain('<c:url value = '/'/>crud/apply.ebook.list.action','#apply_ebook_list','&pager.offset='+offset+'&pager.currentPage='+page);
}

//變更顯示筆數
function chagePageSize(recordPerPage,recordPoint){
        goMain('<c:url value = '/'/>crud/apply.ebook.list.action','#apply_ebook_list','&recordPerPage='+recordPerPage+'&recordPoint='+recordPoint);
}

//批次匯入
function goImport(){
	goDetail('<%=request.getContextPath()%>/crud/apply.ebook.imports.action','電子書-匯入');
}

</script>
</head>
<body>
	<s:form action="apply.ebook.list" namespace="/crud" method="post"
		onsubmit="return false;">
		<div class="tabs-box">
			<div>
				<a id="tabs-items_A" class="tabs-items-hover"><span
					class="tabs-items-hover-span">查詢</span></a>
			</div>
			<div id="TabsContain_A" class="tabs-contain">
				<table border="0" cellspacing="4" cellpadding="0">

					<tbody>
						<tr>
							<td align="left"><select name="option"
								id="listForm_searchCondition">
									<option value="entity.bookName">書名</option>
									<option value="isbn">ISBN</option>
							</select></td>
							<c:set var="option">
								<c:out value="${option }" />
							</c:set>
							<c:choose>
								<c:when test="${not empty option }">
									<c:set var="find">
										<c:out
											value='<%=request.getParameter(request
									.getAttribute("option").toString())%>' />
									</c:set>
									<td align="left"><input type="text" name="${option }"
										maxlength="20" id="search" class="input_text" value="${find }">
									</td>
								</c:when>
								<c:otherwise>
									<td align="left"><input type="text" name="entity.bookName"
										maxlength="20" id="search" class="input_text"></td>
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
			目前位置：<span>帳戶管理</span> &gt; <span>電子書</span>
		</div>
		<div class="list-box">
			<div class="list-buttons">
				<c:choose>
					<c:when
						test="${(not empty ds.pager.totalRecord)&& (0 ne ds.pager.totalRecord) }">
						<a class="state-default" onclick="allSelect(1);">全選</a>
						<a class="state-default" onclick="allSelect(0);">取消</a>
						<a class="state-default" onclick="goAdd();">新增</a>
						<a class="state-default" onclick="goDelete();">刪除</a>
						<a class="state-default" onclick="goImport()">批次匯入</a>
					</c:when>
					<c:otherwise>
						<a class="state-default" onclick="goAdd();">新增</a>
						<a class="state-default" onclick="goImport()">批次匯入</a>
					</c:otherwise>
				</c:choose>
			</div>
			<table cellspacing="1" class="list-table">
				<tbody>
					<tr>
						<td colspan="8" class="topic">基本設定</td>
					</tr>
					<tr>
						<th width="50" align="center">&nbsp;</th>
						<th>名稱</th>
						<th>種類</th>
						<th>建立者</th>
						<th>更新者</th>
						<th>操作</th>
					</tr>
					<c:forEach var="item" items="${ds.results}" varStatus="status">
						<tr>
							<td align="center" class="td_first" nowrap><input
								type="checkbox" class="checkbox" name="checkItem"
								value="${item.serNo}"></td>
							<td><c:out value="${item.bookName }" /></td>
							<td align="center">${item.resourcesBuyers.rType.type }</td>
							<td><c:out value="${item.cUid }" /></td>
							<td align="center"><c:out value="${item.uUid }" /></td>
							<td align="center"><a class="state-default2"
								onclick="goView(${item.serNo });"><span
									class="icon-default icon-view"></span>檢視</a> <a
								class="state-default2" onclick="goUpdate(${item.serNo});"><span
									class="icon-default icon-edit"></span>修改</a></td>
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
										<jsp:param name="action" value="apply.ebook.list" />
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
									</c:set> 每頁顯示 <select name="recordPerPage" id="listForm_pageSize"
									onchange="chagePageSize(this.value,${ds.pager.recordPoint })">
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
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>