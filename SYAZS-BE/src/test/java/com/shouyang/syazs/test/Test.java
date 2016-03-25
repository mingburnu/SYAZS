package com.shouyang.syazs.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.xml.sax.helpers.DefaultHandler;

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

		System.out.println(ISBN_Validator.toIsbn10("9780140143959"));
		System.out.println(ISBN_Validator.toIsbn13("0140143955"));
		System.out.println("978-986-181-728-6".trim().replace("-", "")
				.substring(3, 12));
		
		
		File file = new File("C:\\Users\\user\\Desktop\\csv.csv");
		AutoDetectParser parser = new AutoDetectParser();
		parser.setParsers(new HashMap<MediaType, Parser>());

		Metadata metadata = new Metadata();
		metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, file.getName());

		InputStream stream = new FileInputStream(file);
		parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
		stream.close();

		System.out.println(metadata.get(HttpHeaders.CONTENT_TYPE));
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
