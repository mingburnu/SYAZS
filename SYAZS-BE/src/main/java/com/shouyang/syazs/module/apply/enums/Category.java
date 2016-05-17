package com.shouyang.syazs.module.apply.enums;

/**
 * 資源類別
 * 
 * @author Roderick
 * @version 2014/10/15
 */
public enum Category {
	/** 賣斷. */
	賣斷("賣斷"),

	/** 租賃. */
	租賃("租賃"),

	/** 免費提供閱讀. */
	免費提供閱讀("免費提供閱讀"),

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

	public static Category getByToken(Integer token) {
		switch (token) {
		case 0:
			return Category.未註明;

		case 1:
			return Category.賣斷;

		case 2:
			return Category.租賃;

		case 3:
			return Category.免費提供閱讀;
		}

		return Category.未註明;
	}
}