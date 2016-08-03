package it.webred.diogene.correlazione.backingbeans;

import it.webred.diogene.correlazione.beans.TipoEtichetta;
import it.webred.diogene.correlazione.model.ListaArchiviModel;
import it.webred.diogene.metadata.beans.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Backing bean utilizzato per la visualizzazione della lista degli archivi consultabili per uno o più tipi etichetta 
 * selezionati, in fase di visualizzazione dei dati da archivi.
 * @author Filippo Mazzini
 * @version $Revision: 1.3 $ $Date: 2007/09/24 15:01:59 $
 */ 
public class ListaArchiviBB
{
	
	/**
	 * ArrayList di oggetti di classe Table, utilizzato per il caricamento della lista degli archivi.
	 */
	private ArrayList<Table> archivi;
	/**
	 * Rappresenta la dimensione (size) dell'ArrayList archivi.
	 */
	private Integer numeroArchivi = new Integer(0);
	/**
	 * ArrayList di oggetti di classe Table, che rappresenta gli archivi selezionati nella lista relativa.
	 */
	private ArrayList<Table> selArchivi;
	/**
	 * Rappresenta la dimensione (size) dell'ArrayList selArchivi
	 */
	private Integer numeroSelArchivi = new Integer(0);
	/**
	 * ArrayList di oggetti di classe Table, che rappresenta gli archivi non selezionati nella lista relativa.
	 */
	private ArrayList<Table> altriArchivi;
	/**
	 * Costante di tipo String utilizzata per identificare, tra i parametri della request, quelli che rappresentano la selezione 
	 * degli archivi nella lista relativa (la cui chiave comincia per...).
	 */
	private final String SEL_STARTS_WITH = "form:listaArchivi_";
	/**
	 * Costante di tipo String utilizzata per identificare, tra i parametri della request, quelli che rappresentano la selezione 
	 * degli archivi nella lista relativa (la cui chiave termina con...).
	 */
	private final String SEL_ENDS_WITH = ":selArchivio";
	/**
	 * L'etichetta visualizzata sopra la tabella di valorizzazione della chiave, nella pagina relativa.
	 */
	private String etichetta;
	/**
	 *	Oggetto di classe ListaArchiviModel utilizzato per l'accesso ai dati del DB (caricamento della lista degli archivi).
	 */
	private ListaArchiviModel lam;
	/**
	 *	Identificativo dell'archivio selezionato per effettuare la query alternativa.
	 */
	private Long idArchivioAltraQuery;
	/**
	 * ArrayList di oggetti di classe TipoEtichetta, che rappresenta l'elenco di tutti i tipi etichetta associati all'archivio selezionato, 
	 * per effettuare la query alternativa.
	 */
	ArrayList<TipoEtichetta> tipiEtichettaAltraQuery = new ArrayList<TipoEtichetta>();
	/**
	 * Rappresenta la dimensione (size) dell'ArrayList tipiEtichettaAltraQuery.
	 */
	private Integer numeroTipiEtichettaAltraQuery = new Integer(0);
	/**
	 * Flag che indica se l'ArrayList tipiEtichettaAltraQuery deve essere ricaricato dal DB.
	 */
	private boolean ricaricaTipiEtichettaAltraQuery;
	
	/**
	*	Costruttore che crea un ListaArchiviBB vuoto.
	*/
	public ListaArchiviBB() {
		super();
		lam = new ListaArchiviModel();
		caricaArchivi();
	}
	
