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
//切換查詢項目
$(document).ready(function() {
	$("select#listForm_searchCondition").change(function() {
		$("input#search").attr("name", $(this).val());
	});
});

function goSearch(){
    goMain("<%=request.getContextPath()%>/crud/apply.journal.list.action",
			"#apply_journal_list", "");
}

//新增
function goAdd(){
        goDetail('<%=request.getContextPath()%>/crud/apply.journal.add.action','期刊-新增');
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
                        var url = "<c:url value='/crud/apply.journal.delete.action'/>";
                        var data = $('#apply_journal_list').serialize()+'&pager.currentPage='+'${ds.pager.currentPage}';
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
        var url = "<c:url value = '/'/>crud/apply.journal.view.action";
        var data = 'entity.serNo='+serNo;
        goDetail(url,'期刊-檢視',data);
}

//更新資料
function goUpdate(serNo) {
	goDetail('<%=request.getContextPath()%>/crud/apply.journal.edit.action?'+'entity.serNo='+serNo,'期刊-修改');
}

//批次匯入
function goImport(){
	goDetail('<%=request.getContextPath()%>/crud/apply.journal.imports.action','期刊-匯入');
}
</script>

</head>
<body>
	<s:form action="apply.journal.list" namespace="/crud" method="post"
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
							<td align="left"><s:select name="entity.option"
									id="listForm_searchCondition"
									list="#{'entity.chineseTitle':'中文刊名','entity.englishTitle':'英文刊名','entity.issn':'ISSN'}"></s:select>
							</td>
							<c:choose>
								<c:when test="${entity.option=='entity.issn' }">
									<td align="left"><s:textfield name="entity.issn"
											id="search" cssClass="input_text" /></td>
								</c:when>
								<c:when test="${entity.option=='entity.englishTitle' }">
									<td align="left"><s:textfield name="entity.englishTitle"
											id="search" cssClass="input_text" /></td>
								</c:when>
								<c:otherwise>
									<td align="left"><s:textfield name="entity.chineseTitle"
											id="search" cssClass="input_text" /></td>
								</c:otherwise>
							</c:choose>
							<td align="left"><a class="state-default"
								onclick="goSearch()">查詢</a>&nbsp;&nbsp;</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div id="div_nav">
			目前位置：<span>書目資料管理</span> &gt; <span>期刊</span>
		</div>
		<div class="list-box">
			<div class="list-buttons">
				<c:choose>
					<c:when
						test="${(not empty ds.pager.totalRecord)&& (0 ne ds.pager.totalRecord) }">
						<a class="state-default" onclick="allSelect(1)">全選</a>
						<a class="state-default" onclick="allSelect(0)">取消</a>
						<a class="state-default" onclick="goAdd()">新增</a>
						<a class="state-default" onclick="goDelete()">刪除</a>
						<a class="state-default" onclick="goImport()">批次匯入</a>
					</c:when>
					<c:otherwise>
						<a class="state-default" onclick="goAdd()">新增</a>
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
								type="checkbox" class="checkbox" name="entity.checkItem"
								value="${item.serNo}"></td>
							<td><esapi:encodeForHTML>${item.englishTitle }</esapi:encodeForHTML></td>
							<td align="center">${item.resourcesBuyers.type }</td>
							<td>${item.cUid }</td>
							<td align="center">${item.uUid }</td>
							<td align="center"><a class="state-default2"
								onclick="goView(${item.serNo })"><span
									class="icon-default icon-view"></span>檢視</a> <a
								class="state-default2" onclick="goUpdate(${item.serNo})"><span
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
										<jsp:param name="action" value="apply.journal.list" />
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

	<s:if test="hasActionMessages()">
		<script language="javascript" type="text/javascript">
            var msg = "";
            <s:iterator value="actionMessages">msg += '<s:property escape="true"/><br>';
            </s:iterator>;
            goAlert('訊息', msg);
        </script>
	</s:if>
	<s:if test="hasActionErrors()">
		<script language="javascript" type="text/javascript">
			var msg = "";
			<s:iterator value="actionErrors">
			msg += '<s:property escape="true"/><br>';
			</s:iterator>;
			goAlert('訊息', msg);
		</script>
	</s:if>
</body>
</html>