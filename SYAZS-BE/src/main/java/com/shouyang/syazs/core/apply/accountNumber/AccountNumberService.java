package com.shouyang.syazs.core.apply.accountNumber;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.apply.customer.CustomerService;
import com.shouyang.syazs.core.apply.enums.Role;
import com.shouyang.syazs.core.apply.enums.Status;
import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.service.GenericServiceFull;
import com.shouyang.syazs.core.util.DsBeanFactory;
import com.shouyang.syazs.core.util.EncryptorUtil;

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

	@Autowired
	private CustomerService customerService;

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
		DsRestrictions restrictions = DsBeanFactory.getDsRestrictions();

		if (StringUtils.isNotEmpty(entity.getUserId())) {
			restrictions.eq("userId", entity.getUserId());
		}

		return dao.findByRestrictions(restrictions, ds);
	}

	@Override
	public AccountNumber save(AccountNumber entity, AccountNumber user)
			throws Exception {
		Assert.notNull(entity);

		entity.initInsert(user);
		if (StringUtils.isNotEmpty(entity.getUserPw())) { // 密碼非空則進行加密
			final String encryptedPassword = EncryptorUtil.encrypt(entity
					.getUserPw());
			entity.setUserPw(encryptedPassword);
		}

		AccountNumber dbEntity = dao.save(entity);
		makeUserInfo(dbEntity);

		return dbEntity;
	}

	@Override
	public AccountNumber update(AccountNumber entity, AccountNumber user,
			String... ignoreProperties) throws Exception {
		Assert.notNull(entity);

		entity.initUpdate(user);
		if (StringUtils.isNotEmpty(entity.getUserPw())) {
			final String encryptedPassword = EncryptorUtil.encrypt(entity
					.getUserPw());
			entity.setUserPw(encryptedPassword);
		}

		AccountNumber dbEntity = getDao().findBySerNo(entity.getSerNo());

		BeanUtils.copyProperties(entity, dbEntity, ignoreProperties);

		getDao().update(dbEntity);
		makeUserInfo(dbEntity);

		return dbEntity;
	}

	/**
	 * 檢查登入帳密
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Boolean checkUserId(AccountNumber entity) throws Exception {
		Assert.notNull(entity);

		DsRestrictions restrictions = DsBeanFactory.getDsRestrictions();
		restrictions.eq("userId", entity.getUserId());
		restrictions.ne("role", Role.使用者);
		restrictions.ne("status", Status.審核中);
		restrictions.ne("status", Status.不生效);
		List<AccountNumber> secUsers = dao.findByRestrictions(restrictions);
		if (CollectionUtils.isEmpty(secUsers)) {
			return false;
		} else {
			return true;
		}

	}

	public Boolean checkUserPw(AccountNumber entity) throws Exception {
		Assert.notNull(entity);

		DsRestrictions restrictions = DsBeanFactory.getDsRestrictions();
		restrictions.eq("userId", entity.getUserId());

		List<AccountNumber> secUsers = dao.findByRestrictions(restrictions);
		if (CollectionUtils.isEmpty(secUsers)) {
			return false;
		}
		AccountNumber secUser = secUsers.get(0);

		return EncryptorUtil.checkPassword(entity.getUserPw(),
				secUser.getUserPw());
	}

	public long getSerNoByUserId(String userId) throws Exception {
		DsRestrictions restrictions = DsBeanFactory.getDsRestrictions();
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

	public void updateLog(AccountNumber accountNumber) {
		dao.updateCustomer(accountNumber);
	}
}
