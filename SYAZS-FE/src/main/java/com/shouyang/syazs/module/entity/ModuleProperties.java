package com.shouyang.syazs.module.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import com.shouyang.syazs.core.entity.GenericEntityFull;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;

@MappedSuperclass
public abstract class ModuleProperties extends GenericEntityFull {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5011709439415119645L;

	@Transient
	@Autowired
	private ResourcesBuyers resourcesBuyers;

	@Transient
	private Long cusSerNo;
	
	@Transient
	private String backURL;

	public ResourcesBuyers getResourcesBuyers() {
		return resourcesBuyers;
	}

	public void setResourcesBuyers(ResourcesBuyers resourcesBuyers) {
		this.resourcesBuyers = resourcesBuyers;
	}

	/**
	 * @return the cusSerNo
	 */
	public Long getCusSerNo() {
		return cusSerNo;
	}

	/**
	 * @param cusSerNo
	 *            the cusSerNo to set
	 */
	public void setCusSerNo(Long cusSerNo) {
		this.cusSerNo = cusSerNo;
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
