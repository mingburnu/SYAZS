package com.shouyang.syazs.core.entity;

import java.io.InputStream;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class FileIoProperties implements Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 998220513379335266L;

	/** set struts action dispatcher location */
	@Transient
	private String location;
	
	@Transient
	private String dataStatus;
	
	@Transient
	private InputStream inputStream;

	public String getLocation() {
		StringBuilder locationBuilder = new StringBuilder("/WEB-INF/jsp/");

		String className = getClass().getCanonicalName();
		String packageName = className.substring(0, className.lastIndexOf("."));

		location = locationBuilder
				.append(packageName.substring(0, packageName.lastIndexOf(".")))
				.append("/").toString();
		return location;
	}

	/**
	 * @return the dataStatus
	 */
	public String getDataStatus() {
		return dataStatus;
	}

	/**
	 * @param dataStatus
	 *            the dataStatus to set
	 */
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream
	 *            the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
