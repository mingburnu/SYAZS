<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
<script type="text/javascript">
	function form_reset() {
		document.form_01.reset();
	}

	function form_submit() {
		var url = $("form").attr("action") + "?" + $("form").serialize();
		$.ajax({
			url : url,
			type : "POST",
			success : function(result) {
				$("#container").html(result);
			}
		});
	}
</script>
<!-- container 開始 -->
<s:form name="form_01" action="apply.complex.list"
	namespace="/crud" method="post" onsubmit="return false;">
	<table width="100%" border="0" cellpadding="0" cellspacing="0"
		class="table_01">
		<tr valign="middle">
			<td class="t_01">輸入查詢</td>
			<td class="t_02">
				<div class="input_01">
					<span> <s:textfield cssClass="v_keyword"
							name="entity.indexTerm" />
					</span>
				</div>
			</td>
		</tr>
		<tr valign="middle">
			<td class="t_03" colspan="2" align="center"><a class="btn_01"
				href="javascript:void(0);" onclick="form_reset();">重 新 填 寫</a> <a
				class="btn_01" href="javascript:void(0);" onclick="form_submit();">開
					始 查 詢</a></td>
		</tr>
	</table>
</s:form>
<!-- container 結束 -->
