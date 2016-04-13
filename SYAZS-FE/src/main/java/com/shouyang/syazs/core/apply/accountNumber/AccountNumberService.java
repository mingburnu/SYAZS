package com.shouyang.syazs.core.apply.accountNumber;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceFull;

/**
 * 使用者 Service
 * 
 * @author Roderick
 * @version 2014/9/30
 */
@Service
public class AccountNumberService extends GenericServiceFull<AccountNumber> {

	@Autowired
	private AccountNumberDao dao;

	@Override
	protected GenericDao<AccountNumber> getDao() {
		return dao;
	}

	/**
	 * 登入取得帳號資料
	 * 
	 * @param ds
	 * @return
	 * @throws Exception
	 */
	@Override
	public DataSet<AccountNumber> getByRestrictions(DataSet<AccountNumber> ds)
			throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		AccountNumber entity = ds.getEntity();
		DsRestrictions restrictions = getDsRestrictions();

		if (StringUtils.isNotEmpty(entity.getUserId())) {
			restrictions.eq("userId", entity.getUserId());
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	public long getSerNoByUserId(String userId) throws Exception {
		DsRestrictions restrictions = getDsRestrictions();
		restrictions.customCriterion(Restrictions.eq("userId", userId));

		if (dao.findByRestrictions(restrictions).size() > 0) {
			return (dao.findByRestrictions(restrictions).get(0)).getSerNo();
		} else {
			return 0;
		}
	}

	public DataSet<AccountNumber> getByRestrictions(DataSet<AccountNumber> ds,
			AccountNumber loginUser) throws Exception {
		Assert.notNull(ds);
		Assert.notNull(ds.getEntity());

		return dao.findByRestrictions(loginUser, ds);
	}
}
