package com.shouyang.syazs.core.util;


import org.junit.Assert;
import org.junit.Test;

import com.shouyang.syazs.core.util.EncryptorUtil;

public class EncryptorUtilTest {

	@Test
	public void testCheckPassword() {
		final String inputPassword = "admin";
		final String encryptedPassword = EncryptorUtil.encrypt(inputPassword);
		
		System.out.println("encryptedPassword:" + encryptedPassword);
		
		Assert.assertTrue(EncryptorUtil.checkPassword(inputPassword, encryptedPassword));
	}

}
