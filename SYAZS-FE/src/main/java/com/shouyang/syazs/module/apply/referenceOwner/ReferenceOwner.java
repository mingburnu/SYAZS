package com.shouyang.syazs.module.apply.referenceOwner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.core.entity.GenericEntityFull;

@Entity
@Table(name = "referenceOwner")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReferenceOwner extends GenericEntityFull {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6946147773195509574L;

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

	public ReferenceOwner() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReferenceOwner(String name, String engName, String address, String tel,
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
