package com.shouyang.syazs.core.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.entity.Entity;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.model.Pager;

/**
 * ModuleDao
 * 
 * @author Rodertick
 * @version 2014/11/21
 */
public class ModuleDaoModel<T extends Entity>  {
	
	protected final transient Logger log = Logger.getLogger(getClass());

	@Autowired
	private SessionFactory sessionFactory;
	
	private Class<T> entityClass;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public T findBySerNo(Long serNo) throws Exception {
		Assert.notNull(serNo);
		return (T) getSession().byId(entityClass).load(serNo);
	}

	public List<T> findByRestrictions(DsRestrictions restrictions)
			throws Exception {
		DataSet<T> results = findByRestrictions(restrictions, null);
		return results.getResults();
	}

	@SuppressWarnings("unchecked")
	public DataSet<T> findByRestrictions(DsRestrictions restrictions,
			DataSet<T> ds) throws Exception {
		Assert.notNull(restrictions);
		Criteria dataCri = getSession().createCriteria(entityClass);
		Criteria countCri = getSession().createCriteria(entityClass);

		// createAlias
		Map<String, String> aliases = restrictions.getAliases();
		Iterator<Entry<String, String>> iterator = aliases.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			dataCri.createAlias(entry.getKey(), entry.getValue());
			countCri.createAlias(entry.getKey(), entry.getValue());
		}

		// add restrictions
		List<Criterion> crions = (List<Criterion>) restrictions.getCriterions();
		for (Criterion crion : crions) {
			countCri.add(crion);
			dataCri.add(crion);
		}

		// add orders
		List<Order> orders = (List<Order>) restrictions.getOrders();
		for (Order order : orders) {
			dataCri.addOrder(order);
		}

		if (ds != null && ds.getPager() != null) { // 分頁
			Pager pager = ds.getPager();

			// count total records
			countCri.setProjection(Projections.rowCount());
			Long totalRecord = (Long) countCri.list().get(0);
			log.debug("totalRecord:" + totalRecord);
			pager.setTotalRecord(totalRecord);

			dataCri.setFirstResult(pager.getOffset());
			dataCri.setMaxResults(pager.getRecordPerPage());
		} else {
			ds = new DataSet<T>();
		}

		ds.setResults(dataCri.list());

		return ds;
	}

}
