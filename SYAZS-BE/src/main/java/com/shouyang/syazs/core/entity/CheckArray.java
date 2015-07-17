package com.shouyang.syazs.core.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class CheckArray implements Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8820737802424454266L;

	@Transient
	private String[] checkItem;

	@Transient
	private String[] cusSerNo;

	@Transient
	private String[] importItem;

	public String[] getCheckItem() {
		return checkItem;
	}

	public void setCheckItem(String[] checkItem) {
		this.checkItem = checkItem;
	}

	/**
	 * @return the cusSerNo
	 */
	public String[] getCusSerNo() {
		return cusSerNo;
	}

	/**
	 * @param cusSerNo
	 *            the cusSerNo to set
	 */
	public void setCusSerNo(String[] cusSerNo) {
		this.cusSerNo = cusSerNo;
	}

	/**
	 * @return the importItem
	 */
	public String[] getImportItem() {
		return importItem;
	}

	/**
	 * @param importItem
	 *            the importItem to set
	 */
	public void setImportItem(String[] importItem) {
		this.importItem = importItem;
	}
}
