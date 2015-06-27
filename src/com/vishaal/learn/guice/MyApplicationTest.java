package com.vishaal.learn.guice;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * 
 * @author vishaalkant
 *
 */

public class MyApplicationTest {
	private Injector injector;

	@Before
	public void setup() throws Exception {
		System.out.println("Setup");
		injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(MessageService.class).to(MockMessageService.class);
			}
		});
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Tear Down");
		injector = null;
	}

	@Test
	public void test() {
		MyApplication appTest = injector.getInstance(MyApplication.class);
		Assert.assertEquals(true,
				appTest.sendMessage("Hi Vishaal", "vishaal@abc.com"));;
	}

	@Test
	public void test2() {

		Assert.assertEquals(true, true);
	}

}
