package com.vishaal.learn.interviews;

public class TestNull {

	static int foo(int i) {
		return (i < 10 ? null : i);
	}
	
	
	
	
	public static void main(String[] args) {
		int x = TestNull.foo(5);
		System.out.println(x);

	}

}
