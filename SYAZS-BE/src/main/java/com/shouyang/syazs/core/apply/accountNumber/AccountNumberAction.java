package com.shouyang.syazs.core.apply.accountNumber;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.customer.CustomerService;
import com.shouyang.syazs.core.apply.enums.Role;
import com.shouyang.syazs.core.apply.enums.Status;
import com.shouyang.syazs.core.converter.EnumConverter;
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

	@Autowired
	private EnumConverter enumConverter;

	@Override
	protected void validateSave() throws Exception {
		List<Role> roleList = getLegalRoles();

		if (getEntity().getRole() == null
				|| !roleList.contains(getEntity().getRole())) {
			errorMessages.add("角色錯誤");
		}

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

		if (getLoginUser().getRole().equals(Role.管理員)) {
			getEntity().setCustomer(getLoginUser().getCustomer());
		} else {
			if (!getEntity().getCustomer().hasSerNo()
					|| getEntity().getCustomer().getSerNo() <= 0
					|| customerService.getBySerNo(getEntity().getCustomer()
							.getSerNo()) == null) {
				errorMessages.add("用戶名稱必選");
			}
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
			List<Role> roleList = getLegalRoles();

			if (!roleList.contains(accountNumberService.getBySerNo(
					getEntity().getSerNo()).getRole())) {
				errorMessages.add("權限不足");
				getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
			}

			if (errorMessages.size() == 0) {
				if (getEntity().getRole() == null
						|| !roleList.contains(getEntity().getRole())) {
					errorMessages.add("角色錯誤");
				}

				if (getEntity().getStatus() == null) {
					errorMessages.add("狀態錯誤");
				}

				if (StringUtils.isBlank(getEntity().getUserName())) {
					errorMessages.add("用戶姓名不得空白");
				}

				if (getLoginUser().getRole().equals(Role.管理員)) {
					getEntity().setCustomer(getLoginUser().getCustomer());
				} else {
					if (!getEntity().getCustomer().hasSerNo()
							|| getEntity().getCustomer().getSerNo() <= 0
							|| customerService.getBySerNo(getEntity()
									.getCustomer().getSerNo()) == null) {
						errorMessages.add("用戶名稱必選");
					}
				}
			}

			if (!isEmail(getEntity().getEmail())) {
				errorMessages.add("email格式不正確");
			}
		}
	}

	@Override
	protected void validateDelete() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String add() throws Exception {
		getLegalRoles();

		DataSet<AccountNumber> ds = initDataSet();
		if (getLoginUser().getRole().equals(Role.管理員)) {
			ds.getDatas().put(getLoginUser().getCustomer().getName(),
					getLoginUser().getCustomer().getSerNo());
		} else {
			ds.setDatas(customerService.getCusDatas());
		}

		setDs(ds);
		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
			List<Role> roleList = getLegalRoles();

			boolean isLegalRole = false;
			if (roleList.contains(accountNumber.getRole())) {
				isLegalRole = true;
			}

			if (isLegalRole) {
				if (getLoginUser().getRole().equals(Role.管理員)) {
					if (accountNumber.getCustomer().equals(
							getLoginUser().getCustomer())) {
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

		DataSet<AccountNumber> ds = initDataSet();
		if (getLoginUser().getRole().equals(Role.管理員)) {
			ds.getDatas().put(getLoginUser().getCustomer().getName(),
					getLoginUser().getCustomer().getSerNo());
		} else {
			ds.setDatas(customerService.getCusDatas());
		}

		setDs(ds);
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		DataSet<AccountNumber> ds = accountNumberService.getByRestrictions(
				initDataSet(), getLoginUser());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			ds = accountNumberService.getByRestrictions(ds, getLoginUser());
		}

		setDs(ds);
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
			if (getLoginUser().getRole().equals(Role.管理員)) {
				ds.getDatas().put(getLoginUser().getCustomer().getName(),
						getLoginUser().getCustomer().getSerNo());
			} else {
				ds.setDatas(customerService.getCusDatas());
			}

			setDs(ds);
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
				accountNumberService.updateLog(getEntity());
				accountNumber = accountNumberService.update(getEntity(),
						getLoginUser(), "userId", "userPw");
			} else {
				accountNumberService.updateLog(getEntity());
				accountNumber = accountNumberService.update(getEntity(),
						getLoginUser(), "userId");
			}

			accountNumber = accountNumberService.getBySerNo(accountNumber
					.getSerNo());
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

			setDs(ds);
			return EDIT;
		}
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String deauthorize() throws Exception {
		if (ArrayUtils.isEmpty(getEntity().getCheckItem())) {
			addActionError("請選擇一筆或一筆以上的資料");
		} else {
			Set<Long> deRepeatSet = new HashSet<Long>(Arrays.asList(getEntity()
					.getCheckItem()));
			getEntity().setCheckItem(
					deRepeatSet.toArray(new Long[deRepeatSet.size()]));

			int i = 0;
			while (i < getEntity().getCheckItem().length) {
				if (getEntity().getCheckItem()[i] == null
						|| getEntity().getCheckItem()[i] < 1
						|| accountNumberService.getBySerNo(getEntity()
								.getCheckItem()[i]) == null) {
					addActionError("有錯誤流水號");
					break;
				}
				i++;
			}
		}

		if (!hasActionErrors()) {
			List<Role> roleList = getLegalRoles();

			for (int j = 0; j < getEntity().getCheckItem().length; j++) {
				boolean isLegalRole = false;
				accountNumber = accountNumberService.getBySerNo(getEntity()
						.getCheckItem()[j]);
				if (roleList.contains(accountNumber.getRole())) {
					isLegalRole = true;
				}

				if (!isLegalRole) {
					addActionError(getEntity().getCheckItem()[j] + "權限不可變更");
				}

			}
		}

		if (!hasActionErrors()) {
			int j = 0;
			while (j < getEntity().getCheckItem().length) {
				accountNumber = accountNumberService.getBySerNo(getEntity()
						.getCheckItem()[j]);
				if (!accountNumber.getStatus().equals(Status.不生效)) {
					accountNumber.setStatus(Status.不生效);
					accountNumberService.update(accountNumber, getLoginUser());
				}
				j++;
			}

			list();
			addActionMessage("更新成功");
			return LIST;
		} else {
			list();
			return LIST;
		}
	}

	public String authorize() throws Exception {
		if (ArrayUtils.isEmpty(getEntity().getCheckItem())) {
			addActionError("請選擇一筆或一筆以上的資料");
		} else {
			Set<Long> deRepeatSet = new HashSet<Long>(Arrays.asList(getEntity()
					.getCheckItem()));
			getEntity().setCheckItem(
					deRepeatSet.toArray(new Long[deRepeatSet.size()]));

			int i = 0;
			while (i < getEntity().getCheckItem().length) {
				if (getEntity().getCheckItem()[i] == null
						|| getEntity().getCheckItem()[i] < 1
						|| accountNumberService.getBySerNo(getEntity()
								.getCheckItem()[i]) == null) {
					addActionError("有錯誤流水號");
					break;
				}
				i++;
			}
		}

		if (!hasActionErrors()) {
			List<Role> roleList = getLegalRoles();

			for (int j = 0; j < getEntity().getCheckItem().length; j++) {
				boolean isLegalRole = false;
				accountNumber = accountNumberService.getBySerNo(getEntity()
						.getCheckItem()[j]);
				if (roleList.contains(accountNumber.getRole())) {
					isLegalRole = true;
				}

				if (!isLegalRole) {
					addActionError(getEntity().getCheckItem()[j] + "權限不可變更");
				}

			}
		}

		if (!hasActionErrors()) {
			int j = 0;
			while (j < getEntity().getCheckItem().length) {
				accountNumber = accountNumberService.getBySerNo(getEntity()
						.getCheckItem()[j]);
				if (!accountNumber.getStatus().equals(Status.生效)) {
					accountNumber.setStatus(Status.生效);
					accountNumberService.update(accountNumber, getLoginUser());
				}
				j++;
			}

			list();
			addActionMessage("更新成功");
			return LIST;
		} else {
			list();
			return LIST;
		}
	}

	public String view() throws Exception {
		if (hasEntity()) {
			List<Role> roleList = getLegalRoles();

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
		if (ArrayUtils.isEmpty(getEntity().getFile())
				|| !getEntity().getFile()[0].isFile()) {
			addActionError("請選擇檔案");
		} else {
			if (createWorkBook(new FileInputStream(getEntity().getFile()[0])) == null) {
				addActionError("檔案格式錯誤");
			}
		}

		if (!hasActionErrors()) {
			Workbook book = createWorkBook(new FileInputStream(getEntity()
					.getFile()[0]));
			Sheet sheet = book.createSheet();
			if (book.getNumberOfSheets() != 0) {
				sheet = book.getSheetAt(0);
			}

			Row firstRow = sheet.getRow(0);
			if (firstRow == null) {
				firstRow = sheet.createRow(0);
			}

			// 保存列名
			List<String> cellNames = new ArrayList<String>();
			String[] rowTitles = new String[7];
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

			List<Role> roleList = getLegalRoles();
			LinkedHashSet<AccountNumber> originalData = new LinkedHashSet<AccountNumber>();
			Map<String, AccountNumber> checkRepeatRow = new LinkedHashMap<String, AccountNumber>();
			int normal = 0;

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				String[] rowValues = new String[7];
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
				boolean isLegalRole = false;
				if (StringUtils.isBlank(rowValues[5])) {
					role = Role.不明.getRole();
				} else {
					Object object = getEnum(
							new String[] { rowValues[5].trim() }, Role.class);
					if (object != null
							&& roleList.contains(Role.valueOf(rowValues[5]
									.trim()))) {
						role = rowValues[5].trim();
						isLegalRole = true;
					} else {
						role = Role.不明.getRole();
					}
				}

				String status = "";
				if (StringUtils.isBlank(rowValues[6])) {
					status = Status.審核中.getStatus();
				} else {
					Object object = getEnum(
							new String[] { rowValues[6].trim() }, Status.class);
					if (object != null) {
						status = rowValues[6].trim();
					} else {
						status = Status.審核中.getStatus();
					}
				}

				customer = new Customer();
				customer.setName(rowValues[3].trim());
				accountNumber = new AccountNumber(rowValues[0], rowValues[1],
						rowValues[2], rowValues[4], Role.valueOf(role),
						Status.valueOf(status), customer);

				if (StringUtils.isBlank(accountNumber.getUserId())) {
					accountNumber.setDataStatus("帳號空白");
				} else {
					if (accountNumber.getUserId().replaceAll("[0-9a-zA-Z]", "")
							.length() != 0) {
						accountNumber.setDataStatus("帳號必須英數");
					} else {
						long serNo = accountNumberService
								.getSerNoByUserId(accountNumber.getUserId());
						if (serNo != 0) {
							accountNumber.setDataStatus("已存在");
						} else {
							long cusSerNo = customerService
									.getCusSerNoByName(customer.getName());
							if (cusSerNo != 0) {
								accountNumber.getCustomer().setSerNo(cusSerNo);
							}

							if (getLoginUser().getRole().equals(Role.管理員)) {
								if (StringUtils.isBlank(accountNumber
										.getUserId())
										|| accountNumber.getUserId()
												.replaceAll("[0-9a-zA-Z]", "")
												.length() != 0
										|| StringUtils.isBlank(accountNumber
												.getUserPw())
										|| !isLegalRole
										|| !getLoginUser()
												.getCustomer()
												.getName()
												.equals(accountNumber
														.getCustomer()
														.getName())) {
									accountNumber.setDataStatus("資料錯誤");
								}
							} else {
								if (StringUtils.isBlank(accountNumber
										.getUserId())
										|| accountNumber.getUserId()
												.replaceAll("[0-9a-zA-Z]", "")
												.length() != 0
										|| StringUtils.isBlank(accountNumber
												.getUserPw())
										|| !accountNumber.getCustomer()
												.hasSerNo() || !isLegalRole) {
									accountNumber.setDataStatus("資料錯誤");
								}
							}
						}
					}
				}

				if (!isEmail(accountNumber.getEmail())) {
					accountNumber.setEmail(null);
				}

				if (accountNumber.getDataStatus() == null) {
					accountNumber.setDataStatus("正常");
				}

				if (accountNumber.getDataStatus().equals("正常")
						&& !originalData.contains(accountNumber)) {

					if (checkRepeatRow.containsKey(accountNumber.getUserId())) {
						accountNumber.setDataStatus("帳號重複");

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
			ds.getPager().setTotalRecord((long) excelData.size());

			if (excelData.size() < ds.getPager().getRecordPerPage()) {
				int i = 0;
				while (i < excelData.size()) {
					ds.getResults().add(excelData.get(i));
					i++;
				}
			} else {
				int i = 0;
				while (i < ds.getPager().getRecordPerPage()) {
					ds.getResults().add(excelData.get(i));
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
			return IMPORT;
		}

		clearCheckedItem();

		DataSet<AccountNumber> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int i = 0;
		while (i < importList.size()) {
			if (i >= first && i < last) {
				ds.getResults().add((AccountNumber) importList.get(i));
			}
			i++;
		}

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			first = ds.getPager().getOffset();
			last = first + ds.getPager().getRecordPerPage();

			int j = 0;
			while (j < importList.size()) {
				if (j >= first && j < last) {
					ds.getResults().add((AccountNumber) importList.get(j));
				}
				j++;
			}

		}

		setDs(ds);
		return QUEUE;
	}

	@SuppressWarnings("unchecked")
	public String getCheckedItem() {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return IMPORT;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();
		if (getSession().containsKey("checkItemSet")) {
			checkItemSet = (Set<Integer>) getSession().get("checkItemSet");
		}

		if (ArrayUtils.isNotEmpty(getEntity().getImportItem())) {
			if (getEntity().getImportItem()[0] != null
					&& getEntity().getImportItem()[0] >= 0
					&& getEntity().getImportItem()[0] < importList.size()) {
				if (!checkItemSet.contains(getEntity().getImportItem()[0])) {
					if (((AccountNumber) importList.get(getEntity()
							.getImportItem()[0])).getDataStatus().equals("正常")) {
						checkItemSet.add(getEntity().getImportItem()[0]);
					}
				} else {
					checkItemSet.remove(getEntity().getImportItem()[0]);
				}
			}
		}

		getSession().put("checkItemSet", checkItemSet);
		return QUEUE;
	}

	public String allCheckedItem() {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return IMPORT;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();

		if (ArrayUtils.isNotEmpty(getEntity().getImportItem())) {
			Set<Integer> deRepeatSet = new HashSet<Integer>(
					Arrays.asList(getEntity().getImportItem()));
			getEntity().setImportItem(
					deRepeatSet.toArray(new Integer[deRepeatSet.size()]));

			int i = 0;
			while (i < getEntity().getImportItem().length) {
				if (getEntity().getImportItem()[i] != null
						&& getEntity().getImportItem()[i] >= 0
						&& getEntity().getImportItem()[i] < importList.size()) {
					if (((AccountNumber) importList.get(getEntity()
							.getImportItem()[i])).getDataStatus().equals("正常")) {
						checkItemSet.add(getEntity().getImportItem()[i]);
					}

					if (checkItemSet.size() == importList.size()) {
						break;
					}
				}
				i++;
			}
		}

		getSession().put("checkItemSet", checkItemSet);
		return QUEUE;
	}

	public String clearCheckedItem() {
		if (getSession().get("importList") == null) {
			return IMPORT;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();
		getSession().put("checkItemSet", checkItemSet);
		return null;
	}

	public String importData() throws Exception {
		List<?> importList = (List<?>) getSession().get("importList");

		if (importList == null) {
			return IMPORT;
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
				accountNumber.setDataStatus("已存在");
				++successCount;
			}

			getRequest().setAttribute("successCount", successCount);
			int normal = (int) getSession().get("normal");
			getSession().put("normal", normal - successCount);
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
		empinfo.put("1",
				new Object[] { "userID/使用者", "userPW/使用者密碼", "userName/姓名",
						"fk_name/用戶名稱", "email", "role/角色", "status/狀態" });

		empinfo.put("2", new Object[] { "cdc", "cdc", "XXX", "疾病管制局",
				"cdc@cdc.org", "管理員", "生效" });

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
		getEntity()
				.setInputStream(new ByteArrayInputStream(boas.toByteArray()));

		return XLSX;
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

	// 判斷文件類型
	protected Workbook createWorkBook(InputStream is) throws IOException {
		try {
			if (getEntity().getFileFileName()[0].toLowerCase().endsWith("xls")) {
				return new HSSFWorkbook(is);
			}

			if (getEntity().getFileFileName()[0].toLowerCase().endsWith("xlsx")) {
				return new XSSFWorkbook(is);
			}
		} catch (InvalidOperationException e) {
			return null;
		}

		return null;
	}

	protected boolean isEmail(String email) {
		return ESAPI.validator().isValidInput("account Email", email, "Email",
				254, true);
	}

	@SuppressWarnings("rawtypes")
	protected Object getEnum(String[] values, Class toClass) {
		return enumConverter.convertFromString(null, values, toClass);
	}

	protected List<Role> getLegalRoles() {
		List<Role> roleList = new ArrayList<Role>(Arrays.asList(Role.values()));
		List<Role> tempList = new ArrayList<Role>();
		roleList.remove(roleList.size() - 1);

		int roleCode = roleList.indexOf(getLoginUser().getRole());

		for (int i = 0; i < roleCode; i++) {
			tempList.add(roleList.get(i));
		}

		roleList.removeAll(tempList);
		ActionContext.getContext().getValueStack().set("roleList", roleList);

		return roleList;
	}
}
