package com.shouyang.syazs.core.apply.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceGroup;
import com.shouyang.syazs.core.util.DsBeanFactory;

@Service
public class GroupService extends GenericServiceGroup<Group> {

	@Autowired
	private GroupDao dao;

	@Override
	public DataSet<Group> getByRestrictions(DataSet<Group> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		DsRestrictions restrictions = DsBeanFactory.getDsRestrictions();

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<Group> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public Group getCustomerGroup(Customer entity) throws Exception {
		DsRestrictions restrictions = DsBeanFactory.getDsRestrictions();
		restrictions.eq("customer.serNo", entity.getSerNo());
		return dao.findByRestrictions(restrictions).get(0);
	}
}
