package it.webred.ct.service.carContrib.web.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import it.webred.cet.permission.CeTUser;

public class Permessi {
	public final static String PERMESSO_SUPERVISIONE_RICHIESTE_CARTELLA ="Supervisione Richieste Cartella";
	public final static String PERMESSO_GESTIONE_RICHIESTE_CARTELLA="Gestione Richieste Cartella";
	public final static String PERMESSO_INSERIMENTO_RICHIESTE_CARTELLA="Immissione Richieste Cartella";
	public final static String PERMESSO_VIS_CARTELLA_SOCIALE="Visualizzazione Cartella Sociale";
	
	
	public static boolean controlla(CeTUser user, String permesso) {
		boolean hasPermission=false;
		HashMap<String,String> hashPermessi = user.getPermList();
		Collection<String> collValoriPermessi=null;
		if (hashPermessi != null) {
			collValoriPermessi = hashPermessi .values();
			Iterator it =collValoriPermessi.iterator();
			while(it.hasNext() )		{
				String permessoVal =it.next().toString();
				//logger.info("-->PERMESSO: " + permessoVal);
				if (permessoVal.contains(permesso)) {
					hasPermission=true;
					break;
				}
						
			}	
		}
			
		return hasPermission;
	}
}
