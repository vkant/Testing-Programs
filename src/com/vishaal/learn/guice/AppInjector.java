/**
 * 
 */
package com.vishaal.learn.guice;

import com.google.inject.AbstractModule;

/**
 * @author vishaalkant
 *
 */
public class AppInjector extends AbstractModule {
	
	public AppInjector() {
		System.out.println("Creating App Injector");
	}
	
	/* (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		//bind(MessageService.class).to(EmailService.class);
		System.out.println("Binding Messaging service implementation !!");
		bind(MessageService.class).to(FacebookService.class);
	}
}
