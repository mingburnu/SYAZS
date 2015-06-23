package com.shouyang.syazs.core.apply.accountNumber;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.GenericHibernateDaoFull;

/**
 * 使用者 Dao
 * 
 * @author Roderick
 * @version 2014/9/29
 */
@Repository
public class AccountNumberDao extends GenericHibernateDaoFull<AccountNumber> {

	public AccountNumber findByUserId(String userId) {
		Criteria criteria = getSession().createCriteria(AccountNumber.class);
		Restrictions.eq("userId", userId);

		return (AccountNumber) criteria.list().get(0);
	}

	public void updateCustomer(AccountNumber accountNumber) {
		Map<String, ClassMetadata> map = (Map<String, ClassMetadata>) getSession()
				.getSessionFactory().getAllClassMetadata();

		for (String entityName : map.keySet()) {
			Query query = getSession().createQuery("FROM " + entityName);
			query.setFirstResult(0);
			query.setMaxResults(1);

			if (query.list().toString().contains("customer=")
					&& query.list().toString().contains("accountNumber=")) {
				Query update = getSession().createQuery(
						"UPDATE " + entityName + " SET customer.serNo ="
								+ accountNumber.getCustomer().getSerNo()
								+ " WHERE accountNumber.serNo ="
								+ accountNumber.getSerNo());
				update.executeUpdate();
			}

		}
	}
}
