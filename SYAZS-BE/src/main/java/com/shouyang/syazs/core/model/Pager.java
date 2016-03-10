package com.shouyang.syazs.core.model;

import java.io.Serializable;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Roderick
 * @version 2014/12/01
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Pager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6769777808559296049L;

	/**
	 * 每頁顯示筆數
	 */
	private Integer recordPerPage = 10;

	/**
	 * 當前頁數
	 */
	private Integer currentPage = 1;

	/**
	 * 總筆數
	 */
	private Long totalRecord;

	private Integer offset = 0;

	/**
	 * 紀錄點
	 */
	private Integer recordPoint;

	public Integer getRecordPerPage() {
		return recordPerPage;
	}

	public void setRecordPerPage(Integer recordPerPage) {
		if (recordPerPage > 0) {
			this.recordPerPage = recordPerPage;
		}
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		if (currentPage > 0) {
			this.currentPage = currentPage;
		}
	}

	public Long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Long totalRecord) {
		this.totalRecord = totalRecord;
	}

	public Integer getOffset() {
		this.offset = getRecordPerPage() * (getCurrentPage() - 1);
		return offset;
	}

	/**
	 * @return the recordPoint
	 */
	public Integer getRecordPoint() {
		return recordPoint;
	}

	/**
	 * @param recordPoint
	 *            the recordPoint to set
	 */
	public void setRecordPoint(Integer recordPoint) {
		this.recordPoint = recordPoint;
	}

	public static Pager getChangedPager(Integer recordPerPage,
			Integer recordPoint, Pager pager) {
		if (recordPerPage != null && recordPerPage > 0 && recordPoint != null
				&& recordPoint >= 0) {

			if (recordPerPage <= 100) {
				pager.setRecordPerPage(recordPerPage);
			} else {
				pager.setRecordPerPage(100);
			}

			pager.setCurrentPage(recordPoint / recordPerPage + 1);
			pager.setRecordPoint(recordPoint);

			return pager;
		} else if (recordPerPage != null && recordPerPage > 0
				&& recordPoint != null) {

			if (recordPerPage <= 100) {
				pager.setRecordPerPage(recordPerPage);
			} else {
				pager.setRecordPerPage(100);
			}

			pager.setRecordPerPage(recordPerPage);
			pager.setRecordPoint(pager.getOffset());
			return pager;
		} else if (recordPerPage != null && recordPerPage > 0
				&& recordPoint == null) {

			if (recordPerPage <= 100) {
				pager.setRecordPerPage(recordPerPage);
			} else {
				pager.setRecordPerPage(100);
			}

			pager.setRecordPerPage(recordPerPage);
			pager.setRecordPoint(pager.getOffset());
			return pager;
		} else {
			pager.setRecordPoint(pager.getOffset());
			return pager;
		}
	}

}
