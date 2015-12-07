package com.shouyang.syazs.module.apply.journal;

import org.hibernate.Criteria;
import org.hibernate.Query;
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

	public long countByOwner(long ownerSerNo) {
		Query query = getSession()
				.createQuery(
						"SELECT COUNT(*)+(SELECT COUNT(*) FROM Journal j JOIN j.database jd JOIN jd.referenceOwners jdr WHERE jdr.serNo=:ownerSerNo) FROM Journal j JOIN j.referenceOwners jr WHERE jr.serNo=:ownerSerNo");
		query.setParameter("ownerSerNo", ownerSerNo);
		return (long) query.list().get(0);
	}
}
