package com.shouyang.syazs.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;

/**
 * Restrictions
 * 
 * @author Roderick
 * @version 2014/10/21
 */
public interface DsRestrictions extends Serializable {

	public List<?> getCriterions();

	public List<?> getOrders();

	/**
	 * Apply an "equal" constraint to the named property
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void eq(String propertyName, Object value);

	/**
	 * Apply a "not equal" constraint to the named property
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void ne(String propertyName, Object value);

	/**
	 * Apply a "like" constraint to the named property
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void like(String propertyName, Object value);

	/**
	 * Apply a "like" constraint to the named property
	 * 
	 * @param propertyName
	 * @param value
	 * @param matchMode
	 */
	public void like(String propertyName, String value, MatchMode matchMode);

	/**
	 * A case-insensitive "like", similar to Postgres ilike operator
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void likeIgnoreCase(String propertyName, Object value);

	/**
	 * A case-insensitive "like", similar to Postgres ilike operator
	 * 
	 * @param propertyName
	 * @param value
	 * @param matchMode
	 */
	public void likeIgnoreCase(String propertyName, String value,
			MatchMode matchMode);

	/**
	 * Apply a "greater than" constraint to the named property
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void gt(String propertyName, Object value);

	/**
	 * Apply a "less than" constraint to the named property
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void lt(String propertyName, Object value);

	/**
	 * Apply a "greater than or equal" constraint to the named property
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void ge(String propertyName, Object value);

	/**
	 * Apply a "less than or equal" constraint to the named property
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void le(String propertyName, Object value);

	/**
	 * Apply a "between" constraint to the named property
	 * 
	 * @param propertyName
	 * @param leftValue
	 * @param rightValue
	 */
	public void between(String propertyName, Object leftValue, Object rightValue);

	/**
	 * Apply an "in" constraint to the named property
	 * 
	 * @param propertyName
	 * @param values
	 */
	public void in(String propertyName, Object[] values);

	/**
	 * Apply an "in" constraint to the named property
	 * 
	 * @param propertyName
	 * @param values
	 */
	public void in(String propertyName, Collection<?> values);

	/**
	 * Apply an "is null" constraint to the named property
	 * 
	 * @param propertyName
	 */
	public void isNull(String propertyName);

	/**
	 * Apply an "is not null" constraint to the named property
	 * 
	 * @param propertyName
	 */
	public void isNotNull(String propertyName);

	/**
	 * Apply an "equal" constraint to two properties
	 * 
	 * @param propertyName
	 * @param otherPropertyName
	 */
	public void eqProperty(String propertyName, String otherPropertyName);

	/**
	 * Apply a "not equal" constraint to two properties
	 * 
	 * @param propertyName
	 * @param otherPropertyName
	 */
	public void neProperty(String propertyName, String otherPropertyName);

	/**
	 * Apply a "greater than" constraint to two properties
	 * 
	 * @param propertyName
	 * @param otherPropertyName
	 */
	public void gtProperty(String propertyName, String otherPropertyName);

	/**
	 * Apply a "less than" constraint to two properties
	 * 
	 * @param propertyName
	 * @param otherPropertyName
	 */
	public void ltProperty(String propertyName, String otherPropertyName);

	/**
	 * Apply a "greater than or equal" constraint to two properties
	 * 
	 * @param propertyName
	 * @param otherPropertyName
	 */
	public void geProperty(String propertyName, String otherPropertyName);

	/**
	 * Apply a "less than or equal" constraint to two properties
	 * 
	 * @param propertyName
	 * @param otherPropertyName
	 */
	public void leProperty(String propertyName, String otherPropertyName);

	/**
	 * Ascending order
	 * 
	 * @param propertyName
	 */
	public void addOrderAsc(String propertyName);

	/**
	 * Descending order
	 * 
	 * @param propertyName
	 */
	public void addOrderDesc(String propertyName);

	public void sqlQuery(String sql);
	
	public void customCriterion(Criterion criterion);
}
