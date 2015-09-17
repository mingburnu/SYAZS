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

/**
 * Servlet Filter implementation class ClassNotFoundFilter
 */
public class ClassNotFoundFilter implements Filter {

	private String coreEntityPackage;

	private String moduleEntityPackge;

	/**
	 * Default constructor.
	 */
	public ClassNotFoundFilter() {
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

		if (request.getServletPath().startsWith("/page/")
				&& request.getServletPath().endsWith(".action")) {

			String actionName = request.getServletPath().replaceFirst("/page/",
					"");

			String entityName = actionName.substring(0,
					actionName.lastIndexOf("."));

			if (entityName != null && !entityName.equals("index")
					&& !entityName.equals("query")) {
				try {
					if (entityName.length() == 0) {
						entityName = " ";
					}

					String className = moduleEntityPackge + "." + entityName
							+ "." + entityName.substring(0, 1).toUpperCase()
							+ entityName.substring(1, entityName.length())
							+ "Action";
					Class.forName(className);

				} catch (ClassNotFoundException e) {
					try {
						String className = coreEntityPackage + "." + entityName
								+ "."
								+ entityName.substring(0, 1).toUpperCase()
								+ entityName.substring(1, entityName.length())
								+ "Action";
						Class.forName(className);
					} catch (ClassNotFoundException e1) {
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
						return;
					}
				}
			}
		}

		if (request.getServletPath().startsWith("/crud/apply.")
				&& request.getServletPath().endsWith(".action")) {

			String entityName = request.getServletPath()
					.replaceFirst("/crud/apply.", "").split("\\.")[0];

			try {
				if (entityName.length() == 0) {
					entityName = " ";
				}

				String className = moduleEntityPackge + "." + entityName + "."
						+ entityName.substring(0, 1).toUpperCase()
						+ entityName.substring(1, entityName.length())
						+ "Action";
				Class.forName(className);

			} catch (ClassNotFoundException e) {
				try {
					System.out.println(entityName);
					String className = coreEntityPackage + "." + entityName
							+ "." + entityName.substring(0, 1).toUpperCase()
							+ entityName.substring(1, entityName.length())
							+ "Action";
					Class.forName(className);
				} catch (ClassNotFoundException e1) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					return;
				}
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		coreEntityPackage = filterConfig.getInitParameter("coreEntityPackage");
		moduleEntityPackge = filterConfig
				.getInitParameter("moduleEntityPackage");
	}

}
