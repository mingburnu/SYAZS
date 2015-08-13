package com.shouyang.syazs.core.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class CheckArray extends SearchProperties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8820737802424454266L;

	@Transient
	private Long[] checkItem;

	@Transient
	private Long[] refSerNo;

	@Transient
	private Integer[] importItem;

	public Long[] getCheckItem() {
		return checkItem;
	}

	public void setCheckItem(Long[] checkItem) {
		this.checkItem = checkItem;
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
	 * @return the importItem
	 */
	public Integer[] getImportItem() {
		return importItem;
	}

	/**
	 * @param importItem
	 *            the importItem to set
	 */
	public void setImportItem(Integer[] importItem) {
		this.importItem = importItem;
	}
}
