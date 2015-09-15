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

	綜合查詢("綜合查詢"),

	項目查詢("項目查詢"),

	借閱("借閱");

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
