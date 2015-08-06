package com.shouyang.syazs.module.entity;

import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.entity.GenericEntityFull;

@MappedSuperclass
public abstract class ModuleProperties extends GenericEntityFull {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5011709439415119645L;

	@Transient
	private List<Customer> customers;

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
}
