<%@ page import="org.owasp.esapi.ESAPI"%>
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
	//重設所有欄位(清空)
	function resetData() {
		goDetail("<c:url value = '/'/>/crud/apply.database.edit.action?"
				+ 'entity.serNo=${entity.serNo}', '資料庫-修改');
	}

	//遞交表單
	function submitData() {
		var data = $('#apply_database_update').serialize();
		closeDetail();
		goDetail(
				"<c:url value = '/'/>crud/apply.database.update.action?entity.serNo=${entity.serNo}",
				'資料庫-修改', data);
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

textarea#apply_database_update_entity_content {
	resize: none;
	width: 617px;
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
	%>
	<s:form namespace="/crud" action="apply.database.update">
		<table cellspacing="1" class="detail-table">
			<tr>
				<th width="130">資料庫題名<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.dbTitle" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">出版社<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.publishName"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">語文</th>
				<td><s:textfield name="entity.languages" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">收錄種類</th>
				<td><s:textfield name="entity.includedSpecies"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">收錄內容</th>
				<td><s:textarea name="entity.content" cssClass="input_text" />
				</td>
			</tr>
			<tr>
				<th width="130">主題</th>
				<td><s:textfield name="entity.topic" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">分類</th>
				<td><s:textfield name="entity.classification"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">收錄年代</th>
				<td><s:textfield name="entity.indexedYears"
						cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">URL<span class="required">(&#8226;)</span></th>
				<td><s:textfield name="entity.url" cssClass="input_text" />&nbsp;<span
					id="span-url-tip" class="tip">http://www.sydt.com.tw或https://www.sydt.com.tw</span></td>
			</tr>
			<tr>
				<th width="130">全文取得授權刊期(embargo period)</th>
				<td><s:textfield name="entity.embargo" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">起始日</th>
				<td><input type="text" name="entity.startDate"
					value="<%=ESAPI.encoder().encodeForHTMLAttribute(startDate)%>"
					id="apply_database_update_entity_startDate" class="input_text">&nbsp;<span
					id="span-date-tip" class="tip">yyyy-mm-dd或yyyy/mm/dd</span></td>
			</tr>
			<tr>
				<th width="130">到期日</th>
				<td><input type="text" name="entity.maturityDate"
					value="<%=ESAPI.encoder().encodeForHTMLAttribute(maturityDate)%>"
					id="apply_database_update_entity_maturityDate" class="input_text">&nbsp;<span
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
			<tr>
				<th width="130">資源種類</th>
				<td><c:choose>
						<c:when test="${empty entity.type }">
							<s:radio name="entity.type"
								list="@com.shouyang.syazs.module.apply.enums.Type@values()"
								listKey="name()" listValue="type" value="'資料庫'" />
						</c:when>
						<c:otherwise>
							<s:radio name="entity.type"
								list="@com.shouyang.syazs.module.apply.enums.Type@values()"
								listKey="name()" listValue="type" />
						</c:otherwise>
					</c:choose></td>
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