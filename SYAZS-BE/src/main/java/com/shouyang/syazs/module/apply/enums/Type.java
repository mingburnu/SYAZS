package com.shouyang.syazs.module.apply.enums;

/**
 * 資源種類
 * 
 * @author Roderick
 * @version 2014/10/15
 */
public enum Type {
	/** 電子書. */
	電子書("電子書"),

	/** 期刊. */
	期刊("期刊"),

	/** 資料庫. */
	資料庫("資料庫");

	private String type;

	private Type() {
	}

	private Type(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
