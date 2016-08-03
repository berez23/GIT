package it.webred.diogene.correlazione.backingbeans;

import it.webred.diogene.correlazione.beans.Ricerca;
import it.webred.diogene.correlazione.beans.TipoEtichetta;
import it.webred.diogene.correlazione.model.RicercheModel;

import java.util.ArrayList;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.column.HtmlSimpleColumn;

/**
 * Backing bean utilizzato per la visualizzazione dei dati delle ricerche in archivi consultabili per mezzo di 
 * popup con paginazione (una popup per ogni archivio).
 * @author Filippo Mazzini
 * @version $Revision: 1.2 $ $Date: 2007/11/22 15:59:57 $
 */ 
public class VisualizzaDatiBB
{
	
	/**
	 * Indice della ricerca corrente, all'interno dell'ArrayList ricerche in RicercheBB (in base 0).
	 */
	private String idxRicerca;
	/**
	 * Indice dell'archivio di cui si stanno visualizzando i dati, all'interno della ricerca corrente (in base 0).
	 */
	private String idxArchivio;
	/**
	 * Numero della pagina visualizzata (in base 1).
	 */
	private String pagina;
	/**
	 * Oggetto di classe Ricerca che contiene i dati della ricerca corrente.
	 */
	Ricerca ricerca;
	
	public String getIdxArchivio()
	{
		return idxArchivio;
	}
	public void setIdxArchivio(String idxArchivio)
	{
		this.idxArchivio = idxArchivio;
	}
	public String getPagina()
	{
		return pagina;
	}
	public void setPagina(String pagina)
	{
		this.pagina = pagina;
	}
	public String getIdxRicerca()
	{
		return idxRicerca;
	}
	/**
	 * Oltre ad impostare idxRicerca, valorizza secondo il nuovo valore di esso l'oggetto ricerca.
	 * @param idxRicerca Nuovo valore per idxRicerca.
	 */
	public void setIdxRicerca(String idxRicerca)
	{
		this.idxRicerca = idxRicerca;
		if (this.idxRicerca == null) {
			ricerca = new Ricerca();
		}else{
			Map sessMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			ricerca = ((RicercheBB)sessMap.get("Ricerche")).getRicerche().get(new Integer(this.idxRicerca).intValue());
		}
	}

	/**
	 * Restituisce un oggetto String, che rappresenta il numero della pagina precedente a quella visualizzata nella popup.
	 * @return Un oggetto String che rappresenta il numero della pagina precedente a quella visualizzata nella popup.
	 */
	public String getPaginaIndietro() {
		return "" + new Integer(new Integer(pagina).intValue() - 1).intValue();
	}
	
	/**
	 * Restituisce un oggetto String, che rappresenta il numero della pagina successiva a quella visualizzata nella popup.
	 * @return Un oggetto String che rappresenta il numero della pagina successiva a quella visualizzata nella popup.
	 */
	public String getPaginaAvanti() {
		return "" + new Integer(new Integer(pagina).intValue() + 1).intValue();
	}
	
	/**
	 * Restituisce il nome dell'archivio di cui sono visualizzati i dati nella popup corrente.
	 * @return Un oggetto String, che rappresenta il nome dell'archivio di cui sono visualizzati i dati nella popup corrente.
	 */
	public String getNomeArchivio() {
		return getRicerca().getArchivi().get(new Integer(idxArchivio).intValue()).getName();
	}
	
	public Ricerca getRicerca()
	{
		return ricerca;
	}
	
	public void setRicerca(Ricerca ricerca) {
		this.ricerca = ricerca;
	}
	
