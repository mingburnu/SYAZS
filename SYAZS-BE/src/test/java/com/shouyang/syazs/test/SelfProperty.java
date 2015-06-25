package com.shouyang.syazs.test;

public class SelfProperty {

	private long serNo;
	
	private SelfProperty selfProperty;

	/**
	 * @return the serNo
	 */
	public long getSerNo() {
		return serNo;
	}

	/**
	 * @param serNo the serNo to set
	 */
	public void setSerNo(long serNo) {
		this.serNo = serNo;
	}

	/**
	 * @return the selfProperty
	 */
	public SelfProperty getSelfProperty() {
		return selfProperty;
	}

	/**
	 * @param selfProperty the selfProperty to set
	 */
	public void setSelfProperty(SelfProperty selfProperty) {
		this.selfProperty = selfProperty;
	}
	
}
