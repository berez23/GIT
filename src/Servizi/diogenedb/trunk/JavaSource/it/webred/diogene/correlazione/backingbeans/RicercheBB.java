package it.webred.diogene.correlazione.backingbeans;

import it.webred.diogene.correlazione.beans.Ricerca;
import it.webred.diogene.correlazione.beans.Tema;
import it.webred.diogene.correlazione.beans.TipoEtichetta;
import it.webred.diogene.correlazione.model.RicercheModel;
import it.webred.diogene.metadata.beans.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * Backing bean utilizzato per la gestione di una o più ricerche effettuate negli archivi in fase di visualizzazione dati da 
 * archivi.
 * @author Filippo Mazzini
 * @version $Revision: 1.4 $ $Date: 2007/11/22 15:59:57 $
 */ 
public class RicercheBB
{
	
	/**
	 * ArrayList di oggetti di classe Ricerca, che contiene i dati delle ricerche effettuate negli archivi consultabili.
	 */
	private ArrayList<Ricerca> ricerche;
	/**
	 * Numero delle popup che devono essere aperte al load della pagina (diverso da zero solo a seguito di ricerca).
	 */
	private Integer numeroPopup;
	/**
	 *	Oggetto di classe RicercheModel utilizzato per l'accesso ai dati del DB.
	 */
	private RicercheModel rm;
	/**
	 *	Costante che specifica quante righe per pagina devono essere mostrate nelle popup di visualizzazione dati ricerca.
	 */
	public static final int righePerPagina = 10;	
	/**
	 * Indice della ricerca corrente (in base 0).
	 */
	private Integer idxRicerca;
	/**
	 * ArrayList di oggetti SelectItem contenente la lista degli operatori.
	 */
	private ArrayList<SelectItem> operatori;

	/**
	*	Costruttore che crea un RicercheBB vuoto.
	*/
	public RicercheBB() {
		super();
		rm = new RicercheModel();
		ricerche = new ArrayList<Ricerca>();
		numeroPopup = new Integer(0);
		operatori = rm.getOperatori();
	}
	
	/**
	 * Elimina il bean Ricerche dalla SessionMap perché venga chiamato di nuovo il suo costruttore 
	 * ripristinando la situazione iniziale (come alla prima apertura della pagina di valorizzazione chiave).
	 */
	private void cancellaForm() {
		/*elimino il bean Ricerche dalla SessionMap perché venga chiamato di nuovo il suo costruttore
		 ripristinando la situazione iniziale (come alla prima apertura della pagina di valorizzazione chiave)*/
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("Ricerche");
	}
	
	/**
	 * Effettua la ricerca nel DB per i parametri specificati, salvandone i risultati in un oggetto di classe Ricerca che 
	 * viene aggiunto in coda all'ArrayList ricerche.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette la permanenza nella pagina 
	 * di valorizzazione della chiave.
	 */
	public String faiRicerca() {
		try {
			Ricerca ricerca = new Ricerca();
			Map sessMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			ricerca.setDataRicerca(new Date());
			Tema temaToClone = ((ListaTemiBB)sessMap.get("ListaTemi")).getTemaSel().get(0);
			Tema tema = (Tema)temaToClone.clone();
			ricerca.setTema(tema);
			ArrayList<TipoEtichetta> tipiEtichettaToClone = ((ListaTipiEtichettaBB)sessMap.get("ListaTipiEtichetta")).getSelTipiEtichetta();
			ArrayList<TipoEtichetta> tipiEtichetta = new ArrayList<TipoEtichetta>();
			for (TipoEtichetta tipoEtichettaToClone : tipiEtichettaToClone) {
				tipiEtichetta.add((TipoEtichetta)tipoEtichettaToClone.clone());
			}
			ricerca.setTipiEtichetta(tipiEtichetta);
			ArrayList<Table> archiviToClone = ((ListaArchiviBB)sessMap.get("ListaArchivi")).getSelArchivi();
			ArrayList<Table> archivi = new ArrayList<Table>();
			for (Table archivioToClone : archiviToClone) {
				archivi.add((Table)archivioToClone.clone());
			}
			ricerca.setArchivi(archivi);
			numeroPopup = new Integer(archivi.size());
			rm.getRisultati(ricerca);
			rm.getRisultatiVis(ricerca);
			ricerche.add(ricerca);
			idxRicerca = ricerche.size() - 1;
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile effettuare la ricerca"));
			e.printStackTrace();
		}
		return "vaiAValorizzaChiave";
	}
	
