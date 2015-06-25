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
	var saveForm = "";
	var updateForm = "";
	$(document).ready(function() {
		saveForm = $("form#apply_accountNumber_save").html();
		updateForm = $("form#apply_accountNumber_update").html();
	});

	$(document).ready(
			function() {
				$("select#apply_accountNumber_save_cusSerNo").children().each(
						function() {
							if ($(this).val() == "${entity.customer.serNo}") {
								this.selected = true;
							}
						});

				$("select#apply_accountNumber_save_role").children().each(
						function() {
							if ($(this).text() == "${entity.role.role}") {
								this.selected = true;
							}
						});

				$("select#apply_accountNumber_save_status").children().each(
						function() {
							if ($(this).text() == "${entity.status.status}") {
								this.selected = true;
							}
						});

				$("select#apply_accountNumber_update_cusSerNo").children()
						.each(function() {
							if ($(this).val() == "${entity.customer.serNo}") {
								this.selected = true;
							}
						});

				$("select#apply_accountNumber_update_role").children().each(
						function() {
							if ($(this).text() == "${entity.role.role}") {
								this.selected = true;
							}
						});

				$("select#apply_accountNumber_update_status").children().each(
						function() {
							if ($(this).text() == "${entity.status.status}") {
								this.selected = true;
							}
						});
			});

	//重設所有欄位(清空)
	function resetData() {
		$("form#apply_accountNumber_save").html(saveForm);
		$("form#apply_accountNumber_update").html(updateForm);
	}

	//遞交表單
	function submitData() {
		closeDetail();
		var data = "";
		if ($("form#apply_accountNumber_save").length != 0) {
			data = $('#apply_accountNumber_save').serialize();
			goDetail(
					"<c:url value = '/'/>crud/apply.accountNumber.save.action",
					'帳戶-新增', data);
		} else {
			data = $('#apply_accountNumber_update').serialize();
			goDetail(
					"<c:url value = '/'/>crud/apply.accountNumber.update.action?entity.serNo=${entity.serNo}",
					'帳戶-修改', data);
		}
	}
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty entity.serNo }">
			<s:form namespace="/crud" action="apply.accountNumber.save">
				<table cellspacing="1" class="detail-table">
					<tr>
						<th width="130">用戶代碼<span class="required">(&#8226;)</span></th>
						<td><s:textfield name="entity.userId" cssClass="input_text" /></td>
					</tr>
					<tr>
						<th width="130">用戶密碼<span class="required">(&#8226;)</span></th>
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
									<s:select name="cusSerNo" cssClass="input_text"
										list="dsCustomer.results" listKey="serNo" listValue="name" />
								</c:when>
								<c:otherwise>
									<s:select headerValue="--用戶名稱--" headerKey="0" name="cusSerNo"
										cssClass="input_text" list="dsCustomer.results"
										listKey="serNo" listValue="name" />
								</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<th width="130">Email</th>
						<td><s:textfield name="entity.email" cssClass="input_text" /></td>
					</tr>
					<tr>
						<th width="130">帳戶角色</th>
						<td><select name="role" id="apply_accountNumber_save_role"
							class="input_text">
								<c:forEach var="item" items="${roleList}" varStatus="status">
									<option value="${item.role }">${item.role }</option>
								</c:forEach>
						</select></td>
					</tr>
					<tr>
						<th width="130">狀態</th>
						<td><select name="status"
							id="apply_accountNumber_save_status" class="input_text">
								<c:forEach var="item" items="${statusList}" varStatus="status">
									<option value="${item.status }">${item.status }</option>
								</c:forEach>
						</select></td>
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
		</c:when>
		<c:otherwise>
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
									<s:select name="cusSerNo" cssClass="input_text"
										list="dsCustomer.results" listKey="serNo" listValue="name" />
								</c:when>
								<c:otherwise>
									<s:select headerValue="--用戶名稱--" headerKey="0" name="cusSerNo"
										cssClass="input_text" list="dsCustomer.results"
										listKey="serNo" listValue="name" />
								</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<th width="130">帳戶角色</th>
						<td><select name="role" id="apply_accountNumber_save_role"
							class="input_text">
								<c:forEach var="item" items="${roleList}" varStatus="status">
									<option value="${item.role }">${item.role }</option>
								</c:forEach>
						</select></td>
					</tr>
					<tr>
						<th width="130">Email</th>
						<td><s:textfield name="entity.email" cssClass="input_text" /></td>
					</tr>
					<tr>
						<th width="130">狀態</th>
						<td><select name="status"
							id="apply_accountNumber_save_status" class="input_text">
								<c:forEach var="item" items="${statusList}" varStatus="status">
									<option value="${item.status }">${item.status }</option>
								</c:forEach>
						</select></td>
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
		</c:otherwise>
	</c:choose>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>