package com.shouyang.syazs.core.interceptor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionInvocation;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumberService;
import com.shouyang.syazs.core.apply.beLogs.BeLogs;
import com.shouyang.syazs.core.apply.beLogs.BeLogsService;
import com.shouyang.syazs.core.apply.enums.Act;

/**
 * 儲存登入log
 * 
 * @author Roderick
 * @version 2015/1/20
 */
public class LoginLogInterceptor extends RootInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5641049812365809693L;

	@Autowired
	private AccountNumber accountNumber;

	@Autowired
	private AccountNumberService accountNumberService;

	@Autowired
	private BeLogsService beLogsService;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		removeErrorParameters(invocation);

		Map<String, Object> session = invocation.getInvocationContext()
				.getSession();
		String method = invocation.getProxy().getMethod();

		if (method.equals("logout")) {
			accountNumber = (AccountNumber) session.get("login");
			if (accountNumber != null
					&& accountNumber.hasSerNo()
					&& accountNumberService
							.getBySerNo(accountNumber.getSerNo()) != null) {
				beLogsService.save(new BeLogs(Act.登出, accountNumber,
						accountNumber.getCustomer()), accountNumber);
			} else {
				session.clear();
			}
		}

		String result = invocation.invoke();

		if (method.equals("login")) {
			accountNumber = (AccountNumber) session.get("login");
			if (accountNumber != null
					&& accountNumber.hasSerNo()
					&& accountNumberService
							.getBySerNo(accountNumber.getSerNo()) != null) {
				beLogsService.save(new BeLogs(Act.登入, accountNumber,
						accountNumber.getCustomer()), accountNumber);
			}
		}

		return result;
	}

}
