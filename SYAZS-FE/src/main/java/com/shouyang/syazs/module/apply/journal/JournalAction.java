package com.shouyang.syazs.module.apply.journal;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwnerService;

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
	private Customer customer;

	@Autowired
	private ReferenceOwnerService referenceOwnerService;

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

	public String owner() throws Exception {
		if (getEntity().getRefSerNo() == null
				|| getEntity().getRefSerNo() <= 0
				|| referenceOwnerService.getBySerNo(getEntity().getRefSerNo()) == null) {
			addActionError("Owner Null");
		}

		if (!hasActionErrors()) {
			getRequest().setAttribute(
					"owner",
					getRequest().getContextPath()
							+ "/crud/apply.journal.owner.action");

			DataSet<Journal> ds = journalService.getByRefSerNo(initDataSet());

			if (ds.getResults().size() == 0
					&& ds.getPager().getCurrentPage() > 1) {
				ds.getPager().setCurrentPage(
						(int) Math.ceil(ds.getPager().getTotalRecord()
								/ ds.getPager().getRecordPerPage()));
				ds = journalService.getByRefSerNo(ds);
			}

			setDs(ds);

		}

		return LIST;
	}

	public String focus() throws Exception {
		getRequest().setAttribute(
				"focus",
				getRequest().getContextPath()
						+ "/crud/apply.journal.focus.action");

		DataSet<Journal> ds = journalService.getByOption(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			ds = journalService.getByOption(ds);
		}

		setDs(ds);

		return LIST;
	}

	public String prefix() throws Exception {
		getRequest().setAttribute(
				"focus",
				getRequest().getContextPath()
						+ "/crud/apply.journal.prefix.action");
		DataSet<Journal> ds = journalService.getByPrefix(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			ds = journalService.getByPrefix(ds);
		}

		setDs(ds);
		return LIST;
	}

	public String view() throws Exception {
		if (hasEntity()) {
			List<String> ownerNameList = new ArrayList<String>();

			String ownerNames = ownerNameList.toString().replace("[", "")
					.replace("]", "");

			getRequest().setAttribute("ownerNames", ownerNames);
			journal.setBackURL(getEntity().getBackURL());

			setDs(initDataSet());
			setEntity(journal);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return VIEW;
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
