package com.shouyang.syazs.core.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class DelegatingFilterProxy
 */
public class LoginFilter implements Filter {

	final static private List<String> IGNORE_URLS = Arrays.asList("/login.jsp",
			"/index.jsp", "/authorization/userEntry.action", "/admin/h2");

	/**
	 * Default constructor.
	 */
	public LoginFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String servletPath = request.getServletPath();

		if (request.getSession().getAttribute("login") == null
				&& IGNORE_URLS.indexOf(servletPath) == -1) {
			response.sendRedirect(request.getContextPath()
					+ "/authorization/userEntry.action?" + Math.random());
		}

		try {
			chain.doFilter(request, response);
		} catch (IllegalStateException e) {

		}

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
