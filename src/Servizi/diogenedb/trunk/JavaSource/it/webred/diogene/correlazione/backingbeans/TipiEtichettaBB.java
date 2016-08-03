package it.webred.diogene.correlazione.backingbeans;

import java.util.ArrayList;
import java.util.Map;

import it.webred.diogene.correlazione.beans.TipoEtichetta;
import it.webred.diogene.correlazione.model.TipiEtichettaModel;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 * Backing bean utilizzato per la gestione (visualizzazione in lista, inserimento, modifica, cancellazione) dei tipi etichetta.
 * @author Filippo Mazzini
 * @version $Revision: 1.5 $ $Date: 2007/09/25 15:32:05 $
 */ 
public class TipiEtichettaBB
{

	/**
	 * ArrayList di oggetti di classe SelectItem, utilizzato per il caricamento delle combo box dei temi.
	 */
	private ArrayList<SelectItem> temi;
	/**
	 * ArrayList di oggetti di classe TipoEtichetta, utilizzato per il caricamento della lista dei tipi etichetta.
	 */
	private ArrayList<TipoEtichetta> tipiEtichetta;
	/**
	 * Rappresenta la dimensione (size) dell'ArrayList tipiEtichetta
	 */
	private Integer numeroTipiEtichetta = new Integer(0);
	/**
	 * Rappresenta l'identificativo del tipo etichetta selezionato (per modifica o cancellazione)
	 */
	private String idTipoEtichettaSel = "-1";
	/**
	 * Flag che indica se alla pressione del pulsante "Indietro" deve essere visualizzato un messaggio di 
	 * conferma (accade se l'utente ha modificato dei dati nella pagina).
	 */
	private boolean needsConfirm = false;
	/**
	 *	Oggetto di classe TipiEtichettaModel utilizzato per l'accesso ai dati del DB (query di selezione, inserimento, 
	 *	modifica, cancellazione).
	 */
	private TipiEtichettaModel tem;
	/**
	 * Nome del nuovo tipo etichetta da inserire.
	 */
	private String nameNuovo = "";
	/**
	 * Identificativo del tema per cui si filtrano i tipi etichetta.
	 */
	private Long idTemaFiltro;
	/**
	 * Identificativo del tema a cui è associato il nuovo tipo etichetta da inserire.
	 */
	private Long idTemaNuovo;
	/**
	 * Stringa che indica il nome (tipo) del browser.
	 */
	private String navigatorAppName = "";	
	/**
	 * Stringa che indica lo stile CSS della tabella di inserimento nuovo tipo etichetta; indica se tale tabella deve essere visibile o no.
	 */
	private String displayNuovo = "none";
	/**
	 * Costante di tipo String per il valore assunto da navigatorAppName se il browser è Microsoft Internet Explorer.
	 */
	private final String IE = "Microsoft Internet Explorer";
	
	/**
	*	Costruttore che crea un TipiEtichettaBB vuoto.
	*/
	public TipiEtichettaBB() {
		super();
		tem = new TipiEtichettaModel();
		Map sessMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Long idTemaFiltroOld = (Long)sessMap.get("idTemaFiltroOld");
		if (idTemaFiltroOld == null) {
			idTemaFiltro = new Long(-1);
		}else{
			idTemaFiltro = new Long(idTemaFiltroOld.longValue());
			sessMap.remove("idTemaFiltroOld");
		}		
		caricaListe();
	}
	
	/**
	 * Metodo privato che carica le liste dei temi e dei tipi etichetta, mediante chiamata ai due metodi privati appositi.
	 */
	private void caricaListe() {
		caricaTemi();
		caricaTipiEtichetta();
	}
	
