package com.shouyang.syazs.module.apply.journal;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoFull;

@Repository
public class JournalDao extends ModuleDaoFull<Journal> {
	public void name() {
		SQLQuery sqlQuery = getSession()
				.createSQLQuery(
						"SELECT journal.* FROM journal WHERE journal.dat_serNo in ( SELECT db.serNo FROM db inner join ref_dat on db.serNo=ref_dat.dat_serNo inner join referenceOwner on referenceOwner.serNo=ref_dat.ref_serNo WHERE referenceOwner.serNo=1 ) UNION SELECT journal.* FROM journal inner join ref_jou on journal.serNo=ref_jou.jou_serNo inner join referenceOwner on referenceOwner.serNo=ref_jou.ref_serNo WHERE referenceOwner.serNo=1");

		List<?> list = sqlQuery.list();
		log.info(list.size());
	}
}
