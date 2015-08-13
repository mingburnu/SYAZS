package com.shouyang.syazs.module.apply.journal;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoFull;

@Repository
public class JournalDao extends ModuleDaoFull<Journal> {

	@SuppressWarnings("unchecked")
	public Journal findByJouSerNoRefSeNo(long jouSerNo, long refSerNo) {
		Criteria criteria = getSession().createCriteria(Journal.class);
		criteria.createAlias("referenceOwners", "referenceOwners");
		criteria.add(Restrictions.eq("serNo", jouSerNo));
		criteria.add(Restrictions.eq("referenceOwners.serNo", refSerNo));

		List<Journal> list = (List<Journal>) criteria.list();
		if (list.size() != 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
