package com.shouyang.syazs.core;

import java.io.FileNotFoundException;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

/**
 * load the log4j.properties for JUnit
 * 
 * @author Roderick
 * @version 2014/9/29
 */
public class JUnit4ClassRunner extends SpringJUnit4ClassRunner {

	public JUnit4ClassRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	static {
		try {
			Log4jConfigurer.initLogging("classpath:config/log4j.properties");
		} catch (FileNotFoundException ex) {
			System.err.println("Cannot Initialize log4j");
		}
	}

}
