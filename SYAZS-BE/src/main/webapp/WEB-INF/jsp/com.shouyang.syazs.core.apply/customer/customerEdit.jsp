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
		goDetail("<c:url value = '/'/>crud/apply.customer.edit.action?"
				+ 'entity.serNo=${entity.serNo}', '客戶-修改');
	}

	//遞交表單
	function submitData() {
		var f = document.getElementById("apply_customer_update_entity_file");

		if (f.files.length > 0) {
			if (f.files.item(0).size > 2097152) {
				goAlert("訊息", "檔案不可超過2MB");
				return;
			}
		}

		function getDoc(frame) {
			var doc = null;

			// IE8 cascading access check
			try {
				if (frame.contentWindow) {
					doc = frame.contentWindow.document;
				}
			} catch (err) {
			}

			if (doc) { // successful getting content
				return doc;
			}

			try { // simply checking may throw in ie8 under ssl or mismatched protocol
				doc = frame.contentDocument ? frame.contentDocument
						: frame.document;
			} catch (err) {
				// last attempt
				doc = frame.document;
			}
			return doc;
		}

		showLoading();
		//alert(document.getElementById("apply_customer_update"));
		var formObj = $("form#apply_customer_update");
		var formURL = $("form#apply_customer_update").attr("action")
				+ "?entity.serNo=${entity.serNo}";

		if (window.FormData !== undefined) // for HTML5 browsers
		//			if(false)
		{

			var formData = new FormData(document
					.getElementById("apply_customer_update"));
			$.ajax({
				url : formURL,
				type : 'POST',
				data : formData,
				mimeType : "multipart/form-data",
				contentType : false,
				cache : false,
				processData : false,
				success : function(data, textStatus, jqXHR) {
					$("#div_Detail").show();
					UI_Resize();
					$(window).scrollTop(0);
					$("#div_Detail .content > .header > .title").html("客戶-修改");
					$("#div_Detail .content > .contain").empty().html(data);
					closeLoading();

				},
				error : function(jqXHR, textStatus, errorThrown) {
					goAlert("結果", XMLHttpRequest.responseText);
					closeLoading();
				}
			});

		} else //for olden browsers
		{
			//generate a random id
			var iframeId = 'unique' + (new Date().getTime());

			//create an empty iframe
			var iframe = $('<iframe src="javascript:false;" name="'+iframeId+'" />');

			//hide it
			iframe.hide();

			//set form target to iframe
			formObj.attr('target', iframeId);

			//Add iframe to body
			iframe.appendTo('body');
			iframe.load(function(e) {
				var doc = getDoc(iframe[0]);
				var docRoot = doc.body ? doc.body : doc.documentElement;
				var data = docRoot.innerHTML;
				$("#div_Detail").show();
				UI_Resize();
				$(window).scrollTop(0);
				$("#div_Detail .content > .header > .title").html("客戶-修改");
				$("#div_Detail .content > .contain").empty().html(data);
				closeLoading();
			});
		}

	}
</script>
</head>
<body>
	<s:form namespace="/crud" action="apply.customer.update"
		enctype="multipart/form-data" method="post">
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
			<tr>
				<th width="130">備註</th>
				<td><s:textfield name="entity.memo" cssClass="input_text" /></td>
			</tr>
			<tr>
				<th width="130">LOGO</th>
				<td><s:file name="entity.file" cssClass="input_text" /><br>尺寸以240px
					✕ 70px為佳<c:if test="${not empty entity.logo }">
						<div id="logo"></div>
					</c:if></td>
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
	<c:if test="${not empty entity.logo }">
		<script type="text/javascript">
			var logo = $("<img />")
					.attr(
							"src",
							'<c:url value="/"/>crud/apply.customer.show.action?entity.serNo=${entity.serNo}'
									+ '&' + Math.random()).attr("width", "270")
					.attr("height", "70");
			$("div#logo").html(logo);
		</script>
	</c:if>
</body>
</html>