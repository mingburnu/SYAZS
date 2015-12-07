package com.shouyang.syazs.module.apply.referenceOwner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;
import com.shouyang.syazs.module.apply.database.DatabaseService;
import com.shouyang.syazs.module.apply.ebook.EbookService;
import com.shouyang.syazs.module.apply.journal.JournalService;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReferenceOwnerAction extends GenericWebActionFull<ReferenceOwner> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2177287834510404474L;

	@Autowired
	private ReferenceOwner referenceOwner;

	@Autowired
	private ReferenceOwnerService referenceOwnerService;

	@Autowired
	private DatabaseService databaseService;

	@Autowired
	private EbookService ebookService;

	@Autowired
	private JournalService journalService;

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
		getRequest().setAttribute("list", "apply.referenceOwner.list.action");

		DataSet<ReferenceOwner> ds = referenceOwnerService
				.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			ds = referenceOwnerService.getByRestrictions(ds);
		}

		List<ReferenceOwner> results = ds.getResults();
		for (int i = 0; i < results.size(); i++) {
			referenceOwner = results.get(i);
			referenceOwner.setCounts(new Long[] {
					databaseService.countByOwner(referenceOwner.getSerNo()),
					ebookService.countByOwner(referenceOwner.getSerNo()),
					journalService.countByOwner(referenceOwner.getSerNo()) });
		}

		setDs(ds);
		return LIST;
	}

	public String notePoint() {
		getSession().put("entityRecord", getEntity());
		getSession().put("pagerRecord", getPager());
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
}
