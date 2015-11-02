<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						if ($("select#viewGroups option").length == 1) {
							$("select#viewGroups").attr('disabled', true);
						} else {
							if ($("select#viewGroups option").length == 2) {
								if ($("select#viewGroups option:eq(1)").val() == null
										|| $("select#viewGroups option:eq(1)")
												.val() == '') {
									$("select#viewGroups").attr('disabled',
											true);
								}
							}
						}

						$("input#checkLevel2")
								.each(
										function() {
											if ($(this).next().attr('checked') != $(
													this).attr('checked')) {
												$(this).attr(
														'checked',
														$(this).next().attr(
																'checked'));
											}
										});

						$("input#checkLevel3")
								.each(
										function() {
											if ($(this).next().attr('checked') != $(
													this).attr('checked')) {
												$(this).attr(
														'checked',
														$(this).next().attr(
																'checked'));
											}
										});
					});

	//重設所有欄位(清空)
	function resetData() {
		var url = "<c:url value = '/'/>/crud/apply.group.edit.action";
		var data = 'entity.customer.serNo=${entity.customer.serNo}'
				+ '&entity.serNo=${entity.serNo}';
		goDetail_2(url, '客戶-群組修改', data);
	}

	//遞交表單
	function submitData() {
		var data = $('form#apply_group_update').serialize();
		closeDetail_2();
		goDetail_2(
				"<c:url value = '/'/>crud/apply.group.update.action?entity.serNo=${entity.serNo}&entity.customer.serNo=${entity.customer.serNo}",
				'客戶-群組修改', data);
	}

	function showSubGroups() {
		var data = $('form#apply_group_update').serialize();
		console.log(data);
		goDetail_2(
				"<c:url value = '/'/>crud/apply.group.edit.action?entity.customer.serNo=${entity.customer.serNo}"
						+ "&entity.serNo=${entity.serNo}", '客戶-群組修改', data);
	}
</script>
<style type="text/css">
select.input_text {
    width: 103px;
}

input[name="entity.secondLevelOption"] {
	display: none;
}

