package com.shouyang.syazs.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.shouyang.syazs.core.dao.DsQueryLanguage;

/**
 * DsQueryLanguage
 * @author Roderick
 * @version 2015/01/27
 */
public interface DsQueryLanguage extends Serializable {

	public String getHql();
	
	public void setHql(String hql);
	
	public Map<String, Object> getParameters();
	
	public Map<String, Collection<?>> getParameterLists();
	
	public DsQueryLanguage addParameter(String name, Object value);
	
	public DsQueryLanguage addParameterList(String name, Collection<?> collection);
	
}
