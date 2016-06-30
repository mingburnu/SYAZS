package com.shouyang.syazs.module.apply.complex;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.shouyang.syazs.core.entity.GenericEntityModel;
import com.shouyang.syazs.module.entity.ModuleProperties;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Complex extends GenericEntityModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1647915342720534484L;

	private List<ModuleProperties> results = Lists.newArrayList();

	public List<ModuleProperties> getResults() {
		return results;
	}
}