	/**
	 * Restituisce, per la visualizzazione nella popup, una String che contiene la chiave della ricerca effettuata.
	 * @return Un oggetto di classe String che contiene la chiave della ricerca effettuata.
	 */
	public String getParametriRicerca() {
		Ricerca ricerca = getRicerca();
		if (ricerca == null)
			return null;
		String retVal = "";
		ArrayList<TipoEtichetta> tipiEtichetta = ricerca.getTipiEtichetta();
		for (TipoEtichetta tipoEtichetta : tipiEtichetta) {
			if (!retVal.equals(""))
				retVal += ", ";
			retVal += tipoEtichetta.getName();
			String chiave = tipoEtichetta.getChiave();
			String operatore = tipoEtichetta.getOperatore();
			if (operatore.equals(RicercheModel.UGUALE_A.getValue().toString()) ||
					operatore.equals(RicercheModel.DIVERSO_DA.getValue().toString()) ||
					operatore.equals(RicercheModel.MAGGIORE_DI.getValue().toString()) ||
					operatore.equals(RicercheModel.MINORE_DI.getValue().toString())) {
					retVal += (" " + operatore + " " + chiave);
			} else if (operatore.equals(RicercheModel.COMPRESO_TRA.getValue().toString()) ||
					operatore.equals(RicercheModel.NON_COMPRESO_TRA.getValue().toString())) {
				retVal += " ";
				String[] params = chiave.split(RicercheModel.separaParams);
				if (operatore.equals(RicercheModel.NON_COMPRESO_TRA.getValue().toString())) {
					retVal += "NOT ";
				}
				retVal += ("BETWEEN " + params[0] + " AND " + (params.length == 1 ? params[0] : params[1]));
			} else if (operatore.equals(RicercheModel.COMINCIA_PER.getValue().toString())) {
				retVal += (" LIKE " + chiave + "%");
			} else if (operatore.equals(RicercheModel.FINISCE_PER.getValue().toString())) {
				retVal += (" LIKE %" + chiave);
			} else if (operatore.equals(RicercheModel.CONTIENE.getValue().toString())) {
				retVal += (" LIKE %" + chiave + "%");
			} else if (operatore.equals(RicercheModel.VUOTO.getValue().toString())) {
				retVal += " IS NULL";
			} else if (operatore.equals(RicercheModel.NON_VUOTO.getValue().toString())) {
				retVal += " IS NOT NULL";
			} else if (operatore.equals(RicercheModel.CONTENUTO_IN.getValue().toString())) {
				retVal += " ";
				String[] params = chiave.split(RicercheModel.separaParams);
				int numParams = params.length;
				retVal += "IN (";
				for (int i = 0; i < numParams; i++) {
					retVal += (params[i] + ((i < numParams - 1) ? ", " : ""));
				}
				retVal += ")";
			}
		}
		return retVal;
	}
	
	/**
	 * Metodo che restituisce un oggetto HtmlDataTable (da utilizzarsi per il value binding delle tabelle dinamiche), 
	 * a partire dall'ArrayList tridimensionale risultatiVis dell'oggetto Ricerca restituito da getRicerca().
	 * @return Un oggetto HtmlDataTable, da utilizzarsi per il value binding delle tabelle dinamiche della popup di 
	 * visualizzazione dati.
	 */
	public HtmlDataTable getTabella() {
		HtmlDataTable retVal = new HtmlDataTable();
		if (getRicerca() == null)
			return retVal;
		ArrayList<ArrayList<String>> al1 = getRicerca().getRisultatiVis().get(new Integer(idxArchivio).intValue());
		//creo la tabella
		if (al1 == null || al1.size() == 0) {
			return retVal;
		}
		//imposto stili, value binding e campo "var"
		retVal.setStyleClass("griglia");
		retVal.setHeaderClass("thgrigliacorrel");
		retVal.setRowClasses(",alternateRow");
		retVal.setValueBinding("value", FacesContext.getCurrentInstance().getApplication().createValueBinding("#{VisualizzaDati.datiVis}"));
		retVal.setVar("dato"); 
		int columns = al1.get(0).size();
		ArrayList<String> headers = al1.get(0);
        for (int i = 0; i < columns; i++) 
        {
            //intestazione (header)
        	UIOutput header = new UIOutput();
            header.setValue(headers.get(i));
            //oggetto colonna (HtmlSimpleColumn)
            HtmlSimpleColumn column = new HtmlSimpleColumn();
            column.setHeader(header);
            //visualizzazione del valore
            HtmlOutputText output = new HtmlOutputText();
            output.setValueBinding("value", FacesContext.getCurrentInstance().getApplication().createValueBinding("#{dato[" + i + "]}"));
            column.getChildren().add(output);
            //aggiungo la colonna alla tabella
            retVal.getChildren().add(column);
        }
		return retVal;
	}
	
