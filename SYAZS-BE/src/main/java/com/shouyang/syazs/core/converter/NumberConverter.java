package com.shouyang.syazs.core.converter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@Component
public class NumberConverter extends TypeConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		Number number = null;

		if (NumberUtils.isNumber(values[0])) {
			try {
				number = NumberFormat.getInstance().parse(values[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return number;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		return o.toString();
	}

}
