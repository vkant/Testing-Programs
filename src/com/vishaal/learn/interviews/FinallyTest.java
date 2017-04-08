package com.vishaal.learn.interviews;

public class FinallyTest {

	public static void main(String[] args) {
		
		System.out.println(returnSomething());

	}
	
	public static int returnSomething() {
		try {
			throw new Exception("foo");
		}
		finally {
			return 0;
		}
	}
	
}
