package com.shouyang.syazs.module.apply.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
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
import org.apache.commons.lang3.math.NumberUtils;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.collect.Lists;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;
import com.shouyang.syazs.module.apply.enums.Category;
import com.shouyang.syazs.module.apply.enums.Type;
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

	@Override
	protected void validateSave() throws Exception {

		if (StringUtils.isBlank(getEntity().getDbTitle())) {
			errorMessages.add("沒有資料庫名稱");
		}

		if (StringUtils.isBlank(getEntity().getPublishName())) {
			errorMessages.add("沒有出版社名稱");
		}

		if (errorMessages.size() == 0) {
			if (hasRepeatDb(getEntity().getDbTitle(), getEntity()
					.getPublishName(), getEntity().getSerNo())) {
				errorMessages.add("資料庫重複");
			}
		}

		if (!isURL(getEntity().getUrl())) {
			errorMessages.add("URL格式不正確");
		}

		if (StringUtils.isNotEmpty(getRequest()
				.getParameter("entity.startDate"))) {
			if (getEntity().getStartDate() == null) {
				errorMessages.add("起始日不正確");
			}
		}

		if (StringUtils.isNotEmpty(getRequest().getParameter(
				"entity.maturityDate"))) {
			if (getEntity().getMaturityDate() == null) {
				errorMessages.add("到期日不正確");
			}
		}

		if (getEntity().getStartDate() != null
				&& getEntity().getMaturityDate() != null) {
			if (getEntity().getStartDate().isAfter(
					getEntity().getMaturityDate())) {
				errorMessages.add("到期日早於起始日");
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
			if (StringUtils.isBlank(getEntity().getDbTitle())) {
				errorMessages.add("沒有資料庫名稱");
			}

			if (StringUtils.isBlank(getEntity().getPublishName())) {
				errorMessages.add("沒有出版社名稱");
			}

			if (errorMessages.size() == 0) {
				if (hasRepeatDb(getEntity().getDbTitle(), getEntity()
						.getPublishName(), getEntity().getSerNo())) {
					errorMessages.add("資料庫重複");
				}
			}

			if (!isURL(getEntity().getUrl())) {
				errorMessages.add("URL格式不正確");
			}

			if (StringUtils.isNotEmpty(getRequest().getParameter(
					"entity.startDate"))) {
				if (getEntity().getStartDate() == null) {
					errorMessages.add("起始日不正確");
				}
			}

			if (StringUtils.isNotEmpty(getRequest().getParameter(
					"entity.maturityDate"))) {
				if (getEntity().getMaturityDate() == null) {
					errorMessages.add("到期日不正確");
				}
			}

			if (getEntity().getStartDate() != null
					&& getEntity().getMaturityDate() != null) {
				if (getEntity().getStartDate().isAfter(
						getEntity().getMaturityDate())) {
					errorMessages.add("到期日早於起始日");
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
		setEntity(database);
		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
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
			Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
					.doubleValue()
					/ ds.getPager().getRecordPerPage().doubleValue());
			ds.getPager().setCurrentPage(lastPage.intValue());
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
			database = databaseService.save(getEntity(), getLoginUser());
			setEntity(database);
			addActionMessage("新增成功");
			return VIEW;
		} else {
			setEntity(getEntity());
			return ADD;
		}
	}

	@Override
	public String update() throws Exception {
		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			getEntity().setDbTitle(getEntity().getDbTitle().trim());

			database = databaseService.update(getEntity(), getLoginUser(),
					"uuIdentifier");
			setEntity(database);
			addActionMessage("修改成功");
			return VIEW;
		} else {
			setEntity(getEntity());
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

			addActionMessage("刪除成功");

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
			setEntity(database);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return VIEW;
	}

	public String box() throws Exception {
		List<Database> allDbs = databaseService.getAllDbs();
		getRequest().setAttribute("resDbs", allDbs);
		return BOX;
	}

	public String tip() throws Exception {
		if (hasRepeatDbTitle(getEntity().getDbTitle(), getEntity().getSerNo())) {
			getRequest().setAttribute("tip", "有同名資料庫");
		}

		return TIP;
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
			if (gtMaxSize(getRequest(), 1024 * 1024 * 10)) {
				addActionError("檔案超過10MB，請分批");
			} else {
				if (!getFileMime(getEntity().getFile()[0],
						getEntity().getFileFileName()[0]).equals("text/csv")) {
					addActionError("檔案格式錯誤");
				}
			}
		}

		if (!hasActionErrors()) {
			List<String> cellNames = new ArrayList<String>();

			LinkedHashSet<Database> originalData = new LinkedHashSet<Database>();
			Map<String, Database> checkRepeatRow = new LinkedHashMap<String, Database>();

			int normal = 0;

			CSVReader reader = new CSVReader(new FileReader(getEntity()
					.getFile()[0]), ',');

			String[] row;
			int rowLength = 15;
			while ((row = reader.readNext()) != null) {
				if (row.length < rowLength) {
					String[] spaceArray = new String[rowLength - row.length];
					row = ArrayUtils.addAll(row, spaceArray);
				}

				for (int i = 0; i < rowLength; i++) {
					if (row[i] == null) {
						row[i] = "";
					} else {
						row[i] = row[i].trim();
					}
				}

				if (reader.getRecordsRead() == 1) {
					cellNames = Arrays.asList(row);
				} else {
					List<String> errorList = Lists.newArrayList();

					Type type = null;
					Object objType = toEnum(row[9], Type.class);
					if (objType != null) {
						type = (Type) objType;
					} else {
						type = Type.資料庫;
					}

					boolean openAccess = false;
					if (StringUtils.isNotBlank(row[11])) {
						if (row[11].equals("1")
								|| row[11].toLowerCase().equals("yes")
								|| row[11].toLowerCase().equals("true")
								|| row[11].equals("是") || row[11].equals("真")) {
							openAccess = true;
						}
					}

					Category category = null;
					if (NumberUtils.isDigits(row[14])) {
						category = Category.getByToken(Integer
								.parseInt(row[14]));
					} else {
						category = (Category) toEnum(row[14], Category.class);
					}

					if (category == null) {
						category = Category.未註明;
					}

					resourcesBuyers = new ResourcesBuyers(category);

					database = new Database(row[0], row[1], row[2], row[3],
							row[4], row[5], row[6], row[7], row[8], type,
							row[10], openAccess, toLocalDateTime(row[12]),
							toLocalDateTime(row[13]), null, resourcesBuyers);

					if (StringUtils.isNotBlank(row[12])
							&& database.getStartDate() == null) {
						errorList.add("起始日錯誤");
					}

					if (StringUtils.isNotBlank(row[13])
							&& database.getMaturityDate() == null) {
						errorList.add("到期日錯誤");
					}

					if (database.getStartDate() != null
							&& database.getMaturityDate() != null) {
						if (database.getStartDate().isAfter(
								database.getMaturityDate())) {
							errorList.add("到期日早於起始日");
						}
					}

					if (StringUtils.isBlank(database.getDbTitle())) {
						errorList.add("沒有資料庫名稱");
					}

					if (StringUtils.isBlank(database.getPublishName())) {
						errorList.add("沒有出版社名稱");
					}

					if (StringUtils.isNotBlank(database.getDbTitle())
							&& StringUtils
									.isNotBlank(database.getPublishName())) {
						if (hasRepeatDb(database.getDbTitle(),
								database.getPublishName(), null)) {
							errorList.add("資料庫重複");
						}
					}

					if (!isURL(database.getUrl())) {
						errorList.add("URL未填寫或不正確");
					}

					if (errorList.size() == 0) {
						if (StringUtils.isNotBlank(database.getDbTitle())
								&& StringUtils.isNotBlank(database
										.getPublishName())) {
							if (checkRepeatRow.containsKey(database
									.getDbTitle() + database.getPublishName())) {
								errorList.add("清單資源重複");
							} else {
								checkRepeatRow.put(database.getDbTitle()
										+ database.getPublishName(), database);
							}
						}
					}

					if (errorList.size() == 0) {
						database.setDataStatus("正常");
						++normal;
					} else {
						database.setDataStatus(errorList.toString()
								.replace("[", "").replace("]", ""));
					}

					originalData.add(database);
				}
			}

			List<Database> csvData = new ArrayList<Database>(originalData);

			DataSet<Database> ds = initDataSet();
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

		DataSet<Database> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int index = first;
		while (index >= first && index < last) {
			if (index < importList.size()) {
				ds.getResults().add((Database) importList.get(index));
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
					ds.getResults().add((Database) importList.get(index));
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

	public String example() throws Exception {
		List<String[]> rows = new ArrayList<String[]>();
		rows.add(new String[] { "資料庫題名", "語文", "收錄種類", "出版社", "內容", "主題", "分類",
				"收錄年代", "全文取得授權刊期", "資源種類", "URL", "開放近用", "起始日", "到期日", "資源類型" });
		rows.add(new String[] {
				"遠足台灣(台灣行旅)電子書",
				"中文",
				"電子書",
				"碩亞數碼",
				"本選輯專為台灣地理空間、人文風情與歷史社會風貌打造的平台檢索系統，精心挑選遠足文化出版社最具代表性的出版品，並經由台灣百餘位學界教授、地方文史工作者、公部門專業研究員共同編撰，以豐富的數位內容與專業平台檢索系統的結合，引領讀者按圖索驥，開啟「台灣學」新視野。 @內容皆採全文本(pure –efile)格式製作，可支援關鍵字全文檢索。@提供讀者二大閱讀模式：(1)【下載】離線閱讀授權範圍內下載並安裝”L&B專屬之SMART Reader閱讀器”至您的桌機/筆電，即使無法網際網路連線，也能進行閱讀、管理下載書目(離線閱讀檔案共可使用30天)。運用章節標引導航、全文檢索、文字引用及底線等標註多樣化常用文具，為使用者節省並增加資訊檢索的正確率，有效提升學術研究、主題討論之品質。(2)【線上閱讀】連線閱讀：Flash翻頁式電子書@本平臺之電子書不限制同時使用人數，目前提供約60本電子書。",
				"台灣行旅", "地理、人文、歷史 、社會", "N/A", "N/A", "電子書",
				"http://lb20.tpml.libraryandbook.net/FE", "1", "2015-05-10",
				"", "0" });
		rows.add(new String[] { "Tesuka Manga手塚治虫系列漫畫電子書", "中文", "漫畫",
				"iGroup", "繁體中文12種157冊、日文15種377冊、英文6種55冊", "手塚治虫系列漫畫", "N/A",
				"N/A", "N/A", "電子書", "http://www.mymanga365.com/tezuka/", "0",
				"", "2015-10-15", "1" });
		rows.add(new String[] { "Nature Publish Group", "英文", "期刊",
				"nature.com", "", "Science & Medicine Journal", "N/A", "N/A",
				"N/A", "期刊", "http://www.nature.com/", "0", "", "2015-10-15",
				"2" });
		rows.add(new String[] { "台灣地理線上百科資料庫 ", "中文", "多媒體", "SYDT", "",
				"台灣地理", "N/A", "N/A", "N/A", "資料庫",
				"http://geo.twonline.libraryandbook.net/main.action", "0", "",
				"2015-10-15", "1" });

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos), ',',
				CSVWriter.DEFAULT_QUOTE_CHARACTER,
				CSVWriter.NO_ESCAPE_CHARACTER, "\n");

		writer.writeAll(rows);
		writer.close();

		getEntity().setReportFile("database_sample.csv");
		getEntity()
				.setInputStream(new ByteArrayInputStream(baos.toByteArray()));

		return XLSX;
	}

	protected boolean isURL(String url) {
		if (StringUtils.isNotEmpty(url)) {
			url = url.trim();
		}

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
		List<Long> results = databaseService.getSerNosByTitle(dbTitle);

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

	protected boolean hasRepeatDb(String dbTitle, String publishName, Long serNo)
			throws Exception {
		List<Long> results = databaseService.getByTitlePublishName(dbTitle,
				publishName);

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
}
