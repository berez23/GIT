package it.webred.diogene.metadata.backingbeans;

import java.util.*;

import javax.faces.application.*;
import javax.faces.context.*;
import javax.faces.event.*;
import javax.faces.model.*;

import it.webred.diogene.metadata.model.*;
import it.webred.diogene.metadata.beans.*;

/**
 * Backing bean utilizzato dalla pagina di mappatura tabelle.<p>
 * Contiene i metodi e i dati necessari per la mappatura su catalogo delle tabelle per un DB di origine dati, 
 * i parametri per la connessione al quale sono specificati (come default) in una tabella del DB destinazione.<p>
 * All'apertura della pagina di mappatura tabelle, tale connessione è già avvenuta senza intervento dell'utente.<p>
 * Se il DB di origine presenta una suddivisione in schemi (schemas), all'utente viene data la possibilità di selezionarne 
 * uno tramite una casella combinata (combo box), caricando nella lista di entità del DB di origine i nomi delle tabelle 
 * incluse in quello schema (altrimenti, vi saranno caricate tutte le tabelle del DB di origine).
 * Dalla pagina di mappatura tabelle ("entità"), per tutte le tabelle da aggiungere al DB di destinazione è necessario 
 * effettuare la configurazione delle colonne e delle chiavi relative, aprendo la pagina di mappatura colonne per la 
 * tabella selezionata nella lista di destinazione.<p>
 * A questo punto sarà possibile il salvataggio dei dati nel DB di destinazione; tale salvataggio riguarderà i dati  
 * immessi sia nella pagina di mappatura tabelle, che in quella di mappatura colonne.<p>
 * L'accesso ai dati non viene direttamente effettuato in questa classe ma avviene tramite l'istanza di un oggetto di 
 * classe CaricaTabelleModel.
 * @author Filippo Mazzini
 * @version $Revision: 1.5.2.1 $ $Date: 2012/01/26 16:59:39 $
 */
public class CaricaTabelleBB {
	
	/**
	 * Oggetto di classe ArrayList (di SelectItem) utilizzato per il caricamento della combo box di selezione degli schemi.
	 */
	private ArrayList<SelectItem> schemas;
	/**
	 * Oggetto di classe ArrayList (di SelectItem) utilizzato per il caricamento della lista delle entità (tabelle) nel DB 
	 * di origine.
	 */
	private ArrayList<SelectItem> fromTables;
	/**
	 * Oggetto di classe HashMap (con chiavi di classe String e valori di classe Table) utilizzato per il caricamento 
	 * della lista delle entità (tabelle) nel DB di destinazione.
	 */
	protected HashMap<String,Table> toTables;
	/**
	 * Oggetto di classe HashMap (con chiavi di classe String e valori di classe Table) di comodo; serve per il recupero 
	 * dei dati di un'entità prima aggiunta (o presente) nella lista di destinazione, poi cancellata e quindi aggiunta 
	 * nuovamente alla lista di destinazione.
	 */
	private HashMap<String,Table> toTablesTrash;
	/**
	 * Stringa che corrisponde all'elemento selezionato nella combo box degli schemi.
	 */
	private String selectedSchema;
	/**
	 * Stringa che specifica l'id del campo della pagina di mappatura tabelle che ha il focus.
	 */
	protected String focus;
	/**
	 *	Array di oggetti String corrispondente alla selezione effettuata nella lista delle entità del database di 
	 *	origine.
	 */
	private String[] selValFromTables;
	/**
	 *	Array di oggetti String corrispondente alla selezione effettuata nella lista delle entità definite nel sistema.
	 */
	private String[] selValToTables;
	/**
	 * Flag che indica se alcuni pulsanti (salvataggio e aggiunta di entità alla lista di destinazione) devono essere 
	 * disabilitati.
	 */
	protected boolean disabled = false;
	/**
	 * Flag che indica se alla pressione dei pulsanti "Indietro" e "Annulla" deve essere visualizzato un messaggio di 
	 * conferma (accade se l'utente ha modificato dei dati nella pagina).
	 */
	protected boolean needsConfirm = false;
	/**
	 *	Oggetto di classe CaricaTabelleModel utilizzato per l'accesso ai dati del DB (query di selezione, inserimento, 
	 *	modifica, cancellazione), nonché per la restituzione di eventuali dati statici.
	 */
	protected CaricaTabelleModel ctm;	
	/**
	 *	Oggetto di classe String che viene utilizzato per la marcatura visiva di determinati elementi nella lista di 
	 *	destinazione.
	 */
	protected String stars = " *** incompleta ***";

