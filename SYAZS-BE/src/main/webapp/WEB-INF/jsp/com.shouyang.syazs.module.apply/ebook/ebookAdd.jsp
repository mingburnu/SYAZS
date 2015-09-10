<%@ page import="java.util.*"%>
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
						var contain = $("#div_Detail_2 .content .header .title")
								.html();
						if (contain != '擁有人-新增') {
							goReferenceOwners(
									"<c:url value = '/'/>crud/apply.referenceOwner.box.action",
									'擁有人-新增');
						}
					});

	$(document)
			.ready(
					function() {
						var contain = $("#div_Detail_3 .content .header .title")
								.html();
						if (contain != '資料庫-選擇') {
							goResDbs(
									"<c:url value = '/'/>crud/apply.database.box.action",
									'資料庫-選擇');
						}
					});

	$(document)
			.ready(
					function() {
						$("#div_Detail .content .header .close")
								.html(
										'<a href="#" onclick="clearReferenceOwners();clearResDbs();closeDetail();">關閉</a>');
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

	$(document).ready(function() {
		setResField();
		checkIsbn();
	});

	$(document).ready(function() {
		$("input#apply_ebook_save_entity_isbn").bind('input', function() {
			checkIsbn();
		});

		$("input[name='entity.database.serNo']").change(function() {
			checkIsbn();
		});
	});

	function setResField() {
		var datSerNo = $("input[name='entity.database.serNo']").val();
		if (datSerNo == null || datSerNo == "") {
			$("input#referenceOwner_name").parent().parent().prev().prev()
					.prev().show();
			$("input#referenceOwner_name").parent().parent().prev().prev()
					.show();
			$("input#referenceOwner_name").parent().parent().prev().show();
			$("input#referenceOwner_name").parent().parent().show();
		} else {
			$("input#referenceOwner_name").parent().parent().prev().prev()
					.prev().hide();
			$("input#referenceOwner_name").parent().parent().prev().prev()
					.hide();
			$("input#referenceOwner_name").parent().parent().prev().hide();
			$("input#referenceOwner_name").parent().parent().hide();
		}
	}

	//重設所有欄位(清空)
	function resetData() {
		$("[id^='apply_ebook_save_entity'][type!='radio']").val("");

		for (var i = 0; i < $("input[type='radio']").length; i++) {
			$("input[type='radio']:eq(" + i + ")").val(
					$("input[type='radio']:eq(" + i + ")").next().html());
		}

		$("input[type='radio']:eq(1)").attr("checked", true);
		$("input[type='radio']:eq(4)").attr("checked", true);

		allSelect_referenceOwners(0);
		checkData();
		clearRes();
	}

	//遞交表單
	function submitData() {
		var data = $('#apply_ebook_save').serialize();
		closeDetail();
		clearResDbs();
		clearReferenceOwners();
		goDetail("<c:url value = '/'/>crud/apply.ebook.save.action", '電子書-新增',
				data);
	}

	function checkIsbn() {
		var isbn = $("input#apply_ebook_save_entity_isbn").val();
		var datSerNo = $("input#apply_ebook_save_entity_database_serNo").val();

		if (isbn == null || isbn.trim() == "") {
			$("#span-tip").html("ISBN未填寫");
		} else {
			goTip('<c:url value = "/"/>crud/apply.ebook.tip.action?entity.isbn='
					+ isbn + '&entity.database.serNo=' + datSerNo);
		}
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

input[type="text"]:disabled {
	background: #aaaaaa;
}
</style>
</head>
<body>
	<%
		String isbn = "";
		if (request.getParameter("entity.isbn") != null) {
			isbn = request.getParameter("entity.isbn");
		}
	%>
	<s:form namespace="/crud" action="apply.ebook.save">
		<table cellspacing="1" class="detail-table">
			<tr>
				<th width="130">書名<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.bookName" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">ISBN/13碼<span class="required">(&#8226;)</span></th>
				<td><input type="text" name="entity.isbn" class="input_text"
					id="apply_ebook_save_entity_isbn"
					value="<esapi:encodeForHTMLAttribute><%=isbn%></esapi:encodeForHTMLAttribute>">&nbsp;<span
					id="span-tip"></span></td>
			</tr>
			<tr>
				<th width="130">出版社</th>
				<td><s:textfield name="entity.publishName"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">第一作者</th>
				<td><s:textfield name="entity.autherName" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">次要作者</th>
				<td><s:textfield name="entity.authers" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">系列叢書名</th>
				<td><s:textfield name="entity.uppeName" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">出版日期</th>
				<td><s:textfield name="entity.pubDate" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">語文</th>
				<td><s:textfield name="entity.languages" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">版本</th>
				<td><s:textfield name="entity.version" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">中國圖書分類碼</th>
				<td><s:textfield name="entity.cnClassBzStr"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">杜威十進位分類號</th>
				<td><s:textfield name="entity.bookInfoIntegral"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">URL<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.url" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">類型</th>
				<td><s:textfield name="entity.style" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">出版地</th>
				<td><s:textfield name="entity.publication"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">公開資源</th>
				<td><c:choose>
						<c:when test="${true eq entity.openAccess }">
							<s:radio name="entity.openAccess"
								list="#@java.util.LinkedHashMap@{true:'是',false:'否'}"
								value="true" />
						</c:when>
						<c:otherwise>
							<s:radio name="entity.openAccess"
								list="#@java.util.LinkedHashMap@{true:'是',false:'否'}"
								value="false" />
						</c:otherwise>
					</c:choose></td>
			</tr>
			<tr>
				<th>資料庫題名</th>
				<td><s:hidden name="entity.database.serNo" /><input
					id="datName" class="input_text" disabled="disabled">&nbsp;<a
					class="state-default" onclick="addResDb()">選擇</a>&nbsp;<a
					class="state-default" onclick="clearRes()">清除</a></td>
			</tr>
			<tr>
				<th width="130">起始日</th>
				<td><s:textfield name="entity.resourcesBuyers.startDate"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">到期日</th>
				<td><s:textfield name="entity.resourcesBuyers.maturityDate"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">資源類型</th>
				<td><c:choose>
						<c:when test="${empty entity.resourcesBuyers.category }">
							<s:radio name="entity.resourcesBuyers.category"
								list="@com.shouyang.syazs.module.apply.enums.Category@values()"
								listKey="name()" listValue="name()" value="'未註明'" />
						</c:when>
						<c:otherwise>
							<s:radio name="entity.resourcesBuyers.category"
								list="@com.shouyang.syazs.module.apply.enums.Category@values()"
								listKey="name()" listValue="name()" />
						</c:otherwise>
					</c:choose></td>
			</tr>
			<tr>
				<th width="130">購買單位<span class="required">(&#8226;)</span></th>
				<td><input type="text" id="referenceOwner_name"
					class="input_text" disabled="disabled" value="增加單位"><img
					id="add" src="<c:url value = '/'/>resources/images/add.png"
					onclick="addReferenceOwner();"> <c:forEach var="item"
						items="${entity.owners}">
						<div style="">
							<input class="input_text" disabled="disabled"
								value="${item.name}"><img id="minus"
								src="<c:url value = '/'/>resources/images/minus.png"><input
								id="unit" type="hidden" value="${item.serNo }"
								name="entity.refSerNo">
						</div>
					</c:forEach> <c:forEach var="item" items="${uncheckReferenceOwners}">
						<div style="display: none;">
							<input class="input_text" disabled="disabled"
								value="${item.name}"><img id="minus"
								src="<c:url value = '/'/>resources/images/minus.png"><input
								id="unit" type="hidden" value="${item.serNo }">
						</div>
					</c:forEach></td>
			</tr>
		</table>
		<div class="button_box">
			<div class="detail-func-button">
				<a class="state-default"
					onclick="clearResDbs();clearReferenceOwners();closeDetail();">取消</a>
				&nbsp;<a class="state-default" onclick="resetData();">重設</a>&nbsp; <a
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