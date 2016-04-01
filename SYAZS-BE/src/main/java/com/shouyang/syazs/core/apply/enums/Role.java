package com.shouyang.syazs.core.apply.enums;

/**
 * Role
 * 
 * @author Roderick
 * @version 2014/9/29
 */
public enum Role {

	/**
	 * 系統管理員
	 */
	系統管理員("系統管理員"),

	/**
	 * 管理員
	 */
	管理員("管理員"),	

	不明("不明");

	private String role;

	private Role() {

	}

	private Role(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

}
