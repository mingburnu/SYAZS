package com.shouyang.syazs.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Writesheet {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("example");
		// Create row object
		XSSFRow row;
		// This data needs to be written (Object[])
		Map<String, Object[]> empinfo = new TreeMap<String, Object[]>();
		empinfo.put("1", new Object[] { "chinesetitle/中文刊名",
				"englishtitle/英文刊名", "abbreviationtitle/英文縮寫刊名", "ISSN",
				"languages/語文", "publishname/出版社", "yearpublication/出版年",
				"publication/刊別", "congressclassification/國會分類號",
				"startdate/起始日", "maturitydate/到期日", "Rcategory/資源類型",
				"Rtype/資源種類", "Dbchttitle/資料庫中文題名", "Dbengtitle/資料庫英文題名",
				"name/購買單位名稱", "egName/購買單位英文名稱" });
		empinfo.put("2", new Object[] { "",
				"American Journal of Public Health", "", "1541-0048", "eng",
				"", "", "M", "", "", "", "", "期刊", "", "", "國防醫學中心", "NDMC" });
		// empinfo.put("3", new Object[] { "tp02", "Manisha", "Proof Reader" });
		// empinfo.put("4", new Object[] { "tp03", "Masthan", "Technical Writer"
		// });
		// empinfo.put("5", new Object[] { "tp04", "Satish", "Technical Writer"
		// });
		// empinfo.put("6", new Object[] { "tp05", "Krishna", "Technical Writer"
		// });
		// Iterate over data and write to sheet
		Set<String> keyid = empinfo.keySet();
		int rowid = 0;
		for (String key : keyid) {
			row = spreadsheet.createRow(rowid++);
			Object[] objectArr = empinfo.get(key);
			int cellid = 0;
			for (Object obj : objectArr) {
				Cell cell = row.createCell(cellid++);
				cell.setCellValue((String) obj);
			}
		}
		// Write the workbook in file system
		FileOutputStream out = new FileOutputStream(new File("Writesheet.xlsx"));
		workbook.write(out);
		out.close();
		System.out.println("Writesheet.xlsx written successfully");
	}

}
