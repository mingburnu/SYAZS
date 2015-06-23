package com.shouyang.syazs.core.converter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Joda Time Converter
 * 
 * @author Roderick
 * @version 2014/3/17
 */
@Component
public class JodaTimeConverter {

	protected final transient Logger log = Logger.getLogger(getClass());

	@Value("#{systemConfigurer['dateTime.pattern']}")
	private String pattern;

	public LocalDateTime convertFromString(String date) {
		LocalDateTime dateTime = null;
		if (StringUtils.isNotBlank(date)) {
			try {
				dateTime = LocalDateTime.parse(date,
						DateTimeFormat.forPattern(pattern));
			} catch (Exception e) {
				return null;
			}
		}
		return dateTime;
	}

	public String convertToString(LocalDateTime dateTime) {
		String formattedTime = dateTime.toString(DateTimeFormat
				.forPattern(pattern));
		return formattedTime;

	}

}
