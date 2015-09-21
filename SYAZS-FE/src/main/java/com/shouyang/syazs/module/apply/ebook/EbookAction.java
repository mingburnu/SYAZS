package com.shouyang.syazs.module.apply.ebook;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionFull;
import com.shouyang.syazs.module.apply.database.Database;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwner;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwnerService;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EbookAction extends GenericWebActionFull<Ebook> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2746869215531950704L;

	@Autowired
	private Ebook ebook;

	@Autowired
	private EbookService ebookService;

	@Autowired
	private Database database;

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
		getRequest()
				.setAttribute(
						"list",
						getRequest().getContextPath()
								+ "/crud/apply.ebook.list.action");

		DataSet<Ebook> ds = ebookService.getByRestrictions(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			ds = ebookService.getByRestrictions(ds);
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
		if (getEntity().getRefSerNo() == null || getEntity().getRefSerNo() <= 0) {
			addActionError("Owner Null");
		} else {
			referenceOwner = referenceOwnerService.getBySerNo(getEntity()
					.getRefSerNo());
			if (referenceOwner == null) {
				addActionError("Owner Null");
			}
		}

		if (!hasActionErrors()) {
			getRequest().setAttribute(
					"owner",
					getRequest().getContextPath()
							+ "/crud/apply.ebook.owner.action");

			DataSet<Ebook> ds = initDataSet();
			List<Ebook> ebooks = new ArrayList<Ebook>(
					referenceOwner.getEbooks());
			List<Database> databases = new ArrayList<Database>(
					referenceOwner.getDatabases());

			Iterator<Database> iterator = databases.iterator();
			while (iterator.hasNext()) {
				database = iterator.next();
				ebooks.addAll(database.getEbooks());
			}

			ds.getPager().setTotalRecord((long) ebooks.size());
			int first = ds.getPager().getOffset();
			int last = first + ds.getPager().getRecordPerPage();

			int i = 0;
			while (i < ebooks.size()) {
				if (i >= first && i < last) {
					ds.getResults().add(ebooks.get(i));
				}
				i++;
			}

			if (ds.getResults().size() == 0
					&& ds.getPager().getCurrentPage() > 1) {
				ds.getPager().setCurrentPage(
						(int) Math.ceil(ds.getPager().getTotalRecord()
								/ ds.getPager().getRecordPerPage()));
				first = ds.getPager().getOffset();
				last = first + ds.getPager().getRecordPerPage();

				int j = 0;
				while (j < databases.size()) {
					if (j >= first && j < last) {
						ds.getResults().add(ebooks.get(j));
					}
					j++;
				}

			}

			setDs(ds);
		}

		return LIST;

	}

	public String focus() throws Exception {
		getRequest().setAttribute(
				"focus",
				getRequest().getContextPath()
						+ "/crud/apply.ebook.focus.action");

		DataSet<Ebook> ds = ebookService.getByOption(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			ds = ebookService.getByOption(ds);
		}

		setDs(ds);

		return LIST;
	}

	public String prefix() throws Exception {
		getRequest().setAttribute(
				"prefix",
				getRequest().getContextPath()
						+ "/crud/apply.ebook.prefix.action");
		DataSet<Ebook> ds = ebookService.getByPrefix(initDataSet());

		if (ds.getResults().size() == 0 && ds.getPager().getCurrentPage() > 1) {
			ds.getPager().setCurrentPage(
					(int) Math.ceil(ds.getPager().getTotalRecord()
							/ ds.getPager().getRecordPerPage()));
			ds = ebookService.getByPrefix(ds);
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
			ebook.setBackURL(getEntity().getBackURL());

			setDs(initDataSet());
			setEntity(ebook);
		} else {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		return VIEW;
	}

	public String count() {
		getRequest().setAttribute("count", ebookService.countToatal());
		return COUNT;
	}

	protected boolean hasEntity() throws Exception {
		if (!getEntity().hasSerNo()) {
			return false;
		}

		ebook = ebookService.getBySerNo(getEntity().getSerNo());
		if (ebook == null) {
			return false;
		}

		return true;
	}
}
