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
		goDetail("<c:url value = '/'/>/crud/apply.classification.edit.action?"
				+ 'entity.serNo=${entity.serNo}', '分類法-修改');
	}

	//遞交表單
	function submitData() {
		var data = $('#apply_classification_update').serialize();
		closeDetail();
		goDetail(
				"<c:url value = '/'/>crud/apply.classification.update.action?entity.serNo=${entity.serNo}",
				'分類法-修改', data);
	}
</script>
</head>
<body>
	<s:form namespace="/crud" action="apply.classification.update">
		<table cellspacing="1" class="detail-table">
			<tr>
				<th width="130">ID</th>
				<td>${entity.serNo }</td>
			</tr>
			<tr>
				<th width="130">名稱<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.classname" cssClass="input_text" /></td>
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