package com.shouyang.syazs.module.apply.ebook;

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
import com.shouyang.syazs.module.apply.ebook.Ebook;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwner;

@Repository
public class EbookDao extends ModuleDaoFull<Ebook> {

	@Autowired
	private Ebook ebook;

	public long count(ReferenceOwner owner) {
		Criteria single = getSession().createCriteria(Ebook.class)
				.createAlias("referenceOwners", "r")
				.add(Restrictions.eq("r.serNo", owner.getSerNo()));
		single.setProjection(Projections.rowCount());

		Criteria packaged = getSession().createCriteria(Ebook.class)
				.createAlias("database", "d")
				.createAlias("d.referenceOwners", "dr")
				.add(Restrictions.eq("dr.serNo", owner.getSerNo()));
		packaged.setProjection(Projections.rowCount());
		return (Long) single.list().get(0) + (Long) packaged.list().get(0);
	}

	public DataSet<Ebook> findByOwner(DataSet<Ebook> ds, ReferenceOwner owner) {
		SQLQuery sqlQuery = getSession()
				.createSQLQuery(
						"SELECT ebook.serNo, ebook.bookname, ebook.authername, ebook.publishname FROM ebook WHERE ebook.dat_serNo in ( SELECT db.serNo FROM db inner join ref_dat on db.serNo=ref_dat.dat_serNo inner join referenceOwner on referenceOwner.serNo=ref_dat.ref_serNo WHERE referenceOwner.serNo="
								+ owner.getSerNo()
								+ " ) UNION SELECT ebook.serNo, ebook.bookname, ebook.authername, ebook.publishname FROM ebook inner join ref_ebk on ebook.serNo=ref_ebk.ebk_serNo inner join referenceOwner on referenceOwner.serNo=ref_ebk.ref_serNo WHERE referenceOwner.serNo="
								+ owner.getSerNo());

		sqlQuery.setFirstResult(ds.getPager().getOffset());
		sqlQuery.setMaxResults(ds.getPager().getRecordPerPage());
		List<?> list = sqlQuery.list();
		Iterator<?> iterator = list.iterator();

		List<Ebook> data = new ArrayList<Ebook>();
		while (iterator.hasNext()) {
			Object[] row = (Object[]) iterator.next();

			ebook = new Ebook();
			ebook.setSerNo(Long.parseLong(row[0].toString()));
			ebook.setBookName(row[1].toString());
			if (row[2] != null) {
				ebook.setAutherName(row[2].toString());
			}

			if (row[3] != null) {
				ebook.setPublishName(row[3].toString());
			}

			data.add(ebook);
		}

		ds.getPager().setTotalRecord(count(owner));
		ds.setResults(data);
		return ds;
	}
}
