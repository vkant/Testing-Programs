package com.vishaal.learn.poi;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.ArrayListMultimap;

public class ReadExcelForDuplicates {
	
	
	
	
	public static void main(String[] args) throws Exception {
		String excelFilePath = "/Users/vishaalkant/git/Testing-Programs/src/resources/dups.xlsx";
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheet("SQL Results");

		Iterator<Row> iterator = firstSheet.iterator();
		ArrayListMultimap myMap = ArrayListMultimap.create();
		List<DataObj> rows = new ArrayList<>();
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();	
			
			Cell ID = nextRow.getCell(1);
			Cell name = nextRow.getCell(3);
			Cell clob = nextRow.getCell(4);
			
			try {
				int IDIntVal = Double.valueOf(ID.toString()).intValue();
				System.out.println(name);
			  myMap.put(name, new DataObj(String.valueOf(IDIntVal), name.getStringCellValue(), clob.getStringCellValue()));
			} catch (Exception e) {
				System.err.println("Error: " + ID.toString());
			}
			
			
			
			
		}
		
		workbook.close();
		inputStream.close();
		
		System.out.println(myMap);
		
		
	}
}

class DataObj {
	
	private String Id;
	private String name;
	private String clob;
	
	public DataObj(String Id, String name, String clob) {
		this.Id = Id;
		this.name = name;
		this.clob = clob;
	}

	public String getId() {
		return Id;
	}

	public String getName() {
		return name;
	}

	public String getClob() {
		return clob;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataObj other = (DataObj) obj;
		if (Id == null) {
			if (other.Id != null)
				return false;
		} else if (!Id.equals(other.Id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Id;
	}
	
	
}


