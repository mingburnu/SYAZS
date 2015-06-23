package com.shouyang.syazs.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class OpenWorkBook {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException,FileNotFoundException {
		// TODO Auto-generated method stub
		File file = new File("Writesheet.xlsx");
		FileInputStream fIP = new FileInputStream(file);
		// Get the workbook instance for XLSX file
		XSSFWorkbook workbook = new XSSFWorkbook(fIP);
		if (file.isFile() && file.exists()) {
			System.out.println("openworkbook.xlsx file open successfully.");
			
		} else {
			System.out.println("Error to open openworkbook.xlsx file.");
		}
	}

}
