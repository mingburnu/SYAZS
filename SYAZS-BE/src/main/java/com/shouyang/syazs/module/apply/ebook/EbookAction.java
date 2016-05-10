package com.shouyang.syazs.module.apply.ebook;

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
public class EbookAction extends GenericWebActionFull<Ebook> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9166237220863961574L;

	@Autowired
	private Ebook ebook;

	@Autowired
	private Database database;

	@Autowired
	private Classification classification;

	@Autowired
	private EbookService ebookService;

	@Autowired
	private DatabaseService databaseService;

	@Autowired
	private ResourcesBuyers resourcesBuyers;

	@Autowired
	private ClassificationService classificationService;

	@Override
	protected void validateSave() throws Exception {
		if (StringUtils.isBlank(getEntity().getBookName())) {
			errorMessages.add("書名不得空白");
		}

		if (StringUtils.isBlank(getEntity().getPublishName())) {
			errorMessages.add("出版社不得空白");
		}

		if (errorMessages.size() == 0) {
			if (hasRepeatEbk(getEntity().getBookName(), getEntity()
					.getPublishName(), getEntity().getSerNo())) {
				errorMessages.add("電子書重複");
			}
		}

		if (StringUtils.isNotBlank(getEntity().getIsbn())) {
			if (getEntity().getIsbn().trim().length() > 20) {
				errorMessages.add("ISBN長度超過");
			}
		}

		if (!isURL(getEntity().getUrl())) {
			errorMessages.add("URL必須填寫");
		}

		if (StringUtils.isNotEmpty(getRequest().getParameter("entity.pubDate"))) {
			if (getEntity().getPubDate() == null) {
				errorMessages.add("出版日不正確");
			}
		}

		if (getEntity().getDatabase().hasSerNo()) {
			database = databaseService.getBySerNo(getEntity().getDatabase()
					.getSerNo());
			if (database == null) {
				errorMessages.add("不可利用的資料庫流水號");
			} else {
				getEntity().setDatabase(database);
				getEntity().setResourcesBuyers(database.getResourcesBuyers());
			}
		} else {
			getEntity().setDatabase(null);
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
			if (StringUtils.isBlank(getEntity().getBookName())) {
				errorMessages.add("書名不得空白");
			}

			if (StringUtils.isBlank(getEntity().getPublishName())) {
				errorMessages.add("出版社不得空白");
			}

			if (errorMessages.size() == 0) {
				if (hasRepeatEbk(getEntity().getBookName(), getEntity()
						.getPublishName(), getEntity().getSerNo())) {
					errorMessages.add("電子書重複");
				}
			}

			if (StringUtils.isNotBlank(getEntity().getIsbn())) {
				if (getEntity().getIsbn().trim().length() > 20) {
					errorMessages.add("ISBN長度超過");
				}
			}

			if (!isURL(getEntity().getUrl())) {
				errorMessages.add("URL必須填寫");
			}

			if (StringUtils.isNotEmpty(getRequest().getParameter(
					"entity.pubDate"))) {
				if (getEntity().getPubDate() == null) {
					errorMessages.add("出版日不正確");
				}
			}

			if (getEntity().getDatabase().hasSerNo()) {
				database = databaseService.getBySerNo(getEntity().getDatabase()
						.getSerNo());
				if (database == null) {
					errorMessages.add("不可利用的資料庫流水號");
				} else {
					getEntity().setDatabase(database);
					getEntity().setResourcesBuyers(
							database.getResourcesBuyers());
				}
			} else {
				getEntity().setDatabase(null);
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
						|| ebookService
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
		DataSet<Ebook> ds = initDataSet();
		ds.setDatas(classificationService.getClsDatas());
		setEntity(ebook);
		setDs(ds);
		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
			DataSet<Ebook> ds = initDataSet();
			ds.setDatas(classificationService.getClsDatas());
			setEntity(ebook);
			setDs(ds);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		if (StringUtils.isNotBlank(getEntity().getOption())) {
			if (!getEntity().getOption().equals("entity.bookName")
					&& !getEntity().getOption().equals("entity.isbn")) {
				getEntity().setOption("entity.bookName");
			}
		} else {
			getEntity().setOption("entity.bookName");
		}

		DataSet<Ebook> ds = ebookService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
					.doubleValue()
					/ ds.getPager().getRecordPerPage().doubleValue());
			ds.getPager().setCurrentPage(lastPage.intValue());
			ds = ebookService.getByRestrictions(ds);
		}

		setDs(ds);
		return LIST;
	}

	@Override
	public String save() throws Exception {
		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			if (StringUtils.isNotBlank(getEntity().getIsbn())) {
				getEntity().setIsbn(
						getEntity().getIsbn().replace("-", "").toUpperCase());
			}
			ebook = ebookService.save(getEntity(), getLoginUser());

			setEntity(ebook);
			addActionMessage("新增成功");
			return VIEW;
		} else {
			DataSet<Ebook> ds = initDataSet();
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
			if (StringUtils.isNotBlank(getEntity().getIsbn())) {
				getEntity().setIsbn(
						getEntity().getIsbn().replace("-", "").toUpperCase());
			}
			ebook = ebookService.update(getEntity(), getLoginUser());
			setEntity(ebook);
			addActionMessage("修改成功");
			return VIEW;
		} else {
			DataSet<Ebook> ds = initDataSet();
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
				ebookService.deleteBySerNo(getEntity().getCheckItem()[i]);
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
			setEntity(ebook);
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

			LinkedHashSet<Ebook> originalData = new LinkedHashSet<Ebook>();
			Map<String, Ebook> checkRepeatRow = new LinkedHashMap<String, Ebook>();
			BidiMap clsMap = new DualHashBidiMap(
					classificationService.getClsDatas());

			int normal = 0;

			CSVReader reader = new CSVReader(new FileReader(getEntity()
					.getFile()[0]), ',');

			String[] row;
			int rowLength = 16;
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

					boolean openAccess = false;
					if (StringUtils.isNotBlank(row[14])) {
						if (row[14].equals("1")
								|| row[14].toLowerCase().equals("yes")
								|| row[14].toLowerCase().equals("true")
								|| row[14].equals("是") || row[14].equals("真")) {
							openAccess = true;
						}
					}

					classification = new Classification();

					if (StringUtils.isNotBlank(row[9])) {
						if (NumberUtils.isDigits(row[9])) {
							classification.setSerNo(Long.parseLong(row[9]));

							if (clsMap.containsValue(classification.getSerNo())) {
								classification.setClassname((String) clsMap
										.getKey(classification.getSerNo()));
							} else {
								errorList.add("分類法ID錯誤");
							}
						} else {
							classification.setClassname(row[9]);

							if (clsMap.containsKey(classification
									.getClassname())) {
								classification.setSerNo((Long) clsMap
										.get(classification.getClassname()));
							} else {
								errorList.add("無此分類法");
							}
						}
					}

					if (StringUtils.isNotBlank(row[15])) {
						if (getEntity().getOption().equals("package")) {
							database = databaseService.getByUUID(row[15]);

							if (database != null) {
								resourcesBuyers = database.getResourcesBuyers();
							} else {
								database = new Database();
								database.setUuIdentifier(row[15]);
								errorList.add("資料庫代碼錯誤");
							}
						} else {
							database = null;
							Category category = null;
							if (NumberUtils.isDigits(row[15])) {
								category = Category.getByToken(Integer
										.parseInt(row[15]));
							} else {
								category = (Category) toEnum(row[15],
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

					ebook = new Ebook(row[0], row[1], row[2], row[3], row[4],
							row[5], toLocalDateTime(row[6]), row[7], row[8],
							row[12], row[13], row[11], openAccess, database,
							classification, row[10], null, resourcesBuyers);

					if (StringUtils.isBlank(ebook.getBookName())) {
						errorList.add("書名不得空白");
					}

					if (StringUtils.isBlank(ebook.getPublishName())) {
						errorList.add("沒有出版社名稱");
					}

					if (StringUtils.isNotBlank(ebook.getIsbn())) {
						if (ebook.getIsbn().length() > 20) {
							errorList.add("ISBN長度超過");
						}
					}

					if (StringUtils.isNotBlank(ebook.getBookName())
							&& StringUtils.isNotBlank(ebook.getPublishName())) {
						if (hasRepeatEbk(ebook.getBookName(),
								ebook.getPublishName(), null)) {
							errorList.add("電子書重複");
						}
					}

					if (!isURL(ebook.getUrl())) {
						errorList.add("URL未填寫或不正確");
					}

					if (StringUtils.isNotBlank(row[6])
							&& ebook.getPubDate() == null) {
						errorList.add("出版日不正確");
					}

					if (errorList.size() == 0) {
						if (StringUtils.isNotBlank(ebook.getBookName())
								&& StringUtils.isNotBlank(ebook
										.getPublishName())) {
							if (checkRepeatRow.containsKey(ebook.getBookName()
									+ ebook.getPublishName())) {
								errorList.add("清單資源重複");
							} else {
								checkRepeatRow
										.put(ebook.getBookName()
												+ ebook.getPublishName(), ebook);
							}
						}
					}

					if (errorList.size() == 0) {
						ebook.setDataStatus("正常");
						++normal;
					} else {
						ebook.setDataStatus(errorList.toString()
								.replace("[", "").replace("]", ""));
					}

					originalData.add(ebook);
				}
			}

			List<Ebook> csvData = new ArrayList<Ebook>(originalData);

			DataSet<Ebook> ds = initDataSet();
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

		DataSet<Ebook> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int index = first;
		while (index >= first && index < last) {
			if (index < importList.size()) {
				ds.getResults().add((Ebook) importList.get(index));
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
					ds.getResults().add((Ebook) importList.get(index));
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
			ebook = (Ebook) importList.get(i);
			if (ebook.getDataStatus().equals("正常")) {
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
					if (((Ebook) importList.get(getEntity().getImportItem()[0]))
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
					if (((Ebook) importList.get(getEntity().getImportItem()[i]))
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
				ebook = (Ebook) importList.get(index);

				if (StringUtils.isNotBlank(ebook.getIsbn())) {
					ebook.setIsbn(ebook.getIsbn().replace("-", "")
							.toUpperCase());
				}

				ebookService.save(ebook, getLoginUser());
				ebook.setDataStatus("已匯入");
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
			rows.add(new String[] { "書名", "ISBN", "出版社", "第一作者", "次要作者",
					"系列叢書名", "出版日期", "語文", "版本", "分類法", "分類碼", "URL", "類型",
					"出版地", "開放近用", "資料庫UUID" });
			rows.add(new String[] {
					"Ophthalmic clinical procedures：a multimedia guide",
					"978-0-08-044978-4", "Elsevier(ClinicalKey)",
					"Frank Eperjesi", "Hannah Bartlett & Mark Dunne", "N/A",
					"2008-2-7", "eng", "1", "1", "Q",
					"https://lib3.cgmh.org.tw/cgi-bin/er4/browse.cgi", "醫學",
					"Netherlands", "1", "sd3erw" });
			rows.add(new String[] { "C嘉魔法彩繪", "978-9-88-807293-4", "青森文化",
					"陳嘉慧", "N/A", "N/A", "2011-5-5", "cht", "N/A", "1", "900",
					"http://tpml.ebook.hyread.com.tw/bookDetail.jsp?id=40258",
					"藝術", "Taiwan", "0", "sd3erw" });
			rows.add(new String[] { "Topics in Pathology for Hong Kong",
					"9789622093362", "Hong Kong University Press",
					"	Faith C.S. Ho", "P.C. Wu", "N/A", "1995-4-4", "eng",
					"N/A", "1", "R",
					"http://tpml.ebook.hyread.com.tw/bookDetail.jsp?id=40258",
					"醫學", "HK", "0", "sd3erw" });
		} else {
			rows.add(new String[] { "書名", "ISBN", "出版社", "第一作者", "次要作者",
					"系列叢書名", "出版日期", "語文", "版本", "分類法", "分類碼", "URL", "類型",
					"出版地", "開放近用", "資源類型" });
			rows.add(new String[] {
					"Ophthalmic clinical procedures：a multimedia guide",
					"978-0-08-044978-4", "Elsevier(ClinicalKey)",
					"Frank Eperjesi", "Hannah Bartlett & Mark Dunne", "N/A",
					"2008-2-7", "eng", "1", "1", "Q",
					"https://lib3.cgmh.org.tw/cgi-bin/er4/browse.cgi", "醫學",
					"Netherlands", "1", "0" });
			rows.add(new String[] { "C嘉魔法彩繪", "978-9-88-807293-4", "青森文化",
					"陳嘉慧", "N/A", "N/A", "2011-5-5", "cht", "N/A", "1", "900",
					"http://tpml.ebook.hyread.com.tw/bookDetail.jsp?id=40258",
					"藝術", "Taiwan", "0", "1" });
			rows.add(new String[] { "Topics in Pathology for Hong Kong",
					"9789622093362", "Hong Kong University Press",
					"	Faith C.S. Ho", "P.C. Wu", "N/A", "1995-4-4", "eng",
					"N/A", "1", "R",
					"http://tpml.ebook.hyread.com.tw/bookDetail.jsp?id=40258",
					"醫學", "HK", "0", "2" });
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos), ',',
				CSVWriter.DEFAULT_QUOTE_CHARACTER,
				CSVWriter.NO_ESCAPE_CHARACTER, "\n");

		writer.writeAll(rows);
		writer.close();

		getEntity().setReportFile(
				"ebook_" + getEntity().getOption() + "_sample.csv");
		getEntity()
				.setInputStream(new ByteArrayInputStream(baos.toByteArray()));

		return XLSX;
	}

	protected boolean isURL(String url) {
		if (StringUtils.isNotEmpty(url)) {
			url = url.trim();
		}

		return ESAPI.validator().isValidInput("Ebook URL", url, "URL",
				Integer.MAX_VALUE, false);
	}

	protected boolean hasEntity() throws Exception {
		if (getEntity().getSerNo() == null) {
			getEntity().setSerNo(-1L);
			return false;
		}

		ebook = ebookService.getBySerNo(getEntity().getSerNo());
		if (ebook == null) {
			return false;
		}

		return true;
	}

	protected boolean hasRepeatIsbn(String isbn, Long serNo) throws Exception {
		List<Long> results = ebookService.getSerNosByIsbn(Long.parseLong(isbn
				.trim().replace("-", "")));
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

	protected boolean hasRepeatName(String bookName, Long serNo)
			throws Exception {
		List<Long> results = ebookService.getSerNosByName(bookName.trim());
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

	protected boolean dbHasRepeatName(String bookName, Long serNo, Database db)
			throws Exception {
		if (db != null && db.hasSerNo()) {
			List<Long> results = ebookService.getSerNosInDbByName(
					bookName.trim(), db);
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

	protected boolean hasRepeatEbk(String bookName, String publishName,
			Long serNo) throws Exception {
		List<Long> results = ebookService.getByTitlePublishName(bookName,
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
