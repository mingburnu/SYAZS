<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>碩陽電子資源檢索</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<link href="<%=request.getContextPath()%>/resources/css/air-ui-cms.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/resources/css/jquery.autocomplete.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/resources/css/calendar-win2k-cold-1.css"
	rel="stylesheet" type="text/css">
<script language="javascript" type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/jquery-1.5.2.js">
	
</script>
<script language="javascript" type="text/javascript"
	src="<c:url value = '/'/>resources/js/common.js">
	
</script>
<script language="javascript" type="text/javascript"
	src="<c:url value = '/'/>resources/js/check.js">
	
</script>
<script language="javascript" type="text/javascript"
	src="<c:url value = '/'/>resources/js/jquery.autocomplete.js">
	
</script>
<script language="javascript" type="text/javascript"
	src="<c:url value = '/'/>resources/js/calendar.js">
	
</script>
<script language="javascript" type="text/javascript"
	src="<c:url value = '/'/>resources/js/calendar-setup.js">
	
</script>
<script language="javascript" type="text/javascript"
	src="<c:url value = '/'/>resources/js/calendar-en.js">
	
</script>
<script language="javascript" type="text/javascript"
	src="<c:url value = '/'/>resources/js/calendar-big5-utf8.js">
	
</script>
<script type="text/javascript">
	$(document).ready(function() {
		showMenuItems('1');
	});

	//打開Alert畫面之函式
	function goAlert(argTitle, argMsg, argBtnFunc) {
		$("#div_Alert").show();
		UI_Resize();
		$(window).scrollTop(0);
		$("#div_Alert .content > .header > .title").html(argTitle);
		$("#div_Alert .content > .contain").html(argMsg);
		$("#div_Alert .content > .func-button").empty();
		if (argBtnFunc) {
			if (argBtnFunc.trueText && argBtnFunc.trueFunc) {
				$("#div_Alert .content > .func-button").append(
						'<a id="true_btn" class="state-default" onclick="closeAlert();">'
								+ argBtnFunc.trueText + '</a>&nbsp;&nbsp;');
				$("#div_Alert .content > .func-button > #true_btn").bind(
						'click', argBtnFunc.trueFunc);
			}
			if (argBtnFunc.falseText && argBtnFunc.falseFunc) {
				$("#div_Alert .content > .func-button").append(
						'<a id="false_btn" class="state-default" onclick="closeAlert();">'
								+ argBtnFunc.falseText + '</a>&nbsp;&nbsp;');
				$("#div_Alert .content > .func-button > #false_btn").bind(
						'click', argBtnFunc.falseFunc);
			}
		} else {
			$("#div_Alert .content > .func-button").append(
					'<a class="state-default" onclick="closeAlert();">關閉</a>');
		}
	}

	//全選之函式
	function allSelect(action) {
		for (var i = 0; i < $("[name='entity.checkItem']").length; i++) {
			if (action == 1) {
				$("[name='entity.checkItem']").get(i).checked = true;
			} else {
				$("[name='entity.checkItem']").get(i).checked = false;
			}
		}
	}
</script>
<jsp:include page="/WEB-INF/jsp/layout/css.jsp" />
</head>
<body>
	<div id="div-wrapper">
		<jsp:include page="/WEB-INF/jsp/layout/header.jsp" />
		<div id="div-middle">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tbody>
					<tr>
						<td align="left" valign="top" width="175"><jsp:include
								page="/WEB-INF/jsp/layout/menu.jsp" /></td>
						<td align="left" valign="top">
							<div id="div-contain">
								<div class="tabs-box">
									<div>
										<a id="tabs-items_A" class="tabs-items-hover"
											onclick="showTabsContain('A')"><span
											class="tabs-items-hover-span">系統公告</span></a>
									</div>
								</div>
								<div id="div_nav">
									目前位置：<span>系統公告</span>
								</div>
								<div class="list-box">
									<table cellspacing="1" class="list-table">
										<tbody>
											<tr>
												<td>
													<table width="100%" border="0" cellspacing="2"
														cellpadding="0">
														<tbody>
															<tr>
																<td align="center" valign="top" width="40">
																	<div class="cal_cal">
																		<div class="cal_month">03月</div>
																		<div class="cal_day">24</div>
																	</div>
																</td>
																<td align="left" valign="top">
																	<div class="cal_title">系統上線</div>
																	<div class="cal_content">歡迎蒞臨!</div>
																	<div class="cal_time">發佈時間:2010年03月24日 PM 12:00</div>
																</td>
															</tr>
														</tbody>
													</table>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<jsp:include page="/WEB-INF/jsp/layout/footer.jsp" />
	</div>
	<div id="div_Detail">
		<div class="overlay"></div>
		<div class="content">
			<div class="header">
				<div class="title"></div>
				<div class="close">
					<a href="#" onclick="closeDetail();">關閉</a>
				</div>
			</div>
			<div class="contain"></div>
		</div>
	</div>

	<div id="div_Detail_2">
		<div class="overlay"></div>
		<div class="content">
			<div class="header">
				<div class="title"></div>
				<div class="close">
					<a href="#" onclick="closeDetail_2();">關閉</a>
				</div>
			</div>
			<div class="contain"></div>
		</div>
	</div>

	<div id="div_Detail_3">
		<div class="overlay"></div>
		<div class="content">
			<div class="header">
				<div class="title"></div>
				<div class="close">
					<a href="#" onclick="closeDetail_3();">關閉</a>
				</div>
			</div>
			<div class="contain"></div>
		</div>
	</div>

	<div id="div_ReferenceOwners">
		<div class="overlay"></div>
		<div class="content">
			<div class="header">
				<div class="title"></div>
				<div class="close">
					<a href="#" onclick="closeReferenceOwners();">&nbsp;</a>
				</div>
			</div>
			<div class="contain"></div>
		</div>
	</div>

	<div id="div_ResDbs">
		<div class="overlay"></div>
		<div class="content">
			<div class="header">
				<div class="title"></div>
				<div class="close">
					<a href="#" onclick="closeResDbs();">&nbsp;</a>
				</div>
			</div>
			<div class="contain"></div>
		</div>
	</div>

	<div id="div_Alert">
		<div class="overlay"></div>
		<div class="content">
			<div class="header">
				<div class="title"></div>
				<div class="close">
					<a href="#" onclick="closeAlert();">&nbsp;</a>
				</div>
			</div>
			<div class="contain"></div>
			<div class="func-button"></div>
		</div>
	</div>

	<div id="div_Loading">
		<div class="overlay"></div>
		<div class="content">
			<span class="div_loading_icon"></span>
		</div>
	</div>
</body>
</html>