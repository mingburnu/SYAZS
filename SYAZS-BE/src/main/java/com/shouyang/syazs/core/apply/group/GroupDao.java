package com.shouyang.syazs.core.apply.group;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.ModuleDaoGroup;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.model.Pager;

@Repository
public class GroupDao extends ModuleDaoGroup<Group> {

	@SuppressWarnings("unchecked")
	public DataSet<Group> getCustomerGroups(DsRestrictions restrictions,
			DataSet<Group> ds) {
		Criteria dataCri = getSession().createCriteria(Group.class);
		Criteria countCri = getSession().createCriteria(Group.class);
		dataCri.createAlias("groupMapping", "groupMapping");
		countCri.createAlias("groupMapping", "groupMapping");

		countCri.add(Restrictions.ne("groupMapping.level", 0));
		dataCri.add(Restrictions.ne("groupMapping.level", 0));

		// add restrictions
		List<Criterion> crions = (List<Criterion>) restrictions.getCriterions();
		for (Criterion crion : crions) {
			countCri.add(crion);
			dataCri.add(crion);
		}

		// add orders
		List<Order> orders = (List<Order>) restrictions.getOrders();
		for (Order order : orders) {
			dataCri.addOrder(order);
		}

		if (ds != null && ds.getPager() != null) { // 分頁
			Pager pager = ds.getPager();

			// count total records
			countCri.setProjection(Projections.rowCount());
			Long totalRecord = (Long) countCri.list().get(0);
			log.debug("totalRecord:" + totalRecord);
			pager.setTotalRecord(totalRecord);

			dataCri.setFirstResult(pager.getOffset());
			dataCri.setMaxResults(pager.getRecordPerPage());
		} else {
			ds = new DataSet<Group>();
		}

		ds.setResults(dataCri.list());

		return ds;
	}

	public Group getRootGroup(long cusSerNo) {
		Criteria criteria = getSession().createCriteria(Group.class);
		criteria.createAlias("groupMapping", "groupMapping");
		criteria.add(Restrictions.eq("customer.serNo", cusSerNo));
		criteria.add(Restrictions.eq("groupMapping.level", 0));
		return (Group) criteria.list().get(0);
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
