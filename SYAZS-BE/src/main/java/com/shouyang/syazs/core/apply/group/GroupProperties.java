package com.shouyang.syazs.core.apply.group;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.shouyang.syazs.core.entity.GenericEntityGroup;

@MappedSuperclass
public class GroupProperties extends GenericEntityGroup {
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

	@Transient
	private Group firstLevelGroup;

	@Transient
	private Group secondLevelGroup;

	@Transient
	private Group thirdLevelGroup;

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

	/**
	 * @return the firstLevelGroup
	 */
	public Group getFirstLevelGroup() {
		return firstLevelGroup;
	}

	/**
	 * @param firstLevelGroup
	 *            the firstLevelGroup to set
	 */
	public void setFirstLevelGroup(Group firstLevelGroup) {
		this.firstLevelGroup = firstLevelGroup;
	}

	/**
	 * @return the secondLevelGroup
	 */
	public Group getSecondLevelGroup() {
		return secondLevelGroup;
	}

	/**
	 * @param secondLevelGroup
	 *            the secondLevelGroup to set
	 */
	public void setSecondLevelGroup(Group secondLevelGroup) {
		this.secondLevelGroup = secondLevelGroup;
	}

	/**
	 * @return the thirdLevelGroup
	 */
	public Group getThirdLevelGroup() {
		return thirdLevelGroup;
	}

	/**
	 * @param thirdLevelGroup
	 *            the thirdLevelGroup to set
	 */
	public void setThirdLevelGroup(Group thirdLevelGroup) {
		this.thirdLevelGroup = thirdLevelGroup;
	}
}
