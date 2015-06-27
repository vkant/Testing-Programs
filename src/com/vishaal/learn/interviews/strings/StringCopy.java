package com.vishaal.learn.interviews.strings;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;



public class StringCopy {

	public static void main(String[] args) {
		List<String> sourceList = new ArrayList<String>();
		sourceList.add("Vishaal Kant");
		sourceList.add("Kapeesh Kant");
		
		
		List<String> copyList = new ArrayList<String>();
		
		for (String str : sourceList) {
			copyList.add(str);
		}
		
		//Transforming the source list
		/*for (String str : sourceList) {
			//str.toUpperCase(Locale.ENGLISH);
			StringUtils.upperCase(str, Locale.ENGLISH);
		}*/
		CollectionUtils.transform(sourceList, new Transformer() {
			
			public Object transform(Object arg0) {
				String input = ((String) arg0).toUpperCase();
				return input;
			}
		});
		
		System.out.println("scr List : " + sourceList);
		System.out.println("==========================");
		System.out.println("Target List : " + copyList);
		

	}

}
