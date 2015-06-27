package com.vishaal.learn.guice;

import com.google.inject.Singleton;

/**
 * 
 * @author vishaalkant
 *
 */
@Singleton
public class EmailService implements MessageService {
	
	
	public EmailService() {
		System.out.println("Creating EmailService");
	}
	
	public boolean sendMessage(String msg, String receipient) {
		//some fancy code to send email
    System.out.println("Email Message sent to "+receipient+" with message="+msg);
    return true;
	}

}
