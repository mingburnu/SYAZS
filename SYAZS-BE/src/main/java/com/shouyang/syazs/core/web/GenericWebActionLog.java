package com.shouyang.syazs.core.web;

import java.io.InputStream;

import com.shouyang.syazs.core.entity.GenericEntityLog;

/**
 * GenericCRUDAction
 * 
 * @author Roderick
 * @version 2014/11/21
 */
public abstract class GenericWebActionLog<T extends GenericEntityLog> extends
		GenericCRUDAction<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2906569110124794683L;

	private InputStream inputStream;

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream
	 *            the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
