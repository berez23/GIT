package it.webred.diogene.menu;

/**
 * Backing bean utilizzato dalla pagina di menu iniziale.<p>
 * Contiene i metodi chiamati dagli actionListener di questa pagina per il funzionamento dei link (apertura delle
 * pagine successive).
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class MenuBB {
	
	/**	Metodo che permette il funzionamento del link di apertura della pagina di menu metadati (mappatura dati 
	 * su catalogo).
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di menu metadati
	 */
	public String apriMenuMetadata() {
		return "vaiAMenuMetadata";
	}
	
	/** Metodo che permette il funzionamento del link di apertura della pagina di menu visualizzatore.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di menu visualizzatore
	 */
	public String apriMenuVisualizzatore() {
		return "vaiAMenuVisualizzatore";
	}
	
	public String apriMenuNavigatore() {
		return "vaiAMenuNavigatore";
	}	
}
