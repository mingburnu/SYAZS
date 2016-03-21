package com.shouyang.syazs.module.apply.ebook;

import java.util.regex.Pattern;

import org.apache.commons.validator.routines.ISBNValidator;
import org.springframework.stereotype.Component;

@Component
public class ISBN_Validator {

	private static ISBNValidator isbnValidator = new ISBNValidator();

	public static boolean isIsbn13(Long isbn) {
		return isIsbn13(isbn + "");
	}

	public static boolean isIsbn13(String isbn) {
		Pattern amazonPattern = Pattern
				.compile("(97)([8-9])(\\-)(\\d)(\\d{2})(\\d{6})(\\d)");

		String code;
		if ((amazonPattern.matcher(isbn).matches())
				&& isbn.replace("-", "").trim().length() == 13) {
			code = isbn.replace("-", "").trim();
		} else {
			code = isbn;
		}

		return isbnValidator.isValidISBN13(code);
	}

	public static boolean isIsbn10(String isbn) {
		return isbnValidator.isValidISBN10(isbn.toUpperCase());
	}

	public static String toIsbn13(String isbn10) {
		return isbnValidator.convertToISBN13(isbn10.toUpperCase());
	}

	public static String toIsbn10(String isbn13) {
		if (isIsbn13(isbn13)) {
			String code = isbn13.trim().replace("-", "").substring(3, 12);
			int s = 0;

			int i = 10;
			while (i > 1) {
				s = s + Character.getNumericValue(code.charAt(10 - i)) * i;
				i--;
			}

			int m = s % 11;
			Integer n = 11 - m;

			switch (n) {
			case 10:
				code = code.concat("X");
				break;

			case 11:
				code = code.concat("0");
				break;

			default:
				code = code.concat(n.toString());
				break;
			}

			return code;
		}
		return null;
	}
}