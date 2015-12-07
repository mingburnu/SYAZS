package com.shouyang.syazs.core.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.shouyang.syazs.core.dao.DsQueryLanguage;

/**
 * HQL
 * @author Roderick
 * @version 2014/11/6
 */
public class HibernateQueryLanguage implements DsQueryLanguage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 456532219336289896L;

	private String hql;
	
	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	private Map<String, Collection<?>> collections = new HashMap<String, Collection<?>>();
	
	@Override
	public String getHql() {
		return hql;
	}
	
	@Override
	public void setHql(String hql) {
		this.hql = hql;
	}

	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	@Override
	public Map<String, Collection<?>> getParameterLists() {
		return collections;
	}

	@Override
	public DsQueryLanguage addParameter(String name, Object value) {
		parameters.put(name, value);
		return this;
	}

	@Override
	public DsQueryLanguage addParameterList(String name, Collection<?> collection) {
		collections.put(name, collection);
		return this;
	}

}
