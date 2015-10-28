package com.shouyang.syazs.module.apply.feLogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.customer.Customer;
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

	public DataSet<FeLogs> loginRanks(DataSet<FeLogs> ds) throws Exception {
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
			String listHql = "SELECT F.accountNumber, F.customer, F.actionType, count(F.accountNumber) FROM FeLogs F WHERE F.cDTime >'"
					+ start
					+ "' and F.cDTime <'"
					+ end
					+ "' and F.customer.serNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' and F.actionType ='"
					+ Act.登入
					+ "' GROUP BY F.accountNumber ORDER BY count(F.accountNumber) DESC";

			if (entity.getCustomer().getSerNo() == 0) {
				listHql = listHql.replace("' and F.customer.serNo ='"
						+ entity.getCustomer().getSerNo(), "");
			}

			listQuery = getSession().createQuery(listHql);
			countQuery = getSession().createQuery(listHql);
		} else {
			String listHql = "SELECT F.accountNumber, F.customer, F.actionType, count(F.accountNumber) FROM FeLogs F WHERE F.cDTime >'"
					+ start
					+ "' and F.customer.serNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' and F.actionType ='"
					+ Act.登入
					+ "' GROUP BY F.accountNumber ORDER BY count(F.accountNumber) DESC";

			if (entity.getCustomer().getSerNo() == 0) {
				listHql = listHql.replace("' and F.customer.serNo ='"
						+ entity.getCustomer().getSerNo(), "");
			}

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

			feLogs = new FeLogs(Act.valueOf(row[2].toString()), null,
					(Customer) row[1], (AccountNumber) row[0], null, null,
					null, null);

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
			String listHql = "SELECT F.customer, F.keyword, count(F.keyword) FROM FeLogs F WHERE F.cDTime >'"
					+ start
					+ "' and F.cDTime <'"
					+ end
					+ "' and F.customer.serNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' and F.keyword is not null"
					+ " GROUP BY F.customer.serNo, F.keyword ORDER BY count(F.keyword) DESC";

			if (entity.getCustomer().getSerNo() == 0) {
				listHql = listHql.replace("' and F.customer.serNo ='"
						+ entity.getCustomer().getSerNo(), "");
			}

			listQuery = getSession().createQuery(listHql);
			countQuery = getSession().createQuery(listHql);
		} else {
			String listHql = "SELECT F.customer, F.keyword, count(F.keyword) FROM FeLogs F WHERE F.cDTime >'"
					+ start
					+ "' and F.customer.serNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' and F.keyword is not null"
					+ " GROUP BY F.customer.serNo, F.keyword ORDER BY count(F.keyword) DESC";

			if (entity.getCustomer().getSerNo() == 0) {
				listHql = listHql.replace("' and F.customer.serNo ='"
						+ entity.getCustomer().getSerNo(), "");
			}

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
			feLogs = new FeLogs(null, row[1].toString(), (Customer) row[0],
					null, null, null, null, null);
			feLogs.setCount(Integer.parseInt(row[2].toString()));
			feLogs.setRank(i + ds.getPager().getOffset() + 1);
			ranks.add(feLogs);
			i++;
		}

		log.info("totalRecord:" + totalRecord);
		ds.getPager().setTotalRecord(totalRecord);
		ds.setResults(ranks);

		return ds;
	}

	public DataSet<FeLogs> urlRanks(DataSet<FeLogs> ds) {// TODO
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
			String listHql = "SELECT F.customer, F.database, count(F.click), F.ebook, F.ebook.serNo, F.customer.serNo, F.database.serNo FROM FeLogs F WHERE F.cDTime >'"
					+ start
					+ "' and F.cDTime <'"
					+ end
					+ "' and F.customer.serNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' and F.click ="
					+ true
					+ " GROUP BY F.customer.serNo, F.database.serNo, F.ebook.serNo"
					+ " ORDER BY count(F.click) DESC";

			if (entity.getCustomer().getSerNo() == 0) {
				listHql = listHql.replace("' and F.customer.serNo ='"
						+ entity.getCustomer().getSerNo(), "");
			}

			listQuery = getSession().createQuery(listHql);
			countQuery = getSession().createQuery(listHql);
		} else {
			String listHql = "SELECT F.customer.serNo, F.database.serNo, F.ebook.serNo, F.journal.serNo, count(F.click), F.customer FROM FeLogs F WHERE F.cDTime >'"
					+ start
					+ "' and F.customer.serNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' and F.click ="
					+ true
					+ " GROUP BY F.customer.serNo, F.database.serNo, F.ebook.serNo, F.journal.serNo"
					+ " ORDER BY count(F.click) DESC";

			if (entity.getCustomer().getSerNo() == 0) {
				listHql = listHql.replace("' and F.customer.serNo ='"
						+ entity.getCustomer().getSerNo(), "");
			}

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
			feLogs = new FeLogs(Act.借閱, null, (Customer) row[5], null, null,
					null, null, true);

			Query query = null;
			if (row[1] != null) {
				query = getSession().createQuery("FROM Database WHERE serNo=?");
				query.setLong(0, (long) row[1]);
				feLogs.setDatabase((Database) query.list().get(0));
			}

			if (row[2] != null) {
				query = getSession().createQuery("FROM Ebook WHERE serNo=?");
				query.setLong(0, (long) row[2]);
				feLogs.setEbook((Ebook) query.list().get(0));
			}

			if (row[3] != null) {
				query = getSession().createQuery("FROM Journal WHERE serNo=?");
				query.setLong(0, (long) row[3]);
				feLogs.setJournal((Journal) query.list().get(0));
			}

			feLogs.setCount(Integer.parseInt(row[4].toString()));
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
