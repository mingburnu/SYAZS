package com.shouyang.syazs.module.apply.ebook;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoFull;

@Repository
public class EbookDao extends ModuleDaoFull<Ebook> {

	@SuppressWarnings("unchecked")
	public Ebook findByEbkSerNoRefSeNo(long ebkSerNo, long refSerNo) {
		Criteria criteria = getSession().createCriteria(Ebook.class);
		criteria.createAlias("referenceOwners", "referenceOwners");
		criteria.add(Restrictions.eq("serNo", ebkSerNo));
		criteria.add(Restrictions.eq("referenceOwners.serNo", refSerNo));

		List<Ebook> list = (List<Ebook>) criteria.list();
		if (list.size() != 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
