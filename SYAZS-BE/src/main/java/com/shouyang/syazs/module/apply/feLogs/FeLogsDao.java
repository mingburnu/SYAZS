package com.shouyang.syazs.module.apply.feLogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.apply.enums.Act;
import com.shouyang.syazs.core.converter.JodaTimeConverter;
import com.shouyang.syazs.core.dao.ModuleDaoLog;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.module.apply.database.Database;
import com.shouyang.syazs.module.apply.ebook.Ebook;
import com.shouyang.syazs.module.apply.journal.Journal;

@Repository
public class FeLogsDao extends ModuleDaoLog<FeLogs> {

	@Autowired
	private FeLogs feLogs;

	@Autowired
	private JodaTimeConverter converter;

	public DataSet<FeLogs> keywordRanks(DataSet<FeLogs> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		FeLogs entity = ds.getEntity();

		String start = "";
		String end = "";
		if (entity.getStart() != null) {
			start = converter.convertToString(null, entity.getStart());
		}

		if (entity.getEnd() != null) {
			end = converter.convertToString(null, entity.getEnd().plusDays(1));
		}

		Query listQuery = null;
		Query countQuery = null;
		if (entity.getEnd() != null) {
			String listHql = "SELECT F.keyword, count(F.keyword) FROM FeLogs F WHERE F.cDTime >'"
					+ start
					+ "' and F.cDTime <'"
					+ end
					+ "' and F.keyword is not null"
					+ " GROUP BY F.keyword ORDER BY count(F.keyword) DESC";

			listQuery = getSession().createQuery(listHql);
			countQuery = getSession().createQuery(listHql);
		} else {
			String listHql = "SELECT F.keyword, count(F.keyword) FROM FeLogs F WHERE F.cDTime >'"
					+ start
					+ "' and F.keyword is not null"
					+ " GROUP BY F.keyword ORDER BY count(F.keyword) DESC";

			listQuery = getSession().createQuery(listHql);
			countQuery = getSession().createQuery(listHql);
		}

		listQuery.setFirstResult(ds.getPager().getOffset());
		listQuery.setMaxResults(ds.getPager().getRecordPerPage());

		List<?> data = listQuery.list();
		long totalRecord = countQuery.list().size();

		Iterator<?> iterator = data.iterator();
		List<FeLogs> ranks = new ArrayList<FeLogs>();

		int i = 0;
		while (iterator.hasNext()) {
			Object[] row = (Object[]) iterator.next();
			feLogs = new FeLogs(null, row[0].toString(), null, null, null);
			feLogs.setCount(Integer.parseInt(row[1].toString()));
			feLogs.setRank(i + ds.getPager().getOffset() + 1);
			ranks.add(feLogs);
			i++;
		}

		log.info("totalRecord:" + totalRecord);
		ds.getPager().setTotalRecord(totalRecord);
		ds.setResults(ranks);

		return ds;
	}

	public DataSet<FeLogs> urlRanks(DataSet<FeLogs> ds) {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		FeLogs entity = ds.getEntity();

		String start = "";
		String end = "";
		if (entity.getStart() != null) {
			start = converter.convertToString(null, entity.getStart());
		}

		if (entity.getEnd() != null) {
			end = converter.convertToString(null, entity.getEnd().plusDays(1));
		}

		Query listQuery = null;
		Query countQuery = null;
		if (entity.getEnd() != null) {
			String listHql = "SELECT F.database.serNo, F.ebook.serNo, F.journal.serNo, count(F.actionType) FROM FeLogs F WHERE F.cDTime >'"
					+ start
					+ "' and F.cDTime <'"
					+ end
					+ "' and F.actionType ='"
					+ Act.點擊.name()
					+ "' GROUP BY F.database.serNo, F.ebook.serNo, F.journal.serNo"
					+ " ORDER BY count(F.actionType) DESC";

			listQuery = getSession().createQuery(listHql);
			countQuery = getSession().createQuery(listHql);
		} else {
			String listHql = "SELECT F.database.serNo, F.ebook.serNo, F.journal.serNo, count(F.actionType) FROM FeLogs F WHERE F.cDTime >'"
					+ start
					+ "' and F.actionType ='"
					+ Act.點擊.name()
					+ "' GROUP BY F.database.serNo, F.ebook.serNo, F.journal.serNo"
					+ " ORDER BY count(F.actionType) DESC";

			listQuery = getSession().createQuery(listHql);
			countQuery = getSession().createQuery(listHql);
		}

		listQuery.setFirstResult(ds.getPager().getOffset());
		listQuery.setMaxResults(ds.getPager().getRecordPerPage());

		List<?> data = listQuery.list();
		long totalRecord = countQuery.list().size();

		Iterator<?> iterator = data.iterator();
		List<FeLogs> ranks = new ArrayList<FeLogs>();

		int i = 0;
		while (iterator.hasNext()) {
			Object[] row = (Object[]) iterator.next();
			feLogs = new FeLogs(Act.點擊, null, null, null, null);

			Query query = null;
			if (row[0] != null) {
				query = getSession().createQuery("FROM Database WHERE serNo=?");
				query.setLong(0, (long) row[0]);
				feLogs.setDatabase((Database) query.list().get(0));
			}

			if (row[1] != null) {
				query = getSession().createQuery("FROM Ebook WHERE serNo=?");
				query.setLong(0, (long) row[1]);
				feLogs.setEbook((Ebook) query.list().get(0));
			}

			if (row[2] != null) {
				query = getSession().createQuery("FROM Journal WHERE serNo=?");
				query.setLong(0, (long) row[2]);
				feLogs.setJournal((Journal) query.list().get(0));
			}

			feLogs.setCount(Integer.parseInt(row[3].toString()));
			feLogs.setRank(i + ds.getPager().getOffset() + 1);
			ranks.add(feLogs);
			i++;
		}

		log.info("totalRecord:" + totalRecord);
		ds.getPager().setTotalRecord(totalRecord);
		ds.setResults(ranks);

		return ds;
	}
}
