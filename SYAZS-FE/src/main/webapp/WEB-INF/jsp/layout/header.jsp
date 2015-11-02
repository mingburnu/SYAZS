<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
	function goURL(url) {
		$.ajax({
			url : url,
			success : function(result) {
				$("#container").html(result);
			}
		});
		$("body").scrollTop(0);
	}

	function switchMenu(thisItem) {
		$("a.a_hover").removeAttr("class");
		$(thisItem).attr("class", "a_hover");
	}
</script>
<div id="header">
	<img src="<c:url value = '/'/>/resources/images/header.png" width="900"
		height="190">
</div>
<div id="menu_box">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td><a class="a_hover"
				href="javascript:goURL('<c:url value = '/'/>page/query.action')"
				onclick="switchMenu(this)">綜 合 查 詢</a></td>
			<td><a
				href="javascript:goURL('<c:url value = '/'/>page/journal.action')"
				onclick="switchMenu(this)">僅 電 子 期 刊</a></td>
			<td><a
				href="javascript:goURL('<c:url value = '/'/>page/ebook.action')"
				onclick="switchMenu(this)">僅 電 子 書</a></td>
			<td><a
				href="javascript:goURL('<c:url value = '/'/>crud/apply.database.all.action')"
				onclick="switchMenu(this)">僅 資 料 庫</a></td>
		</tr>
	</table>
</div>
<!-- func_box 開始 -->
<div id="func_box">
	<div class="box_in">
		<span class="txt">${login.customer.name}，${login.customer.contactUserName}</span>
		<c:choose>
			<c:when test="${not empty login.serNo }">
				<a class="btn_02"
					href='<s:url namespace="/authorization" action="logout" />'><span>登出</span></a>
			</c:when>
			<c:otherwise>
				<a class="btn_02" href="<c:url value = '/'/>login.jsp"><span>登入</span></a>
			</c:otherwise>
		</c:choose>
	</div>
</div>
<!-- func_box 結束 -->