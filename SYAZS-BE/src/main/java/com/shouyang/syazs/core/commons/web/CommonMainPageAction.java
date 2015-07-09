package com.shouyang.syazs.core.commons.web;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.web.GenericAction;

/**
 * CommonHomePageAction
 * 
 * @author Roderick
 * @version 2014/10/12
 */
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CommonMainPageAction extends GenericAction<AccountNumber> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8307378612623091026L;
	
}
