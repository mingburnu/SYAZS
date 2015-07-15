package com.shouyang.syazs.core.apply.beLogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.enums.Act;
import com.shouyang.syazs.core.converter.JodaTimeConverter;
import com.shouyang.syazs.core.dao.ModuleDaoLog;
import com.shouyang.syazs.core.model.DataSet;

@Repository
public class BeLogsDao extends ModuleDaoLog<BeLogs> {

	@Autowired
	private BeLogs beLogs;

	@Autowired
	private JodaTimeConverter converter;

	public DataSet<BeLogs> getRanks(DataSet<BeLogs> ds) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());
		BeLogs entity = ds.getEntity();

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
			String listHql = "SELECT B.accountNumber, B.customer, B.customer.serNo, B.actionType, count(B.accountNumber) FROM BeLogs B WHERE B.cDTime >'"
					+ start
					+ "' and B.cDTime <'"
					+ end
					+ "' and B.customer.serNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' GROUP BY B.accountNumber ORDER BY count(B.accountNumber) DESC";
			String countSql = "SELECT count(*) as total FROM (SELECT count(acc_serNo) FROM BE_Logs WHERE cDTime >'"
					+ start
					+ "' and cDTime <'"
					+ end
					+ "' and cus_serNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' group by acc_serNo) as countTotal";

			if (entity.getCustomer().getSerNo() == 0) {
				listHql = listHql.replace("' and B.customer.serNo ='"
						+ entity.getCustomer().getSerNo(), "");
				countSql = countSql.replace("' and cus_serNo ='"
						+ entity.getCustomer().getSerNo(), "");
			}

			listQuery = getSession().createQuery(listHql);
			totalQuery = getSession().createSQLQuery(countSql);
		} else {
			String listHql = "SELECT B.accountNumber, B.customer, B.customer.serNo, B.actionType, count(B.accountNumber) FROM BeLogs B WHERE B.cDTime >'"
					+ start
					+ "' and B.customer.serNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' GROUP BY B.accountNumber ORDER BY count(B.accountNumber) DESC";
			String countSql = "SELECT count(*) as total FROM (SELECT count(acc_serNo) FROM BE_Logs WHERE cDTime >'"
					+ start
					+ "' and cus_serNo ='"
					+ entity.getCustomer().getSerNo()
					+ "' group by acc_serNo) as countTotal";

			if (entity.getCustomer().getSerNo() == 0) {
				listHql = listHql.replace("' and B.customer.serNo ='"
						+ entity.getCustomer().getSerNo(), "");
				countSql = countSql.replace("' and cus_serNo ='"
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
		List<BeLogs> ranks = new ArrayList<BeLogs>();

		int i = 0;
		while (iterator.hasNext()) {
			Object[] row = (Object[]) iterator.next();

			beLogs = new BeLogs(Act.valueOf(row[3].toString()),
					(AccountNumber) row[0], (Customer) row[1]);
			beLogs.setCount(Integer.parseInt(row[4].toString()));
			beLogs.setRank(i + ds.getPager().getOffset() + 1);
			ranks.add(beLogs);
			i++;
		}

		ds.getPager().setTotalRecord(Long.parseLong(total.get(0).toString()));
		ds.setResults(ranks);

		return ds;
	}
}
