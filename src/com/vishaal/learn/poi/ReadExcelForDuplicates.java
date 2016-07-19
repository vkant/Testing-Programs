package com.vishaal.learn.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.ArrayListMultimap;

/**
 * @author vkant
 *
 */
public class ReadExcelForDuplicates {
	
  private MultiValueMap excelMap;
  private String post2010Pattern = "<void property=\"status\"";
  
  public interface DUPSTYPE {
    public String CLOB_STRING_EQUAL = "CLOB_STRING_EQUAL";
    public String CLOB_PRE_2010 = "CLOB_PRE_2010";
    public String CLOB_JAVA_VERSION = "CLOB_JAVA_VERSION";
  }
  
  public ReadExcelForDuplicates(MultiValueMap excelMap) {
    this.excelMap = excelMap;
    findDupsToBeRemoved();
  }
  
	private void findDupsToBeRemoved() {
	  Set<String> keys = excelMap.keySet();
	  
	  MultiValueMap destinationsToBeRemoved = MultiValueMap.decorate(new LinkedHashMap(), TreeSet.class);
	  List<DataObj> toBeHandeledManually = new ArrayList<DataObj>();
	  
	  
	  for (String key : keys) {
	    List<DataObj> dupsList =  (List) excelMap.getCollection(key);
	    DataObj row1 = dupsList.get(0);
	    DataObj row2 = dupsList.get(1);
	    
	    String row1Clob = row1.getClob();
	    String row2Clob = row2.getClob();
	    
	    if (row1Clob.equalsIgnoreCase(row2Clob)) { // check for clob equality
	      destinationsToBeRemoved.put(DUPSTYPE.CLOB_STRING_EQUAL, row2);
	      continue;
	    }
	    
	    if (row2Clob.contains(post2010Pattern) && (!row1Clob.contains(post2010Pattern))) {
	      destinationsToBeRemoved.put(DUPSTYPE.CLOB_PRE_2010, row1);
	      continue;
	    }
	    
	    if (row1Clob.contains(post2010Pattern) && !row2Clob.contains(post2010Pattern)) {
	      destinationsToBeRemoved.put(DUPSTYPE.CLOB_PRE_2010, row2);
	      continue;
	    }
	    
	    if ( (row2Clob.contains(post2010Pattern) && row1Clob.contains(post2010Pattern)) || 
	        (!row2Clob.contains(post2010Pattern) && !row1Clob.contains(post2010Pattern)) ) {
	      String row1JavaVersion = getJavVersion(row1Clob);
	      String row2JavaVersion = getJavVersion(row2Clob);
	      
	      int retVal = row1JavaVersion.compareTo(row2JavaVersion); 
	      
	      if (retVal == 0) { // if java version matches how on earth I can diff now -> write to file
	        toBeHandeledManually.add(row1);
          toBeHandeledManually.add(row2);
	      }
	      else if (retVal > 0) {
	        destinationsToBeRemoved.put(DUPSTYPE.CLOB_JAVA_VERSION, row2);
	      }
	      else {
	        destinationsToBeRemoved.put(DUPSTYPE.CLOB_JAVA_VERSION, row1);
	      }
	    }
	  }
	  
	  System.out.println("Excel Size [without duplicates] : " + excelMap.size());
	  System.out.println("CLOB_STRING_EQUAL : size : " + destinationsToBeRemoved.getCollection(DUPSTYPE.CLOB_STRING_EQUAL).size());
	  System.out.println("CLOB_STRING_EQUAL :  " + destinationsToBeRemoved.getCollection(DUPSTYPE.CLOB_STRING_EQUAL));
	  System.out.println("******");
	  System.out.println("CLOB_PRE_2010 to be removed size:  " + destinationsToBeRemoved.getCollection(DUPSTYPE.CLOB_PRE_2010).size());
	  System.out.println("CLOB_PRE_2010 to be removed:  " + destinationsToBeRemoved.getCollection(DUPSTYPE.CLOB_PRE_2010));
	  System.out.println("******");
	  System.out.println("Old Java Version to be removed : size: " + destinationsToBeRemoved.getCollection(DUPSTYPE.CLOB_JAVA_VERSION).size());
	  System.out.println("Old Java Version to be removed : " + destinationsToBeRemoved.getCollection(DUPSTYPE.CLOB_JAVA_VERSION));
	  
	  System.out.println("toBeHandeledManually : Size : " + toBeHandeledManually.size() + "List : " + toBeHandeledManually);
	  
	  System.out.println(toBeHandeledManually);
	  
	   writeJavaVersionMismatchToFile(destinationsToBeRemoved);
	  
	  
  }
	
	private void writeJavaVersionMismatchToFile(MultiValueMap destinationsToBeRemoved) {
	  TreeSet<DataObj> col = (TreeSet) destinationsToBeRemoved.getCollection(DUPSTYPE.CLOB_JAVA_VERSION);
	  System.out.println("Set Size : " + col.size());
	  
	  for (DataObj obj : col) {
	    List rows = (List) excelMap.get(obj.getName());
	    DataObj row1 = (DataObj) rows.get(0);
	    DataObj row2 = (DataObj) rows.get(1);
	    
	    System.out.println("row1 : " + row1.getName() + row1.getId());
	    System.out.println("row2 : " + row2.getName() + row2.getId());
	    
	    try {
        FileUtils.writeStringToFile(new File(row1.getName()+row1.getId()+".xml"), row1.getClob());
        FileUtils.writeStringToFile(new File(row2.getName()+row2.getId()+".xml"), row2.getClob());
      }
      catch (IOException e) {
        e.printStackTrace();
      }
	  }
    
  }

  private String getJavVersion(String clobString) {
	  int index = clobString.indexOf("java");
	  return clobString.substring(index +14, index + 22);
	}
	
  public static void main(String[] args) throws Exception {
		String excelFilePath = "/Users/vkant/git/Testing-Programs/src/resources/dups.xlsx";
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheet("SQL Results");

		Iterator<Row> iterator = firstSheet.iterator();
		ArrayListMultimap myMap = ArrayListMultimap.create();
		
		MultiValueMap multValueMap = MultiValueMap.decorate(new LinkedHashMap(), ArrayList.class);
		
		List<DataObj> rows = new ArrayList<>();
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();	
			
			Cell ID = nextRow.getCell(1);
			Cell name = nextRow.getCell(3);
			Cell clob = nextRow.getCell(4);
			try {
				int IDIntVal = Double.valueOf(ID.toString()).intValue();
				String destName = name.getStringCellValue();
				DataObj dataObj = new DataObj(String.valueOf(IDIntVal), destName, clob.getStringCellValue());
				//System.out.println(dataObj);
				multValueMap.put(destName, dataObj);
			} catch (Exception e) {
				System.err.println("Error: " + ID.toString());
			}
		}
		
		workbook.close();
		inputStream.close();
		
		//System.out.println(multValueMap);
		//System.out.println(multValueMap.size());
		
		new ReadExcelForDuplicates(multValueMap);
	}
}

class DataObj implements Comparable {
	
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

  @Override
  public int compareTo(Object o) {
    
    DataObj thisObject = (DataObj) o;
    return this.Id.compareTo(thisObject.getId());
  }
}


