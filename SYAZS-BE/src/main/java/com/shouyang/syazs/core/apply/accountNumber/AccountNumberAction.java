package com.shouyang.syazs.core.apply.accountNumber;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.customer.CustomerService;
import com.shouyang.syazs.core.apply.enums.Role;
import com.shouyang.syazs.core.apply.enums.Status;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.model.Pager;
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

	private String[] checkItem;

	private File[] file;

	private String[] fileFileName;

	private String[] fileContentType;

	@Autowired
	private AccountNumber accountNumber;

	@Autowired
	private AccountNumberService accountNumberService;

	@Autowired
	private Customer customer;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private DataSet<Customer> dsCustomer;

	private String[] importSerNos;

	@Override
	protected void validateSave() throws Exception {
		List<Role> roleList = new ArrayList<Role>(Arrays.asList(Role.values()));
		List<Role> tempList = new ArrayList<Role>();
		roleList.remove(roleList.size() - 1);

		int roleCode = roleList.indexOf(getLoginUser().getRole());

		for (int i = 0; i < roleCode; i++) {
			tempList.add(roleList.get(i));
		}

		roleList.removeAll(tempList);

		boolean isLegalRole = false;
		for (int i = 0; i < roleList.size(); i++) {
			if (StringUtils.isNotBlank(getRequest().getParameter("role"))
					&& getRequest().getParameter("role").equals(
							roleList.get(i).getRole())) {
				isLegalRole = true;
			}
		}

		List<Status> statusList = new ArrayList<Status>(Arrays.asList(Status
				.values()));

		boolean isLegalStatus = false;
		for (int i = 0; i < statusList.size(); i++) {
			if (StringUtils.isNotBlank(getRequest().getParameter("status"))
					&& getRequest().getParameter("status").equals(
							statusList.get(i).getStatus())) {
				isLegalStatus = true;
			}
		}
		
		if (isLegalRole) {
			getEntity()
					.setRole(Role.valueOf(getRequest().getParameter("role")));
		} else {
			errorMessages.add("角色錯誤");
		}

		if (isLegalStatus) {
			getEntity().setStatus(
					Status.valueOf(getRequest().getParameter("status")));
		} else {
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

				if (getEntity().getUserId().equals("guest")) {
					errorMessages.add("不可使用guest作為用戶代碼");
				}
			}
		}

		if (StringUtils.isBlank(getEntity().getUserPw())) {
			errorMessages.add("用戶密碼不得空白");
		}

		if (StringUtils.isBlank(getEntity().getUserName())) {
			errorMessages.add("用戶姓名不得空白");
		}

		if (StringUtils.isBlank(getRequest().getParameter("cusSerNo"))
				|| !NumberUtils.isDigits(getRequest().getParameter("cusSerNo"))
				|| Long.parseLong(getRequest().getParameter("cusSerNo")) < 1
				|| customerService.getBySerNo(Long.parseLong(getRequest()
						.getParameter("cusSerNo"))) == null) {
			errorMessages.add("用戶名稱必選");
		} else {
			getEntity().setCustomer(
					customerService.getBySerNo(Long.parseLong(getRequest()
							.getParameter("cusSerNo"))));

			if (getLoginUser().getRole().equals(Role.管理員)) {
				if (getLoginUser().getCustomer().getSerNo() != Long
						.parseLong(getRequest().getParameter("cusSerNo"))) {
					errorMessages.add("用戶名稱不正確");
				}
			}
		}

		if (StringUtils.isNotEmpty(getEntity().getEmail())) {
			if (!isEmail(getEntity().getEmail())) {
				errorMessages.add("email格式不正確");
			}
		}

	}

	@Override
	protected void validateUpdate() throws Exception {
		if (!hasEntity()) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			List<Status> statusList = new ArrayList<Status>(Arrays.asList(Status
					.values()));
			List<Role> roleList = new ArrayList<Role>(Arrays.asList(Role.values()));
			List<Role> tempList = new ArrayList<Role>();
			roleList.remove(roleList.size() - 1);

			int roleCode = roleList.indexOf(getLoginUser().getRole());

			for (int i = 0; i < roleCode; i++) {
				tempList.add(roleList.get(i));
			}

			roleList.removeAll(tempList);
			
			if (!roleList.contains(accountNumberService.getBySerNo(
					getEntity().getSerNo()).getRole())) {
				errorMessages.add("權限不足");
				getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
			}
			
			if (errorMessages.size() == 0) {
				boolean isLegalRole = false;
				for (int i = 0; i < roleList.size(); i++) {
					if (StringUtils.isNotBlank(getRequest()
							.getParameter("role"))
							&& getRequest().getParameter("role").equals(
									roleList.get(i).getRole())) {
						isLegalRole = true;
					}
				}

				boolean isLegalStatus = false;
				for (int i = 0; i < statusList.size(); i++) {
					if (StringUtils.isNotBlank(getRequest().getParameter(
							"status"))
							&& getRequest().getParameter("status").equals(
									statusList.get(i).getStatus())) {
						isLegalStatus = true;
					}
				}

				if (isLegalRole) {
					getEntity().setRole(
							Role.valueOf(getRequest().getParameter("role")));
				} else {
					errorMessages.add("角色錯誤");
				}

				if (isLegalStatus) {
					getEntity()
							.setStatus(
									Status.valueOf(getRequest().getParameter(
											"status")));
				} else {
					errorMessages.add("狀態錯誤");
				}

				if (StringUtils.isBlank(getEntity().getUserName())) {
					errorMessages.add("用戶姓名不得空白");
				}

				if (StringUtils.isBlank(getRequest().getParameter("cusSerNo"))
						|| !NumberUtils.isDigits(getRequest().getParameter(
								"cusSerNo"))
						|| Long.parseLong(getRequest().getParameter("cusSerNo")) < 1
						|| customerService.getBySerNo(Long
								.parseLong(getRequest()
										.getParameter("cusSerNo"))) == null) {
					errorMessages.add("用戶名稱必選");
				} else {
					getEntity().setCustomer(
							customerService.getBySerNo(Long
									.parseLong(getRequest().getParameter(
											"cusSerNo"))));

					if (getLoginUser().getRole().equals(Role.管理員)) {
						if (getLoginUser().getCustomer().getSerNo() != getEntity()
								.getCustomer().getSerNo()) {
							errorMessages.add("用戶名稱不正確");
						}
					}
				}
			}

			if (StringUtils.isNotEmpty(getEntity().getEmail())) {
				if (!isEmail(getEntity().getEmail())) {
					errorMessages.add("email格式不正確");
				}
			}
		}
	}

	@Override
	protected void validateDelete() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String add() throws Exception {
		List<Role> roleList = new ArrayList<Role>(Arrays.asList(Role.values()));
		List<Role> tempList = new ArrayList<Role>();
		roleList.remove(roleList.size() - 1);

		int roleCode = roleList.indexOf(getLoginUser().getRole());

		for (int i = 0; i < roleCode; i++) {
			tempList.add(roleList.get(i));
		}

		roleList.removeAll(tempList);

		getRequest().setAttribute("roleList", roleList);

		List<Status> statusList = new ArrayList<Status>(Arrays.asList(Status
				.values()));
		getRequest().setAttribute("statusList", statusList);

		if (getLoginUser().getRole().equals(Role.管理員)) {
			customer.setSerNo(getLoginUser().getCustomer().getSerNo());

		}

		dsCustomer.setEntity(customer);
		dsCustomer = customerService.getByRestrictions(dsCustomer);

		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
			List<Role> roleList = new ArrayList<Role>(Arrays.asList(Role.values()));
			List<Role> tempList = new ArrayList<Role>();
			roleList.remove(roleList.size() - 1);

			int roleCode = roleList.indexOf(getLoginUser().getRole());

			for (int i = 0; i < roleCode; i++) {
				tempList.add(roleList.get(i));
			}

			roleList.removeAll(tempList);

			getRequest().setAttribute("roleList", roleList);

			List<Status> statusList = new ArrayList<Status>(Arrays.asList(Status
					.values()));
			getRequest().setAttribute("statusList", statusList);

			if (getLoginUser().getRole().equals(Role.管理員)) {
				customer.setSerNo(getLoginUser().getCustomer().getSerNo());

			}

			dsCustomer.setEntity(customer);
			dsCustomer = customerService.getByRestrictions(dsCustomer);
			
			boolean isLegalRole = false;

			if (roleList.contains(accountNumber.getRole())) {
				isLegalRole = true;
			}

			if (isLegalRole) {
				if (getLoginUser().getRole().equals(Role.管理員)) {
					if (accountNumber.getCustomer().getName()
							.equals(getLoginUser().getCustomer().getName())) {
						setEntity(accountNumber);
					} else {
						getResponse().sendError(
								HttpServletResponse.SC_FORBIDDEN);
					}
				} else {
					setEntity(accountNumber);
				}

			} else {
				getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return EDIT;
	}

	@Override
	public String list() throws Exception {
		DataSet<AccountNumber> ds = initDataSet();
		ds.setPager(Pager.getChangedPager(
				getRequest().getParameter("recordPerPage"), getRequest()
						.getParameter("recordPoint"), ds.getPager()));

		ds = accountNumberService.getByRestrictions(ds, getLoginUser());

		setDs(ds);
		return LIST;
	}

	@Override
	public String save() throws Exception {
		List<Role> roleList = new ArrayList<Role>(Arrays.asList(Role.values()));
		List<Role> tempList = new ArrayList<Role>();
		roleList.remove(roleList.size() - 1);

		int roleCode = roleList.indexOf(getLoginUser().getRole());

		for (int i = 0; i < roleCode; i++) {
			tempList.add(roleList.get(i));
		}

		roleList.removeAll(tempList);

		List<Status> statusList = new ArrayList<Status>(Arrays.asList(Status
				.values()));

		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			getEntity().setUserId(getEntity().getUserId());
			getEntity().setUserName(getEntity().getUserName());
			accountNumber = accountNumberService.save(getEntity(),
					getLoginUser());
			accountNumber.setCustomer(accountNumber.getCustomer());
			setEntity(accountNumber);
			return VIEW;
		} else {
			getRequest().setAttribute("roleList", roleList);
			getRequest().setAttribute("statusList", statusList);
			setEntity(getEntity());
			if (getLoginUser().getRole().equals(Role.管理員)) {
				customer.setSerNo(getLoginUser().getCustomer().getSerNo());

			}

			dsCustomer.setEntity(customer);
			dsCustomer = customerService.getByRestrictions(dsCustomer);
			setEntity(getEntity());
			return ADD;
		}
	}

	@Override
	public String update() throws Exception {
		List<Status> statusList = new ArrayList<Status>(Arrays.asList(Status
				.values()));
		List<Role> roleList = new ArrayList<Role>(Arrays.asList(Role.values()));
		List<Role> tempList = new ArrayList<Role>();
		roleList.remove(roleList.size() - 1);

		int roleCode = roleList.indexOf(getLoginUser().getRole());

		for (int i = 0; i < roleCode; i++) {
			tempList.add(roleList.get(i));
		}

		roleList.removeAll(tempList);

		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			getEntity().setUserName(getEntity().getUserName());
			if (StringUtils.isBlank(getEntity().getUserPw())) {
				accountNumberService.updateLog(getEntity());
				accountNumber = accountNumberService.update(getEntity(),
						getLoginUser(), "userId", "userPw");
			} else {
				accountNumberService.updateLog(getEntity());
				accountNumber = accountNumberService.update(getEntity(),
						getLoginUser(), "userId");
			}

			setEntity(accountNumber);
			addActionMessage("修改成功");
			return VIEW;
		} else {
			getRequest().setAttribute("roleList", roleList);
			getRequest().setAttribute("statusList", statusList);

			if (hasEntity()) {
				getEntity().setUserId(
						accountNumberService.getBySerNo(getEntity().getSerNo())
								.getUserId());
			}

			if (getLoginUser().getRole().equals(Role.管理員)) {
				customer.setSerNo(getLoginUser().getCustomer().getSerNo());

			}

			dsCustomer.setEntity(customer);
			dsCustomer = customerService.getByRestrictions(dsCustomer);
			return EDIT;
		}
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String deauthorize() throws Exception {
		if (ArrayUtils.isEmpty(checkItem)) {
			addActionError("請選擇一筆或一筆以上的資料");
		} else {
			int i = 0;
			while (i < checkItem.length) {
				if (!NumberUtils.isDigits(String.valueOf(checkItem[i]))
						|| Long.parseLong(checkItem[i]) < 1
						|| accountNumberService.getBySerNo(Long
								.parseLong(checkItem[i])) == null) {
					addActionError(checkItem[i] + "為不可利用的流水號");
				}
				i++;
			}
		}

		if (!hasActionErrors()) {
			List<Role> roleList = new ArrayList<Role>(Arrays.asList(Role
					.values()));
			List<Role> tempList = new ArrayList<Role>();

			roleList.remove(roleList.size() - 1);

			int roleCode = roleList.indexOf(getLoginUser().getRole());

			for (int i = 0; i < roleCode; i++) {
				tempList.add(roleList.get(i));
			}

			roleList.removeAll(tempList);

			for (int j = 0; j < checkItem.length; j++) {
				boolean isLegalRole = false;
				accountNumber = accountNumberService.getBySerNo(Long
						.parseLong(checkItem[j]));
				if (accountNumber != null
						&& roleList.contains(accountNumber.getRole())) {
					isLegalRole = true;
				}

				if (!isLegalRole) {
					addActionError(checkItem[j] + "權限不可變更");
				}

			}
		}

		if (!hasActionErrors()) {
			int j = 0;
			while (j < checkItem.length) {
				accountNumber = accountNumberService.getBySerNo(Long
						.parseLong(checkItem[j]));
				accountNumber.setStatus(Status.不生效);
				accountNumberService.update(accountNumber, getLoginUser());

				j++;
			}

			DataSet<AccountNumber> ds = initDataSet();
			ds.setPager(Pager.getChangedPager(
					getRequest().getParameter("recordPerPage"), getRequest()
							.getParameter("recordPoint"), ds.getPager()));

			ds = accountNumberService.getByRestrictions(ds, getLoginUser());

			setDs(ds);
			addActionMessage("更新成功");
			return LIST;
		} else {
			DataSet<AccountNumber> ds = accountNumberService.getByRestrictions(
					initDataSet(), getLoginUser());
			List<AccountNumber> results = ds.getResults();

			ds.setResults(results);

			setDs(ds);
			return LIST;
		}
	}

	public String authorize() throws Exception {
		if (ArrayUtils.isEmpty(checkItem)) {
			addActionError("請選擇一筆或一筆以上的資料");
		} else {
			int i = 0;
			while (i < checkItem.length) {
				if (!NumberUtils.isDigits(String.valueOf(checkItem[i]))
						|| Long.parseLong(checkItem[i]) < 1
						|| accountNumberService.getBySerNo(Long
								.parseLong(checkItem[i])) == null) {
					addActionError(checkItem[i] + "為不可利用的流水號");
				}
				i++;
			}
		}

		if (!hasActionErrors()) {
			List<Role> roleList = new ArrayList<Role>(Arrays.asList(Role
					.values()));
			List<Role> tempList = new ArrayList<Role>();

			roleList.remove(roleList.size() - 1);

			int roleCode = roleList.indexOf(getLoginUser().getRole());

			for (int i = 0; i < roleCode; i++) {
				tempList.add(roleList.get(i));
			}

			roleList.removeAll(tempList);

			for (int j = 0; j < checkItem.length; j++) {
				boolean isLegalRole = false;
				accountNumber = accountNumberService.getBySerNo(Long
						.parseLong(checkItem[j]));
				if (accountNumber != null
						&& roleList.contains(accountNumber.getRole())) {
					isLegalRole = true;
				}

				if (!isLegalRole) {
					addActionError(checkItem[j] + "權限不可變更");
				}

			}
		}

		if (!hasActionErrors()) {
			int j = 0;
			while (j < checkItem.length) {
				accountNumber = accountNumberService.getBySerNo(Long
						.parseLong(checkItem[j]));
				accountNumber.setStatus(Status.生效);
				accountNumberService.update(accountNumber, getLoginUser());

				j++;
			}

			DataSet<AccountNumber> ds = initDataSet();
			ds.setPager(Pager.getChangedPager(
					getRequest().getParameter("recordPerPage"), getRequest()
							.getParameter("recordPoint"), ds.getPager()));

			ds = accountNumberService.getByRestrictions(ds, getLoginUser());

			setDs(ds);
			addActionMessage("更新成功");
			return LIST;
		} else {
			DataSet<AccountNumber> ds = accountNumberService.getByRestrictions(
					initDataSet(), getLoginUser());
			List<AccountNumber> results = ds.getResults();

			ds.setResults(results);

			setDs(ds);
			return LIST;
		}
	}

	public String view() throws Exception {
		if (hasEntity()) {
			List<Role> roleList = new ArrayList<Role>(Arrays.asList(Role
					.values()));
			List<Role> tempList = new ArrayList<Role>();

			roleList.remove(roleList.size() - 1);

			int roleCode = roleList.indexOf(getLoginUser().getRole());

			for (int i = 0; i < roleCode; i++) {
				tempList.add(roleList.get(i));
			}

			roleList.removeAll(tempList);

			boolean isLegalRole = false;
			if (roleList.contains(accountNumber.getRole())) {
				isLegalRole = true;
			}

			if (isLegalRole) {
				getRequest().setAttribute("viewSerNo", getEntity().getSerNo());
				setEntity(accountNumber);
			} else {
				if (accountNumber != null) {
					getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
				}
			}
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		return VIEW;
	}

	public String imports() {
		return IMPORT;
	}

	public String queue() throws Exception {
		if (ArrayUtils.isEmpty(file) || !file[0].isFile()) {
			addActionError("請選擇檔案");
		} else {
			if (createWorkBook(new FileInputStream(file[0])) == null) {
				addActionError("檔案格式錯誤");
			}
		}

		if (!hasActionErrors()) {
			Workbook book = createWorkBook(new FileInputStream(file[0]));
			// book.getNumberOfSheets(); 判斷Excel文件有多少個sheet
			Sheet sheet = book.getSheetAt(0);

			Row firstRow = sheet.getRow(0);
			if (firstRow == null) {
				firstRow = sheet.createRow(0);
			}

			// 保存列名
			List<String> cellNames = new ArrayList<String>();
			String[] rowTitles = new String[6];
			int n = 0;
			while (n < rowTitles.length) {
				if (firstRow.getCell(n) == null) {
					rowTitles[n] = "";
				} else {
					int typeInt = firstRow.getCell(n).getCellType();
					switch (typeInt) {
					case 0:
						String tempNumeric = "";
						tempNumeric = tempNumeric
								+ firstRow.getCell(n).getNumericCellValue();
						rowTitles[n] = tempNumeric;
						break;

					case 1:
						rowTitles[n] = firstRow.getCell(n).getStringCellValue();
						break;

					case 2:
						rowTitles[n] = firstRow.getCell(n).getCellFormula();
						break;

					case 3:
						rowTitles[n] = "";
						break;

					case 4:
						String tempBoolean = "";
						tempBoolean = ""
								+ firstRow.getCell(n).getBooleanCellValue();
						rowTitles[n] = tempBoolean;
						break;

					case 5:
						String tempByte = "";
						tempByte = tempByte
								+ firstRow.getCell(n).getErrorCellValue();
						rowTitles[n] = tempByte;
						break;
					}

				}

				cellNames.add(rowTitles[n]);
				n++;
			}

			LinkedHashSet<AccountNumber> originalData = new LinkedHashSet<AccountNumber>();
			Map<String, AccountNumber> checkRepeatRow = new LinkedHashMap<String, AccountNumber>();
			int normal = 0;

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				String[] rowValues = new String[6];
				int k = 0;
				while (k < rowValues.length) {
					if (row.getCell(k) == null) {
						rowValues[k] = "";
					} else {
						int typeInt = row.getCell(k).getCellType();
						switch (typeInt) {
						case 0:
							String tempNumeric = "";
							tempNumeric = tempNumeric
									+ row.getCell(k).getNumericCellValue();
							rowValues[k] = tempNumeric;
							break;

						case 1:
							rowValues[k] = row.getCell(k).getStringCellValue();
							break;

						case 2:
							rowValues[k] = row.getCell(k).getCellFormula();
							break;

						case 3:
							rowValues[k] = "";
							break;

						case 4:
							String tempBoolean = "";
							tempBoolean = ""
									+ row.getCell(k).getBooleanCellValue();
							rowValues[k] = tempBoolean;
							break;

						case 5:
							String tempByte = "";
							tempByte = tempByte
									+ row.getCell(k).getErrorCellValue();
							rowValues[k] = tempByte;
							break;
						}

					}
					k++;
				}

				String role = "";
				List<Role> roleList = new ArrayList<Role>(Arrays.asList(Role
						.values()));
				List<Role> tempList = new ArrayList<Role>();
				for (int j = 0; j < roleList.size(); j++) {
					if (rowValues[4].trim().equals(roleList.get(j).getRole())) {
						role = roleList.get(j).getRole();
					}
				}

				if (role.equals("")) {
					role = Role.不明.getRole();
				}

				int roleCode = roleList.indexOf(getLoginUser().getRole());

				for (int j = 0; j < roleCode; j++) {
					tempList.add(roleList.get(j));
				}

				roleList.removeAll(tempList);

				boolean isLegalRole = false;
				roleList.remove(roleList.size() - 1);
				for (int j = 0; j < roleList.size(); j++) {
					if (role.equals(roleList.get(j).getRole())) {
						isLegalRole = true;
					}
				}

				List<Status> statusList = new ArrayList<Status>(
						Arrays.asList(Status.values()));
				String status = "";
				for (int j = 0; j < statusList.size(); j++) {
					if (rowValues[5].trim().equals(
							statusList.get(j).getStatus())) {
						status = statusList.get(j).getStatus();
					}
				}

				if (status.equals("")) {
					status = Status.審核中.getStatus();
				}

				accountNumber = new AccountNumber(rowValues[0], rowValues[1],
						rowValues[2], Role.valueOf(role),
						Status.valueOf(status), null, "");

				if (customerService.getCusSerNoByName(rowValues[3].trim()) != 0) {
					customer = customerService.getBySerNo(customerService
							.getCusSerNoByName(rowValues[3].trim()));
					accountNumber.setCustomer(customer);
				}

				if (StringUtils.isBlank(accountNumber.getUserId())) {
					accountNumber.setExistStatus("帳號空白");
				} else {
					if (accountNumber.getUserId().replaceAll("[0-9a-zA-Z]", "")
							.length() != 0) {
						accountNumber.setExistStatus("帳號必須英數");
					} else {
						long serNo = accountNumberService
								.getSerNoByUserId(accountNumber.getUserId());
						if (serNo != 0) {
							accountNumber.setExistStatus("已存在");
						} else {
							if (getLoginUser().getRole().equals(Role.管理員)) {
								if (StringUtils.isBlank(accountNumber
										.getUserId())
										|| StringUtils.isBlank(accountNumber
												.getUserPw())
										|| accountNumber.getCustomer() == null
										|| !isLegalRole
										|| !getLoginUser().getCustomer()
												.equals(accountNumber
														.getCustomer())) {
									customer = new Customer();
									customer.setName(rowValues[3]);
									accountNumber.setCustomer(customer);
									accountNumber.setExistStatus("資料錯誤");
								}
							} else {
								if (StringUtils.isBlank(accountNumber
										.getUserId())
										|| accountNumber.getUserId()
												.replaceAll("[0-9a-zA-Z]", "")
												.length() != 0
										|| StringUtils.isBlank(accountNumber
												.getUserPw())
										|| accountNumber.getCustomer() == null
										|| !isLegalRole) {
									customer = new Customer();
									customer.setName(rowValues[3]);
									accountNumber.setCustomer(customer);
									accountNumber.setExistStatus("資料錯誤");
								}
							}
						}
					}
				}

				if (StringUtils.isNotEmpty(accountNumber.getEmail())) {
					if (!isEmail(accountNumber.getEmail())) {
						accountNumber.setEmail(null);
					}
				}

				if (accountNumber.getExistStatus().equals("")) {
					accountNumber.setExistStatus("正常");
				}

				if (accountNumber.getExistStatus().equals("正常")
						&& !originalData.contains(accountNumber)) {

					if (checkRepeatRow.containsKey(accountNumber.getUserId())) {
						accountNumber.setExistStatus("帳號重複");

					} else {
						checkRepeatRow.put(accountNumber.getUserId(),
								accountNumber);

						++normal;
					}
				}

				originalData.add(accountNumber);
			}

			List<AccountNumber> excelData = new ArrayList<AccountNumber>(
					originalData);

			DataSet<AccountNumber> ds = initDataSet();
			List<AccountNumber> results = ds.getResults();

			ds.getPager().setTotalRecord((long) excelData.size());
			ds.getPager().setRecordPoint(0);

			if (excelData.size() < ds.getPager().getRecordPerPage()) {
				int i = 0;
				while (i < excelData.size()) {
					results.add(excelData.get(i));
					i++;
				}
			} else {
				int i = 0;
				while (i < ds.getPager().getRecordPerPage()) {
					results.add(excelData.get(i));
					i++;
				}
			}

			getSession().put("cellNames", cellNames);
			getSession().put("importList", excelData);
			getSession().put("total", excelData.size());
			getSession().put("normal", normal);

			setDs(ds);
			return QUEUE;
		} else {
			return IMPORT;
		}
	}

	public String paginate() throws Exception {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return null;
		}

		clearCheckedItem();

		DataSet<AccountNumber> ds = initDataSet();
		ds.setPager(Pager.getChangedPager(
				getRequest().getParameter("recordPerPage"), getRequest()
						.getParameter("recordPoint"), ds.getPager()));
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getRecordPerPage()
				* (ds.getPager().getCurrentPage() - 1);
		int last = first + ds.getPager().getRecordPerPage();

		List<AccountNumber> results = new ArrayList<AccountNumber>();

		int i = 0;
		while (i < importList.size()) {
			if (i >= first && i < last) {
				results.add((AccountNumber) importList.get(i));
			}
			i++;
		}

		ds.setResults(results);
		setDs(ds);
		return QUEUE;
	}

	@SuppressWarnings("unchecked")
	public String getCheckedItem() {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return null;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();
		if (getSession().containsKey("checkItemSet")) {
			checkItemSet = (Set<Integer>) getSession().get("checkItemSet");
		}

		if (ArrayUtils.isNotEmpty(importSerNos)) {
			if (NumberUtils.isDigits(importSerNos[0])) {
				if (!checkItemSet.contains(Integer.parseInt(importSerNos[0]))) {
					if (((AccountNumber) importList.get(Integer
							.parseInt(importSerNos[0]))).getExistStatus()
							.equals("正常")) {
						checkItemSet.add(Integer.parseInt(importSerNos[0]));

					}
				} else {
					checkItemSet.remove(Integer.parseInt(importSerNos[0]));
				}
			}
		}

		getSession().put("checkItemSet", checkItemSet);

		return null;
	}

	public String allCheckedItem() {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return null;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();

		if (ArrayUtils.isNotEmpty(importSerNos)) {
			int i = 0;
			while (i < importSerNos.length) {
				if (NumberUtils.isDigits(importSerNos[i])) {
					if (Long.parseLong(importSerNos[i]) < importList.size()) {
						if (((AccountNumber) importList.get(Integer
								.parseInt(importSerNos[i]))).getExistStatus()
								.equals("正常")) {
							checkItemSet.add(Integer.parseInt(importSerNos[i]));
						}
					}

					if (checkItemSet.size() == importList.size()) {
						break;
					}
				}
				i++;
			}
		}

		getSession().put("checkItemSet", checkItemSet);
		return null;
	}

	public String clearCheckedItem() {
		if (getSession().get("importList") == null) {
			return null;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();
		getSession().put("checkItemSet", checkItemSet);
		return null;
	}

	public String importData() throws Exception {
		List<?> importList = (List<?>) getSession().get("importList");

		if (importList == null) {
			return null;
		}

		Set<?> checkItemSet = (Set<?>) getSession().get("checkItemSet");

		if (CollectionUtils.isEmpty(checkItemSet)) {
			addActionError("請選擇一筆或一筆以上的資料");
		}

		if (!hasActionErrors()) {
			Iterator<?> iterator = checkItemSet.iterator();
			int successCount = 0;
			while (iterator.hasNext()) {
				int index = (Integer) iterator.next();
				accountNumber = (AccountNumber) importList.get(index);
				accountNumberService.save(accountNumber, getLoginUser());
				++successCount;
			}

			getRequest().setAttribute("successCount", successCount);
			return VIEW;
		} else {
			paginate();
			return QUEUE;
		}
	}

	public String example() throws Exception {
		getEntity().setReportFile("account_sample.xlsx");

		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("account");
		// Create row object
		XSSFRow row;
		// This data needs to be written (Object[])
		Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
		empinfo.put("1", new Object[] { "userID/使用者", "userPW/使用者密碼",
				"userName/姓名", "fk_name/用戶名稱", "role/角色", "status/狀態" });

		empinfo.put("2", new Object[] { "cdc", "cdc", "XXX", "疾病管制局", "管理員",
				"生效" });

		// Iterate over data and write to sheet
		Set<String> keyid = empinfo.keySet();
		int rowid = 0;
		for (String key : keyid) {
			row = spreadsheet.createRow(rowid++);
			Object[] objectArr = empinfo.get(key);
			int cellid = 0;
			for (Object obj : objectArr) {
				Cell cell = row.createCell(cellid++);
				cell.setCellValue((String) obj);
			}
		}

		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		workbook.write(boas);
		setInputStream(new ByteArrayInputStream(boas.toByteArray()));

		return XLSX;
	}

	public boolean hasEntity() throws Exception {
		if (getEntity().getSerNo() == null) {
			getEntity().setSerNo(-1L);
			return false;
		}

		accountNumber = accountNumberService.getBySerNo(getEntity().getSerNo());
		if (accountNumber == null) {
			return false;
		}

		return true;
	}

	// 判斷文件類型
	public Workbook createWorkBook(InputStream is) throws IOException {
		try {
			if (fileFileName[0].toLowerCase().endsWith("xls")) {
				return new HSSFWorkbook(is);
			}

			if (fileFileName[0].toLowerCase().endsWith("xlsx")) {
				return new XSSFWorkbook(is);
			}
		} catch (InvalidOperationException e) {
			return null;
		}

		return null;
	}

	public boolean isEmail(String email) {
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		return Pattern.compile(emailPattern).matcher(email).matches();
	}

	/**
	 * @return the dsCustomer
	 */
	public DataSet<Customer> getDsCustomer() {
		return dsCustomer;
	}

	/**
	 * @param dsCustomer
	 *            the dsCustomer to set
	 */
	public void setDsCustomer(DataSet<Customer> dsCustomer) {
		this.dsCustomer = dsCustomer;
	}

	/**
	 * @return the checkItem
	 */
	public String[] getCheckItem() {
		return checkItem;
	}

	/**
	 * @param checkItem
	 *            the checkItem to set
	 */
	public void setCheckItem(String[] checkItem) {
		this.checkItem = checkItem;
	}

	/**
	 * @return the file
	 */
	public File[] getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File[] file) {
		this.file = file;
	}

	/**
	 * @return the fileFileName
	 */
	public String[] getFileFileName() {
		return fileFileName;
	}

	/**
	 * @param fileFileName
	 *            the fileFileName to set
	 */
	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}

	/**
	 * @return the fileContentType
	 */
	public String[] getFileContentType() {
		return fileContentType;
	}

	/**
	 * @param fileContentType
	 *            the fileContentType to set
	 */
	public void setFileContentType(String[] fileContentType) {
		this.fileContentType = fileContentType;
	}

	/**
	 * @return the importSerNos
	 */
	public String[] getImportSerNos() {
		return importSerNos;
	}

	/**
	 * @param importSerNos
	 *            the importSerNos to set
	 */
	public void setImportSerNos(String[] importSerNos) {
		this.importSerNos = importSerNos;
	}
}
