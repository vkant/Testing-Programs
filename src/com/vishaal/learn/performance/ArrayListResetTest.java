package com.vishaal.learn.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class ArrayListResetTest {
	private static final int SIZE = 100_000;
	
	public static void main (String args[]) {
		List<Integer> clearList = new ArrayList<Integer>();
		List<Integer> removeAllList = new ArrayList<Integer>();
		
		for (int i = 0; i < SIZE; i++) {
			clearList.add(i);
			removeAllList.add(i);
		} 
		
		Stopwatch stopWatch = Stopwatch.createStarted();
		clearList.clear();
		stopWatch.elapsed(TimeUnit.NANOSECONDS);
		stopWatch.stop();
		
		System.out.println("Time Taken for clear -> " + stopWatch.toString());
		
		stopWatch.start();
		removeAllList.removeAll(removeAllList);
		stopWatch.elapsed(TimeUnit.NANOSECONDS);
		stopWatch.stop();
		
		System.out.println("Time Taken for remove All -> " + stopWatch.toString());
	}
}