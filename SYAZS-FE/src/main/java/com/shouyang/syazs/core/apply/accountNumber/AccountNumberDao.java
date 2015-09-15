package com.shouyang.syazs.core.apply.accountNumber;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.shouyang.syazs.core.dao.ModuleDaoFull;

/**
 * 使用者 Dao
 * 
 * @author Roderick
 * @version 2014/9/29
 */
@Repository
public class AccountNumberDao extends ModuleDaoFull<AccountNumber> {
	public AccountNumber findByUserId(String userId) {
		Criteria criteria = getSession().createCriteria(AccountNumber.class);
		Restrictions.eq("userId", userId);

		return (AccountNumber) criteria.list().get(0);
	}
}
