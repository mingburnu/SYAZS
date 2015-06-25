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
		saveForm = $("form#apply_customer_save").html();
		updateForm = $("form#apply_customer_update").html();
	});

	//重設所有欄位(清空)
	function resetData() {
		$("form#apply_customer_save > table.detail-table tr td input").val("");
		$("form#apply_customer_update").html(updateForm);
	}

	//遞交表單
	function submitData() {
		closeDetail();
		var data = "";
		if ($("form#apply_customer_save").length != 0) {
			data = $('#apply_customer_save').serialize();
			goDetail("<c:url value = '/'/>crud/apply.customer.save.action",
					'客戶-新增', data);
		} else {
			data = $('#apply_customer_update').serialize();
			goDetail(
					"<c:url value = '/'/>crud/apply.customer.update.action?entity.serNo=${entity.serNo}",
					'客戶-修改', data);
		}
	}
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty entity.serNo }">
			<s:form namespace="/crud" action="apply.customer.save">
				<table cellspacing="1" class="detail-table">
					<tr>
						<th width="130">用戶名稱<span class="required">(&#8226;)</span></th>
						<td><s:textfield name="entity.name" cssClass="input_text" /></td>
					</tr>
					<tr>
						<th width="130">用戶英文名稱</th>
						<td><s:textfield name="entity.engName" cssClass="input_text" /></td>
					</tr>
					<tr>
						<th width="130">聯絡人</th>
						<td><s:textfield name="entity.contactUserName"
								cssClass="input_text" /></td>
					</tr>
					<tr>
						<th width="130">地址</th>
						<td><s:textfield name="entity.address" cssClass="input_text" /></td>
					</tr>
					<tr>
						<th width="130">電話</th>
						<td><s:textfield name="entity.tel" cssClass="input_text" /></td>
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
			<s:form namespace="/crud" action="apply.customer.update">
				<table cellspacing="1" class="detail-table">
					<tr>
						<th width="130">用戶名稱<span class="required">(&#8226;)</span></th>
						<td><c:out value="${entity.name }" /></td>
					</tr>
					<tr>
						<th width="130">用戶英文名稱</th>
						<td><s:textfield name="entity.engName" cssClass="input_text" /></td>
					</tr>
					<tr>
						<th width="130">聯絡人</th>
						<td><s:textfield name="entity.contactUserName"
								cssClass="input_text" /></td>
					</tr>
					<tr>
						<th width="130">地址</th>
						<td><s:textfield name="entity.address" cssClass="input_text" /></td>
					</tr>
					<tr>
						<th width="130">電話</th>
						<td><s:textfield name="entity.tel" cssClass="input_text" /></td>
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