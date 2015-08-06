package com.shouyang.syazs.module.apply.resourcesUnion;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.entity.GenericEntitySerNo;

@Entity
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Table(name = "resourcesUnion")
public class ResourcesUnion extends GenericEntitySerNo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4506632636578324717L;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "cus_serNo", nullable = false)
	@Autowired
	private Customer customer;

	@Column(name = "ebk_SerNo")
	private Long ebkSerNo;

	@Column(name = "dat_SerNo")
	private Long datSerNo;

	@Column(name = "jou_SerNo")
	private Long jouSerNo;

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
	 * @return the ebkSerNo
	 */
	public Long getEbkSerNo() {
		return ebkSerNo;
	}

	/**
	 * @param ebkSerNo
	 *            the ebkSerNo to set
	 */
	public void setEbkSerNo(Long ebkSerNo) {
		this.ebkSerNo = ebkSerNo;
	}

	/**
	 * @return the datSerNo
	 */
	public Long getDatSerNo() {
		return datSerNo;
	}

	/**
	 * @param datSerNo
	 *            the datSerNo to set
	 */
	public void setDatSerNo(Long datSerNo) {
		this.datSerNo = datSerNo;
	}

	/**
	 * @return the jouSerNo
	 */
	public Long getJouSerNo() {
		return jouSerNo;
	}

	/**
	 * @param jouSerNo
	 *            the jouSerNo to set
	 */
	public void setJouSerNo(Long jouSerNo) {
		this.jouSerNo = jouSerNo;
	}

	public ResourcesUnion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResourcesUnion(Customer customer, Long ebkSerNo, Long datSerNo,
			Long jouSerNo) {
		super();
		this.customer = customer;
		this.ebkSerNo = ebkSerNo;
		this.datSerNo = datSerNo;
		this.jouSerNo = jouSerNo;
	}

}
