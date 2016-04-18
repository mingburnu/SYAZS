package com.shouyang.syazs.module.apply.classification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceSerNo;
import com.shouyang.syazs.module.entity.ModuleProperties;

@Service
public class ClassificationService extends GenericServiceSerNo<Classification> {

	@Autowired
	private Classification entity;

	@Autowired
	private ClassificationDao dao;

	@Override
	public DataSet<Classification> getByRestrictions(DataSet<Classification> ds)
			throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<Classification> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public DataSet<ModuleProperties> queryModuleProperties(
			DataSet<ModuleProperties> ds) {
		return dao.query(ds);
	}

}
