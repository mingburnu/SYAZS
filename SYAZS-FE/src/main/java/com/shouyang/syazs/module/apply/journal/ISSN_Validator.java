package com.shouyang.syazs.module.apply.journal;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class ISSN_Validator {

	protected static boolean isIssn(String issn) {
		Pattern pattern = Pattern.compile("(\\d{4})(\\-?)(\\d{3})[\\dX]");
		issn = issn.trim().toUpperCase();

		if (pattern.matcher(issn.toUpperCase()).matches()) {
			issn = issn.replace("-", "");
			int sum = 0;
			for (int i = 0; i < 7; i++) {
				sum = sum + Integer.parseInt(issn.substring(i, i + 1))
						* (8 - i);
			}

			int remainder = sum % 11;

			if (remainder == 0) {
				if (!issn.substring(7).equals("0")) {
					return false;
				}
			} else {
				if (11 - remainder == 10) {
					if (!issn.substring(7).equals("X")) {
						return false;
					}
				} else {
					if (issn.substring(7).equals("X")
							|| Integer.parseInt(issn.substring(7)) != 11 - remainder) {
						return false;
					}
				}
			}

		} else {
			return false;
		}
		return true;
	}
}
