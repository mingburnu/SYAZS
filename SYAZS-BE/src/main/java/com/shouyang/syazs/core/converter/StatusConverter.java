package com.shouyang.syazs.core.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.shouyang.syazs.core.apply.enums.Status;
import com.shouyang.syazs.core.converter.RootConverter;

/**
 * Status Converter
 * 
 * @author Roderick
 * @version 2014/3/17
 */
@Component
public class StatusConverter extends RootConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		boolean isLegalStatus = false;

		if (StringUtils.isNotBlank(values[0])) {
			List<Status> statusList = new ArrayList<Status>(
					Arrays.asList(Status.values()));

			Iterator<Status> iterator = statusList.iterator();

			while (iterator.hasNext()) {
				if (values[0].equals(iterator.next().getStatus())) {
					isLegalStatus = true;
				}
			}

		}

		if (isLegalStatus) {
			return Status.valueOf(values[0]);
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		if (o instanceof Status) {
			Status status = (Status) o;
			return status.getStatus();
		} else {
			return "";
		}
	}
}
