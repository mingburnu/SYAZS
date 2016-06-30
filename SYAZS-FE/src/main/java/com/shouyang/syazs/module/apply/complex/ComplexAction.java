package com.shouyang.syazs.module.apply.complex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.web.GenericAction;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ComplexAction extends GenericAction<Complex> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2472341597272275749L;

	@Autowired
	private ComplexService complexService;

	public String list() throws Exception {
		getRequest().setAttribute(
				"list",
				getRequest().getContextPath()
						+ "/crud/apply.complex.list.action");

		complexService.getByRestrictions(initDataSet());

		return LIST;
	}

}
