package com.shouyang.syazs.module.apply.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
public class DatabaseAction extends GenericWebActionFull<Database> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5792761795605940212L;

	private String[] checkItem;

	private String[] cusSerNo;

	private File[] file;

	private String[] fileFileName;

	private String[] fileContentType;

	@Autowired
	private Database database;

	@Autowired
	private DatabaseService databaseService;

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
		Set<String> deRepeatSet = new HashSet<String>(Arrays.asList(cusSerNo));
		cusSerNo = deRepeatSet.toArray(new String[deRepeatSet.size()]);
		
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);

		List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type.values()));

		if (StringUtils.isBlank(getEntity().getDbChtTitle())
				&& StringUtils.isBlank(getEntity().getDbEngTitle())) {
			errorMessages.add("沒有資料庫名稱");
		} else {
			if (databaseService.getDatSerNoByChtName(getEntity()
					.getDbChtTitle()) != 0) {
				errorMessages.add("資料庫中文名稱已存在");
			}

			if (databaseService.getDatSerNoByEngName(getEntity()
					.getDbEngTitle()) != 0) {
				errorMessages.add("資料庫英文名稱已存在");
			}
		}

		if (StringUtils.isNotEmpty(getEntity().getUrl())) {
			if (!isURL(getEntity().getUrl())) {
				errorMessages.add("URL格式不正確");
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
			getRequest().setAttribute("rCategory",
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
		if (!hasEntity()) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			Set<String> deRepeatSet = new HashSet<String>(Arrays.asList(cusSerNo));
			cusSerNo = deRepeatSet.toArray(new String[deRepeatSet.size()]);
			
			List<Category> categoryList = new ArrayList<Category>(
					Arrays.asList(Category.values()));
			categoryList.remove(categoryList.size() - 1);

			List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type
					.values()));

			if (StringUtils.isBlank(getEntity().getDbChtTitle())
					&& StringUtils.isBlank(getEntity().getDbEngTitle())) {
				errorMessages.add("沒有資料庫名稱");
			} else {
				long datSerNo = databaseService
						.getDatSerNoByChtName(getEntity().getDbChtTitle());
				if (datSerNo != 0 && datSerNo != getEntity().getSerNo()) {
					errorMessages.add("資料庫中文名稱已存在");
				}

				datSerNo = databaseService.getDatSerNoByEngName(getEntity()
						.getDbEngTitle());
				if (datSerNo != 0 && datSerNo != getEntity().getSerNo()) {
					errorMessages.add("資料庫英文文名稱已存在");
				}
			}

			if (StringUtils.isNotEmpty(getEntity().getUrl())) {
				if (!isURL(getEntity().getUrl())) {
					errorMessages.add("URL格式不正確");
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
				getRequest().setAttribute("rCategory",
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
	}

	@Override
	protected void validateDelete() throws Exception {
		Set<String> deRepeatSet = new HashSet<String>(Arrays.asList(checkItem));
		checkItem = deRepeatSet.toArray(new String[deRepeatSet.size()]);

		if (ArrayUtils.isEmpty(checkItem)) {
			errorMessages.add("請選擇一筆或一筆以上的資料");
		} else {
			int i = 0;
			while (i < checkItem.length) {
				if (!NumberUtils.isDigits(String.valueOf(checkItem[i]))
						|| Long.parseLong(checkItem[i]) < 1
						|| databaseService.getBySerNo(Long
								.parseLong(checkItem[i])) == null) {
					errorMessages.add(checkItem[i] + "為不可利用的流水號");
				}
				i++;
			}
		}
	}

	@Override
	public String add() throws Exception {
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);
		getRequest().setAttribute("categoryList", categoryList);

		List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type.values()));
		getRequest().setAttribute("typeList", typeList);

		getRequest().setAttribute("allCustomers",
				customerService.getAllCustomers());

		List<Customer> customers = new ArrayList<Customer>();
		database.setCustomers(customers);
		setEntity(database);

		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
			List<Category> categoryList = new ArrayList<Category>(
					Arrays.asList(Category.values()));
			categoryList.remove(categoryList.size() - 1);
			getRequest().setAttribute("categoryList", categoryList);

			List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type
					.values()));
			getRequest().setAttribute("typeList", typeList);

			getRequest().setAttribute("allCustomers",
					customerService.getAllCustomers());

			Iterator<ResourcesUnion> iterator = resourcesUnionService
					.getResourcesUnionsByObj(getEntity(), Database.class)
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
			database.setCustomers(customers);

			setEntity(database);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		if (StringUtils.isNotBlank(getRequest().getParameter("option"))) {
			if (getRequest().getParameter("option").equals("entity.dbChtTitle")
					|| getRequest().getParameter("option").equals(
							"entity.dbEngTitle")) {
				getRequest().setAttribute("option",
						getRequest().getParameter("option"));

			} else {
				getRequest().setAttribute("option", "entity.dbChtTitle");
			}

		} else {
			getRequest().setAttribute("option", "entity.dbChtTitle");

		}

		DataSet<Database> ds = databaseService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) (ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage() + 1));
			ds = databaseService.getByRestrictions(ds);
		}

		int i = 0;
		while (i < ds.getResults().size()) {
			ds.getResults()
					.get(i)
					.setResourcesBuyers(
							resourcesUnionService.getByObjSerNo(
									ds.getResults().get(i).getSerNo(),
									Database.class).getResourcesBuyers());
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
			if (StringUtils.isNotEmpty(getEntity().getDbChtTitle())) {
				getEntity().setDbChtTitle(getEntity().getDbChtTitle().trim());
			}

			if (StringUtils.isNotEmpty(getEntity().getDbEngTitle())) {
				getEntity().setDbEngTitle(getEntity().getDbEngTitle().trim());
			}

			database = databaseService.save(getEntity(), getLoginUser());

			resourcesBuyers = resourcesBuyersService
					.save(new ResourcesBuyers(getRequest().getParameter(
							"resourcesBuyers.startDate"), getRequest()
							.getParameter("resourcesBuyers.maturityDate"),
							Category.valueOf(getRequest().getParameter(
									"rCategory")), Type.valueOf(getRequest()
									.getParameter("rType")), database
									.getDbChtTitle(), database.getDbEngTitle()),
							getLoginUser());

			int i = 0;
			while (i < cusSerNo.length) {
				resourcesUnionService.save(
						new ResourcesUnion(customerService.getBySerNo(Long
								.parseLong(cusSerNo[i])), resourcesBuyers, 0L,
								database.getSerNo(), 0L), getLoginUser());

				i++;
			}

			resourcesUnion = resourcesUnionService.getByObjSerNo(
					database.getSerNo(), Database.class);
			database.setResourcesBuyers(resourcesUnion.getResourcesBuyers());

			List<ResourcesUnion> resourceUnions = resourcesUnionService
					.getResourcesUnionsByObj(database, Database.class);
			List<Customer> customers = new ArrayList<Customer>();

			Iterator<ResourcesUnion> iterator = resourceUnions.iterator();
			while (iterator.hasNext()) {
				resourcesUnion = iterator.next();
				customers.add(resourcesUnion.getCustomer());
			}

			database.setCustomers(customers);
			setEntity(database);
			return VIEW;
		} else {
			List<Category> categoryList = new ArrayList<Category>(
					Arrays.asList(Category.values()));
			categoryList.remove(categoryList.size() - 1);

			List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type
					.values()));

			getRequest().setAttribute("categoryList", categoryList);
			getRequest().setAttribute("typeList", typeList);
			getRequest().setAttribute("allCustomers",
					customerService.getAllCustomers());

			List<Customer> customers = new ArrayList<Customer>();
			if (ArrayUtils.isNotEmpty(cusSerNo)) {
				int i = 0;
				while (i < cusSerNo.length) {
					customers.add(customerService.getBySerNo(Long
							.parseLong(cusSerNo[i])));
					i++;
				}
			}

			database = getEntity();
			database.setCustomers(customers);
			setEntity(database);
			return ADD;
		}
	}

	@Override
	public String update() throws Exception {
		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			if (StringUtils.isNotEmpty(getEntity().getDbChtTitle())) {
				getEntity().setDbChtTitle(getEntity().getDbChtTitle().trim());
			}

			if (StringUtils.isNotEmpty(getEntity().getDbEngTitle())) {
				getEntity().setDbEngTitle(getEntity().getDbEngTitle().trim());
			}

			database = databaseService.update(getEntity(), getLoginUser());

			resourcesBuyers = resourcesUnionService.getByObjSerNo(
					database.getSerNo(), Database.class).getResourcesBuyers();
			resourcesBuyers.setStartDate(getRequest().getParameter(
					"resourcesBuyers.startDate"));
			resourcesBuyers.setMaturityDate(getRequest().getParameter(
					"resourcesBuyers.maturityDate"));
			resourcesBuyers.setrCategory(Category.valueOf(getRequest()
					.getParameter("rCategory")));
			resourcesBuyers.setrType(Type.valueOf(getRequest().getParameter(
					"rType")));
			resourcesBuyers.setDbChtTitle(database.getDbChtTitle());
			resourcesBuyers.setDbEngTitle(database.getDbEngTitle());
			resourcesBuyersService.update(resourcesBuyers, getLoginUser());

			List<ResourcesUnion> resourcesUnions = resourcesUnionService
					.getResourcesUnionsByObj(database, Database.class);

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
				if (!resourcesUnionService.isExist(database, Database.class,
						Long.parseLong(cusSerNo[i]))) {
					resourcesUnionService.save(
							new ResourcesUnion(customerService.getBySerNo(Long
									.parseLong(cusSerNo[i])), resourcesBuyers,
									0L, database.getSerNo(), 0L),
							getLoginUser());
				}

				i++;
			}

			resourcesUnion = resourcesUnionService.getByObjSerNo(
					database.getSerNo(), Database.class);
			database.setResourcesBuyers(resourcesUnion.getResourcesBuyers());

			List<ResourcesUnion> resourceUnions = resourcesUnionService
					.getResourcesUnionsByObj(database, Database.class);
			List<Customer> customers = new ArrayList<Customer>();

			iterator = resourceUnions.iterator();
			while (iterator.hasNext()) {
				resourcesUnion = iterator.next();
				customers.add(resourcesUnion.getCustomer());
			}

			database.setCustomers(customers);
			setEntity(database);
			return VIEW;
		} else {
			List<Category> categoryList = new ArrayList<Category>(
					Arrays.asList(Category.values()));
			categoryList.remove(categoryList.size() - 1);

			List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type
					.values()));

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

			database = getEntity();
			database.setCustomers(customers);
			setEntity(database);
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
						.getResourcesUnionsByObj(databaseService
								.getBySerNo(Long.parseLong(checkItem[j])),
								Database.class);
				resourcesUnion = resourcesUnions.get(0);

				Iterator<ResourcesUnion> iterator = resourcesUnions.iterator();
				while (iterator.hasNext()) {
					resourcesUnion = iterator.next();
					resourcesUnionService.deleteBySerNo(resourcesUnion
							.getSerNo());
				}

				resourcesBuyersService.deleteBySerNo(resourcesUnion
						.getResourcesBuyers().getSerNo());
				databaseService.deleteBySerNo(Long.parseLong(checkItem[j]));

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
					database.getSerNo(), Database.class);

			database.setResourcesBuyers(resourcesUnion.getResourcesBuyers());

			List<ResourcesUnion> resourceUnions = resourcesUnionService
					.getResourcesUnionsByObj(database, Database.class);
			List<Customer> customers = new ArrayList<Customer>();

			Iterator<ResourcesUnion> iterator = resourceUnions.iterator();
			while (iterator.hasNext()) {
				resourcesUnion = iterator.next();
				customers.add(resourcesUnion.getCustomer());
			}

			database.setCustomers(customers);
			getRequest().setAttribute("viewSerNo", getEntity().getSerNo());
			setEntity(database);
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
			String[] rowTitles = new String[13];
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

			LinkedHashSet<Database> originalData = new LinkedHashSet<Database>();
			Map<String, Database> checkRepeatRow = new LinkedHashMap<String, Database>();
			Map<String, String> checkErrorRow = new LinkedHashMap<String, String>();
			int normal = 0;

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				String[] rowValues = new String[13];
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

				List<Category> categoryList = new ArrayList<Category>(
						Arrays.asList(Category.values()));
				categoryList.remove(categoryList.size() - 1);

				String category = "";
				if (rowValues[9] == null || rowValues[9].trim().equals("")) {
					category = Category.未註明.getCategory();
				} else {
					boolean isLegalCategory = false;
					for (int j = 0; j < categoryList.size(); j++) {
						if (rowValues[9].trim().equals(
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
				if (rowValues[10] == null || rowValues[10].trim().equals("")) {
					type = Type.資料庫.getType();
				} else {
					boolean isLegalType = false;
					for (int j = 0; j < typeList.size(); j++) {
						if (rowValues[10].trim().equals(
								typeList.get(j).getType())) {
							type = typeList.get(j).getType();
							isLegalType = true;
						}
					}

					if (!isLegalType) {
						type = Type.資料庫.getType();
					}
				}

				resourcesBuyers = new ResourcesBuyers(rowValues[7],
						rowValues[8], Category.valueOf(category),
						Type.valueOf(type), rowValues[0], rowValues[1]);

				database = new Database(rowValues[0].trim(),
						rowValues[1].trim(), rowValues[3], rowValues[4],
						rowValues[2], rowValues[5], rowValues[6], "", "", "",
						resourcesBuyers, null, "");

				customer = new Customer();
				customer.setName(rowValues[11].trim());
				customer.setEngName(rowValues[12].trim());

				List<Customer> customers = new ArrayList<Customer>();
				customers.add(customer);
				database.setCustomers(customers);

				long cusSerNo = customerService.getCusSerNoByName(rowValues[11]
						.trim());

				if (cusSerNo != 0) {
					if (StringUtils.isNotBlank(database.getDbChtTitle())
							|| StringUtils.isNotBlank(database.getDbEngTitle())) {
						long datSerNoByChtName = databaseService
								.getDatSerNoByChtName(database.getDbChtTitle());
						long datSerNoByEngName = databaseService
								.getDatSerNoByEngName(database.getDbEngTitle());

						if (datSerNoByChtName != 0 && datSerNoByEngName != 0
								&& datSerNoByChtName == datSerNoByEngName) {
							if (resourcesUnionService.isExist(databaseService
									.getBySerNo(datSerNoByChtName),
									Database.class, cusSerNo)) {
								database.setExistStatus("已存在");
							}

						} else if (datSerNoByChtName != 0
								&& datSerNoByEngName != 0
								&& datSerNoByChtName != datSerNoByEngName) {
							database.setExistStatus("資料庫名稱混亂");

						} else if (datSerNoByChtName == 0
								&& datSerNoByEngName != 0) {
							if (databaseService.getDatSerNoByBothName(
									database.getDbChtTitle(),
									database.getDbEngTitle()) == 0) {
								database.setExistStatus("資料庫名稱混亂");

							} else if (resourcesUnionService.isExist(
									databaseService
											.getBySerNo(datSerNoByEngName),
									Database.class, cusSerNo)) {
								database.setExistStatus("已存在");
							}

						} else if (datSerNoByChtName != 0
								&& datSerNoByEngName == 0) {
							if (databaseService.getDatSerNoByBothName(
									database.getDbChtTitle(),
									database.getDbEngTitle()) == 0) {
								database.setExistStatus("資料庫名稱混亂");

							} else if (resourcesUnionService.isExist(
									databaseService
											.getBySerNo(datSerNoByChtName),
									Database.class, cusSerNo)) {
								database.setExistStatus("已存在");
							}

						} else {
							if (database.getResourcesBuyers().getrCategory()
									.equals(Category.不明)) {
								database.setExistStatus("資源類型不明");
							}
						}

					} else {
						database.setExistStatus("資料庫名稱空白");

					}
				} else {
					database.setExistStatus("無此客戶");

				}

				if (StringUtils.isNotEmpty(database.getUrl())) {
					if (!isURL(database.getUrl())) {
						database.setUrl(null);
					}
				}

				if (database.getExistStatus().equals("")) {
					database.setExistStatus("正常");
				}

				if (database.getExistStatus().equals("正常")
						&& !originalData.contains(database)) {

					if (checkRepeatRow.containsKey(database.getDbChtTitle()
							+ database.getDbEngTitle() + customer.getName())) {
						database.setExistStatus("資料重複");

					} else if (checkErrorRow.containsKey(database
							.getDbEngTitle())
							&& !checkErrorRow.get(database.getDbEngTitle())
									.equals(database.getDbChtTitle()
											+ database.getDbEngTitle())) {
						database.setExistStatus("不能新增");

					} else if (checkErrorRow.containsKey(database
							.getDbChtTitle())
							&& !checkErrorRow.get(database.getDbChtTitle())
									.equals(database.getDbChtTitle()
											+ database.getDbEngTitle())) {
						database.setExistStatus("不能新增");
					} else {
						checkRepeatRow.put(
								database.getDbChtTitle()
										+ database.getDbEngTitle()
										+ customer.getName(), database);
						if (StringUtils.isNotBlank(database.getDbEngTitle())) {
							checkErrorRow.put(
									database.getDbEngTitle(),
									database.getDbChtTitle()
											+ database.getDbEngTitle());
						}

						if (StringUtils.isNotBlank(database.getDbChtTitle())) {
							checkErrorRow.put(
									database.getDbChtTitle(),
									database.getDbChtTitle()
											+ database.getDbEngTitle());
						}

						++normal;
					}
				}

				originalData.add(database);
			}

			List<Database> excelData = new ArrayList<Database>(originalData);

			DataSet<Database> ds = initDataSet();
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
			return null;
		}

		clearCheckedItem();

		DataSet<Database> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int i = 0;
		while (i < importList.size()) {
			if (i >= first && i < last) {
				ds.getResults().add((Database) importList.get(i));
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
					ds.getResults().add((Database) importList.get(j));
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

		if (ArrayUtils.isNotEmpty(importSerNos)) {
			if (NumberUtils.isDigits(importSerNos[0])) {
				if (!checkItemSet.contains(Integer.parseInt(importSerNos[0]))) {
					if (((Database) importList.get(Integer
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
		Set<String> deRepeatSet = new HashSet<String>(
				Arrays.asList(importSerNos));
		importSerNos = deRepeatSet.toArray(new String[deRepeatSet.size()]);

		if (ArrayUtils.isNotEmpty(importSerNos)) {
			int i = 0;
			while (i < importSerNos.length) {
				if (NumberUtils.isDigits(importSerNos[i])) {
					if (Long.parseLong(importSerNos[i]) < importList.size()) {
						if (((Database) importList.get(Integer
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
				database = (Database) importList.get(index);

				long datSerNo = databaseService.getDatSerNoByBothName(
						database.getDbChtTitle(), database.getDbEngTitle());
				long cusSerNo = customerService.getCusSerNoByName(database
						.getCustomers().get(0).getName());

				if (datSerNo == 0) {
					resourcesBuyers = resourcesBuyersService.save(
							database.getResourcesBuyers(), getLoginUser());
					database = databaseService.save(database, getLoginUser());

					resourcesUnionService.save(new ResourcesUnion(
							customerService.getBySerNo(cusSerNo),
							resourcesBuyers, 0L, database.getSerNo(), 0L),
							getLoginUser());
				} else {
					resourcesUnion = resourcesUnionService.getByObjSerNo(
							datSerNo, Database.class);
					resourcesUnionService.save(new ResourcesUnion(
							customerService.getBySerNo(cusSerNo),
							resourcesUnion.getResourcesBuyers(), 0L, datSerNo,
							0L), getLoginUser());
				}

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
		getEntity().setReportFile("database_sample.xlsx");

		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("database");
		// Create row object
		XSSFRow row;
		// This data needs to be written (Object[])
		Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
		empinfo.put("1", new Object[] { "資料庫中文題名", "資料庫英文題名",
				"publishname/出版社", "語文", "IncludedSpecies/收錄種類",
				"Content/收錄內容", "URL", "起始日", "到期日", "資源類型", "資源種類", "購買單位名稱",
				"購買單位英文名稱" });

		empinfo.put("2", new Object[] { "BMJ 醫學期刊", "BMJ  Journal",
				"The BMJ Publishing Group Ltd", "eng", "", "", "", "N/A",
				"N/A", "租貸", " 資料庫", "衛生福利部基隆醫院", "" });
		empinfo.put("2", new Object[] { "BMJ 醫學期刊", "BMJ  Journal",
				"The BMJ Publishing Group Ltd", "eng", "", "", "", "N/A",
				"N/A", "租貸", " 資料庫", "衛生福利部臺北醫院", "" });

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

	public boolean isURL(String url) {
		String urlPattern = "(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?(https://)?[a-zA-Z_0-9\\-]+(\\.\\w[`~!@#$%^&*()_-{[}]|;:<>?,./a-zA-Z0-9\u0000-\uffff\\+=]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?";

		return Pattern.compile(urlPattern).matcher(url).matches();
	}

	public boolean hasEntity() throws Exception {
		if (getEntity().getSerNo() == null) {
			getEntity().setSerNo(-1L);
			return false;
		}

		database = databaseService.getBySerNo(getEntity().getSerNo());
		if (database == null) {
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
