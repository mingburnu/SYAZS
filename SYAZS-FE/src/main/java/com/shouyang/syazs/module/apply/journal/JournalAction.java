package com.shouyang.syazs.module.apply.journal;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;
import com.shouyang.syazs.module.apply.database.Database;
import com.shouyang.syazs.module.apply.database.DatabaseService;
import com.shouyang.syazs.module.apply.journal.Journal;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JournalAction extends GenericWebActionFull<Journal> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7274416912972675392L;

	@Autowired
	private Journal journal;

	@Autowired
	private JournalService journalService;

	@Autowired
	private DatabaseService databaseService;

	@Autowired
	private Database database;

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
		getRequest().setAttribute(
				"list",
				getRequest().getContextPath()
						+ "/crud/apply.journal.list.action");

		DataSet<Journal> ds = journalService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
					.doubleValue()
					/ ds.getPager().getRecordPerPage().doubleValue());
			ds.getPager().setCurrentPage(lastPage.intValue());
			journalService.getByRestrictions(ds);
		}

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

	public String prefix() throws Exception {
		getRequest().setAttribute(
				"prefix",
				getRequest().getContextPath()
						+ "/crud/apply.journal.prefix.action");
		DataSet<Journal> ds = journalService.getByPrefix(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			Double lastPage = Math.ceil(ds.getPager().getTotalRecord()
					.doubleValue()
					/ ds.getPager().getRecordPerPage().doubleValue());
			ds.getPager().setCurrentPage(lastPage.intValue());
			journalService.getByPrefix(ds);
		}

		return LIST;
	}

	public String view() throws Exception {
		if (hasEntity()) {
			journal.setBackURL(getEntity().getBackURL());

			setDs(initDataSet());
			setEntity(journal);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return VIEW;
	}

	public void click() {

	}

	public String count() {
		getRequest().setAttribute("count", journalService.countToatal());
		return COUNT;
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
