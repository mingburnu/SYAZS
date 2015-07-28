package com.shouyang.syazs.core.apply.groupMapping;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.core.apply.group.Group;
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
	private GroupMapping parentGroupMapping;

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

	@OneToOne(mappedBy = "groupMapping", orphanRemoval = true)
	private Group group;

	@OneToMany(mappedBy = "parentGroupMapping", orphanRemoval = true)
	private Set<GroupMapping> groupMappings;

	/**
	 * @return the parentGroupMapping
	 */
	public GroupMapping getParentGroupMapping() {
		return parentGroupMapping;
	}

	/**
	 * @param parentGroupMapping
	 *            the parentGroupMapping to set
	 */
	public void setParentGroupMapping(GroupMapping parentGroupMapping) {
		this.parentGroupMapping = parentGroupMapping;
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

	/**
	 * @return the group
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * @return the groupMappings
	 */
	public Set<GroupMapping> getGroupMappings() {
		return groupMappings;
	}

	public GroupMapping() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GroupMapping(GroupMapping parentGroupMapping, String title,
			Integer level) {
		super();
		this.parentGroupMapping = parentGroupMapping;
		this.title = title;
		this.level = level;
	}
}
