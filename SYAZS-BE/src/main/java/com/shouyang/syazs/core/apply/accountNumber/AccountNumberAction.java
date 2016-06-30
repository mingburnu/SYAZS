package com.shouyang.syazs.core.apply.accountNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.customer.CustomerService;
import com.shouyang.syazs.core.apply.enums.Role;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;

/**
 * 使用者
 * 
 * @author Roderick
 * @version 2014/9/29
 */
@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccountNumberAction extends GenericWebActionFull<AccountNumber> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9198667697775718131L;

	@Autowired
	private AccountNumber accountNumber;

	@Autowired
	private AccountNumberService accountNumberService;

	@Autowired
	private Customer customer;

	@Autowired
	private CustomerService customerService;

	@Override
	protected void validateSave() throws Exception {
		getLegalRoles();
		getEntity().setCustomer(getLoginUser().getCustomer());
		getEntity().setRole(Role.管理員);

		if (getEntity().getStatus() == null) {
			errorMessages.add("狀態錯誤");
		}

		if (StringUtils.isBlank(getEntity().getUserId())) {
			errorMessages.add("用戶代碼不得空白");
		} else {
			if (getEntity().getUserId().replaceAll("[0-9a-zA-Z]", "").length() != 0) {
				errorMessages.add("用戶代碼請使用英文字母及數字");
			} else {
				if (accountNumberService.getSerNoByUserId(getEntity()
						.getUserId()) != 0) {
					errorMessages.add("用戶代碼已存在");
				}
			}
		}

		if (StringUtils.isBlank(getEntity().getUserPw())) {
			errorMessages.add("用戶密碼不得空白");
		}

		if (StringUtils.isBlank(getEntity().getUserName())) {
			errorMessages.add("用戶姓名不得空白");
		}

		if (!isEmail(getEntity().getEmail())) {
			errorMessages.add("email格式不正確");
		}

	}

	@Override
	protected void validateUpdate() throws Exception {
		if (!hasEntity()) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			getLegalRoles();
			getEntity().setCustomer(getLoginUser().getCustomer());
			getEntity().setRole(accountNumber.getRole());

			if (getEntity().getStatus() == null) {
				errorMessages.add("狀態錯誤");
			}

			if (StringUtils.isBlank(getEntity().getUserName())) {
				errorMessages.add("用戶姓名不得空白");
			}

			if (getLoginUser().getRole().equals(Role.管理員)) {
				getEntity().setCustomer(getLoginUser().getCustomer());
			} else {
				if (!hasCustomer()) {
					errorMessages.add("用戶名稱必選");
				} else {
					getEntity().setCustomer(customer);
				}
			}

			if (!isEmail(getEntity().getEmail())) {
				errorMessages.add("email格式不正確");
			}
		}
	}

	@Override
	protected void validateDelete() throws Exception {
		if (!getEntity().hasSerNo()) {
			errorMessages.add("請選擇一筆資料");
		} else {
			if (getEntity().getSerNo() < 1
					|| getEntity().getSerNo() == 9
					|| accountNumberService.getBySerNo(getEntity().getSerNo()) == null) {
				addActionError("有錯誤流水號");
			}
		}
	}

	@Override
	public String add() throws Exception {
		getLegalRoles();

		DataSet<AccountNumber> ds = initDataSet();
		ds.getDatas().put(getLoginUser().getCustomer().getName(),
				getLoginUser().getCustomer().getSerNo());

		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
			if (accountNumber.getRole().equals(Role.系統管理員)
					&& !getLoginUser().getRole().equals(Role.系統管理員)) {
				getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		getLegalRoles();
		DataSet<AccountNumber> ds = initDataSet();
		ds.getDatas().put(getLoginUser().getCustomer().getName(),
				getLoginUser().getCustomer().getSerNo());

		setEntity(accountNumber);
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		DataSet<AccountNumber> ds = accountNumberService.getByRestrictions(
				initDataSet(), getLoginUser());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
					.doubleValue()
					/ ds.getPager().getRecordPerPage().doubleValue());
			ds.getPager().setCurrentPage(lastPage.intValue());
			accountNumberService.getByRestrictions(ds, getLoginUser());
		}

		return LIST;
	}

	@Override
	public String save() throws Exception {
		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			accountNumber = accountNumberService.save(getEntity(),
					getLoginUser());
			accountNumber = accountNumberService.getBySerNo(accountNumber
					.getSerNo());
			setEntity(accountNumber);
			addActionMessage("新增成功");
			return VIEW;
		} else {
			setEntity(getEntity());

			DataSet<AccountNumber> ds = initDataSet();
			ds.getDatas().put(getLoginUser().getCustomer().getName(),
					getLoginUser().getCustomer().getSerNo());

			return ADD;
		}
	}

	@Override
	public String update() throws Exception {
		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			getEntity().setUserName(getEntity().getUserName());
			if (StringUtils.isBlank(getEntity().getUserPw())) {
				accountNumber = accountNumberService.update(getEntity(),
						getLoginUser(), "userId", "userPw");
			} else {
				accountNumber = accountNumberService.update(getEntity(),
						getLoginUser(), "userId");
			}

			accountNumber = accountNumberService.getBySerNo(accountNumber
					.getSerNo());

			if (getLoginUser().getSerNo().equals(accountNumber.getSerNo())) {
				getLoginUser().setCustomer(customer);
			}

			setEntity(accountNumber);
			addActionMessage("修改成功");
			return VIEW;
		} else {
			getEntity().setUserId(
					accountNumberService.getBySerNo(getEntity().getSerNo())
							.getUserId());

			DataSet<AccountNumber> ds = initDataSet();
			if (getLoginUser().getRole().equals(Role.管理員)) {
				ds.getDatas().put(getLoginUser().getCustomer().getName(),
						getLoginUser().getCustomer().getSerNo());
			} else {
				ds.setDatas(customerService.getCusDatas());
			}

			return EDIT;
		}
	}

	@Override
	public String delete() throws Exception {
		validateDelete();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			accountNumberService.deleteBySerNo(getEntity().getSerNo());

			list();
			addActionMessage("刪除成功");
			return LIST;
		} else {
			list();
			return LIST;
		}
	}

	protected boolean hasEntity() throws Exception {
		if (!getEntity().hasSerNo()) {
			return false;
		}

		accountNumber = accountNumberService.getBySerNo(getEntity().getSerNo());
		if (accountNumber == null) {
			return false;
		}

		return true;
	}

	protected boolean isEmail(String email) {
		return ESAPI.validator().isValidInput("account Email", email, "Email",
				254, true);
	}

	protected boolean hasCustomer() throws Exception {
		if (getEntity().getCustomer() == null
				|| !getEntity().getCustomer().hasSerNo()
				|| getEntity().getCustomer().getSerNo() <= 0) {
			return false;
		} else {
			customer = customerService.getBySerNo(getEntity().getCustomer()
					.getSerNo());
			if (customer == null) {
				return false;
			} else {
				return true;
			}
		}
	}

	protected List<Role> getLegalRoles() {
		List<Role> roleList = new ArrayList<Role>(Arrays.asList(Role.values()));
		List<Role> tempList = new ArrayList<Role>();
		roleList.remove(roleList.size() - 1);

		tempList.add(Role.系統管理員);

		roleList.removeAll(tempList);
		ActionContext.getContext().getValueStack().set("roleList", roleList);

		return roleList;
	}
}
