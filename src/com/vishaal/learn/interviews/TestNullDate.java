package com.vishaal.learn.interviews;

import java.util.Date;

public class TestNullDate {

	public static void main(String[] args) {
		
		Date date = new Date();
		System.out.println("Before : " + date);
		modifyDate(date);
		System.out.println("After : " + date);
	}

	private static void modifyDate(Date date) {
		date.setDate(17);
		date = null;
	}
}
