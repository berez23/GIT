package it.webred.successioni.output;


import it.webred.utils.GenericTuples;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
//import org.eclipse.jdt.internal.compiler.ast.ThrowStatement;


public class GestioneStringheVie {

	private static HashMap<String, String> sedimario = new HashMap<String, String>();
	private static Collection<String> ctrSedime = new ArrayList<String>();
	private static final org.apache.log4j.Logger log = Logger.getLogger(GestioneStringheVie.class.getName());
	
	private static HashMap<String, Indirizzo<String, String>> cacheIndirizziRiconosciuti = new HashMap<String, Indirizzo<String, String>>();

	

	
	public GestioneStringheVie() {
		
	}
	
	static
	{
		try
		{
			sedimario.put("CORSO", "CORSO");
			sedimario.put("C.SO", "CORSO");
			sedimario.put("FRAZIONE", "FRAZIONE");
			sedimario.put("FRAZ.", "FRAZIONE");
			sedimario.put("FRAZ", "FRAZIONE");
			sedimario.put("LARGO", "LARGO");
			sedimario.put("L.GO", "LARGO");
			sedimario.put("L.", "LARGO");
			sedimario.put("L", "LARGO");
			sedimario.put("LOCALITA'", "LOCALITA'");
			sedimario.put("LOCALITA''", "LOCALITA'");
			sedimario.put("LOC.", "LOCALITA'");
			sedimario.put("LOC", "LOCALITA'");
			sedimario.put("PIAZZOLA", "PIAZZOLA");
			sedimario.put("P.LA", "PIAZZOLA");
			sedimario.put("PIAZZALE", "PIAZZALE");
			sedimario.put("P.LE", "PIAZZALE");
			sedimario.put("PIAZZA", "PIAZZA");
			sedimario.put("P.ZZA", "PIAZZA");
			sedimario.put("P.", "PIAZZA");
			sedimario.put("P", "PIAZZA");
			sedimario.put("PIAZZETTA", "PIAZZETTA");
			sedimario.put("P.TTA", "PIAZZETTA");
			sedimario.put("STRADA", "STRADA");
			sedimario.put("SRADA", "STRADA");
			sedimario.put("STR.", "STRADA");
			sedimario.put("STR", "STRADA");
			sedimario.put("SC", "STRADA COMUNALE");
			sedimario.put("S.C.", "STRADA COMUNALE");
			sedimario.put("SP", "STRADA PROVINCIALE");
			sedimario.put("S.P.", "STRADA PROVINCIALE");
			sedimario.put("SR", "STRADA REGIONALE");
			sedimario.put("S.R.", "STRADA REGIONALE");
			sedimario.put("SS", "STRADA STATALE");
			sedimario.put("S.S.", "STRADA STATALE");
			sedimario.put("STRADA PROVINCIALE", "STRADA PROVINCIALE");
			sedimario.put("STRADA COMUNALE", "STRADA COMUNALE");
			sedimario.put("STRADA REGIONALE", "STRADA REGIONALE");
			sedimario.put("ZONA INDUSTRIALE", "ZONA INDUSTRIALE");
			sedimario.put("ZI", "ZONA INDUSTRIALE");
			sedimario.put("Z.I", "ZONA INDUSTRIALE");
			sedimario.put("Z.I.", "ZONA INDUSTRIALE");
			sedimario.put("VIA", "VIA");
			sedimario.put("V.", "VIA");
			sedimario.put("V", "VIA");
			sedimario.put("VIALE", "VIALE");
			sedimario.put("V.LE", "VIALE");
			sedimario.put("VICOLO", "VICOLO");
			sedimario.put("V.LO", "VICOLO");
			sedimario.put("VOCABOLO", "VOCABOLO");
			sedimario.put("VICOLO", "VICOLO");
			sedimario.put("VICOLETTO", "VICOLO");
			sedimario.put("VIC.", "VICOLO");
			sedimario.put("VOC.", "VOCABOLO");
			sedimario.put("VOC", "VOCABOLO");
			sedimario.put("COLLE", "COLLE");
			sedimario.put("COL.", "COLLE");
			sedimario.put("COLL.", "COLLE");
			sedimario.put("C.DA", "CONTRADA");
			sedimario.put("CONTRADA", "CONTRADA");
			sedimario.put("C/DA", "CONTRADA");
			sedimario.put("CON", "CONTRADA");
			sedimario.put("CONTR", "CONTRADA");
			sedimario.put("TRAVERSA", "TRAVERSA");
			sedimario.put("TRAV.", "TRAVERSA");
			sedimario.put("RIONE", "RIONE");
			sedimario.put("R.NE", "RIONE");
			sedimario.put("-", "-");

			
			ctrSedime.addAll(sedimario.values());
		    //Aggiungo anche le chiavi di sedimario 
			Set es = sedimario.entrySet();
			Iterator it = es.iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = ((String)entry.getKey()).toUpperCase();
				if (!ctrSedime.contains(key)) 
					ctrSedime.add(key);
			}


		
		}
		catch (Exception e1)
		{
			log.error("Errore nella costruzione delle tabelle di riferimento sedime vie",e1);
			throw new RuntimeException(e1);
		}
		finally
		{
		}

	}
	
	public static String trovaSedimeUnivoco(String sedime){
		
		if (sedime != null)
			return sedimario.get(sedime.toUpperCase());
		else
			return "-";
		
	}
	
	
	
	public static Collection<Civico> restituisciCivico(String input) throws Exception { 
		try {
			
			System.out.println("------"+ input);
		       Pattern pattern = Pattern.compile("(\\D*)?(\\d+)");
	        
	        Matcher matcher = pattern.matcher( input );
	        LinkedList<String> numeriCivici = new LinkedList<String>();
	        int numeroFind =1;
	        int endOld=0;
	        while ( matcher.find() ) 
	        {
	        	String substr = matcher.group();
	        	System.out.println(substr);
	        	
	            int start = matcher.start();
	            int end = matcher.end();
	            
	            // controllo sul contenuto di stringhe non valide per il numero civico
	            boolean contieneStrighe = false;
				String[] c = {"INTERNO","SCALA","PIANO","ANTRONE","CORTILE"};
				// SE IL NUMERO È RELATIVO A UNA SCALA, PIANO ECC ECC ALLORA NON LO SCELGO COME CIVICO
				for(int l=0; l < c.length; l++){
					if (substr.toUpperCase().indexOf(c[l])!=-1) {
						contieneStrighe = true;
						l=c.length;
					}
			    }
					
				if (!contieneStrighe) {
		            String civico = null;
		            int charat = substr.length();
		            while (charat>1) {
		            	String taglia = substr.substring(charat-1).toUpperCase();
		            	try {
		            		new Integer(taglia);
		            		charat = charat-1;
		            		civico = taglia;
		            	} catch (Exception e) {
		            		charat = 0;
		            	}
		            }

						// controllo che nel caso che sia il secondo numero trovato nell'indirizzo, il primo trovato non sia più di quattro caratteri indietro
						// altrimenti vorrebbe dire che il primo appartiene al nome della via ( es. via 20 settembre 14)
						if (numeroFind==2) {
							if (end-endOld>4)
								numeriCivici.remove(0);
						}
		            	numeriCivici.add(civico);
		    			numeroFind = numeroFind +1;
		    			System.out.println(civico + ":" + start + "-" + end);
		        }
				endOld = end;
	        }
	        
	        // tolgo dalla stringa in input la parte riguardante il civico in modo da poter ridare indietro anche la via
	        Indirizzo<String, String> ind =  null;
	        if (numeriCivici.size()>0) {
	        	String indirizzo = input;
	        	int lastpos = indirizzo.lastIndexOf(numeriCivici.get(0));
	        	indirizzo.substring(0, lastpos).trim();
	        	ind = restituisciIndirizzo(indirizzo, null);
	        } else  {
	        	ind = restituisciIndirizzo(input, null);
	        	numeriCivici.add("0");
	        }
	        
	        if (ind==null)
	        	throw new Exception("Errore trasformazione civico:" + input);

	        Collection civici =  new ArrayList<Civico>();
			for(int i=0; i < numeriCivici.size(); i++){
				Civico c = new Civico(ind.getSed(), ind.getInd(),numeriCivici.get(i));
				civici.add(c);
		    }
			
			return civici;
	        
		} catch (Exception e) {
			log.error("Errore trasformazione civico" + input,e);
			throw new Exception(e);
		}
	}
	
	
	/**
	 * Dato un indirizzo : "via degli sciri" , il metodo restituisce il sedime (univoco da hash)
	 * e il nome via separatamente
	 * Se vengono forniti uno o più sedime di riferimento il metodo semplicemente non ricerca il sedime ma toglie quello di riferimento dalla stringa
	 * @param descrInd
	 * @param sedimeRif -  un eventuale sedime già attribuito come valore separato all'indirizzo (viene preso in considerazione in modo subordinato rispetto a quello contenuto nell'indirizzo)
	 * @return
	 * @throws Exception 
	 */
	public static Indirizzo<String,String> restituisciIndirizzo(String descrInd, String sedimeRif) throws Exception {
		
		// controllo se indirizzo presente in cache
		if (cacheIndirizziRiconosciuti.containsKey(descrInd))
			return cacheIndirizziRiconosciuti.get(descrInd);
		
		String sedimeOk = null;
		String sedime = null;
		descrInd = descrInd==null?"-":descrInd.toUpperCase();
		String indirizzo = descrInd.trim();
		try {
			
			if (sedimeRif!=null)
				sedimeRif = sedimeRif.toUpperCase();
			
			
			String[] predicati ={"DEI", "DEGLI", "DEL", "DELLO", "DELLA" ,"DELLE", "AL", "AGLI", "ALLA","ALLE","FRA","TRA","SU","PER","DI","A", "DA","IN" , "CON", "NEL","NELL'","NELLO","NELLA","NEI","NEGLI","NELLE", "DAL","DALL'","DALLA","DAI","DAGLI","DALLE" };
			descrInd = descrInd.trim();

			if (descrInd.equals("STRADA PROVINCIALE 457_1 DI BEROIDE"))
				descrInd=descrInd;

			int posSpazio = descrInd.lastIndexOf(" ");
			int posSpazio2 = descrInd.lastIndexOf(".");
			if (posSpazio2>posSpazio)
				posSpazio = posSpazio2;
			
			while (posSpazio>0) {
				String sottoS = descrInd.substring(0, posSpazio).toUpperCase();
				if (ctrSedime.contains(sottoS)) {
					sedime = descrInd.substring(0, posSpazio);
					String articoloInSedime=null;
					for (int ii=0 ; ii<predicati.length;ii++) {
						if (sedime.endsWith(predicati[ii])) {
							articoloInSedime = predicati[ii];
							ii=predicati.length;
						}
					}
					String sedime1 = trovaSedimeUnivoco(sedime);
					sedimeOk = null;
					//cerco descrizione univoca per il sedime
					if (sedime1 != null)
						sedimeOk = sedime1;
					if (sedimeOk!=null && descrInd.startsWith(sedimeOk)) {
						if (articoloInSedime!=null)
							indirizzo = descrInd.substring(sedimeOk.length()).trim();
						else
							indirizzo = descrInd.substring(sedime.length()).trim();
					}
					else {
						indirizzo = descrInd.substring(posSpazio).trim();
					}
					if (indirizzo.startsWith("."))
						indirizzo = indirizzo.substring(1);
							
					posSpazio=-1;
				} else {
					posSpazio = sottoS.lastIndexOf(" ");
					posSpazio2 = sottoS.lastIndexOf(".");
					if (posSpazio2>posSpazio)
						posSpazio = posSpazio2;			
				}
			}
					
		} catch (Exception e) {
			log.error("Errore trasformazione indirizzo" + descrInd,e);
			throw new Exception(e);
		}
		Indirizzo<String, String> ret = null;
		if (sedimeOk==null && sedimeRif!=null) {
			ret =  restituisciIndirizzo(sedimeRif + " " + descrInd,null);
			cacheIndirizziRiconosciuti.put(descrInd, ret);
			return ret;
		}
		sedimeOk = sedimeOk==null?"-":sedimeOk.toUpperCase();
		ret =  new Indirizzo<String, String>(sedimeOk,indirizzo);
		cacheIndirizziRiconosciuti.put(descrInd, ret);
		
		return ret;

	}
	public static void main(String[] args)
	{
		GestioneStringheVie gsv = new GestioneStringheVie();
		try {
			Collection<Civico> SSS = gsv.restituisciCivico("AAAAAA 10A");
			SSS.size();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
