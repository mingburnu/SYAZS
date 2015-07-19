package com.shouyang.syazs.core.converter;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class EnumConverter extends RootConverter {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		try {
			log.info(Enum.valueOf(toClass, values[0]));
			return Enum.valueOf(toClass, values[0]);
		} catch (java.lang.IllegalArgumentException e) {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		if (o instanceof Enum) {
			Enum e = (Enum) o;
			return e.name();
		} else {
			return "";
		}
	}

}
