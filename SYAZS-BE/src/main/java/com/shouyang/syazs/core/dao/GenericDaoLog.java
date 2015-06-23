package com.shouyang.syazs.core.dao;

import org.apache.log4j.Logger;

import com.shouyang.syazs.core.entity.GenericEntityLog;

/**
 * GenericDao
 * 
 * @author Roderick
 * @version 2015/01/19
 */
public abstract class GenericDaoLog<T extends GenericEntityLog>
		implements Dao<T> {

	protected final transient Logger log = Logger.getLogger(getClass());

}
