package com.shouyang.syazs.core.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class GroupProperties implements Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1270586375481968902L;

	@Transient
	private String firstLevelName;

	@Transient
	private String secondLevelName;

	@Transient
	private String thirdLevelName;

	@Transient
	private String firstLevelOption;

	@Transient
	private String secondLevelOption;

	@Transient
	private String thirdLevelOption;

	@Transient
	private Long firstLevelSelect;

	@Transient
	private Long secondLevelSelect;

	/**
	 * @return the firstLevelName
	 */
	public String getFirstLevelName() {
		return firstLevelName;
	}

	/**
	 * @param firstLevelName
	 *            the firstLevelName to set
	 */
	public void setFirstLevelName(String firstLevelName) {
		this.firstLevelName = firstLevelName;
	}

	/**
	 * @return the secondLevelName
	 */
	public String getSecondLevelName() {
		return secondLevelName;
	}

	/**
	 * @param secondLevelName
	 *            the secondLevelName to set
	 */
	public void setSecondLevelName(String secondLevelName) {
		this.secondLevelName = secondLevelName;
	}

	/**
	 * @return the thirdLevelName
	 */
	public String getThirdLevelName() {
		return thirdLevelName;
	}

	/**
	 * @param thirdLevelName
	 *            the thirdLevelName to set
	 */
	public void setThirdLevelName(String thirdLevelName) {
		this.thirdLevelName = thirdLevelName;
	}

	/**
	 * @return the firstLevelOption
	 */
	public String getFirstLevelOption() {
		return firstLevelOption;
	}

	/**
	 * @param firstLevelOption
	 *            the firstLevelOption to set
	 */
	public void setFirstLevelOption(String firstLevelOption) {
		this.firstLevelOption = firstLevelOption;
	}

	/**
	 * @return the secondLevelOption
	 */
	public String getSecondLevelOption() {
		return secondLevelOption;
	}

	/**
	 * @param secondLevelOption
	 *            the secondLevelOption to set
	 */
	public void setSecondLevelOption(String secondLevelOption) {
		this.secondLevelOption = secondLevelOption;
	}

	/**
	 * @return the thirdLevelOption
	 */
	public String getThirdLevelOption() {
		return thirdLevelOption;
	}

	/**
	 * @param thirdLevelOption
	 *            the thirdLevelOption to set
	 */
	public void setThirdLevelOption(String thirdLevelOption) {
		this.thirdLevelOption = thirdLevelOption;
	}

	/**
	 * @return the firstLevelSelect
	 */
	public Long getFirstLevelSelect() {
		return firstLevelSelect;
	}

	/**
	 * @param firstLevelSelect
	 *            the firstLevelSelect to set
	 */
	public void setFirstLevelSelect(Long firstLevelSelect) {
		this.firstLevelSelect = firstLevelSelect;
	}

	/**
	 * @return the secondLevelSelect
	 */
	public Long getSecondLevelSelect() {
		return secondLevelSelect;
	}

	/**
	 * @param secondLevelSelect
	 *            the secondLevelSelect to set
	 */
	public void setSecondLevelSelect(Long secondLevelSelect) {
		this.secondLevelSelect = secondLevelSelect;
	}
}
