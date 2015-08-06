package com.shouyang.syazs.core.apply.ipRange;

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

import com.shouyang.syazs.core.apply.customer.CustomerService;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IpRangeAction extends GenericWebActionFull<IpRange> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3274723922938216387L;

	@Autowired
	private IpRange ipRange;

	@Autowired
	private IpRangeService ipRangeService;

	@Autowired
	private CustomerService customerService;

	@Override
	protected void validateSave() throws Exception {
		if (!getEntity().getCustomer().hasSerNo()
				|| getEntity().getCustomer().getSerNo() <= 0
				|| customerService.getBySerNo(getEntity().getCustomer()
						.getSerNo()) == null) {
			errorMessages.add("could not execute statement");
		}

		if (!isIp(getEntity().getIpRangeStart())) {
			errorMessages.add("IP起值輸入格式錯誤");
		}

		if (!isIp(getEntity().getIpRangeEnd())) {
			errorMessages.add("IP起值輸入格式錯誤");
		}

		if (isIp(getEntity().getIpRangeStart())
				&& isIp(getEntity().getIpRangeEnd())) {
			String[] ipStartNum = getEntity().getIpRangeStart().split("\\.");
			String[] ipEndNum = getEntity().getIpRangeEnd().split("\\.");

			int i = 0;
			while (i < 2) {
				if (Integer.parseInt(ipStartNum[i]) != Integer
						.parseInt(ipEndNum[i])) {
					errorMessages.add("IP起迄值第" + (i + 1) + "位數字必須相同");
				}
				i++;
			}

			if (Integer.parseInt(ipStartNum[0]) == Integer
					.parseInt(ipEndNum[0])
					&& Integer.parseInt(ipStartNum[1]) == Integer
							.parseInt(ipEndNum[1])) {
				if (Integer.parseInt(ipStartNum[2]) * 1000
						+ Integer.parseInt(ipStartNum[3]) > Integer
						.parseInt(ipEndNum[2])
						* 1000
						+ Integer.parseInt(ipEndNum[3])) {
					errorMessages.add("IP起值不可大於IP迄值");
				} else {
					if (isPrivateIp(getEntity().getIpRangeStart(), getEntity()
							.getIpRangeEnd())) {
						errorMessages.add("此IP區間為私有Ip");
					} else {
						IpRange repeatIpRange = checkRepeatIpRange(getEntity()
								.getIpRangeStart(),
								getEntity().getIpRangeEnd(),
								ipRangeService.getAllIpList(0));
						if (repeatIpRange != null) {
							errorMessages.add("此IP區間"
									+ repeatIpRange.getCustomer().getName()
									+ "正在使用");
						}
					}
				}
			}
		}
	}

	@Override
	protected void validateUpdate() throws Exception {
		if (ipRangeService.getTargetEntity(initDataSet()) == null) {
			errorMessages.add("Target must not be null");
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			if (!isIp(getEntity().getIpRangeStart())) {
				errorMessages.add("IP起值輸入格式錯誤");
			}

			if (!isIp(getEntity().getIpRangeEnd())) {
				errorMessages.add("IP起值輸入格式錯誤");
			}

			if (isIp(getEntity().getIpRangeStart())
					&& isIp(getEntity().getIpRangeEnd())) {
				String[] ipStartNum = getEntity().getIpRangeStart()
						.split("\\.");
				String[] ipEndNum = getEntity().getIpRangeEnd().split("\\.");

				int i = 0;
				while (i < 2) {
					if (Integer.parseInt(ipStartNum[i]) != Integer
							.parseInt(ipEndNum[i])) {
						errorMessages.add("IP起迄值第" + (i + 1) + "位數字必須相同");
					}
					i++;
				}

				if (Integer.parseInt(ipStartNum[0]) == Integer
						.parseInt(ipEndNum[0])
						&& Integer.parseInt(ipStartNum[1]) == Integer
								.parseInt(ipEndNum[1])) {
					if (Integer.parseInt(ipStartNum[2]) * 1000
							+ Integer.parseInt(ipStartNum[3]) > Integer
							.parseInt(ipEndNum[2])
							* 1000
							+ Integer.parseInt(ipEndNum[3])) {
						errorMessages.add("IP起值不可大於IP迄值");
					} else {
						if (isPrivateIp(getEntity().getIpRangeStart(),
								getEntity().getIpRangeEnd())) {
							errorMessages.add("此IP區間為私有Ip");
						} else {
							IpRange repeatIpRange = checkRepeatIpRange(
									getEntity().getIpRangeStart(), getEntity()
											.getIpRangeEnd(),
									ipRangeService.getAllIpList(getEntity()
											.getSerNo()));

							if (repeatIpRange != null) {
								String name = repeatIpRange.getCustomer()
										.getName();
								errorMessages.add("此IP區間" + name + "正在使用");
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void validateDelete() throws Exception {
		if (ipRangeService.getTargetEntity(initDataSet()) == null) {
			errorMessages.add("沒有這個物件");
		}
	}

	@Override
	public String add() throws Exception {
		return ADD;
	}

	@Override
	public String edit() throws Exception {
		ipRange = ipRangeService.getTargetEntity(initDataSet());
		if (ipRange != null) {
			ipRange.setListNo(getEntity().getListNo());
			setEntity(ipRange);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		return EDIT;
	}

	@Override
	public String list() throws Exception {
		if (getEntity().getCustomer().hasSerNo()
				&& getEntity().getCustomer().getSerNo() > 0) {
			DataSet<IpRange> ds = ipRangeService
					.getByRestrictions(initDataSet());

			if (ds.getResults().size() == 0
					&& ds.getPager().getCurrentPage() > 1) {
				ds.getPager().setCurrentPage(
						(int) Math.ceil(ds.getPager().getTotalRecord()
								/ ds.getPager().getRecordPerPage()));
				ds = ipRangeService.getByRestrictions(ds);
			}

			setDs(ds);
		} else {
			setDs(initDataSet());
		}

		return LIST;
	}

	@Override
	public String save() throws Exception {
		validateSave();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			String[] ipStartNums = getEntity().getIpRangeStart().split("\\.");
			String[] ipEndNums = getEntity().getIpRangeEnd().split("\\.");

			StringBuilder ipStartBuilder = new StringBuilder();
			StringBuilder ipEndBuilder = new StringBuilder();

			int i = 0;
			while (i < 4) {
				if (i < 3) {
					ipStartBuilder.append(Integer.parseInt(ipStartNums[i])
							+ ".");
					ipEndBuilder.append(Integer.parseInt(ipEndNums[i]) + ".");
				} else {
					ipStartBuilder.append(Integer.parseInt(ipStartNums[i]));
					ipEndBuilder.append(Integer.parseInt(ipEndNums[i]));
				}

				i++;
			}

			getEntity().setIpRangeStart(ipStartBuilder.toString());
			getEntity().setIpRangeEnd(ipEndBuilder.toString());

			ipRange = ipRangeService.save(getEntity(), getLoginUser());
			setEntity(ipRange);
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
			String[] ipStartNums = getEntity().getIpRangeStart().split("\\.");
			String[] ipEndNums = getEntity().getIpRangeEnd().split("\\.");

			StringBuilder ipStartBuilder = new StringBuilder();
			StringBuilder ipEndBuilder = new StringBuilder();

			int i = 0;
			while (i < 4) {
				if (i < 3) {
					ipStartBuilder.append(Integer.parseInt(ipStartNums[i])
							+ ".");
					ipEndBuilder.append(Integer.parseInt(ipEndNums[i]) + ".");
				} else {
					ipStartBuilder.append(Integer.parseInt(ipStartNums[i]));
					ipEndBuilder.append(Integer.parseInt(ipEndNums[i]));
				}

				i++;
			}

			getEntity().setIpRangeStart(ipStartBuilder.toString());
			getEntity().setIpRangeEnd(ipEndBuilder.toString());

			ipRange = ipRangeService.update(getEntity(), getLoginUser());
			ipRange.setListNo(getEntity().getListNo());
			setEntity(ipRange);
			addActionMessage("修改成功");
			return VIEW;
		} else {
			return EDIT;
		}
	}

	@Override
	public String delete() throws Exception {
		validateDelete();
		setActionErrors(errorMessages);

		if (!hasActionErrors()) {
			ipRangeService.deleteBySerNo(getEntity().getSerNo());
			list();
			return LIST;
		} else {
			list();
			return LIST;
		}
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
			} else {
				if (!getEntity().getCustomer().hasSerNo()
						|| getEntity().getCustomer().getSerNo() <= 0
						|| customerService.getBySerNo(getEntity().getCustomer()
								.getSerNo()) == null) {
					addActionError("客戶錯誤");
				}
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
			String[] rowTitles = new String[2];
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

			LinkedHashSet<IpRange> originalData = new LinkedHashSet<IpRange>();
			Map<String, IpRange> checkRepeatRow = new LinkedHashMap<String, IpRange>();
			int normal = 0;

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				String[] rowValues = new String[2];
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

				ipRange = new IpRange(getEntity().getCustomer(), rowValues[0],
						rowValues[1]);

				if (!isIp(ipRange.getIpRangeStart())
						|| !isIp(ipRange.getIpRangeStart())
						|| !ipRange.getCustomer().hasSerNo()) {
					ipRange.setDataStatus("資料錯誤");
				} else if (isPrivateIp(ipRange.getIpRangeStart(),
						ipRange.getIpRangeEnd())) {
					ipRange.setDataStatus("私有IP");
				} else {
					if (Integer
							.parseInt(ipRange.getIpRangeStart().split("\\.")[0]) != Integer
							.parseInt(ipRange.getIpRangeEnd().split("\\.")[0])
							|| Integer.parseInt(ipRange.getIpRangeStart()
									.split("\\.")[1]) != Integer
									.parseInt(ipRange.getIpRangeEnd().split(
											"\\.")[1])) {
						ipRange.setDataStatus("資料錯誤");
					} else if (Integer.parseInt(ipRange.getIpRangeStart()
							.split("\\.")[2])
							* 1000
							+ Integer.parseInt(ipRange.getIpRangeStart().split(
									"\\.")[3]) > Integer.parseInt(ipRange
							.getIpRangeEnd().split("\\.")[2])
							* 1000
							+ Integer.parseInt(ipRange.getIpRangeEnd().split(
									"\\.")[3])) {
						ipRange.setDataStatus("資料錯誤");
					} else {

						IpRange repeatIpRange = checkRepeatIpRange(
								ipRange.getIpRangeStart(),
								ipRange.getIpRangeEnd(),
								ipRangeService.getAllIpList(0));
						if (repeatIpRange != null) {
							ipRange.setDataStatus(repeatIpRange.getCustomer()
									.getName() + "IP");
						}
					}
				}

				if (ipRange.getDataStatus() == null) {
					ipRange.setDataStatus("正常");
				}

				if (ipRange.getDataStatus().equals("正常")
						&& !originalData.contains(ipRange)) {
					IpRange repeatIpRange = checkRepeatIpRange(
							ipRange.getIpRangeStart(), ipRange.getIpRangeEnd(),
							new ArrayList<IpRange>(checkRepeatRow.values()));
					if (repeatIpRange != null) {
						ipRange.setDataStatus("資料重複");
					} else {
						checkRepeatRow.put(
								ipRange.getIpRangeStart()
										+ ipRange.getIpRangeEnd(), ipRange);
						++normal;
					}

				}

				originalData.add(ipRange);
			}

			List<IpRange> excelData = new ArrayList<IpRange>(originalData);

			DataSet<IpRange> ds = initDataSet();
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

		DataSet<IpRange> ds = initDataSet();
		ds.getPager().setTotalRecord((long) importList.size());

		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int i = 0;
		while (i < importList.size()) {
			if (i >= first && i < last) {
				ds.getResults().add((IpRange) importList.get(i));
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
					ds.getResults().add((IpRange) importList.get(j));
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
					if (((IpRange) importList
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
					if (((IpRange) importList
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
				ipRange = (IpRange) importList.get(index);

				String[] ipStartNums = ipRange.getIpRangeStart().split("\\.");
				String[] ipEndNums = ipRange.getIpRangeEnd().split("\\.");

				StringBuilder ipStartBuilder = new StringBuilder();
				StringBuilder ipEndBuilder = new StringBuilder();

				int i = 0;
				while (i < 4) {
					if (i < 3) {
						ipStartBuilder.append(Integer.parseInt(ipStartNums[i])
								+ ".");
						ipEndBuilder.append(Integer.parseInt(ipEndNums[i])
								+ ".");
					} else {
						ipStartBuilder.append(Integer.parseInt(ipStartNums[i]));
						ipEndBuilder.append(Integer.parseInt(ipEndNums[i]));
					}

					i++;
				}

				ipRange.setIpRangeStart(ipStartBuilder.toString());
				ipRange.setIpRangeEnd(ipEndBuilder.toString());
				ipRangeService.save(ipRange, getLoginUser());

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
		// reportFile = "ipRange_sample.xlsx";
		getEntity().setReportFile("ipRange_sample.xlsx");
		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("customer");
		// Create row object
		XSSFRow row;
		// This data needs to be written (Object[])
		Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
		empinfo.put("1", new Object[] { "IP Range開始", "IP Range結束" });

		empinfo.put("2", new Object[] { "122.15.26.91", "122.15.26.98" });

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

	protected boolean isIp(String ip) {
		return ESAPI.validator().isValidInput("IpRange ip", ip, "IPAddress",
				15, false);
	}

	protected boolean isPrivateIp(String ipStart, String ipEnd) {
		String[] ipStartNum = ipStart.split("\\.");
		String[] ipEndNum = ipEnd.split("\\.");

		// 10.0.0.0~10.255.255.255
		if (Integer.parseInt(ipStartNum[0]) == 10
				&& Integer.parseInt(ipEndNum[0]) == 10) {
			return true;
		}

		// 172.16.0.0~172.31.255.255
		if (ipStartNum[0].equals("172") && ipEndNum[0].equals("172")) {
			if (Integer.parseInt(ipStartNum[1]) >= 16
					&& Integer.parseInt(ipStartNum[1]) <= 31) {
				return true;
			}
			if (Integer.parseInt(ipEndNum[1]) >= 16
					&& Integer.parseInt(ipEndNum[1]) <= 31) {
				return true;
			}
		}

		// 192.168.0.0~192.168.255.255
		if (ipStartNum[0].equals("192") && ipEndNum[0].equals("192")
				&& ipStartNum[0].equals("168") && ipEndNum[0].equals("168")) {
			return true;
		}

		return false;
	}

	protected IpRange checkRepeatIpRange(String ipStart, String ipEnd,
			List<IpRange> allIpList) {

		String[] ipStartNum = ipStart.split("\\.");
		String[] ipEndNum = ipEnd.split("\\.");
		List<Integer> entityIpRange = new ArrayList<Integer>();
		for (int i = Integer.parseInt(ipStartNum[2]) * 1000
				+ Integer.parseInt(ipStartNum[3]); i <= Integer
				.parseInt(ipEndNum[2]) * 1000 + Integer.parseInt(ipEndNum[3]); i++) {
			entityIpRange.add(i);
			if (i % 1000 == 255) {
				i = i - 255 + 999;
			}
		}

		for (int i = 0; i < allIpList.size(); i++) {
			String[] existIpStartNum = allIpList.get(i).getIpRangeStart()
					.split("\\.");
			String[] existIpEndNum = allIpList.get(i).getIpRangeEnd()
					.split("\\.");

			if (ipStartNum[0].equals(existIpStartNum[0])
					&& ipStartNum[1].equals(existIpStartNum[1])) {

				for (int j = 0; j < entityIpRange.size(); j++) {
					if (entityIpRange.get(j) >= Integer
							.parseInt(existIpStartNum[2])
							* 1000
							+ Integer.parseInt(existIpStartNum[3])
							&& entityIpRange.get(j) <= Integer
									.parseInt(existIpEndNum[2])
									* 1000
									+ Integer.parseInt(existIpEndNum[3])) {
						return allIpList.get(i);
					}

				}
			}
		}
		return null;
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
}
