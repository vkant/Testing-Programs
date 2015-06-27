package com.vishaal.learn.guice;

import com.google.inject.Inject;

/**
 * 
 * @author vishaalkant
 *
 */
public class MyApplication {
	private MessageService service;

	@Inject
	public void setService(MessageService service) {
		System.out.println();
		this.service = service;
	}

	public boolean sendMessage(String msg, String rec) {
		// some business logic here
		return service.sendMessage(msg, rec);
	}
}
