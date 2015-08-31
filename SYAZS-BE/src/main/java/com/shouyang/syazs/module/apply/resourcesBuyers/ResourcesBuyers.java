package com.shouyang.syazs.module.apply.resourcesBuyers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.shouyang.syazs.core.entity.GenericEntitySerNo;
import com.shouyang.syazs.module.apply.enums.Category;

@Entity
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Table(name = "resourcesBuyers")
public class ResourcesBuyers extends GenericEntitySerNo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6786606719444916598L;
	@Column(name = "startdate")
	private String startDate;

	@Column(name = "maturitydate")
	private String maturityDate;

	@Column(name = "Rcategory")
	@Enumerated(EnumType.STRING)
	private Category category;

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the maturityDate
	 */
	public String getMaturityDate() {
		return maturityDate;
	}

	/**
	 * @param maturityDate
	 *            the maturityDate to set
	 */
	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	public ResourcesBuyers() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResourcesBuyers(String startDate, String maturityDate,
			Category category) {
		super();
		this.startDate = startDate;
		this.maturityDate = maturityDate;
		this.category = category;
	}
}