	/**
	 * Restituisce un oggetto Integer che contiene il numero delle pagine per la popup visualizzata.
	 * @return Un oggetto Integer che rappresenta il numero delle pagine per la popup visualizzata.
	 */
	public Integer getNumPagineRicerca() {
		Ricerca ricerca = getRicerca();
		if (ricerca == null)
			return null;
		Integer recordCount = ricerca.getRecordCount().get(new Integer(idxArchivio).intValue());
		int numPagine = (recordCount.intValue()
				 		- (recordCount.intValue() % RicercheBB.righePerPagina)) / RicercheBB.righePerPagina 
						+ (recordCount.intValue() % RicercheBB.righePerPagina == 0 ? 0 : 1);
		if (numPagine < 1)
			numPagine = 1;
		return new Integer(numPagine);
	}
	
	/**
	 * Restituisce i dati da visualizzare nella popup, escludendo le intestazioni. 
	 * @return Una ArrayList bidimensionale, contenente i dati da visualizzare nella popup, escludendo le intestazioni. 
	 */	
	public ArrayList<ArrayList<String>> getDatiVis() {
		ArrayList<ArrayList<String>> retVal = new ArrayList<ArrayList<String>>();
		if (getRicerca().getRisultatiVis() == null || getRicerca().getRisultatiVis().size() == 0)
			return retVal;
		ArrayList<ArrayList<String>> al1 = getRicerca().getRisultatiVis().get(new Integer(idxArchivio).intValue());
		int conta = 0;
		for (ArrayList<String> al2 : al1) {
			if (conta > 0) {
				//non considero il primo elemento (intestazione)
				retVal.add(al2);
			}
			conta++;
		}
		return retVal;
	}
	
	/** 
	 * Restituisce un flag che indica se deve essere visualizzato il messaggio di errore.
	 * @return Un flag che indica se deve essere visualizzato il messaggio di errore.
	 */
	public boolean isVisualizzaMessaggioErrore() {
		return getRicerca().getRisultatiVis() == null || getRicerca().getRisultatiVis().size() == 0;
	}
	
	/**
	 * Restituisce il numero totale dei record trovati nell'archivio corrente.
	 * @return Un oggetto Integer che rappresenta il numero totale dei record trovati nell'archivio corrente.
	 */
	public Integer getRecordCount() {
		return getRicerca().getRecordCount().get(new Integer(idxArchivio).intValue());
	}
	
	/**
	 * Effettua la paginazione, ricercando nel DB solo i record con i numeri di riga (ROWNUM) previsti per la pagina richiesta.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette la permanenza nella popup 
	 * di visualizzazione dei dati.
	 */
	public String faiRicercaPag() {		
		try {
			RicercheModel rm = new RicercheModel();
			int paginaInt = new Integer(pagina).intValue();
			int rigaDa = ((paginaInt - 1) * RicercheBB.righePerPagina) + 1;
			int rigaA = ((paginaInt - 1) * RicercheBB.righePerPagina) + RicercheBB.righePerPagina;
			Ricerca ricerca = getRicerca();
			rm.getRisultatiPag(ricerca, new Integer(idxArchivio), rigaDa, rigaA);
			rm.getRisultatiVis(ricerca);
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile effettuare la paginazione"));
			e.printStackTrace();
		}
		return "vaiAVisualizzaDati";
	}
	
	/**
	 * Restituisce una String contenente il carattere ">" per la visualizzazione (senza errori di compilazione) nella popup.
	 * @return Un oggetto String contenente il carattere ">".
	 */
	public String getSegnoMaggiore() {
		return ">";
	}
	
	/**
	 * Restituisce una String contenente il carattere "<" per la visualizzazione (senza errori di compilazione) nella popup.
	 * @return Un oggetto String contenente il carattere "<".
	 */
	public String getSegnoMinore() {
		return "<";
	}
	
}
