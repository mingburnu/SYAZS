<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="esapi"
	uri="http://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	//勾選單位
	$(document).ready(function() {
		$("input#dat").click(function() {
			selectUnits($(this));
		});
	});

	//載入選項
	$(document)
			.ready(
					function() {
						var value = $("input[name='entity.database.serNo']")
								.val();
						$("input#dat")
								.each(
										function() {
											if (value == $(this).val()) {
												$(this).attr("checked", true);
												$(
														"input[name='entity.resourcesBuyers.category']")
														.attr("disabled", true);
											}
										});
					});

	$(document).ready(function() {
		$("input#dat").click(function() {
			var box = $(this).attr("checked");
			$("input#dat").attr("checked", false);
			$(this).attr("checked", box);
		});
	});

	$(document).ready(function() {
		$("input#dat").mouseenter(function() {
			$("div#box_detail").html($(this).next().next().html());
		});

		$("input#dat").next().mouseenter(function() {
			$("div#box_detail").html($(this).next().html());
		});
	});

	function selectUnits(datBox) {
		if (datBox.is(':checked')) {
			var value = datBox.val();
			$("input[name='entity.database.serNo']").val(value).trigger(
					'change');

			$("input[name='entity.resourcesBuyers.category']").filter(
					'[value='
							+ datBox.next().next().find("#category").html()
									.split("：")[1].trim() + ']').attr(
					'checked', true);
			$("input[name='entity.resourcesBuyers.category']").attr("disabled",
					true);

			$("input#datName").val(datBox.prev().val());
		} else {
			$("input[name='entity.database.serNo']").val("").trigger('change');
			$("input#datName").val("");
			$("input[name='entity.resourcesBuyers.category']").attr("disabled",
					false);
		}
	}

	function addResDb() {
		$("#div_ResDbs").show();
	}

	function clearRes() {
		$("input#dat").attr("checked", false);
		$("input#datName").val("");
		$("input[name='entity.database.serNo']").val("").trigger('change');
		$("input[name='entity.resourcesBuyers.category']").attr("disabled",
				false);
	}

	function clearResDbs() {
		$("#div_ResDbs .content .header .title").html("");
		$("#div_ResDbs .content .contain").html("");
	}
</script>
<style type="text/css">
div#resDbRow {
	display: inline-block;
}
</style>
</head>
<body>
	<c:forEach var="item" items="${resDbs}">
		<div id=resDbRow>
			<input type="hidden"
				value="<esapi:encodeForHTMLAttribute>${item.dbTitle }</esapi:encodeForHTMLAttribute>">
			<input id="dat" type="checkbox" value="${item.serNo }"> <label
				id="resDbTitle"><esapi:encodeForHTML>${item.dbTitle }</esapi:encodeForHTML></label>
			<div id="detail" style="display: none;">
				<div id="dbTitle">
					<label>資料庫題名</label>：
					<esapi:encodeForHTML>${item.dbTitle }</esapi:encodeForHTML>
				</div>
				<div id="uuid">
					<label>UUID</label>：${item.uuIdentifier }
				</div>
				<div id="startDate">
					<label>起始日</label>：${fn:split(item.startDate, 'T')[0]}
				</div>
				<div id="maturityDate">
					<label>到期日</label>：${fn:split(item.maturityDate, 'T')[0]}
				</div>
				<div id="category">
					<label>資源類別</label>：${item.resourcesBuyers.category }
				</div>
				<div id="type">
					<label>資源種類</label>：${item.type }
				</div>
			</div>
		</div>
	</c:forEach>
	<div class="button_box">
		<div class="detail-func-button">
			<a class="state-default" onclick="closeResDbs();">確認</a>
		</div>
	</div>
	<div class="detail_note">
		<div class="detail_note_title">Note</div>
		<div id="box_detail" class="box_note_content"></div>
	</div>
</body>
</html>