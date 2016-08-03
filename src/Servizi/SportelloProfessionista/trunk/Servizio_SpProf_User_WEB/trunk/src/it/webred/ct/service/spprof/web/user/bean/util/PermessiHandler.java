package it.webred.ct.service.spprof.web.user.bean.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import it.webred.cet.permission.CeTUser;

public class PermessiHandler {

	public final static String PERMESSO_SUPERVISIONE_RICHIESTE_FASCICOLO ="Supervisione Richieste Fascicolo";
	public final static String PERMESSO_GESTIONE_RICHIESTE_FASCICOLO="Gestione Richieste Fascicolo";
	public final static String PERMESSO_INSERIMENTO_RICHIESTE_FASCICOLO="Immissione Richieste Fascicolo";
	
	
	public static boolean controlla(CeTUser user, String permesso) {
		boolean hasPermission=false;
		if (user == null)
			return false;
		
		HashMap<String,String> hashPermessi = user.getPermList();
		Collection<String> collValoriPermessi=null;
		if (hashPermessi != null) {
			collValoriPermessi = hashPermessi .values();
			Iterator<String> it =collValoriPermessi.iterator();
			while(it.hasNext() )		{
				String permessoVal =it.next().toString();
				//System.out.println("-->PERMESSO: " + permessoVal);
				if (permessoVal.contains(permesso)) {
					//System.out.println("-->PERMESSO  " + permesso + " TROVATO");
					return true;
					
				}
						
			}	
		}
			
		//return hasPermission;
		// TODO: Far ritornare false
		return false;
	}
}
