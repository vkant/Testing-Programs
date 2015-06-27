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
public class MockMessageService implements MessageService {

	/* (non-Javadoc)
	 * @see com.vishaal.learn.guice.MessageService#sendMessage(java.lang.String, java.lang.String)
	 */
	public boolean sendMessage(String msg, String receipient) {
		return true;
	}
}
