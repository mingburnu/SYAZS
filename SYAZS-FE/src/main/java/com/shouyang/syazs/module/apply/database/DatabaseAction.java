package com.shouyang.syazs.module.apply.database;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwner;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwnerService;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DatabaseAction extends GenericWebActionFull<Database> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -365109377798301323L;

	@Autowired
	private Database database;

	@Autowired
	private DatabaseService databaseService;

	@Autowired
	private ReferenceOwner referenceOwner;

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
						+ "/crud/apply.database.list.action");

		DataSet<Database> ds = databaseService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			ds = databaseService.getByRestrictions(ds);
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
		referenceOwner = referenceOwnerService.getBySerNo(getEntity()
				.getRefSerNo());

		getRequest().setAttribute(
				"owner",
				getRequest().getContextPath()
						+ "/crud/apply.database.owner.action");

		DataSet<Database> ds = initDataSet();
		List<Database> databases = new ArrayList<Database>(
				referenceOwner.getDatabases());

		ds.getPager().setTotalRecord((long) databases.size());
		int first = ds.getPager().getOffset();
		int last = first + ds.getPager().getRecordPerPage();

		int i = 0;
		while (i < databases.size()) {
			if (i >= first && i < last) {
				ds.getResults().add(databases.get(i));
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
			while (j < databases.size()) {
				if (j >= first && j < last) {
					ds.getResults().add((Database) databases.get(j));
				}
				j++;
			}

		}

		setDs(ds);
		return LIST;
	}

	public String all() throws Exception {
		DataSet<Database> ds = initDataSet();
		ds.setResults(databaseService.getAllDb());
		setDs(ds);

		return LIST;
	}

	public String view() throws Exception {
		if (hasEntity()) {
			database.setBackURL(getEntity().getBackURL());

			setDs(initDataSet());
			setEntity(database);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return VIEW;
	}

	public void click() {

	}

	public String count() {
		getRequest().setAttribute("count", databaseService.countToatal());
		return COUNT;
	}

	protected boolean hasEntity() throws Exception {
		if (!getEntity().hasSerNo()) {
			return false;
		}

		database = databaseService.getBySerNo(getEntity().getSerNo());
		if (database == null) {
			return false;
		}

		return true;
	}
}
