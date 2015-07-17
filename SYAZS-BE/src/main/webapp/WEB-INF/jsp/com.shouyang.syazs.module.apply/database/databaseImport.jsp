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
		var url = "<c:url value = '/'/>crud/apply.database.example.action";
		window.open(url, "_top");
	}

	//Excel列表
	function goQueue() {
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
		//alert(document.getElementById("apply_database_queue"));
		var formObj = $("form#apply_database_queue");
		var formURL = $("form#apply_database_queue").attr("action");

		if (window.FormData !== undefined) // for HTML5 browsers
		//			if(false)
		{

			var formData = new FormData(document
					.getElementById("apply_database_queue"));
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
									"資料庫-匯入");
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
				$("#div_Detail .content > .header > .title").html("資料庫-匯入");
				$("#div_Detail .content > .contain").empty().html(data);
				closeLoading();
			});
		}

	}
</script>
</head>
<body>
	<s:form namespace="/crud" action="apply.database.queue"
		enctype="multipart/form-data" method="post">
		<table cellspacing="1" class="detail-table">
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
				<span class="required">(&#8226;)</span>為必填欄位
			</div>
		</div>
	</s:form>
	<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
</body>
</html>