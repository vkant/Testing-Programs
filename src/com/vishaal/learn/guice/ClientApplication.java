/**
 * 
 */
package com.vishaal.learn.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author vishaalkant
 *
 */
public class ClientApplication {
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AppInjector());        
    MyApplication app = injector.getInstance(MyApplication.class);
    app.sendMessage("Hi Vishaal", "vishaal.kant@gmail.com");
	}
}
