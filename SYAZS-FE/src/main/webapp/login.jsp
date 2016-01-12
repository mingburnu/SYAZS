<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>碩陽電子資源檢索</title>
<style type="text/css">
#wrapper {
	margin: 0;
	padding: 70px 0 0 0;
	font-family: Verdana, Geneva, sans-serif, "新細明體";
	font-size: 15px;
}

.btn_01 {
	display: -moz-inline-stack;
	display: inline-block;
	text-decoration: none;
	width: 100px;
	height: 27px;
	line-height: 27px;
	margin: 0 10px 0 0;
	padding: 0;
	font-size: 13px;
	color: #fff;
	background:
		url("<%=request.getContextPath()%>/resources/images/btn_01.png") 0 0
		no-repeat;
	cursor: pointer;
}

.btn_01:hover {
	background:
		url("<%=request.getContextPath()%>/resources/images/btn_01.png") 0
		-30px no-repeat;
}

.login_box {
	-webkit-border-radius: 10px;
	-moz-border-radius: 10px;
	border-radius: 10px;
	border: 1px solid #ccc;
	width: 360px;
}

.login_table {
	margin: 0;
	padding: 5px;
}

.login_table th {
	margin: 0;
	padding: 10px;
	font-weight: normal;
}

.login_table td {
	margin: 0;
	padding: 10px;
}

.input_02 {
	width: 200px;
	height: 35px;
	background:
		url("<%=request.getContextPath()%>/resources/images/input_02.png") 0 0
		no-repeat;
	margin: 0;
	padding: 0 0 0 10px;
}

.input_02 span {
	display: block;
	background:
		url("<%=request.getContextPath()%>/resources/images/input_02.png")
		100% 0 no-repeat;
	margin: 0;
	padding: 5px 10px 5px 0;
}

.input_02 span input {
	width: 100%;
	border: none;
	height: 25px;
	line-height: 25px;
	background: #fafafa;
	font-size: 15px;
}

.input_02 span input:focus {
	outline: none;
}

form {
	margin-bottom: 0.5em;
}

.footer {
	font-size: 12px;
	margin: 10px 0 0 0;
	padding: 0;
	color: #888;
}

input:-webkit-autofill {
	-webkit-box-shadow: 0 0 0px 1000px #fafafa inset;
}
</style>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/jquery-1.7.2.min.js"></script>
<s:if test="hasActionErrors()">
	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							var msg = "";
							<s:iterator value="actionErrors">msg += '<s:property escape="true"/>\r\n';
							</s:iterator>;
							alert(msg);
						});
	</script>
</s:if>
<script type="text/javascript">
	function form_sumbit() {
		var msg = "";
		if ($(".v_username").val() == "") {
			msg += "．請輸入您的帳號。\r\n";
		}
		if ($(".v_password").val() == "") {
			msg += "．請輸入您的密碼。\r\n";
		}
		if (msg != "") {
			alert(msg);
			return false;
		} else {
			$('#login').submit();
		}
	}
</script>
</head>
<c:if
	test="${(login.role =='系統管理員') || (login.role =='維護人員') || (login.role =='管理員') || (login.role =='使用者')}">
	<%
		response.sendRedirect(request.getContextPath()
					+ "/page/index.action");
	%>
</c:if>
<body>
	<div id="wrapper" align="center">
		<s:form action="login" namespace="/authorization" method="post"
			name="form_01">
			<div align="center">
				<img src="<c:url value = '/resources/images/login_box_header.png'/>"
					width="360" height="80">
			</div>
			<div align="center" class="login_box">
				<table border="0" cellpadding="0" cellspacing="0"
					class="login_table">
					<tr>
						<th>您的帳號</th>
						<td><div class="input_02">
								<span><input class="v_username" type="text"
									name="user.userId" /></span>
							</div></td>
					</tr>
					<tr>
						<th>您的密碼</th>
						<td><div class="input_02">
								<span><input class="v_password" type="password"
									name="user.userPw" /></span>
							</div></td>
					</tr>
					<tr>
						<td colspan="2" align="center"><a class="btn_01"
							onClick="form_sumbit();"><span>登 入</span></a></td>
					</tr>
				</table>
			</div>
		</s:form>
		<div class="footer" align="center">
			<div>Copyright &copy; Shou Yang Digital Technology Co., Ltd.</div>
			<div>All Rights Reserved.</div>
		</div>
	</div>
</body>
</html>