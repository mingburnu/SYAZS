package com.shouyang.syazs.module.entity;

import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.entity.GenericEntityFull;
import com.shouyang.syazs.module.apply.resourcesBuyers.ResourcesBuyers;

@MappedSuperclass
public abstract class ModuleProperties extends GenericEntityFull {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5011709439415119645L;

	@Transient
	@Autowired
	private ResourcesBuyers resourcesBuyers;

	@Transient
	private List<Customer> customers;

	public ResourcesBuyers getResourcesBuyers() {
		return resourcesBuyers;
	}

	public void setResourcesBuyers(ResourcesBuyers resourcesBuyers) {
		this.resourcesBuyers = resourcesBuyers;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
}
