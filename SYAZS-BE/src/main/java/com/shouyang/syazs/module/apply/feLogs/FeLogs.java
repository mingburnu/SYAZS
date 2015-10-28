package com.shouyang.syazs.module.apply.feLogs;

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
import com.shouyang.syazs.module.apply.database.Database;
import com.shouyang.syazs.module.apply.ebook.Ebook;
import com.shouyang.syazs.module.apply.journal.Journal;

/**
 * FE_Logs
 * 
 * @author Roderick
 * @version 2015/01/26
 */
@Entity
@Table(name = "FE_Logs")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FeLogs extends GenericEntityLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1299854351303327443L;

	// 類型
	@Column(name = "actionType")
	@Enumerated(EnumType.STRING)
	private Act actionType;

	// 關鍵字
	@Column(name = "keyword")
	private String keyword;

	/**
	 * 用戶流水號
	 */
	@ManyToOne
	@JoinColumn(name = "cus_serNo", nullable = false)
	@Autowired
	private Customer customer;

	/**
	 * 帳戶流水號
	 */
	@ManyToOne
	@JoinColumn(name = "acc_SerNo", nullable = true)
	@Autowired
	private AccountNumber accountNumber;

	// 資料庫流水號
	@ManyToOne
	@JoinColumn(name = "dat_SerNo")
	private Database database;

	// 電子書流水號
	@ManyToOne
	@JoinColumn(name = "ebk_SerNo")
	private Ebook ebook;

	// 期刊流水號
	@ManyToOne
	@JoinColumn(name = "jou_SerNo")
	private Journal journal;

	// 連結使用紀錄
	@Column(name = "URLclick")
	private Boolean click;

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
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword
	 *            the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
	 * @return the database
	 */
	public Database getDatabase() {
		return database;
	}

	/**
	 * @param database
	 *            the database to set
	 */
	public void setDatabase(Database database) {
		this.database = database;
	}

	/**
	 * @return the ebook
	 */
	public Ebook getEbook() {
		return ebook;
	}

	/**
	 * @param ebook
	 *            the ebook to set
	 */
	public void setEbook(Ebook ebook) {
		this.ebook = ebook;
	}

	/**
	 * @return the journal
	 */
	public Journal getJournal() {
		return journal;
	}

	/**
	 * @param journal
	 *            the journal to set
	 */
	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	/**
	 * @return the click
	 */
	public Boolean getClick() {
		return click;
	}

	/**
	 * @param click
	 *            the click to set
	 */
	public void setClick(Boolean click) {
		this.click = click;
	}

	public FeLogs() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FeLogs(Act actionType, String keyword, Customer customer,
			AccountNumber accountNumber, Database database, Ebook ebook,
			Journal journal, Boolean click) {
		super();
		this.actionType = actionType;
		this.keyword = keyword;
		this.customer = customer;
		this.accountNumber = accountNumber;
		this.database = database;
		this.ebook = ebook;
		this.journal = journal;
		this.click = click;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FeLogs [actionType=" + actionType + ", keyword=" + keyword
				+ ", customer=" + customer + ", accountNumber=" + accountNumber
				+ ", database=" + database + ", ebook=" + ebook + ", journal="
				+ journal + ", click=" + click + "]";
	}

}
