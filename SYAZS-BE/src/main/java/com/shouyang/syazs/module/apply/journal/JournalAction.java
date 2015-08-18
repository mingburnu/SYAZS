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
import java.util.LinkedList;
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
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.shouyang.syazs.core.converter.EnumConverter;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;
import com.shouyang.syazs.module.apply.enums.Category;
import com.shouyang.syazs.module.apply.enums.Type;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwner;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwnerService;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyersService;

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
	private Journal targetJou;

	@Autowired
	private JournalService journalService;

	@Autowired
	private ResourcesBuyers resourcesBuyers;

	@Autowired
	private ResourcesBuyersService resourcesBuyersService;

	@Autowired
	private ReferenceOwner referenceOwner;

	@Autowired
	private ReferenceOwnerService referenceOwnerService;

	@Autowired
	private EnumConverter enumConverter;

	@Override
	protected void validateSave() throws Exception {
		if (StringUtils.isBlank(getEntity().getTitle())) {
			errorMessages.add("刊名不得空白");
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
						referenceOwner = referenceOwnerService
								.getBySerNo(getEntity().getRefSerNo()[i]);
						if (referenceOwner == null) {
							errorMessages.add(getEntity().getRefSerNo()[i]
									+ "為不可利用的流水號");
						} else {
							getEntity().getOwners().add(referenceOwner);
						}
					}
				}

				i++;
			}
		}

		if (getEntity().getResourcesBuyers().getCategory() == null
				|| getEntity().getResourcesBuyers().getCategory()
						.equals(Category.不明)) {
			getEntity().getResourcesBuyers().setCategory(Category.未註明);
		}

		if (getEntity().getResourcesBuyers().getType() == null) {
			getEntity().getResourcesBuyers().setType(Type.期刊);
		}

		if (getEntity().getResourcesBuyers().getOpenAccess() == null) {
			getEntity().getResourcesBuyers().setOpenAccess(false);
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
							referenceOwner = referenceOwnerService
									.getBySerNo(getEntity().getRefSerNo()[i]);
							if (referenceOwner == null) {
								errorMessages.add(getEntity().getRefSerNo()[i]
										+ "為不可利用的流水號");
							} else {
								getEntity().getOwners().add(referenceOwner);
							}
						}
					}

					i++;
				}
			}

			if (getEntity().getResourcesBuyers().getCategory() == null
					|| getEntity().getResourcesBuyers().getCategory()
							.equals(Category.不明)) {
				getEntity().getResourcesBuyers().setCategory(Category.未註明);
			}

			if (getEntity().getResourcesBuyers().getType() == null) {
				getEntity().getResourcesBuyers().setType(Type.期刊);
			}

			if (getEntity().getResourcesBuyers().getOpenAccess() == null) {
				getEntity().getResourcesBuyers().setOpenAccess(false);
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

		List<ReferenceOwner> owners = new ArrayList<ReferenceOwner>();
		journal.setOwners(owners);
		getRequest().setAttribute("uncheckReferenceOwners",
				referenceOwnerService.getUncheckOwners(owners));

		setEntity(journal);

		return ADD;
	}

	@Override
	public String edit() throws Exception {
		if (hasEntity()) {
			setCategoryList();

			List<ReferenceOwner> owners = new ArrayList<ReferenceOwner>(
					journal.getReferenceOwners());
			journal.setOwners(owners);
			getRequest().setAttribute("uncheckReferenceOwners",
					referenceOwnerService.getUncheckOwners(owners));
			setEntity(journal);
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
			getEntity().setOption("entity.chineseTitle");
		}

		if (getEntity().getOption().equals("entity.title")) {
			getEntity().setIssn(null);
		} else {
			getEntity().setTitle(null);
		}

		DataSet<Journal> ds = journalService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
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
			getEntity()
					.setIssn(
							getEntity().getIssn().toUpperCase()
									.replace("-", "").trim());

			getEntity().setReferenceOwners(
					new HashSet<ReferenceOwner>(getEntity().getOwners()));

			journal = journalService.save(getEntity(), getLoginUser());

			setEntity(journal);
			addActionMessage("新增成功");
			return VIEW;
		} else {
			setCategoryList();

			journal = getEntity();

			getRequest().setAttribute(
					"uncheckReferenceOwners",
					referenceOwnerService.getUncheckOwners(getEntity()
							.getOwners()));
			setEntity(journal);
			return ADD;
		}
	}

	@Override
	public String update() throws Exception {
		validateUpdate();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			getEntity()
					.setIssn(
							getEntity().getIssn().toUpperCase()
									.replace("-", "").trim());

			getEntity().setReferenceOwners(
					new HashSet<ReferenceOwner>(getEntity().getOwners()));
			journal = journalService.update(getEntity(), getLoginUser());

			setEntity(journal);
			addActionMessage("修改成功");
			return VIEW;
		} else {
			setCategoryList();
			journal = getEntity();

			getRequest().setAttribute(
					"uncheckReferenceOwners",
					referenceOwnerService.getUncheckOwners(getEntity()
							.getOwners()));
			setEntity(journal);
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
				if (StringUtils.isBlank(rowValues[11])) {
					category = Category.未註明.getCategory();
				} else {
					Object object = getEnum(
							new String[] { rowValues[11].trim() },
							Category.class);
					if (object != null) {
						category = rowValues[11].trim();
					} else {
						category = Category.不明.getCategory();
					}
				}

				String type = "";
				if (StringUtils.isBlank(rowValues[12])) {
					type = Type.期刊.getType();
				} else {
					Object object = getEnum(
							new String[] { rowValues[12].trim() }, Type.class);
					if (object != null) {
						type = rowValues[12].trim();
					} else {
						type = Type.期刊.getType();
					}
				}

				boolean openAccess = false;
				if (rowValues[15].toLowerCase().equals("yes")
						|| rowValues[15].toLowerCase().equals("true")
						|| rowValues[15].equals("是")) {
					openAccess = true;
				}

				resourcesBuyers = new ResourcesBuyers(rowValues[9],
						rowValues[10], Category.valueOf(category),
						Type.valueOf(type), rowValues[13], rowValues[14],
						openAccess);

				String issn = rowValues[2].trim().toUpperCase();

				referenceOwner = new ReferenceOwner();
				referenceOwner.setName(rowValues[16].trim());

				List<ReferenceOwner> owners = new LinkedList<ReferenceOwner>();
				owners.add(referenceOwner);

				journal = new Journal(rowValues[0], rowValues[1], "", issn,
						rowValues[3], rowValues[4], rowValues[5], "", "",
						rowValues[6], rowValues[7], null, rowValues[8],
						resourcesBuyers);
				journal.setOwners(owners);

				if (isIssn(issn)) {
					long jouSerNo = journalService.getJouSerNoByIssn(issn
							.replace("-", ""));

					long refSerNo = referenceOwnerService
							.getRefSerNoByName(referenceOwner.getName());
					if (refSerNo != 0) {
						journal.getOwners().get(0).setSerNo(refSerNo);
						if (jouSerNo != 0) {
							if (journalService.isExist(jouSerNo, refSerNo)) {
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

				if (!isURL(journal.getResourcesBuyers().getUrl())) {
					journal.getResourcesBuyers().setUrl(null);
				}

				if (journal.getDataStatus() == null) {
					journal.setDataStatus("正常");
				}

				if (journal.getDataStatus().equals("正常")
						&& !originalData.contains(journal)) {

					if (checkRepeatRow.containsKey(journal.getIssn()
							+ referenceOwner.getName())) {
						journal.setDataStatus("資料重複");

					} else {
						checkRepeatRow.put(
								journal.getIssn() + referenceOwner.getName(),
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
			return IMPORT;
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
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
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
				journal = (Journal) importList.get(index);

				targetJou = journalService.getJouByIssn(journal.getIssn()
						.replace("-", ""));

				if (targetJou == null) {
					journal.setReferenceOwners(new HashSet<ReferenceOwner>(
							journal.getOwners()));
					journal.setIssn(journal.getIssn().replace("-", ""));
					journal = journalService.save(journal, getLoginUser());

				} else {
					targetJou.getReferenceOwners().add(
							journal.getOwners().get(0));
					journal = journalService.update(targetJou, getLoginUser());
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
		getEntity().setReportFile("journal_sample.xlsx");

		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("journal");
		// Create row object
		XSSFRow row;
		// This data needs to be written (Object[])
		Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
		empinfo.put("1", new Object[] { "刊名", "英文縮寫刊名", "ISSN", "語文",
				"出版項/出版社", "出版年", "刊別/期刊頻率", "國會分類號", "出版時間差", "起始日", "到期日",
				"資源類型(買斷/租用)", "資源分類", "資料庫題名", "URL", "公開資源", "購買人名稱" });

		empinfo.put("2", new Object[] { "The New England Journal of Medicine",
				"", "15334406", "eng", "NEJM", "1812", "weekly", "N/A", "",
				"N/A", "N/A", "租貸", "期刊", "", "", "是", "獨孤寧珂" });
		empinfo.put("3", new Object[] { "The New England Journal of Medicine",
				"", "15334406", "eng", "NEJM", "1812", "weekly", "N/A", "",
				"N/A", "N/A", "租貸", "期刊", "", "", "否", "劉季" });

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
		String regex = "(\\d{4})(\\-?)(\\d{3})[\\dX]";
		Pattern pattern = Pattern.compile(regex);
		issn = issn.trim();

		Matcher matcher = pattern.matcher(issn.toUpperCase());
		if (matcher.matches()) {
			issn = issn.replace("-", "");
			int sum = 0;
			for (int i = 0; i < 7; i++) {
				sum = sum + Integer.parseInt(issn.substring(i, i + 1))
						* (8 - i);
			}

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

	protected boolean isURL(String url) {
		return ESAPI.validator().isValidInput("Journal URL", url, "URL",
				Integer.MAX_VALUE, true);
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
		if (!getEntity().hasSerNo()) {
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
