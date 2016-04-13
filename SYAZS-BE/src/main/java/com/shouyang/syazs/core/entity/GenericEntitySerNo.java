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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roderick
 * 
 */
@MappedSuperclass
public abstract class GenericEntitySerNo extends FileIoProperties {

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
	 * check entity has id or not.
	 * 
	 * @return true, if has id
	 */
	public boolean hasSerNo() {
		return serNo != null;
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
