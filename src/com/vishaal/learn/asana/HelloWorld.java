package com.vishaal.learn.asana;

import java.util.UUID;

import com.asana.Client;
import com.asana.dispatcher.OAuthDispatcher;

public class HelloWorld {

	public static void main(String[] args) throws Exception {
		Client client = Client.accessToken("ASANA_ACCESS_TOKEN");
		String state = UUID.randomUUID().toString();
		
		OAuthDispatcher dispatcher = (OAuthDispatcher)client.dispatcher;
		
		
		//String url = dispatcher.getAuthorizationUrl(state);
	}

}
