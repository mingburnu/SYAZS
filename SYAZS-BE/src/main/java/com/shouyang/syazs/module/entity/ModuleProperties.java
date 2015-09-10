package com.shouyang.syazs.module.entity;

import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.google.common.collect.Lists;
import com.shouyang.syazs.core.entity.GenericEntityFull;
import com.shouyang.syazs.module.apply.referenceOwner.ReferenceOwner;

@MappedSuperclass
public abstract class ModuleProperties extends GenericEntityFull {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5011709439415119645L;

	@Transient
	private List<ReferenceOwner> owners = Lists.newArrayList();

	@Transient
	private Long[] refSerNo;

	@Transient
	private Long[] resDbSerNo;

	@Transient
	private String tempNote;

	/**
	 * @return the owners
	 */
	public List<ReferenceOwner> getOwners() {
		return owners;
	}

	/**
	 * @param owners
	 *            the owners to set
	 */
	public void setOwners(List<ReferenceOwner> owners) {
		this.owners = owners;
	}

	/**
	 * @return the refSerNo
	 */
	public Long[] getRefSerNo() {
		return refSerNo;
	}

	/**
	 * @param refSerNo
	 *            the refSerNo to set
	 */
	public void setRefSerNo(Long[] refSerNo) {
		this.refSerNo = refSerNo;
	}

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

	/**
	 * @return the tempNote
	 */
	public String getTempNote() {
		return tempNote;
	}

	/**
	 * @param tempNote
	 *            the tempNote to set
	 */
	public void setTempNote(String tempNote) {
		this.tempNote = tempNote;
	}
}
