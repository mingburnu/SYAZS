package com.shouyang.syazs.module.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.shouyang.syazs.core.entity.GenericEntityFull;

@MappedSuperclass
public abstract class ModuleProperties extends GenericEntityFull {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5011709439415119645L;

	@Transient
	private Long refSerNo;

	@Transient
	private String backURL;

	/**
	 * @return the refSerNo
	 */
	public Long getRefSerNo() {
		return refSerNo;
	}

	/**
	 * @param refSerNo
	 *            the refSerNo to set
	 */
	public void setRefSerNo(Long refSerNo) {
		this.refSerNo = refSerNo;
	}

	/**
	 * @return the backURL
	 */
	public String getBackURL() {
		return backURL;
	}

	/**
	 * @param backURL
	 *            the backURL to set
	 */
	public void setBackURL(String backURL) {
		this.backURL = backURL;
	}
}
