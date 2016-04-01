package com.shouyang.syazs.core.apply.customer;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.dao.ModuleDaoFull;

@Repository
public class CustomerDao extends ModuleDaoFull<Customer> {

	@Override
	public void deleteBySerNo(Long serNo) throws Exception {
		Assert.notNull(serNo);
		Customer t = findBySerNo(serNo);

		Query beQuery = getSession().createQuery(
				"DELETE BeLogs B WHERE B.customer.serNo=?");
		Query feQuery = getSession().createQuery(
				"DELETE FeLogs F WHERE F.customer.serNo=?");

		beQuery.setLong(0, t.getSerNo());
		feQuery.setLong(0, t.getSerNo());

		beQuery.executeUpdate();
		feQuery.executeUpdate();

		Query beAccQuery = getSession().createQuery(
				"DELETE BeLogs B WHERE B.accountNumber IN (:accs)");
		Query feAccQuery = getSession().createQuery(
				"DELETE FeLogs F WHERE F.accountNumber IN (:accs)");

		List<AccountNumber> accs = Lists.newArrayList();
		accs.addAll(t.getAccountNumbers());

		beAccQuery.setParameterList("accs", accs);
		feAccQuery.setParameterList("accs", accs);

		beAccQuery.executeUpdate();
		feAccQuery.executeUpdate();

		delete(t);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap(Map<String, Object> datas) {
		Query query = getSession().createQuery(
				"SELECT c.name, c.serNo FROM Customer c");
		List<Object[]> rows = query.list();

		for (Object[] row : rows) {
			String name = (String) row[0];
			Long serNo = (Long) row[1];
			datas.put(name, serNo);
		}

		return datas;
	}
}
