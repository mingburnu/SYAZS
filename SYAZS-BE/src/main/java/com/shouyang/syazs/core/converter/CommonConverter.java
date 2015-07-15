package com.shouyang.syazs.core.converter;

import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Common Converter
 * 
 * @author Roderick
 * @version 2015/7/7
 */
@Component
public class CommonConverter extends RootConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		return null;
	}
}