	/**
	*	Costruttore che crea un CaricaTabelleBB vuoto e inizializza inoltre il valore del campo focus.<p>
	*	Quindi effettua la connessione al DB di origine tramite la chiamata al metodo defaultConnectionAction().
	*/
	public CaricaTabelleBB() {
		super();
		ctm = new CaricaTabelleModel();
		setSchemas(new ArrayList<SelectItem>());
		getSchemas().add(new SelectItem("")); //item vuoto
		setFromTables(null, new ArrayList<SelectItem>());
		try {
			setToTablesHashMap(ctm.getDestinationTables());
			toTablesTrash = new HashMap<String,Table>();
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare la lista di destinazione"));
			e.printStackTrace();
		}
		focus = "form:schema";
		defaultConnectionAction();
	}
	
	/**
	 * Elimina il bean corrente dal FacesContext tramite la chiamata a cancellaForm(). Quindi effettua il rientro alla 
	 * pagina di menu metadati (mappatura).
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di menu metadati (mappatura).
	 */
	public String esci() {
		cancellaForm();
		//return "vaiAMenuMetadata";
		return "vaiAListaEntita";
	}
	
	/**
	 * Effettua il salvataggio dei dati (mappatura tabelle e configurazione colonne) nel DB di destinazione, tramite la 
	 * chiamata al metodo delegato nell'oggetto CaricaTabelleModel. Se si verifica un errore visualizza un messaggio nella 
	 * pagina.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di menu metadati (mappatura) o la permanenza nella pagina di mappatura tabelle (se si verifica un errore).
	 */
	public String salva(){
		try {	
			ctm.saveTables(toTables);
			cancellaForm();
			String message = getErrEntitiesMessage();
			if (message != null && !message.equals("")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
				return "vaiACaricaTabelle";
			}
		} catch (Exception e) {
			String message = "Errore nel salvataggio dei dati: ";
			message += e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			e.printStackTrace();
			return "vaiACaricaTabelle";
		}
		//return "vaiAMenuMetadata";
		return "vaiAListaEntita";
	}
	
	/**
	 * Effettua la chiamata alla pagina di mappatura colonne per la configurazione relativa.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di mappatura colonne.
	 */
	public String caricaColonne(){
		needsConfirm = true;
		return "vaiACaricaColonne";
	}
	
	/** 
	 * Elimina il bean corrente dal FacesContext tramite la chiamata a cancellaForm().<p>
	 * Quindi reinizializza la pagina tramite la chiamata a defaultConnectionAction().
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Annulla" nella pagina di mappatura 
	 * tabelle.
	 */
	public void cancella(ActionEvent event){
		needsConfirm = false;
		cancellaForm();
		defaultConnectionAction();
	}
	
	/**
	 * Elimina il bean CaricaTabelle dalla SessionMap perché venga chiamato di nuovo il suo costruttore 
	 * ripristinando la situazione iniziale (come alla prima apertura della pagina).
	 */
	protected void cancellaForm() {
		/*elimino il bean CaricaTabelle dalla SessionMap perché venga chiamato di nuovo il suo costruttore
		 ripristinando la situazione iniziale (come alla prima apertura della pagina)*/
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("CaricaTabelle");
	}
	
