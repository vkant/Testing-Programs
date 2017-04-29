package com.vishaal.learn.interviews;

public class AmbigousContructor {

	public static void main(String[] args) {
		Integer i = new Integer(null);
		
		System.out.println(i.intValue());
		
		//String s = new String(null);

	}

}
