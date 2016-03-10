package com.shouyang.syazs.module.apply.feLogs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.apply.customer.CustomerService;
import com.shouyang.syazs.core.apply.enums.Role;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionLog;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FeLogsAction extends GenericWebActionLog<FeLogs> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9182601107720081288L;

	@Autowired
	private FeLogsService feLogsService;

	@Autowired
	private FeLogs feLogs;

	@Autowired
	private CustomerService customerService;

	@Override
	protected void validateSave() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validateUpdate() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validateDelete() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String add() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String edit() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		if (getLoginUser().getRole().equals(Role.管理員)) {
			getEntity().getCustomer().setSerNo(
					getLoginUser().getCustomer().getSerNo());
		}

		if (!getEntity().getCustomer().hasSerNo()) {
			addActionError("請正確填寫機構名稱");
		} else {
			if (getEntity().getCustomer().getSerNo() < 0
					|| (getEntity().getCustomer().getSerNo() != 0 && customerService
							.getBySerNo(getEntity().getCustomer().getSerNo()) == null)) {
				addActionError("請正確填寫機構名稱");
			}
		}

		if (!hasActionErrors()) {
			if (getEntity().getStart() == null) {
				getEntity().setStart(LocalDateTime.parse("2015-01-01"));
			}

			if (getEntity().getCustomer().getSerNo() > 0) {
				getEntity().setCustomer(
						customerService.getBySerNo(getEntity().getCustomer()
								.getSerNo()));
			}

			DataSet<FeLogs> ds = feLogsService.getByRestrictions(initDataSet());

			if (ds.getResults().size() == 0
					&& ds.getPager().getCurrentPage() > 1) {
				Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
						.doubleValue()
						/ ds.getPager().getRecordPerPage().doubleValue());
				ds.getPager().setCurrentPage(lastPage.intValue());
				ds = feLogsService.getByRestrictions(ds);
			}

			setDs(ds);
			getRequest().setAttribute("keywords", "keywords");
			return LIST;
		} else {
			getRequest().setAttribute("keywords", "keywords");
			return LIST;
		}
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String rank() throws Exception {
		if (getLoginUser().getRole().equals(Role.管理員)) {
			getEntity().getCustomer().setSerNo(
					getLoginUser().getCustomer().getSerNo());
		}

		if (!getEntity().getCustomer().hasSerNo()) {
			addActionError("請正確填寫機構名稱");
		} else {
			if (getEntity().getCustomer().getSerNo() < 0
					|| (getEntity().getCustomer().getSerNo() != 0 && customerService
							.getBySerNo(getEntity().getCustomer().getSerNo()) == null)) {
				addActionError("請正確填寫機構名稱");
			}
		}

		if (!hasActionErrors()) {
			if (getEntity().getStart() == null) {
				getEntity().setStart(LocalDateTime.parse("2015-01-01"));
			}

			if (getEntity().getCustomer().getSerNo() > 0) {
				getEntity().setCustomer(
						customerService.getBySerNo(getEntity().getCustomer()
								.getSerNo()));
			}

			DataSet<FeLogs> ds = feLogsService.getByLogin(initDataSet());

			if (ds.getResults().size() == 0
					&& ds.getPager().getCurrentPage() > 1) {
				Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
						.doubleValue()
						/ ds.getPager().getRecordPerPage().doubleValue());
				ds.getPager().setCurrentPage(lastPage.intValue());
				ds = feLogsService.getByLogin(ds);
			}

			getRequest().setAttribute("logins", "logins");
			setDs(ds);
			return LIST;
		} else {
			getRequest().setAttribute("logins", "logins");
			return LIST;
		}
	}

	public String link() throws Exception {
		if (getLoginUser().getRole().equals(Role.管理員)) {
			getEntity().getCustomer().setSerNo(
					getLoginUser().getCustomer().getSerNo());
		}

		if (!getEntity().getCustomer().hasSerNo()) {
			addActionError("請正確填寫機構名稱");
		} else {
			if (getEntity().getCustomer().getSerNo() < 0
					|| (getEntity().getCustomer().getSerNo() != 0 && customerService
							.getBySerNo(getEntity().getCustomer().getSerNo()) == null)) {
				addActionError("請正確填寫機構名稱");
			}
		}

		if (!hasActionErrors()) {
			if (getEntity().getStart() == null) {
				getEntity().setStart(LocalDateTime.parse("2015-01-01"));
			}

			if (getEntity().getCustomer().getSerNo() > 0) {
				getEntity().setCustomer(
						customerService.getBySerNo(getEntity().getCustomer()
								.getSerNo()));
			}

			DataSet<FeLogs> ds = feLogsService.getByLink(initDataSet());

			if (ds.getResults().size() == 0
					&& ds.getPager().getCurrentPage() > 1) {
				Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
						.doubleValue()
						/ ds.getPager().getRecordPerPage().doubleValue());
				ds.getPager().setCurrentPage(lastPage.intValue());
				ds = feLogsService.getByLink(ds);
			}

			getRequest().setAttribute("clicks", "clicks");
			setDs(ds);
			return LIST;
		} else {
			getRequest().setAttribute("clicks", "clicks");
			return LIST;
		}
	}

	public String exportKeyword() throws Exception {
		if (getLoginUser().getRole().equals(Role.管理員)) {
			getEntity().getCustomer().setSerNo(
					getLoginUser().getCustomer().getSerNo());
		}

		if (!getEntity().getCustomer().hasSerNo()) {
			addActionError("請正確填寫機構名稱");
		} else {
			if (getEntity().getCustomer().getSerNo() < 0
					|| (getEntity().getCustomer().getSerNo() != 0 && customerService
							.getBySerNo(getEntity().getCustomer().getSerNo()) == null)) {
				addActionError("請正確填寫機構名稱");
			}
		}

		if (!hasActionErrors()) {
			if (getEntity().getStart() == null) {
				getEntity().setStart(LocalDateTime.parse("2015-01-01"));
			}

			DataSet<FeLogs> ds = initDataSet();
			ds.getPager().setRecordPerPage(Integer.MAX_VALUE);
			ds = feLogsService.getByRestrictions(ds);

			getEntity().setReportFile("keyword_statics.xlsx");

			// Create blank workbook
			XSSFWorkbook workbook = new XSSFWorkbook();
			// Create a blank sheet
			XSSFSheet spreadsheet = workbook.createSheet("keyword statics");
			// Create row object
			XSSFRow row;
			// This data needs to be written (Object[])
			Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
			empinfo.put("1", new Object[] { "年月", "名次", "關鍵字", "次數" });

			int i = 0;
			while (i < ds.getResults().size()) {
				feLogs = ds.getResults().get(i);
				empinfo.put(
						String.valueOf(i + 2),
						new Object[] {
								dateToString(getEntity().getStart()) + "~"
										+ dateToString(getEntity().getEnd()),
								String.valueOf(feLogs.getRank()),
								feLogs.getKeyword(),
								String.valueOf(feLogs.getCount()) });
				i++;
			}

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
			getEntity().setInputStream(
					new ByteArrayInputStream(boas.toByteArray()));

			return XLSX;
		} else {
			return null;
		}
	}

	public String exportLogin() throws Exception {
		if (getLoginUser().getRole().equals(Role.管理員)) {
			getEntity().getCustomer().setSerNo(
					getLoginUser().getCustomer().getSerNo());
		}
		if (!getEntity().getCustomer().hasSerNo()) {
			addActionError("請正確填寫機構名稱");
		} else {
			if (getEntity().getCustomer().getSerNo() < 0
					|| (getEntity().getCustomer().getSerNo() != 0 && customerService
							.getBySerNo(getEntity().getCustomer().getSerNo()) == null)) {
				addActionError("請正確填寫機構名稱");
			}
		}

		if (!hasActionErrors()) {
			if (getEntity().getStart() == null) {
				getEntity().setStart(LocalDateTime.parse("2015-01-01"));
			}

			DataSet<FeLogs> ds = initDataSet();
			ds.getPager().setRecordPerPage(Integer.MAX_VALUE);
			ds = feLogsService.getByLogin(ds);

			getEntity().setReportFile("login_statics.xlsx");

			// Create blank workbook
			XSSFWorkbook workbook = new XSSFWorkbook();
			// Create a blank sheet
			XSSFSheet spreadsheet = workbook.createSheet("login statics");
			// Create row object
			XSSFRow row;
			// This data needs to be written (Object[])
			Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
			empinfo.put("1", new Object[] { "年月", "名次", "帳號", "用戶姓名", "用戶身分",
					"客戶名稱", "狀態", "次數" });

			int i = 0;
			while (i < ds.getResults().size()) {
				feLogs = ds.getResults().get(i);
				empinfo.put(
						String.valueOf(i + 2),
						new Object[] {
								dateToString(getEntity().getStart()) + "~"
										+ dateToString(getEntity().getEnd()),
								String.valueOf(feLogs.getRank()),
								feLogs.getAccountNumber().getUserId(),
								feLogs.getAccountNumber().getUserName(),
								feLogs.getAccountNumber().getRole().getRole(),
								feLogs.getCustomer().getName(),
								feLogs.getAccountNumber().getStatus()
										.getStatus(),
								String.valueOf(feLogs.getCount()) });

				i++;
			}

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
			getEntity().setInputStream(
					new ByteArrayInputStream(boas.toByteArray()));
			return XLSX;
		} else {
			return null;
		}
	}

	public String exportClick() throws Exception {
		if (getLoginUser().getRole().equals(Role.管理員)) {
			getEntity().getCustomer().setSerNo(
					getLoginUser().getCustomer().getSerNo());
		}
		if (!getEntity().getCustomer().hasSerNo()) {
			addActionError("請正確填寫機構名稱");
		} else {
			if (getEntity().getCustomer().getSerNo() < 0
					|| (getEntity().getCustomer().getSerNo() != 0 && customerService
							.getBySerNo(getEntity().getCustomer().getSerNo()) == null)) {
				addActionError("請正確填寫機構名稱");
			}
		}

		if (!hasActionErrors()) {
			if (getEntity().getStart() == null) {
				getEntity().setStart(LocalDateTime.parse("2015-01-01"));
			}

			DataSet<FeLogs> ds = initDataSet();
			ds.getPager().setRecordPerPage(Integer.MAX_VALUE);
			ds = feLogsService.getByLink(ds);

			getEntity().setReportFile("click_statics.xlsx");

			// Create blank workbook
			XSSFWorkbook workbook = new XSSFWorkbook();
			// Create a blank sheet
			XSSFSheet spreadsheet = workbook.createSheet("click statics");
			// Create row object
			XSSFRow row;
			// This data needs to be written (Object[])
			Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
			empinfo.put("1", new Object[] { "年月", "名次", "資源類型", "標題", "次數",
					"客戶名稱" });

			int i = 0;
			while (i < ds.getResults().size()) {
				feLogs = ds.getResults().get(i);
				String type = null;
				String title = null;
				if (feLogs.getDatabase() != null) {
					type = "資料庫";
					title = feLogs.getDatabase().getDbTitle();
				} else if (feLogs.getEbook() != null) {
					type = "電子書";
					title = feLogs.getEbook().getBookName();
				} else if (feLogs.getJournal() != null) {
					type = "期刊";
					title = feLogs.getJournal().getTitle();
				}

				empinfo.put(String.valueOf(i + 2),
						new Object[] {
								dateToString(getEntity().getStart()) + "~"
										+ dateToString(getEntity().getEnd()),
								String.valueOf(feLogs.getRank()), type, title,
								String.valueOf(feLogs.getCount()),
								feLogs.getCustomer().getName() });

				i++;
			}

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
			getEntity().setInputStream(
					new ByteArrayInputStream(boas.toByteArray()));
			return XLSX;
		} else {
			return null;
		}
	}
}
