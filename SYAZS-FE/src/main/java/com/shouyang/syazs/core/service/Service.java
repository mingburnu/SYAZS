package com.shouyang.syazs.core.service;

import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.entity.Entity;
import com.shouyang.syazs.core.model.DataSet;

/**
 * Service
 * 
 * @author Roderick
 * @version 2014/3/11
 */
public interface Service<T extends Entity> {

	public T save(T entity, AccountNumber user) throws Exception;

	public T getBySerNo(Long serNo) throws Exception;

	public DataSet<T> getByRestrictions(DataSet<T> ds) throws Exception;

	public T update(T entity, AccountNumber user, String... ignoreProperties)
			throws Exception;

	public void deleteBySerNo(Long serNo) throws Exception;

}
