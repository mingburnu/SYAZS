package com.shouyang.syazs.core.util;

import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 * EncryptorUtil
 * @author Roderick
 * @version 2014/3/12
 */
public class EncryptorUtil {

	public static StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
	
	public static String encrypt(String password) {
		return encryptor.encryptPassword(password);
	}
	
	public static Boolean checkPassword(String inputPassword, String encryptedPassword) {
		return encryptor.checkPassword(inputPassword, encryptedPassword);
	}
	
}
