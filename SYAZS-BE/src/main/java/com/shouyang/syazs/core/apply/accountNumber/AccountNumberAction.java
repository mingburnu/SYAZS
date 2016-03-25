package com.shouyang.syazs.core.apply.accountNumber;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.collect.Lists;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opensymphony.xwork2.ActionContext;
import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.customer.CustomerService;
import com.shouyang.syazs.core.apply.enums.Role;
import com.shouyang.syazs.core.apply.enums.Status;
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
					if (!hasCustomer()) {
						errorMessages.add("用戶名稱必選");
					} else {
						getEntity().setCustomer(customer);
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
			Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
					.doubleValue()
					/ ds.getPager().getRecordPerPage().doubleValue());
			ds.getPager().setCurrentPage(lastPage.intValue());
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

	@SuppressWarnings("resource")
	public String queue() throws Exception {
		if (ArrayUtils.isEmpty(getEntity().getFile())
				|| !getEntity().getFile()[0].isFile()) {
			addActionError("請選擇檔案");
		} else {
			if (getEntity().getFile()[0].length() > 10485760) {
				addActionError("檔案超過10MB，請分批");
			} else {
				if (!getFileMime(getEntity().getFile()[0],
						getEntity().getFileFileName()[0]).equals("text/csv")) {
					addActionError("檔案格式錯誤");
				}
			}
		}

		if (!hasActionErrors()) {// TODO
			List<String> cellNames = new ArrayList<String>();
			List<String> errorList = Lists.newArrayList();

			LinkedHashSet<AccountNumber> originalData = new LinkedHashSet<AccountNumber>();
			Map<String, AccountNumber> checkRepeatRow = new LinkedHashMap<String, AccountNumber>();
			int normal = 0;

			CSVReader reader = new CSVReader(new FileReader(getEntity()
					.getFile()[0]), ',');

			String[] row;
			int rowLength = 7;
			List<Role> roleList = getLegalRoles();

			while ((row = reader.readNext()) != null) {
				if (row.length < rowLength) {
					String[] spaceArray = new String[rowLength - row.length];
					row = ArrayUtils.addAll(row, spaceArray);
				}

				if (reader.getRecordsRead() == 1) {
					cellNames = Arrays.asList(row);

				} else {
					String role = "";
					boolean isLegalRole = false;
					if (StringUtils.isBlank(row[5])) {
						role = Role.不明.getRole();
					} else {
						Object object = toEnum(row[5].trim(), Role.class);
						if (object != null
								&& roleList
										.contains(Role.valueOf(row[5].trim()))) {
							role = row[5].trim();
							isLegalRole = true;
						} else {
							role = Role.不明.getRole();
						}
					}

					String status = "";
					if (StringUtils.isBlank(row[6])) {
						status = Status.審核中.getStatus();
					} else {
						Object object = toEnum(row[6].trim(), Status.class);
						if (object != null) {
							status = row[6].trim();
						} else {
							status = Status.審核中.getStatus();
						}
					}

					customer = new Customer();
					customer.setName(row[3].trim());
					accountNumber = new AccountNumber(row[0], row[1], row[2],
							row[4], Role.valueOf(role), Status.valueOf(status),
							customer);

					if (StringUtils.isBlank(accountNumber.getUserId())) {
						errorList.add("帳號空白");
					} else {
						if (accountNumber.getUserId()
								.replaceAll("[0-9a-zA-Z]", "").length() != 0) {
							errorList.add("帳號必須英數");
						} else {
							long serNo = accountNumberService
									.getSerNoByUserId(accountNumber.getUserId());
							if (serNo != 0) {
								errorList.add("帳號已存在");
							}
						}
					}

					if (StringUtils.isBlank(accountNumber.getUserPw())) {
						errorList.add("密碼空白");
					}

					if (StringUtils.isBlank(customer.getName())) {
						errorList.add("沒有客戶");
					} else {
						long cusSerNo = customerService
								.getCusSerNoByName(customer.getName());
						if (cusSerNo != 0) {
							accountNumber.getCustomer().setSerNo(cusSerNo);
						} else {
							errorList.add("無此客戶");
						}
					}

					if (getLoginUser().getRole().equals(Role.管理員)) {
						if (!getLoginUser().getCustomer().getName()
								.equals(accountNumber.getCustomer().getName())) {
							errorList.add("客戶錯誤");
						}
					} else {
						if (!accountNumber.getCustomer().hasSerNo()) {
							errorList.add("客戶錯誤");
						}
					}

					if (!isLegalRole) {
						errorList.add("角色錯誤");
					}

					if (!isEmail(accountNumber.getEmail())) {
						errorList.add("email錯誤");
					}

					if (errorList.size() != 0) {
						accountNumber.setDataStatus(errorList.toString()
								.replace("[", "").replace("]", ""));
					} else {
						accountNumber.setDataStatus("正常");
					}

					if (accountNumber.getDataStatus().equals("正常")
							&& !originalData.contains(accountNumber)) {

						if (checkRepeatRow.containsKey(accountNumber
								.getUserId())) {
							accountNumber.setDataStatus("帳號重複");

						} else {
							checkRepeatRow.put(accountNumber.getUserId(),
									accountNumber);

							++normal;
						}
					}

					originalData.add(accountNumber);
				}
			}

			List<AccountNumber> csvData = new ArrayList<AccountNumber>(
					originalData);

			DataSet<AccountNumber> ds = initDataSet();
			ds.getPager().setTotalRecord((long) csvData.size());

			if (csvData.size() < ds.getPager().getRecordPerPage()) {
				int i = 0;
				while (i < csvData.size()) {
					ds.getResults().add(csvData.get(i));
					i++;
				}
			} else {
				int i = 0;
				while (i < ds.getPager().getRecordPerPage()) {
					ds.getResults().add(csvData.get(i));
					i++;
				}
			}

			getSession().put("cellNames", cellNames);
			getSession().put("importList", csvData);
			getSession().put("total", csvData.size());
			getSession().put("normal", normal);
			getSession().put("insert", 0);
			getSession().put("clazz", this.getClass());

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

		DataSet<AccountNumber> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int index = first;
		while (index >= first && index < last) {
			if (index < importList.size()) {
				ds.getResults().add((AccountNumber) importList.get(index));
			} else {
				break;
			}
			index++;
		}

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
					.doubleValue()
					/ ds.getPager().getRecordPerPage().doubleValue());
			ds.getPager().setCurrentPage(lastPage.intValue());
			first = ds.getPager().getOffset();
			last = first + ds.getPager().getRecordPerPage();

			index = first;
			while (index >= first && index < last) {
				if (index < importList.size()) {
					ds.getResults().add((AccountNumber) importList.get(index));
				} else {
					break;
				}
				index++;
			}
		}

		setDs(ds);
		return QUEUE;
	}

	@SuppressWarnings("unchecked")
	public String addAllItem() {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return IMPORT;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();
		if (getSession().containsKey("checkItemSet")) {
			checkItemSet = (Set<Integer>) getSession().get("checkItemSet");
		}

		Integer i = 0;
		while (i < importList.size()) {
			accountNumber = (AccountNumber) importList.get(i);
			if (accountNumber.getDataStatus().equals("正常")) {
				checkItemSet.add(i);
			}
			i++;
		}

		getSession().put("allChecked", true);
		getSession().put("checkItemSet", checkItemSet);
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

	public String allUncheckedItem() {
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
					checkItemSet.remove(getEntity().getImportItem()[i]);

					if (checkItemSet.size() == 0) {
						break;
					}
				}
				i++;
			}
		}

		getSession().put("checkItemSet", checkItemSet);
		return QUEUE;
	}

	public String removeAllItem() {
		if (getSession().get("importList") == null) {
			return IMPORT;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();
		getSession().put("allChecked", false);
		getSession().put("checkItemSet", checkItemSet);
		return QUEUE;
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
				accountNumber.setDataStatus("已匯入");
				++successCount;
			}

			getRequest().setAttribute("successCount", successCount);
			int insert = (int) getSession().get("insert");
			getSession().put("insert", insert + successCount);
			getSession().remove("checkItemSet");
			getSession().remove("allChecked");
			return VIEW;
		} else {
			paginate();
			return QUEUE;
		}
	}

	public String backErrors() throws IOException {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return IMPORT;
		}

		List<String[]> rows = new ArrayList<String[]>();
		rows.add(new String[] { "userID/使用者", "userPW/使用者密碼", "userName/姓名",
				"fk_name/用戶名稱", "email", "role/角色", "status/狀態", "錯誤原因" });

		int i = 0;
		while (i < importList.size()) {
			accountNumber = (AccountNumber) importList.get(i);
			if (!accountNumber.getDataStatus().equals("正常")
					&& !accountNumber.getDataStatus().equals("已匯入")) {
				rows.add(new String[] { accountNumber.getUserId(),
						accountNumber.getUserPw(), accountNumber.getUserName(),
						accountNumber.getCustomer().getName(),
						accountNumber.getEmail(),
						accountNumber.getRole().getRole(),
						accountNumber.getStatus().getStatus(),
						accountNumber.getDataStatus() });
			}
			i++;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos), ',',
				CSVWriter.DEFAULT_QUOTE_CHARACTER,
				CSVWriter.NO_ESCAPE_CHARACTER, "\n");

		writer.writeAll(rows);
		writer.close();

		getEntity().setReportFile("account_error.csv");
		getEntity()
				.setInputStream(new ByteArrayInputStream(baos.toByteArray()));

		return XLSX;
	}

	public String example() throws Exception {
		List<String[]> rows = new ArrayList<String[]>();
		rows.add(new String[] { "userID/使用者", "userPW/使用者密碼", "userName/姓名",
				"fk_name/用戶名稱", "email", "role/角色", "status/狀態" });
		rows.add(new String[] { "cdc", "cdc", "XXX", "疾病管制局", "cdc@cdc.org",
				"管理員", "生效" });

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos), ',',
				CSVWriter.DEFAULT_QUOTE_CHARACTER,
				CSVWriter.NO_ESCAPE_CHARACTER, "\n");

		writer.writeAll(rows);
		writer.close();

		getEntity().setReportFile("account_sample.csv");
		getEntity()
				.setInputStream(new ByteArrayInputStream(baos.toByteArray()));

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
			return WorkbookFactory.create(is);
		} catch (InvalidFormatException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
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

		int roleCode = roleList.indexOf(getLoginUser().getRole());

		for (int i = 0; i < roleCode; i++) {
			tempList.add(roleList.get(i));
		}

		roleList.removeAll(tempList);
		ActionContext.getContext().getValueStack().set("roleList", roleList);

		return roleList;
	}
}
