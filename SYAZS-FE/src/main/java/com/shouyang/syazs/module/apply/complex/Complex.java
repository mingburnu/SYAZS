package com.shouyang.syazs.module.apply.complex;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.shouyang.syazs.core.entity.GenericEntityModel;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Complex extends GenericEntityModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1647915342720534484L;

}
