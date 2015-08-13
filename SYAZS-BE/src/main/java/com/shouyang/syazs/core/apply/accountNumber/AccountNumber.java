package com.shouyang.syazs.core.apply.accountNumber;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.core.apply.customer.Customer;
import com.shouyang.syazs.core.apply.enums.Role;
import com.shouyang.syazs.core.apply.enums.Status;
import com.shouyang.syazs.core.entity.GenericEntityFull;

/**
 * 使用者
 * 
 * @author Roderick
 * @version 2014/9/30
 */
@Entity
@Table(name = "accountNumber")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccountNumber extends GenericEntityFull {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5804311089729989927L;

	/**
	 * 使用者代號
	 */
	@Column(name = "userID", unique = true)
	private String userId;

	/**
	 * 使用者密碼
	 */
	@Column(name = "userPW")
	private String userPw;

	/**
	 * 使用者姓名
	 */
	@Column(name = "userName")
	private String userName;

	/**
	 * email
	 */
	@Column(name = "email")
	private String email;

	/**
	 * user角色
	 */
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Role role;

	/**
	 * 狀態
	 */
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private Status status;

	/**
	 * 用戶流水號
	 */
	@ManyToOne
	@JoinColumn(name = "cus_serNo", nullable = false)
	@Autowired
	private Customer customer;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userPw
	 */
	@JSON(serialize = false)
	public String getUserPw() {
		return userPw;
	}

	/**
	 * @param userPw
	 *            the userPw to set
	 */
	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
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

	public AccountNumber() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccountNumber(String userId, String userPw, String userName,
			String email, Role role, Status status, Customer customer) {
		super();
		this.userId = userId;
		this.userPw = userPw;
		this.userName = userName;
		this.email = email;
		this.role = role;
		this.status = status;
		this.customer = customer;
	}
}
