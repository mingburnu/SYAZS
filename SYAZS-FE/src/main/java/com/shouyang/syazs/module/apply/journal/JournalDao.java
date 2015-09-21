package com.shouyang.syazs.module.apply.journal;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoFull;

@Repository
public class JournalDao extends ModuleDaoFull<Journal> {
	public long countAll() {
		Criteria criteria = getSession().createCriteria(Journal.class);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().get(0);
	}
}
