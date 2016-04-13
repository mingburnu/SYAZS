package com.shouyang.syazs.core.apply.enums;

/**
 * Action
 * 
 * @author Roderick
 * @version 2015/01/19
 */
public enum Act {
	/**
	 * Logs
	 */
	登入("登入"),

	登出("登出"),

	查詢("查詢"),

	點擊("點擊");

	private String act;

	private Act() {
	}

	private Act(String act) {
		this.act = act;
	}

	/**
	 * @return the action
	 */
	public String getAct() {
		return act;
	}

}
