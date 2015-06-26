package com.shouyang.syazs.core.apply.group;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
