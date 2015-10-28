package com.shouyang.syazs.core.security.web;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumberService;
import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.ipRange.IpRange;
import com.shouyang.syazs.core.apply.ipRange.IpRangeService;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericAction;

/**
 * LoginAction
 * 
 * @author Roderick
 * @version 2014/10/2
 */
@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AuthorizationAction extends GenericAction<AccountNumber> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2493250353997086462L;

	@Autowired
	private AccountNumberService userService;

	@Autowired
	private AccountNumber user;

	@Autowired
	private Customer customer;

	@Autowired
	private IpRangeService ipRangeService;

	@Autowired
	private DataSet<AccountNumber> ds;

	protected void validateLogin() throws Exception {
		boolean checkLogin = true;
		if (StringUtils.isBlank(user.getUserId())) {
			addActionError("請輸入帳號。");
			checkLogin = false;
		}

		if (StringUtils.isBlank(user.getUserPw())) {
			addActionError("請輸入密碼。");
			checkLogin = false;
		}

		// 帳號密碼皆有輸入時才進行檢核
		if (checkLogin) {
			boolean isValidUser = false;
			try {
				isValidUser = userService.checkUser(user);
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
				throw new Exception(e);
			}

			if (!isValidUser) {
				addActionError("帳號密碼錯誤，請重新輸入。");
			}

			if (!userService.isValidStatus(user)) {
				addActionError("帳號未生效或審核中。");
			}
		}
	}

	protected boolean validateIp(String ip, List<IpRange> allIpList) {
		String[] ipNum = ip.split("\\.");
		for (int i = 0; i < allIpList.size(); i++) {
			String[] start = allIpList.get(i).getIpRangeStart().split("\\.");
			String[] end = allIpList.get(i).getIpRangeEnd().split("\\.");

			if (ipNum[0].equals(start[0]) && ipNum[1].equals(start[1])) {
				if (Integer.parseInt(ipNum[2]) * 1000
						+ Integer.parseInt(ipNum[3]) >= Integer
						.parseInt(start[2]) * 1000 + Integer.parseInt(start[3])
						&& Integer.parseInt(ipNum[2]) * 1000
								+ Integer.parseInt(ipNum[3]) <= Integer
								.parseInt(end[2])
								* 1000
								+ Integer.parseInt(end[3])) {
					return true;
				}
			}
		}
		return false;
	}

	protected Customer getMatchCustomer(String ip, List<IpRange> allIpList) {
		String[] ipNum = ip.split("\\.");
		for (int i = 0; i < allIpList.size(); i++) {
			String[] start = allIpList.get(i).getIpRangeStart().split("\\.");
			String[] end = allIpList.get(i).getIpRangeEnd().split("\\.");

			if (ipNum[0].equals(start[0]) && ipNum[1].equals(start[1])) {
				if (Integer.parseInt(ipNum[2]) * 1000
						+ Integer.parseInt(ipNum[3]) >= Integer
						.parseInt(start[2]) * 1000 + Integer.parseInt(start[3])
						&& Integer.parseInt(ipNum[2]) * 1000
								+ Integer.parseInt(ipNum[3]) <= Integer
								.parseInt(end[2])
								* 1000
								+ Integer.parseInt(end[3])) {
					return allIpList.get(i).getCustomer();
				}
			}
		}
		return null;
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
	 * 訪客進入
	 * 
	 * @return
	 * @throws Exception
	 */
	public String userEntry() throws Exception {
		String ip = getRequest().getRemoteAddr();

		if (validateIp(ip, ipRangeService.getAllIpList())
				&& getLoginUser() == null) {
			try {
				user = new AccountNumber();
				user.setUserId("guest");
				user.setCustomer(getMatchCustomer(ip,
						ipRangeService.getAllIpList()));
				ds.setEntity(user);
				ds = userService.getByRestrictions(ds);
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
				throw new Exception(e);
			}

			customer.setContactUserName("訪客");
			getSession().put(LOGIN, user);
			return LOGIN;
		} else if (getLoginUser() != null) {
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

		String ip = getRequest().getRemoteAddr();
		List<IpRange> allipList = ipRangeService.getAllIpList();
		if (validateIp(ip, allipList) && getLoginUser() == null) {
			try {
				user = new AccountNumber();
				user.setUserId("guest");
				user.setCustomer(getMatchCustomer(ip, allipList));
				ds.setEntity(user);
				ds = userService.getByRestrictions(ds);
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
				throw new Exception(e);
			}

			customer.setContactUserName("訪客");
			getSession().put(LOGIN, user);
			return LOGIN;
		} else {
			return INPUT;
		}
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
