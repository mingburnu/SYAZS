package com.shouyang.syazs.core.model;

import java.io.Serializable;

import org.apache.commons.lang3.math.NumberUtils;
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
		return offset;
	}

	public void setOffset(Integer offset) {
		if (offset >= 0) {
			this.offset = offset;
		}
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

	public static Pager getChangedPager(String recordPerPage,
			String recordPoint, Pager pager) {
		if (recordPerPage != null && NumberUtils.isDigits(recordPerPage)
				&& Integer.parseInt(recordPerPage) > 0 && recordPoint != null
				&& NumberUtils.isDigits(recordPoint)
				&& Integer.parseInt(recordPoint) >= 0) {

			if (Integer.parseInt(recordPerPage) <= 50) {
				pager.setRecordPerPage(Integer.parseInt(recordPerPage));
			} else {
				pager.setRecordPerPage(50);
			}

			pager.setCurrentPage(Integer.parseInt(recordPoint)
					/ Integer.parseInt(recordPerPage) + 1);
			pager.setOffset(Integer.parseInt(recordPerPage)
					* (pager.getCurrentPage() - 1));
			pager.setRecordPoint(Integer.parseInt(recordPoint));

			return pager;
		} else if (recordPerPage != null && NumberUtils.isDigits(recordPerPage)
				&& Integer.parseInt(recordPerPage) > 0 && recordPoint != null
				&& !NumberUtils.isDigits(recordPoint)) {

			if (Integer.parseInt(recordPerPage) <= 50) {
				pager.setRecordPerPage(Integer.parseInt(recordPerPage));
			} else {
				pager.setRecordPerPage(50);
			}

			pager.setRecordPerPage(Integer.parseInt(recordPerPage));
			pager.setRecordPoint(pager.getOffset());
			return pager;
		} else if (recordPerPage != null && NumberUtils.isDigits(recordPerPage)
				&& Integer.parseInt(recordPerPage) > 0 && recordPoint == null) {

			if (Integer.parseInt(recordPerPage) <= 50) {
				pager.setRecordPerPage(Integer.parseInt(recordPerPage));
			} else {
				pager.setRecordPerPage(50);
			}

			pager.setRecordPerPage(Integer.parseInt(recordPerPage));
			pager.setRecordPoint(pager.getOffset());
			return pager;
		} else {
			pager.setRecordPoint(pager.getOffset());
			return pager;
		}
	}

}
