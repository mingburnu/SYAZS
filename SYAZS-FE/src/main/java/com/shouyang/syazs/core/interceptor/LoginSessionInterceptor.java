package com.shouyang.syazs.core.interceptor;

import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * 檢查登入session
 * 
 * @author Roderick
 * @version 2014/3/26
 */
public class LoginSessionInterceptor extends RootInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3380957463430167238L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> session = invocation.getInvocationContext()
				.getSession();
		if (session.get(Action.LOGIN) == null) {
			return Action.INPUT;
		}

		return invocation.invoke();
	}

}
