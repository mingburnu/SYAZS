<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
		$("input#referenceOwner_unit").click(function() {
			if ($(this).is(':checked')) {
				var value = $(this).val();
				$("input#unit").each(function() {
					if (value == $(this).val()) {
						$(this).attr("name", "entity.refSerNo");
						$(this).parent().show();
					}
				});
			} else {
				var value = $(this).val();
				$("input#unit").each(function() {
					if (value == $(this).val()) {
						$(this).attr("name", "");
						$(this).parent().hide();
					}
				});
			}
		});
	});

	//載入選項
	$(document).ready(function() {
		$("input[name='entity.refSerNo']").each(function() {
			var value = $(this).val();
			$("input#referenceOwner_unit").each(function() {
				if (value == $(this).val()) {
					$(this).attr("checked", true);
				}
			});
		});
	});

	function addResDb() {
		$("#div_ResDbs").show();
	}

	function clearResDbs() {
		$("#div_ResDbs .content .header .title").html("");
		$("#div_ResDbs .content .contain").html("");
	}
</script>
</head>
<body>
	<table>
		<tr>
			<th>名稱</th>
			<th>起始日</th>
			<th>到期日</th>
			<th>資源類別</th>
			<th>資源種類</th>
			<th>購買單位</th>
			<th>UUID</th>
		</tr>
		<c:forEach var="item" items="${resDbs}">
			<tr>
				<td><input id="dat" type="checkbox" value="${item.serNo }"
					name="resDbSerNo"> <label><esapi:encodeForHTML>${item.dbTitle }</esapi:encodeForHTML></label></td>
				<td>${item.resourcesBuyers.startDate }</td>
				<td>${item.resourcesBuyers.maturityDate }</td>
				<td>${item.resourcesBuyers.category }</td>
				<td>${item.type }</td>
				<td><c:forEach var="owner" items="${item.referenceOwners }"
						varStatus="index">
						<c:choose>
							<c:when test="${!index.last }">${owner.name }、</c:when>
							<c:otherwise>${owner.name }</c:otherwise>
						</c:choose>
					</c:forEach></td>
				<td>${item.uuIdentifier }</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>