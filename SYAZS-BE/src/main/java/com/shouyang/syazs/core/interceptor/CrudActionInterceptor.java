package com.shouyang.syazs.core.interceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.enums.Role;

/**
 * CRUD Action Interceptor
 * 
 * @author Roderick
 * @version 2015/1/20
 */
public class CrudActionInterceptor extends RootInterceptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4326400245013459644L;

	@Autowired
	private AccountNumber accountNumber;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		removeErrorParameters(invocation);

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		if (invocation.getProxy().getNamespace().equals("/crud")) {
			if (!isUsableMethod(invocation)) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return "list";
			}
		}

		if (!invocation.getAction().toString().contains("beLogs")
				&& !invocation.getAction().toString().contains("feLogs")) {

			String method = invocation.getProxy().getMethod();

			List<String> methodList = Arrays.asList("queue", "paginate",
					"addAllItem", "getCheckedItem", "allCheckedItem",
					"allUncheckedItem", "removeAllItem", "importData",
					"backErrors");

			if (!methodList.contains(method)) {
				if (request.getSession().getAttribute("importList") != null) {
					request.getSession().removeAttribute("cellNames");
					request.getSession().removeAttribute("importList");
					request.getSession().removeAttribute("total");
					request.getSession().removeAttribute("normal");
					request.getSession().removeAttribute("insert");
					request.getSession().removeAttribute("checkItemSet");
					request.getSession().removeAttribute("tip");
					request.getSession().removeAttribute("allChecked");
				}
			}
		}

		if (invocation.getAction().toString().contains("ipRange")) {
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			accountNumber = (AccountNumber) session.get("login");

			if (accountNumber.getRole().equals(Role.管理員)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return "list";
			}
		}

		if (invocation.getAction().toString().contains("group")) {
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			accountNumber = (AccountNumber) session.get("login");

			if (accountNumber.getRole().equals(Role.管理員)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return "list";
			}
		}

		if (invocation.getAction().toString().contains("customer")) {
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			accountNumber = (AccountNumber) session.get("login");

			if (accountNumber.getRole().equals(Role.管理員)) {
				String method = invocation.getProxy().getMethod();

				if (!method.equals("json") && !method.equals("box")) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
					return "list";
				}
			}

		}
		return invocation.invoke();
	}
}