input[name="entity.thirdLevelOption"] {
	display: none;
}
</style>
</head>
<body>
	<s:form namespace="/crud" action="apply.group.update">
		<table cellspacing="1" class="detail-table">
			<tr>
				<th width="130">用戶名稱<span class="required">(&#8226;)</span></th>
				<td>${entity.customer.name }</td>
			</tr>
			<tr>
				<th width="130">LEVEL 1<span class="required">(&#8226;)</span></th>
				<td><c:choose>
						<c:when test="${1 eq entity.groupMapping.level }">
							<s:radio list="#{'extend':''}" name="entity.firstLevelOption"
								disabled="true" />
							<c:choose>
								<c:when test="${not empty entity.firstLevelGroups }">
									<s:select cssClass="input_text" headerValue="--觀看群組--"
										headerKey="0" list="entity.firstLevelGroups" listKey="serNo"
										listValue="groupName" />
								</c:when>
								<c:otherwise>
									<select class="input_text">
										<option value="0">--選擇群組--</option>
									</select>
								</c:otherwise>
							</c:choose>
							<s:radio list="#{'modify':'修改Level 1群組'}"
								name="entity.firstLevelOption" disabled="true" value="'modify'" />
							<s:textfield cssClass="input_text" name="entity.firstLevelName" />
						</c:when>
						<c:when test="${3 eq entity.groupMapping.level }">
							<s:radio list="#{'extend':''}" name="entity.firstLevelOption"
								disabled="true" value="'extend'" />
							<select class="input_text" name="entity.firstLevelSelect"><option
									value="${entity.firstLevelSelect}"><esapi:encodeForHTML>${entity.groupMapping.parentGroupMapping.parentGroupMapping.group.groupName}</esapi:encodeForHTML></option></select>
							<s:radio list="#{'modify':'修改Level 1群組'}"
								name="entity.firstLevelOption" disabled="true" />
							<s:textfield cssClass="input_text" disabled="true" />
						</c:when>
						<c:otherwise>
							<s:radio list="#{'extend':''}" name="entity.firstLevelOption"
								disabled="true" value="'extend'" />
							<c:choose>
								<c:when test="${not empty entity.firstLevelGroups }">
									<s:select cssClass="input_text" name="entity.firstLevelSelect"
										list="entity.firstLevelGroups" listKey="serNo"
										listValue="groupName" onchange="showSubGroups()" />
								</c:when>
								<c:otherwise>
									<select class="input_text">
										<option value="0">--選擇群組--</option>
									</select>
								</c:otherwise>
							</c:choose>
							<s:radio list="#{'modify':'修改Level 1群組'}"
								name="entity.firstLevelOption" disabled="true" />
							<s:textfield cssClass="input_text" disabled="true" />
						</c:otherwise>
					</c:choose></td>
			</tr>
			<tr>
				<th width="130">LEVEL 2</th>
				<td><c:choose>
						<c:when test="${2 eq entity.groupMapping.level }">
							<input type="checkbox" id="checkLevel2" disabled="disabled">
							<s:radio list="#{'extend':''}" name="entity.secondLevelOption" />
							<c:choose>
								<c:when test="${not empty entity.secondLevelGroups }">
									<s:select cssClass="input_text" headerValue="--觀看群組--"
										headerKey="0" list="entity.secondLevelGroups" listKey="serNo"
										listValue="groupName" />
								</c:when>
								<c:otherwise>
									<select class="input_text">
										<option value="0">--觀看群組--</option>
									</select>
								</c:otherwise>
							</c:choose>

							<input type="checkbox" id="checkLevel2" disabled="disabled">
							<s:radio list="#{'modify':'修改Level 2群組'}"
								name="entity.secondLevelOption" disabled="true" value="'modify'" />
							<s:textfield name="entity.secondLevelName" cssClass="input_text" />
						</c:when>
						<c:when test="${3 eq entity.groupMapping.level }">
							<input type="checkbox" id="checkLevel2" disabled="disabled">
							<s:radio list="#{'extend':''}" name="entity.secondLevelOption" />
							<c:choose>
								<c:when test="${not empty entity.secondLevelGroups }">
									<s:select cssClass="input_text" name="entity.secondLevelSelect"
										list="entity.secondLevelGroups" listKey="serNo"
										listValue="groupName" onchange="showSubGroups()" />
								</c:when>
								<c:otherwise>
									<select class="input_text">
										<option value="0">--選擇群組--</option>
									</select>
								</c:otherwise>
							</c:choose>
							<input type="checkbox" id="checkLevel2" disabled="disabled">
							<s:radio list="#{'modify':'修改Level 2群組'}"
								name="entity.secondLevelOption" disabled="true" />
							<s:textfield cssClass="input_text" disabled="true" />
						</c:when>
						<c:otherwise>
							<input type="checkbox" id="checkLevel2" disabled="disabled">
							<s:radio list="#{'extend':''}" name="entity.secondLevelOption"
								disabled="true" />
							<s:select cssClass="input_text" list="#{'0':'--選擇群組--'}"
								disabled="true" />
							<input type="checkbox" id="checkLevel2" disabled="disabled">
							<s:radio list="#{'modify':'修改Level 2群組'}"
								name="entity.secondLevelOption" disabled="true" />
							<s:textfield cssClass="input_text" disabled="true" />
						</c:otherwise>
					</c:choose></td>
			</tr>
			<tr>
				<th width="130">LEVEL 3</th>
				<td><c:choose>
						<c:when test="${3 eq entity.groupMapping.level }">
							<input type="checkbox" disabled>
							<c:choose>
								<c:when test="${not empty entity.thirdLevelGroups }">
									<s:select id="viewGroups" cssClass="input_text"
										headerValue="--觀看群組--" headerKey="0"
										list="entity.thirdLevelGroups" listKey="serNo"
										listValue="groupName" />
								</c:when>
								<c:otherwise>
									<select class="input_text">
										<option value="0">--觀看群組--</option>
									</select>
								</c:otherwise>
							</c:choose>

							<input type="checkbox" id="checkLevel3" disabled="disabled">
							<s:radio list="#{'modify':'修改Level 3群組'}"
								name="entity.thirdLevelOption" />
							<s:textfield name="entity.thirdLevelName" cssClass="input_text" />
						</c:when>
						<c:otherwise>
							<input type="checkbox" disabled>
							<select id="viewGroups" class="input_text" disabled="disabled"><option
									value="0">--觀看群組--</option></select>
							<input type="checkbox" id="checkLevel3" disabled="disabled">
							<s:radio list="#{'modify':'修改Level 3群組'}"
								name="entity.thirdLevelOption" disabled="disabled" />
							<s:textfield cssClass="input_text" disabled="true" />
						</c:otherwise>
					</c:choose></td>
			</tr>
		</table>
		<div class="button_box">
			<div class="detail-func-button">
				<a class="state-default" onclick="closeDetail_2();">取消</a> &nbsp;<a
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