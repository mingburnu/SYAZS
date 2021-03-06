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
		$("input[name='entity.file']").val("");
	}

	//匯出範本
	function goExample() {
		var option = $("input[name='entity.option']:checked",
				"form#apply_ebook_queue").val();
		var url = "<c:url value = '/'/>crud/apply.ebook.example.action?entity.option="
				+ option;
		window.open(url, "_top");
	}

	//Excel列表
	function goQueue() {
		var f = document.getElementById("file");
		if (f.files.length == 0) {
			goAlert("訊息", "請選擇檔案");
			return;
		}

		if (f.files.item(0).size > 10485760) {
			goAlert("訊息", "檔案超過10MB，請分批");
			return;
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
		//alert(document.getElementById("apply_ebook_queue"));
		var formObj = $("form#apply_ebook_queue");
		var formURL = $("form#apply_ebook_queue").attr("action");

		if (window.FormData !== undefined) // for HTML5 browsers
		//			if(false)
		{

			var formData = new FormData(document
					.getElementById("apply_ebook_queue"));
			$
					.ajax({
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
							$("#div_Detail .content > .header > .title").html(
									"電子書-匯入");
							$("#div_Detail .content > .contain").empty().html(
									data);
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
				$("#div_Detail .content > .header > .title").html("電子書-匯入");
				$("#div_Detail .content > .contain").empty().html(data);
				closeLoading();
			});
		}

	}
</script>
</head>
<body>
	<s:form namespace="/crud" action="apply.ebook.queue"
		enctype="multipart/form-data" method="post">

		<table cellspacing="1" class="detail-table">
			<tr>
				<th>匯入選項</th>
				<td><c:choose>
						<c:when test="${not empty entity.option }">
							<s:radio
								list="#@java.util.LinkedHashMap@{'package':'資料庫資源','individual':'個別資源'}"
								name="entity.option" />
						</c:when>
						<c:otherwise>
							<s:radio
								list="#@java.util.LinkedHashMap@{'package':'資料庫資源','individual':'個別資源'}"
								name="entity.option" value="'package'" />
						</c:otherwise>
					</c:choose></td>
			</tr>
			<tr>
				<th width="130">匯入檔案<span class="required">(•)</span>(<a
					href="#" onclick="goExample();">範例</a>)
				</th>
				<td><input type="file" id="file" name="entity.file" size="50"></td>
			</tr>
		</table>
		<div class="button_box">
			<div class="detail-func-button">
				<a class="state-default" onclick="closeDetail();">取消</a> &nbsp;<a
					class="state-default" onclick="resetData();">重置</a>&nbsp;<a
					id="ports" class="state-default" onclick="goQueue();">下一步</a>
			</div>
		</div>
		<div class="detail_note">
			<div class="detail_note_title">Note</div>
			<div class="detail_note_content">
				<span class="required">(&#8226;)</span>為必填欄位<br> <br> <span>開放近用(是
					-1 , 否 - 0)<br> <br> 資料類型(賣斷 -1 , 租賃 - 2, 免費提供閱讀 - 3, 未註明
					-0) <br> <br>分類法請使用ID
				</span>
			</div>
		</div>
	</s:form>
</body>
<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</html>