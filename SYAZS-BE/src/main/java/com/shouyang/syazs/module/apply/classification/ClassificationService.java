package com.shouyang.syazs.module.apply.classification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsQueryLanguage;
import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceSerNo;

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

	public Map<String, Object> getClsDatas() {
		return dao.getMap(new HashMap<String, Object>());
	}

	@Override
	protected GenericDao<Classification> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	@SuppressWarnings("unchecked")
	public List<Long> getByName(String classname) {
		DsQueryLanguage queryLanguage = getDsQueryLanguage();
		queryLanguage
				.setHql("SELECT serNo FROM Classification WHERE LOWER(classname) = :classname");
		queryLanguage.addParameter("classname", classname.trim().toLowerCase());
		return (List<Long>) dao.findByHQL(queryLanguage);
	}
}
