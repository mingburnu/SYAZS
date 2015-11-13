package com.shouyang.syazs.module.apply.ebook;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class ISBN_Validator {

	public static boolean isIsbn(Long isbnNum) {
		return isIsbn(isbnNum + "");
	}

	public static boolean isIsbn(String isbnString) {
		Pattern pattern1 = Pattern
				.compile("(97)([8-9])(\\-)(\\d{1,5})(\\-)(\\d{2,5})(\\-)(\\d{1,6})(\\-)(\\d)");
		Pattern pattern2 = Pattern
				.compile("(97)([8-9])(\\d)(\\d{2})(\\d{6})(\\d)");
		Pattern pattern3 = Pattern
				.compile("(97)([8-9])(\\-)(\\d)(\\d{2})(\\d{6})(\\d)");

		String isbn;
		if ((pattern1.matcher(isbnString).matches()
				|| pattern2.matcher(isbnString).matches() || pattern3.matcher(
				isbnString).matches())
				&& isbnString.replace("-", "").trim().length() == 13) {
			isbn = isbnString.replace("-", "").trim();
		} else {
			return false;
		}

		int sum = 0;
		for (int i = 0; i < 12; i++) {
			if (i % 2 == 0) {
				sum = sum + Integer.parseInt(isbn.substring(i, i + 1)) * 1;
			} else {
				sum = sum + Integer.parseInt(isbn.substring(i, i + 1)) * 3;
			}
		}

		int remainder = sum % 10;
		int num = 10 - remainder;

		if (num == 10) {
			if (Integer.parseInt(isbn.substring(12)) != 0) {
				return false;
			}
		} else {
			if (Integer.parseInt(isbn.substring(12)) != num) {
				return false;
			}
		}

		return true;
	}
}