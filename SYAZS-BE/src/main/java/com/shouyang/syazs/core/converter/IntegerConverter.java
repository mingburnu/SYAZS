package com.shouyang.syazs.core.converter;

import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@Component
public class IntegerConverter extends RootConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		Integer number = null;

		try {
			if (NumberUtils.isNumber(values[0])) {
				number = Integer.parseInt(values[0]);
			}
		} catch (NumberFormatException e) {
			return null;
		}

		return number;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {

		if (o != null) {
			return o.toString();
		} else {
			return "";
		}
	}

}
