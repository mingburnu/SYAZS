package com.shouyang.syazs.core.apply.customer;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.apply.enums.Role;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomerAction extends GenericWebActionFull<Customer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4530353636126561614L;

	@Autowired
	private Customer customer;

	@Autowired
	private CustomerService customerService;

	@Override
	protected void validateSave() throws Exception {
		if (StringUtils.isBlank(getEntity().getName())) {
			errorMessages.add("用戶名稱不可空白");
		} else {
			if (getEntity().getName()
					.replaceAll("[a-zA-Z0-9\u4e00-\u9fa5]", "").length() != 0) {
				errorMessages.add("用戶名稱必須是英、數或漢字");
			} else {
				if (customerService.getCusSerNoByName(getEntity().getName()) != 0) {
					errorMessages.add("用戶名稱已存在");
				}
			}
		}

		if (StringUtils.isNotEmpty(getEntity().getTel())) {
			String tel = getEntity().getTel().replaceAll("[/()+-]", "")
					.replace(" ", "");
			if (!NumberUtils.isDigits(tel)) {
				errorMessages.add("電話格式不正確");
			}
		}
	}

	@Override
	protected void validateUpdate() throws Exception {
		if (!hasEntity()) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			if (getEntity().getSerNo() == 9) {
				if (!getLoginUser().getRole().equals(Role.系統管理員)) {
					errorMessages.add("權限不足");
				}
			}

			if (StringUtils.isNotEmpty(getEntity().getTel())) {
				String tel = getEntity().getTel().replaceAll("[/()+-]", "")
						.replace(" ", "");
				if (!NumberUtils.isDigits(tel)) {
					errorMessages.add("電話格式不正確");
				}
			}

			if (gtMaxSize(getRequest(), 1024 * 1024 * 2)) {
				errorMessages.add("檔案太大");
			} else {
				if (ArrayUtils.isNotEmpty(getEntity().getFile())
						&& getEntity().getFile()[0].isFile()) {
					List<String> mimes = Arrays.asList(new String[] {
							"image/gif", "image/jpeg", "image/png" });

					if (!mimes.contains(getFileMime(getEntity().getFile()[0],
							getEntity().getFileFileName()[0]))) {
						errorMessages.add("圖檔限png, jpg, jpeg, gif");
					}
				}
			}
		}
	}

	@Override
	protected void validateDelete() throws Exception {
		if (getLoginUser().getRole().equals(Role.系統管理員)) {

			if (ArrayUtils.isEmpty(getEntity().getCheckItem())) {
				errorMessages.add("請選擇一筆或一筆以上的資料");
			} else {
				Set<Long> deRepeatSet = new HashSet<Long>(
						Arrays.asList(getEntity().getCheckItem()));
				getEntity().setCheckItem(
						deRepeatSet.toArray(new Long[deRepeatSet.size()]));

				int i = 0;
				while (i < getEntity().getCheckItem().length) {
					if (getEntity().getCheckItem()[i] == null
							|| getEntity().getCheckItem()[i] < 1
							|| getEntity().getCheckItem()[i] == 9
							|| customerService.getBySerNo(getEntity()
									.getCheckItem()[i]) == null) {
						errorMessages.add("有錯誤流水號");
						break;
					}
					i++;
				}
			}
		} else {
			errorMessages.add("權限不足");
		}
	}

	@Override
	public String add() throws Exception {
		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (getEntity().getSerNo() != null) {
			customer = customerService.getBySerNo(getEntity().getSerNo());
			setEntity(customer);
		}

		return EDIT;
	}

	@Override
	public String list() throws Exception {
		DataSet<Customer> ds = customerService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
					.doubleValue()
					/ ds.getPager().getRecordPerPage().doubleValue());
			ds.getPager().setCurrentPage(lastPage.intValue());
			ds = customerService.getByRestrictions(ds);
		}

		if (StringUtils.isBlank(getEntity().getOption())) {
			getEntity().setOption("done");
		}

		setDs(ds);
		return LIST;
	}

	@Override
	public String save() throws Exception {
		return null;
		// validateSave();
		// setActionErrors(errorMessages);
		//
		// if (!hasActionErrors()) {
		// customer = customerService.save(getEntity(), getLoginUser());
		//
		// setEntity(customer);
		//
		// addActionMessage("新增成功");
		// return VIEW;
		// } else {
		// return ADD;
		// }
	}

	@Override
	public String update() throws Exception {
		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			if (ArrayUtils.isNotEmpty(getEntity().getFile())
					&& getEntity().getFile()[0].isFile()) {
				String parentDir = "/logo/" + customer.getSerNo();

				if (StringUtils.isNotBlank(customer.getLogo())) {
					FileUtils.deleteDirectory(new File(parentDir));
				}

				File destFile = new File(parentDir, getEntity()
						.getFileFileName()[0]);
				FileUtils.copyFile(getEntity().getFile()[0], destFile);
				getEntity().setLogo(destFile.getAbsolutePath());
			} else {
				getEntity().setLogo(customer.getLogo());
			}

			customer = customerService.update(getEntity(), getLoginUser(),
					"name");

			setEntity(customer);
			addActionMessage("修改成功");
			return VIEW;
		} else {
			if (hasEntity()) {
				getEntity().setName(
						customerService.getBySerNo(getEntity().getSerNo())
								.getName());
				getEntity().setLogo(customer.getLogo());
			}

			return EDIT;
		}
	}

	public String show() throws Exception {
		if (hasEntity()) {
			if (StringUtils.isNotBlank(customer.getLogo())) {
				FileInputStream fis = new FileInputStream(customer.getLogo());
				String mime = getFileMime(new File(customer.getLogo()), "pic");
				getEntity().setDataStatus(mime);
				getEntity().setInputStream(fis);
			} else {
				getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return PIC;
	}

	@Override
	public String delete() throws Exception {
		return null;
		// validateDelete();
		// setActionErrors(errorMessages);
		//
		// if (!hasActionErrors()) {
		// int i = 0;
		// while (i < getEntity().getCheckItem().length) {
		// String name = customerService.getBySerNo(
		// getEntity().getCheckItem()[i]).getName();
		// customerService.deleteBySerNo(getEntity().getCheckItem()[i]);
		// addActionMessage(name + "刪除成功");
		// i++;
		// }
		//
		// list();
		// return LIST;
		// } else {
		// list();
		// return LIST;
		// }
	}

	// public String view() throws Exception {
	// if (hasEntity()) {
	// getRequest().setAttribute("viewSerNo", getEntity().getSerNo());
	// setEntity(customer);
	// } else {
	// getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
	// }
	// return VIEW;
	// }

	public String box() throws Exception {
		getRequest().setAttribute("allCustomers",
				customerService.getAllCustomers());

		return BOX;
	}

	protected boolean hasEntity() throws Exception {
		if (!getEntity().hasSerNo()) {
			return false;
		}

		customer = customerService.getBySerNo(getEntity().getSerNo());
		if (customer == null) {
			return false;
		}

		return true;
	}
}
