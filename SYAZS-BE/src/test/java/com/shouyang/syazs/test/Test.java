package com.shouyang.syazs.test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jasypt.util.password.StrongPasswordEncryptor;

import com.shouyang.syazs.module.apply.ebook.ISBN_Validator;

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
		System.out.println(s);

		while (!check(s)) {
			s = UUID.randomUUID().toString();
			System.out.println(s);
		}
		System.out.println(s);

		String listHql = "SELECT B.accountNumber, B.customer, B.customer.serNo, B.actionType, count(B.accountNumber) FROM BeLogs B WHERE B.cDTime >'";

		System.out.println(listHql.toLowerCase().indexOf("from "));
		int index = listHql.toLowerCase().indexOf("from ");
		System.out.println(listHql.substring(index, listHql.length()));
		System.out.println(Modifier.toString(9).contains("public"));

		// Pattern pattern4 = Pattern
		// .compile("(97)([8-9])(\\-)(\\d{1,5})(\\-)(\\d{2,5})(\\-)(\\d{1,6})(\\-)(\\d)");
		// System.out.println(pattern4.matcher("978-0-08-044978-4").matches());

		Pattern pattern = Pattern
				.compile("(97)([8-9])(\\-)(\\d{1,5})(\\-)(\\d{2,5})(\\-)(\\d{1,6})(\\-)(\\d)");
		Matcher isbnMatcher = pattern.matcher("978-986-181-728-6");
		while (isbnMatcher.find()) {
			System.out.println("ISBN Number is valid and number is : "
					+ isbnMatcher.group());
		}

		System.out.println(ISBN_Validator.toIsbn10("978-0552162401"));
		Long ss = null;
		System.out.println("978-986-181-728-6".trim().replace("-", "")
				.substring(3, 12));
	}

	private static boolean check(String s) {
		return s.startsWith("e");
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
