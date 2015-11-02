package com.shouyang.syazs.core.apply.customer;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

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
