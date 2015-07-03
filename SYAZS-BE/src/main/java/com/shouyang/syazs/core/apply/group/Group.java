package com.shouyang.syazs.core.apply.group;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.groupMapping.GroupMapping;
import com.shouyang.syazs.core.entity.GenericEntityGroup;

@Entity
@Table(name = "groups")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Group extends GenericEntityGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1360265508471175349L;

	/**
	 * group mapping
	 */
	@OneToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "gro_m_serNo", nullable = false)
	private GroupMapping groupMapping;

	/**
	 * 用戶流水號
	 */
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "cus_serNo", nullable = false)
	private Customer customer;

	@Column(name = "groupName")
	private String groupName;

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
	private String firstLevelSelect;
	
	@Transient
	private String secondLevelSelect;

	/**
	 * @return the groupMapping
	 */
	public GroupMapping getGroupMapping() {
		return groupMapping;
	}

	/**
	 * @param groupMapping
	 *            the groupMapping to set
	 */
	public void setGroupMapping(GroupMapping groupMapping) {
		this.groupMapping = groupMapping;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 *            the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the firstLevelName
	 */
	public String getFirstLevelName() {
		return firstLevelName;
	}

	/**
	 * @param firstLevelName the firstLevelName to set
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
	 * @param secondLevelName the secondLevelName to set
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
	 * @param thirdLevelName the thirdLevelName to set
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
	 * @param firstLevelOption the firstLevelOption to set
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
	 * @param secondLevelOption the secondLevelOption to set
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
	 * @param thirdLevelOption the thirdLevelOption to set
	 */
	public void setThirdLevelOption(String thirdLevelOption) {
		this.thirdLevelOption = thirdLevelOption;
	}

	/**
	 * @return the firstLevelSelect
	 */
	public String getFirstLevelSelect() {
		return firstLevelSelect;
	}

	/**
	 * @param firstLevelSelect the firstLevelSelect to set
	 */
	public void setFirstLevelSelect(String firstLevelSelect) {
		this.firstLevelSelect = firstLevelSelect;
	}

	/**
	 * @return the secondLevelSelect
	 */
	public String getSecondLevelSelect() {
		return secondLevelSelect;
	}

	/**
	 * @param secondLevelSelect the secondLevelSelect to set
	 */
	public void setSecondLevelSelect(String secondLevelSelect) {
		this.secondLevelSelect = secondLevelSelect;
	}

	public Group() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Group(GroupMapping groupMapping, Customer customer, String groupName) {
		super();
		this.groupMapping = groupMapping;
		this.customer = customer;
		this.groupName = groupName;
	}
}
