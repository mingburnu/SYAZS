package com.shouyang.syazs.module.apply.database;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoFull;

@Repository
public class DatabaseDao extends ModuleDaoFull<Database> {

	public long countAll() {
		Criteria criteria = getSession().createCriteria(Database.class);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().get(0);
	}

	public long countByOwner(long ownerSerNo) {
		Criteria criteria = getSession().createCriteria(Database.class);
		criteria.createAlias("referenceOwners", "dr");
		criteria.add(Restrictions.eq("dr.serNo", ownerSerNo));
		criteria.setProjection(Projections.rowCount());
		return (long) criteria.list().get(0);
	}
}
