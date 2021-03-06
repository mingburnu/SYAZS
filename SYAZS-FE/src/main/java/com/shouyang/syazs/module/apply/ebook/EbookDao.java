package com.shouyang.syazs.module.apply.ebook;

import org.hibernate.Criteria;
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
}
