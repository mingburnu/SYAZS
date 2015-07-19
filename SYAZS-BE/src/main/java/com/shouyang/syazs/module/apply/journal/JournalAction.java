package com.shouyang.syazs.module.apply.journal;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.customer.CustomerService;
import com.shouyang.syazs.core.converter.EnumConverter;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;
import com.shouyang.syazs.module.apply.enums.Category;
import com.shouyang.syazs.module.apply.enums.Type;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyersService;
import com.shouyang.syazs.module.apply.resourcesUnion.ResourcesUnion;
import com.shouyang.syazs.module.apply.resourcesUnion.ResourcesUnionService;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JournalAction extends GenericWebActionFull<Journal> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4383738517930055495L;

	@Autowired
	private Journal journal;

	@Autowired
	private JournalService journalService;

	@Autowired
	private ResourcesUnion resourcesUnion;

	@Autowired
	private ResourcesUnionService resourcesUnionService;

	@Autowired
	private ResourcesBuyers resourcesBuyers;

	@Autowired
	private ResourcesBuyersService resourcesBuyersService;

	@Autowired
	private Customer customer;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private EnumConverter enumConverter;

	@Override
	protected void validateSave() throws Exception {
		if (StringUtils.isBlank(getEntity().getEnglishTitle())) {
			errorMessages.add("英文刊名不得空白");
		}

		if (StringUtils.isBlank(getEntity().getIssn())) {
			errorMessages.add("ISSN不得空白");
		} else {
			if (!isIssn(getEntity().getIssn())) {
				errorMessages.add("ISSN不正確");
			} else {
				if (journalService.getJouSerNoByIssn(getEntity().getIssn()
						.replace("-", "").trim()) != 0) {
					errorMessages.add("ISSN不可重複");
				}
			}
		}

		if (StringUtils.isNotEmpty(getEntity().getCongressClassification())) {
			if (!isLCC(getEntity().getCongressClassification())) {
				errorMessages.add("國會分類號格式不正確");
			}
		}

		if (ArrayUtils.isEmpty(getEntity().getCusSerNo())) {
			errorMessages.add("至少選擇一筆以上購買單位");
		} else {
			Set<Long> deRepeatSet = new HashSet<Long>(Arrays.asList(getEntity()
					.getCusSerNo()));
			getEntity().setCusSerNo(
					deRepeatSet.toArray(new Long[deRepeatSet.size()]));

			int i = 0;
			while (i < getEntity().getCusSerNo().length) {
				if (getEntity().getCusSerNo()[i] == null
						|| getEntity().getCusSerNo()[i] < 1
						|| customerService
								.getBySerNo(getEntity().getCusSerNo()[i]) == null) {
					errorMessages.add(getEntity().getCusSerNo()[i]
							+ "為不可利用的流水號");
					getEntity().getCusSerNo()[i] = null;
				}
				i++;
			}
		}

		if (getEntity().getResourcesBuyers().getCategory() == null
				|| getEntity().getResourcesBuyers().getCategory()
						.equals(Category.不明)) {
			errorMessages.add("資源類型錯誤");
			getEntity().getResourcesBuyers().setCategory(Category.未註明);
		}

		if (getEntity().getResourcesBuyers().getType() == null) {
			errorMessages.add("資源種類錯誤");
			getEntity().getResourcesBuyers().setType(Type.期刊);
		}
	}

	@Override
	protected void validateUpdate() throws Exception {
		if (!hasEntity()) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			if (StringUtils.isBlank(getEntity().getEnglishTitle())) {
				errorMessages.add("英文刊名不得空白");
			}

			if (StringUtils.isBlank(getEntity().getIssn())) {
				errorMessages.add("ISSN不得空白");
			} else {
				if (!isIssn(getEntity().getIssn())) {
					errorMessages.add("ISSN不正確");
				} else {
					long jouSerNo = journalService
							.getJouSerNoByIssn(getEntity().getIssn().trim()
									.replace("-", ""));
					if (jouSerNo != 0 && jouSerNo != getEntity().getSerNo()) {
						errorMessages.add("ISSN不可重複");
					}
				}
			}

			if (StringUtils.isNotEmpty(getEntity().getCongressClassification())) {
				if (!isLCC(getEntity().getCongressClassification())) {
					errorMessages.add("國會分類號格式不正確");
				}
			}

			if (ArrayUtils.isEmpty(getEntity().getCusSerNo())) {
				errorMessages.add("至少選擇一筆以上購買單位");
			} else {
				Set<Long> deRepeatSet = new HashSet<Long>(
						Arrays.asList(getEntity().getCusSerNo()));
				getEntity().setCusSerNo(
						deRepeatSet.toArray(new Long[deRepeatSet.size()]));

				int i = 0;
				while (i < getEntity().getCusSerNo().length) {
					if (getEntity().getCusSerNo()[i] == null
							|| getEntity().getCusSerNo()[i] < 1
							|| customerService.getBySerNo(getEntity()
									.getCusSerNo()[i]) == null) {
						errorMessages.add(getEntity().getCusSerNo()[i]
								+ "為不可利用的流水號");
						getEntity().getCusSerNo()[i] = null;
					}
					i++;
				}
			}

			if (getEntity().getResourcesBuyers().getCategory() == null
					|| getEntity().getResourcesBuyers().getCategory()
							.equals(Category.不明)) {
				errorMessages.add("資源類型錯誤");
				getEntity().getResourcesBuyers().setCategory(Category.未註明);
			}

			if (getEntity().getResourcesBuyers().getType() == null) {
				errorMessages.add("資源種類錯誤");
				getEntity().getResourcesBuyers().setType(Type.期刊);
			}
		}
	}

	@Override
	protected void validateDelete() throws Exception {
		if (ArrayUtils.isEmpty(getEntity().getCheckItem())) {
			errorMessages.add("請選擇一筆或一筆以上的資料");
		} else {
			Set<Long> deRepeatSet = new HashSet<Long>(Arrays.asList(getEntity()
					.getCheckItem()));
			getEntity().setCheckItem(
					deRepeatSet.toArray(new Long[deRepeatSet.size()]));

			int i = 0;
			while (i < getEntity().getCheckItem().length) {
				if (getEntity().getCheckItem()[i] == null
						|| getEntity().getCheckItem()[i] < 1
						|| journalService
								.getBySerNo(getEntity().getCheckItem()[i]) == null) {
					addActionError("有錯誤流水號");
					break;
				}
				i++;
			}
		}
	}

	@Override
	public String add() throws Exception {
		setCategoryList();

		List<Customer> customers = new ArrayList<Customer>();
		journal.setCustomers(customers);
		getRequest().setAttribute("uncheckCustomers",
				customerService.getUncheckCustomers(customers));
		journal.getResourcesBuyers().setCategory(Category.未註明);
		journal.getResourcesBuyers().setType(Type.期刊);
		setEntity(journal);

		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
			setCategoryList();

			Iterator<ResourcesUnion> iterator = resourcesUnionService
					.getResourcesUnionsByObj(getEntity(), Journal.class)
					.iterator();

			List<Customer> customers = new ArrayList<Customer>();

			while (iterator.hasNext()) {
				resourcesUnion = iterator.next();
				customer = resourcesUnion.getCustomer();
				if (customer != null) {
					customers.add(customer);
				}
			}

			journal.setResourcesBuyers(resourcesUnion.getResourcesBuyers());
			journal.setCustomers(customers);
			getRequest().setAttribute("uncheckCustomers",
					customerService.getUncheckCustomers(customers));
			setEntity(journal);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		if (StringUtils.isNotEmpty(getEntity().getOption())) {
			if (!getEntity().getOption().equals("entity.chineseTitle")
					&& !getEntity().getOption().equals("entity.englishTitle")
					&& !getEntity().getOption().equals("entity.issn")) {
				getEntity().setOption("entity.chineseTitle");
			}
		} else {
			getEntity().setOption("entity.chineseTitle");
		}

		if (getEntity().getOption().equals("entity.chineseTitle")) {
			getEntity().setEnglishTitle(null);
			getEntity().setIssn(null);
		} else if (getEntity().getOption().equals("entity.englishTitle")) {
			getEntity().setChineseTitle(null);
			getEntity().setIssn(null);
		} else {
			getEntity().setChineseTitle(null);
			getEntity().setEnglishTitle(null);
		}

		DataSet<Journal> ds = journalService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) (ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage() + 1));
			ds = journalService.getByRestrictions(ds);
		}

		int i = 0;
		while (i < ds.getResults().size()) {
			ds.getResults()
					.get(i)
					.setResourcesBuyers(
							resourcesUnionService.getByObjSerNo(
									ds.getResults().get(i).getSerNo(),
									Journal.class).getResourcesBuyers());
			i++;
		}

		setDs(ds);
		return LIST;
	}

	@Override
	public String save() throws Exception {
		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			journal = getEntity();
			journal.setIssn(getEntity().getIssn().toUpperCase()
					.replace("-", "").trim());

			journal = journalService.save(journal, getLoginUser());

			resourcesBuyers = resourcesBuyersService.save(getEntity()
					.getResourcesBuyers(), getLoginUser());

			int i = 0;
			while (i < getEntity().getCusSerNo().length) {
				resourcesUnionService.save(
						new ResourcesUnion(customerService
								.getBySerNo(getEntity().getCusSerNo()[i]),
								resourcesBuyers, 0L, 0L, journal.getSerNo()),
						getLoginUser());

				i++;
			}

			resourcesUnion = resourcesUnionService.getByObjSerNo(
					journal.getSerNo(), Journal.class);
			journal.setResourcesBuyers(resourcesUnion.getResourcesBuyers());

			List<ResourcesUnion> resourceUnions = resourcesUnionService
					.getResourcesUnionsByObj(journal, Journal.class);
			List<Customer> customers = new ArrayList<Customer>();

			Iterator<ResourcesUnion> iterator = resourceUnions.iterator();
			while (iterator.hasNext()) {
				resourcesUnion = iterator.next();
				customers.add(resourcesUnion.getCustomer());
			}

			journal.setCustomers(customers);
			setEntity(journal);
			addActionMessage("新增成功");
			return VIEW;
		} else {
			setCategoryList();
			getRequest().setAttribute("allCustomers",
					customerService.getAllCustomers());

			List<Customer> customers = new ArrayList<Customer>();
			if (ArrayUtils.isNotEmpty(getEntity().getCusSerNo())) {
				int i = 0;
				while (i < getEntity().getCusSerNo().length) {
					if (getEntity().getCusSerNo()[i] != null) {
						customers.add(customerService.getBySerNo(getEntity()
								.getCusSerNo()[i]));
					}
					i++;
				}
			}

			journal = getEntity();
			journal.setCustomers(customers);
			getRequest().setAttribute("uncheckCustomers",
					customerService.getUncheckCustomers(customers));
			setEntity(journal);
			return ADD;
		}
	}

	@Override
	public String update() throws Exception {
		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			journal = getEntity();
			journal.setIssn(getEntity().getIssn().toUpperCase()
					.replace("-", "").trim());
			journal = journalService.update(journal, getLoginUser());

			resourcesBuyers = new ResourcesBuyers(getEntity()
					.getResourcesBuyers().getStartDate(), getEntity()
					.getResourcesBuyers().getMaturityDate(), getEntity()
					.getResourcesBuyers().getCategory(), getEntity()
					.getResourcesBuyers().getType(), getEntity()
					.getResourcesBuyers().getDbChtTitle(), getEntity()
					.getResourcesBuyers().getDbEngTitle());
			resourcesBuyers.setSerNo(resourcesUnionService
					.getByObjSerNo(journal.getSerNo(), Journal.class)
					.getResourcesBuyers().getSerNo());
			resourcesBuyersService.update(resourcesBuyers, getLoginUser());

			List<ResourcesUnion> resourcesUnions = resourcesUnionService
					.getResourcesUnionsByObj(journal, Journal.class);

			for (int j = 0; j < getEntity().getCusSerNo().length; j++) {
				for (int i = 0; i < resourcesUnions.size(); i++) {
					resourcesUnion = resourcesUnions.get(i);
					if (resourcesUnion.getCustomer().getSerNo() == getEntity()
							.getCusSerNo()[j]) {
						resourcesUnions.remove(i);
					}
				}
			}

			Iterator<ResourcesUnion> iterator = resourcesUnions.iterator();
			while (iterator.hasNext()) {
				resourcesUnion = (ResourcesUnion) iterator.next();
				resourcesUnionService.deleteBySerNo(resourcesUnion.getSerNo());
			}

			int i = 0;
			while (i < getEntity().getCusSerNo().length) {
				if (!resourcesUnionService.isExist(journal, Journal.class,
						getEntity().getCusSerNo()[i])) {
					resourcesUnionService
							.save(new ResourcesUnion(customerService
									.getBySerNo(getEntity().getCusSerNo()[i]),
									resourcesBuyers, 0L, 0L, journal.getSerNo()),
									getLoginUser());
				}

				i++;
			}

			resourcesUnion = resourcesUnionService.getByObjSerNo(
					journal.getSerNo(), Journal.class);
			journal.setResourcesBuyers(resourcesUnion.getResourcesBuyers());

			List<ResourcesUnion> resourceUnions = resourcesUnionService
					.getResourcesUnionsByObj(journal, Journal.class);
			List<Customer> customers = new ArrayList<Customer>();

			iterator = resourceUnions.iterator();
			while (iterator.hasNext()) {
				resourcesUnion = iterator.next();
				customers.add(resourcesUnion.getCustomer());
			}

			journal.setCustomers(customers);
			setEntity(journal);
			addActionMessage("修改成功");
			return VIEW;
		} else {
			setCategoryList();
			getRequest().setAttribute("allCustomers",
					customerService.getAllCustomers());

			List<Customer> customers = new ArrayList<Customer>();
			if (ArrayUtils.isNotEmpty(getEntity().getCusSerNo())) {
				int i = 0;
				while (i < getEntity().getCusSerNo().length) {
					if (getEntity().getCusSerNo()[i] != null) {
						customers.add(customerService.getBySerNo(getEntity()
								.getCusSerNo()[i]));
					}
					i++;
				}
			}

			journal = getEntity();
			journal.setCustomers(customers);
			getRequest().setAttribute("uncheckCustomers",
					customerService.getUncheckCustomers(customers));
			setEntity(journal);
			return EDIT;
		}

	}

	@Override
	public String delete() throws Exception {
		validateDelete();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			int j = 0;
			while (j < getEntity().getCheckItem().length) {
				List<ResourcesUnion> resourcesUnions = resourcesUnionService
						.getResourcesUnionsByObj(journalService
								.getBySerNo(getEntity().getCheckItem()[j]),
								Journal.class);
				resourcesUnion = resourcesUnions.get(0);

				Iterator<ResourcesUnion> iterator = resourcesUnions.iterator();
				while (iterator.hasNext()) {
					resourcesUnion = iterator.next();
					resourcesUnionService.deleteBySerNo(resourcesUnion
							.getSerNo());
				}
				resourcesBuyersService.deleteBySerNo(resourcesUnion
						.getResourcesBuyers().getSerNo());
				journalService.deleteBySerNo(getEntity().getCheckItem()[j]);

				j++;
			}

			list();
			addActionMessage("刪除成功");
			return LIST;
		} else {
			list();
			return LIST;
		}
	}

	public String view() throws NumberFormatException, Exception {
		if (hasEntity()) {
			resourcesUnion = resourcesUnionService.getByObjSerNo(
					journal.getSerNo(), Journal.class);

			journal.setResourcesBuyers(resourcesUnion.getResourcesBuyers());

			List<ResourcesUnion> resourceUnions = resourcesUnionService
					.getResourcesUnionsByObj(journal, Journal.class);
			List<Customer> customers = new ArrayList<Customer>();

			Iterator<ResourcesUnion> iterator = resourceUnions.iterator();
			while (iterator.hasNext()) {
				resourcesUnion = iterator.next();
				customers.add(resourcesUnion.getCustomer());
			}

			journal.setCustomers(customers);
			getRequest().setAttribute("viewSerNo", getEntity().getSerNo());
			setEntity(journal);
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
			// book.getNumberOfSheets(); 判斷Excel文件有多少個sheet
			Sheet sheet = book.getSheetAt(0);

			Row firstRow = sheet.getRow(0);
			if (firstRow == null) {
				firstRow = sheet.createRow(0);
			}

			// 保存列名
			List<String> cellNames = new ArrayList<String>();
			String[] rowTitles = new String[17];
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

			LinkedHashSet<Journal> originalData = new LinkedHashSet<Journal>();
			Map<String, Journal> checkRepeatRow = new LinkedHashMap<String, Journal>();
			int normal = 0;

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				String[] rowValues = new String[17];
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
							rowValues[k] = row.getCell(k).getStringCellValue()
									.trim();
							break;

						case 2:
							rowValues[k] = row.getCell(k).getCellFormula()
									.trim();
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

				String category = "";
				if (rowValues[13] == null || rowValues[13].trim().equals("")) {
					category = Category.未註明.getCategory();
				} else {
					Object object = getEnum(
							new String[] { rowValues[13].trim() },
							Category.class);
					if (object != null) {
						category = rowValues[13].trim();
					} else {
						category = Category.不明.getCategory();
					}
				}

				String type = "";
				if (rowValues[14] == null || rowValues[14].trim().equals("")) {
					type = Type.資料庫.getType();
				} else {
					Object object = getEnum(
							new String[] { rowValues[14].trim() }, Type.class);
					if (object != null) {
						type = rowValues[14].trim();
					} else {
						type = Type.資料庫.getType();
					}
				}

				resourcesBuyers = new ResourcesBuyers(rowValues[9],
						rowValues[10], Category.valueOf(category),
						Type.valueOf(type), rowValues[13], rowValues[14]);

				String issn = rowValues[3].trim().replace("-", "")
						.toUpperCase();

				customer = new Customer();
				customer.setName(rowValues[15].trim());
				customer.setEngName(rowValues[16].trim());

				List<Customer> customers = new ArrayList<Customer>();
				customers.add(customer);

				journal = new Journal(rowValues[0], rowValues[1], rowValues[2],
						"", issn, rowValues[4], rowValues[5], rowValues[6], "",
						"", "", rowValues[7], rowValues[8], null);
				journal.setResourcesBuyers(resourcesBuyers);
				journal.setCustomers(customers);

				if (isIssn(issn)) {
					long jouSerNo = journalService.getJouSerNoByIssn(issn);

					long cusSerNo = customerService
							.getCusSerNoByName(rowValues[15].trim());
					if (cusSerNo != 0) {
						if (jouSerNo != 0) {
							if (resourcesUnionService.isExist(
									journalService.getBySerNo(jouSerNo),
									Journal.class, cusSerNo)) {

								journal.setDataStatus("已存在");
							}
						} else {
							if (journal.getResourcesBuyers().getCategory()
									.equals(Category.不明)) {
								journal.setDataStatus("資源類型不明");
							}
						}
					} else {
						journal.setDataStatus("無此客戶");
					}
				} else {
					journal.setDataStatus("ISSN異常");
				}

				if (StringUtils.isNotEmpty(journal.getCongressClassification())) {
					if (!isLCC(journal.getCongressClassification())) {
						journal.setCongressClassification(null);
					}
				}

				if (journal.getDataStatus() == null) {
					journal.setDataStatus("正常");
				}

				if (journal.getDataStatus().equals("正常")
						&& !originalData.contains(journal)) {

					if (checkRepeatRow.containsKey(journal.getIssn()
							+ customer.getName())) {
						journal.setDataStatus("資料重複");

					} else {
						checkRepeatRow
								.put(journal.getIssn() + customer.getName(),
										journal);
						++normal;
					}
				}

				originalData.add(journal);
			}

			List<Journal> excelData = new ArrayList<Journal>(originalData);

			DataSet<Journal> ds = initDataSet();
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

		DataSet<Journal> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int i = 0;
		while (i < importList.size()) {
			if (i >= first && i < last) {
				ds.getResults().add((Journal) importList.get(i));
			}
			i++;
		}

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) (ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage() + 1));
			first = ds.getPager().getOffset();
			last = first + ds.getPager().getRecordPerPage();

			int j = 0;
			while (j < importList.size()) {
				if (j >= first && j < last) {
					ds.getResults().add((Journal) importList.get(j));
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
			return null;
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

		return null;
	}

	public String allCheckedItem() {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return null;
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
				journal = (Journal) importList.get(index);

				long jouSerNo = journalService.getJouSerNoByIssn(journal
						.getIssn());
				long cusSerNo = customerService.getCusSerNoByName(journal
						.getCustomers().get(0).getName());

				if (jouSerNo == 0) {
					resourcesBuyers = resourcesBuyersService.save(
							journal.getResourcesBuyers(), getLoginUser());
					journal = journalService.save(journal, getLoginUser());

					resourcesUnionService.save(new ResourcesUnion(
							customerService.getBySerNo(cusSerNo),
							resourcesBuyers, 0L, 0L, journal.getSerNo()),
							getLoginUser());
				} else {
					resourcesUnion = resourcesUnionService.getByObjSerNo(
							jouSerNo, Journal.class);
					resourcesUnionService.save(new ResourcesUnion(
							customerService.getBySerNo(cusSerNo),
							resourcesUnion.getResourcesBuyers(), 0L, 0L,
							jouSerNo), getLoginUser());
				}

				successCount = successCount + 1;
			}

			getRequest().setAttribute("successCount", successCount);
			return VIEW;
		} else {
			paginate();
			return QUEUE;
		}
	}

	public String example() throws Exception {
		getEntity().setReportFile("journal_sample.xlsx");

		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("journal");
		// Create row object
		XSSFRow row;
		// This data needs to be written (Object[])
		Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
		empinfo.put("1", new Object[] { "中文刊名", "英文刊名", "英文縮寫刊名", "ISSN", "語文",
				"出版項/出版社", "出版年", "刊別/期刊頻率", "國會分類號", "起始日", "到期日",
				"資源類型(買斷/租用)", "資源分類", "資料庫中文題名", "資料庫英文題名", "購買單位名稱",
				"購買單位英文名稱" });

		empinfo.put("2", new Object[] { "N/A",
				"The New England Journal of Medicine", "", "15334406", "eng",
				"NEJM", "1812", "weekly", "N/A", "N/A", "N/A", "租貸", "期刊", "",
				"", "衛生福利部台北醫院", "" });
		empinfo.put("3", new Object[] { "N/A",
				"The New England Journal of Medicine", "", "15334406", "eng",
				"NEJM", "1812", "weekly", "N/A", "N/A", "N/A", "租貸", "期刊", "",
				"", "衛生福利部桃園醫院", "" });

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

	protected boolean isIssn(String issn) {
		String regex = "\\d{7}[\\dX]";
		Pattern pattern = Pattern.compile(regex);
		issn = issn.replace("-", "").trim();

		Matcher matcher = pattern.matcher(issn.toUpperCase());
		if (matcher.matches()) {
			int sum = Integer.parseInt(issn.substring(0, 1)) * 8
					+ Integer.parseInt(issn.substring(1, 2)) * 7
					+ Integer.parseInt(issn.substring(2, 3)) * 6
					+ Integer.parseInt(issn.substring(3, 4)) * 5
					+ Integer.parseInt(issn.substring(4, 5)) * 4
					+ Integer.parseInt(issn.substring(5, 6)) * 3
					+ Integer.parseInt(issn.substring(6, 7)) * 2;

			int remainder = sum % 11;

			if (remainder == 0) {
				if (!issn.substring(7).equals("0")) {
					return false;
				}
			} else {
				if (11 - remainder == 10) {
					if (!issn.substring(7).toUpperCase().equals("X")) {
						return false;
					}
				} else {
					if (issn.substring(7).equals("X")
							|| issn.substring(7).equals("x")
							|| Integer.parseInt(issn.substring(7)) != 11 - remainder) {
						return false;
					}
				}
			}

		} else {
			return false;
		}
		return true;
	}

	protected boolean isLCC(String LCC) {
		String LCCPattern = "([A-Z]{1,3})((\\d+)(\\.?)(\\d+))";

		return Pattern.compile(LCCPattern).matcher(LCC).matches();
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

	protected void setCategoryList() {
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);
		ActionContext.getContext().getValueStack()
				.set("categoryList", categoryList);
	}

	protected boolean hasEntity() throws Exception {
		if (getEntity().isNew()) {
			return false;
		}

		journal = journalService.getBySerNo(getEntity().getSerNo());
		if (journal == null) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("rawtypes")
	protected Object getEnum(String[] values, Class toClass) {
		return enumConverter.convertFromString(null, values, toClass);
	}
}
