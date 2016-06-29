package com.shouyang.syazs.core.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.customer.CustomerService;
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

	@Autowired
	private Customer customer;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private FeLogsService feLogsService;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		removeErrorParameters(invocation);

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Map<String, Object> session = invocation.getInvocationContext()
				.getSession();

		if (invocation.getProxy().getNamespace().equals("/page")) {
			if (session.get("entityRecord") != null) {
				session.remove("entityRecord");
				session.remove("pagerRecord");
			}

			if (invocation.getProxy().getActionName().equals("index")) {
				customer = customerService.getBySerNo(9L);
				if (customer != null) {
					request.setAttribute("customer", customer);
				} else {
					request.setAttribute("customer", new Customer());
				}
			}

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
			}
		}

		if (invocation.getProxy().getNamespace().equals("/crud")) {
			if (!isUsableMethod(invocation)) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return "list";
			}
		}

		String method = invocation.getProxy().getMethod();

		if (method.equals("list")) {
			boolean saveLog = true;
			String item = request.getServletPath().replace("/crud/apply.", "")
					.replace(".list.action", "");
			String indexTerm = request.getParameter("entity.indexTerm");
			String option = request.getParameter("entity.option");

			if (session.get("entityRecord") != null) {
				session.remove("entityRecord");
				session.remove("pagerRecord");
			}

			if (item.equals("database")) {
				if (StringUtils.isBlank(indexTerm)) {
					addActionError(invocation, "．請輸入關鍵字。");
				}

				if (StringUtils.isBlank(option)) {
					addActionError(invocation, "．請輸入選項。");
					option = "標題開頭為";
				} else if (!option.equals("標題開頭為") && !option.equals("標題包含文字")
						&& !option.equals("出版社")) {
					addActionError(invocation, "．請輸入選項。");
					option = "標題開頭為";
				}
			}

			if (item.equals("ebook")) {
				if (StringUtils.isBlank(indexTerm)) {
					addActionError(invocation, "．請輸入關鍵字。");
				}

				if (StringUtils.isBlank(option)) {
					addActionError(invocation, "．請輸入選項。");
					option = "標題開頭為";
				} else if (!option.equals("標題開頭為") && !option.equals("標題包含文字")
						&& !option.equals("ISBN 等於") && !option.equals("出版社")
						&& !option.equals("第一作者") && !option.equals("次要作者")
						&& !option.equals("分類號")) {
					addActionError(invocation, "．請輸入選項。");
					option = "標題開頭為";
				}
			}

			if (item.equals("journal")) {
				if (StringUtils.isBlank(indexTerm)) {
					addActionError(invocation, "．請輸入關鍵字。");
				}

				if (StringUtils.isBlank(option)) {
					addActionError(invocation, "．請輸入選項。");
					option = "標題開頭為";
				} else if (!option.equals("標題開頭為") && !option.equals("標題包含文字")
						&& !option.equals("ISSN 等於") && !option.equals("出版社")
						&& !option.equals("分類號")) {
					addActionError(invocation, "．請輸入選項。");
					option = "標題開頭為";
				}
			}

			if (item.equals("complex")) {
				if (StringUtils.isBlank(indexTerm)) {
					saveLog = false;
				}
			}

			if (!hasActionErrors(invocation)) {
				if (saveLog) {
					if (request.getParameter("pager.recordPerPage") == null) {
						feLogsService.save(
								new FeLogs(Act.查詢, request
										.getParameter("entity.indexTerm"),
										null, null, null), null);
					}
				}
			} else {
				invocation.getInvocationContext().getValueStack()
						.set("item", item);
				invocation.getInvocationContext().getValueStack()
						.set("option", option);
				invocation.getInvocationContext().getValueStack()
						.set("indexTerm", indexTerm);
				return "prefix";
			}
		}

		if (method.equals("click")) {
			String serNo = request.getParameter("entity.serNo");
			String item = request.getServletPath().replace("/crud/apply.", "")
					.replace(".click.action", "");

			Query query = sessionFactory.getCurrentSession().createQuery(
					"SELECT url FROM " + StringUtils.capitalize(item)
							+ " o WHERE o.serNo=?");

			if (serNo != null && NumberUtils.isDigits(serNo)
					&& Long.parseLong(serNo) > 0) {
				if (item.equals("database")) {
					query.setLong(0, Long.parseLong(serNo));
					List<?> url = query.list();

					if (CollectionUtils.isNotEmpty(url)
							&& StringUtils.isNotBlank((String) url.get(0))) {
						feLogsService.save(
								new FeLogs(Act.點擊, null, Long.parseLong(serNo),
										null, null), null);

						response.sendRedirect((String) url.get(0));
					} else {
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
					}
				}

				if (item.equals("ebook")) {
					query.setLong(0, Long.parseLong(serNo));
					List<?> url = query.list();

					if (CollectionUtils.isNotEmpty(url)
							&& StringUtils.isNotBlank((String) url.get(0))) {
						feLogsService.save(
								new FeLogs(Act.點擊, null, null, Long
										.parseLong(serNo), null), null);

						response.sendRedirect((String) url.get(0));
					} else {
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
					}
				}

				if (item.equals("journal")) {
					query.setLong(0, Long.parseLong(serNo));
					List<?> url = query.list();

					if (CollectionUtils.isNotEmpty(url)
							&& StringUtils.isNotBlank((String) url.get(0))) {
						feLogsService.save(new FeLogs(Act.點擊, null, null, null,
								Long.parseLong(serNo)), null);

						response.sendRedirect((String) url.get(0));
					} else {
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
					}
				}

			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		}

		if (method.equals("prefix")) {
			String item = request.getServletPath().replace("/crud/apply.", "")
					.replace(".prefix.action", "");
			String option = request.getParameter("entity.option");

			if (session.get("entityRecord") != null) {
				session.remove("entityRecord");
				session.remove("pagerRecord");
			}

			if (item.equals("database") || item.equals("ebook")
					|| item.equals("journal")) {
				if (StringUtils.isBlank(option)) {
					addActionError(invocation, "．請選擇字首。");
				} else if (option.equals("0-9") || option.equals("其他")) {
					log.info(option);
				} else if (option.length() == 1
						&& option.replaceAll("[a-zA-Z\u3105-\u3129]", "")
								.length() == 0) {
					log.info(option);
				} else {
					log.info(option);
					addActionError(invocation, "．請選擇正確字首。");
				}
			}

			if (hasActionErrors(invocation)) {
				return "prefix";
			}
		}

		return invocation.invoke();
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
