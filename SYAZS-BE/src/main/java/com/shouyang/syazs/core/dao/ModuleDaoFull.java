package com.shouyang.syazs.core.dao;

import java.lang.reflect.Field;
import java.util.Properties;

import org.hibernate.internal.SessionFactoryImpl;

import com.shouyang.syazs.core.dao.GenericHibernateDao;
import com.shouyang.syazs.core.entity.GenericEntityFull;

/**
 * ModuleDao
 * 
 * @author Rodertick
 * @version 2014/11/21
 */
public class ModuleDaoFull<T extends GenericEntityFull> extends
		GenericHibernateDao<T> {
	public String getDialect() throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field f = SessionFactoryImpl.class.getDeclaredField("properties");
		f.setAccessible(true);
		Properties p = (Properties) f.get(getSession().getSessionFactory());
		return p.get("hibernate.dialect").toString();
	}
}
