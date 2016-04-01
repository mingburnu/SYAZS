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
		checkIssn();
		checkTitle();
	});

	$(document).ready(function() {
		$("input#apply_journal_update_entity_issn").bind('input', function() {
			checkIssn();
			checkTitle();
		});

		$("input[name='entity.database.serNo']").change(function() {
			checkIssn();
			checkTitle();
		});

		$("input#apply_journal_update_entity_title").bind('input', function() {
			checkTitle();
		});
	});

	//重設所有欄位(清空)
	function resetData() {
		goDetail("<c:url value = '/'/>crud/apply.journal.edit.action?"
				+ 'entity.serNo=${entity.serNo}', '期刊-修改');
	}

	//遞交表單
	function submitData() {
		var data = $('#apply_journal_update').serialize();
		closeDetail();
		goDetail(
				"<c:url value = '/'/>crud/apply.journal.update.action?entity.serNo=${entity.serNo}",
				'期刊-修改', data);
	}

	function checkIssn() {
		var issn = $("input#apply_journal_update_entity_issn").val();
		var datSerNo = $("input#apply_journal_update_entity_database_serNo")
				.val();

		if (issn != null && issn.trim() != "") {
			if (isValidISSN(issn.trim())) {
				goNumTip('<c:url value = "/"/>crud/apply.journal.tip.action?entity.serNo=${entity.serNo}&entity.issn='
						+ issn + '&entity.database.serNo=' + datSerNo);
			} else {
				$("#span-num-tip").html("ISSN不正確");
			}
		}
	}

	function checkTitle() {
		var title = $("input#apply_journal_update_entity_title").val();
		var issn = $("input#apply_journal_update_entity_issn").val();
		var datSerNo = $("input#apply_journal_update_entity_database_serNo")
				.val();
		if (issn == null || issn.trim() == "") {
			goTitleNameTip('<c:url value = "/"/>crud/apply.journal.tip.action?entity.serNo=${entity.serNo}&entity.title='
					+ title + '&entity.database.serNo=' + datSerNo);
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

input#referenceOwner_name {
	background-color: #aaaaaa;
}
</style>
</head>
<body>
	<%
		String startDate = "";
		if (request.getParameter("entity.startDate") != null) {
			startDate = request.getParameter("entity.startDate");
		} else {
			if (request.getAttribute("entity.startDate") != null) {
				startDate = request.getAttribute("entity.startDate")
						.toString().split("T")[0];
			}
		}

		String maturityDate = "";
		if (request.getParameter("entity.maturityDate") != null) {
			maturityDate = request.getParameter("entity.maturityDate");
		} else {
			if (request.getAttribute("entity.maturityDate") != null) {
				maturityDate = request.getAttribute("entity.maturityDate")
						.toString().split("T")[0];
			}
		}

		String publishYear = "";
		if (request.getParameter("entity.publishYear") != null) {
			publishYear = request.getParameter("entity.publishYear");
		} else {
			if (request.getAttribute("entity.publishYear") != null) {
				publishYear = request.getAttribute("entity.publishYear")
						.toString();
			}
		}
	%>
	<s:form namespace="/crud" action="apply.journal.update">
		<table cellspacing="1" class="detail-table">
			<tr>
				<th width="130">刊名<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.title" cssClass="input_text" />&nbsp;<span
					id="span-title-name-tip" class="tip"></span></td>
			</tr>
			<tr>
				<th width="130">英文縮寫刊名</th>
				<td><s:textfield name="entity.abbreviationTitle"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">刊名演變</th>
				<td><s:textfield name="entity.titleEvolution"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">ISSN</th>
				<td><s:textfield name="entity.issn" cssClass="input_text" />&nbsp;<span
					id="span-num-tip" class="tip"></span></td>
			</tr>
			<tr>
				<th width="130">語文</th>
				<td><s:textfield name="entity.languages" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">標題</th>
				<td><s:textfield name="entity.caption" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">出版社<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.publishName"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">出版年</th>
				<td><input type="text" name="entity.publishYear"
					value="<%=ESAPI.encoder().encodeForHTMLAttribute(publishYear)%>"
					id="apply_journal_update_entity_publishYear" class="input_text">&nbsp;<span
					id="span-year-tip" class="tip">yyyy</span></td>
			</tr>
			<tr>
				<th width="130">刊別</th>
				<td><s:textfield name="entity.publication"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">編號</th>
				<td><s:textfield name="entity.numB" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">國會分類號</th>
				<td><s:textfield name="entity.congressClassification"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">版本</th>
				<td><s:textfield name="entity.version" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">全文取得授權刊期(embargo period)</th>
				<td><s:textfield name="entity.embargo" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">URL<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.url" cssClass="input_text" />&nbsp;<span
					id="span-url-tip" class="tip">http://www.sydt.com.tw或https://www.sydt.com.tw</span></td>
			</tr>
			<tr>
				<th width="130">開放近用</th>
				<td><c:choose>
						<c:when test="${empty entity.openAccess }">
							<s:radio name="entity.openAccess"
								list="#@java.util.LinkedHashMap@{true:'是',false:'否'}"
								value="false" />
						</c:when>
						<c:otherwise>
							<s:radio name="entity.openAccess"
								list="#@java.util.LinkedHashMap@{true:'是',false:'否'}" />
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
				<th width="130">起始日</th>
				<td><input type="text" name="entity.startDate"
					value="<%=ESAPI.encoder().encodeForHTMLAttribute(startDate)%>"
					id="apply_journal_update_entity_startDate" class="input_text">&nbsp;<span
					id="span-date-tip" class="tip">yyyy-mm-dd或yyyy/mm/dd</span></td>
			</tr>
			<tr>
				<th width="130">到期日</th>
				<td><input type="text" name="entity.maturityDate"
					value="<%=ESAPI.encoder().encodeForHTMLAttribute(maturityDate)%>"
					id="apply_journal_update_entity_maturityDate" class="input_text">&nbsp;<span
					id="span-date-tip" class="tip">yyyy-mm-dd或yyyy/mm/dd</span></td>
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
				<span class="required">(•)</span>為必填欄位
			</div>
		</div>
	</s:form>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>