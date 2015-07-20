package com.shouyang.syazs.core.apply.beLogs;

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
import com.shouyang.syazs.core.converter.JodaTimeConverter;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionLog;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BeLogsAction extends GenericWebActionLog<BeLogs> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4677113896253423654L;

	@Autowired
	private BeLogsService beLogsService;

	@Autowired
	private BeLogs beLogs;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private JodaTimeConverter jodaTimeConverter;

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

			DataSet<BeLogs> ds = beLogsService.getByRestrictions(initDataSet());

			if (ds.getResults().size() == 0
					&& ds.getPager().getCurrentPage() > 1) {
				ds.getPager().setCurrentPage(
						(int) (ds.getPager().getTotalRecord()
								/ ds.getPager().getRecordPerPage() + 1));
				ds = beLogsService.getByRestrictions(ds);
			}

			setDs(ds);
			return LIST;
		} else {
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

	public String exports() throws Exception {
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

			DataSet<BeLogs> ds = initDataSet();
			ds.getPager().setRecordPerPage(Integer.MAX_VALUE);
			ds = beLogsService.getByRestrictions(ds);

			getEntity().setReportFile("beLogs.xlsx");

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
				beLogs = ds.getResults().get(i);
				empinfo.put(
						String.valueOf(i + 2),
						new Object[] {
								getDateString(getEntity().getStart()) + "~"
										+ getDateString(getEntity().getEnd()),
								String.valueOf(beLogs.getRank()),
								beLogs.getAccountNumber().getUserId(),
								beLogs.getAccountNumber().getUserName(),
								beLogs.getAccountNumber().getRole().getRole(),
								beLogs.getCustomer().getName(),
								beLogs.getAccountNumber().getStatus()
										.getStatus(),
								String.valueOf(beLogs.getCount()) });

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

	protected String getDateString(LocalDateTime dateTime) {
		return jodaTimeConverter.convertToString(null, dateTime);
	}
}
