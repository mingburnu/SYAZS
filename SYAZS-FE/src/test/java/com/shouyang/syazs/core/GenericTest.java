package com.shouyang.syazs.core;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * GenericTest
 * @author Roderick
 * @version 2014/9/29
 */
@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
	"classpath:config/spring-context.xml", 
	"classpath:config/spring-service.xml", 
	"classpath:config/spring-database.xml"})
@TransactionConfiguration(defaultRollback = false, transactionManager = "transactionManager")
@Transactional(isolation = Isolation.READ_COMMITTED)
@ActiveProfiles("dev")
public abstract class GenericTest {

	protected final transient Logger log = Logger.getLogger(getClass());
	
	@Autowired
	private ApplicationContext context;

	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}
	
	protected Object getBean(String beanName) {
		return getContext().getBean(beanName);
	}

}