	/**
	 * Metodo privato che carica la lista dei temi.
	 */
	private void caricaTemi() {
		try {
			setTemi(tem.getTemi());			
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare la lista dei temi"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo privato che carica la lista dei tipi etichetta.
	 */
	private void caricaTipiEtichetta() {
		try {
			setTipiEtichetta(tem.getTipiEtichetta(getIdTemaSel()));
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare la lista dei tipi etichetta"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Elimina il bean corrente dal FacesContext tramite la chiamata a cancellaForm(). Quindi effettua il rientro alla 
	 * pagina di menu correlazione.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di menu correlazione.
	 */
	public String esci() {
		cancellaForm();
		return "vaiAMenuCorrelazione";
	}
	
	/** 
	 * Visualizza la tabella di inserimento nuovo tipo etichetta nella pagina di gestione tipi etichetta.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Inserisci nuovo tipo etichetta" nella pagina di gestione 
	 * tipi etichetta.
	 */
	public void inserisci(ActionEvent event){
		nameNuovo = "";
		idTemaNuovo = idTemaFiltro == null ? new Long(-1) : new Long(idTemaFiltro.longValue());
		if (navigatorAppName.equals(IE)) {
			displayNuovo = "block";
		}else{
			displayNuovo = "table";
		}
		//ricarico la lista dei tipi etichetta per escludere eventuali modifiche effettuate in precedenza
		caricaTipiEtichetta();
	}
	
	/**
	 * Predispone la lista dei tipi etichetta per la modifica del tipo etichetta selezionato.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Modifica" nella tabella di lista tipi etichetta.
	 */
	public void modifica(ActionEvent event){
		//ricarico la lista dei tipi etichetta per escludere eventuali modifiche effettuate in precedenza
		caricaTipiEtichetta();
	}
	
	/**
	 * Filtra i tipi etichetta secondo il tema selezionato nella combo box di filtro per tema.
	 * @param event L'oggetto che rappresenta l'evento di modifica della selezione nella combo box di filtro per tema.
	 */
	public void filtra(ValueChangeEvent event){
		//ricarico la lista dei tipi etichetta secondo il tema selezionato nella combo box di filtro
		idTemaFiltro = event.getNewValue() == null ? new Long(-1) : (Long)event.getNewValue();
		caricaTipiEtichetta();
	}
	
	/** 
	 * Nasconde la tabella di inserimento nuovo tipo etichetta nella pagina di gestione tipi etichetta.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Annulla" nella tabella di inserimento nuovo tipo etichetta.
	 */
	public void annullaInserimento(ActionEvent event){
		nameNuovo = "";
		idTemaNuovo = idTemaFiltro == null ? new Long(-1) : new Long(idTemaFiltro.longValue());
		displayNuovo = "none";
	}
	
	/** 
	 * Annulla la richiesta di modifica di un tipo etichetta riportando la pagina di gestione tipi etichetta alla situazione iniziale.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Annulla" nella tabella di lista tipi etichetta (in fase di modifica).
	 */
	public void annullaModifica(ActionEvent event){
		cancellaForm();
	}
	
	/**
	 * Effettua il salvataggio del nuovo tipo etichetta nel DB, tramite la chiamata al metodo delegato nell'oggetto TipiEtichettaModel. 
	 * Se si verifica un errore visualizza un messaggio nella pagina.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette la permanenza nella 
	 * pagina di gestione tipi etichetta.
	 */
	public String salvaNuovo(){
		try {
			TipoEtichetta tipoEtichettaNuovo = new TipoEtichetta();
			tipoEtichettaNuovo.setName(nameNuovo.trim());
			if (idTemaNuovo != null && idTemaNuovo.longValue() != -1) {
				tipoEtichettaNuovo.setFkDvTema(idTemaNuovo);
			}
			String message = tem.salvaOAggiornaTipoEtichetta(tipoEtichettaNuovo);
			if (message != null && !message.equals("")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
				return "vaiAGestioneTipiEtichetta";
			}
			//cancello ma mantengo il tema selezionato per il filtro
			Long idTemaFiltroOld = new Long(idTemaFiltro.longValue());
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idTemaFiltroOld", idTemaFiltroOld);
			cancellaForm();			
		} catch (Exception e) {
			String message = "Errore nel salvataggio dei dati: ";
			message += e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			e.printStackTrace();
		}
		return "vaiAGestioneTipiEtichetta";
	}
	
	/**
	 * Effettua il salvataggio del tipo etichetta modificato nel DB, tramite la chiamata al metodo delegato nell'oggetto TipiEtichettaModel. 
	 * Se si verifica un errore visualizza un messaggio nella pagina.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette la permanenza nella 
	 * pagina di gestione tipi etichetta.
	 */
	public String salvaMod(){
		try {
			TipoEtichetta tipoEtichettaMod = new TipoEtichetta();
			tipoEtichettaMod.setId(new Long(idTipoEtichettaSel));
			String name = "";
			Long fkDvTema = null;
			for (TipoEtichetta tipoEtichetta : tipiEtichetta) {
				if (tipoEtichetta.getId().longValue() == tipoEtichettaMod.getId().longValue()) {
					name = tipoEtichetta.getName().trim();
					fkDvTema = tipoEtichetta.getFkDvTema();
					break;
				}
			}
			tipoEtichettaMod.setName(name);
			if (fkDvTema != null && fkDvTema.longValue() != -1) {
				tipoEtichettaMod.setFkDvTema(fkDvTema);
			}
			String message = tem.salvaOAggiornaTipoEtichetta(tipoEtichettaMod);
			if (message != null && !message.equals("")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
				return "vaiAGestioneTipiEtichetta";
			}
			//cancello ma mantengo il tema selezionato per il filtro
			Long idTemaFiltroOld = new Long(idTemaFiltro.longValue());
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idTemaFiltroOld", idTemaFiltroOld);
			cancellaForm();			
		} catch (Exception e) {
			String message = "Errore nel salvataggio dei dati: ";
			message += e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			e.printStackTrace();
		}
		return "vaiAGestioneTipiEtichetta";
	}
	
	/**
	 * Cancella il tipo etichetta selezionato dal DB, tramite la chiamata al metodo delegato nell'oggetto TipiEtichettaModel. 
	 * Se si verifica un errore visualizza un messaggio nella pagina.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette la permanenza nella 
	 * pagina di gestione tipi etichetta.
	 */
	public String cancella() {
		try {
			tem.cancellaTipoEtichetta(new Long(idTipoEtichettaSel));
			//cancello ma mantengo il tema selezionato per il filtro
			Long idTemaFiltroOld = new Long(idTemaFiltro.longValue());
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idTemaFiltroOld", idTemaFiltroOld);
			cancellaForm();		
		} catch (Exception e) {
			String message = "Errore nella cancellazione: ";
			message += e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			e.printStackTrace();
		}
		return "vaiAGestioneTipiEtichetta";
	}
	
	/**
	 * Elimina il bean TipiEtichetta dalla SessionMap perché venga chiamato di nuovo il suo costruttore 
	 * ripristinando la situazione iniziale (come alla prima apertura della pagina).
	 */
	private void cancellaForm() {
		/*elimino il bean TipiEtichetta dalla SessionMap perché venga chiamato di nuovo il suo costruttore
		 ripristinando la situazione iniziale (come alla prima apertura della pagina)*/
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("TipiEtichetta");
	}
	
	/**
	 * Metodo che restituisce l'identificativo del tema per cui si ricercano i tipi etichetta associati (può 
	 * essere ridefinito nelle sottoclassi).
	 * @return L'identificativo del tema per cui si ricercano i tipi etichetta associati.
	 */
	protected Long getIdTemaSel() {
		return idTemaFiltro == null || idTemaFiltro.longValue() == - 1? null : idTemaFiltro;
	}
	
	public ArrayList<SelectItem> getTemi()
	{
		return temi;
	}

	public void setTemi(ArrayList<SelectItem> temi)
	{
		this.temi = temi;
	}

	public ArrayList<TipoEtichetta> getTipiEtichetta()
	{
		return tipiEtichetta;
	}

	public void setTipiEtichetta(ArrayList<TipoEtichetta> tipiEtichetta)
	{
		this.tipiEtichetta = tipiEtichetta;
		numeroTipiEtichetta = new Integer(tipiEtichetta.size());
	}

	public Integer getNumeroTipiEtichetta()
	{
		return numeroTipiEtichetta;
	}

	public void setNumeroTipiEtichetta(Integer numeroTipiEtichetta)
	{
		this.numeroTipiEtichetta = numeroTipiEtichetta;
	}

	public String getIdTipoEtichettaSel()
	{
		return idTipoEtichettaSel;
	}

	public void setIdTipoEtichettaSel(String idTipoEtichettaSel)
	{
		this.idTipoEtichettaSel = idTipoEtichettaSel;
	}

	public boolean isNeedsConfirm() {
		return needsConfirm;
	}

	public void setNeedsConfirm(boolean needsConfirm) {
		this.needsConfirm = needsConfirm;
	}
	
	public String getNameNuovo()
	{
		return nameNuovo;
	}

	public void setNameNuovo(String nameNuovo)
	{
		this.nameNuovo = nameNuovo;
	}
	
	public Long getIdTemaFiltro()
	{
		return idTemaFiltro;
	}

	public void setIdTemaFiltro(Long idTemaFiltro)
	{
		this.idTemaFiltro = idTemaFiltro;
	}

	public Long getIdTemaNuovo()
	{
		return idTemaNuovo;
	}

	public void setIdTemaNuovo(Long idTemaNuovo)
	{
		this.idTemaNuovo = idTemaNuovo;
	}

	public String getNavigatorAppName()
	{
		return navigatorAppName;
	}

	public void setNavigatorAppName(String navigatorAppName)
	{
		this.navigatorAppName = navigatorAppName;
	}

	public String getDisplayNuovo()
	{
		return displayNuovo;
	}

	public void setDisplayNuovo(String displayNuovo)
	{
		this.displayNuovo = displayNuovo;
	}
	
}
