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
//IE press Enter GoPage
$(document).ready(function() {
	$("input#listForm_currentPageHeader:(1)").keyup(function(e){
		if(e.keyCode == 13){gotoPage_detail($(this).val());}
	});
});
	
//新增IP Range
function goAdd_detail() {
	var url = "<c:url value = '/'/>/crud/apply.ipRange.add.action";
	var data ='entity.customer.serNo='+'${entity.customer.serNo }';
	goDetail_2(url, 'IP Range管理-新增', data);
}

//IP Range編輯
function goUpdate_detail(listNo,serNo) {
	var isNum = /^\d+$/.test(serNo);
	var islistNo = /^\d+$/.test(listNo);
	if (isNum && islistNo && parseInt(serNo) > 0){
		var url = "<c:url value = '/'/>crud/apply.ipRange.edit.action";
		var data = 'entity.serNo=' + serNo +'&entity.listNo='+listNo+'&entity.customer.serNo='+'${entity.customer.serNo }';
		goDetail_2(url, 'IP Range管理-修改', data); 
	}
}

//單筆刪除
function goDel_detail(serNo) {
	 var f = {
			 trueText:'是',
			 trueFunc:function(){
				 var url = "<c:url value = '/'/>crud/apply.ipRange.delete.action";
				 var data = $('#apply_ipRange_list').serialize()+'&entity.serNo='+serNo+'&entity.customer.serNo='+'${entity.customer.serNo }'+'&pager.currentPage='+'${ds.pager.currentPage}';
				 goDetail_Main(url,'',data);
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

//GoPage
function gotoPage_detail(page) {
	var isNum = /^\d+$/.test(page);
	var totalPage = $("span.totalNum:eq(1)").html();
	var recordPerPage = "${ds.pager.recordPerPage}";
	var offset = parseInt(recordPerPage) * (parseInt(page) - 1);
	if(!isNum){
		page="${ds.pager.currentPage}";
	} else {
		if (parseInt(page) < 1){
			page=1;
		}		
			
		if (parseInt(page) > parseInt(totalPage)){
			page=totalPage;
			
		}
	}
	
	goDetail_Main('<c:url value = '/'/>crud/apply.ipRange.list.action','#apply_ipRange_list', '&entity.customer.serNo='+'${entity.customer.serNo }'+'&pager.currentPage='+page);
}

//變更顯示筆數
function changePageSize_detail() {
	goDetail_Main('<c:url value = '/'/>crud/apply.ipRange.list.action','#apply_ipRange_list', '&entity.customer.serNo='+'${entity.customer.serNo }'+'&pager.recordPoint='+'${ds.pager.recordPoint }');
}
	
function closeDetail() {
	$("#div_Detail").hide();
	UI_Resize();
	$("#div_Detail .content > .header > .title").empty();
	$("#div_Detail .content > .contain").empty();
}
</script>
</head>
<body>
	<s:form action="apply.ipRange.list" namespace="/crud" method="post">
		<div class="list-box">
			<div class="list-buttons">
				<a class="state-default" onclick="goAdd_detail();">新增</a>
			</div>
			<table cellspacing="1" class="list-table">
				<tbody>
					<tr>
						<td colspan="4" class="topic">IP Range管理</td>
					</tr>
					<tr>
						<th>NO.</th>
						<th>IP Range</th>
						<th>操作</th>
					</tr>
					<c:forEach var="item" items="${ds.results}" varStatus="status">
						<c:set var="orderInt" scope="session"
							value="${ds.pager.offset+(status.index+1)}" />
						<tr>
							<td align="center">${orderInt}</td>
							<td align="center">${item.ipRangeStart}~${item.ipRangeEnd}</td>
							<td align="center"><a class="state-default2"
								onclick="goUpdate_detail(${orderInt},${item.serNo});"><span
									class="icon-default icon-edit"></span>修改</a> <a
								class="state-default2" onclick="goDel_detail(${item.serNo});"><span
									class="icon-default icon-delete"></span>刪除</a></td>
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
									<jsp:param name="action" value="apply.ipRange.list" />
									<jsp:param name="pager" value="${ds.pager}" />
									<jsp:param name="recordPerPage"
										value="${ds.pager.recordPerPage}" />
									<jsp:param name="detail" value="1" />
								</jsp:include></td>
							<td><c:set var="pageFactor"
									value="${ds.pager.totalRecord/ds.pager.recordPerPage}" /> <c:set
									var="totalPage">
									<fmt:formatNumber type="number" pattern="#"
										value="${pageFactor+(1-(pageFactor%1))%1}" />
								</c:set> 每頁顯示 <select id="listForm_pageSize" name="pager.recordPerPage"
								onchange="changePageSize_detail()">
									<option value="${ds.pager.recordPerPage}">${ds.pager.recordPerPage}</option>
									<option value="5">5</option>
									<option value="10">10</option>
									<option value="20">20</option>
									<option value="50">50</option>
							</select> 筆紀錄, 第 <input id="listForm_currentPageHeader"
								value="${ds.pager.currentPage }" type="number" min="1"
								max="${totalPage }" onchange="gotoPage_detail(this.value)">
								頁, 共<span class="totalNum">${totalPage }</span>頁</td>
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