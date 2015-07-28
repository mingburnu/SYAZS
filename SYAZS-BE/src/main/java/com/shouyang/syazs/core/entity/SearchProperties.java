package com.shouyang.syazs.core.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.joda.time.LocalDateTime;

@MappedSuperclass
public abstract class SearchProperties extends GroupProperties {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7905793020259867930L;

	@Transient
	private String option;

	@Transient
	private LocalDateTime start;

	@Transient
	private LocalDateTime end;

	@Transient
	private Integer count;

	@Transient
	private Integer rank;

	@Transient
	private Integer listNo;

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getListNo() {
		return listNo;
	}

	public void setListNo(Integer listNo) {
		this.listNo = listNo;
	}
}
