package com.shouyang.syazs.core.apply.group;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoGroup;

@Repository
public class GroupDao extends ModuleDaoGroup<Group> {

	@SuppressWarnings("unchecked")
	public List<Group> getMainGroups(long cusSerNo) {
		Criteria criteria = getSession().createCriteria(Group.class);
		criteria.createAlias("groupMapping", "groupMapping");
		criteria.add(Restrictions.eq("customer.serNo", cusSerNo));
		criteria.add(Restrictions.eq("groupMapping.level", 1));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Group> getSubGroups(long cusSerNo, Group mainGroup) {
		Criteria criteria = getSession().createCriteria(Group.class);
		criteria.createAlias("groupMapping", "groupMapping");
		criteria.add(Restrictions.eq("customer.serNo", cusSerNo));
		criteria.add(Restrictions.eq("groupMapping.parentGroupMapping",
				mainGroup.getGroupMapping()));
		criteria.add(Restrictions.eq("groupMapping.level", mainGroup
				.getGroupMapping().getLevel() + 1));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Group> getMainGroupByName(String groupName, long cusSerNo) {
		Criteria criteria = getSession().createCriteria(Group.class);
		criteria.createAlias("groupMapping", "groupMapping");
		criteria.add(Restrictions.eq("customer.serNo", cusSerNo));
		criteria.add(Restrictions.eq("groupMapping.level", 1));
		criteria.add(Restrictions.eq("groupName", groupName));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Group> getSubGroupByName(String groupName, long cusSerNo,
			Group mainGroup) {
		Criteria criteria = getSession().createCriteria(Group.class);
		criteria.createAlias("groupMapping", "groupMapping");
		criteria.add(Restrictions.eq("customer.serNo", cusSerNo));
		criteria.add(Restrictions.eq("groupMapping.parentGroupMapping",
				mainGroup.getGroupMapping()));
		criteria.add(Restrictions.eq("groupMapping.level", mainGroup
				.getGroupMapping().getLevel() + 1));
		criteria.add(Restrictions.eq("groupName", groupName));
		return criteria.list();
	}
}
