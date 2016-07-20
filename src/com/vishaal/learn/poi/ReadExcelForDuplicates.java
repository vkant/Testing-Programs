package com.vishaal.learn.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author vkant
 *
 */
public class ReadExcelForDuplicates {
  
  private MultiValueMap excelMap;
  private static final String post2010Pattern = "<void property=\"status\"";
  private static final String EXCEL_WORKSHEET_NAME = "SQL Results"; 
  //private static final String EXCEL_FILE = "/Users/vkant/git/Testing-Programs/src/resources/dups.xlsx";
  private static final String UAT_EXCEL = "/Users/vkant/git/Testing-Programs/src/resources/uat_dup_destinations.xlsx";
  private static final String PROD_EXCEL = "/Users/vkant/git/Testing-Programs/src/resources/dups.xlsx";
  
  
  public static final String JAVA_VERSION_REGEX = "java" + "\\s" + "version" + "=" + "\"" + "\\d\\.\\d\\.\\d\\w\\d{2}" + "\"";
  public static final Pattern pattern = Pattern.compile(JAVA_VERSION_REGEX);
  
  public interface DUPSTYPE {
    public String CLOB_STRING_EQUAL = "CLOB_STRING_EQUAL";
    public String CLOB_PRE_2010 = "CLOB_PRE_2010";
    public String CLOB_JAVA_VERSION = "CLOB_JAVA_VERSION";
  }
  
  public ReadExcelForDuplicates(MultiValueMap excelMap) {
    this.excelMap = excelMap;
    handleDuplicates();
  }

  private void handleDuplicates() {
    if (isProd()) {
      findDupsToBeRemoved1();
    }
    else {
      findDupsToBeRemoved();
    }
  }
  
  private void findDupsToBeRemoved() {
    Set<String> keys = excelMap.keySet();

    MultiValueMap destinationsToBeRemoved = MultiValueMap.decorate(
            new LinkedHashMap(), TreeSet.class);
    List<DataObj> toBeHandeledManually = new ArrayList<DataObj>();
    List<DataObj> javaListOne = new ArrayList<DataObj>();
    List<DataObj> javaListTwo = new ArrayList<DataObj>();

    for(String key : keys) {
      javaListOne.clear();
      javaListTwo.clear();
      Set<DataObj> dupSet = (Set) excelMap.getCollection(key);
      List<DataObj> dupList = new ArrayList<DataObj>(dupSet);
      String javaVersionOfFirst = dupList.get(0).getJavaVersion();
      javaListOne.add(dupList.get(0));
      for(int i = 1; i < dupList.size(); i++) {
        if(javaVersionOfFirst.compareTo(dupList.get(i).getJavaVersion()) == 0) {
          javaListOne.add(dupList.get(i));
        }
        else {
          javaListTwo.add(dupList.get(i));
        }
      }
      
      if(javaListTwo.size() == 0) {
        for (DataObj obj : javaListOne) {
          toBeHandeledManually.add(obj);
        }
        continue;
      }
      
      if(javaListOne.size() == 0) {
        for (DataObj obj : javaListTwo) {
          toBeHandeledManually.add(obj);
        }
        continue;
      }
      
      boolean isListOneProper = false;
      if (javaVersionOfFirst.substring(14, 17).equals("1.7")) {
        isListOneProper = true;
        for (DataObj obj : javaListTwo) {
          destinationsToBeRemoved.put(DUPSTYPE.CLOB_JAVA_VERSION, obj);
        }
      } else {
        for (DataObj obj : javaListOne) {
          destinationsToBeRemoved.put(DUPSTYPE.CLOB_JAVA_VERSION, obj);
        }
      }

      if(isListOneProper && javaListOne.size() > 1) {
        for (DataObj obj : javaListOne) {
          toBeHandeledManually.add(obj);
        }
      }
      else if(!isListOneProper && javaListTwo.size() >1){
        for (DataObj obj : javaListTwo) {
          toBeHandeledManually.add(obj);
        }
      }
    }
    
   System.out.println("Destination to be Removed :");
   System.out.println("**********************************************************************************************************************");
   System.out.println(destinationsToBeRemoved);
   System.out.println("Details of the destinations to be Removed -> ");
   printMultiValueMap(destinationsToBeRemoved,DUPSTYPE.CLOB_JAVA_VERSION);
   System.out.println("**********************************************************************************************************************");
   System.out.println("Destinations to be handled manually are : ");
   System.out.println(toBeHandeledManually); 
   Set<String> testDups = new HashSet<String>();
   
   for(DataObj obj : toBeHandeledManually) {
     System.out.println(obj.getDisplayString());
     
     testDups.add(obj.getClob());
   }

   System.out.println("**********************************************************************************************************************");
   printConfigsToRetain(excelMap, destinationsToBeRemoved);
   
  } 
  
