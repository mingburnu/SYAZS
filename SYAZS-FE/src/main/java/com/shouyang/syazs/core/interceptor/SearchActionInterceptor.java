package com.shouyang.syazs.core.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.enums.Act;
import com.shouyang.syazs.core.apply.feLogs.FeLogs;
import com.shouyang.syazs.core.apply.feLogs.FeLogsService;

/**
 * 儲存檢索log
 * 
 * @author Roderick
 * @version 2015/1/20
 */
public class SearchActionInterceptor extends RootInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5107110044080126585L;

	Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private AccountNumber accountNumber;

	@Autowired
	private FeLogs feLogs;

	@Autowired
	private FeLogsService feLogsService;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		removeErrorParameters(invocation);

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		Map<String, Object> session = invocation.getInvocationContext()
				.getSession();

		String method = invocation.getProxy().getMethod();

		if (method.equals("list")) {
			String item = request.getServletPath().replace("/crud/apply.", "")
					.replace(".list.action", "");
			if (StringUtils.isBlank(request.getParameter("entity.indexTerm"))) {
				addActionError(invocation, "．請輸入關鍵字。");
			}

			if (hasActionErrors(invocation)) {
				invocation.getInvocationContext().getValueStack()
						.set("item", item);
				return "query";
			}
		}

		if (method.equals("focus")) {
			String item = request.getServletPath().replace("/crud/apply.", "")
					.replace(".focus.action", "");
			String indexTerm = request.getParameter("entity.indexTerm");
			String option = request.getParameter("entity.option");
			if (StringUtils.isBlank(indexTerm)) {
				addActionError(invocation, "．請輸入關鍵字。");
			}

			if (item.equals("database")) {
				if (StringUtils.isBlank(option)) {
					addActionError(invocation, "．請輸入選項。");
					option = "中文題名";
				} else if (!option.equals("中文題名") && !option.equals("英文題名")
						&& !option.equals("出版社") && !option.equals("內容描述")) {
					addActionError(invocation, "．請輸入選項。");
					option = "中文題名";
				}
			}

			if (item.equals("ebook")) {
				if (StringUtils.isBlank(option)) {
					addActionError(invocation, "．請輸入選項。");
					option = "標題開頭為";
				} else if (!option.equals("標題開頭為") && !option.equals("標題等於")
						&& !option.equals("標題包含文字")
						&& !option.equals("ISBN 等於")) {
					addActionError(invocation, "．請輸入選項。");
					option = "標題開頭為";
				}
			}

			if (item.equals("journal")) {
				if (StringUtils.isBlank(option)) {
					addActionError(invocation, "．請輸入選項。");
					option = "中文刊名";
				} else if (!option.equals("中文刊名") && !option.equals("英文刊名")
						&& !option.equals("英文縮寫") && !option.equals("出版商")
						&& !option.equals("ISSN")) {
					addActionError(invocation, "．請輸入選項。");
					option = "中文刊名";
				}
			}

			if (hasActionErrors(invocation)) {
				invocation.getInvocationContext().getValueStack()
						.set("item", item);
				invocation.getInvocationContext().getValueStack()
						.set("option", option);
				invocation.getInvocationContext().getValueStack()
						.set("indexTerm", indexTerm);
				response.sendRedirect(request.getContextPath() + "/" + item
						+ ".jsp");
				// return "adv_query";
			}
		}

		String result = invocation.invoke();

		if (method.equals("list")) {

			accountNumber = (AccountNumber) session.get("login");

			if (request.getParameter("pager.recordPerPage") == null
					&& request.getParameter("pager.recordPoint") == null) {
				if (accountNumber.getSerNo() != null) {
					feLogsService.save(
							new FeLogs(Act.綜合查詢, request
									.getParameter("keywords"), accountNumber
									.getCustomer(), accountNumber, 0L, 0L, 0L),
							accountNumber);
				} else {
					feLogsService.save(
							new FeLogs(Act.綜合查詢, request
									.getParameter("keywords"), accountNumber
									.getCustomer(), null, 0L, 0L, 0L),
							accountNumber);
				}

			}
		}

		if (method.equals("focus")) {

			accountNumber = (AccountNumber) session.get("login");

			if (request.getParameter("pager.recordPerPage") == null) {
				if (accountNumber.getSerNo() != null) {
					feLogsService.save(
							new FeLogs(Act.項目查詢, request
									.getParameter("keywords"), accountNumber
									.getCustomer(), accountNumber, 0L, 0L, 0L),
							accountNumber);
				} else {
					feLogsService.save(
							new FeLogs(Act.項目查詢, request
									.getParameter("keywords"), accountNumber
									.getCustomer(), null, 0L, 0L, 0L),
							accountNumber);
				}
			}
		}

		return result;
	}

	private void addActionError(ActionInvocation invocation, String message) {
		Object action = invocation.getAction();
		if (action instanceof ValidationAware) {
			((ValidationAware) action).addActionError(message);
		}
	}

	private boolean hasActionErrors(ActionInvocation invocation) {
		Object action = invocation.getAction();
		return ((ValidationAware) action).hasActionErrors();
	}
}
