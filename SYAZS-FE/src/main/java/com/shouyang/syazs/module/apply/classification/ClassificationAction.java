package com.shouyang.syazs.module.apply.classification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.web.GenericWebActionSerNo;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClassificationAction extends GenericWebActionSerNo<Classification> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2472341597272275749L;

	@Autowired
	private ClassificationService classificationService;

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
		DataSet<Classification> ds = classificationService
				.getByRestrictions(initDataSet());

		ds.getPager().setRecordPerPage(Integer.MAX_VALUE);

		ds = classificationService.getByRestrictions(ds);

		if (StringUtils.isBlank(getEntity().getDataStatus())) {
			getEntity().setDataStatus("done");
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

}
