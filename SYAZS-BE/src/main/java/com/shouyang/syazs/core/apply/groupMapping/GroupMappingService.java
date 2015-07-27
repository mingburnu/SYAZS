package com.shouyang.syazs.core.apply.groupMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceSerNo;

@Service
public class GroupMappingService extends GenericServiceSerNo<GroupMapping> {

	@Autowired
	private GroupMappingDao dao;

	@Override
	public DataSet<GroupMapping> getByRestrictions(DataSet<GroupMapping> ds)
			throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		DsRestrictions restrictions = getDsRestrictions();

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<GroupMapping> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public void delByCustomerName(String title) {
		dao.deleteByTitle(title);
	}

	public GroupMapping getRootMapping(String title) throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.eq("level", 0);
		restrictions.eq("title", title);
		return dao.findByRestrictions(restrictions).get(0);
	}
}
