package it.webred.diogene.correlazione.backingbeans;

import java.util.ArrayList;
import java.util.Map;

import it.webred.diogene.correlazione.beans.Tema;
import it.webred.diogene.correlazione.model.TemiModel;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * Backing bean utilizzato per la gestione (visualizzazione in lista, inserimento, modifica, cancellazione) dei temi.
 * @author Filippo Mazzini
 * @version $Revision: 1.2 $ $Date: 2007/09/26 15:06:40 $
 */
public class TemiBB
{

	/**
	 * ArrayList di oggetti di classe Tema, utilizzato per il caricamento della lista dei temi.
	 */
	private ArrayList<Tema> temi;
	/**
	 * Rappresenta la dimensione (size) dell'ArrayList temi
	 */
	private Integer numeroTemi = new Integer(0);
	/**
	 * Rappresenta l'identificativo del tema selezionato (per modifica o cancellazione)
	 */
	private String idTemaSel = "-1";
	/**
	 * Flag che indica se alla pressione del pulsante "Indietro" deve essere visualizzato un messaggio di 
	 * conferma (accade se l'utente ha modificato dei dati nella pagina).
	 */
	private boolean needsConfirm = false;
	/**
	 *	Oggetto di classe TemiModel utilizzato per l'accesso ai dati del DB (query di selezione, inserimento, 
	 *	modifica, cancellazione).
	 */
	private TemiModel tm;
	/**
	 * Nome del nuovo tema da inserire.
	 */
	private String nameNuovo = "";
	/**
	 * Stringa che indica il nome (tipo) del browser.
	 */
	private String navigatorAppName = "";	
	/**
	 * Stringa che indica lo stile CSS della tabella di inserimento nuovo tema; indica se tale tabella deve essere visibile o no.
	 */
	private String displayNuovo = "none";
	/**
	 * Costante di tipo String per il valore assunto da navigatorAppName se il browser è Microsoft Internet Explorer.
	 */
	private final String IE = "Microsoft Internet Explorer";
	
	/**
	*	Costruttore che crea un TemiBB vuoto.
	*/
	public TemiBB() {
		super();
		tm = new TemiModel();
		caricaTemi();
	}
	
	/**
	 * Metodo privato che carica la lista dei temi.
	 */
	private void caricaTemi() {
		try {
			setTemi(tm.getTemi());
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare la lista dei temi"));
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
	 * Visualizza la tabella di inserimento nuovo tema nella pagina di gestione temi.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Inserisci nuovo tema" nella pagina di gestione 
	 * temi.
	 */
	public void inserisci(ActionEvent event){
		nameNuovo = "";
		if (navigatorAppName.equals(IE)) {
			displayNuovo = "block";
		}else{
			displayNuovo = "table";
		}
		//ricarico la lista dei temi per escludere eventuali modifiche effettuate in precedenza
		caricaTemi();
	}
	
	/**
	 * Predispone la lista dei temi per la modifica del tema selezionato.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Modifica" nella tabella di lista temi.
	 */
	public void modifica(ActionEvent event){
		//ricarico la lista dei temi per escludere eventuali modifiche effettuate in precedenza
		caricaTemi();
	}
	
	/** 
	 * Nasconde la tabella di inserimento nuovo tema nella pagina di gestione temi.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Annulla" nella tabella di inserimento nuovo tema.
	 */
	public void annullaInserimento(ActionEvent event){
		nameNuovo = "";
		displayNuovo = "none";
	}
	
	/** 
	 * Annulla la richiesta di modifica di un tema riportando la pagina di gestione temi alla situazione iniziale.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Annulla" nella tabella di lista temi (in fase di modifica).
	 */
	public void annullaModifica(ActionEvent event){
		cancellaForm();
	}
	
	/**
	 * Effettua il salvataggio del nuovo tema nel DB, tramite la chiamata al metodo delegato nell'oggetto TemiModel. 
	 * Se si verifica un errore visualizza un messaggio nella pagina.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette la permanenza nella 
	 * pagina di gestione temi.
	 */
	public String salvaNuovo(){
		try {
			Tema temaNuovo = new Tema();
			temaNuovo.setName(nameNuovo.trim());
			String message = tm.salvaOAggiornaTema(temaNuovo);
			if (message != null && !message.equals("")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
				return "vaiAGestioneTemi";
			}
			cancellaForm();			
		} catch (Exception e) {
			String message = "Errore nel salvataggio dei dati: ";
			message += e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			e.printStackTrace();
		}
		return "vaiAGestioneTemi";
	}
	
	/**
	 * Effettua il salvataggio del tema modificato nel DB, tramite la chiamata al metodo delegato nell'oggetto TemiModel. 
	 * Se si verifica un errore visualizza un messaggio nella pagina.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette la permanenza nella 
	 * pagina di gestione temi.
	 */
	public String salvaMod(){
		try {
			Tema temaMod = new Tema();
			temaMod.setId(new Long(idTemaSel));
			String name = "";
			for (Tema tema : temi) {
				if (tema.getId().longValue() == temaMod.getId().longValue()) {
					name = tema.getName().trim();
					break;
				}
			}
			temaMod.setName(name);
			String message = tm.salvaOAggiornaTema(temaMod);
			if (message != null && !message.equals("")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
				return "vaiAGestioneTemi";
			}
			cancellaForm();			
		} catch (Exception e) {
			String message = "Errore nel salvataggio dei dati: ";
			message += e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			e.printStackTrace();
		}
		return "vaiAGestioneTemi";
	}
	
	/**
	 * Cancella il tema selezionato dal DB, tramite la chiamata al metodo delegato nell'oggetto TemiModel. 
	 * Se si verifica un errore visualizza un messaggio nella pagina.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette la permanenza nella 
	 * pagina di gestione temi.
	 */
	public String cancella() {
		try {
			tm.cancellaTema(new Long(idTemaSel));
			cancellaForm();			
		} catch (Exception e) {
			String message = "Errore nella cancellazione: ";
			message += e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			e.printStackTrace();
		}
		return "vaiAGestioneTemi";
	}
	
	/**
	 * Elimina il bean Temi dalla SessionMap perché venga chiamato di nuovo il suo costruttore 
	 * ripristinando la situazione iniziale (come alla prima apertura della pagina).
	 */
	private void cancellaForm() {
		/*elimino il bean Temi dalla SessionMap perché venga chiamato di nuovo il suo costruttore
		 ripristinando la situazione iniziale (come alla prima apertura della pagina)*/
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("Temi");
	}	
	
	public ArrayList<Tema> getTemi()
	{
		return temi;
	}

	public void setTemi(ArrayList<Tema> temi)
	{
		this.temi = temi;
		numeroTemi = new Integer(temi.size());
	}

	public Integer getNumeroTemi()
	{
		return numeroTemi;
	}

	public void setNumeroTemi(Integer numeroTemi)
	{
		this.numeroTemi = numeroTemi;
	}

	public String getIdTemaSel()
	{
		return idTemaSel;
	}

	public void setIdTemaSel(String idTemaSel)
	{
		this.idTemaSel = idTemaSel;
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
