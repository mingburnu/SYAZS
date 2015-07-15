package com.shouyang.syazs.module.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.shouyang.syazs.core.converter.RootConverter;
import com.shouyang.syazs.module.apply.enums.Type;

/**
 * Type Converter
 * 
 * @author Roderick
 * @version 2014/3/17
 */
@Component
public class TypeConverter extends RootConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		boolean isLegalType = false;

		if (StringUtils.isNotBlank(values[0])) {
			List<Type> typeList = new ArrayList<Type>(Arrays.asList(Type
					.values()));

			Iterator<Type> iterator = typeList.iterator();

			while (iterator.hasNext()) {
				if (values[0].equals(iterator.next().getType())) {
					isLegalType = true;
				}
			}

		}

		if (isLegalType) {
			return Type.valueOf(values[0]);
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		if (o instanceof Type) {
			Type type = (Type) o;
			return type.getType();
		} else {
			return "";
		}
	}
}
