package com.shouyang.syazs.core.interceptor;

import java.util.Map;

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

		return invocation.invoke();
	}
}
