<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
<script type="text/javascript">
	$(document).ready(function() {
		do_event();
	});
	function clickItem(arg) {
		var currentValue = $(arg).html();
		$(arg).parent().parent().parent().find('input[type="hidden"]').attr(
				"value", currentValue);
		$(arg).parent().parent().parent().find("div a").html(currentValue);
	}
	function do_event() {
		$('.select_01 div a').click(function(e) {
			$('.select_01 ul').hide();
			$(this).parent().next('ul').show();

			return false;
		});

		//
		$('body').click(function(e) {
			$('.select_01 ul').hide();
		});
	}

	function form_reset() {
		document.form_01.reset();
	}

	function form_submit() {
		var url = "<c:url value = '/'/>crud/apply.database.list.action?entity.option="
				+ $("input[name='entity.option']").val()
				+ "&entity.indexTerm="
				+ $("input[name='entity.indexTerm']").val();
		$.ajax({
			url : url,
			success : function(result) {
				$("#container").html(result);
			}
		});
	}

	function queryByPrefix(prefix) {
		var url = "<c:url value = '/'/>crud/apply.database.prefix.action?entity.option="
				+ prefix;
		$.ajax({
			url : url,
			success : function(result) {
				$("#container").html(result);
			}
		});
	}
</script>
<form name="form_01" method="post"
	onSubmit="form_submit();return false;">
	<div class="title">依標題尋找資料庫</div>
	<table border="0" cellpadding="0" cellspacing="0" class="table_02">
		<tr>
			<td>
				<div class="select_01">
					<input type="hidden" value="標題開頭為" name="entity.option" />
					<div>
						<a href="javascript:void(0);">標題開頭為</a>
					</div>
					<ul>
						<li><a href="javascript:void(0);" onClick="clickItem(this);">標題開頭為</a></li>
						<li><a href="javascript:void(0);" onClick="clickItem(this);">標題包含文字</a></li>
						<li><a href="javascript:void(0);" onClick="clickItem(this);">出版社</a></li>
					</ul>
				</div>
			</td>
			<td><div class="input_01">
					<span><input class="v_keyword" type="text"
						name="entity.indexTerm" /></span>
				</div></td>
			<td><a class="btn_03" href="javascript:void(0);"
				onClick="form_submit();">&nbsp;</a></td>
		</tr>
	</table>

	<jsp:include page="/WEB-INF/jsp/layout/prefix.jsp">
		<jsp:param name="title" value="依標題瀏覽資料庫" />
	</jsp:include>
</form>