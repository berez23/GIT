package it.webred.ct.service.wrapper.intTerritorio.handler;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.log4j.Logger;
import org.apache.ws.security.WSPasswordCallback;

public class PWCBServiceHandler implements CallbackHandler {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		log.info("------- PWCBHandler::handle -----------");

		for (int i = 0; i < callbacks.length; i++) {

			// When the server side need to authenticate the user
			WSPasswordCallback pwcb = (WSPasswordCallback) callbacks[i];

			// For plain text password scenarios
			if (pwcb.getUsage() == WSPasswordCallback.USERNAME_TOKEN_UNKNOWN) {
				log.info("USERNAME_TOKEN_UNKNOWN");
				String utente = pwcb.getIdentifer();
				log.info("Autorizzazione per utente '"+utente+"'");
				
				//TODO: i valori di user e psw devo essere ripresi da db
				
				if (utente.equals("test") && pwcb.getPassword().equals("test")) {					
					//do nothing
					log.info("Utente '"+utente+"' autorizzato");
				} 
				else {
					throw new UnsupportedCallbackException(callbacks[i],"check failed");
				}
			}
		}

		log.info("------- F I N E -----------");
	}

}