	/**
	 * Inizializza la pagina di mappatura tabelle impostando il focus e caricando gli schemi presenti 
	 * nel DB di origine (chiamata a findSchemas()).
	 */
	private void defaultConnectionAction() {
		focus = "form:schema";
		findSchemas();
		try {
			setToTablesHashMap(ctm.getDestinationTables());
			toTablesTrash = new HashMap<String,Table>();
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare la lista di destinazione"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Restituisce l'eventuale messaggio di errore ("" se non c'è errore) qualora non sia stato possibile salvare una 
	 * o più entità, perché modificate nel frattempo da un altro utente.
	 * @return Il messaggio di errore ("" se non c'è errore) da visualizzare qualora non sia stato possibile salvare 
	 * una o più entità, perché modificate nel frattempo da un altro utente
	 */
	private String getErrEntitiesMessage() {
		String retVal = "";
		ArrayList<String> errEntities = ctm.getErrEntities();
		if (errEntities != null && errEntities.size() > 0) {
			if (errEntities.size() == 1) {
				retVal = "Non è stato possibile procedere al salvataggio dell'entità \"";
				retVal += errEntities.get(0);
				retVal += "\", perché modificata nel frattempo da un altro utente.";
			}else{
				retVal = "Non è stato possibile procedere al salvataggio delle entità ";
				for (int i = 0; i < errEntities.size(); i++) {
					if (i == 0)
						retVal += "\"" + errEntities.get(i) + "\"";
					else if (i == errEntities.size() - 1)
						retVal += " e \"" + errEntities.get(i) + "\"";
					else
						retVal += ", \"" + errEntities.get(i) + "\"";
				}
				retVal += ", perché modificate nel frattempo da un altro utente.";
			}
		}
		return retVal;
	}
	
	/**
	 * Metodo che carica gli schemi presenti nel DB di origine tramite la chiamata al metodo a ciò delegato nell'oggetto 
	 * CaricaTabelleModel. Inoltre preseleziona lo schema il cui nome è uguale al nome dell'utente della connessione di 
	 * default.
	 */
	private void findSchemas() {
		String message;
		try {
			setSchemas(ctm.getSchemas());
			setSelectedSchema(getDefaultSelectedSchema());
			if (getSchemas().size() < 2) { //cioè se non esistono schemi e c'è solo l'item vuoto
				setFromTables(null, ctm.getTables(null, null, null, null));
			}
		}catch (Exception e) {
			setSchemas(new ArrayList<SelectItem>());
			getSchemas().add(new SelectItem("")); //item vuoto
			message = "Errore di connessione al database per i parametri inseriti";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			e.printStackTrace();
		}finally{
			isSchemaChanged(getSelectedSchema());
		}
	}
	
	
	/**
	 * Recupera il nome dello schema di default tramite la chiamata al metodo a ciò delegato nell'oggetto CaricaTabelleModel.
	 * @return Il nome dello schema di default (per la preselezione della combo box degli schemi)
	 */
	private String getDefaultSelectedSchema() {
		String myUsername = "";
		if (ctm.hasDefaultConnection()) {
			try {
				myUsername = ctm.getDefaultConnUsername();
			}catch (Exception e) {
				//non deve fare nulla, solo lasciare myUsername = ""
				e.printStackTrace();
			}
		}
		for (SelectItem si : getSchemas()) {
			if (si.getValue().toString().toUpperCase().equals(myUsername.toUpperCase()))
				return si.getValue().toString();
		}
		return "";
	}
	
	/** 
	 * Chiama il metodo isSchemaChanged() alla modifica della selezione nella combo box degli schemi.
	 * @param event L'oggetto che rappresenta l'evento di modifica della selezione nella combo box degli schemi.
	 */
	public void schemaChanged(ValueChangeEvent event) {
		String newValStr = (String)event.getNewValue();
		isSchemaChanged(newValStr);
		needsConfirm = true;
	}
	
	/** 
	 * Legge, tramite la chiamata al metodo a ciò delegato nell'oggetto CaricaTabelleModel, i nomi delle tabelle 
	 * presenti nel DB di origine per lo schema selezionato.
	 * @param newValStr Lo schema selezionato nella combo box degli schemi.
	 */
	private void isSchemaChanged(String newValStr) {
		try {
			if (getSchemas().size() > 1) {
				setFromTables(newValStr, ctm.getTables(null, newValStr, null, null));
			}
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare le tabelle per lo schema selezionato"));
			e.printStackTrace();
		}
	}
	
	/** 
	 * Esegue lo spostamento nella lista delle entità definite nel sistema degli elementi selezionati 
	 * nella lista delle entità nel DB di origine.<p>
	 * Effettua i controlli preliminari (obbligatorietà della selezione).<p>
	 * Quindi cancella dall'HashMap delle tabelle del DB di origine gli oggetti Table corrispondenti agli elementi 
	 * selezionati nella lista.<p>
	 * La prima (o unica) tabella spostata viene infine selezionata.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Aggiungi" della pagina 
	 * di mappatura tabelle.
	 * @throws AbortProcessingException Se si verifica una qualsiasi eccezione durante l'esecuzione del metodo.
	 */
	public void spostaADx (ActionEvent event) throws AbortProcessingException {
		if (fromTables == null || fromTables.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nella lista di origine non è presente nessun elemento"));
			return;
		}
		if (selValFromTables == null || selValFromTables.length == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nella lista di origine non è selezionato nessun elemento"));
			return;
		}
		String firstKey = "";
		for (int i = 0; i < fromTables.size(); i++) {
			for (int j = 0; j < selValFromTables.length; j++) {
				SelectItem sif = fromTables.get(i);
				if (sif.getValue().toString().equalsIgnoreCase(selValFromTables[j])) {
					Table tab = new Table();
					String value = sif.getValue().toString();					
					if (toTablesTrash.containsKey(value)) {
						toTables.put(value, toTablesTrash.get(value));
						toTablesTrash.remove(value);
					}else{
						tab.setSqlName(value);
						tab.setColumns(new HashMap<String, Column>());
						tab.setKeys(new HashMap<String, Key>());
						tab.setUpdated(false);
						tab.setDtMod(null);
						toTables.put(value, tab);
					}
					if (firstKey.equals("")) {
						firstKey = !isTableValid(toTables.get(value)) ? value + stars : value;
					}
					fromTables.remove(sif);
					i--;
					break;
				}
			}
		}
		//seleziono la prima (o l'unica) tabella spostata a destra
		selValToTables = new String[]{firstKey};
		selValFromTables = null;
		
		needsConfirm = true;
	}
	
	/** 
	 * Esegue lo spostamento nella lista delle entità nel DB di origine degli elementi selezionati 
	 * nella lista delle entità definite nel sistema.<p>
	 * Gestisce all'atto pratico la cancellazione di entità dalla suddetta lista.<p>
	 * Effettua i controlli preliminari (obbligatorietà della selezione).<p>
	 * Quindi cancella dall'HashMap delle tabelle definite nel sistema gli oggetti Table corrispondenti agli elementi 
	 * selezionati nella lista.<p>
	 * Le colonne spostate vengono infine selezionate.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Rimuovi" della pagina 
	 * di mappatura tabelle.
	 * @throws AbortProcessingException Se si verifica una qualsiasi eccezione durante l'esecuzione del metodo.
	 */
	public void spostaASx (ActionEvent event) throws AbortProcessingException {
		if (toTables == null || toTables.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nella lista di destinazione non è presente nessun elemento"));
			return;
		}
		if (selValToTables == null || selValToTables.length == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nella lista di destinazione non è selezionato nessun elemento"));
			return;
		}
		if (toTables == null || toTables.size() == 0) return;
		ArrayList<String> selValArr = new ArrayList<String>();
		for (int i = 0; i < getToTables().size(); i++) {
			for (int j = 0; j < selValToTables.length; j++) {
				SelectItem sit = getToTables().get(i);
				String sitVal = subtractStars(sit.getValue().toString());
				if (sitVal.equalsIgnoreCase(subtractStars(selValToTables[j]))) {
					SelectItem si = new SelectItem(sitVal);
					fromTables.add(si);
					selValArr.add(sitVal);
					toTablesTrash.put(sitVal, toTables.get(sitVal));
					toTables.remove(sitVal);
					i--;
					break;
				}
			}
		}
		//seleziono le tabelle spostate a sinistra
		selValFromTables = selValArr.toArray(new String[selValArr.size()]);
		selValToTables = null;
		
		needsConfirm = true;
	}
	
	public String[] getSelValFromTables() {
		return selValFromTables;
	}

	public void setSelValFromTables(String[] selValFromTables) {
		this.selValFromTables = selValFromTables;
	}
	
	public ArrayList<SelectItem> getSchemas() {
		return schemas;
	}
	
	public void setSchemas(ArrayList<SelectItem> schemas) {
		this.schemas = schemas;
	}

	public String getSelectedSchema() {
		return selectedSchema;
	}

	public void setSelectedSchema(String selectedSchema) {
		this.selectedSchema = selectedSchema;
	}

	/**
	 * Restituisce la lista delle entità nel DB di origine (fromTables) ordinata alfabeticamente per nome tabella.
	 * @return La lista delle entità nel DB di origine ordinata alfabeticamente.
	 */
	public ArrayList<SelectItem> getFromTables() {
		if (fromTables == null || fromTables.size() == 0) return fromTables;
		String[] fromTablesStr = new String[fromTables.size()];
		int index = 0;
		for (SelectItem si : fromTables) {
			fromTablesStr[index] = si.getValue().toString();
			index++;
		}
		Arrays.sort(fromTablesStr);
		fromTables = new ArrayList<SelectItem>();
		for (String tableStr : fromTablesStr) {
			fromTables.add(new SelectItem(tableStr));
		}
		return fromTables;
	}

	/** 
	 * Imposta fromTables con valori recuperati da un'ArrayList di SelectItem ed esegue il confronto delle due liste 
	 * (matchTables()).
	 * @param selectedSchema Lo schema selezionato (null o "" se il nome dello schema non deve essere aggiunto)
	 * @param fromTables Un'ArrayList di SelectItem
	 */
	public void setFromTables(String selectedSchema, ArrayList<SelectItem> fromTables) {
		if (selectedSchema != null && !selectedSchema.equals("")) {
			for (SelectItem si : fromTables) {
				
				// TODO marcoric 20-01-2012 --> non metto il selectedschema nel nome perche
				// poi fa casino quando si crea l'alias nella query 
				// SE SI VOGLIONO FARE QUERY CROSS SCHEMA BISOGNA TROVARE UN ALTRO MODO
				// ALTRIMENTI LE QUERY NON SONO PORTABILI
				//si.setValue(selectedSchema + "." + (String)si.getValue());
				si.setValue((String)si.getValue());
			}
		}
		this.fromTables = fromTables;
		matchTables();
	}
	
	/** 
	 * Elimina dalla lista delle entità del DB di origine gli elementi che hanno lo stesso nome di elementi presenti 
	 * nella lista delle entità definite nel sistema.
	 */
	private void matchTables() {
		if (fromTables != null) {
			for (int i = 0; i < fromTables.size(); i++) {
				SelectItem sif = fromTables.get(i);
				if (toTables != null) {
					if (toTables.containsKey(sif.getValue().toString())) {
						fromTables.remove(sif);
						i--;
					}
				}
			}
		}
	}

	/** 
	 * Restituisce la lista delle entità definite nel sistema come oggetto di classe ArrayList (di SelectItem)
	 * @return Un'ArrayList (di SelectItem) corrispondente all'HashMap toTables
	 */
	public ArrayList<SelectItem> getToTables() {
		if (toTables == null) return null;
		if (toTables.size() == 0) return new ArrayList<SelectItem>();
		Set<String> keys = toTables.keySet();
		String[] keysStr = (String[])keys.toArray(new String[keys.size()]);
		Arrays.sort(keysStr);
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		for (String keyStr : keysStr) {
			Table tab = toTables.get(keyStr);
			if (!isTableValid(tab))
				keyStr += stars;
			retVal.add(new SelectItem(keyStr));
		}
		return retVal;
	}

	/** 
	 * Imposta toTables con valori recuperati da un'ArrayList di SelectItem ed esegue il confronto delle due liste 
	 * (matchTables()).
	 * @param toTables Un'ArrayList di SelectItem
	 */
	public void setToTables(ArrayList<SelectItem> toTables) {
		if (toTables == null) {
			this.toTables = null;
			return;
		}
		if (toTables.size() == 0) {
			this.toTables = new HashMap<String,Table>();
			return;
		}
		for (SelectItem si : toTables) {
			Table tab = new Table();
			String value = si.getValue().toString();
			tab.setSqlName(value);
			tab.setUpdated(false);
			tab.setDtMod(null);
			if (this.toTables == null) this.toTables = new HashMap<String,Table>();
			this.toTables.put(value, tab);
		}
		matchTables();
	}
	
	/** 
	 * Imposta toTables con l'HashMap parametro ed esegue il confronto delle due liste (matchTables()).
	 * @param toTables Un'HashMap di chiavi di tipo String e valori di tipo Table
	 */
	protected void setToTablesHashMap(HashMap<String, Table> toTables) {
		this.toTables = toTables;
		matchTables();
	}
	
	/** 
	 * Restituisce l'oggetto toTables (HashMap di chiavi di tipo String e valori di tipo Table)
	 * @return L'oggetto toTables (HashMap di chiavi di tipo String e valori di tipo Table)
	 */
	protected HashMap<String, Table> getToTablesHashMap() {
		return toTables;
	}

	/** 
	 * Restituisce il valore del campo focus; essendo il get del primo campo della form, qui vengono effettuati i 
	 * controlli generali, in particolare la chiamata a setDisabled() per controllare se i pulsanti Salva e Aggiungi 
	 * devono essere disabilitati.
	 * @return Il valore del campo focus (id del campo della pagina di mappatura tabelle su cui impostare il focus).
	 */
	public String getFocus() {
		//questo è il get del primo campo della form, quindi è delegato ai controlli generali sulla pagina
		setDisabled(areToTablesValid() != null);
		//
		return focus;
	}

	public void setFocus(String focus) {
		this.focus = focus;
	}

	public String[] getSelValToTables() {
		return selValToTables;
	}

	public void setSelValToTables(String[] selValToTables) {
		this.selValToTables = selValToTables;
	}
	
	/**
	 * Aggiunge la String di marcatura visiva (stars) alla String passata a parametro.
	 * @param str La String da marcare visivamente
	 * @return La String marcata visivamente
	 */
	protected String addStars(String str) {
		if (str == null || str.equals(""))
			return str;
		return str += stars;
	}
	
	/**
	 * Sottrae la String (se presente) di marcatura visiva (stars) alla String passata a parametro.
	 * @param str La String a cui sottrarre la marcatura visiva
	 * @return La String privata della marcatura visiva
	 */
	protected String subtractStars(String str) {
		if (str == null || str.equals("") || stars == null || stars.equals("") || str.indexOf(stars) == -1)
			return str;
		return str.substring(0, str.indexOf(stars));
	}
	
	/**
	 * Verifica se un oggetto Table è valido, cioè completo di configurazione colonne, nome e descrizione estesa.
	 * @param table L'oggetto Table da testare
	 * @return Il flag che indica se l'oggetto Table è valido, cioè completo di configurazione colonne, nome e 
	 * descrizione estesa. 
	 */
	private boolean isTableValid(Table table) {
		return table.getName() != null && !table.getName().equals("") &&
				table.getColumns() != null && table.getColumns().size() > 0;
	}
	
	/**
	 * Verifica se le tabelle inserite nella lista delle entità definite nel sistema sono valide tramite la chiamata a 
	 * isTableValid().
	 * @return La prima tabella che non supera le regole di validazione; se sono tutte valide restituisce null
	 */
	private Table areToTablesValid() {
		Set<String> keys = toTables.keySet();
		for (String key : keys) {
			Table tab = toTables.get(key);
			if (!isTableValid(tab))
				return tab;
		}
		return null;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isNeedsConfirm() {
		return needsConfirm;
	}

	public void setNeedsConfirm(boolean needsConfirm) {
		this.needsConfirm = needsConfirm;
	}
	
}