	/**
	 * Effettua la ricerca alternativa nel DB per i parametri specificati, salvandone i risultati in un oggetto di classe Ricerca che 
	 * viene aggiunto in coda all'ArrayList ricerche.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette la permanenza nella pagina 
	 * di valorizzazione della chiave.
	 */
	public String faiRicercaAlternativa() {
		try {
			Ricerca ricerca = new Ricerca();
			Map sessMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			ricerca.setDataRicerca(new Date());
			Tema temaToClone = ((ListaTemiBB)sessMap.get("ListaTemi")).getTemaSel().get(0);
			Tema tema = (Tema)temaToClone.clone();
			ricerca.setTema(tema);
			ArrayList<TipoEtichetta> selTipiEtichettaAltraQuery = new ArrayList<TipoEtichetta>();
			// verifico quali sono i tipi etichetta selezionati (check box)
			ArrayList<TipoEtichetta> tipiEtichettaAltraQuery = ((ListaArchiviBB)sessMap.get("ListaArchivi")).getTipiEtichettaAltraQuery();
			for (TipoEtichetta tipoEtichettaAltraQuery : tipiEtichettaAltraQuery) {
				if (tipoEtichettaAltraQuery.isSelezionato()) {
					selTipiEtichettaAltraQuery.add((TipoEtichetta)tipoEtichettaAltraQuery.clone());
				}
			}
			ricerca.setTipiEtichetta(selTipiEtichettaAltraQuery);
			ArrayList<Table> archivi = new ArrayList<Table>();
			// l'unico archivio è quello selezionato nella pagina per la query alternativa
			archivi.add((Table)((ListaArchiviBB)sessMap.get("ListaArchivi")).getArchivioAltraQuery().clone());
			ricerca.setArchivi(archivi);
			numeroPopup = new Integer(archivi.size()); //è comunque sempre 1
			rm.getRisultati(ricerca);
			rm.getRisultatiVis(ricerca);
			ricerche.add(ricerca);
			idxRicerca = ricerche.size() - 1;
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile effettuare la ricerca alternativa"));
			e.printStackTrace();
		}
		return "vaiAValorizzaChiave";
	}
	
	/**
	 * Resta nella pagina di valorizzazione della chiave caricando la tabella da utilizzare per la ricerca alternativa.
	 * @return  Valore di tipo String che, secondo la configurazione della navigazione, permette la permanenza nella pagina 
	 * di valorizzazione della chiave.
	 */
	public String caricaRicercaAlternativa() {
		return "vaiAValorizzaChiave";
	}
	
	/**
	 * Effettua il rientro alla pagina di visualizzazione lista archivi.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di visualizzazione lista archivi.
	 */
	public String esci() {
		cancellaForm();
		cancellaValoriChiave();
		cancellaArchivioAltraQuery();
		return "vaiAListaArchivi";
	}
	
	/**
	 * Cancella dalla memoria le ricerche eventualmente effettuate e torna alla pagina di selezione tema.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di selezione tema.
	 */
	public String cancellaRicercheETornaATemi() {
		cancellaForm();
		numeroPopup = new Integer(0);
		Map map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		if (map.containsKey("ListaTemi")) {
			map.put("renderedBack", new Boolean(((ListaTemiBB)map.get("ListaTemi")).isRenderedBack()));
			map.remove("ListaTemi");
		}
		if (map.containsKey("ListaTipiEtichetta"))
			map.remove("ListaTipiEtichetta");
		if (map.containsKey("ListaArchivi"))
			map.remove("ListaArchivi");
		return "vaiAListaTemi";
	}
	
	/**
	 * Chiude la ricerca alternativa rendendo non visibile la tabella relativa.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette la permanenza nella pagina 
	 * di valorizzazione della chiave.
	 */
	public String chiudiRicercaAlternativa() {
		cancellaArchivioAltraQuery();
		return "vaiAValorizzaChiave";
	}
	
	/**
	 * Metodo privato che cancella dall'ArrayList selTipiEtichetta di ListaTipiEtichettaBB i valori inseriti come chiave.
	 */
	private void cancellaValoriChiave() {
		Map sessMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		ArrayList<TipoEtichetta> tipiEtichetta = ((ListaTipiEtichettaBB)sessMap.get("ListaTipiEtichetta")).getSelTipiEtichetta();
		for (TipoEtichetta tipoEtichetta : tipiEtichetta) {
			tipoEtichetta.setChiave(null);
		}
	}
	
	/**
	 * Metodo privato che cancella idArchivioAltraQuery (identificativo archivio per query alternativa) di ListaArchiviBB.
	 */
	private void cancellaArchivioAltraQuery() {
		Map sessMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		((ListaArchiviBB)sessMap.get("ListaArchivi")).setIdArchivioAltraQuery(null);		
	}

	public ArrayList<Ricerca> getRicerche()
	{
		return ricerche;
	}

	public void setRicerche(ArrayList<Ricerca> ricerche)
	{
		this.ricerche = ricerche;
	}

	public Integer getNumeroPopup()
	{
		return numeroPopup;
	}

	public void setNumeroPopup(Integer numeroPopup)
	{
		this.numeroPopup = numeroPopup;
	}

	public Integer getIdxRicerca()
	{
		return idxRicerca;
	}

	public void setIdxRicerca(Integer idxRicerca)
	{
		this.idxRicerca = idxRicerca;
	}

	public ArrayList<SelectItem> getOperatori() {
		return operatori;
	}

	public void setOperatori(ArrayList<SelectItem> operatori) {
		this.operatori = operatori;
	}

	public String getSeparaParams() {
		return RicercheModel.separaParams;
	}
	
}
