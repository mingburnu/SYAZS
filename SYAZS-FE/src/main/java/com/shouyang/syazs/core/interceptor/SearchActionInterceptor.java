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

	@Autowired
	private AccountNumber accountNumber;

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
		accountNumber = (AccountNumber) session.get("login");

		if (method.equals("list")) {
			String item = request.getServletPath().replace("/crud/apply.", "")
					.replace(".list.action", "");
			if (StringUtils.isBlank(request.getParameter("entity.indexTerm"))) {
				addActionError(invocation, "．請輸入關鍵字。");
			}

			if (session.get("entityRecord") != null) {
				session.remove("entityRecord");
				session.remove("pagerRecord");
			}

			if (!hasActionErrors(invocation)) {
				if (request.getParameter("pager.recordPerPage") == null
						&& request.getParameter("pager.recordPoint") == null) {
					if (accountNumber.hasSerNo()) {
						feLogsService.save(
								new FeLogs(Act.快速查詢, request
										.getParameter("entity.indexTerm"),
										accountNumber.getCustomer(),
										accountNumber, null, null, null, null),
								accountNumber);
					} else {
						feLogsService.save(
								new FeLogs(Act.快速查詢, request
										.getParameter("entity.indexTerm"),
										accountNumber.getCustomer(), null,
										null, null, null, null), accountNumber);
					}
				}
			} else {
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

			if (session.get("entityRecord") != null) {
				session.remove("entityRecord");
				session.remove("pagerRecord");
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
					option = "標題開頭為";
				} else if (!option.equals("標題開頭為") && !option.equals("標題等於")
						&& !option.equals("標題包含文字")
						&& !option.equals("ISSN 等於")) {
					addActionError(invocation, "．請輸入選項。");
					option = "標題開頭為";
				}
			}

			if (!hasActionErrors(invocation)) {
				if (request.getParameter("pager.recordPerPage") == null) {
					if (accountNumber.hasSerNo()) {
						feLogsService.save(
								new FeLogs(Act.項目查詢, request
										.getParameter("entity.indexTerm"),
										accountNumber.getCustomer(),
										accountNumber, null, null, null, null),
								accountNumber);
					} else {
						feLogsService.save(
								new FeLogs(Act.項目查詢, request
										.getParameter("entity.indexTerm"),
										accountNumber.getCustomer(), null,
										null, null, null, null), accountNumber);
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
						if (accountNumber.hasSerNo()) {
							feLogsService.save(new FeLogs(Act.借閱, null,
									accountNumber.getCustomer(), accountNumber,
									Long.parseLong(serNo), null, null, true),
									accountNumber);
						} else {
							feLogsService
									.save(new FeLogs(Act.借閱, null,
											accountNumber.getCustomer(), null,
											Long.parseLong(serNo), null, null,
											true), accountNumber);
						}

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
						if (accountNumber.hasSerNo()) {
							feLogsService.save(new FeLogs(Act.借閱, null,
									accountNumber.getCustomer(), accountNumber,
									null, Long.parseLong(serNo), null, true),
									accountNumber);
						} else {
							feLogsService.save(new FeLogs(Act.借閱, null,
									accountNumber.getCustomer(), null, null,
									Long.parseLong(serNo), null, true),
									accountNumber);
						}

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
						if (accountNumber.hasSerNo()) {
							feLogsService.save(new FeLogs(Act.借閱, null,
									accountNumber.getCustomer(), accountNumber,
									null, null, Long.parseLong(serNo), true),
									accountNumber);
						} else {
							feLogsService.save(new FeLogs(Act.借閱, null,
									accountNumber.getCustomer(), null, null,
									null, Long.parseLong(serNo), true),
									accountNumber);
						}

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

			if (item.equals("ebook") || item.equals("journal")) {
				if (StringUtils.isBlank(option)) {
					addActionError(invocation, "．請選擇字首。");
				} else if (option.equals("0-9") || option.equals("其他")) {
					log.info(option);
				} else if (option.length() == 1
						&& option.replaceAll("[a-zA-Z\u3105-\u3126]", "")
								.length() == 0) {
					log.info(option);
				} else {
					addActionError(invocation, "．請選擇正確字首。");
				}
			}

			if (!hasActionErrors(invocation)) {
				if (accountNumber.hasSerNo()) {
					feLogsService.save(
							new FeLogs(Act.標題查詢, null, accountNumber
									.getCustomer(), accountNumber, null, null,
									null, null), accountNumber);
				} else {
					feLogsService.save(
							new FeLogs(Act.標題查詢, null, accountNumber
									.getCustomer(), null, null, null, null,
									null), accountNumber);
				}
			} else {
				return "prefix";
			}
		}

		if (method.equals("owner")) {
			String serNo = request.getParameter("entity.refSerNo");
			String item = request.getServletPath().replace("/crud/apply.", "")
					.replace(".owner.action", "");

			if (item.equals("database") || item.equals("ebook")
					|| item.equals("journal")) {
				Query query = sessionFactory.getCurrentSession().createQuery(
						"FROM ReferenceOwner r WHERE r.serNo=?");

				if (serNo != null && NumberUtils.isDigits(serNo)
						&& Long.parseLong(serNo) > 0) {
					query.setLong(0, Long.parseLong(serNo));
					List<?> owner = query.list();

					if (CollectionUtils.isNotEmpty(owner)) {
						if (accountNumber.hasSerNo()) {
							feLogsService.save(new FeLogs(Act.單位查詢, null,
									accountNumber.getCustomer(), accountNumber,
									null, null, null, null), accountNumber);
						} else {
							feLogsService.save(new FeLogs(Act.單位查詢, null,
									accountNumber.getCustomer(), null, null,
									null, null, null), accountNumber);
						}
					} else {
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
						return "list";
					}
				} else {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					return "list";
				}
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
