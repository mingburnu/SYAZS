package com.shouyang.syazs.core.apply.customer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.dao.ModuleDaoFull;

@Repository
public class CustomerDao extends ModuleDaoFull<Customer> {

	@Autowired
	private Customer customer;

	@Autowired
	private AccountNumber accountNumber;

	@Override
	public void deleteBySerNo(Long serNo) throws Exception {
		Assert.notNull(serNo);
		Customer t = findBySerNo(serNo);

		Iterator<AccountNumber> accountNumbers = t.getAccountNumbers()
				.iterator();
		while (accountNumbers.hasNext()) {
			accountNumber = accountNumbers.next();
			Query beQuery = getSession().createQuery(
					"DELETE FROM BeLogs B WHERE B.accountNumber.serNo=?");
			Query feQuery = getSession().createQuery(
					"DELETE FROM FeLogs F WHERE F.accountNumber.serNo=?");
			beQuery.setLong(0, accountNumber.getSerNo());
			feQuery.setLong(0, accountNumber.getSerNo());
			beQuery.executeUpdate();
			feQuery.executeUpdate();

			getSession().delete(accountNumber);
		}

		getSession().delete(t);
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
