package com.shouyang.syazs.module.apply.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.shouyang.syazs.core.converter.EnumConverter;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;
import com.shouyang.syazs.module.apply.enums.Category;
import com.shouyang.syazs.module.apply.enums.Type;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwner;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwnerService;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;

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
	private ResourcesBuyers resourcesBuyers;

	@Autowired
	private ReferenceOwner referenceOwner;

	@Autowired
	private ReferenceOwnerService referenceOwnerService;

	@Autowired
	private EnumConverter enumConverter;

	@Override
	protected void validateSave() throws Exception {
		if (StringUtils.isBlank(getEntity().getDbTitle())) {
			errorMessages.add("沒有資料庫名稱");
		}

		if (!isURL(getEntity().getUrl())) {
			errorMessages.add("URL格式不正確");
		}

		if (ArrayUtils.isEmpty(getEntity().getRefSerNo())) {
			errorMessages.add("至少選擇一筆以上擁有人");
		} else {
			Set<Long> deRepeatSet = new HashSet<Long>(Arrays.asList(getEntity()
					.getRefSerNo()));
			getEntity().setRefSerNo(
					deRepeatSet.toArray(new Long[deRepeatSet.size()]));
			getEntity().setOwners(new LinkedList<ReferenceOwner>());

			int i = 0;
			while (i < getEntity().getRefSerNo().length) {
				if (getEntity().getRefSerNo()[i] == null) {
					errorMessages.add("null為不可利用的流水號");
				} else {
					if (getEntity().getRefSerNo()[i] < 1) {
						errorMessages.add(getEntity().getRefSerNo()[i]
								+ "為不可利用的流水號");
					} else {
						Object[] ownerValue = referenceOwnerService
								.getOwnerBySerNo(getEntity().getRefSerNo()[i]);
						if (ownerValue == null) {
							errorMessages.add(getEntity().getRefSerNo()[i]
									+ "為不可利用的流水號");
						} else {
							referenceOwner = new ReferenceOwner();
							referenceOwner
									.setSerNo(getEntity().getRefSerNo()[i]);
							referenceOwner.setName(ownerValue[1].toString());

							getEntity().getOwners().add(referenceOwner);
						}
					}
				}

				i++;
			}
		}

		if (getEntity().getResourcesBuyers().getCategory() == null) {
			getEntity().getResourcesBuyers().setCategory(Category.未註明);
		}

		if (getEntity().getType() == null) {
			getEntity().setType(Type.資料庫);
		}

		if (getEntity().getOpenAccess() == null) {
			getEntity().setOpenAccess(false);
		}
	}

	@Override
	protected void validateUpdate() throws Exception {
		if (!hasEntity()) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			if (StringUtils.isBlank(getEntity().getDbTitle())
					&& StringUtils.isBlank(getEntity().getDbTitle())) {
				errorMessages.add("沒有資料庫名稱");
			}

			if (!isURL(getEntity().getUrl())) {
				errorMessages.add("URL格式不正確");
			}

			if (ArrayUtils.isEmpty(getEntity().getRefSerNo())) {
				errorMessages.add("至少選擇一筆以上擁有人");
			} else {
				Set<Long> deRepeatSet = new HashSet<Long>(
						Arrays.asList(getEntity().getRefSerNo()));
				getEntity().setRefSerNo(
						deRepeatSet.toArray(new Long[deRepeatSet.size()]));
				getEntity().setOwners(new LinkedList<ReferenceOwner>());

				int i = 0;
				while (i < getEntity().getRefSerNo().length) {
					if (getEntity().getRefSerNo()[i] == null) {
						errorMessages.add("null為不可利用的流水號");
					} else {
						if (getEntity().getRefSerNo()[i] < 1) {
							errorMessages.add(getEntity().getRefSerNo()[i]
									+ "為不可利用的流水號");
						} else {
							Object[] ownerValue = referenceOwnerService
									.getOwnerBySerNo(getEntity().getRefSerNo()[i]);
							if (ownerValue == null) {
								errorMessages.add(getEntity().getRefSerNo()[i]
										+ "為不可利用的流水號");
							} else {
								referenceOwner = new ReferenceOwner();
								referenceOwner.setSerNo(getEntity()
										.getRefSerNo()[i]);
								referenceOwner
										.setName(ownerValue[1].toString());

								getEntity().getOwners().add(referenceOwner);
							}
						}
					}

					i++;
				}
			}

			if (getEntity().getResourcesBuyers().getCategory() == null) {
				getEntity().getResourcesBuyers().setCategory(Category.未註明);
			}

			if (getEntity().getType() == null) {
				getEntity().setType(Type.資料庫);
			}

			if (getEntity().getOpenAccess() == null) {
				getEntity().setOpenAccess(false);
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
		getRequest().setAttribute(
				"uncheckReferenceOwners",
				referenceOwnerService
						.getUncheckOwners(new ArrayList<ReferenceOwner>()));

		setEntity(database);

		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
			List<ReferenceOwner> owners = databaseService
					.getcheckOwners(database.getSerNo());
			database.setOwners(owners);
			getRequest().setAttribute("uncheckReferenceOwners",
					referenceOwnerService.getUncheckOwners(owners));
			setEntity(database);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		if (StringUtils.isNotBlank(getEntity().getOption())) {
			if (!getEntity().getOption().equals("entity.dbTitle")
					&& !getEntity().getOption().equals("entity.uuIdentifier")) {
				getEntity().setOption("entity.dbTitle");
			}
		} else {
			getEntity().setOption("entity.dbTitle");
		}

		DataSet<Database> ds = databaseService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			ds = databaseService.getByRestrictions(ds);
		}

		setDs(ds);
		return LIST;
	}

	@Override
	public String save() throws Exception {
		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			getEntity().setDbTitle(getEntity().getDbTitle().trim());
			getEntity().setReferenceOwners(
					new HashSet<ReferenceOwner>(getEntity().getOwners()));

			database = databaseService.save(getEntity(), getLoginUser());
			setOwners();
			setEntity(database);
			addActionMessage("新增成功");
			return VIEW;
		} else {
			database = getEntity();

			getRequest().setAttribute(
					"uncheckReferenceOwners",
					referenceOwnerService.getUncheckOwners(getEntity()
							.getOwners()));
			setEntity(database);
			return ADD;
		}
	}

	@Override
	public String update() throws Exception {
		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			getEntity().setDbTitle(getEntity().getDbTitle().trim());

			getEntity().setReferenceOwners(
					new HashSet<ReferenceOwner>(getEntity().getOwners()));
			database = databaseService.update(getEntity(), getLoginUser(),
					"uuIdentifier");
			setOwners();
			setEntity(database);
			addActionMessage("修改成功");
			return VIEW;
		} else {
			database = getEntity();

			getRequest().setAttribute(
					"uncheckReferenceOwners",
					referenceOwnerService.getUncheckOwners(getEntity()
							.getOwners()));
			setEntity(database);
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
				databaseService.deleteBySerNo(getEntity().getCheckItem()[i]);
				i++;
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
			getRequest().setAttribute("viewSerNo", getEntity().getSerNo());
			setOwners();
			setEntity(database);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return VIEW;
	}

	public String box() throws Exception {
		List<Database> allDbs = databaseService.getAllDbs();
		Set<ReferenceOwner> owners = Sets.newHashSet();
		for (int i = 0; i < allDbs.size(); i++) {
			database = allDbs.get(i);
			List<Object[]> dataList = databaseService.getResOwners(database
					.getSerNo());
			for (int j = 0; j < dataList.size(); j++) {
				referenceOwner = new ReferenceOwner();
				referenceOwner.setSerNo((Long) dataList.get(j)[0]);
				referenceOwner.setName(dataList.get(j)[1].toString());
				owners.add(referenceOwner);
			}

			database.setReferenceOwners(owners);
		}

		getRequest().setAttribute("resDbs", allDbs);
		return BOX;
	}

	public String tip() throws Exception {
		Long serNo = getEntity().getSerNo();
		String dbTitle = getEntity().getDbTitle();

		if (hasRepeatDbTitle(dbTitle, serNo)) {
			getRequest().setAttribute("tip", "有同名資料庫");
		}

		return TIP;
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
			String[] rowTitles = new String[16];
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

			List<Database> originalData = new LinkedList<Database>();
			List<String> titles = databaseService.getAllDbTitles();
			Map<String, Integer> checkRepeatTitle = new HashMap<String, Integer>();
			int normal = 0;
			int tip = 0;

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				String[] rowValues = new String[16];
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

				List<String> errorList = Lists.newArrayList();

				Category category = null;
				Object objCategory = getEnum(
						new String[] { rowValues[14].trim() }, Category.class);
				if (objCategory != null) {
					category = (Category) objCategory;
				} else {
					category = Category.未註明;
				}

				Type type = null;
				Object objType = getEnum(new String[] { rowValues[9].trim() },
						Type.class);
				if (objType != null) {
					type = (Type) objType;
				} else {
					type = Type.資料庫;
				}

				boolean openAccess = false;
				if (rowValues[11].toLowerCase().equals("yes")
						|| rowValues[11].toLowerCase().equals("true")
						|| rowValues[11].equals("是")) {
					openAccess = true;
				}

				resourcesBuyers = new ResourcesBuyers(rowValues[12],
						rowValues[13], category);

				Set<ReferenceOwner> owners = new HashSet<ReferenceOwner>();
				if (StringUtils.isNotBlank(rowValues[15].replace(",", ""))) {
					String[] names = rowValues[15].trim().split(",");

					int j = 0;
					while (j < names.length) {
						if (StringUtils.isNotBlank(names[j])) {
							referenceOwner = new ReferenceOwner();
							referenceOwner.setName(names[j].trim());
							referenceOwner
									.setSerNo(referenceOwnerService
											.getRefSerNoByName(referenceOwner
													.getName()));

							if (referenceOwner.getSerNo() == 0) {
								errorList.add(referenceOwner.getName() + "不存在");
							}
							owners.add(referenceOwner);
						}
						j++;
					}

				}

				database = new Database(rowValues[0].trim(), rowValues[1],
						rowValues[2], rowValues[3], rowValues[4], rowValues[5],
						rowValues[6], rowValues[7], rowValues[8], type,
						rowValues[10], openAccess, null, resourcesBuyers,
						new HashSet<ReferenceOwner>(owners));
				database.getResourcesBuyers().setDataStatus("");

				if (CollectionUtils.isEmpty(database.getReferenceOwners())) {
					errorList.add("沒有擁有者");
				}

				if (StringUtils.isBlank(database.getDbTitle())) {
					errorList.add("沒有資料庫名稱");
				}

				if (!isURL(database.getUrl())) {
					errorList.add("url不正確");
				}

				if (titles.contains(database.getDbTitle().toLowerCase())) {
					database.getResourcesBuyers().setDataStatus("同名資料庫已存在<br>");
				}

				if (StringUtils.isNotBlank(database.getDbTitle())) {
					if (checkRepeatTitle.containsKey(database.getDbTitle())) {
						String prevResStatus = (originalData
								.get(checkRepeatTitle.get(database.getDbTitle()))
								.getResourcesBuyers().getDataStatus() + "清單有同名資料庫<br>")
								.replace("清單有同名資料庫<br>清單有同名資料庫<br>",
										"清單有同名資料庫<br>");
						originalData
								.get(checkRepeatTitle.get(database.getDbTitle()))
								.getResourcesBuyers()
								.setDataStatus(prevResStatus);

						String nextResStatus = database.getResourcesBuyers()
								.getDataStatus() + "清單有同名資料庫<br>";
						database.getResourcesBuyers().setDataStatus(
								nextResStatus);
					}

					checkRepeatTitle.put(database.getDbTitle(),
							originalData.size());
				}

				if (errorList.size() != 0) {
					database.setDataStatus(errorList.toString()
							.replace("[", "").replace("]", ""));
				} else {
					database.setDataStatus("正常");
					++normal;
				}

				originalData.add(database);
			}

			Iterator<Database> iterator = originalData.iterator();
			while (iterator.hasNext()) {
				database = iterator.next();
				if (StringUtils.isNotBlank(database.getResourcesBuyers()
						.getDataStatus())
						&& database.getDataStatus().equals("正常")) {
					++tip;
				}
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
			getSession().put("insert", 0);
			getSession().put("tip", tip);
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
			database = (Database) importList.get(i);
			if (database.getDataStatus().equals("正常")) {
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
				database = (Database) importList.get(index);
				database = databaseService.save(database, getLoginUser());
				database.setDataStatus("已匯入");
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

	public String backErrors() throws Exception {
		List<?> importList = (List<?>) getSession().get("importList");
		if (importList == null) {
			return IMPORT;
		}

		if (StringUtils.isBlank(getEntity().getOption())) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else if (getEntity().getOption().equals("errors")) {

			getEntity().setReportFile("database_error.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet spreadsheet = workbook.createSheet("database_error");
			XSSFRow row;

			Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();

			Integer mark = 1;
			empinfo.put("1", new Object[] { "資料庫題名", "語文", "收錄種類", "出版社", "內容",
					"主題", "分類", "收錄年代", "出版時間差", "資源種類", "URL", "公開資源", "起始日",
					"到期日", "資源類型", "購買人名稱", "錯誤提示", "其他提示" });

			int i = 0;
			while (i < importList.size()) {
				database = (Database) importList.get(i);
				if (!database.getDataStatus().equals("正常")
						&& !database.getDataStatus().equals("已匯入")) {
					List<String> ownerNames = Lists.newArrayList();
					for (ReferenceOwner owner : database.getReferenceOwners()) {
						ownerNames.add(owner.getName());
					}

					String tips = database.getResourcesBuyers().getDataStatus()
							.replace("<br>", ",");
					if (tips.length() > 0) {
						tips = tips.substring(0, tips.length() - 1);
					}

					mark = mark + 1;
					empinfo.put(
							mark.toString(),
							new Object[] {
									database.getDbTitle(),
									database.getLanguages(),
									database.getIncludedSpecies(),
									database.getPublishName(),
									database.getContent(),
									database.getTopic(),
									database.getClassification(),
									database.getIndexedYears(),
									database.getEmbargo(),
									database.getType().getType(),
									database.getUrl(),
									database.getOpenAccess().toString(),
									database.getResourcesBuyers()
											.getStartDate(),
									database.getResourcesBuyers()
											.getMaturityDate(),
									database.getResourcesBuyers().getCategory()
											.getCategory(),
									ownerNames.toString().substring(1,
											ownerNames.toString().length() - 1),
									database.getDataStatus(), tips });
				}
				i++;
			}

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
			getEntity().setInputStream(
					new ByteArrayInputStream(boas.toByteArray()));
		} else if (getEntity().getOption().equals("tips")) {
			getEntity().setReportFile("database_tip.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet spreadsheet = workbook.createSheet("database_tip");
			XSSFRow row;

			Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();

			Integer mark = 1;
			empinfo.put("1", new Object[] { "資料庫題名", "語文", "收錄種類", "出版社", "內容",
					"主題", "分類", "收錄年代", "出版時間差", "資源種類", "URL", "公開資源", "起始日",
					"到期日", "資源類型", "購買人名稱", "物件狀態", "其他提示" });

			int i = 0;
			while (i < importList.size()) {
				database = (Database) importList.get(i);
				if (database.getDataStatus().equals("正常")
						|| database.getDataStatus().equals("已匯入")) {
					List<String> ownerNames = Lists.newArrayList();
					for (ReferenceOwner owner : database.getReferenceOwners()) {
						ownerNames.add(owner.getName());
					}

					String tips = database.getResourcesBuyers().getDataStatus()
							.replace("<br>", ",");
					if (tips.length() > 0) {
						tips = tips.substring(0, tips.length() - 1);
					}

					mark = mark + 1;
					empinfo.put(
							mark.toString(),
							new Object[] {
									database.getDbTitle(),
									database.getLanguages(),
									database.getIncludedSpecies(),
									database.getPublishName(),
									database.getContent(),
									database.getTopic(),
									database.getClassification(),
									database.getIndexedYears(),
									database.getEmbargo(),
									database.getType().getType(),
									database.getUrl(),
									database.getOpenAccess().toString(),
									database.getResourcesBuyers()
											.getStartDate(),
									database.getResourcesBuyers()
											.getMaturityDate(),
									database.getResourcesBuyers().getCategory()
											.getCategory(),
									ownerNames.toString().substring(1,
											ownerNames.toString().length() - 1),
									database.getDataStatus(), tips });
				}
				i++;
			}

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
			getEntity().setInputStream(
					new ByteArrayInputStream(boas.toByteArray()));
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return XLSX;
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

		empinfo.put("1", new Object[] { "資料庫題名", "語文", "收錄種類", "出版社", "內容",
				"主題", "分類", "收錄年代", "出版時間差", "資源種類", "URL", "公開資源", "起始日",
				"到期日", "資源類型", "購買人名稱" });

		empinfo.put(
				"2",
				new Object[] {
						"遠足台灣(台灣行旅)電子書",
						"中文",
						"電子書",
						"碩亞數碼",
						"本選輯專為台灣地理空間、人文風情與歷史社會風貌打造的平台檢索系統，精心挑選遠足文化出版社最具代表性的出版品，並經由台灣百餘位學界教授、地方文史工作者、公部門專業研究員共同編撰，以豐富的數位內容與專業平台檢索系統的結合，引領讀者按圖索驥，開啟「台灣學」新視野。 @內容皆採全文本(pure –efile)格式製作，可支援關鍵字全文檢索。@提供讀者二大閱讀模式：(1)【下載】離線閱讀授權範圍內下載並安裝”L&B專屬之SMART Reader閱讀器”至您的桌機/筆電，即使無法網際網路連線，也能進行閱讀、管理下載書目(離線閱讀檔案共可使用30天)。運用章節標引導航、全文檢索、文字引用及底線等標註多樣化常用文具，為使用者節省並增加資訊檢索的正確率，有效提升學術研究、主題討論之品質。(2)【線上閱讀】連線閱讀：Flash翻頁式電子書@本平臺之電子書不限制同時使用人數，目前提供約60本電子書。",
						"台灣行旅", "地理、人文、歷史 、社會", "N/A", "N/A", "電子書",
						"http://lb20.tpml.libraryandbook.net/FE", "否", "N/A",
						"N/A", "租貸", "高雄醫學院附設醫院、疾病管制署" });
		empinfo.put("3", new Object[] { "Tesuka Manga手塚治虫系列漫畫電子書", "中文", "漫畫",
				"iGroup", "繁體中文12種157冊、日文15種377冊、英文6種55冊", "手塚治虫系列漫畫", "N/A",
				"N/A", "N/A", "電子書", "http://www.mymanga365.com/tezuka/", "否",
				"N/A", "N/A", "租貸", "高雄醫學院附設醫院" });

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
				Integer.MAX_VALUE, false);
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

	protected boolean hasRepeatDbTitle(String dbTitle, Long serNo)
			throws Exception {
		List<Long> results = databaseService.getSerNosByTitle(getEntity()
				.getDbTitle());

		if (serNo != null) {
			if (CollectionUtils.isNotEmpty(results)) {
				results.remove(serNo);
				if (results.size() != 0) {
					return true;
				}
			}
		} else {
			if (CollectionUtils.isNotEmpty(results)) {
				return true;
			}
		}

		return false;
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

	protected void setOwners() {
		getRequest().setAttribute("referenceOwners",
				databaseService.getResOwners(database.getSerNo()));
	}

	@SuppressWarnings("rawtypes")
	protected Object getEnum(String[] values, Class toClass) {
		return enumConverter.convertFromString(null, values, toClass);
	}
}
