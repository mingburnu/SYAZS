package com.shouyang.syazs.core.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.shouyang.syazs.core.dao.DsQueryLanguage;
import com.shouyang.syazs.core.dao.DsRestrictions;
import com.shouyang.syazs.core.dao.GenericDao;
import com.shouyang.syazs.core.entity.Entity;
import com.shouyang.syazs.core.model.DataSet;
import com.shouyang.syazs.core.model.Pager;

/**
 * GenericHibernateDao
 * 
 * @author Roderick
 * @version 2014/11/7
 */
public abstract class GenericHibernateDao<T extends Entity> extends
		GenericDao<T> {

	@Autowired
	private SessionFactory sessionFactory;

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public GenericHibernateDao() {
		this.entityClass = null;
		Class<?> c = getClass();
		Type t = c.getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();
			this.entityClass = (Class<T>) p[0];
		}
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findBySerNo(Long serNo) throws Exception {
		Assert.notNull(serNo);
		return (T) getSession().byId(entityClass).load(serNo);
	}

	@Override
	public List<T> findByRestrictions(DsRestrictions restrictions)
			throws Exception {
		DataSet<T> results = findByRestrictions(restrictions, null);
		return results.getResults();
	}

	@SuppressWarnings("unchecked")
	@Override
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

	@Override
	public T save(T entity) throws IllegalAccessException,
			InvocationTargetException {
		Assert.notNull(entity);
		Serializable serNo = getSession().save(entity);
		BeanUtils.setProperty(entity, "serNo", serNo);
		return entity;
	}

	@Override
	public void update(T entity) throws Exception {
		Assert.notNull(entity);
		getSession().update(entity);
	}

	@Override
	public void merge(T entity) throws Exception {
		Assert.notNull(entity);
		getSession().merge(entity);
	}

	@Override
	public void deleteBySerNo(Long serNo) throws Exception {
		Assert.notNull(serNo);
		T t = findBySerNo(serNo);
		getSession().delete(t);
	}

	@Override
	public void delete(T entity) throws Exception {
		Assert.notNull(entity);
		getSession().delete(entity);
	}

	@Override
	public List<?> findByQL(DsQueryLanguage dsQL) {
		Assert.notNull(dsQL);
		Assert.notNull(dsQL.getSql());

		Query query = getSession().createQuery(dsQL.getSql());
		for (Entry<String, Object> keyValue : dsQL.getParameters().entrySet()) {
			query.setParameter(keyValue.getKey(), keyValue.getValue());
		}

		return query.list();
	}

	@Override
	public void checkFK(Boolean constraint) {
		if (constraint) {
			// SET REFERENTIAL_INTEGRITY TRUE
			SQLQuery fkAble = getSession().createSQLQuery(
					"SET FOREIGN_KEY_CHECKS=1");
			fkAble.executeUpdate();
		} else {
			// SET REFERENTIAL_INTEGRITY FALSE
			SQLQuery fkDisable = getSession().createSQLQuery(
					"SET FOREIGN_KEY_CHECKS=0");
			fkDisable.executeUpdate();
		}

	}

}
