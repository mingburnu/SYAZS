package com.shouyang.syazs.module.apply.journal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoFull;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwner;

@Repository
public class JournalDao extends ModuleDaoFull<Journal> {

	@Autowired
	private Journal journal;

	public long count(ReferenceOwner owner) {
		Criteria single = getSession().createCriteria(Journal.class)
				.createAlias("referenceOwners", "r")
				.add(Restrictions.eq("r.serNo", owner.getSerNo()));
		single.setProjection(Projections.rowCount());

		Criteria packaged = getSession().createCriteria(Journal.class)
				.createAlias("database", "d")
				.createAlias("d.referenceOwners", "dr")
				.add(Restrictions.eq("dr.serNo", owner.getSerNo()));
		packaged.setProjection(Projections.rowCount());
		return (Long) single.list().get(0) + (Long) packaged.list().get(0);
	}

	public DataSet<Journal> findByOwner(DataSet<Journal> ds,
			ReferenceOwner owner) {
		SQLQuery sqlQuery = getSession()
				.createSQLQuery(
						"SELECT journal.serNo, journal.title, journal.publishname, journal.publishyear FROM journal WHERE journal.dat_serNo in ( SELECT db.serNo FROM db inner join ref_dat on db.serNo=ref_dat.dat_serNo inner join referenceOwner on referenceOwner.serNo=ref_dat.ref_serNo WHERE referenceOwner.serNo="
								+ owner.getSerNo()
								+ " ) UNION SELECT journal.serNo, journal.title, journal.publishname, journal.publishyear FROM journal inner join ref_jou on journal.serNo=ref_jou.jou_serNo inner join referenceOwner on referenceOwner.serNo=ref_jou.ref_serNo WHERE referenceOwner.serNo="
								+ owner.getSerNo());

		sqlQuery.setFirstResult(ds.getPager().getOffset());
		sqlQuery.setMaxResults(ds.getPager().getRecordPerPage());
		List<?> list = sqlQuery.list();
		Iterator<?> iterator = list.iterator();

		List<Journal> data = new ArrayList<Journal>();
		while (iterator.hasNext()) {
			Object[] row = (Object[]) iterator.next();

			journal = new Journal();
			journal.setSerNo(Long.parseLong(row[0].toString()));
			journal.setTitle(row[1].toString());
			if (row[2] != null) {
				journal.setPublishName(row[2].toString());
			}

			if (row[3] != null) {
				journal.setPublishYear(row[3].toString());
			}

			data.add(journal);
		}

		ds.getPager().setTotalRecord(count(owner));
		ds.setResults(data);
		return ds;
	}
}
