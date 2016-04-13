package com.shouyang.syazs.core.apply.customer;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.shouyang.syazs.core.dao.ModuleDaoFull;

@Repository
public class CustomerDao extends ModuleDaoFull<Customer> {

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
