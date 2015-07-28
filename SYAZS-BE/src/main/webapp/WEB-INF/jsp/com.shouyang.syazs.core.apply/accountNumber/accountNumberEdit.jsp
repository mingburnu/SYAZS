<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	
	//重設所有欄位(清空)
	function resetData() {
		goDetail('<%=request.getContextPath()%>/crud/apply.accountNumber.edit.action?'
						+ 'entity.serNo=${entity.serNo}', '帳戶-修改');
	}

	//遞交表單
	function submitData() {
		var data = $('#apply_accountNumber_update').serialize();
		closeDetail();
		goDetail(
				"<c:url value = '/'/>crud/apply.accountNumber.update.action?entity.serNo=${entity.serNo}",
				'帳戶-修改', data);
	}
</script>
</head>
<body>
	<s:form namespace="/crud" action="apply.accountNumber.update">
		<table cellspacing="1" class="detail-table">
			<tr>
				<th width="130">用戶代碼<span class="required">(&#8226;)</span></th>
				<td><c:out value="${entity.userId }" /></td>
			</tr>
			<tr>
				<th width="130">用戶密碼</th>
				<td><s:password name="entity.userPw" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">用戶姓名<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.userName" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">用戶名稱<span class="required">(&#8226;)</span></th>
				<td><c:choose>
						<c:when test="${login.role.role == '管理員' }">
							<s:select name="entity.customer.serNo" cssClass="input_text"
								list="ds.datas" listKey="value" listValue="key" />
						</c:when>
						<c:otherwise>
							<s:select headerValue="--用戶名稱--" headerKey="0"
								name="entity.customer.serNo" cssClass="input_text"
								list="ds.datas" listKey="value" listValue="key" />
						</c:otherwise>
					</c:choose></td>
			</tr>
			<tr>
				<th width="130">Email</th>
				<td><s:textfield name="entity.email" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">帳戶角色</th>
				<td><s:select name="entity.role" list="roleList"
						listKey="name()" listValue="role" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">狀態</th>
				<td><s:select name="entity.status"
						list="@com.shouyang.syazs.core.apply.enums.Status@values()"
						listKey="name()" listValue="status" cssClass="input_text" /></td>
			</tr>
		</table>
		<div class="button_box">
			<div class="detail-func-button">
				<a class="state-default" onclick="closeDetail();">取消</a> &nbsp;<a
					class="state-default" onclick="resetData();">重設</a>&nbsp; <a
					class="state-default" onclick="submitData();">確認</a>
			</div>
		</div>
		<div class="detail_note">
			<div class="detail_note_title">Note</div>
			<div class="detail_note_content">
				<span class="required">(&#8226;)</span>為必填欄位
			</div>
		</div>
	</s:form>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>