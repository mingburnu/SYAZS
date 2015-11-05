package com.shouyang.syazs.core.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public abstract class RootInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7306761765890885509L;

	protected final transient Logger log = Logger.getLogger(getClass());

	public void removeErrorParameters(ActionInvocation invocation) {
		invocation.getInvocationContext().getParameters().remove("ds");
		invocation.getInvocationContext().getParameters().remove("ds.entity");
		invocation.getInvocationContext().getParameters().remove("ds.pager");
		invocation.getInvocationContext().getParameters().remove("ds.results");
		invocation.getInvocationContext().getParameters().remove("ds.datas");
		invocation.getInvocationContext().getParameters().remove("pager");
		invocation.getInvocationContext().getParameters().remove("entity");
	}

	public boolean isUsableMethod(ActionInvocation invocation)
			throws SecurityException {
		Class<?> noparams[] = {};
		try {
			Method method = invocation
					.getAction()
					.getClass()
					.getDeclaredMethod(invocation.getProxy().getMethod(),
							noparams);

			if (Modifier.toString(method.getModifiers()).contains("public")) {
				return true;
			} else {
				return false;
			}

		} catch (NoSuchMethodException e) {
			return false;
		}
	}
}
