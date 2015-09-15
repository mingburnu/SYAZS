package com.shouyang.syazs.core.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class SearchProperties extends FileIoProperties {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7905793020259867930L;

	@Transient
	private String option;

	@Transient
	private String indexTerm;

	/**
	 * @return the option
	 */
	public String getOption() {
		return option;
	}

	/**
	 * @param option
	 *            the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}

	/**
	 * @return the indexTerm
	 */
	public String getIndexTerm() {
		return indexTerm;
	}

	/**
	 * @param indexTerm
	 *            the indexTerm to set
	 */
	public void setIndexTerm(String indexTerm) {
		this.indexTerm = indexTerm;
	}

}
