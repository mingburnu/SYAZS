package com.shouyang.syazs.core.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.shouyang.syazs.core.apply.enums.Role;
import com.shouyang.syazs.core.converter.RootConverter;

/**
 * Role Converter
 * 
 * @author Roderick
 * @version 2014/3/17
 */
@Component
public class RoleConverter extends RootConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		boolean isLegalRole = false;

		if (StringUtils.isNotBlank(values[0])) {
			List<Role> roleList = new ArrayList<Role>(
					Arrays.asList(Role.values()));

			Iterator<Role> iterator = roleList.iterator();

			while (iterator.hasNext()) {
				if (values[0].equals(iterator.next().getRole())) {
					isLegalRole = true;
				}
			}

		}

		if (isLegalRole) {
			return Role.valueOf(values[0]);
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		if (o instanceof Role) {
			Role role = (Role) o;
			return role.getRole();
		} else {
			return "";
		}
	}
}
