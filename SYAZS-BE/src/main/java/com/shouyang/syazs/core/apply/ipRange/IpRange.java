package com.shouyang.syazs.core.apply.ipRange;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.entity.GenericEntityFull;

@Entity
@Table(name = "ip_range")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IpRange extends GenericEntityFull {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1606674601447344379L;

	/**
	 * 用戶流水號
	 */
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "cus_serNo", nullable = false)
	private Customer customer;

	// IP開始
	@Column(name="ipRangeStart")
	private String ipRangeStart;

	// IP結束
	@Column(name="ipRangeEnd")
	private String ipRangeEnd;
	
	@Transient
	private int listNo;

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the ipRangeStart
	 */
	public String getIpRangeStart() {
		return ipRangeStart;
	}

	/**
	 * @param ipRangeStart
	 *            the ipRangeStart to set
	 */
	public void setIpRangeStart(String ipRangeStart) {
		this.ipRangeStart = ipRangeStart;
	}

	/**
	 * @return the ipRangeEnd
	 */
	public String getIpRangeEnd() {
		return ipRangeEnd;
	}

	/**
	 * @param ipRangeEnd
	 *            the ipRangeEnd to set
	 */
	public void setIpRangeEnd(String ipRangeEnd) {
		this.ipRangeEnd = ipRangeEnd;
	}

	/**
	 * @return the listNo
	 */
	public int getListNo() {
		return listNo;
	}

	/**
	 * @param listNo the listNo to set
	 */
	public void setListNo(int listNo) {
		this.listNo = listNo;
	}

}
