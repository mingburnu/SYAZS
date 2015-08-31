package com.shouyang.syazs.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.catalina.util.MD5Encoder;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encryptor;
import org.owasp.esapi.crypto.PlainText;
import org.owasp.esapi.reference.crypto.JavaEncryptor;

public class Test {
	public static void main(String[] args) throws Exception {

		Long[] arr = new Long[4];
		arr[0] = null;
		arr[1] = 8L;
		arr[2] = null;
		arr[3] = 9L;

		Set<Long> set = new HashSet<Long>(Arrays.asList(arr));
		System.out.println(set.contains(null));

		for (int i = 0; i < 4; i++) {

			if (i == 1) {
				continue;
			} else {
				System.out.println(i);
			}

		}

		StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
		System.out.println(encryptor.encryptPassword("1").hashCode());

		String s = UUID.randomUUID().toString();
		UUID.fromString("37383107-1341-3246-87f0-c71dbbfc76b6");
		System.out.println(s);
		System.out.println(UUID.nameUUIDFromBytes(new byte[]{Byte.MAX_VALUE,Byte.MIN_VALUE,Byte.MAX_VALUE+Byte.MIN_VALUE}));
	}

	public static boolean isLCC(String LCC) {
		String LCCPattern = "([A-Z]{1,3})((\\d+)(\\.?)(\\d+))";

		Pattern pattern = Pattern.compile(LCCPattern);

		return pattern.matcher(LCC).matches();
	}

	public static boolean isNum(String num) {
		// String numPattern = "(\\d+)(\\.?)(\\d+)";

		String numPattern = "\\d+(\\.\\d+)";
		// String numPattern = "^[1-9]\\d*(\\.\\d+)?$";
		Pattern pattern = Pattern.compile(numPattern);

		return pattern.matcher(num).matches();
	}

	public static void dotString(String... strs) {
		System.out.println(strs == null);
	}

	public static boolean isDate(String date) {
		Pattern pattern = Pattern
				.compile("((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])");
		Matcher matcher = pattern.matcher(date);
		return matcher.matches();
	}
}
