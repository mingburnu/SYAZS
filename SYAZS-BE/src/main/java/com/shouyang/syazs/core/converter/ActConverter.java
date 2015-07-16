package com.shouyang.syazs.core.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.shouyang.syazs.core.apply.enums.Act;
import com.shouyang.syazs.core.converter.RootConverter;

/**
 * Act Converter
 * 
 * @author Roderick
 * @version 2014/3/17
 */
@Component
public class ActConverter extends RootConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		boolean isLegalAct = false;

		if (StringUtils.isNotBlank(values[0])) {
			List<Act> actList = new ArrayList<Act>(
					Arrays.asList(Act.values()));

			Iterator<Act> iterator = actList.iterator();

			while (iterator.hasNext()) {
				if (values[0].equals(iterator.next().getAct())) {
					isLegalAct = true;
				}
			}

		}

		if (isLegalAct) {
			return Act.valueOf(values[0]);
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		if (o instanceof Act) {
			Act act = (Act) o;
			return act.getAct();
		} else {
			return "";
		}
	}
}
