package com.shouyang.syazs.module.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.shouyang.syazs.core.converter.RootConverter;
import com.shouyang.syazs.module.apply.enums.Category;

/**
 * Category Converter
 * 
 * @author Roderick
 * @version 2014/3/17
 */
@Component
public class CategoryConverter extends RootConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		boolean isLegalCategory = false;

		if (StringUtils.isNotBlank(values[0])) {
			List<Category> categoryList = new ArrayList<Category>(
					Arrays.asList(Category.values()));

			Iterator<Category> iterator = categoryList.iterator();

			while (iterator.hasNext()) {
				if (values[0].equals(iterator.next().getCategory())) {
					isLegalCategory = true;
				}
			}

		}

		if (isLegalCategory) {
			return Category.valueOf(values[0]);
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		if (o instanceof Category) {
			Category category = (Category) o;
			return category.getCategory();
		} else {
			return "";
		}
	}
}