	/**
	 * Metodo privato che carica la lista degli archivi.
	 */
	private void caricaArchivi() {
		try {
			Map map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			ArrayList<TipoEtichetta> selTipiEtichetta = ((ListaTipiEtichettaBB)map.get("ListaTipiEtichetta")).getSelTipiEtichetta();
			setArchivi(lam.getArchivi(selTipiEtichetta));
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare la lista degli archivi"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Elimina il bean ListaArchivi dalla SessionMap perché venga chiamato di nuovo il suo costruttore 
	 * ripristinando la situazione iniziale (come alla prima apertura della pagina).
	 */
	private void cancellaForm() {
		/*elimino il bean ListaArchivi dalla SessionMap perché venga chiamato di nuovo il suo costruttore
		 ripristinando la situazione iniziale (come alla prima apertura della pagina)*/
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("ListaArchivi");
	}	
	
	/**
	 * Effettua il rientro alla pagina di visualizzazione lista tipi etichetta.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di visualizzazione lista tipi etichetta.
	 */
	public String esci() {
		cancellaForm();
		cancellaValoriChiave();
		return "vaiAListaTipiEtichetta";
	}
	
	/**
	 * Salva la selezione degli archivi effettuata nella lista relativa e chiama la pagina di valorizzazione della chiave.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della 
	 * pagina di valorizzazione della chiave.
	 */
	public String seleziona() {
		Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		Iterator it = map.keySet().iterator();
		ArrayList<Integer> arrList = new ArrayList<Integer>();
		while (it.hasNext()) {
			String key = (String)it.next();
			if (key.startsWith(SEL_STARTS_WITH) && key.endsWith(SEL_ENDS_WITH)) {
				Integer index = new Integer(key.substring(SEL_STARTS_WITH.length(), key.length() - SEL_ENDS_WITH.length()));
				arrList.add(index);
			}
		}
		Object[] objArr = arrList.toArray();
		Integer[] arr = new Integer[objArr.length];
		for (int i = 0; i < objArr.length; i++) {
			arr[i] = (Integer)objArr[i];
		}
		Arrays.sort(arr);
		selArchivi = new ArrayList<Table>();
		altriArchivi = new ArrayList<Table>();
		for (Integer index : arr) {
			selArchivi.add(archivi.get(index.intValue()));
		}
		numeroSelArchivi = selArchivi.size();
		etichetta = "Inserire un valore per ";
		Map sessMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		ListaTipiEtichettaBB listaTipiEtichettaBB = (ListaTipiEtichettaBB)sessMap.get("ListaTipiEtichetta");
		etichetta += listaTipiEtichettaBB.getNumeroSelTipiEtichetta().intValue() == 1 ? "il" : "ogni";
		etichetta += " tipo etichetta per effettuare la ricerca.";
		for (int i = 0; i < archivi.size(); i++) {
			boolean sel = false;
			for (Integer index : arr) {
				if (index.intValue() == i) {
					sel = true;
					break;
				}
			}
			if (!sel) {
				altriArchivi.add(archivi.get(i));
			}
		}
		try {
			lam.setFormatoEtichette(listaTipiEtichettaBB.getSelTipiEtichetta(), selArchivi);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile valorizzare il formato dei tipi etichetta"));
			e.printStackTrace();
		}
		return "vaiAValorizzaChiave";
	}
	
	/** 
	 * Restituisce un oggetto di classe Table che rappresenta l'archivio selezionato per la query alternativa.
	 * @return Un oggetto di classe Table che rappresenta l'archivio selezionato per la query alternativa.
	 */
	public Table getArchivioAltraQuery() {
		if (getIdArchivioAltraQuery() == null)
			return null;
		for (Table archivio : selArchivi) {
			if (archivio.getId().longValue() == getIdArchivioAltraQuery().longValue()) {
				return archivio;
			}
		}
		return null;
	}
	
	/**
	 * Verifica se è stata appena modificata la selezione dell'archivio per la query alternativa, chiamando 
	 * verificaRicaricaTipiEtichettaAltraQuery, che in questo caso ricarica tipiEtichettaAltraQuery, rivalorizzando anche 
	 * numeroTipiEtichettaAltraQuery. Quindi restituisce l'ArrayList di oggetti di classe TipoEtichetta che rappresenta l'elenco 
	 * di tutti i tipi etichetta associati all'archivio selezionato.
	 * @return Una ArrayList di oggetti di classe TipoEtichetta, che rappresenta l'elenco di tutti i tipi etichetta associati 
	 * all'archivio selezionato, per effettuare la query alternativa.
	 */
	public ArrayList<TipoEtichetta> getTipiEtichettaAltraQuery() {
		verificaRicaricaTipiEtichettaAltraQuery();
		return tipiEtichettaAltraQuery;
	}
	
	public void setTipiEtichettaAltraQuery(ArrayList<TipoEtichetta> tipiEtichettaAltraQuery)
	{
		this.tipiEtichettaAltraQuery = tipiEtichettaAltraQuery;
		numeroTipiEtichettaAltraQuery = new Integer(tipiEtichettaAltraQuery.size());
	}

	public boolean isRicaricaTipiEtichettaAltraQuery()
	{
		return ricaricaTipiEtichettaAltraQuery;
	}

	public void setRicaricaTipiEtichettaAltraQuery(boolean ricaricaTipiEtichettaAltraQuery)
	{
		this.ricaricaTipiEtichettaAltraQuery = ricaricaTipiEtichettaAltraQuery;
	}
	
	/**
	 * Metodo privato che verifica se è stata appena modificata la selezione dell'archivio per la query alternativa: in questo 
	 * caso imposta a false il flag ricaricaTipiEtichettaAltraQuery, ricarica tipiEtichettaAltraQuery, tramite la chiamata al 
	 * metodo a ciò delegato nell'oggetto ListaArchiviModel, e rivalorizza, di conseguenza, numeroTipiEtichettaAltraQuery.
	 */
	private void verificaRicaricaTipiEtichettaAltraQuery() {
		if (isRicaricaTipiEtichettaAltraQuery()) {
			setRicaricaTipiEtichettaAltraQuery(false);
			tipiEtichettaAltraQuery = new ArrayList<TipoEtichetta>();
			if (getIdArchivioAltraQuery() != null) {
				try {
					tipiEtichettaAltraQuery = lam.getTipiEtichettaAltraQuery(getIdArchivioAltraQuery());
					ArrayList<Table> archiviPerFormatoEtichette = new ArrayList<Table>();
					archiviPerFormatoEtichette.add(getArchivioAltraQuery());
					lam.setFormatoEtichette(tipiEtichettaAltraQuery, archiviPerFormatoEtichette);
				}catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare la lista dei tipi etichetta per la query alternativa"));
					e.printStackTrace();
				}	
			}
			numeroTipiEtichettaAltraQuery = new Integer(tipiEtichettaAltraQuery.size());		
		}
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

	public ArrayList<Table> getArchivi()
	{
		return archivi;
	}

	public void setArchivi(ArrayList<Table> archivi)
	{
		this.archivi = archivi;
		numeroArchivi = new Integer(archivi.size());
	}

	public Integer getNumeroArchivi()
	{
		return numeroArchivi;
	}

	public void setNumeroArchivi(Integer numeroArchivi)
	{
		this.numeroArchivi = numeroArchivi;
	}

	public Integer getNumeroSelArchivi()
	{
		return numeroSelArchivi;
	}

	public void setNumeroSelArchivi(Integer numeroSelArchivi)
	{
		this.numeroSelArchivi = numeroSelArchivi;
	}

	public String getEtichetta()
	{
		return etichetta;
	}

	public void setEtichetta(String etichetta)
	{
		this.etichetta = etichetta;
	}

	public ArrayList<Table> getSelArchivi()
	{
		return selArchivi;
	}

	public void setSelArchivi(ArrayList<Table> selArchivi)
	{
		this.selArchivi = selArchivi;
	}

	public ArrayList<Table> getAltriArchivi()
	{
		return altriArchivi;
	}

	public void setAltriArchivi(ArrayList<Table> altriArchivi)
	{
		this.altriArchivi = altriArchivi;
	}
	
	public Long getIdArchivioAltraQuery()
	{
		return idArchivioAltraQuery;
	}

	public void setIdArchivioAltraQuery(Long idArchivioAltraQuery)
	{
		this.idArchivioAltraQuery = idArchivioAltraQuery;
	}

	/**
	 * Verifica se è stata appena modificata la selezione dell'archivio per la query alternativa, chiamando 
	 * verificaRicaricaTipiEtichettaAltraQuery, che in questo caso ricarica tipiEtichettaAltraQuery, rivalorizzando anche 
	 * numeroTipiEtichettaAltraQuery. Quindi restituisce la dimensione dell'ArrayList tipiEtichettaAltraQuery.
	 * @return Un oggetto di classe Integer, che rappresenta la dimensione dell'ArrayList tipiEtichettaAltraQuery.
	 */
	public Integer getNumeroTipiEtichettaAltraQuery()
	{
		verificaRicaricaTipiEtichettaAltraQuery();
		return numeroTipiEtichettaAltraQuery;
	}

	public void setNumeroTipiEtichettaAltraQuery(Integer numeroTipiEtichettaAltraQuery)
	{
		this.numeroTipiEtichettaAltraQuery = numeroTipiEtichettaAltraQuery;
	}
	
}
