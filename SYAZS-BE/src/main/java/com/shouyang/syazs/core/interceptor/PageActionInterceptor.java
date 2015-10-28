package com.shouyang.syazs.core.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		if (session.get("importList") != null) {
			session.remove("cellNames");
			session.remove("importList");
			session.remove("total");
			session.remove("normal");
			session.remove("checkItemSet");
		}

		if (invocation.getAction().toString().contains("feLogs")) {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
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
