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
	private Long[] resDbSerNo;

	/**
	 * @return the resDbSerNo
	 */
	public Long[] getResDbSerNo() {
		return resDbSerNo;
	}

	/**
	 * @param resDbSerNo
	 *            the resDbSerNo to set
	 */
	public void setResDbSerNo(Long[] resDbSerNo) {
		this.resDbSerNo = resDbSerNo;
	}
}
