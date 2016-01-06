package com.shouyang.syazs.module.apply.referenceOwner;

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
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.collect.Lists;
import com.shouyang.syazs.core.apply.enums.Role;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReferenceOwnerAction extends GenericWebActionFull<ReferenceOwner> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4935047754421984759L;

	@Autowired
	private ReferenceOwner referenceOwner;

	@Autowired
	private ReferenceOwnerService referenceOwnerService;

	@Override
	protected void validateSave() throws Exception {
		if (StringUtils.isBlank(getEntity().getName())) {
			errorMessages.add("用戶名稱不可空白");
		} else {
			if (getEntity().getName()
					.replaceAll("[a-zA-Z0-9\u4e00-\u9fa5]", "").length() != 0) {
				errorMessages.add("用戶名稱必須是英、數或漢字");
			} else {
				if (referenceOwnerService.getRefSerNoByName(getEntity()
						.getName()) != 0) {
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
							|| referenceOwnerService.getBySerNo(getEntity()
									.getCheckItem()[i]) == null) {
						addActionError("有錯誤流水號");
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
			referenceOwner = referenceOwnerService.getBySerNo(getEntity()
					.getSerNo());
			setEntity(referenceOwner);
		}

		return EDIT;
	}

	@Override
	public String list() throws Exception {
		if (StringUtils.isNotBlank(getEntity().getOption())) {
			if (!getEntity().getOption().equals("entity.name")
					&& !getEntity().getOption().equals("entity.engName")) {
				getEntity().setOption("entity.name");
			}
		} else {
			getEntity().setOption("entity.name");
		}

		DataSet<ReferenceOwner> ds = referenceOwnerService
				.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			ds = referenceOwnerService.getByRestrictions(ds);
		}

		setDs(ds);
		return LIST;
	}

	@Override
	public String save() throws Exception {
		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			referenceOwner = referenceOwnerService.save(getEntity(),
					getLoginUser());

			setEntity(referenceOwner);

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
			referenceOwner = referenceOwnerService.update(getEntity(),
					getLoginUser(), "name");

			setEntity(referenceOwner);
			addActionMessage("修改成功");
			return VIEW;
		} else {
			if (hasEntity()) {
				getEntity().setName(
						referenceOwnerService
								.getBySerNo(getEntity().getSerNo()).getName());
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
				referenceOwner = referenceOwnerService.getBySerNo(getEntity()
						.getCheckItem()[i]);
				String name = referenceOwner.getName();

				referenceOwnerService
						.deleteBySerNo(getEntity().getCheckItem()[i]);
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
			setEntity(referenceOwner);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		return VIEW;
	}

	public String box() throws Exception {
		getRequest().setAttribute("allReferenceOwners",
				referenceOwnerService.getAllOwners());

		return BOX;
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
			String[] rowTitles = new String[6];
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

			LinkedHashSet<ReferenceOwner> originalData = new LinkedHashSet<ReferenceOwner>();
			Map<String, ReferenceOwner> checkRepeatRow = new LinkedHashMap<String, ReferenceOwner>();
			int normal = 0;

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				String[] rowValues = new String[6];
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

				referenceOwner = new ReferenceOwner(rowValues[0], rowValues[1],
						rowValues[2], rowValues[4], rowValues[3], "");
				List<String> errorList = Lists.newArrayList();

				if (StringUtils.isBlank(referenceOwner.getName())) {
					errorList.add("名稱空白");
				} else {
					if (referenceOwner.getName()
							.replaceAll("[a-zA-Z0-9\u4e00-\u9fa5]", "")
							.length() != 0) {
						errorList.add("名稱字元異常");
					} else {
						long cusSerNo = referenceOwnerService
								.getRefSerNoByName(referenceOwner.getName());
						if (cusSerNo != 0) {
							errorList.add("已存在");
						}
					}
				}

				if (StringUtils.isNotEmpty(referenceOwner.getTel())) {
					String tel = referenceOwner.getTel()
							.replaceAll("[/()+-]", "").replace(" ", "");
					if (!NumberUtils.isDigits(tel)) {
						errorList.add("電話異常");
					}
				}

				if (errorList.size() != 0) {
					referenceOwner.setDataStatus(errorList.toString()
							.replace("[", "").replace("]", ""));
				} else {
					referenceOwner.setDataStatus("正常");
				}

				if (referenceOwner.getDataStatus().equals("正常")
						&& !originalData.contains(referenceOwner)) {

					if (checkRepeatRow.containsKey(referenceOwner.getName())) {
						referenceOwner.setDataStatus("名稱重複");

					} else {
						checkRepeatRow.put(referenceOwner.getName(),
								referenceOwner);

						++normal;
					}
				}
				originalData.add(referenceOwner);
			}

			List<ReferenceOwner> excelData = new ArrayList<ReferenceOwner>(
					originalData);

			DataSet<ReferenceOwner> ds = initDataSet();
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

		DataSet<ReferenceOwner> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int i = 0;
		while (i < importList.size()) {
			if (i >= first && i < last) {
				ds.getResults().add((ReferenceOwner) importList.get(i));
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
					ds.getResults().add((ReferenceOwner) importList.get(j));
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
			referenceOwner = (ReferenceOwner) importList.get(i);
			if (referenceOwner.getDataStatus().equals("正常")) {
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
					if (((ReferenceOwner) importList.get(getEntity()
							.getImportItem()[0])).getDataStatus().equals("正常")) {
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
					if (((ReferenceOwner) importList.get(getEntity()
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
				referenceOwner = (ReferenceOwner) importList.get(index);
				referenceOwnerService.save(referenceOwner, getLoginUser());
				referenceOwner.setDataStatus("已存在");
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

		getEntity().setReportFile("referenceOwner_error.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("referenceOwner_error");
		XSSFRow row;

		Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();

		Integer mark = 1;
		empinfo.put(mark.toString(), new Object[] { "name/姓名", "egName/英文姓名",
				"address/地址", "tel/電話", "contactUserName/聯絡人", "錯誤原因" });

		int i = 0;
		while (i < importList.size()) {
			referenceOwner = (ReferenceOwner) importList.get(i);
			if (!referenceOwner.getDataStatus().equals("正常")
					&& !referenceOwner.getDataStatus().equals("已匯入")) {
				mark = mark + 1;
				empinfo.put(
						mark.toString(),
						new Object[] { referenceOwner.getName(),
								referenceOwner.getEngName(),
								referenceOwner.getAddress(),
								referenceOwner.getTel(),
								referenceOwner.getContactUserName(),
								referenceOwner.getDataStatus() });
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
		getEntity()
				.setInputStream(new ByteArrayInputStream(boas.toByteArray()));

		return XLSX;
	}

	public String example() throws Exception {
		// reportFile = "referenceOwner_sample.xlsx";
		getEntity().setReportFile("referenceOwner_sample.xlsx");
		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("referenceOwner");
		// Create row object
		XSSFRow row;
		// This data needs to be written (Object[])
		Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
		empinfo.put("1", new Object[] { "name/姓名", "egName/英文姓名", "address/地址",
				"tel/電話", "contactUserName/聯絡人" });

		empinfo.put("2", new Object[] { "雲天河", "Tien-Ho Yun",
				"台北市內湖區民權東路六段161號", "886-2-87923100", "總機" });

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

	protected boolean hasEntity() throws Exception {
		if (!getEntity().hasSerNo()) {
			return false;
		}

		referenceOwner = referenceOwnerService.getBySerNo(getEntity()
				.getSerNo());
		if (referenceOwner == null) {
			return false;
		}

		return true;
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
}
