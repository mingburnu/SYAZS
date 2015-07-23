<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<c:choose>
	<c:when
		test="${empty entity.firstLevelOption||(('extend' ne entity.firstLevelOption)&&('new' ne entity.firstLevelOption))}">
		<script type="text/javascript">
			$(document).ready(
					function() {
						$("input[name='entity.firstLevelOption']:eq(1)").attr(
								"checked", true);

					});
		</script>
	</c:when>
	<c:otherwise>
		<c:if
			test="${('extend' ne entity.firstLevelOption) && ('new' ne entity.firstLevelOption)}">
			<script type="text/javascript">
				$(document).ready(
						function() {
							$("input[name='entity.firstLevelOption']:eq(1)")
									.attr("checked", true);

						});
			</script>
		</c:if>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
	var updateForm = "";
	$(document).ready(function() {
		updateForm = $("form#apply_group_update").html();
	});

	$(document)
			.ready(
					function() {
						if ($("select#apply_group_save_entity_firstLevelSelect")
								.val() == 0) {
							$("input#checkLevel2:eq(0)").next().next().next()
									.attr('disabled', true);
							$("input#checkLevel2:eq(0)").next().attr(
									'disabled', true);
							$("input#checkLevel2:eq(0)").attr('disabled', true);
						}

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

	$(document)
			.ready(
					function() {
						$(
								"input#apply_group_save_entity_firstLevelOptionextend")
								.click(
										function() {
											if ($(
													"select#apply_group_save_entity_firstLevelSelect")
													.val() != 0) {
												$("input#checkLevel2:eq(0)")
														.next()
														.next()
														.next()
														.attr('disabled', false);
												$("input#checkLevel2:eq(0)")
														.next().attr(
																'disabled',
																false);
												$("input#checkLevel2:eq(0)")
														.attr('disabled', false);
											}
										});

						$("input#apply_group_save_entity_firstLevelOptionnew")
								.click(
										function() {
											$("input#checkLevel2:eq(0)").next()
													.next().next().attr(
															'disabled', true);
											$("input#checkLevel2:eq(0)").next()
													.attr('disabled', true);
											$("input#checkLevel2:eq(0)").attr(
													'disabled', true);
										});

						if ($(
								"input#apply_group_save_entity_firstLevelOptionnew")
								.attr('checked') == true) {
							$("input#checkLevel2:eq(0)").next().next().next()
									.attr('disabled', true);
							$("input#checkLevel2:eq(0)").next().attr(
									'disabled', true);
							$("input#checkLevel2:eq(0)").attr('disabled', true);
						}
						;

					});

	$(document)
			.ready(
					function() {
						$("input#checkLevel2")
								.click(
										function(e) {
											$(this).next().attr('checked',
													$(this).attr('checked'));

											if ($("input#checkLevel2").length == 2) {
												if ($("input#checkLevel2:eq(0)")
														.attr('checked')) {
													$("input#checkLevel3")
															.attr(
																	'checked',
																	$(this)
																			.attr(
																					'checked'));
													$("input#checkLevel3")
															.next()
															.attr(
																	'checked',
																	$(this)
																			.attr(
																					'checked'));
												}
											}

											$("input#checkLevel2")
													.each(
															function() {
																if ($(this)
																		.next()
																		.attr(
																				'checked') != $(
																		this)
																		.attr(
																				'checked')) {
																	$(this)
																			.attr(
																					'checked',
																					$(
																							this)
																							.next()
																							.attr(
																									'checked'));
																}
															});
										});

						$("input#checkLevel3")
								.click(
										function(e) {
											$(this).next().attr('checked',
													$(this).attr('checked'));
											if ($("input#checkLevel2").length == 2) {
												if ($("input#checkLevel2:eq(0)")
														.attr('checked')) {
													$(this)
															.next()
															.attr(
																	'checked',
																	$(
																			"input#checkLevel2:eq(0)")
																			.attr(
																					'checked'));
													$(this)
															.attr(
																	'checked',
																	$(
																			"input#checkLevel2:eq(0)")
																			.attr(
																					'checked'));
												}
											}
										});

					});

	//重設所有欄位(清空)
	function resetData() {
		var url = "<c:url value = '/'/>/crud/apply.group.add.action";
		var data = 'entity.customer.serNo=${entity.customer.serNo}';
		goDetail_2(url, '客戶-群組新增', data);
	}

	//遞交表單
	function submitData() {
		closeDetail_2();
		if ($("form#apply_group_save").length != 0) {
			var data = $('form#apply_group_save').serialize();
			goDetail_2(
					"<c:url value = '/'/>crud/apply.group.save.action?entity.customer.serNo=${entity.customer.serNo}",
					'客戶-群組新增', data);
			console.log(data);
		} else {
			var data = $('form#apply_group_update').serialize();
			goDetail_2(
					"<c:url value = '/'/>crud/apply.group.update.action?entity.serNo=${entity.serNo}&entity.customer.serNo=${entity.customer.serNo}",
					'客戶-群組修改', data);
		}
	}

	function showSubGroups() {
		var data = $('form#apply_group_save').serialize();
		console.log(data);
		goDetail_2(
				"<c:url value = '/'/>crud/apply.group.add.action?entity.customer.serNo=${entity.customer.serNo}",
				'客戶-群組新增', data);
	}
</script>
<style type="text/css">
input[name="entity.secondLevelOption"] {
	display: none;
}

input[name="entity.thirdLevelOption"] {
	display: none;
}
</style>
</head>
<body>
	<div>${success }</div>
	<c:choose>
		<c:when test="${empty entity.serNo }">
			<s:form namespace="/crud" action="apply.group.save">
				<table cellspacing="1" class="detail-table">
					<tr>
						<th width="130">用戶名稱<span class="required">(&#8226;)</span></th>
						<td>${entity.customer.name }</td>
					</tr>
					<tr>
						<th width="130">LEVEL 1<span class="required">(&#8226;)</span></th>
						<td><s:radio list="#{'extend':''}"
								name="entity.firstLevelOption" /> <s:select
								cssClass="input_text" headerValue="--選擇群組--" headerKey="0"
								name="entity.firstLevelSelect" list="firstLevelGroups"
								listKey="serNo" listValue="groupName" onchange="showSubGroups()" />
							<s:radio list="#{'new':'新增Level 1群組'}"
								name="entity.firstLevelOption" /> <s:textfield
								cssClass="input_text" name="entity.firstLevelName" /></td>
					</tr>
					<tr>
						<th width="130">LEVEL 2</th>
						<td><input type="checkbox" id="checkLevel2"> <s:radio
								list="#{'extend':''}" name="entity.secondLevelOption" /> <s:select
								cssClass="input_text" headerValue="--選擇群組--" headerKey="0"
								name="entity.secondLevelSelect" list="secondLevelGroups"
								listKey="serNo" listValue="groupName" onchange="showSubGroups()" />
							<input type="checkbox" id="checkLevel2"> <s:radio
								list="#{'new':'新增Level 2群組'}" name="entity.secondLevelOption" />
							<s:textfield name="entity.secondLevelName" cssClass="input_text" /></td>
					</tr>
					<tr>
						<th width="130">LEVEL 3</th>
						<td><input type="checkbox" disabled> <s:select
								id="viewGroups" cssClass="input_text" headerValue="--觀看群組--"
								headerKey="0" list="thirdLevelGroups" listKey="serNo"
								listValue="groupName" /> <input type="checkbox"
							id="checkLevel3"> <s:radio list="#{'new':'新增Level 3群組'}"
								name="entity.thirdLevelOption" /> <s:textfield
								name="entity.thirdLevelName" cssClass="input_text" /></td>
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
		</c:when>
		<c:otherwise>
			<s:form namespace="/crud" action="apply.group.update">
				<table cellspacing="1" class="detail-table">
					<tr>
						<th width="130">用戶名稱<span class="required">(&#8226;)</span></th>
						<td>${entity.customer.name }</td>
					</tr>
					<tr>
						<th width="130"></th>
						<td><s:select headerValue="--用戶名稱--" headerKey="0"
								name="cusSerNo" cssClass="input_text"
								list="dsFirstLevel.results" listKey="serNo" listValue="name" />
							<s:textfield name="entity.group" cssClass="input_text" /></td>
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
		</c:otherwise>
	</c:choose>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>