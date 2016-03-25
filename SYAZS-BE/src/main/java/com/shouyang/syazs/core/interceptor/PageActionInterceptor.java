package com.shouyang.syazs.core.interceptor;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * 儲存檢索log
 * 
 * @author Roderick
 * @version 2015/1/20
 */
public class PageActionInterceptor extends RootInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5010620168664114783L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		removeErrorParameters(invocation);

		Map<String, Object> session = ActionContext.getContext().getSession();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		if (invocation.getProxy().getActionName().equals("addCookies")) {
			if (NumberUtils.isDigits(request
					.getParameter("pager.recordPerPage"))) {
				Cookie recordPerPage = new Cookie("recordPerPage",
						request.getParameter("pager.recordPerPage"));
				recordPerPage.setMaxAge(60 * 60 * 24);
				recordPerPage.setPath(request.getContextPath() + "/crud");

				response.addCookie(recordPerPage);

				response.setContentType("text/html");
			}
		} else {
			if (session.get("clazz") != null) {
				session.remove("cellNames");
				session.remove("importList");
				session.remove("total");
				session.remove("normal");
				session.remove("insert");
				session.remove("checkItemSet");
				session.remove("tip");
				session.remove("allChecked");
				session.remove("clazz");
			}
		}

		if (invocation.getAction().toString().contains("feLogs")) {
			String option = request.getParameter("entity.option");
			if (option != null) {
				if (option.equals("logins")) {
					request.setAttribute("logins", "logins");
				} else if (option.equals("keywords")) {
					request.setAttribute("keywords", "keywords");
				} else if (option.equals("clicks")) {
					request.setAttribute("clicks", "clicks");
				} else {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		}
		String result = invocation.invoke();

		return result;
	}
}
