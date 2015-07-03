package com.shouyang.syazs.core.web;

import java.io.InputStream;

import com.shouyang.syazs.core.entity.GenericEntityFull;

/**
 * GenericCRUDAction
 * 
 * @author Roderick
 * @version 2014/11/21
 */
public abstract class GenericWebActionFull<T extends GenericEntityFull> extends
		GenericCRUDAction<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -923075655713880057L;

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
