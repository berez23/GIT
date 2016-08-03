package it.webred.diogene.visualizzatore.beans;

import java.util.*;
import javax.faces.model.ListDataModel;

import javax.faces.model.DataModel;

/**
 * Bean che contiene i dati per la gestione della visualizzazione del dettaglio per una classe (definizione del valore 
 * per il campo DETAIL, di tipo XMLTYPE, della tabella DV_FORMAT_CLASSE).<p>
 * Ogni oggetto di questa classe rappresenta un elemento (tabella o divisione) dell'elenco definito per il dettaglio.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class DetailGroup {

	/**
	 * Numerico che rappresenta la posizione del bean nell'ArrayList detailAL di DefinizioneClasseBB.<p>
	 * è utilizzato come chiave, non essendovi altri campi che sono presenti sempre.
	 */
	private int index;
	
	/**
	 * Flag che indica se il bean contiene i dati di una tabella (se true) o di una divisione (se false).
	 */
	private boolean table;
	
	/**
	 * DataModel per la costruzione della tabella utilizzata per la visualizzazione e la gestione dell'elemento 
	 * (tabella o divisione).
	 */
	private DataModel detailDM;
	
	/**
	 * ArrayList di oggetti Detail che rappresenta le righe aggiunte alla tabella (se table è true) oppure la 
	 * divisione (se table è false).
	 */
	private ArrayList<Detail> detailAL;
	

	public ArrayList<Detail> getDetailAL() {
		return detailAL;
	}

	/**
	 * Imposta l'ArrayList detailAL e il DataModel detailDM.
	 * @param detailAL Il nuovo valore per l'oggetto detailAL
	 */
	public void setDetailAL(ArrayList<Detail> detailAL) {
		this.detailAL = detailAL;
		if (detailDM == null)
			detailDM = new ListDataModel();
		detailDM.setWrappedData(this.detailAL);
	}		

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isTable() {
		return table;
	}

	public void setTable(boolean table) {
		this.table = table;
	}
	
	public DataModel getDetailDM() {
		return detailDM;
	}

	public void setDetailDM(DataModel detailDM) {
		this.detailDM = detailDM;
	}
	
}
