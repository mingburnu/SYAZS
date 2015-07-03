package com.shouyang.syazs.module.apply.journal;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.model.Pager;
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

	private String[] checkItem;

	private String[] cusSerNo;

	private File[] file;

	private String[] fileFileName;

	private String[] fileContentType;

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

	private String[] importSerNos;

	@Override
	protected void validateSave() throws Exception {
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);

		List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type.values()));

		if (StringUtils.isBlank(getEntity().getEnglishTitle())) {
			errorMessages.add("英文刊名不得空白");
		}

		if (StringUtils.isBlank(getEntity().getIssn())) {
			errorMessages.add("ISSN不得空白");
		} else {
			if (!isIssn(getEntity().getIssn())) {
				errorMessages.add("ISSN不正確");
			} else {
				if (journalService.getJouSerNoByIssn(getEntity().getIssn()) != 0) {
					errorMessages.add("ISSN不可重複");
				}
			}
		}

		if (StringUtils.isNotEmpty(getEntity().getCongressClassification())) {
			if (!isLCC(getEntity().getCongressClassification())) {
				errorMessages.add("國會分類號格式不正確");
			}
		}

		if (ArrayUtils.isEmpty(cusSerNo)) {
			errorMessages.add("至少選擇一筆以上購買單位");
		} else {
			int i = 0;
			while (i < cusSerNo.length) {
				if (!NumberUtils.isDigits(String.valueOf(cusSerNo[i]))
						|| Long.parseLong(cusSerNo[i]) < 1
						|| customerService.getBySerNo(Long
								.parseLong(cusSerNo[i])) == null) {
					errorMessages.add(cusSerNo[i] + "為不可利用的流水號");
				}
				i++;
			}
		}

		boolean isLegalCategory = false;
		for (int i = 0; i < categoryList.size(); i++) {
			if (getRequest().getParameter("rCategory") != null
					&& getRequest().getParameter("rCategory").equals(
							categoryList.get(i).getCategory())) {
				isLegalCategory = true;
			}
		}

		if (isLegalCategory) {
			getRequest().setAttribute("rType",
					getRequest().getParameter("rCategory"));
		} else {
			errorMessages.add("資源類型錯誤");
		}

		boolean isLegalType = false;
		for (int i = 0; i < categoryList.size(); i++) {
			if (getRequest().getParameter("rType") != null
					&& getRequest().getParameter("rType").equals(
							typeList.get(i).getType())) {
				isLegalType = true;
			}
		}

		if (isLegalType) {
			getRequest().setAttribute("rType",
					getRequest().getParameter("rType"));
		} else {
			errorMessages.add("資源種類錯誤");
		}
	}

	@Override
	protected void validateUpdate() throws Exception {
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);

		List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type.values()));

		if (!hasEntity()) {
			errorMessages.add("Target must not be null");
		}

		if (StringUtils.isBlank(getEntity().getEnglishTitle())) {
			errorMessages.add("英文刊名不得空白");
		}

		if (StringUtils.isBlank(getEntity().getIssn())) {
			errorMessages.add("ISSN不得空白");
		} else {
			if (!isIssn(getEntity().getIssn())) {
				errorMessages.add("ISSN不正確");
			} else {
				long jouSerNo = journalService.getJouSerNoByIssn(getEntity()
						.getIssn().trim().replace("-", ""));
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

		if (ArrayUtils.isEmpty(cusSerNo)) {
			errorMessages.add("至少選擇一筆以上購買單位");
		} else {
			int i = 0;
			while (i < cusSerNo.length) {
				if (!NumberUtils.isDigits(String.valueOf(cusSerNo[i]))
						|| Long.parseLong(cusSerNo[i]) < 1
						|| customerService.getBySerNo(Long
								.parseLong(cusSerNo[i])) == null) {
					errorMessages.add(cusSerNo[i] + "為不可利用的流水號");
				}
				i++;
			}
		}

		boolean isLegalCategory = false;
		for (int i = 0; i < categoryList.size(); i++) {
			if (getRequest().getParameter("rCategory") != null
					&& getRequest().getParameter("rCategory").equals(
							categoryList.get(i).getCategory())) {
				isLegalCategory = true;
			}
		}

		if (isLegalCategory) {
			getRequest().setAttribute("rType",
					getRequest().getParameter("rCategory"));
		} else {
			errorMessages.add("資源類型錯誤");
		}

		boolean isLegalType = false;
		for (int i = 0; i < categoryList.size(); i++) {
			if (getRequest().getParameter("rType") != null
					&& getRequest().getParameter("rType").equals(
							typeList.get(i).getType())) {
				isLegalType = true;
			}
		}

		if (isLegalType) {
			getRequest().setAttribute("rType",
					getRequest().getParameter("rType"));
		} else {
			errorMessages.add("資源種類錯誤");
		}
	}

	@Override
	protected void validateDelete() throws Exception {
		if (ArrayUtils.isEmpty(checkItem)) {
			errorMessages.add("請選擇一筆或一筆以上的資料");
		} else {
			int i = 0;
			while (i < checkItem.length) {
				if (!NumberUtils.isDigits(String.valueOf(checkItem[i]))
						|| Long.parseLong(checkItem[i]) < 1
						|| journalService.getBySerNo(Long
								.parseLong(checkItem[i])) == null) {
					errorMessages.add(checkItem[i] + "為不可利用的流水號");
				}
				i++;
			}
		}
	}

	@Override
	public String edit() throws Exception {
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);
		getRequest().setAttribute("categoryList", categoryList);

		List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type.values()));
		getRequest().setAttribute("typeList", typeList);

		getRequest().setAttribute("allCustomers",
				customerService.getAllCustomers());
		if (getEntity().getSerNo() != null) {
			journal = journalService.getBySerNo(getEntity().getSerNo());

			if (journal != null) {
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

				resourcesBuyers = resourcesUnion.getResourcesBuyers();
				getRequest().setAttribute("rCategory",
						resourcesBuyers.getrCategory().getCategory());
				getRequest().setAttribute("rType",
						resourcesBuyers.getrType().getType());
				journal.setCustomers(customers);
			}

			setEntity(journal);
		} else {
			List<Customer> customers = new ArrayList<Customer>();
			journal.setCustomers(customers);
			setEntity(journal);
		}
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		if (StringUtils.isNotEmpty(getRequest().getParameter("option"))) {
			if (getRequest().getParameter("option").equals(
					"entity.chineseTitle")
					|| getRequest().getParameter("option").equals(
							"entity.englishTitle")
					|| getRequest().getParameter("option")
							.equals("entity.issn")) {
				getRequest().setAttribute("option",
						getRequest().getParameter("option"));

			} else {
				getRequest().setAttribute("option", "entity.chineseTitle");
			}

		} else {
			getRequest().setAttribute("option", "entity.chineseTitle");

		}

		DataSet<Journal> ds = initDataSet();
		ds.setPager(Pager.getChangedPager(
				getRequest().getParameter("recordPerPage"), getRequest()
						.getParameter("recordPoint"), ds.getPager()));
		ds = journalService.getByRestrictions(ds);

		List<Journal> results = ds.getResults();

		int i = 0;
		while (i < results.size()) {
			results.get(i).setResourcesBuyers(
					resourcesUnionService.getByObjSerNo(
							results.get(i).getSerNo(), Journal.class)
							.getResourcesBuyers());
			i++;
		}

		ds.setResults(results);
		setDs(ds);
		return LIST;
	}

	@Override
	public String save() throws Exception {
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);

		List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type.values()));

		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			journal = getEntity();
			journal.setIssn(getEntity().getIssn().toUpperCase());
			journal = journalService.save(journal, getLoginUser());

			resourcesBuyers = resourcesBuyersService.save(new ResourcesBuyers(
					getRequest().getParameter("resourcesBuyers.startDate"),
					getRequest().getParameter("resourcesBuyers.maturityDate"),
					Category.valueOf(getRequest().getParameter("rCategory")),
					Type.valueOf(getRequest().getParameter("rType")),
					getRequest().getParameter("resourcesBuyers.dbChtTitle"),
					getRequest().getParameter("resourcesBuyers.dbEngTitle")),
					getLoginUser());

			int i = 0;
			while (i < cusSerNo.length) {
				resourcesUnionService.save(
						new ResourcesUnion(customerService.getBySerNo(Long
								.parseLong(cusSerNo[i])), resourcesBuyers, 0L,
								0L, journal.getSerNo()), getLoginUser());

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
			getRequest().setAttribute("categoryList", categoryList);
			getRequest().setAttribute("typeList", typeList);
			getRequest().setAttribute("allCustomers",
					customerService.getAllCustomers());

			List<Customer> customers = new ArrayList<Customer>();
			if (cusSerNo != null && cusSerNo.length != 0) {
				int i = 0;
				while (i < cusSerNo.length) {
					if (cusSerNo[i] != null
							&& NumberUtils.isDigits(cusSerNo[i])) {
						if (customerService.getBySerNo(Long
								.parseLong(cusSerNo[i])) != null) {
							customers.add(customerService.getBySerNo(Long
									.parseLong(cusSerNo[i])));
						}
					}
					i++;
				}
			}

			journal = getEntity();
			journal.setCustomers(customers);
			setEntity(journal);
			return EDIT;
		}
	}

	@Override
	public String update() throws Exception {
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);

		List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type.values()));

		boolean isLegalCategory = false;
		for (int i = 0; i < categoryList.size(); i++) {
			if (getRequest().getParameter("rCategory") != null
					&& getRequest().getParameter("rCategory").equals(
							categoryList.get(i).getCategory())) {
				isLegalCategory = true;
			}
		}

		if (isLegalCategory) {
			getRequest().setAttribute("rCategory",
					getRequest().getParameter("rCategory"));
		}

		boolean isLegalType = false;
		for (int i = 0; i < categoryList.size(); i++) {
			if (getRequest().getParameter("rType") != null
					&& getRequest().getParameter("rType").equals(
							typeList.get(i).getType())) {
				isLegalType = true;
			}
		}

		if (isLegalType) {
			getRequest().setAttribute("rType",
					getRequest().getParameter("rType"));
		}

		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			journal = getEntity();
			journal.setIssn(getEntity().getIssn().toUpperCase());
			journal = journalService.update(journal, getLoginUser());

			resourcesBuyers = resourcesUnionService.getByObjSerNo(
					journal.getSerNo(), Journal.class).getResourcesBuyers();
			resourcesBuyers.setStartDate(getRequest().getParameter(
					"resourcesBuyers.startDate"));
			resourcesBuyers.setMaturityDate(getRequest().getParameter(
					"resourcesBuyers.maturityDate"));
			resourcesBuyers.setrCategory(Category.valueOf(getRequest()
					.getParameter("rCategory")));
			resourcesBuyers.setrType(Type.valueOf(getRequest().getParameter(
					"rType")));
			resourcesBuyers.setDbChtTitle(getRequest().getParameter(
					"resourcesBuyers.dbChtTitle"));
			resourcesBuyers.setDbEngTitle(getRequest().getParameter(
					"resourcesBuyers.dbEngTitle"));
			resourcesBuyersService.update(resourcesBuyers, getLoginUser());

			List<ResourcesUnion> resourcesUnions = resourcesUnionService
					.getResourcesUnionsByObj(journal, Journal.class);

			for (int j = 0; j < cusSerNo.length; j++) {
				for (int i = 0; i < resourcesUnions.size(); i++) {
					resourcesUnion = resourcesUnions.get(i);
					if (resourcesUnion.getCustomer().getSerNo() == Long
							.parseLong(cusSerNo[j])) {
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
			while (i < cusSerNo.length) {
				if (!resourcesUnionService.isExist(journal, Journal.class,
						Long.parseLong(cusSerNo[i]))) {
					resourcesUnionService
							.save(new ResourcesUnion(customerService
									.getBySerNo(Long.parseLong(cusSerNo[i])),
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
			getRequest().setAttribute("typeList", typeList);
			getRequest().setAttribute("categoryList", categoryList);
			getRequest().setAttribute("allCustomers",
					customerService.getAllCustomers());

			List<Customer> customers = new ArrayList<Customer>();
			if (cusSerNo != null && cusSerNo.length != 0) {
				int i = 0;
				while (i < cusSerNo.length) {
					customers.add(customerService.getBySerNo(Long
							.parseLong(cusSerNo[i])));
					i++;
				}
			}

			journal = getEntity();
			journal.setCustomers(customers);
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
			while (j < checkItem.length) {
				List<ResourcesUnion> resourcesUnions = resourcesUnionService
						.getResourcesUnionsByObj(journalService.getBySerNo(Long
								.parseLong(checkItem[j])), Journal.class);
				resourcesUnion = resourcesUnions.get(0);

				Iterator<ResourcesUnion> iterator = resourcesUnions.iterator();
				while (iterator.hasNext()) {
					resourcesUnion = iterator.next();
					resourcesUnionService.deleteBySerNo(resourcesUnion
							.getSerNo());
				}
				resourcesBuyersService.deleteBySerNo(resourcesUnion
						.getResourcesBuyers().getSerNo());
				journalService.deleteBySerNo(Long.parseLong(checkItem[j]));

				j++;
			}

			DataSet<Journal> ds = journalService
					.getByRestrictions(initDataSet());
			List<Journal> results = ds.getResults();

			int i = 0;
			while (i < results.size()) {
				results.get(i).setResourcesBuyers(
						resourcesUnionService.getByObjSerNo(
								results.get(i).getSerNo(), Journal.class)
								.getResourcesBuyers());
				i++;
			}

			setDs(ds);
			addActionMessage("刪除成功");
			return LIST;
		} else {
			DataSet<Journal> ds = journalService
					.getByRestrictions(initDataSet());
			List<Journal> results = ds.getResults();

			int i = 0;
			while (i < results.size()) {
				results.get(i).setResourcesBuyers(
						resourcesUnionService.getByObjSerNo(
								results.get(i).getSerNo(), Journal.class)
								.getResourcesBuyers());
				i++;
			}

			setDs(ds);
			return LIST;
		}
	}

	public String view() throws NumberFormatException, Exception {
		getRequest().setAttribute("viewSerNo",
				getRequest().getParameter("viewSerNo"));

		if (StringUtils.isNotBlank(getRequest().getParameter("viewSerNo"))
				&& NumberUtils.isDigits(getRequest().getParameter("viewSerNo"))) {
			journal = journalService.getBySerNo(Long.parseLong(getRequest()
					.getParameter("viewSerNo")));
			if (journal != null) {
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
			}
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

				List<Category> categoryList = new ArrayList<Category>(
						Arrays.asList(Category.values()));
				categoryList.remove(categoryList.size() - 1);

				String category = "";
				if (rowValues[11] == null || rowValues[11].trim().equals("")) {
					category = Category.未註明.getCategory();
				} else {
					boolean isLegalCategory = false;
					for (int j = 0; j < categoryList.size(); j++) {
						if (rowValues[11].trim().equals(
								categoryList.get(j).getCategory())) {
							category = categoryList.get(j).getCategory();
							isLegalCategory = true;
						}
					}

					if (!isLegalCategory) {
						category = Category.不明.getCategory();
					}
				}

				List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type
						.values()));
				String type = "";
				if (rowValues[12] == null || rowValues[12].trim().equals("")) {
					type = Type.期刊.getType();
				} else {
					boolean isLegalType = false;
					for (int j = 0; j < typeList.size(); j++) {
						if (rowValues[12].trim().equals(
								typeList.get(j).getType())) {
							type = typeList.get(j).getType();
							isLegalType = true;
						}
					}

					if (!isLegalType) {
						type = Type.期刊.getType();
					}
				}

				resourcesBuyers = new ResourcesBuyers(rowValues[9],
						rowValues[10], Category.valueOf(category),
						Type.valueOf(type), rowValues[13], rowValues[14]);

				String issn = rowValues[3].trim().replace("-", "")
						.toUpperCase();

				journal = new Journal(rowValues[0], rowValues[1], rowValues[2],
						"", issn, rowValues[4], rowValues[5], rowValues[6], "",
						"", "", rowValues[7], rowValues[8], null,
						resourcesBuyers, null, "");

				customer = new Customer();
				customer.setName(rowValues[15].trim());
				customer.setEngName(rowValues[16].trim());

				List<Customer> customers = new ArrayList<Customer>();
				customers.add(customer);
				journal.setCustomers(customers);

				if (isIssn(issn)) {
					long jouSerNo = journalService.getJouSerNoByIssn(issn
							.toUpperCase());

					long cusSerNo = customerService
							.getCusSerNoByName(rowValues[15].trim());
					if (cusSerNo != 0) {
						if (jouSerNo != 0) {
							if (resourcesUnionService.isExist(
									journalService.getBySerNo(jouSerNo),
									Journal.class, cusSerNo)) {

								journal.setExistStatus("已存在");
							}
						} else {
							if (journal.getResourcesBuyers().getrCategory()
									.equals(Category.不明)) {
								journal.setExistStatus("資源類型不明");
							}
						}
					} else {
						journal.setExistStatus("無此客戶");
					}
				} else {
					journal.setExistStatus("ISSN異常");
				}

				if (StringUtils.isNotEmpty(journal.getCongressClassification())) {
					if (!isLCC(journal.getCongressClassification())) {
						journal.setCongressClassification(null);
					}
				}

				if (journal.getExistStatus().equals("")) {
					journal.setExistStatus("正常");
				}

				if (journal.getExistStatus().equals("正常")
						&& !originalData.contains(journal)) {

					if (checkRepeatRow.containsKey(journal.getIssn()
							+ customer.getName())) {
						journal.setExistStatus("資料重複");

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
			List<Journal> results = ds.getResults();

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
		ds.setPager(Pager.getChangedPager(
				getRequest().getParameter("recordPerPage"), getRequest()
						.getParameter("recordPoint"), ds.getPager()));
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getRecordPerPage()
				* (ds.getPager().getCurrentPage() - 1);
		int last = first + ds.getPager().getRecordPerPage();

		List<Journal> results = new ArrayList<Journal>();

		int i = 0;
		while (i < importList.size()) {
			if (i >= first && i < last) {
				results.add((Journal) importList.get(i));
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
					if (((Journal) importList.get(Integer
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
						if (((Journal) importList.get(Integer
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

		Set<Integer> checkItem = new TreeSet<Integer>();
		getSession().put("checkItem", checkItem);
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
		setInputStream(new ByteArrayInputStream(boas.toByteArray()));

		return XLSX;
	}

	public boolean isIssn(String issn) {
		String regex = "\\d{7}[\\dX]";
		Pattern pattern = Pattern.compile(regex);

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

	public boolean isLCC(String LCC) {
		String LCCPattern = "([A-Z]{1,3})((\\d+)(\\.?)(\\d+))";

		return Pattern.compile(LCCPattern).matcher(LCC).matches();
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

	public boolean hasEntity() throws Exception {
		if (getEntity().getSerNo() == null) {
			getEntity().setSerNo(-1L);
			return false;
		}

		journal = journalService.getBySerNo(getEntity().getSerNo());
		if (journal == null) {
			return false;
		}

		return true;
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
	 * @return the cusSerNo
	 */
	public String[] getCusSerNo() {
		return cusSerNo;
	}

	/**
	 * @param cusSerNo
	 *            the cusSerNo to set
	 */
	public void setCusSerNo(String[] cusSerNo) {
		this.cusSerNo = cusSerNo;
	}

	/**
	 * @return the resourcesBuyers
	 */
	public ResourcesBuyers getResourcesBuyers() {
		return resourcesBuyers;
	}

	/**
	 * @param resourcesBuyers
	 *            the resourcesBuyers to set
	 */
	public void setResourcesBuyers(ResourcesBuyers resourcesBuyers) {
		this.resourcesBuyers = resourcesBuyers;
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
