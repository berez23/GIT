package it.webred.diogene.metadata.backingbeans;

import java.util.Map;

import javax.faces.context.FacesContext;

import it.webred.diogene.menu.*;

/**
 * Backing bean utilizzato dalla pagina di menu metadati.<p>
 * Contiene i metodi chiamati dagli actionListener di questa pagina per il funzionamento dei link (apertura delle
 * pagine successive).
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class MenuMetadataBB implements Menu {

	/**
	 *	Costante che corrisponde al valore String che, secondo la configurazione della navigazione, permette l'apertura 
	 *	della pagina di mappatura tabelle.
	 */
	public static final String APRI_CARICA_TABELLE = "vaiACaricaTabelle";
	/**
	 *	Costante che corrisponde al valore String che, secondo la configurazione della navigazione, permette il rientro 
	 *	al menu iniziale.
	 */
	public static final String APRI_MENU = "vaiAMenu";
	
	/**	
	 * Metodo che permette il funzionamento del link di apertura della pagina di mappatura tabelle.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di mappatura tabelle
	 */
	public String apriCaricaTabelle() {
		String retVal = APRI_CARICA_TABELLE;
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
	
	/* (non-Javadoc)
	 * @see it.webred.diogene.menu.Menu#removeObsoleteBB(java.lang.String)
	 */
	public void removeObsoleteBB(String mode) {
		Map map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if (mode.equals(APRI_CARICA_TABELLE)) {
			if (map.containsKey("CaricaTabelle"))
				map.remove("CaricaTabelle");
			if (map.containsKey("CaricaColonne"))
				map.remove("CaricaColonne");
		}
	}
	
}
