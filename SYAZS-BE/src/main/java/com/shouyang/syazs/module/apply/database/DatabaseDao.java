package com.shouyang.syazs.module.apply.database;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoFull;

@Repository
public class DatabaseDao extends ModuleDaoFull<Database> {

	@SuppressWarnings("unchecked")
	public Database findByDatSerNoRefSeNo(long datSerNo, long refSerNo) {
		Criteria criteria = getSession().createCriteria(Database.class);
		criteria.createAlias("referenceOwners", "referenceOwners");
		criteria.add(Restrictions.eq("serNo", datSerNo));
		criteria.add(Restrictions.eq("referenceOwners.serNo", refSerNo));

		List<Database> list = (List<Database>) criteria.list();
		if (list.size() != 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
