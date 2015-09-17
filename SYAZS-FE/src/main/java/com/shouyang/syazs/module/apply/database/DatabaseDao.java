package com.shouyang.syazs.module.apply.database;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoFull;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwner;

@Repository
public class DatabaseDao extends ModuleDaoFull<Database> {

	public long count(ReferenceOwner owner) {
		Criteria criteria = getSession().createCriteria(Database.class)
				.createAlias("referenceOwners", "r")
				.add(Restrictions.eq("r.serNo", owner.getSerNo()));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().get(0);
	}
}
