package com.shouyang.syazs.module.apply.feLogs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opencsv.CSVWriter;
import com.shouyang.syazs.core.apply.customer.CustomerService;
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
		if (getEntity().getStart() == null) {
			getEntity().setStart(LocalDateTime.parse("2015-01-01"));
		}

		DataSet<FeLogs> ds = feLogsService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
					.doubleValue()
					/ ds.getPager().getRecordPerPage().doubleValue());
			ds.getPager().setCurrentPage(lastPage.intValue());
			ds = feLogsService.getByRestrictions(ds);
		}

		setDs(ds);
		getRequest().setAttribute("keywords", "keywords");
		return LIST;
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

	public String link() throws Exception {
		if (getEntity().getStart() == null) {
			getEntity().setStart(LocalDateTime.parse("2015-01-01"));
		}

		DataSet<FeLogs> ds = feLogsService.getByLink(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
					.doubleValue()
					/ ds.getPager().getRecordPerPage().doubleValue());
			ds.getPager().setCurrentPage(lastPage.intValue());
			ds = feLogsService.getByLink(ds);
		}

		getRequest().setAttribute("clicks", "clicks");
		setDs(ds);
		return LIST;
	}

	public String exportKeyword() throws Exception {
		List<String[]> rows = new ArrayList<String[]>();

		if (getEntity().getStart() == null) {
			getEntity().setStart(LocalDateTime.parse("2015-01-01"));
		}

		DataSet<FeLogs> ds = initDataSet();
		ds.getPager().setRecordPerPage(Integer.MAX_VALUE);
		ds = feLogsService.getByRestrictions(ds);

		rows.add(new String[] { "年月", "名次", "關鍵字", "次數" });

		int i = 0;
		while (i < ds.getResults().size()) {
			feLogs = ds.getResults().get(i);
			rows.add(new String[] {
					dateToString(getEntity().getStart()) + "~"
							+ dateToString(getEntity().getEnd()),
					String.valueOf(feLogs.getRank()), feLogs.getKeyword(),
					String.valueOf(feLogs.getCount()) });
			i++;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos), ',',
				CSVWriter.DEFAULT_QUOTE_CHARACTER,
				CSVWriter.NO_ESCAPE_CHARACTER, "\n");

		writer.writeAll(rows);
		writer.close();

		getEntity().setReportFile("keyword_statics.csv");
		getEntity()
				.setInputStream(new ByteArrayInputStream(baos.toByteArray()));

		return XLSX;
	}

	public String exportClick() throws Exception {
		List<String[]> rows = new ArrayList<String[]>();

		if (getEntity().getStart() == null) {
			getEntity().setStart(LocalDateTime.parse("2015-01-01"));
		}

		DataSet<FeLogs> ds = initDataSet();
		ds.getPager().setRecordPerPage(Integer.MAX_VALUE);
		ds = feLogsService.getByLink(ds);

		rows.add(new String[] { "年月", "名次", "資源類型", "標題", "次數" });

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

			rows.add(new String[] {
					dateToString(getEntity().getStart()) + "~"
							+ dateToString(getEntity().getEnd()),
					String.valueOf(feLogs.getRank()), type, title,
					String.valueOf(feLogs.getCount()) });

			i++;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos), ',',
				CSVWriter.DEFAULT_QUOTE_CHARACTER,
				CSVWriter.NO_ESCAPE_CHARACTER, "\n");

		writer.writeAll(rows);
		writer.close();

		getEntity().setReportFile("click_statics.csv");
		getEntity()
				.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
		return XLSX;
	}
}
