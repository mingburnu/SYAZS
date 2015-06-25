package com.shouyang.syazs.core.apply.groupMapping;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.core.entity.GenericEntitySerNo;

@Entity
@Table(name = "group_mapping")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GroupMapping extends GenericEntitySerNo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3903258162518581393L;

	/**
	 * 上層編號
	 */
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "parentGID", nullable = true)
	private GroupMapping GroupMapping;

	/**
	 * 名稱
	 */
	@Column(name = "Title")
	private String title;

	/**
	 * 層級
	 */
	@Column(name = "Level")
	private Integer level;

	/**
	 * @return the groupMapping
	 */
	public GroupMapping getGroupMapping() {
		return GroupMapping;
	}

	/**
	 * @param groupMapping
	 *            the groupMapping to set
	 */
	public void setGroupMapping(GroupMapping groupMapping) {
		GroupMapping = groupMapping;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	public GroupMapping(GroupMapping groupMapping, String title, Integer level) {
		super();
		GroupMapping = groupMapping;
		this.title = title;
		this.level = level;
	}	
}
