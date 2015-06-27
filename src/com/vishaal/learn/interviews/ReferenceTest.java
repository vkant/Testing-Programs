package com.vishaal.learn.interviews;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
 * @author vkant
 *
 */
public class ReferenceTest {
	
	private Date d1; 
	private Date d2; 
	
	private void refTest() {
		testDate();
		testEmployee();
	}

	private void testDate() {
		Calendar cal1 = new GregorianCalendar(1979, 2, 26);
		Calendar cal2 = new GregorianCalendar(1979, 9, 14);

		d1 = cal1.getTime();
		d2 = cal2.getTime();

		System.out.println("a : " + d1 + " - b : " + d2);

		test(d1, d2);

		if (d1.before(d2)) {
			System.out.println("Why");
		}

		System.out.println("a : " + d1 + " - b : " + d2);
	}
	
	private void testEmployee() {
		Employee e1 = new Employee(1, Calendar.getInstance().getTime(), null);
		Employee e2 = new Employee(2, Calendar.getInstance().getTime(), null);
		System.out.println("E1 : " + e1);
		System.out.println("E2 : " + e2);
		nullEmployee(e1, e2);
		System.out.println("E1 : " + e1);
		System.out.println("E2 : " + e2);
	}
	
	public static void swap(Integer a, Integer b) {
		Integer temp = a;
		a = b;
		b = temp;
	}
	
	private void nullEmployee(Employee e1, Employee e2) {
		e1 = new Employee(5, null,null);
		e2 = null;
	}
	
	private void test(Date a, Date b) {
		// Date temp = a;
		// a = b;
		// b = temp;
		a = null;
		b = null;
	}

	public static void main(String[] args) {
		Integer a = 1;
		Integer b = 2;
		System.out.println("a =  " + a + " : b = " + b);
		swap(a, b);
		System.out.println("a =  " + a + " : b = " + b);

		int c = 3;
		int d = 4;
		System.out.println("c = " + c + ", d = " + d);
		swap(c, d);
		System.out.println("c = " + c + ", d = " + d);
		
		ReferenceTest refTest = new ReferenceTest();
		refTest.refTest();
		
	}
}
