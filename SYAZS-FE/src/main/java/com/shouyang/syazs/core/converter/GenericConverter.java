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
public class GenericConverter extends RootConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		Object o = null;
		try {
			o = toClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info(o.toString());

		return o;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		return o.toString();
	}
}
