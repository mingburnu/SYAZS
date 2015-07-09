package com.shouyang.syazs.module.apply.feLogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.enums.Act;
import com.shouyang.syazs.core.converter.JodaTimeConverter;
import com.shouyang.syazs.core.dao.ModuleDaoLog;
import com.shouyang.syazs.core.model.DataSet;

@Repository
public class FeLogsDao extends ModuleDaoLog<FeLogs> {

	@Autowired
	private FeLogs feLogs;

	@Autowired
	private JodaTimeConverter converter;

	public DataSet<FeLogs> getRanks(DataSet<FeLogs> ds) throws Exception {
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
		SQLQuery totalQuery = null;
		if (entity.getEnd() != null) {
			String listHql = "SELECT F.customer, F.customer.serNo, F.keyword, count(F.keyword) FROM FeLogs F WHERE F.cDTime >'"
					+ start
					+ "' and F.cDTime <'"
					+ end
					+ "' and F.customer.serNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' GROUP BY F.customer.serNo, F.keyword ORDER BY count(F.keyword) DESC";
			String countSql = "SELECT count(*) as total FROM (SELECT count(keyword) FROM FE_Logs WHERE cDTime >'"
					+ start
					+ "' and cDTime <'"
					+ end
					+ "' and cus_SerNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' group by cus_SerNo, keyword) as countTotal";

			if (entity.getCustomer().getSerNo() == 0) {
				listHql = listHql.replace("' and F.customer.serNo ='"
						+ entity.getCustomer().getSerNo(), "");
				countSql = countSql.replace("' and cus_SerNo ='"
						+ entity.getCustomer().getSerNo(), "");
			}

			listQuery = getSession().createQuery(listHql);
			totalQuery = getSession().createSQLQuery(countSql);
		} else {
			String listHql = "SELECT F.customer, F.customer.serNo, F.keyword, count(F.keyword) FROM FeLogs F WHERE F.cDTime >'"
					+ start
					+ "' and F.customer.serNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' GROUP BY F.customer.serNo, F.keyword ORDER BY count(F.keyword) DESC";
			String countSql = "SELECT count(*) as total FROM (SELECT count(keyword) FROM FE_Logs WHERE cDTime >'"
					+ start
					+ "' and cus_SerNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' group by cus_SerNo, keyword) as countTotal";

			if (entity.getCustomer().getSerNo() == 0) {
				listHql = listHql.replace("' and F.customer.serNo ='"
						+ entity.getCustomer().getSerNo(), "");
				countSql = countSql.replace("' and cus_SerNo ='"
						+ entity.getCustomer().getSerNo(), "");
			}

			listQuery = getSession().createQuery(listHql);
			totalQuery = getSession().createSQLQuery(countSql);
		}

		listQuery.setFirstResult(ds.getPager().getOffset());
		listQuery.setMaxResults(ds.getPager().getRecordPerPage());

		List<?> data = listQuery.list();
		List<?> total = totalQuery.list();
		Iterator<?> iterator = data.iterator();
		List<FeLogs> ranks = new ArrayList<FeLogs>();

		int i = 0;
		while (iterator.hasNext()) {
			Object[] row = (Object[]) iterator.next();
			feLogs = new FeLogs(Act.綜合查詢, row[2].toString(), (Customer) row[0],
					null, 0L, 0L, 0L);
			feLogs.setCount(Integer.parseInt(row[3].toString()));
			feLogs.setRank(i + ds.getPager().getOffset() + 1);
			ranks.add(feLogs);
			i++;
		}

		ds.getPager().setTotalRecord(Long.parseLong(total.get(0).toString()));
		ds.setResults(ranks);

		return ds;
	}
}
