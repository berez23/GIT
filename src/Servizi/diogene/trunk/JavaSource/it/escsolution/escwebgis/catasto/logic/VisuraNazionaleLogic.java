/*
 * Created on 9-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.catasto.logic;

import it.escsolution.escwebgis.catasto.bean.VisuraNazionaleFinder;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.webred.SisterClient.VisuraCatastale;
import it.webred.SisterClient.dto.VisuraDTO;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

import javax.naming.Context;


public class VisuraNazionaleLogic extends EscLogic{
	private String appoggioDataSource;
	private SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	
	public static final String VISURA_NAZIONALE = "VISURA_NAZIONALE";
	public final static String FINDER = "FINDER126";
	
	public VisuraNazionaleLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

	private static final DecimalFormat DF = new DecimalFormat();
	static {
		DF.setGroupingUsed(false);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		DF.setDecimalFormatSymbols(dfs);
		DF.setMaximumFractionDigits(2);
	}
	
	
	public Hashtable getRisultatoRicerca(String cf, String usister, String pwdsister, VisuraNazionaleFinder finder){
		
		Hashtable ht = new Hashtable();
		
		finder.setTotaleRecordFiltrati(1);
		// pagine totali
		finder.setPagineTotali(1);
		finder.setTotaleRecord(1);
		finder.setRighePerPagina(1);

		ht.put(this.FINDER,finder);
		ht.put(this.VISURA_NAZIONALE,this.getVisuraNazionale(cf, usister, pwdsister));
		
		/*INIZIO AUDIT*/
		try {
			Object[] arguments = new Object[2];
			arguments[0] = cf;
			arguments[1] = finder;
			writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
		} catch (Throwable e) {				
			log.debug("ERRORE nella scrittura dell'audit", e);
		}
		/*FINE AUDIT*/
		
		return ht;
	}
	
	
	public VisuraDTO getVisuraNazionale(String cf, String usister, String pwdsister){
		
		Context cont;
		VisuraDTO visura = new VisuraDTO();
		
		try{
			
			if(cf!=null && !"".equals(cf)){
				

				
				if(usister!=null && !"".equals(usister) && pwdsister!=null && !"".equals(pwdsister)){	
					VisuraCatastale classe = new VisuraCatastale();
					visura = classe.visuraCatastale(cf,usister, pwdsister);
				}else
					visura.setMessaggio("Credenziali non valide per l'accesso al portale SISTER");
			
			}else{
				visura.setMessaggio("Codice Fiscale non valido: impossibile effettuare la ricerca");
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);			
			String errText = "Impossibile ottenere una visura!";
    		if (e.getMessage() != null && e.getMessage().toLowerCase().indexOf("Utente gia' connesso da altra postazione".toLowerCase()) > -1) {
    			errText += (" Utente gia' connesso da altra postazione.");
    		}			
			visura.setMessaggio(errText);
		}
		
		return visura;
		
	}
	
}

