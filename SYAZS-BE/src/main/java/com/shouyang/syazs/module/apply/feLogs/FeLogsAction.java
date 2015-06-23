package com.shouyang.syazs.module.apply.feLogs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.customer.CustomerService;
import com.shouyang.syazs.core.apply.enums.Role;
import com.shouyang.syazs.core.converter.JodaTimeConverter;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.model.Pager;
import com.shouyang.syazs.core.web.GenericCRUDActionLog;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FeLogsAction extends GenericCRUDActionLog<FeLogs> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9182601107720081288L;

	@Autowired
	private FeLogsService feLogsService;

	@Autowired
	private FeLogs feLogs;

	@Autowired
	private Customer customer;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private JodaTimeConverter converter;

	private InputStream inputStream;

	private String reportFile;

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
	public String edit() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		String startDate = getRequest().getParameter("start");
		String endDate = getRequest().getParameter("end");

		if (startDate != null) {
			startDate = startDate.trim();
		}

		if (endDate != null) {
			endDate = endDate.trim();
		}

		String customerName = getRequest().getParameter("customer");

		String cusSerNo = getRequest().getParameter("cusSerNo");
		if (getLoginUser().getRole().equals(Role.管理員)) {
			cusSerNo = String.valueOf(getLoginUser().getCustomer().getSerNo());
		}

		if (cusSerNo == null || !NumberUtils.isDigits(cusSerNo)) {
			addActionError("請正確填寫機構名稱");
		} else {
			if (Long.parseLong(cusSerNo) != 0
					&& customerService.getBySerNo(Long.parseLong(cusSerNo)) == null) {
				addActionError("請正確填寫機構名稱");
			}
		}

		if (!hasActionErrors()) {
			if (StringUtils.isNotBlank(startDate)
					&& getLocalDateTime(startDate) != null) {
				getEntity().setStart(getLocalDateTime(startDate));
				getRequest().setAttribute("startDate", startDate);
			} else {
				getEntity().setStart(LocalDateTime.parse("2015-01-01"));
				getRequest().setAttribute("startDate", "2015-01-01");
			}

			if (StringUtils.isNotBlank(endDate)
					&& getLocalDateTime(endDate) != null) {
				getEntity().setEnd(getLocalDateTime(endDate));
				getRequest().setAttribute("endDate", endDate);
			} else {
				getEntity().setEnd(null);
				getRequest().setAttribute("endDate", "");
			}

			DataSet<FeLogs> ds = initDataSet();

			ds.setPager(Pager.getChangedPager(
					getRequest().getParameter("recordPerPage"), getRequest()
							.getParameter("recordPoint"), ds.getPager()));

			getRequest().setAttribute("endDate", endDate);

			if (Long.parseLong(cusSerNo) > 0) {
				getEntity().setCustomer(
						customerService.getBySerNo(Long.parseLong(cusSerNo)));
				getRequest().setAttribute("customer",
						getEntity().getCustomer().getName());
			} else {
				customer = new Customer();
				customer.setSerNo(Long.parseLong(cusSerNo));
				getEntity().setCustomer(customer);
			}

			getRequest().setAttribute("cusSerNo", cusSerNo);

			ds = feLogsService.getByRestrictions(ds);

			setDs(ds);

			return LIST;
		} else {
			getRequest().setAttribute("startDate", startDate);
			getRequest().setAttribute("endDate", endDate);
			getRequest().setAttribute("customer", customerName);
			getRequest().setAttribute("cusSerNo", cusSerNo);
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
		String startDate = getRequest().getParameter("start");
		String endDate = getRequest().getParameter("end");

		if (startDate != null) {
			startDate = startDate.trim();
		}

		if (endDate != null) {
			endDate = endDate.trim();
		}

		String customerName = getRequest().getParameter("customer");

		String cusSerNo = getRequest().getParameter("cusSerNo");
		if (getLoginUser().getRole().equals(Role.管理員)) {
			cusSerNo = String.valueOf(getLoginUser().getCustomer().getSerNo());
		}

		if (cusSerNo == null || !NumberUtils.isDigits(cusSerNo)) {
			addActionError("請正確填寫機構名稱");
		} else {
			if (Long.parseLong(cusSerNo) != 0
					&& customerService.getBySerNo(Long.parseLong(cusSerNo)) == null) {
				addActionError("請正確填寫機構名稱");
			}
		}

		if (!hasActionErrors()) {
			if (StringUtils.isNotBlank(startDate)
					&& getLocalDateTime(startDate) != null) {
				getEntity().setStart(getLocalDateTime(startDate));
			} else {
				getEntity().setStart(LocalDateTime.parse("2015-01-01"));
				startDate = "2015-01-01";
			}

			if (StringUtils.isNotBlank(endDate)
					&& getLocalDateTime(endDate) != null) {
				getEntity().setEnd(getLocalDateTime(endDate));
			} else {
				getEntity().setEnd(null);
				endDate = "";
			}

			DataSet<FeLogs> ds = initDataSet();

			if (Long.parseLong(cusSerNo) > 0) {
				getEntity().setCustomer(
						customerService.getBySerNo(Long.parseLong(cusSerNo)));
				getRequest().setAttribute("customer",
						getEntity().getCustomer().getName());
			} else {
				customer = new Customer();
				customer.setSerNo(Long.parseLong(cusSerNo));
				getEntity().setCustomer(customer);
			}

			Pager pager = ds.getPager();
			pager.setRecordPerPage(Integer.MAX_VALUE);
			ds.setPager(pager);

			ds = feLogsService.getByRestrictions(ds);

			List<FeLogs> results = ds.getResults();

			reportFile = "feLogs.xlsx";

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
			while (i < results.size()) {
				feLogs = results.get(i);
				empinfo.put(
						String.valueOf(i + 2),
						new Object[] { startDate + "~" + endDate,
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
			setInputStream(new ByteArrayInputStream(boas.toByteArray()));

			return XLSX;
		} else {
			getRequest().setAttribute("startDate", startDate);
			getRequest().setAttribute("endDate", endDate);
			getRequest().setAttribute("customer", customerName);
			getRequest().setAttribute("cusSerNo", cusSerNo);
			return LIST;
		}
	}

	public LocalDateTime getLocalDateTime(String date) {
		LocalDateTime dateTime = (LocalDateTime) converter
				.convertFromString(date);

		return dateTime;
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream
	 *            the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * @return the reportFile
	 */
	public String getReportFile() {
		if (reportFile.equals("feLogs.xlsx")) {
			return reportFile;
		} else {
			return null;
		}
	}

	/**
	 * @param reportFile
	 *            the reportFile to set
	 */
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
}
