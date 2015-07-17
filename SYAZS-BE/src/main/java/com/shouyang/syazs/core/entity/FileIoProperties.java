package com.shouyang.syazs.core.entity;

import java.io.File;
import java.io.InputStream;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class FileIoProperties extends CheckArray {

	/**
	 * 
	 */
	private static final long serialVersionUID = 998220513379335266L;

	@Transient
	private File[] file;

	@Transient
	private String[] fileFileName;

	@Transient
	private String[] fileContentType;

	@Transient
	private String existStatus;

	/** export file name */
	@Transient
	private String reportFile;
	
	@Transient
	private InputStream inputStream; 

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public String[] getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String[] getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String[] fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getExistStatus() {
		return existStatus;
	}

	public void setExistStatus(String existStatus) {
		this.existStatus = existStatus;
	}

	public String getReportFile() {
		return reportFile;
	}

	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/** set struts action dispatcher location */
	@Transient
	private String location;

	public String getLocation() {
		StringBuilder locationBuilder = new StringBuilder("/WEB-INF/jsp/");

		String className = getClass().getCanonicalName();
		String packageName = className.substring(0, className.lastIndexOf("."));

		location = locationBuilder
				.append(packageName.substring(0, packageName.lastIndexOf(".")))
				.append("/").toString();
		return location;
	}
}
