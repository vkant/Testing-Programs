/**
 * 
 */
package com.vishaal.learn.guice;

import com.google.inject.Singleton;

/**
 * @author vishaalkant
 *
 */
@Singleton
public class FacebookService implements MessageService {
	
	public FacebookService() {
		System.out.println("Creating FacebookService");
	}
	/* (non-Javadoc)
	 * @see com.vishaal.learn.guice.MessageService#sendMessage(java.lang.String, java.lang.String)
	 */
	public boolean sendMessage(String msg, String receipient) {
	//some complex code to send Facebook message
    System.out.println("Message sent to Facebook user "+receipient+" with message="+msg);
    return true;
	}

}
