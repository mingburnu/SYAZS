package com.shouyang.syazs.core.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shouyang.syazs.core.apply.accountNumber.AccountNumber;

/**
 * @author Roderick
 * 
 */
@MappedSuperclass
public abstract class GenericEntityGroup extends FileIoProperties {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8739689797669392643L;

	/** The log. */
	@Transient
	protected final transient Logger log = LoggerFactory.getLogger(getClass());

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "serNo", unique = true, nullable = false, insertable = true, updatable = false, precision = 20)
	private Long serNo;

	/** The last modified by. */
	@Column(name = "uUid")
	private String uUid;

	@Transient
	private AccountNumber lastModifiedUser;

	/** The last modified date. */
	@Column(name = "uDTime")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime uDTime;

	/**
	 * @return the serNo
	 */
	public Long getSerNo() {
		return serNo;
	}

	/**
	 * @param serNo
	 *            the serNo to set
	 */
	public void setSerNo(Long serNo) {
		this.serNo = serNo;
	}

	/**
	 * @return the uUid
	 */
	public String getuUid() {
		return uUid;
	}

	/**
	 * @param uUid
	 *            the uUid to set
	 */
	public void setuUid(String uUid) {
		this.uUid = uUid;
	}

	/**
	 * @return the uDTime
	 */
	public LocalDateTime getuDTime() {
		return uDTime;
	}

	/**
	 * @param uDTime
	 *            the uDTime to set
	 */
	public void setuDTime(LocalDateTime uDTime) {
		this.uDTime = uDTime;
	}

	/**
	 * @return the lastModifiedUser
	 */
	public AccountNumber getLastModifiedUser() {
		return lastModifiedUser;
	}

	/**
	 * @param lastModifiedUser
	 *            the lastModifiedUser to set
	 */
	public void setLastModifiedUser(AccountNumber lastModifiedUser) {
		this.lastModifiedUser = lastModifiedUser;
	}

	/**
	 * check entity is new or not.
	 * 
	 * @return true, if is new
	 */
	public boolean isNew() {
		return serNo == null;
	}

	/**
	 * initial insert
	 * <p>
	 * initial id, tpmId, create, modify and status when insert <br>
	 * <p>
	 * .
	 * 
	 * @param user
	 *            the user
	 * @param sysStatus
	 *            the sys status
	 * @param bpmId
	 *            the bpm id
	 */
	public void initInsert(AccountNumber user) {
		this.setuUid(user.getUserId());
		this.setuDTime(new LocalDateTime());
	}

	/**
	 * initial update
	 * <p>
	 * initial modify, status when update <br>
	 * <p>
	 * .
	 * 
	 * @param user
	 *            the user
	 * @param sysStatus
	 *            the sys status
	 */
	public void initUpdate(AccountNumber user) {
		this.setuUid(user.getUserId());
		this.setuDTime(new LocalDateTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.DEFAULT_STYLE);
	}

}
