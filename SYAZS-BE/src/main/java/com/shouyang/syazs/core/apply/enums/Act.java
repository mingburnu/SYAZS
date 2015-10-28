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

	快速查詢("快速查詢"),

	項目查詢("項目查詢"),

	借閱("借閱"),

	標題查詢("標題查詢"),

	單位查詢("單位查詢");

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
