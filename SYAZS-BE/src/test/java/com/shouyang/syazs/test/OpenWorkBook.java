package com.shouyang.syazs.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;

public class OpenWorkBook {

	public static void main(String[] args) throws IOException,FileNotFoundException {
		// TODO Auto-generated method stub
		File file = new File("/journal2.xls");
		FileInputStream fIP = new FileInputStream(file);
		// Get the workbook instance for XLSX file
		HSSFWorkbook workbook = new HSSFWorkbook(fIP);
		Sheet sheet = workbook.getSheetAt(0);
		List<Object> list =new ArrayList<Object>();
		for(int i=0;i<=10000000;i++){
			list.add(sheet.getRow(i));
		}
		
		System.out.print(list.size());
		System.out.print(list.get(1000000));
	}

}
