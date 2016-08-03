package it.webred.ct.service.wrapper.base.handler;


import java.io.IOException;
import java.util.Properties;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.log4j.Logger;
import org.apache.ws.security.WSPasswordCallback;

public class PWCBClientHandler implements CallbackHandler {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	private Properties _cfg = null;
	
	public PWCBClientHandler() {
		super();

		try {
			java.io.InputStream is = this.getClass().getResourceAsStream("PWCBClientHandler.properties");
			_cfg = new Properties();
			_cfg.load(is);
			log.info("File di configurazione PWCB client acquisito");
			
		}catch(Throwable t) {
			log.error("Eccezione handler PWCB: "+t.getMessage(),t);
		}
		
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		
		String clientUser = _cfg.getProperty("it.webred.wss.client.user");
		log.info("Utente: "+clientUser);
		
		for (int i = 0; i < callbacks.length; i++) {
			WSPasswordCallback pwcb = (WSPasswordCallback) callbacks[i];
			String id = pwcb.getIdentifer();
			
			//confronto tra l'utente impostato su axis2.xml e quello in configurazione per recupero password
			if (clientUser.equals(id)) {
				String clientPassword = _cfg.getProperty("it.webred.wss.client.password");
				pwcb.setPassword(clientPassword);
				log.info("Password: "+clientPassword);
			} else {
				throw new IOException("Invalid username " + id);
			}
		}
	}

}
