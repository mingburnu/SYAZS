package com.shouyang.syazs.core.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shouyang.syazs.core.entity.Entity;

/**
 * DataSet
 * @author Roderick
 * @version 2014/3/18
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DataSet<T extends Entity> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2324828511681435281L;

	private Pager pager;
	
	private T entity;
	
	private List<T> results = Lists.newArrayList();
	
	private Map<String,Object> datas = Maps.newHashMap();

	public T getEntity() {
		return entity;
	}

	public Map<String, Object> getDatas() {
		return datas;
	}

	public void setDatas(Map<String, Object> dataSetMap) {
		this.datas = dataSetMap;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}
	
}
