package com.shouyang.syazs.core.apply.customer;

import java.util.Map;

import org.hibernate.Query;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoFull;

@Repository
public class CustomerDao extends ModuleDaoFull<Customer> {

	public boolean delRelatedObj(long cusSerNo) {

		Map<String, ClassMetadata> map = (Map<String, ClassMetadata>) getSession()
				.getSessionFactory().getAllClassMetadata();

		for (String entityName : map.keySet()) {
			Query query = getSession().createQuery("FROM " + entityName);
			query.setFirstResult(0);
			query.setMaxResults(1);

			if (query.list().toString().contains("customer=")
					&& !query.list().toString().contains("cDTime=")
					&& !query.list().toString().contains("uDTime=")) {
				Query resourceQuery = getSession().createQuery(
						"SELECT COUNT(*) FROM " + entityName
								+ " WHERE customer.serNo=?");

				if ((Long) resourceQuery.setLong(0, cusSerNo).list().get(0) > 0) {
					return false;
				}
			}
		}

		switchFK(false);

		for (String entityName : map.keySet()) {
			Query query = getSession().createQuery("FROM " + entityName);
			query.setFirstResult(0);
			query.setMaxResults(1);

			if (query.list().toString().contains("customer=")) {
				Query delQuery = getSession()
						.createQuery(
								"DELETE FROM " + entityName
										+ " WHERE customer.serNo=?");
				delQuery.setLong(0, cusSerNo).executeUpdate();
			}
		}

		switchFK(false);
		return true;
	}
}
