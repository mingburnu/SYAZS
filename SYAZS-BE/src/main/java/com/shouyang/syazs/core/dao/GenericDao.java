package com.shouyang.syazs.core.dao;

import org.apache.log4j.Logger;

import com.shouyang.syazs.core.dao.Dao;
import com.shouyang.syazs.core.entity.Entity;

/**
 * GenericDao
 * @author Roderick
 * @version 2014/9/29
 */
public abstract class GenericDao<T extends Entity> implements Dao<T> {
	
	protected final transient Logger log = Logger.getLogger(getClass());

}
