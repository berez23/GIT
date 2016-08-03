package it.webred.ct.service.comma336.web.bean.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import it.webred.cet.permission.CeTUser;

public class PermessiHandler {

	public final static String PERMESSO_SUPERVISIONE_PRATICHE ="Supervisione Pratiche";
	public final static String PERMESSO_GESTIONE_PRATICHE="Gestione Pratiche Comma336";
	public final static String PERMESSO_VISUALIZZAZIONE_PRATICHE="Visualizzazione";
	
	
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
				if (permessoVal.contains(permesso)) {
					hasPermission= true;
					
				}
						
			}	
		}
		return hasPermission;
		
	}
}
