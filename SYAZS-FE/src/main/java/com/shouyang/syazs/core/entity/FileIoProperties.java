package com.shouyang.syazs.core.entity;

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
