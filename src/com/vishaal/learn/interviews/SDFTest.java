package com.vishaal.learn.interviews;


import java.text.SimpleDateFormat;
import java.util.Date;

public class SDFTest {

	public static void main(String[] args) throws Exception {
		
		SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy G");
		SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd G");
		
		Date date = sdfInput.parse("32/1/2017 AD");
		System.out.println(date);
		
		String output = sdfOutput.format(date);
		System.out.println(output);

	}

}
