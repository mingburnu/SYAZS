<%@ page import="org.owasp.esapi.ESAPI"%>
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
						$("#div_Detail .content .header .close")
								.html(
										'<a href="#" onclick="closeDetail();clearResDbs();">關閉</a>');
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

	$(document).ready(function() {
		checkIsbn();
		checkName();
	});

	$(document).ready(function() {
		$("input#apply_ebook_save_entity_isbn").bind('input', function() {
			checkIsbn();
			checkName();
		});

		$("input[name='entity.database.serNo']").change(function() {
			checkIsbn();
			checkName();
		});

		$("input#apply_ebook_save_entity_bookName").bind('input', function() {
			checkName();
		});
	});

	//重設所有欄位(清空)
	function resetData() {
		$("[id^='apply_ebook_save_entity'][type!='radio']").val("");

		for (var i = 0; i < $("input[type='radio']").length; i++) {
			$("input[type='radio']:eq(" + i + ")").val(
					$("input[type='radio']:eq(" + i + ")").next().html());
		}

		$("input[type='radio']:eq(1)").attr("checked", true);
		$("input[type='radio']:eq(4)").attr("checked", true);

		clearRes();
	}

	//遞交表單
	function submitData() {
		var data = $('#apply_ebook_save').serialize();
		closeDetail();
		clearResDbs();
		goDetail("<c:url value = '/'/>crud/apply.ebook.save.action", '電子書-新增',
				data);
	}

	function checkIsbn() {
		var isbn = $("input#apply_ebook_save_entity_isbn").val();
		var datSerNo = $("input#apply_ebook_save_entity_database_serNo").val();

		if (isbn != null && isbn.trim() != "") {
			goNumTip('<c:url value = "/"/>crud/apply.ebook.tip.action?entity.isbn='
					+ isbn + '&entity.database.serNo=' + datSerNo);
		}
	}

	function checkName() {
		var name = $("input#apply_ebook_save_entity_bookName").val();
		var isbn = $("input#apply_ebook_save_entity_isbn").val();
		var datSerNo = $("input#apply_ebook_save_entity_database_serNo").val();
		if (isbn == null || isbn.trim() == "") {
			goTitleNameTip('<c:url value = "/"/>crud/apply.ebook.tip.action?entity.bookName='
					+ name + '&entity.database.serNo=' + datSerNo);
		} else {
			$("#span-title-name-tip").html("");
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

		String pubDate = "";
		if (request.getParameter("entity.pubDate") != null) {
			pubDate = request.getParameter("entity.pubDate");
		}
	%>
	<s:form namespace="/crud" action="apply.ebook.save">
		<table cellspacing="1" class="detail-table">
			<tr>
				<th width="130">書名<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.bookName" cssClass="input_text" />&nbsp;<span
					id="span-title-name-tip" class="tip"></span></td>
			</tr>
			<tr>
				<th width="130">ISBN</th>
				<td><input type="text" name="entity.isbn" class="input_text"
					id="apply_ebook_save_entity_isbn"
					value="<%=ESAPI.encoder().encodeForHTMLAttribute(isbn)%>">&nbsp;<span
					id="span-num-tip" class="tip"></span></td>
			</tr>
			<tr>
				<th width="130">出版社<span class="required">(&#8226;)</span></th>
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
				<td><input type="text" name="entity.pubDate"
					value="<%=ESAPI.encoder().encodeForHTMLAttribute(pubDate)%>"
					id="apply_ebook_save_entity_pubDate" class="input_text">&nbsp;<span
					id="span-date-tip" class="tip">yyyy-mm-dd或yyyy/mm/dd</span></td>
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
				<th width="130">分類法</th>
				<td><s:select name="entity.classification.serNo"
						list="ds.datas" listKey="value" listValue="key" headerKey=""
						headerValue="-分類法-" value="%{entity.classification.serNo}" /></td>
			</tr>
			<tr>
				<th width="130">分類碼</th>
				<td><s:textfield name="entity.lcsCode" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">URL<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.url" cssClass="input_text" />&nbsp;<span
					id="span-url-tip" class="tip">http://www.sydt.com.tw或https://www.sydt.com.tw</span></td>
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
				<th width="130">開放近用</th>
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
				<td><s:hidden name="entity.database.serNo" /> <input
					id="datName" disabled="disabled"
					value="<esapi:encodeForHTMLAttribute>${entity.database.dbTitle }</esapi:encodeForHTMLAttribute>">&nbsp;
					<div id="selectDb">
						<a class="state-default" onclick="addResDb()">選擇</a>&nbsp;<a
							class="state-default" onclick="clearRes()">清除</a>
					</div></td>
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
		</table>
		<div class="button_box">
			<div class="detail-func-button">
				<a class="state-default" onclick="clearResDbs();closeDetail();">取消</a>
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