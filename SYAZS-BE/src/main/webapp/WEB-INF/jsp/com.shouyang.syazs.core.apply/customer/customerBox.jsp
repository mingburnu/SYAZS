<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	//勾選單位
	$(document).ready(function() {
		$("input#customer_unit").click(function() {
			if ($(this).is(':checked')) {
				var value = $(this).val();
				$("input#unit").each(function() {
					if (value == $(this).val()) {
						$(this).attr("name", "entity.cusSerNo");
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
		$("input#unit").each(function() {
			var value = $(this).val();
			var name = $(this).attr("name");

			if (name == "entity.cusSerNo") {
				$("input#customer_unit").each(function() {
					if (value == $(this).val()) {
						$(this).attr("checked", true);
					}
				});
			}
		});
	});

	//全選之函式
	function allSelect_customers(action) {
		$(document).ready(function() {
			for (var i = 0; i < $("input#customer_unit").length; i++) {
				if (action == 1) {
					$("input#customer_unit").get(i).checked = true;
				} else {
					$("input#customer_unit").get(i).checked = false;
				}
			}
		});
	}

	//全刪全選
	function checkData() {
		$("input#customer_unit").each(function() {
			if ($(this).is(':checked')) {
				var value = $(this).val();
				$("input#unit").each(function() {
					if (value == $(this).val()) {
						$(this).attr("name", "entity.cusSerNo");
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
	}

	function addCustomer() {
		$("#div_Customers").show();
	}

	function clearCustomers() {
		$("#div_Customers .content .header .title").html("");
		$("#div_Customers .content .contain").html("");
	}
</script>
<style type="text/css">
#unit_div {
	display: inline-block;
}
</style>
</head>

<body>
	<c:forEach var="item" items="${allCustomers}" varStatus="status">
		<div id="unit_div">
			<input type="checkbox" id="customer_unit" class="checkbox"
				value="${item.serNo }"><label>${item.name}</label>
		</div>
	</c:forEach>
	<div class="button_box">
		<div class="detail-func-button">
			<a class="state-default"
				onclick="allSelect_customers(0);checkData();">清除</a>&nbsp; <a
				class="state-default" onclick="allSelect_customers(1);checkData();">全選</a>&nbsp;
			<a class="state-default" onclick="closeCustomers();">確認</a>
		</div>
	</div>
</body>
</html>