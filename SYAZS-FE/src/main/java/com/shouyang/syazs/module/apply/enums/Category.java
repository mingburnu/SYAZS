package com.shouyang.syazs.module.apply.enums;

/**
 * 資源類別
 * 
 * @author Roderick
 * @version 2014/10/15
 */
public enum Category {
	/** 買斷. */
	買斷("買斷"),

	/** 租貸. */
	租貸("租貸"),
	
	/** 未註明. */
	未註明("");

	private String category;

	private Category() {
	}

	private Category(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

}
