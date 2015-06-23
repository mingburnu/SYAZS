package com.shouyang.syazs.core.apply.customer;

import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
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
					&& !query.list().toString().contains("cDTime=")) {
				Query resourceQuery = getSession().createQuery(
						"SELECT COUNT(*) FROM " + entityName
								+ " WHERE customer.serNo=?");

				if ((Long) resourceQuery.setLong(0, cusSerNo).list().get(0) > 0) {
					return false;
				}
			}
		}

		// SET REFERENTIAL_INTEGRITY FALSE
		SQLQuery fkDisable = getSession().createSQLQuery(
				"SET FOREIGN_KEY_CHECKS=0");
		fkDisable.executeUpdate();

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

		// SET REFERENTIAL_INTEGRITY TRUE
		SQLQuery fkAble = getSession().createSQLQuery(
				"SET FOREIGN_KEY_CHECKS=1");
		fkAble.executeUpdate();
		return true;
	}
}
