<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/WEB-INF/jsp/layout/msg.jsp" />
<script type="text/javascript">
	$(document).ready(function() {
		do_event();
		showType('database');
	});

	$(document).ready(function() {
		changeFormAction();
		$("[id=" + "${item}" + "]").val("${option}");
		$("[id=" + "${item}" + "]").next().children().html("${option}");
	});

	function changeFormAction() {
		var item = $("input[type='radio']:checked").val();
		$("form").attr("id", "apply_" + item + "_focus");
		$("form").attr("name", "apply_" + item + "_focus");
		$("form")
				.attr(
						"action",
						"<c:url value = '/'/>" + "crud/apply." + item
								+ ".focus.action");
		$("input[type='hidden']").removeAttr("name");
		$('input#' + item).attr("name", "entity.option");
		showType(item);
	}

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

		$('body').click(function(e) {
			$('.select_01 ul').hide();
		});
	}

	function showType(arg) {
		$('.select_database').hide();
		$('.select_ebook').hide();
		$('.select_journal').hide();
		//
		$('.select_' + arg).show();
	}

	function form_reset() {
		$("input[name='entity.indexTerm']").val("");
	}

	function form_sumbit() {
		var url = $("form").attr("action") + "?" + $("form").serialize();
		$.ajax({
			url : url,
			success : function(result) {
				$("#container").html(result);
			}
		});
	}
</script>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="table_container">
	<tr valign="top">
		<td class="L">
			<div id="main_box">
				<!-- 內容開始 -->

				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="table_02">
					<tr valign="middle">
						<td class="t_01"><img
							src="<c:url value = '/'/>resources/images/txt_01.png"></td>
						<td class="t_02"><c:choose>
								<c:when test="${empty item }">
									<s:radio name="item" cssClass="v_type"
										onchange="changeFormAction();showType(this.value);"
										list="#{'database':' 資料庫','ebook':' 電子書','journal':' 期刊' }"
										value="'database'" />
								</c:when>
								<c:otherwise>
									<s:radio name="item" cssClass="v_type"
										onchange="changeFormAction()"
										list="#{'database':' 資料庫','ebook':' 電子書','journal':' 期刊' }"
										value="%{item}" />
								</c:otherwise>
							</c:choose></td>
					</tr>
					<tr valign="middle">
						<td class="t_01"><img
							src="<c:url value = '/'/>resources/images/txt_02.png"></td>
						<td class="t_02"><s:form action="apply.database.focus"
								namespace="/crud" method="post" onsubmit="return false;">
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td>
											<div class="select_01 select_database">
												<s:hidden value="中文題名" id="database" name="entity.option" />
												<div>
													<a href="javascript:void(0);">中文題名</a>
												</div>
												<ul>
													<li><a href="javascript:void(0);"
														onClick="clickItem(this);">中文題名</a></li>
													<li><a href="javascript:void(0);"
														onClick="clickItem(this);">英文題名</a></li>
													<li><a href="javascript:void(0);"
														onClick="clickItem(this);">出版社</a></li>
													<li><a href="javascript:void(0);"
														onClick="clickItem(this);">內容描述</a></li>
												</ul>
											</div>

											<div class="select_01 select_ebook">
												<s:hidden value="書名" id="ebook" />
												<div>
													<a href="javascript:void(0);">書名</a>
												</div>
												<ul>
													<li><a href="javascript:void(0);"
														onClick="clickItem(this);">書名</a></li>
													<li><a href="javascript:void(0);"
														onClick="clickItem(this);">ISBN</a></li>
													<li><a href="javascript:void(0);"
														onClick="clickItem(this);">出版社</a></li>
													<li><a href="javascript:void(0);"
														onClick="clickItem(this);">作者</a></li>
												</ul>
											</div>

											<div class="select_01 select_journal">
												<s:hidden value="中文刊名" id="journal" />
												<div>
													<a href="javascript:void(0);">中文刊名</a>
												</div>
												<ul>
													<li><a href="javascript:void(0);"
														onClick="clickItem(this);">中文刊名</a></li>
													<li><a href="javascript:void(0);"
														onClick="clickItem(this);">英文刊名</a></li>
													<li><a href="javascript:void(0);"
														onClick="clickItem(this);">英文縮寫</a></li>
													<li><a href="javascript:void(0);"
														onClick="clickItem(this);">出版商</a></li>
													<li><a href="javascript:void(0);"
														onClick="clickItem(this);">ISSN</a></li>
												</ul>
											</div>

										</td>
										<td><div class="input_01">
												<span><s:textfield cssClass="v_keyword"
														name="entity.indexTerm" value="%{indexTerm}" /></span>
											</div></td>
									</tr>
								</table>
							</s:form></td>
					</tr>
					<tr valign="middle">
						<td class="t_03" colspan="2" align="center"><a class="btn_01"
							href="javascript:void(0);" onClick="form_reset();">重 新 填 寫</a> <a
							class="btn_01" href="#" onClick="form_sumbit();">開 始 查 詢</a></td>
					</tr>
				</table>

				<!-- 內容結束 -->
			</div>
		</td>
	</tr>
</table>