  private void printMultiValueMap(MultiValueMap multiValueMap,String decide) {
    Set obj = (Set) multiValueMap.getCollection(decide);
    Iterator iter = obj.iterator();
    while(iter.hasNext()) {
      DataObj dataObj = (DataObj) iter.next();
      System.out.println(dataObj.getDisplayString());
    }
  }

  
  private void findDupsToBeRemoved1() {
    
    System.out.println("Excel : " + excelMap);
    
    Set<String> keys = excelMap.keySet();
    
    MultiValueMap destinationsToBeRemoved = MultiValueMap.decorate(new LinkedHashMap(), TreeSet.class);
    List<DataObj> toBeHandeledManually = new ArrayList<DataObj>();
    
    
    for (String key : keys) {
      Set<DataObj> dupsSet = (Set) excelMap.getCollection(key);
      List<DataObj> dupsList =  new ArrayList(dupsSet);
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
        String row1JavaVersion = row1.getJavaVersion();
        String row2JavaVersion = row2.getJavaVersion();
        
        if (row1JavaVersion == null || row2JavaVersion == null) {
          throw new RuntimeException("Failed retrieiving the java version !!");
        }
        
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
    System.out.println("CLOB_STRING_EQUAL to be removed : size : " + destinationsToBeRemoved.getCollection(DUPSTYPE.CLOB_STRING_EQUAL).size());
    System.out.println("CLOB_STRING_EQUAL to be removed :  ID : " + destinationsToBeRemoved.getCollection(DUPSTYPE.CLOB_STRING_EQUAL));
    System.out.println("Details of Destinations to be removed -> ");
    printMultiValueMap(destinationsToBeRemoved, DUPSTYPE.CLOB_STRING_EQUAL);
    System.out.println("**********************************************************************************************************************");
    System.out.println("CLOB_PRE_2010 to be removed size:  " + destinationsToBeRemoved.getCollection(DUPSTYPE.CLOB_PRE_2010).size());
    System.out.println("CLOB_PRE_2010 to be removed:  " + destinationsToBeRemoved.getCollection(DUPSTYPE.CLOB_PRE_2010));
    System.out.println("Details of Destinations to be removed -> ");
    printMultiValueMap(destinationsToBeRemoved, DUPSTYPE.CLOB_PRE_2010);
    System.out.println("**********************************************************************************************************************");
    System.out.println("Old Java Version to be removed : size: " + destinationsToBeRemoved.getCollection(DUPSTYPE.CLOB_JAVA_VERSION).size());
    System.out.println("Old Java Version to be removed : " + destinationsToBeRemoved.getCollection(DUPSTYPE.CLOB_JAVA_VERSION));
    System.out.println("Details of Destinations to be removed -> ");
    printMultiValueMap(destinationsToBeRemoved,DUPSTYPE.CLOB_JAVA_VERSION);
    System.out.println("**********************************************************************************************************************");
    System.out.println("toBeHandeledManually : Size : "
            + toBeHandeledManually.size() + "List : " + toBeHandeledManually);
    System.out.println("Details of Destinations to be handled Manually : ");
    System.out.println(toBeHandeledManually);
    System.out.println("**********************************************************************************************************************");
    printConfigsToRetain(excelMap, destinationsToBeRemoved);
  }
  
  private void printConfigsToRetain(MultiValueMap excel, MultiValueMap destinationToBeRemoved) {
    Set<DataObj> allValuesOfExcel = new LinkedHashSet<DataObj>(excel.values());
    Set<DataObj> valuesToBeRemoved = new LinkedHashSet<DataObj>(destinationToBeRemoved.values());
    
    Set<DataObj> finalCollection = new LinkedHashSet<DataObj>(CollectionUtils.disjunction(allValuesOfExcel, valuesToBeRemoved));
    System.out.println("Size of values to be retained : " + finalCollection.size());
    System.out.println("Details of the destinations to be Retained -> ");
    for(DataObj obj : finalCollection) {
      System.out.println(obj.getDisplayString());
    }
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

  
  /*private String getJavVersion(String clobString) {
    int index = clobString.indexOf("java");
    return clobString.substring(index +14, index + 22);
  }*/
  
  private static boolean isProd() {
    return Boolean.valueOf(System.getProperty("PROD"));
  }
  
  private static String getExcelFile() {
    
    String retVal = isProd() ? PROD_EXCEL : UAT_EXCEL; 
    
    System.out.println("Reading File : " + retVal);
    
    return retVal;
  }
  
  public static void main(String[] args) throws Exception {
    FileInputStream inputStream = new FileInputStream(new File(getExcelFile()));

    Workbook workbook = new XSSFWorkbook(inputStream);
    Sheet firstSheet = workbook.getSheet(EXCEL_WORKSHEET_NAME);

    Iterator<Row> iterator = firstSheet.iterator();
    
    MultiValueMap multValueMap = MultiValueMap.decorate(new LinkedHashMap(), TreeSet.class);
    
    List<DataObj> rows = new ArrayList<>();
    while (iterator.hasNext()) {
      Row nextRow = iterator.next();  
      
      Cell ID = nextRow.getCell(isProd() ? 1 : 0);
      Cell name = nextRow.getCell(isProd() ? 3 : 2);
      Cell clob = nextRow.getCell(isProd() ? 4 : 3);
      try {
        int IDIntVal = Double.valueOf(ID.toString()).intValue();
        String destName = name.getStringCellValue();
        DataObj dataObj = new DataObj(String.valueOf(IDIntVal), destName, clob.getStringCellValue());
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
    return Id ;
  }
  
  public String getDisplayString() {
    return "Destination Name : " + getName() + " -  " + getId() + " | Sender CompId : " + getSenderCompID() + " | Target Comp : "
            + getTargetCompID() + " | Translator Class : " + getTranslatorClass() + " | Java version : " + getJavaVersion();
  }

  
  
  public String getTargetCompID() {
    String senderTargetIDPattern = "Target" + "\\s" + "Comp" + "\\s" + "Id" + ".*" + "\\n" + ".*" + "\\n" 
            + "(.*\\n){8}" ; 
    return utilTogetIDS(clob, senderTargetIDPattern);
  }

  public String getTranslatorClass() {
    String translatorClassPattern = "Translator" + "\\s" + "Classes" + ".*" + "\\n" + ".*" + "\\n" 
            + "(.*\\n){10}" ; 
    return utilTogetIDS(clob, translatorClassPattern);
  }

  public String getSenderCompID() {
    String senderCompIDPattern = "Sender" + "\\s" + "Comp" + "\\s" + "Id" + ".*" + "\\n" + ".*" + "\\n" 
            + "(.*\\n){8}" ; 
    return utilTogetIDS(clob, senderCompIDPattern);
  }
  
  public String getJavaVersion() {
    Matcher matcher = ReadExcelForDuplicates.pattern.matcher(clob);
    
    if(matcher.find()){
      String result = matcher.group(0);
      return result;
    } 
    
    return null;
 }

  public int compareTo(Object o) {
    
    DataObj thisObject = (DataObj) o;
    return this.Id.compareTo(thisObject.getId());
  }
  
  private String utilTogetIDS(String clobString, String pattern) {
    String senderCompInsidePatter = "string" + ".*" + "string";
    Pattern pattern1 = Pattern.compile(pattern);
    Matcher matcher = pattern1.matcher(clobString);
    
    if(matcher.find()){
      String result = matcher.group(0);
      Pattern pattern2 = Pattern.compile(senderCompInsidePatter);
      matcher = pattern2.matcher(result);
      if(matcher.find()) {
        String finalResult = matcher.group(0);
        return finalResult.substring(7, finalResult.length()-8);
      }
    }
    
    return null;
  }
  
}
