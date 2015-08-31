package com.shouyang.syazs.module.apply.resourcesBuyers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceSerNo;

@Service
public class ResourcesBuyersService extends GenericServiceSerNo<ResourcesBuyers> {

	@Autowired
	private ResourcesBuyersDao dao;

	@Override
	public DataSet<ResourcesBuyers> getByRestrictions(
			DataSet<ResourcesBuyers> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		DsRestrictions restrictions = getDsRestrictions();
		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<ResourcesBuyers> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
}
