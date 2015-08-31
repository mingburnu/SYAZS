package com.shouyang.syazs.core.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.shouyang.syazs.core.dao.DsRestrictions;

/**
 * Hibernate Restrictions
 * 
 * @author Roderick
 * @version 2014/10/22
 */
public class HibernateRestrictions implements DsRestrictions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1634807264759350521L;

	private List<Criterion> criterions;

	private List<Order> orders;

	private Map<String, String> aliases;

	public HibernateRestrictions() {
		criterions = new ArrayList<Criterion>();
		orders = new ArrayList<Order>();
		aliases = new HashMap<String, String>();
	}

	@Override
	public List<Criterion> getCriterions() {
		return criterions;
	}

	@Override
	public List<Order> getOrders() {
		return orders;
	}

	@Override
	public Map<String, String> getAliases() {
		return aliases;
	}

	@Override
	public void createAlias(String associationPath, String alias) {
		aliases.put(associationPath, alias);
	}

	@Override
	public void eq(String propertyName, Object value) {
		criterions.add(Restrictions.eq(propertyName, value));
	}

	@Override
	public void ne(String propertyName, Object value) {
		criterions.add(Restrictions.ne(propertyName, value));
	}

	@Override
	public void like(String propertyName, Object value) {
		criterions.add(Restrictions.like(propertyName, value));
	}

	@Override
	public void like(String propertyName, String value, MatchMode matchMode) {
		criterions.add(Restrictions.like(propertyName, value, matchMode));
	}

	@Override
	public void likeIgnoreCase(String propertyName, Object value) {
		criterions.add(Restrictions.ilike(propertyName, value));
	}

	@Override
	public void likeIgnoreCase(String propertyName, String value,
			MatchMode matchMode) {
		criterions.add(Restrictions.ilike(propertyName, value, matchMode));
	}

	@Override
	public void gt(String propertyName, Object value) {
		criterions.add(Restrictions.gt(propertyName, value));
	}

	@Override
	public void lt(String propertyName, Object value) {
		criterions.add(Restrictions.lt(propertyName, value));
	}

	@Override
	public void ge(String propertyName, Object value) {
		criterions.add(Restrictions.ge(propertyName, value));
	}

	@Override
	public void le(String propertyName, Object value) {
		criterions.add(Restrictions.le(propertyName, value));
	}

	@Override
	public void between(String propertyName, Object leftValue, Object rightValue) {
		criterions.add(Restrictions
				.between(propertyName, leftValue, rightValue));
	}

	@Override
	public void in(String propertyName, Object[] values) {
		criterions.add(Restrictions.in(propertyName, values));
	}

	@Override
	public void in(String propertyName, Collection<?> values) {
		criterions.add(Restrictions.in(propertyName, values));
	}

	@Override
	public void isNull(String propertyName) {
		criterions.add(Restrictions.isNull(propertyName));
	}

	@Override
	public void isNotNull(String propertyName) {
		criterions.add(Restrictions.isNotNull(propertyName));
	}

	@Override
	public void eqProperty(String propertyName, String otherPropertyName) {
		criterions
				.add(Restrictions.eqProperty(propertyName, otherPropertyName));
	}

	@Override
	public void neProperty(String propertyName, String otherPropertyName) {
		criterions
				.add(Restrictions.neProperty(propertyName, otherPropertyName));
	}

	@Override
	public void gtProperty(String propertyName, String otherPropertyName) {
		criterions
				.add(Restrictions.gtProperty(propertyName, otherPropertyName));
	}

	@Override
	public void ltProperty(String propertyName, String otherPropertyName) {
		criterions
				.add(Restrictions.ltProperty(propertyName, otherPropertyName));
	}

	@Override
	public void geProperty(String propertyName, String otherPropertyName) {
		criterions
				.add(Restrictions.geProperty(propertyName, otherPropertyName));
	}

	@Override
	public void leProperty(String propertyName, String otherPropertyName) {
		criterions
				.add(Restrictions.leProperty(propertyName, otherPropertyName));
	}

	@Override
	public void addOrderAsc(String propertyName) {
		orders.add(Order.asc(propertyName));
	}

	@Override
	public void addOrderDesc(String propertyName) {
		orders.add(Order.desc(propertyName));
	}

	@Override
	public void customCriterion(Criterion criterion) {
		criterions.add(criterion);
	}
}
