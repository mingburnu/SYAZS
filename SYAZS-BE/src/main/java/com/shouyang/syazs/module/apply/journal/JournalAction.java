package com.shouyang.syazs.module.apply.journal;

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

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
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
import com.shouyang.syazs.module.apply.classification.Classification;
import com.shouyang.syazs.module.apply.classification.ClassificationService;
import com.shouyang.syazs.module.apply.database.Database;
import com.shouyang.syazs.module.apply.database.DatabaseService;
import com.shouyang.syazs.module.apply.enums.Category;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;

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
	private Database database;

	@Autowired
	private Classification classification;

	@Autowired
	private JournalService journalService;

	@Autowired
	private DatabaseService databaseService;

	@Autowired
	private ResourcesBuyers resourcesBuyers;

	@Autowired
	private ClassificationService classificationService;

	@Override
	protected void validateSave() throws Exception {
		if (StringUtils.isBlank(getEntity().getTitle())) {
			errorMessages.add("刊名不得空白");
		}

		if (StringUtils.isBlank(getEntity().getPublishName())) {
			errorMessages.add("沒有出版社名稱");
		}

		if (errorMessages.size() == 0) {
			if (hasRepeatJou(getEntity().getTitle(), getEntity()
					.getPublishName(), getEntity().getSerNo())) {
				errorMessages.add("電子期刊重複");
			}
		}

		if (StringUtils.isNotEmpty(getEntity().getIssn())) {
			if (getEntity().getIssn().trim().length() > 10) {
				errorMessages.add("ISSN長度超過");
			}
		}

		if (!isURL(getEntity().getUrl())) {
			errorMessages.add("URL必須填寫");
		}

		if (StringUtils.isNotEmpty(getRequest().getParameter(
				"entity.publishYear"))) {
			if (getEntity().getPublishYear() == null) {
				errorMessages.add("出版年不正確");
			}
		}

		if (getEntity().getDatabase().hasSerNo()) {
			database = databaseService.getBySerNo(getEntity().getDatabase()
					.getSerNo());
			if (database == null) {
				errorMessages.add("不可利用的流水號");
			} else {
				getEntity().setDatabase(database);
				getEntity().setResourcesBuyers(database.getResourcesBuyers());
			}
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

		if (getEntity().getClassification().hasSerNo()) {
			classification = classificationService.getBySerNo(getEntity()
					.getClassification().getSerNo());
			if (classification == null) {
				errorMessages.add("不可利用的分類法流水號");
			} else {
				getEntity().setClassification(classification);
			}
		} else {
			getEntity().setClassification(null);
		}

		if (getEntity().getResourcesBuyers().getCategory() == null) {
			getEntity().getResourcesBuyers().setCategory(Category.未註明);
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
			if (StringUtils.isBlank(getEntity().getTitle())) {
				errorMessages.add("刊名不得空白");
			}

			if (StringUtils.isBlank(getEntity().getPublishName())) {
				errorMessages.add("沒有出版社名稱");
			}

			if (errorMessages.size() == 0) {
				if (hasRepeatJou(getEntity().getTitle(), getEntity()
						.getPublishName(), getEntity().getSerNo())) {
					errorMessages.add("電子期刊重複");
				}
			}

			if (StringUtils.isNotEmpty(getEntity().getIssn())) {
				if (getEntity().getIssn().trim().length() > 10) {
					errorMessages.add("ISSN長度超過");
				}
			}

			if (!isURL(getEntity().getUrl())) {
				errorMessages.add("URL必須填寫");
			}

			if (getEntity().getDatabase().hasSerNo()) {
				database = databaseService.getBySerNo(getEntity().getDatabase()
						.getSerNo());
				if (database == null) {
					errorMessages.add("不可利用的流水號");
				} else {
					getEntity().setDatabase(database);
					getEntity().setResourcesBuyers(
							database.getResourcesBuyers());
				}
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

			if (getEntity().getClassification().hasSerNo()) {
				classification = classificationService.getBySerNo(getEntity()
						.getClassification().getSerNo());
				if (classification == null) {
					errorMessages.add("不可利用的分類法流水號");
				} else {
					getEntity().setClassification(classification);
				}
			} else {
				getEntity().setClassification(null);
			}

			if (getEntity().getResourcesBuyers().getCategory() == null) {
				getEntity().getResourcesBuyers().setCategory(Category.未註明);
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
		DataSet<Journal> ds = initDataSet();
		ds.setDatas(classificationService.getClsDatas());
		setEntity(journal);
		setDs(ds);

		setEntity(journal);
		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
			DataSet<Journal> ds = initDataSet();
			ds.setDatas(classificationService.getClsDatas());
			setEntity(journal);
			setDs(ds);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		if (StringUtils.isNotEmpty(getEntity().getOption())) {
			if (!getEntity().getOption().equals("entity.title")
					&& !getEntity().getOption().equals("entity.issn")) {
				getEntity().setOption("entity.title");
			}
		} else {
			getEntity().setOption("entity.title");
		}

		if (getEntity().getOption().equals("entity.title")) {
			getEntity().setIssn(null);
		} else {
			getEntity().setTitle(null);

			if (StringUtils.isNotBlank(getEntity().getIssn())) {
				if (!ISSN_Validator.isIssn(getEntity().getIssn())) {
					return LIST;
				}
			}
		}

		DataSet<Journal> ds = journalService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
					.doubleValue()
					/ ds.getPager().getRecordPerPage().doubleValue());
			ds.getPager().setCurrentPage(lastPage.intValue());
			ds = journalService.getByRestrictions(ds);
		}

		setDs(ds);
		return LIST;
	}

	@Override
	public String save() throws Exception {
		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			getEntity().setTitle(getEntity().getTitle().trim());
			getEntity()
					.setIssn(
							getEntity().getIssn().toUpperCase()
									.replace("-", "").trim());

			if (!getEntity().getDatabase().hasSerNo()) {
				getEntity().setDatabase(null);
			}

			if (StringUtils.isNotBlank(getEntity().getIssn())) {
				getEntity().setIssn(
						getEntity().getIssn().replace("-", "").toUpperCase());
			}

			journal = journalService.save(getEntity(), getLoginUser());
			setEntity(journal);
			addActionMessage("新增成功");
			return VIEW;
		} else {
			DataSet<Journal> ds = initDataSet();
			ds.setDatas(classificationService.getClsDatas());
			setEntity(getEntity());
			setDs(ds);
			return ADD;
		}
	}

	@Override
	public String update() throws Exception {
		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			getEntity().setTitle(getEntity().getTitle().trim());
			getEntity()
					.setIssn(
							getEntity().getIssn().toUpperCase()
									.replace("-", "").trim());

			if (!getEntity().getDatabase().hasSerNo()) {
				getEntity().setDatabase(null);
			}

			if (StringUtils.isNotBlank(getEntity().getIssn())) {
				getEntity().setIssn(
						getEntity().getIssn().replace("-", "").toUpperCase());
			}

			journal = journalService.update(getEntity(), getLoginUser());
			setEntity(journal);
			addActionMessage("修改成功");
			return VIEW;
		} else {
			DataSet<Journal> ds = initDataSet();
			ds.setDatas(classificationService.getClsDatas());
			setEntity(getEntity());
			setDs(ds);
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
				journalService.deleteBySerNo(getEntity().getCheckItem()[i]);
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
			setEntity(journal);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return VIEW;
	}

	public String tip() throws Exception {
		Long serNo = getEntity().getSerNo();
		Long datserNo = getEntity().getDatabase().getSerNo();
		if (datserNo != null) {
			database = databaseService.getBySerNo(datserNo);
		}

		if (StringUtils.isNotBlank(getEntity().getIssn())) {
			if (ISSN_Validator.isIssn(getEntity().getIssn())) {
				if (hasRepeatIssn(getEntity().getIssn(), serNo)) {
					getRequest().setAttribute("tip", "已有相同ISBN");
				}

				if (dbHasRepeatIssn(getEntity().getIssn(), serNo, database)) {
					getRequest().setAttribute("repeat", "資料庫有重複資源");
				}
			}
		} else {
			if (StringUtils.isNotBlank(getEntity().getTitle())) {
				if (hasRepeatTitle(getEntity().getTitle(), serNo)) {
					getRequest().setAttribute("tip", "相同名稱且無ISSN資源存在");
				}

				if (dbHasRepeatTitle(getEntity().getTitle(), serNo, database)) {
					getRequest().setAttribute("repeat", "資料庫有相同名稱且無ISSN資源存在");
				}
			}
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

		if (StringUtils.isBlank(getEntity().getOption())) {
			getEntity().setOption("package");
		} else {
			if (!getEntity().getOption().equals("package")
					&& !getEntity().getOption().equals("individual")) {
				getEntity().setOption("package");
			}
		}

		if (!hasActionErrors()) {
			List<String> cellNames = new ArrayList<String>();

			LinkedHashSet<Journal> originalData = new LinkedHashSet<Journal>();
			Map<String, Journal> checkRepeatRow = new LinkedHashMap<String, Journal>();
			BidiMap clsMap = new DualHashBidiMap(
					classificationService.getClsDatas());

			int normal = 0;

			CSVReader reader = new CSVReader(new FileReader(getEntity()
					.getFile()[0]), ',');

			String[] row;
			int rowLength = 19;
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

					Short publsihYear = (Short) toNumber(row[6], Short.class);

					boolean openAccess = false;
					if (StringUtils.isNotBlank(row[15])) {
						if (row[15].equals("1")
								|| row[15].toLowerCase().equals("yes")
								|| row[15].toLowerCase().equals("true")
								|| row[15].equals("是") || row[15].equals("真")) {
							openAccess = true;
						}
					}

					classification = new Classification();

					if (StringUtils.isNotBlank(row[10])) {
						if (NumberUtils.isDigits(row[10])) {
							classification.setSerNo(Long.parseLong(row[10]));

							if (clsMap.containsValue(classification.getSerNo())) {
								classification.setClassname((String) clsMap
										.getKey(classification.getSerNo()));
							} else {
								errorList.add("分類法ID錯誤");
							}
						} else {
							classification.setClassname(row[10]);

							if (clsMap.containsKey(classification
									.getClassname())) {
								classification.setSerNo((Long) clsMap
										.get(classification.getClassname()));
							} else {
								errorList.add("無此分類法");
							}
						}
					} else {
						classification = null;
					}

					if (StringUtils.isNotBlank(row[16])) {
						if (getEntity().getOption().equals("package")) {
							database = databaseService.getByUUID(row[16]);

							if (database != null) {
								resourcesBuyers = database.getResourcesBuyers();
							} else {
								database = new Database();
								database.setUuIdentifier(row[16]);
								errorList.add("資料庫代碼錯誤");
							}
						} else {
							database = null;
							Category category = null;
							if (NumberUtils.isDigits(row[16])) {
								category = Category.getByToken(Integer
										.parseInt(row[16]));
							} else {
								category = (Category) toEnum(row[16],
										Category.class);
							}

							if (category == null) {
								category = Category.未註明;
							}

							resourcesBuyers = new ResourcesBuyers(category);
						}
					} else {
						if (getEntity().getOption().equals("package")) {
							errorList.add("資料庫代碼未填");
						}
					}

					journal = new Journal(row[0], row[1], row[2], row[3],
							row[4], row[5], publsihYear, row[7], row[8],
							row[9], row[12], row[13], row[14], openAccess,
							toLocalDateTime(row[17]), toLocalDateTime(row[18]),
							database, classification, row[11], null,
							resourcesBuyers);

					if (StringUtils.isBlank(journal.getTitle())) {
						errorList.add("刊名不得空白");
					}

					if (StringUtils.isBlank(journal.getPublishName())) {
						errorList.add("沒有出版社名稱");
					}

					if (StringUtils.isNotBlank(journal.getTitle())
							&& StringUtils.isNotBlank(journal.getPublishName())) {
						if (hasRepeatJou(journal.getTitle(),
								journal.getPublishName(), null)) {
							errorList.add("電子期刊重複");
						}
					}

					if (StringUtils.isNotEmpty(journal.getIssn())) {
						if (journal.getIssn().trim().length() > 10) {
							errorList.add("ISSN長度超過");
						}
					}

					if (!isURL(journal.getUrl())) {
						errorList.add("URL未填寫或不正確");
					}

					if (StringUtils.isNotBlank(row[6])) {
						if (journal.getPublishYear() == null) {
							errorList.add("出版年不正確");
						}
					}

					if (StringUtils.isNotBlank(row[17])
							&& journal.getStartDate() == null) {
						errorList.add("起始日錯誤");
					}

					if (StringUtils.isNotBlank(row[18])
							&& journal.getMaturityDate() == null) {
						errorList.add("到期日錯誤");
					}

					if (journal.getStartDate() != null
							&& journal.getMaturityDate() != null) {
						if (journal.getStartDate().isAfter(
								journal.getMaturityDate())) {
							errorList.add("到期日早於起始日");
						}
					}

					if (errorList.size() == 0) {
						if (StringUtils.isNotBlank(journal.getTitle())
								&& StringUtils.isNotBlank(journal
										.getPublishName())) {
							if (checkRepeatRow.containsKey(journal.getTitle()
									+ journal.getPublishName())) {
								errorList.add("清單資源重複");
							} else {
								checkRepeatRow.put(
										journal.getTitle()
												+ journal.getPublishName(),
										journal);
							}
						}
					}

					if (errorList.size() == 0) {
						journal.setDataStatus("正常");
						++normal;
					} else {
						journal.setDataStatus(errorList.toString()
								.replace("[", "").replace("]", ""));
					}

					originalData.add(journal);
				}
			}

			List<Journal> csvData = new ArrayList<Journal>(originalData);

			DataSet<Journal> ds = initDataSet();
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

		DataSet<Journal> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int index = first;
		while (index >= first && index < last) {
			if (index < importList.size()) {
				ds.getResults().add((Journal) importList.get(index));
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
					ds.getResults().add((Journal) importList.get(index));
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
			journal = (Journal) importList.get(i);
			if (journal.getDataStatus().equals("正常")) {
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
					if (((Journal) importList
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
					if (((Journal) importList
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
				journal = (Journal) importList.get(index);

				journal.setIssn(journal.getIssn().replace("-", "")
						.toUpperCase());

				journalService.save(journal, getLoginUser());
				journal.setDataStatus("已匯入");
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
		if (StringUtils.isBlank(getEntity().getOption())) {
			getEntity().setOption("package");
		} else {
			if (!getEntity().getOption().equals("package")
					&& !getEntity().getOption().equals("individual")) {
				getEntity().setOption("package");
			}
		}

		List<String[]> rows = new ArrayList<String[]>();

		if (getEntity().getOption().equals("package")) {
			rows.add(new String[] { "刊名", "英文縮寫刊名", "刊名演變", "ISSN", "語文",
					"出版社", "出版年(西元)", "主題類別", "卷期編次", "刊期", "分類法", "分類碼", "版本",
					"出版時間差", "URL", "開放近用", "資料庫UUID", "起始日", "到期日" });
			rows.add(new String[] {
					"The New England Journal of Medicine",
					"N. Engl. j. med.",
					"＜Boston medical and surgical journal 0096-6762",
					"15334406",
					"eng",
					"Boston, Massachusetts Medical Society.",
					"1928",
					"Medicine--Periodicals ; Surgery--Periodicals ; Medicine--periodicals",
					"000955", "Weekly", "1", "R11", "N/A", "N/A",
					"http://www.nejm.org/", "1", "78qwrw", "2010-11-10",
					"2020-11-30" });
			rows.add(new String[] { "Cell", "Cell", "", "00928674", "eng",
					"Cambridge, Mass. : MIT Press.", "1974",
					"Cytology--Periodicals ; Virology--Periodicals", "002064",
					"Biweekly", "1", "QH573", "N/A", "N/A",
					"http://www.cell.com/", "0", "78qwrw", "2010-11-10",
					"2020-11-30" });
			rows.add(new String[] {
					"American Journal of Cancer Research",
					"AJCR",
					"＜Journal of cancer research ;＞Cancer research (Chicago, Ill.) 0008-5472",
					"2156-6976", "eng", "	[Lancaster, Pa., Lancaster Press]",
					"1931", "", "C00201", "", "1", "RC261.A1A55", "N/A", "N/A",
					"http://www.ajcr.us/", "0", "78qwrw", "2010-11-10",
					"2020-11-30" });
		} else {
			rows.add(new String[] { "刊名", "英文縮寫刊名", "刊名演變", "ISSN", "語文",
					"出版社", "出版年(西元)", "主題類別", "卷期編次", "刊期", "分類法", "分類碼", "版本",
					"出版時間差", "URL", "開放近用", "資源類型", "起始日", "到期日" });
			rows.add(new String[] {
					"The New England Journal of Medicine",
					"N. Engl. j. med.",
					"＜Boston medical and surgical journal 0096-6762",
					"15334406",
					"eng",
					"Boston, Massachusetts Medical Society.",
					"1928",
					"Medicine--Periodicals ; Surgery--Periodicals ; Medicine--periodicals",
					"000955", "Weekly", "1", "R11", "N/A", "N/A",
					"http://www.nejm.org/", "1", "1", "2010-11-10",
					"2020-11-30" });
			rows.add(new String[] { "Cell", "Cell", "", "00928674", "eng",
					"Cambridge, Mass. : MIT Press.", "1974",
					"Cytology--Periodicals ; Virology--Periodicals", "002064",
					"Biweekly", "1", "QH573", "N/A", "N/A",
					"http://www.cell.com/", "0", "2", "2010-11-10",
					"2020-11-30" });
			rows.add(new String[] {
					"American Journal of Cancer Research",
					"AJCR",
					"＜Journal of cancer research ;＞Cancer research (Chicago, Ill.) 0008-5472",
					"2156-6976", "eng", "	[Lancaster, Pa., Lancaster Press]",
					"1931", "", "C00201", "", "1", "RC261.A1A55", "N/A", "N/A",
					"http://www.ajcr.us/", "0", "0", "2010-11-10", "2020-11-30" });
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos), ',',
				CSVWriter.DEFAULT_QUOTE_CHARACTER,
				CSVWriter.NO_ESCAPE_CHARACTER, "\n");

		writer.writeAll(rows);
		writer.close();

		getEntity().setReportFile(
				"journal_" + getEntity().getOption() + "_sample.csv");
		getEntity()
				.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
		return XLSX;
	}

	protected boolean isURL(String url) {
		if (StringUtils.isNotEmpty(url)) {
			url = url.trim();
		}

		return ESAPI.validator().isValidInput("Journal URL", url, "URL",
				Integer.MAX_VALUE, false);
	}

	protected boolean hasRepeatIssn(String issn, Long serNo) throws Exception {
		List<Long> results = journalService.getSerNosByIssn(issn.trim()
				.replace("-", ""));
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

	protected boolean dbHasRepeatIssn(String issn, Long serNo, Database db)
			throws Exception {
		if (db != null && db.hasSerNo()) {
			List<Long> results = journalService.getSerNosInDbByIssn(issn.trim()
					.replace("-", ""), db);
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
		}

		return false;
	}

	protected boolean hasRepeatTitle(String title, Long serNo) throws Exception {
		List<Long> results = journalService.getSerNosByTitle(title.trim());
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

	protected boolean dbHasRepeatTitle(String title, Long serNo, Database db)
			throws Exception {
		if (db != null && db.hasSerNo()) {
			List<Long> results = journalService.getSerNosInDbByTitle(
					title.trim(), db);
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
		}

		return false;
	}

	protected boolean hasRepeatJou(String title, String publishName, Long serNo)
			throws Exception {
		List<Long> results = journalService.getByTitlePublishName(title,
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

	protected boolean hasEntity() throws Exception {
		if (!getEntity().hasSerNo()) {
			return false;
		}

		journal = journalService.getBySerNo(getEntity().getSerNo());
		if (journal == null) {
			return false;
		}

		return true;
	}
}
