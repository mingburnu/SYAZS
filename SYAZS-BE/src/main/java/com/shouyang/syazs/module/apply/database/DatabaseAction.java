package com.shouyang.syazs.module.apply.database;

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
public class DatabaseAction extends GenericWebActionFull<Database> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5792761795605940212L;

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

	@Autowired
	private EnumConverter enumConverter;

	@Override
	protected void validateSave() throws Exception {
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

		if (!isURL(getEntity().getUrl())) {
			errorMessages.add("URL格式不正確");
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
			getEntity().getResourcesBuyers().setType(Type.資料庫);
		}
	}

	@Override
	protected void validateUpdate() throws Exception {
		if (!hasEntity()) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
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

			if (!isURL(getEntity().getUrl())) {
				errorMessages.add("URL格式不正確");
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
						getEntity().getCusSerNo()[i] = Long.MIN_VALUE;
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
				getEntity().getResourcesBuyers().setType(Type.資料庫);
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
						|| databaseService.getBySerNo(getEntity()
								.getCheckItem()[i]) == null) {
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
		database.setCustomers(customers);
		getRequest().setAttribute("uncheckCustomers",
				customerService.getUncheckCustomers(customers));
		database.getResourcesBuyers().setCategory(Category.未註明);
		database.getResourcesBuyers().setType(Type.資料庫);

		setEntity(database);

		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
			setCategoryList();

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
			database.setResourcesBuyers(resourcesBuyers);
			database.setCustomers(customers);
			getRequest().setAttribute("uncheckCustomers",
					customerService.getUncheckCustomers(customers));
			setEntity(database);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		if (StringUtils.isNotBlank(getEntity().getOption())) {
			if (!getEntity().getOption().equals("entity.dbChtTitle")
					&& !getEntity().getOption().equals("entity.dbEngTitle")) {
				getEntity().setOption("entity.dbChtTitle");
			}
		} else {
			getEntity().setOption("entity.dbChtTitle");
		}

		DataSet<Database> ds = databaseService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
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
			resourcesBuyers = resourcesBuyersService.save(getEntity()
					.getResourcesBuyers(), getLoginUser());

			int i = 0;
			while (i < getEntity().getCusSerNo().length) {
				resourcesUnionService.save(
						new ResourcesUnion(customerService
								.getBySerNo(getEntity().getCusSerNo()[i]),
								resourcesBuyers, 0L, database.getSerNo(), 0L),
						getLoginUser());

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
			setCategoryList();

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

			database = getEntity();
			database.setCustomers(customers);
			getRequest().setAttribute("uncheckCustomers",
					customerService.getUncheckCustomers(customers));
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

			resourcesBuyers = new ResourcesBuyers(getEntity()
					.getResourcesBuyers().getStartDate(), getEntity()
					.getResourcesBuyers().getMaturityDate(), getEntity()
					.getResourcesBuyers().getCategory(), getEntity()
					.getResourcesBuyers().getType(), database.getDbChtTitle(),
					database.getDbEngTitle());
			resourcesBuyers.setSerNo(resourcesUnionService
					.getByObjSerNo(database.getSerNo(), Database.class)
					.getResourcesBuyers().getSerNo());
			resourcesBuyersService.update(resourcesBuyers, getLoginUser());

			List<ResourcesUnion> resourcesUnions = resourcesUnionService
					.getResourcesUnionsByObj(database, Database.class);

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
				if (!resourcesUnionService.isExist(database, Database.class,
						getEntity().getCusSerNo()[i])) {
					resourcesUnionService.save(
							new ResourcesUnion(customerService
									.getBySerNo(getEntity().getCusSerNo()[i]),
									resourcesBuyers, 0L, database.getSerNo(),
									0L), getLoginUser());
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
			setCategoryList();

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

			database = getEntity();
			database.setCustomers(customers);
			getRequest().setAttribute("uncheckCustomers",
					customerService.getUncheckCustomers(customers));
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
			while (j < getEntity().getCheckItem().length) {
				List<ResourcesUnion> resourcesUnions = resourcesUnionService
						.getResourcesUnionsByObj(databaseService
								.getBySerNo(getEntity().getCheckItem()[j]),
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
				databaseService.deleteBySerNo(getEntity().getCheckItem()[j]);

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

				String category = "";
				if (StringUtils.isBlank(rowValues[9])) {
					category = Category.未註明.getCategory();
				} else {
					Object object = getEnum(
							new String[] { rowValues[9].trim() },
							Category.class);
					if (object != null) {
						category = rowValues[9].trim();
					} else {
						category = Category.不明.getCategory();
					}
				}

				String type = "";
				if (StringUtils.isBlank(rowValues[10])) {
					type = Type.資料庫.getType();
				} else {
					Object object = getEnum(
							new String[] { rowValues[10].trim() }, Type.class);
					if (object != null) {
						type = rowValues[10].trim();
					} else {
						type = Type.資料庫.getType();
					}
				}

				resourcesBuyers = new ResourcesBuyers(rowValues[7],
						rowValues[8], Category.valueOf(category),
						Type.valueOf(type), rowValues[0], rowValues[1]);

				customer = new Customer();
				customer.setName(rowValues[11].trim());
				customer.setEngName(rowValues[12].trim());

				List<Customer> customers = new ArrayList<Customer>();
				customers.add(customer);

				database = new Database(rowValues[0].trim(),
						rowValues[1].trim(), rowValues[3], rowValues[4],
						rowValues[2], rowValues[5], rowValues[6], "", "", "");
				database.setResourcesBuyers(resourcesBuyers);
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
								database.setDataStatus("已存在");
							}

						} else if (datSerNoByChtName != 0
								&& datSerNoByEngName != 0
								&& datSerNoByChtName != datSerNoByEngName) {
							database.setDataStatus("資料庫名稱混亂");

						} else if (datSerNoByChtName == 0
								&& datSerNoByEngName != 0) {
							if (databaseService.getDatSerNoByBothName(
									database.getDbChtTitle(),
									database.getDbEngTitle()) == 0) {
								database.setDataStatus("資料庫名稱混亂");

							} else if (resourcesUnionService.isExist(
									databaseService
											.getBySerNo(datSerNoByEngName),
									Database.class, cusSerNo)) {
								database.setDataStatus("已存在");
							}

						} else if (datSerNoByChtName != 0
								&& datSerNoByEngName == 0) {
							if (databaseService.getDatSerNoByBothName(
									database.getDbChtTitle(),
									database.getDbEngTitle()) == 0) {
								database.setDataStatus("資料庫名稱混亂");

							} else if (resourcesUnionService.isExist(
									databaseService
											.getBySerNo(datSerNoByChtName),
									Database.class, cusSerNo)) {
								database.setDataStatus("已存在");
							}

						} else {
							if (database.getResourcesBuyers().getCategory()
									.equals(Category.不明)) {
								database.setDataStatus("資源類型不明");
							}
						}

					} else {
						database.setDataStatus("資料庫名稱空白");

					}
				} else {
					database.setDataStatus("無此客戶");

				}

				if (!isURL(database.getUrl())) {
					database.setUrl(null);
				}

				if (database.getDataStatus() == null) {
					database.setDataStatus("正常");
				}

				if (database.getDataStatus().equals("正常")
						&& !originalData.contains(database)) {

					if (checkRepeatRow.containsKey(database.getDbChtTitle()
							+ database.getDbEngTitle() + customer.getName())) {
						database.setDataStatus("資料重複");

					} else if (checkErrorRow.containsKey(database
							.getDbEngTitle())
							&& !checkErrorRow.get(database.getDbEngTitle())
									.equals(database.getDbChtTitle()
											+ database.getDbEngTitle())) {
						database.setDataStatus("不能新增");

					} else if (checkErrorRow.containsKey(database
							.getDbChtTitle())
							&& !checkErrorRow.get(database.getDbChtTitle())
									.equals(database.getDbChtTitle()
											+ database.getDbEngTitle())) {
						database.setDataStatus("不能新增");
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
			return IMPORT;
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
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
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
					if (((Database) importList
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
					if (((Database) importList
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
		getEntity()
				.setInputStream(new ByteArrayInputStream(boas.toByteArray()));

		return XLSX;
	}

	protected boolean isURL(String url) {
		return ESAPI.validator().isValidInput("Database URL", url, "URL",
				Integer.MAX_VALUE, true);
	}

	protected void setCategoryList() {
		List<Category> categoryList = new ArrayList<Category>(
				Arrays.asList(Category.values()));
		categoryList.remove(categoryList.size() - 1);
		ActionContext.getContext().getValueStack()
				.set("categoryList", categoryList);
	}

	protected boolean hasEntity() throws Exception {
		if (!getEntity().hasSerNo()) {
			return false;
		}

		database = databaseService.getBySerNo(getEntity().getSerNo());
		if (database == null) {
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

	@SuppressWarnings("rawtypes")
	protected Object getEnum(String[] values, Class toClass) {
		return enumConverter.convertFromString(null, values, toClass);
	}
}
