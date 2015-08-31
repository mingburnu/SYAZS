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
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.createAlias("groupMapping", "groupMapping");

		if (entity.getCustomer().getSerNo() > 0) {
			restrictions.eq("customer.serNo", entity.getCustomer().getSerNo());
		}

		restrictions.ne("groupMapping.level", 0);
		restrictions.addOrderAsc("serNo");

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	protected GenericDao<Group> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public Group getRootGroup(long cusSerNo) throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.createAlias("groupMapping", "groupMapping");
		restrictions.eq("customer.serNo", cusSerNo);
		restrictions.eq("groupMapping.level", 0);
		return dao.findByRestrictions(restrictions).get(0);
	}

	public Group getTargetEntity(DataSet<Group> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		Group entity = ds.getEntity();
		DsRestrictions restrictions = getDsRestrictions();

		if (entity.getSerNo() != null) {
			restrictions.eq("serNo", entity.getSerNo());
		} else {
			restrictions.eq("serNo", -1L);
		}

		if (entity.getCustomer() != null) {
			restrictions.eq("customer.serNo", entity.getCustomer().getSerNo());
		} else {
			restrictions.eq("customer.serNo", -1L);
		}

		List<Group> results = dao.findByRestrictions(restrictions);

		if (results.size() == 1) {
			return results.get(0);
		} else {
			return null;
		}
	}

	public List<Group> getSubGroups(long cusSerNo, Group mainGroup)
			throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.createAlias("groupMapping", "groupMapping");
		restrictions.eq("customer.serNo", cusSerNo);
		restrictions.eq("groupMapping.parentGroupMapping",
				mainGroup.getGroupMapping());
		restrictions.eq("groupMapping.level", mainGroup.getGroupMapping()
				.getLevel() + 1);
		return dao.findByRestrictions(restrictions);
	}

	public boolean isRepeatMainGroup(String groupName, long cusSerNo)
			throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.createAlias("groupMapping", "groupMapping");
		restrictions.eq("customer.serNo", cusSerNo);
		restrictions.eq("groupMapping.level", 1);
		restrictions.eq("groupName", groupName);
		List<Group> groups = dao.findByRestrictions(restrictions);

		if (groups.size() > 0) {
			return true;
		}
		return false;
	}

	public Group getRepeatMainGroup(String groupName, long cusSerNo)
			throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.createAlias("groupMapping", "groupMapping");
		restrictions.eq("customer.serNo", cusSerNo);
		restrictions.eq("groupMapping.level", 1);
		restrictions.eq("groupName", groupName);
		List<Group> groups = dao.findByRestrictions(restrictions);

		if (groups.size() > 0) {
			return groups.get(0);
		}
		return null;
	}

	public boolean isRepeatSubGroup(String groupName, long cusSerNo,
			Group parentGroup) throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.createAlias("groupMapping", "groupMapping");
		restrictions.eq("customer.serNo", cusSerNo);
		restrictions.eq("groupMapping.parentGroupMapping",
				parentGroup.getGroupMapping());
		restrictions.eq("groupMapping.level", parentGroup.getGroupMapping()
				.getLevel() + 1);
		restrictions.eq("groupName", groupName);
		List<Group> groups = dao.findByRestrictions(restrictions);

		if (groups.size() > 0) {
			return true;
		}
		return false;
	}

	public Group getRepeatSubGroup(String groupName, long cusSerNo,
			Group parentGroup) throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.createAlias("groupMapping", "groupMapping");
		restrictions.eq("customer.serNo", cusSerNo);
		restrictions.eq("groupMapping.parentGroupMapping",
				parentGroup.getGroupMapping());
		restrictions.eq("groupMapping.level", parentGroup.getGroupMapping()
				.getLevel() + 1);
		restrictions.eq("groupName", groupName);
		List<Group> groups = dao.findByRestrictions(restrictions);

		if (groups.size() > 0) {
			return groups.get(0);
		}
		return null;
	}
}
