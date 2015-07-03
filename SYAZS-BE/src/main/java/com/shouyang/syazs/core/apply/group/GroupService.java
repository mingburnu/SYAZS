package com.shouyang.syazs.core.apply.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.apply.groupMapping.GroupMappingService;
import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceGroup;
import com.shouyang.syazs.core.util.DsBeanFactory;

@Service
public class GroupService extends GenericServiceGroup<Group> {

	@Autowired
	private GroupDao dao;

	@Autowired
	private GroupMappingService groupMappingService;

	@Override
	public DataSet<Group> getByRestrictions(DataSet<Group> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		Group entity = ds.getEntity();
		DsRestrictions restrictions = DsBeanFactory.getDsRestrictions();

		if (entity.getCustomer().getSerNo() > 0) {
			restrictions.eq("customer.serNo", entity.getCustomer().getSerNo());
		}

		restrictions.addOrderAsc("serNo");

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<Group> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public boolean isLegalEntity(DataSet<Group> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		Group entity = ds.getEntity();
		DsRestrictions restrictions = DsBeanFactory.getDsRestrictions();
		restrictions.eq("serNo", entity.getSerNo());
		if (entity.getCustomer() != null) {
			restrictions.eq("customer.serNo", entity.getCustomer().getSerNo());
		} else {
			restrictions.eq("customer.serNo", -1L);
		}

		if (dao.findByRestrictions(restrictions).size() == 0) {
			return false;
		}

		return true;
	}

	public List<Group> getMainGroups(long cusSerNo) {
		return dao.getMainGroups(cusSerNo);
	}

	public List<Group> getSubGroups(long cusSerNo, Group mainGroup) {
		return dao.getSubGroups(cusSerNo, mainGroup);
	}

	public boolean isRepeatMainGroup(String groupName, long cusSerNo) {
		List<Group> groups = dao.getMainGroupByName(groupName, cusSerNo);

		if (groups.size() > 0) {
			return true;
		}
		return false;
	}

	public boolean isRepeatSubGroup(String groupName, long cusSerNo,
			Group mainGroup) {
		List<Group> groups = dao.getSubGroupByName(groupName, cusSerNo, mainGroup);

		if (groups.size() > 0) {
			return true;
		}
		return false;
	}
}
