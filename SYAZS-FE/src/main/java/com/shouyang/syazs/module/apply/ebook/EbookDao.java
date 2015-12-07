package com.shouyang.syazs.module.apply.ebook;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoFull;
import com.shouyang.syazs.module.apply.ebook.Ebook;

@Repository
public class EbookDao extends ModuleDaoFull<Ebook> {
	public long countAll() {
		Criteria criteria = getSession().createCriteria(Ebook.class);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().get(0);
	}

	public long countByOwner(long ownerSerNo) {
		Query query = getSession()
				.createQuery(
						"SELECT COUNT(*)+(SELECT COUNT(*) FROM Ebook e JOIN e.database ed JOIN ed.referenceOwners edr WHERE edr.serNo=:ownerSerNo) FROM Ebook e JOIN e.referenceOwners er WHERE er.serNo=:ownerSerNo");
		query.setParameter("ownerSerNo", ownerSerNo);
		return (long) query.list().get(0);
	}
}
