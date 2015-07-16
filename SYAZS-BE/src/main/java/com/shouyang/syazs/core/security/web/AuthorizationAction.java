package com.shouyang.syazs.core.security.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumberService;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericAction;

/**
 * LoginAction
 * 
 * @author Roderick
 * @version 2015/01/19
 */
@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AuthorizationAction extends GenericAction<AccountNumber> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8659949915439541326L;

	@Autowired
	private AccountNumberService userService;

	@Autowired
	private AccountNumber user;

	@Autowired
	private DataSet<AccountNumber> ds;

	protected void validateLogin() throws Exception {
		boolean checkLogin = true;
		if (StringUtils.isBlank(user.getUserId())
				|| StringUtils.isBlank(user.getUserPw())) {
			addActionError("請輸入帳號和密碼。");
			checkLogin = false;
		}

		if (checkLogin) { // 帳號密碼皆有輸入時才進行檢核
			boolean isTrueUserId = false;
			boolean isTrueUserPw = false;
			try {
				isTrueUserId = userService.checkUserId(user);
				isTrueUserPw = userService.checkUserPw(user);

			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
				throw new Exception(e);
			}

			// 使用者存在才進行密碼檢核
			if (isTrueUserId) {
				if (!isTrueUserPw) {
					addActionError("您輸入的密碼不正確，請重新輸入。");
				}
			} else {
				addActionError("您輸入的帳號名稱不正確，請重新輸入。");
			}
		}
	}

	/**
	 * 登入
	 * 
	 * @return
	 * @throws Exception
	 */
	public String login() throws Exception {
		validateLogin();

		if (!hasActionErrors()) {
			try {
				ds.setEntity(user);
				ds = userService.getByRestrictions(ds);
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
				throw new Exception(e);
			}

			getSession().put(LOGIN, ds.getResults().get(0));
			return LOGIN;

		} else {
			return INPUT;
		}

	}

	/**
	 * 登出
	 * 
	 * @return
	 * @throws Exception
	 */
	public String logout() throws Exception {
		if (getSession().get(LOGIN) != null) {
			getSession().clear();
		}

		return INPUT;
	}

	public AccountNumber getUser() {
		return user;
	}

	public void setUser(AccountNumber user) {
		this.user = user;
	}

	public DataSet<AccountNumber> getDs() {
		return ds;
	}

	public void setDs(DataSet<AccountNumber> ds) {
		this.ds = ds;
	}

}
