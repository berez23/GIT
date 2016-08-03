package it.webred.diogene.correlazione.backingbeans;

import java.security.Principal;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import it.webred.diogene.menu.*;
import it.webred.permessi.GestionePermessi;

/**
 * Backing bean utilizzato dalla pagina di menu correlazione.<p>
 * Contiene i metodi chiamati dagli actionListener di questa pagina per il funzionamento dei link (apertura delle
 * pagine successive).
 * @author Filippo Mazzini
 * @version $Revision: 1.5 $ $Date: 2012/01/18 07:53:49 $
 */
public class MenuCorrelazioneBB implements Menu {

	/**
	 *	Costante che corrisponde al valore String che, secondo la configurazione della navigazione, permette l'apertura 
	 *	della pagina di gestione temi.
	 */
	public static final String APRI_GESTIONE_TEMI = "vaiAGestioneTemi";
	
	/**
	 *	Costante che corrisponde al valore String che, secondo la configurazione della navigazione, permette l'apertura 
	 *	della pagina di gestione tipi etichetta.
	 */
	public static final String APRI_GESTIONE_TIPI_ETICHETTA = "vaiAGestioneTipiEtichetta";
	
	/**
	 *	Costante che corrisponde al valore String che, secondo la configurazione della navigazione, permette l'apertura 
	 *	della pagina di gestione tipi etichetta.
	 */
	public static final String APRI_VISUALIZZAZIONE = "vaiAListaTemi";
	
	/**
	 *	Costante che corrisponde al valore String che, secondo la configurazione della navigazione, permette il rientro 
	 *	al menu iniziale.
	 */
	public static final String APRI_MENU = "vaiAMenu";
	
	/**
	 *	Costante che corrisponde al valore String che, secondo la configurazione della navigazione, permette il rientro 
	 *	al menu correlazione.
	 */
	public static final String APRI_MENU_CORRELAZIONE = "vaiAMenuCorrelazione";
	
	/**
	 *	Costante utilizzata per il controllo dei permessi sulla gestione dei temi e dei tipi etichetta.
	 */
	public static final String CONFRONTO_DATI_ETICHETTE = "Confronto dati per etichette";
	
	/**
	 *	Costante utilizzata per il controllo dei permessi sulla gestione dei temi e dei tipi etichetta. 
	 */
	public static final String CONFIGURA = "Configura";
	
	/**	
	 * Metodo che permette il funzionamento del link di apertura della pagina di gestione temi.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di gestione temi
	 */
	public String apriGestioneTemi() {
		String retVal = APRI_GESTIONE_TEMI;
		removeObsoleteBB(retVal);
		return retVal;
	}
	
	/**	
	 * Metodo che permette il funzionamento del link di apertura della pagina di gestione tipi etichetta.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di gestione tipi etichetta
	 */
	public String apriGestioneTipiEtichetta() {
		String retVal = APRI_GESTIONE_TIPI_ETICHETTA;
		removeObsoleteBB(retVal);
		return retVal;
	}
	
	/**	
	 * Metodo che permette il funzionamento del link di apertura della pagina di visualizzazione dati da archivi.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di visualizzazione dati da archivi
	 */
	public String apriVisualizzazione() {
		String retVal = APRI_VISUALIZZAZIONE;
		removeObsoleteBB(retVal);
		return retVal;
	}
	
	/**	
	 * Metodo che permette il funzionamento del link di rientro al menu iniziale.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette il rientro al menu iniziale
	 */
	public String apriMenu() {
		String retVal = APRI_MENU;
		removeObsoleteBB(retVal);
		return retVal;
	}
	
	/**	
	 * Metodo che permette il funzionamento del link di rientro al menu correlazione.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette il rientro al menu correlazione
	 */
	public String apriMenuCorrelazione() {
		String retVal = APRI_MENU_CORRELAZIONE;
		removeObsoleteBB(retVal);
		return retVal;
	}
	
	/* (non-Javadoc)
	 * @see it.webred.diogene.menu.Menu#removeObsoleteBB(java.lang.String)
	 */
	public void removeObsoleteBB(String mode) {
		Map map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if (mode.equals(APRI_GESTIONE_TEMI)) {
			if (map.containsKey("Temi"))
				map.remove("Temi");
		}
		if (mode.equals(APRI_GESTIONE_TIPI_ETICHETTA)) {
			if (map.containsKey("TipiEtichetta"))
				map.remove("TipiEtichetta");
		}
		if (mode.equals(APRI_VISUALIZZAZIONE) || mode.equals(APRI_MENU_CORRELAZIONE)) {
			if (map.containsKey("ListaTemi"))
				map.remove("ListaTemi");
			if (map.containsKey("ListaTipiEtichetta"))
				map.remove("ListaTipiEtichetta");
			if (map.containsKey("ListaArchivi"))
				map.remove("ListaArchivi");
			if (map.containsKey("Ricerche"))
				map.remove("Ricerche");
		}
	}
		
	/**
	 * Metodo che restituisce un flag che indica se l'utente è autorizzato alla gestione dei temi.
	 * @return Un flag che indica se l'utente è autorizzato alla gestione dei temi.
	 */
	public boolean isAutGestioneTemi() {
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
		Principal principal = extContext.getUserPrincipal();
		String context = extContext.getRequestContextPath().substring(1);
		/* 
		TODO: REINTEGRARE LA GESTIONE PERMESSI
		*/
		return true  ; 		//GestionePermessi.autorizzato(principal, context, CONFRONTO_DATI_ETICHETTE, CONFIGURA);
	}
	
	/**
	 * Metodo che restituisce un flag che indica se l'utente è autorizzato alla gestione dei tipi etichetta.
	 * @return Un flag che indica se l'utente è autorizzato alla gestione dei tipi etichetta.
	 */
	public boolean isAutGestioneTipiEtichetta() {
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
		Principal principal = extContext.getUserPrincipal();
		String context = extContext.getRequestContextPath().substring(1);
				/* 
		TODO: REINTEGRARE LA GESTIONE PERMESSI
		*/
		
		return true; //GestionePermessi.autorizzato(principal, context, CONFRONTO_DATI_ETICHETTE, CONFIGURA);
	}
	
}
