package com.shouyang.syazs.core.apply.customer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
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

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.collect.Lists;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
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
		} else if (getEntity().getSerNo() == 9) {
			if (!getLoginUser().getRole().equals(Role.系統管理員)) {
				errorMessages.add("權限不足");
			}
		} else {
			if (StringUtils.isNotEmpty(getEntity().getTel())) {
				String tel = getEntity().getTel().replaceAll("[/()+-]", "")
						.replace(" ", "");
				if (!NumberUtils.isDigits(tel)) {
					errorMessages.add("電話格式不正確");
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
		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			customer = customerService.save(getEntity(), getLoginUser());

			setEntity(customer);

			addActionMessage("新增成功");
			return VIEW;
		} else {
			return ADD;
		}
	}

	@Override
	public String update() throws Exception {
		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
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
			}

			return EDIT;
		}
	}

	@Override
	public String delete() throws Exception {
		validateDelete();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			int i = 0;
			while (i < getEntity().getCheckItem().length) {
				String name = customerService.getBySerNo(
						getEntity().getCheckItem()[i]).getName();
				customerService.deleteBySerNo(getEntity().getCheckItem()[i]);
				addActionMessage(name + "刪除成功");
				i++;
			}

			list();
			return LIST;
		} else {
			list();
			return LIST;
		}
	}

	public String view() throws Exception {
		if (hasEntity()) {
			getRequest().setAttribute("viewSerNo", getEntity().getSerNo());
			setEntity(customer);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		return VIEW;
	}

	public String box() throws Exception {
		getRequest().setAttribute("allCustomers",
				customerService.getAllCustomers());

		return BOX;
	}

	public String json() throws Exception {
		List<Customer> customers = customerService.getAllCustomers();
		List<JSONObject> objArray = new ArrayList<JSONObject>();

		int i = 0;
		while (i < customers.size()) {
			JSONObject obj = new JSONObject();
			customer = customers.get(i);

			obj.put("name", customer.getName());
			obj.put("value", customer.getSerNo());
			objArray.add(obj);
			i++;
		}

		getRequest().setAttribute("jsonString", objArray.toString());
		return JSON;
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
			if (getEntity().getFile()[0].length() > 10 * 1024 * 1024) {
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

			LinkedHashSet<Customer> originalData = new LinkedHashSet<Customer>();
			Map<String, Customer> checkRepeatRow = new LinkedHashMap<String, Customer>();
			int normal = 0;

			CSVReader reader = new CSVReader(new FileReader(getEntity()
					.getFile()[0]), ',');

			String[] row;
			int rowLength = 6;
			while ((row = reader.readNext()) != null) {
				if (row.length < rowLength) {
					String[] spaceArray = new String[rowLength - row.length];
					row = ArrayUtils.addAll(row, spaceArray);
				}

				if (reader.getRecordsRead() == 1) {
					cellNames = Arrays.asList(row);
				} else {
					customer = new Customer(row[0], row[1], row[2], row[4],
							row[3], row[5]);

					if (StringUtils.isBlank(customer.getName())) {
						errorList.add("名稱空白");
					} else {
						if (customer.getName()
								.replaceAll("[a-zA-Z0-9\u4e00-\u9fa5]", "")
								.length() != 0) {
							errorList.add("名稱字元異常");
						} else {
							long cusSerNo = customerService
									.getCusSerNoByName(customer.getName());
							if (cusSerNo != 0) {
								errorList.add("已存在");
							}

							if (StringUtils.isNotEmpty(customer.getTel())) {
								String tel = customer.getTel()
										.replaceAll("[/()+-]", "")
										.replace(" ", "");
								if (!NumberUtils.isDigits(tel)) {
									errorList.add("電話異常");
								}
							}

							if (errorList.size() != 0) {
								customer.setDataStatus(errorList.toString()
										.replace("[", "").replace("]", ""));
							} else {
								customer.setDataStatus("正常");
							}

							if (customer.getDataStatus().equals("正常")
									&& !originalData.contains(customer)) {

								if (checkRepeatRow.containsKey(customer
										.getName())) {
									customer.setDataStatus("名稱重複");

								} else {
									checkRepeatRow.put(customer.getName(),
											customer);

									++normal;
								}
							}

							originalData.add(customer);
						}
					}

				}
			}

			List<Customer> csvData = new ArrayList<Customer>(originalData);

			DataSet<Customer> ds = initDataSet();
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

		DataSet<Customer> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());
		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int index = first;
		while (index >= first && index < last) {
			if (index < importList.size()) {
				ds.getResults().add((Customer) importList.get(index));
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
					ds.getResults().add((Customer) importList.get(index));
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
			customer = (Customer) importList.get(i);
			if (customer.getDataStatus().equals("正常")) {
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
					if (((Customer) importList
							.get(getEntity().getImportItem()[0]))
							.getDataStatus().equals("正常")) {
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

	@SuppressWarnings("unchecked")
	public String allCheckedItem() {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return IMPORT;
		}

		Set<Integer> checkItemSet = new TreeSet<Integer>();
		if (getSession().containsKey("checkItemSet")) {
			checkItemSet = (Set<Integer>) getSession().get("checkItemSet");
		}

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
					if (((Customer) importList
							.get(getEntity().getImportItem()[i]))
							.getDataStatus().equals("正常")) {
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
				customer = (Customer) importList.get(index);
				customerService.save(customer, getLoginUser());
				customer.setDataStatus("已匯入");
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
		rows.add(new String[] { "用戶名稱", "用戶英文名稱", "address/地址",
				"contactUserName/聯絡人", "tel/電話", "備註", "錯誤原因" });

		int i = 0;
		while (i < importList.size()) {
			customer = (Customer) importList.get(i);
			if (!customer.getDataStatus().equals("正常")
					&& !customer.getDataStatus().equals("已匯入")) {
				rows.add(new String[] { customer.getName(),
						customer.getEngName(), customer.getAddress(),
						customer.getContactUserName(), customer.getTel(),
						customer.getDataStatus() });
			}
			i++;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos), ',',
				CSVWriter.DEFAULT_QUOTE_CHARACTER,
				CSVWriter.NO_ESCAPE_CHARACTER, "\n");

		writer.writeAll(rows);
		writer.close();

		getEntity().setReportFile("customer_error.csv");
		getEntity()
				.setInputStream(new ByteArrayInputStream(baos.toByteArray()));

		return XLSX;
	}

	public String example() throws Exception {
		List<String[]> rows = new ArrayList<String[]>();
		rows.add(new String[] { "用戶名稱", "用戶英文名稱", "address/地址",
				"contactUserName/聯絡人", "tel/電話", "備註" });
		rows.add(new String[] { "國防醫學中心", "ndmc", "台北市內湖區民權東路六段161號", "總機",
				"886-2-87923100" });

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos), ',',
				CSVWriter.DEFAULT_QUOTE_CHARACTER,
				CSVWriter.NO_ESCAPE_CHARACTER, "\n");

		writer.writeAll(rows);
		writer.close();

		getEntity().setReportFile("customer_sample.csv");
		getEntity()
				.setInputStream(new ByteArrayInputStream(baos.toByteArray()));

		return XLSX;
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
