package com.shouyang.syazs.core.service;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.accountNumber.AccountNumberDao;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.entity.GenericEntitySerNo;

/**
 * GenericService
 * 
 * @author Roderick
 * @version 2014/9/29
 */
public abstract class GenericServiceSerNo<T extends GenericEntitySerNo> extends
		ServiceFactory<T> {

	protected final transient Logger log = Logger.getLogger(getClass());

	protected abstract GenericDao<T> getDao();

	@Autowired
	private AccountNumberDao userDao;

	@Override
	public T save(T entity, AccountNumber user) throws Exception {
		Assert.notNull(entity);

		T dbEntity = getDao().save(entity);
		return dbEntity;
	}

	@Override
	public T getBySerNo(Long serNo) throws Exception {
		Assert.notNull(serNo);

		T entity = getDao().findBySerNo(serNo);
		return entity;
	}

	@Override
	public T update(T entity, AccountNumber user, String... ignoreProperties)
			throws Exception {
		Assert.notNull(entity);

		T dbEntity = getDao().findBySerNo(entity.getSerNo());

		if (ignoreProperties.length == 0) {
			BeanUtils.copyProperties(entity, dbEntity);
		} else {
			BeanUtils.copyProperties(entity, dbEntity, ignoreProperties);
		}

		getDao().update(dbEntity);
		return dbEntity;
	}

	@Override
	public T merge(T entity, AccountNumber user, String... ignoreProperties)
			throws Exception {
		Assert.notNull(entity);

		T dbEntity = getDao().findBySerNo(entity.getSerNo());

		if (ignoreProperties.length == 0) {
			BeanUtils.copyProperties(entity, dbEntity);
		} else {
			BeanUtils.copyProperties(entity, dbEntity, ignoreProperties);
		}

		getDao().merge(dbEntity);
		return dbEntity;
	}

	@Override
	public void deleteBySerNo(Long serNo) throws Exception {
		Assert.notNull(serNo);
		getDao().deleteBySerNo(serNo);
	}
}
