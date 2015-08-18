<%@ page import="java.util.*"%>
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
	$(document)
			.ready(
					function() {
						var contain = $("#div_Detail_2 .content .header .title")
								.html();
						if (contain != '單位-新增') {
							goReferenceOwners(
									"<c:url value = '/'/>crud/apply.referenceOwner.box.action",
									'單位-新增');
						}
					});

	$(document)
			.ready(
					function() {
						$("#div_Detail .content .header .close")
								.html(
										'<a href="#" onclick="clearReferenceOwners();closeDetail();">關閉</a>');
					});

	$(document).ready(function() {
		$("img#minus").click(function() {
			value = $(this).next().val();
			$(this).next().attr("name", "");
			$(this).parent().hide();

			$("input#referenceOwner_unit").each(function() {
				if ($(this).val() == value) {
					$(this).attr("checked", false);
				}
			});
		});
	});

	//重設所有欄位(清空)
	function resetData() {
		$("[id^='apply_journal_save_entity'][type!='radio']").val("");

		for (var i = 0; i < $("input[type='radio']").length; i++) {
			$("input[type='radio']:eq(" + i + ")").val(
					$("input[type='radio']:eq(" + i + ")").next().html());
		}

		$("input[type='radio']:eq(2)").attr("checked", true);
		$("input[type='radio']:eq(4)").attr("checked", true);

		allSelect_referenceOwners(0);
		checkData();
	}

	//遞交表單
	function submitData() {
		var data = $('#apply_journal_save').serialize();
		closeDetail();
		clearReferenceOwners();
		goDetail("<c:url value = '/'/>crud/apply.journal.save.action", '期刊-新增',
				data);
	}
</script>
<style type="text/css">
#div_Detail_2 {
	display: none;
}

img#add,img#minus {
	position: relative;
	top: 5px;
	left: 5px;
}

input#referenceOwner_name {
	background-color: #aaaaaa;
}
</style>
</head>
<body>
	<s:form namespace="/crud" action="apply.journal.save">
		<table cellspacing="1" class="detail-table">
			<tr>
				<th width="130">刊名<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.title"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">英文縮寫刊名</th>
				<td><s:textfield name="entity.abbreviationTitle"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">ISSN<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.issn" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">語文</th>
				<td><s:textfield name="entity.languages" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">出版項</th>
				<td><s:textfield name="entity.publishName"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">出版年</th>
				<td><s:textfield name="entity.publishYear"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">刊別</th>
				<td><s:textfield name="entity.publication"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">國會分類號</th>
				<td><s:textfield name="entity.congressClassification"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">起始日</th>
				<td><s:textfield name="resourcesBuyers.startDate"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">到期日</th>
				<td><s:textfield name="resourcesBuyers.maturityDate"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">URL</th>
				<td><s:textfield name="entity.resourcesBuyers.url"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">資源類型</th>
				<td><c:choose>
						<c:when
							test="${(empty entity.resourcesBuyers.category) || ('不明' eq entity.resourcesBuyers.category) }">
							<s:radio name="entity.resourcesBuyers.category"
								list="categoryList" listKey="name()" listValue="category"
								value="'未註明'" />
						</c:when>
						<c:otherwise>
							<s:radio name="entity.resourcesBuyers.category"
								list="categoryList" listKey="name()" listValue="category" />
						</c:otherwise>
					</c:choose></td>
			</tr>
			<tr>
				<th width="130">資源種類</th>
				<td><c:choose>
						<c:when test="${empty entity.resourcesBuyers.type }">
							<s:radio name="entity.resourcesBuyers.type"
								list="@com.shouyang.syazs.module.apply.enums.Type@values()"
								listKey="name()" listValue="type" value="'期刊'" />
						</c:when>
						<c:otherwise>
							<s:radio name="entity.resourcesBuyers.type"
								list="@com.shouyang.syazs.module.apply.enums.Type@values()"
								listKey="name()" listValue="type" />
						</c:otherwise>
					</c:choose></td>
			</tr>
			<tr>
				<th width="130">公開資源</th>
				<td><c:choose>
						<c:when test="${empty entity.resourcesBuyers.openAccess }">
							<s:radio name="entity.resourcesBuyers.openAccess"
								list="#@java.util.LinkedHashMap@{true:'是',false:'否'}"
								value="false" />
						</c:when>
						<c:otherwise>
							<s:radio name="entity.resourcesBuyers.openAccess"
								list="#@java.util.LinkedHashMap@{true:'是',false:'否'}" />
						</c:otherwise>
					</c:choose></td>
			</tr>
			<tr>
				<th width="130">資料庫中文題名</th>
				<td><s:textfield name="resourcesBuyers.dbChtTitle"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">資料庫英文題名</th>
				<td><s:textfield name="resourcesBuyers.dbEngTitle"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">購買單位<span class="required">(&#8226;)</span></th>
				<td><input type="text" id="referenceOwner_name"
					class="input_text" disabled="disabled" value="增加單位"><img
					id="add" src="<c:url value = '/'/>resources/images/add.png"
					onclick="addReferenceOwner();"> <c:forEach var="item"
						items="${entity.referenceOwners}" varStatus="status2">
						<div style="">
							<input class="input_text" disabled="disabled"
								value='${item.name}'><img id="minus"
								src="<c:url value = '/'/>resources/images/minus.png"><input
								id="unit" type="hidden" value="${item.serNo }"
								name="entity.refSerNo">
						</div>
					</c:forEach> <c:forEach var="item" items="${uncheckReferenceOwners}"
						varStatus="status">
						<div style="display: none;">
							<input class="input_text" disabled="disabled"
								value='${item.name}'><img id="minus"
								src="<c:url value = '/'/>resources/images/minus.png"><input
								id="unit" type="hidden" value="${item.serNo }">
						</div>
					</c:forEach></td>
			</tr>
		</table>
		<div class="button_box">
			<div class="detail-func-button">
				<a class="state-default"
					onclick="clearReferenceOwners();closeDetail();">取消</a> &nbsp;<a
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