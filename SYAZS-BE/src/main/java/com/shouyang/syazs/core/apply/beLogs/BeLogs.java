package com.shouyang.syazs.core.apply.beLogs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.enums.Act;
import com.shouyang.syazs.core.entity.GenericEntityLog;

/**
 * BE_Logs
 * 
 * @author Roderick
 * @version 2015/01/19
 */
@Entity
@Table(name = "BE_Logs")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BeLogs extends GenericEntityLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 493376977661815130L;

	// 類型
	@Column(name = "actionType")
	@Enumerated(EnumType.STRING)
	private Act actionType;

	// 帳戶流水號
	@ManyToOne
	@JoinColumn(name = "acc_serNo", nullable = true)
	@Autowired
	private AccountNumber accountNumber;

	// 用戶流水號
	@ManyToOne
	@JoinColumn(name = "cus_serNo", nullable = false)
	@Autowired
	private Customer customer;

	/**
	 * @return the actionType
	 */
	public Act getActionType() {
		return actionType;
	}

	/**
	 * @param actionType
	 *            the actionType to set
	 */
	public void setActionType(Act actionType) {
		this.actionType = actionType;
	}

	/**
	 * @return the accountNumber
	 */
	public AccountNumber getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *            the accountNumber to set
	 */
	public void setAccountNumber(AccountNumber accountNumber) {
		this.accountNumber = accountNumber;
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

	public BeLogs() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BeLogs(Act actionType, AccountNumber accountNumber, Customer customer) {
		super();
		this.actionType = actionType;
		this.accountNumber = accountNumber;
		this.customer = customer;
	}

}
