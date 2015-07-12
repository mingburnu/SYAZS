package com.shouyang.syazs.core.converter;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
public class JodaTimeConverter extends TypeConverter {

	@Value("#{systemConfigurer['y4MinusM2Minusd2']}")
	private String y4MinusM2Minusd2;

	@Value("#{systemConfigurer['y4DivisionM2Divisiond2']}")
	private String y4DivisionM2Divisiond2;

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		LocalDateTime dateTime = null;

		if (StringUtils.isNotBlank(values[0])) {
			try {
				log.debug("input date: " + values[0]);
				dateTime = LocalDateTime.parse(values[0].trim(),
						DateTimeFormat.forPattern(y4MinusM2Minusd2));
			} catch (IllegalArgumentException e) {
				log.debug("IllegalArgumentException for this pattern, change use another datePattern");
				try {
					dateTime = LocalDateTime.parse(values[0].trim(),
							DateTimeFormat.forPattern(y4DivisionM2Divisiond2));
				} catch (Exception e2) {
					log.error(ExceptionUtils.getStackTrace(e));
				}
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
			}
		}

		return dateTime;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {

		if (o instanceof LocalDateTime) {
			LocalDateTime dateTime = (LocalDateTime) o;
			String formattedTime = dateTime.toString(DateTimeFormat
					.forPattern(y4MinusM2Minusd2));
			log.info(formattedTime);
			return formattedTime;
		} else {
			return "";
		}
	}
}
