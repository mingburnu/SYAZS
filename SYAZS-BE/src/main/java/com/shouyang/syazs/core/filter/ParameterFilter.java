package com.shouyang.syazs.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

/**
 * Servlet Filter implementation class ParameterFilter
 */
public class ParameterFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public ParameterFilter() {
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

//		if (request.getSession().getAttribute("redirect") != null) {
//			request.getSession().removeAttribute("redirect");
//		} else {
//			if (StringUtils.isNotEmpty(request.getParameter("ds.entity"))) {
//				request.getSession().setAttribute("redirect", true);
//				response.sendRedirect(request.getContextPath()
//						+ request.getServletPath());
//			}
//		}

		chain.doFilter(request, response);

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
