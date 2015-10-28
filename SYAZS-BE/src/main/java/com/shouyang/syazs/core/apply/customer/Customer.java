package com.shouyang.syazs.core.apply.customer;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;
import com.shouyang.syazs.core.apply.beLogs.BeLogs;
import com.shouyang.syazs.core.apply.group.Group;
import com.shouyang.syazs.core.apply.ipRange.IpRange;
import com.shouyang.syazs.core.entity.GenericEntityFull;

@Entity
@Table(name = "customer")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Customer extends GenericEntityFull {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5508975058661670537L;

	// 名稱
	@Column(name = "name", unique = true)
	private String name;

	// 英文名稱
	@Column(name = "engName")
	private String engName;

	// 地址
	@Column(name = "address")
	private String address;

	// 電話
	@Column(name = "tel")
	private String tel;

	// 聯絡人
	@Column(name = "contactUserName")
	private String contactUserName;

	// 備註
	@Column(name = "memo")
	@Type(type = "text")
	private String memo;

	@OneToMany(mappedBy = "customer", orphanRemoval = true)
	private Set<AccountNumber> accountNumbers;

	@OneToMany(mappedBy = "customer", orphanRemoval = true)
	private Set<Group> groups;

	@OneToMany(mappedBy = "customer", orphanRemoval = true)
	private Set<IpRange> ipRanges;

	@OneToMany(mappedBy = "customer", orphanRemoval = true)
	private Set<BeLogs> beLogses;

	@OneToMany(mappedBy = "customer", orphanRemoval = true)
	private Set<BeLogs> feLogses;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the engName
	 */
	public String getEngName() {
		return engName;
	}

	/**
	 * @param engName
	 *            the engName to set
	 */
	public void setEngName(String engName) {
		this.engName = engName;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel
	 *            the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the contactUserName
	 */
	public String getContactUserName() {
		return contactUserName;
	}

	/**
	 * @param contactUserName
	 *            the contactUserName to set
	 */
	public void setContactUserName(String contactUserName) {
		this.contactUserName = contactUserName;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the accountNumbers
	 */
	public Set<AccountNumber> getAccountNumbers() {
		return accountNumbers;
	}

	/**
	 * @return the groups
	 */
	public Set<Group> getGroups() {
		return groups;
	}

	/**
	 * @return the ipRanges
	 */
	public Set<IpRange> getIpRanges() {
		return ipRanges;
	}

	/**
	 * @return the beLogses
	 */
	public Set<BeLogs> getBeLogses() {
		return beLogses;
	}

	/**
	 * @return the feLogses
	 */
	public Set<BeLogs> getFeLogses() {
		return feLogses;
	}

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Customer(String name, String engName, String address, String tel,
			String contactUserName, String memo) {
		super();
		this.name = name;
		this.engName = engName;
		this.address = address;
		this.tel = tel;
		this.contactUserName = contactUserName;
		this.memo = memo;
	}
}
