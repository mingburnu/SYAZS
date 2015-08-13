package com.shouyang.syazs.test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) throws IOException, ParseException {

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

		String isbn = "978-0-12-374994-9";
		String regexCodeDash = "(97)([8-9])(\\-)(\\d)(\\-)(\\d{2})(\\-)(\\d{6})(\\-)(\\d)";
		// (\\-?)(\\d{3})[\\dX]
		Pattern patternCodeDash = Pattern.compile(regexCodeDash);
		Matcher matcherCodeDash = patternCodeDash.matcher(isbn);

		System.out.println(matcherCodeDash.matches());
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
