<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=request.getContextPath()%>/resources/css/air-ui-cms.css"
	rel="stylesheet" type="text/css">
<script language="javascript" type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/jquery-1.5.2.js">
	
</script>
<script language="javascript" type="text/javascript">
	$(document).ready(function() {
		formSetCSS();
		state_hover();
	});
	//css
	function formSetCSS() {
		$('input[type="text"]').addClass("input_text");
		$('input[type="password"]').addClass("input_text");
	}
	//state
	function state_hover() {
		$(".state-default").hover(function() {
			$(this).addClass("state-hover");
		}, function() {
			$(this).removeClass("state-hover");
		});
	}
	//打開Alert畫面之函式
	function goAlert(argTitle, argMsg) {
		$("#div_Alert").show();
		UI_Resize();
		//
		$("#div_Alert .content > .header > .title").html(argTitle);
		$("#div_Alert .content > .contain").html(argMsg);
	}
	//關閉Alert畫面之函式
	function closeAlert() {
		$("#div_Alert").hide();
		UI_Resize();
	}
	//UI_Resize
	function UI_Resize() {
		$("#div_Detail > .overlay").css("width", $(window).width());
		$("#div_Detail > .overlay").css("height", $(window).height());
		$("#div_Detail_2 > .overlay").css("width", $(window).width());
		$("#div_Detail_2 > .overlay").css("height", $(window).height());
		$("#div_Alert > .overlay").css("width", $(window).width());
		$("#div_Alert > .overlay").css("height", $(window).height());
	}
	$(window).resize(function() {
		UI_Resize();
	});
	//UI_Scroll
	function UI_Scroll() {
		$("#div_Detail > .overlay").css("top", $(window).scrollTop());
		$("#div_Detail > .overlay").css("left", $(window).scrollLeft());
		$("#div_Detail_2 > .overlay").css("top", $(window).scrollTop());
		$("#div_Detail_2 > .overlay").css("left", $(window).scrollLeft());
		$("#div_Alert > .overlay").css("top", $(window).scrollTop());
		$("#div_Alert > .overlay").css("left", $(window).scrollLeft());
	}
	$(window).scroll(function() {
		UI_Scroll();
	});
</script>
<title>碩陽電子資源檢索</title>
</head>
<c:if
	test="${(login.role =='系統管理員') || (login.role =='維護人員') || (login.role =='管理員') }">
	<%
		response.sendRedirect(request.getContextPath()
					+ "/page/main.action");
	%>
</c:if>
<body style="background-color: #FFFFFF; margin-top: 100px;">
	<s:form action="login" namespace="/authorization" method="post"
		name="form_01">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tbody>
				<tr>
					<td align="center">
						<div align="center" id="login_box">
							<div id="div_login_header"></div>
							<div id="div_login_contain">
								<table align="center" width="100%" border="0" cellspacing="6"
									cellpadding="0">
									<tbody>
										<tr>
											<td align="right">用戶帳號：</td>
											<td align="left"><input type="text" name="user.userId"
												value="" id="Login_UserName" style="width: 160;"
												class="input_text"></td>
										</tr>
										<tr>
											<td align="right">用戶密碼：</td>
											<td align="left"><input type="password"
												name="user.userPw" id="Login_Password" style="width: 160;"
												class="input_text"></td>
										</tr>
										<tr>
											<td align="center" colspan="2"><s:if
													test="hasActionErrors()">
													<s:iterator value="actionErrors">
														<font color="red"> <s:property escape="true" />
														</font>
													</s:iterator>
												</s:if></td>
										</tr>
									</tbody>
								</table>
								<div style="margin-top: 10px;"></div>
								<input type="submit" class="state-default corner-all button"
									value="登入">

							</div>
							<div id="div_login_footer"></div>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</s:form>
</body>
</html>