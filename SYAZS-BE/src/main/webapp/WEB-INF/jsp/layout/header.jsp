<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="div-header">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tbody>
			<tr>
				<td width="75%"><div>
						<a class="logo_box"
							href="<%=request.getContextPath()%>/main.action">&nbsp;</a>
					</div></td>
				<td width="25%" align="left" valign="middle">
					<div>&nbsp;</div>
					<div>&nbsp;</div>
					<div class="header_text">
						<span>嗨,</span> <font face="verdana">${login.customer.name}&nbsp;${login.customer.contactUserName}</font>
						| <a href='<s:url namespace="/authorization" action="logout" />'>登出</a>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
