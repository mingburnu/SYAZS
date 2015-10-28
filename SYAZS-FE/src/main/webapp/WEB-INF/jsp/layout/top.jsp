<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(document).ready(function() {
		var totalEbk = "<c:url value = '/'/>crud/apply.ebook.count.action";
		var totalJou = "<c:url value = '/'/>crud/apply.journal.count.action";
		var totalDat = "<c:url value = '/'/>crud/apply.database.count.action";
		var ebk = 0;
		var jou = 0;
		var dat = 0;

		$.ajax({
			url : totalEbk,
			success : function(result) {
				$("div.bar_box div span:eq(1)").html(result);
				ebk = parseInt(result);
			}
		});

		$.ajax({
			url : totalJou,
			success : function(result) {
				$("div.bar_box div span:eq(2)").html(result);
				jou = parseInt(result);
			}
		});

		$.ajax({
			url : totalDat,
			success : function(result) {
				$("div.bar_box div span:eq(3)").html(result);
				dat = parseInt(result);
			}
		});

		$("div.bar_box div span:eq(0)").ajaxComplete(function() {
			$(this).html(ebk + jou + dat);
		});
	});
</script>
<div id="top">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="left">
				<div class="logo_box">
					<a href="<c:url value = '/'/>"><img
						src="<c:url value = '/'/>resources/images/logo.png" width="240"
						height="70"></a>
				</div>
			</td>
			<td align="right">
				<div class="bar_box">
					<div>
						電子館藏：<span></span> 書籍：<span></span> 期刊：<span></span>
						資料庫：<span></span>
					</div>
				</div>
			</td>
		</tr>
	</table>
</div>