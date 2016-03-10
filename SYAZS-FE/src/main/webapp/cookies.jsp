<%@ page import="org.apache.commons.lang3.math.NumberUtils"%>
<%
	if (NumberUtils.isDigits(request
			.getParameter("pager.recordPerPage"))) {
		Cookie recordPerPage = new Cookie("recordPerPage",
				request.getParameter("pager.recordPerPage"));
		recordPerPage.setMaxAge(60 * 60 * 24);

		response.addCookie(recordPerPage);

		response.setContentType("text/html");
	}

	response.sendRedirect(request.getContextPath());
%>