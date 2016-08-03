package it.webred.diogene.metadata.backingbeans;

import java.util.*;

import javax.faces.application.*;
import javax.faces.context.*;
import javax.faces.event.*;
import javax.faces.model.*;

import it.webred.diogene.metadata.beans.*;
import it.webred.diogene.metadata.model.*;

/**
 * Backing bean utilizzato dalla pagina di mappatura colonne.<p>
 * Contiene i metodi e i dati necessari per la mappatura su catalogo di colonne e chiavi per la tabella selezionata 
 * nella pagina chiamante.<p>
 * Inoltre, per la tabella di cui sopra, la pagina che utilizza questo bean (mappatura colonne) consente l'inserimento 
 * dei valori di "Nome" e "Descrizione estesa" (che sono campi di questo bean).<p>
 * L'accesso ai dati non viene direttamente effettuato in questa classe ma avviene tramite l'istanza di un oggetto di 
 * classe CaricaColonneModel.
 * @author Filippo Mazzini
 * @version $Revision: 1.6 $ $Date: 2008/01/24 11:21:58 $
 */
public class CaricaColonneBB extends CaricaTabelleBB {
	
	/**
	 *	Oggetto di classe CaricaColonneModel utilizzato per l'accesso ai dati del DB (query di selezione, inserimento, 
	 *	modifica, cancellazione), nonché per la restituzione di eventuali dati statici.
	 */
	private CaricaColonneModel ccm;
	/**
	 *	Il nome della tabella selezionata nella pagina chiamante, per la quale si effettua la mappatura di colonne e chiavi, 
	 * 	nonché l'inserimento dei valori "Nome" e "Descrizione estesa".
	 */
	private String selectedTable;
	/**
	 * Oggetto HashMap contenente l'elenco delle colonne disponibili nel database di origine per la tabella selezionata.<p>
	 * La chiave dell'HashMap corrisponde al nome della colonna; il valore è un oggetto di classe Column.
	 */
	private HashMap<String,Column> fromColumns;
	/**
	 * Oggetto HashMap contenente l'elenco delle colonne da definire nel sistema per la tabella selezionata.<p>
	 * La chiave dell'HashMap corrisponde al nome della colonna; il valore è un oggetto di classe Column.
	 */
	private HashMap<String,Column> toColumns;
	/**
	 *	Array di oggetti String corrispondente alla selezione effettuata nella lista delle colonne del database di 
	 *	origine.
	 */
	private String[] selValFromColumns;
	/**
	 *	Array di oggetti String corrispondente alla selezione effettuata nella lista delle colonne definite nel sistema.
	 */
	private String[] selValToColumns;
	/**
	 *	Oggetto HashMap contenente l'elenco delle colonne che costituiscono la chiave primaria definita nel sistema 
	 *	per la tabella selezionata.<p>
	 * 	La chiave dell'HashMap corrisponde al nome della colonna; il valore è un oggetto di classe Key.
	 */
	private HashMap<String,Key> keys;
	/**
	 *	Oggetto HashMap contenente l'elenco delle colonne che costituiscono la chiave primaria definita nel sistema 
	 *	per la tabella selezionata, nel momento in cui la pagina viene caricata per la prima volta (serve per tenerne 
	 *	traccia).<p>
	 * 	La chiave dell'HashMap corrisponde al nome della colonna; il valore è un oggetto di classe Key.
	 */
	private HashMap<String,Key> oldKeys;
	/**
	 *	Flag che indica se il campo descrizione colonna deve essere visibile (se la selezione nella lista delle colonne 
	 *	definite nel sistema è di un solo elemento) o meno.
	 */
	private boolean renderDescription;
	/**
	 *	Flag di comodo per agevolare il corretto recupero del valore per il campo descrizione colonna, a seconda dei 
	 *	casi.
	 */
	private boolean isSavingDesc;
	/**
	 *	Valore inserito nel campo di testo descrizione colonna (rappresenta la descrizione estesa della colonna).
	 */
	private String descrizioneEstesa;
	/**
	 *	Valore inserito nel campo di testo nome tabella.
	 */
	private String tableName;
	/**
	 *	Valore inserito nell'area di testo descrizione tabella (rappresenta la descrizione estesa della tabella).
	 */
	private String tableDescription;
	/**
	 *	Valore precedentemente inserito nel campo di testo nome tabella. Di utilità per verificare se sono state o 
	 *	meno effettuate modifiche da parte dell'utente.
	 */
	private String oldTableName;
	/**
	 *	Valore precedentemente inserito nell'area di testo descrizione tabella. Di utilità per verificare se sono state 
	 *	o meno effettuate modifiche da parte dell'utente.
	 */
	private String oldTableDescription;
	/**
	 *	Istanza di un oggetto di classe CaricaTabelleBB (superclasse di questa classe). Utilizzato per il corretto 
	 *	recupero dei dati della pagina chiamante (mappatura tabelle) necessari nella pagina di mappatura colonne.
	 */
	private CaricaTabelleBB ct;

	/**
	*	Costruttore che crea un CaricaColonneBB vuoto, per poi (tramite la chiamata a setFieldsFromTables()) 
	*	inizializzarne alcuni valori, recuperati dalla pagina chiamante (mappatura tabelle).<p>
	*	Inizializza inoltre i valori dei campi stars e focus (ereditati dalla superclasse CaricaTabelleBB).
	*/
	public CaricaColonneBB() {
		super();
		ccm = new CaricaColonneModel();
		ct = (CaricaTabelleBB)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CaricaTabelle");
		setSelectedTable("");
		setFromColumns(new ArrayList<SelectItem>());
		setToColumns(new ArrayList<SelectItem>());
		setKeys(new ArrayList<SelectItem>());
		oldKeys = new HashMap<String, Key>();
		setFieldsFromTables();
		stars = " *";
		focus = "form:tableName";
	}
	
	/**
	 * Elimina il bean corrente dal FacesContext tramite la chiamata a cancellaForm(). Quindi effettua il rientro alla 
	 * pagina di mappatura tabelle.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di mappatura tabelle
	 */
	public String esci() {
		cancellaForm();
		return "vaiACaricaTabelle";
	}
	
	/**
	 * Effettua i controlli preliminari al salvataggio dei dati (obbligatorietà).<p> 
	 * Quindi, se i controlli non sono superati, visualizza nell'interfaccia grafica il messaggio di errore appropriato.<p>
	 * Se il salvataggio è invece possibile, chiama il metodo privato relativo (saveColumns()) ed effettua il rientro 
	 * alla pagina di mappatura tabelle.<p>
	 * Per salvataggio dei dati, nella pagina di mappatura colonne, non si intende il salvataggio su DB, ma la semplice 
	 * valorizzazione di alcuni campi nella pagina chiamante (mappatura tabelle), che è delegata al salvataggio generale 
	 * dei dati su DB.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di mappatura tabelle, se il salvataggio è possibile, o, in caso contrario, lascia aperta la pagina di mappatura 
	 * colonne.
	 */
	public String salva(){
		try {
			if (tableName == null || tableName.equals("")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Inserire la descrizione della tabella"));
				return "vaiACaricaColonne";
			}
			if (tableDescription == null || tableDescription.equals("")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Inserire la descrizione estesa della tabella"));
				return "vaiACaricaColonne";
			}
			if (toColumns == null || toColumns.size() == 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nessuna colonna inserita: impossibile procedere"));
				return "vaiACaricaColonne";
			}
			if (keys == null || keys.size() == 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nessuna chiave inserita: impossibile procedere"));
				return "vaiACaricaColonne";
			}
			String colDescsErrMessage = getColDescsErrMessage();
			if (colDescsErrMessage != null && !colDescsErrMessage.equals("")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(colDescsErrMessage));
				return "vaiACaricaColonne";
			}
			saveColumns();
			renderDescription = false;
			cancellaForm();
		} catch (Exception e) {
			String message = "Errore nel salvataggio dei dati: ";
			message += e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			e.printStackTrace();
			return "vaiACaricaColonne";
		}
		return "vaiACaricaTabelle";
	}
	
	/**
	 * Metodo privato di utilità che controlla tutte le descrizioni delle colonne e restituisce il messaggio di errore 
	 * appropriato (o "" se non vi sono errori).
	 * @return Il messaggio di errore appropriato ("" se non vi sono errori).
	 */
	private String getColDescsErrMessage() {
		String retVal = "";
		ArrayList<String> errs = new ArrayList<String>();
		for (SelectItem si : getToColumns()) {
			String key = subtractStars(si.getValue().toString());
			Column col = toColumns.get(key);
			if (col.getLongDesc() == null || col.getLongDesc().equals("")) {
				errs.add(key);
			}
		}
		if (errs.size() == 1) {
			retVal = "Inserire la descrizione della colonna " + errs.get(0);
		} else if (errs.size() > 1) {
			retVal = "Inserire la descrizione delle colonne ";
			for (int i = 0; i < errs.size(); i++) {
				retVal += errs.get(i);
				if (i == errs.size() - 2) {
					retVal += " e ";
				} else if (i < errs.size() - 2) {
					retVal += ", ";
				}
			}
		}
		return retVal;
	}
	
	/**
	 * Imposta nome, descrizione estesa, colonne e chiavi nella tabella selezionata nella pagina chiamante 
	 * (mappatura tabelle).
	 */
	private void saveColumns() {
		ct.getToTablesHashMap().get(getSelectedTable()).setName(tableName);
		ct.getToTablesHashMap().get(getSelectedTable()).setLongDesc(tableDescription);
		ct.getToTablesHashMap().get(getSelectedTable()).setColumns(toColumns);
		ct.getToTablesHashMap().get(getSelectedTable()).setKeys(keys);
		ct.getToTablesHashMap().get(getSelectedTable()).setUpdated(true);
		//potrei aver configurato una nuova tabella, quindi reimposto la selezione nella chiamante togliendo stars
		ct.getSelValToTables()[0] = super.subtractStars(ct.getSelValToTables()[0]);
	}
	
	/**
	 * Elimina il bean CaricaColonne dalla SessionMap perché venga chiamato di nuovo il suo costruttore 
	 * ripristinando la situazione iniziale (come alla prima apertura della pagina).
	 */
	protected void cancellaForm() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("CaricaColonne");
	}

	public String getSelectedTable() {
		return selectedTable;
	}
	
	public void setSelectedTable(String selectedTable) {
		this.selectedTable = selectedTable;
	}
	
	/** 
	 * Chiama il metodo privato (renderDescription()) responsabile della visualizzazione del campo di testo 
	 * descrizione estesa colonna.
	 * @param event L'oggetto che rappresenta l'evento di modifica della selezione nella lista delle colonne 
	 * definite nel sistema.
	 * @throws AbortProcessingException Se si verifica una qualsiasi eccezione durante l'esecuzione del metodo.
	 */
	public void selectToColumns(ValueChangeEvent event) throws AbortProcessingException {
		renderDescription((String[])event.getNewValue());
	}
	
	/** 
	 * Esegue lo spostamento nella lista delle colonne definite nel sistema degli elementi selezionati 
	 * nella lista delle colonne nel DB di origine.<p>
	 * Effettua i controlli preliminari (obbligatorietà della selezione).<p>
	 * Quindi cancella dall'HashMap delle colonne del DB di origine gli oggetti Column corrispondenti agli elementi 
	 * selezionati nella lista.<p>
	 * La prima (o unica) colonna spostata viene infine selezionata.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Aggiungi" della pagina 
	 * di mappatura colonne.
	 * @throws AbortProcessingException Se si verifica una qualsiasi eccezione durante l'esecuzione del metodo.
	 */
	public void spostaADx(ActionEvent event) throws AbortProcessingException {
		if (fromColumns == null || fromColumns.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nella lista di origine non è presente nessun elemento"));
			return;
		}
		if (selValFromColumns == null || selValFromColumns.length == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nella lista di origine non è selezionato nessun elemento"));
			return;
		}
		String firstKey = "";
		for (int i = 0; i < getFromColumns().size(); i++) {
			for (int j = 0; j < selValFromColumns.length; j++) {
				SelectItem sit = getFromColumns().get(i);
				String sitVal = sit.getValue().toString();
				if (sitVal.equalsIgnoreCase(selValFromColumns[j])) {
					toColumns.put(sitVal, fromColumns.get(sitVal));
					if (firstKey.equals(""))
						firstKey = sitVal;
					fromColumns.remove(sitVal);
					i--;
					break;
				}
			}
		}
		//seleziono la prima (o l'unica) colonna spostata a destra
		selValToColumns = new String[]{firstKey};
		selValFromColumns = null;
		renderDescription(selValToColumns);
		
		needsConfirm = true;
	}
	
	/** 
	 * Esegue lo spostamento nella lista delle colonne nel DB di origine degli elementi selezionati 
	 * nella lista delle colonne definite nel sistema.<p>
	 * Gestisce all'atto pratico la cancellazione di colonne dalla suddetta lista.<p>
	 * Effettua i controlli preliminari (obbligatorietà della selezione).<p>
	 * Quindi cancella dall'HashMap delle colonne definite nel sistema gli oggetti Column corrispondenti agli elementi 
	 * selezionati nella lista.<p>
	 * Le colonne spostate vengono infine selezionate.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Rimuovi" della pagina 
	 * di mappatura colonne.
	 * @throws AbortProcessingException Se si verifica una qualsiasi eccezione durante l'esecuzione del metodo.
	 */
	public void spostaASx(ActionEvent event) throws AbortProcessingException {
		if (toColumns == null || toColumns.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nella lista di destinazione non è presente nessun elemento"));
			return;
		}
		if (selValToColumns == null || selValToColumns.length == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nella lista di destinazione non è selezionato nessun elemento"));
			return;
		}
		ArrayList<String> selValArr = new ArrayList<String>();
		for (int i = 0; i < getToColumns().size(); i++) {
			for (int j = 0; j < selValToColumns.length; j++) {
				SelectItem sit = getToColumns().get(i);
				String sitVal = subtractStars(sit.getValue().toString());
				if (sitVal.equalsIgnoreCase(subtractStars(selValToColumns[j]))) {
					Column col = toColumns.get(sitVal);
					fromColumns.put(sitVal, col);
					toColumns.remove(sitVal);
					selValArr.add(sitVal);
					if (getKeys() != null) {
						for(SelectItem sik : getKeys()) {
							String sikVal = sik.getValue().toString();
							if (sikVal.equalsIgnoreCase(sitVal)) {
								keys.remove(sikVal);
								break;
							}
						}
					}
					i--;
					break;
				}
			}
		}
		renderDescription = false;
		//seleziono le colonne spostate a sinistra
		selValFromColumns = selValArr.toArray(new String[selValArr.size()]);
		selValToColumns = null;
		
		needsConfirm = true;
	}
	
	/** 
	 * Imposta la chiave per la tabella di cui si sta eseguendo la mappatura colonne.<p>
	 * La chiave corrisponde agli elementi (colonne) selezionati nella lista delle colonne definite nel sistema.<p>
	 * Il metodo effettua i controlli preliminari (obbligatorietà della selezione).<p>
	 * Quindi valorizza l'HashMap (contenente oggetti di classe Key) che rappresenta la chiave.<p>
	 * Ognuno di tali oggetti di classe Key avrà come chiave (columnName) il nome della colonna corrispondente.<p>
	 * La prima (o unica) colonna della chiave viene infine selezionata.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Chiave" della pagina 
	 * di mappatura colonne.
	 * @throws AbortProcessingException Se si verifica una qualsiasi eccezione durante l'esecuzione del metodo.
	 */
	public void impostaComeChiave(ActionEvent event) throws AbortProcessingException {
		if (toColumns == null || toColumns.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nella lista di destinazione non è presente nessun elemento"));
			return;
		}
		setKeys(new ArrayList<SelectItem>());
		for (int i = 0; i < getToColumns().size(); i++) {
			if (selValToColumns != null) {
				for (int j = 0; j < selValToColumns.length; j++) {
					SelectItem sit = getToColumns().get(i);
					String sitVal = subtractStars(sit.getValue().toString());
					if (sitVal.equalsIgnoreCase(subtractStars(selValToColumns[j]))) {
						if (!keys.containsKey(sitVal)) {
							Key key = null;
							if (!oldKeys.containsKey(sitVal)) {
								Column col = toColumns.get(sitVal);
								key = new Key();
								try {
									key.setColumnId(col.getId() == null ? null : ccm.getColIdFromExpressionId(col.getId()));
								} catch (Exception e) {
									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile completare la selezione della chiave"));
									return;
								}
								key.setColumnName(sitVal);
							}else{
								key = (Key)oldKeys.get(sitVal).clone();
							}
							keys.put(key.getColumnName(), key);
						}
						break;
					}
				}
			}
		}
		//seleziono, se presente, la prima colonna della chiave
		if (getKeys() != null && getKeys().size() > 0) {
			String value = getKeys().get(0).getValue().toString();
			selValToColumns = new String[] {addStars(subtractStars(value))};
			renderDescription(selValToColumns);
		}else{
			renderDescription = false;
			selValToColumns = null;
		}
		needsConfirm = true;
	}
	
	/** 
	 * Imposta la descrizione estesa per la colonna selezionata nella lista delle colonne definite nel 
	 * sistema, recuperando il valore inserito nel campo descrizione estesa colonna.<p>
	 * Il metodo effettua il controllo preliminare (sulla lunghezza della descrizione), visualizzando un messaggio di 
	 * errore se il controllo non viene superato.<p>
	 * Viceversa, procede all'impostazione del valore di cui sopra nell'oggetto Column corrispondente alla colonna 
	 * selezionata.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Conferma descrizione" della pagina 
	 * di mappatura colonne.
	 * @throws AbortProcessingException Se si verifica una qualsiasi eccezione durante l'esecuzione del metodo.
	 */
	public void saveDescription (ActionEvent event) throws AbortProcessingException {
		if (selValToColumns != null && selValToColumns.length == 1) {
			Column col = toColumns.get(subtractStars(selValToColumns[0]));
			isSavingDesc = true;
			String descEstesa = getDescrizioneEstesa();
			boolean isError = false;
			if (descEstesa != null && descEstesa.length() > 2000) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile inserire una descrizione estesa lunga più di 2000 caratteri"));
				isError = true;
			}
			if (isError) return;
			if (descEstesa == null || descEstesa.equals(""))
				descEstesa = col.getName(); //default
			col.setLongDesc(descEstesa);
			needsConfirm = true;
		}
	}
	
	/** Metodo di utilità che imposta la visibilità del campo descrizione estesa colonna a seconda della selezione 
	 * effettuata nella lista delle colonne definite nel sistema.<p>
	 * Se tale selezione comprende uno e un solo elemento, il campo è visibile, altrimenti è nascosto.
	 * @param selValToColumnsParam L'array di oggetti String che rappresenta la selezione nella lista delle colonne 
	 * definite nel sistema
	 */
	protected void renderDescription(String[] selValToColumnsParam) {
		renderDescription = selValToColumnsParam != null && selValToColumnsParam.length == 1;		
		setDescrizioneEstesa(renderDescription ? toColumns.get(subtractStars(selValToColumnsParam[0])).getLongDesc() : "");
	}

	/** 
	 * Restituisce la lista delle colonne del DB di origine come oggetto di classe ArrayList (di SelectItem)
	 * @return Un'ArrayList (di SelectItem) corrispondente all'HashMap fromColumns
	 */
	public ArrayList<SelectItem> getFromColumns() {
		if (fromColumns == null) return null;
		if (fromColumns.size() == 0) return new ArrayList<SelectItem>();
		Set<String> keys = fromColumns.keySet();
		String[] keysStr = (String[])keys.toArray(new String[keys.size()]);
		Arrays.sort(keysStr);
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		for (String keyStr : keysStr) {
			retVal.add(new SelectItem(keyStr));
		}
		return retVal;
	}
	
	/** 
	 * Restituisce la lista delle colonne definite nel sistema come oggetto di classe ArrayList (di SelectItem)
	 * @return Un'ArrayList (di SelectItem) corrispondente all'HashMap toColumns
	 */
	public ArrayList<SelectItem> getToColumns() {
		if (toColumns == null) return null;
		if (toColumns.size() == 0) return new ArrayList<SelectItem>();
		Set<String> keys = toColumns.keySet();
		String[] keysStr = (String[])keys.toArray(new String[keys.size()]);
		Arrays.sort(keysStr);
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		for (String keyStr : keysStr) {
			if (getKeys() != null) {
				for (SelectItem keySi : getKeys()) {
					String keyVal = keySi.getValue().toString();
					if (keyVal.equalsIgnoreCase(keyStr)) {
						keyStr = addStars(keyStr);
					}
				}
			}
			retVal.add(new SelectItem(keyStr));
		}
		return retVal;
	}
	
	/** 
	 * Restituisce la chiave come oggetto di classe ArrayList (di SelectItem)
	 * @return Un'ArrayList (di SelectItem) corrispondente all'HashMap keys
	 */
	public ArrayList<SelectItem> getKeys() {
		if (keys == null) return null;
		if (keys.size() == 0) return new ArrayList<SelectItem>();
		Set<String> keySet = keys.keySet();
		String[] keySetStr = (String[])keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keySetStr);
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		for (String keyStr : keySetStr) {
			retVal.add(new SelectItem(keyStr));
		}
		return retVal;
	}
	
	/** 
	 * Elimina dalla lista delle colonne del DB di origine gli elementi che hanno lo stesso nome (chiave
	 * dell'HashMap) di elementi presenti nella lista delle colonne definite nel sistema.
	 */
	private void matchColumns() {
		//devo usare getFromColumns() che mi restituisce l'ArrayList<SelectItem>
		if (getFromColumns() != null) {
			for (int i = 0; i < getFromColumns().size(); i++) {
				SelectItem sit = getFromColumns().get(i);
				String sitVal = sit.getValue().toString();
				if (toColumns != null) {
					if (toColumns.containsKey(sitVal)) {
						fromColumns.remove(sitVal);
						i--;
					}
				}
			}
		}
	}
	
	/** 
	 * Imposta fromColumns con valori recuperati da un'ArrayList di SelectItem ed esegue il confronto delle due liste 
	 * (matchColumns()).
	 * @param fromColumns Un'ArrayList di SelectItem
	 */
	public void setFromColumns(ArrayList<SelectItem> fromColumns) {
		if (fromColumns == null) {
			this.fromColumns = null;
			return;
		}
		if (fromColumns.size() == 0) {
			this.fromColumns = new HashMap<String,Column>();
			return;
		}
		for (SelectItem si : fromColumns) {
			Column col = new Column();
			String value = si.getValue().toString();
			col.setName(value);
			if (this.fromColumns == null) this.fromColumns = new HashMap<String,Column>();
			this.fromColumns.put(value, col);
		}
		matchColumns();
	}
	
	/** 
	 * Imposta fromColumns con l'HashMap parametro ed esegue il confronto delle due liste (matchColumns()).
	 * @param fromColumns Un'HashMap di chiavi di tipo String e valori di tipo Column
	 */
	private void setFromColumnsHashMap(HashMap<String, Column> fromColumns) {
		this.fromColumns = fromColumns;
		matchColumns();
	}

	/** 
	 * Imposta toColumns con valori recuperati da un'ArrayList di SelectItem ed esegue il confronto delle due liste 
	 * (matchColumns()).
	 * @param toColumns Un'ArrayList di SelectItem
	 */
	public void setToColumns(ArrayList<SelectItem> toColumns) {
		if (toColumns == null) {
			this.toColumns = null;
			return;
		}
		if (toColumns.size() == 0) {
			this.toColumns = new HashMap<String,Column>();
			return;
		}
		for (SelectItem si : toColumns) {
			Column col = new Column();
			String value = si.getValue().toString();
			col.setName(value);
			if (this.toColumns == null) this.toColumns = new HashMap<String,Column>();
			this.toColumns.put(value, col);
		}
		matchColumns();
	}
	
	/** 
	 * Imposta toColumns con l'HashMap parametro ed esegue il confronto delle due liste (matchColumns()).
	 * @param toColumns Un'HashMap di chiavi di tipo String e valori di tipo Column
	 */
	private void setToColumnsHashMap(HashMap<String, Column> toColumns) {
		this.toColumns = toColumns;
		matchColumns();
	}
	
	/** 
	 * Imposta keys con valori recuperati da un'ArrayList di SelectItem.
	 * @param keys Un'ArrayList di SelectItem
	 */
	public void setKeys(ArrayList<SelectItem> keys) {
		if (keys == null) {
			this.keys = null;
			return;
		}
		if (keys.size() == 0) {
			this.keys = new HashMap<String,Key>();
			return;
		}
		for (SelectItem si : keys) {
			Key key = new Key();
			String value = si.getValue().toString();
			key.setColumnName(value);
			if (this.keys == null) this.keys = new HashMap<String,Key>();
			this.keys.put(value, key);
		}
		matchColumns();
	}
	
	/** 
	 * Imposta keys con l'HashMap parametro.
	 * @param keys Un'HashMap di chiavi di tipo String e valori di tipo Key
	 */
	private void setKeysHashMap(HashMap<String, Key> keys) {
		this.keys = keys;
	}
	
	public String getDescrizioneEstesa() {
		if (!isSavingDesc)
			descrizioneEstesa = selValToColumns != null && selValToColumns.length == 1 ?
					((Column)toColumns.get(subtractStars(selValToColumns[0]))).getLongDesc()
					: "";
		else
			isSavingDesc = false;
		return descrizioneEstesa;
	}
	
	/**
	 * Imposta il valore di alcuni campi con i valori corrispondenti nell'oggetto ct (di classe CaricaTabelleBB).<p>
	 * Questo oggetto contiene i valori inseriti nella pagina chiamante (mappatura tabelle).
	 */
	private void setFieldsFromTables() {
		setSchemas(ct.getSchemas());
		setSelectedSchema(ct.getSelectedSchema());
		setSelectedTable(super.subtractStars(ct.getSelValToTables()[0]));
		setTableName(ct.getToTablesHashMap().get(getSelectedTable()).getName() == null ? 
				getSelectedTable() :	
				new String(ct.getToTablesHashMap().get(getSelectedTable()).getName()));
		setTableDescription(ct.getToTablesHashMap().get(getSelectedTable()).getLongDesc() == null ?
				null :
				new String(ct.getToTablesHashMap().get(getSelectedTable()).getLongDesc()));
		setOldTableName(getTableName() == null ? null : new String(getTableName()));
		setOldTableDescription(getTableDescription() == null ? null : new String(getTableDescription()));
		try {
			String schemaTabella=null;
			if (getSchemas().size() > 1) {
				if (getSelectedTable() != null && !getSelectedTable().equals("")) {
					String selectedTable = getSelectedTable();
					if (getSelectedSchema() != null && !getSelectedSchema().equals("") && getSelectedTable().indexOf(getSelectedSchema() + ".") > -1) {
						selectedTable = selectedTable.substring((getSelectedSchema() + ".").length());
					} else {
						// la tabella contiene già il nome dello schema incorporato (solitamente quando siamo in modifica)
						if(getSelectedTable().indexOf(".") > -1) 
							schemaTabella = selectedTable.substring(0,selectedTable.indexOf("."));
							selectedTable = selectedTable.substring(selectedTable.indexOf(".")+1);
					}
					setFromColumnsHashMap(ccm.getColumns(null, schemaTabella, selectedTable, null));
				}else{
					setFromColumns(new ArrayList<SelectItem>());
				}
			}
			cloneColumnsAndKeys();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare le colonne per lo schema e la tabella selezionati"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo di utilità che clona le HashMap che contengono colonne (Column) e chiavi (Key).<p>
	 * Questo perché tali HashMap vengono caricate inizialmente con i valori inseriti nella pagina chiamante 
	 * (mappatura tabelle), ma vengono poi modificate; è quindi necessario evitare il puntamento agli oggetti 
	 * originari che devono invece rimanere immutati.
	 */
	private void cloneColumnsAndKeys() throws Exception {
		//queste hash map possono essere modificate, le clono per evitare errati puntamenti agli oggetti di ct
		//colonne
		HashMap<?,?> orig = (HashMap<?,?>)ct.getToTablesHashMap().get(getSelectedTable()).getColumns();
		if (orig.keySet().size() == 1 && orig.containsKey(CaricaTabelleModel.NON_CONFIGURATA) && orig.get(CaricaTabelleModel.NON_CONFIGURATA) == null) {
			HashMap<String, Column> columns = ccm.getDestinationColumns(ct.getToTablesHashMap().get(getSelectedTable()).getId());
			ct.getToTablesHashMap().get(getSelectedTable()).setColumns(columns);
			orig = (HashMap<?,?>)ct.getToTablesHashMap().get(getSelectedTable()).getColumns();
		}
		HashMap<?,?> clone = (HashMap<?,?>)orig.clone();
		HashMap<String,Column> cloneHMColumns = new HashMap<String,Column>();
		Set<?> keySet = clone.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			String myKey = (String)it.next();
			cloneHMColumns.put(myKey, (Column)((Column)clone.get(myKey)).clone());
		}
		setToColumnsHashMap(cloneHMColumns);
		//chiavi
		orig = (HashMap<?,?>)ct.getToTablesHashMap().get(getSelectedTable()).getKeys();
		if (orig.keySet().size() == 1 && orig.containsKey(CaricaTabelleModel.NON_CONFIGURATA) && orig.get(CaricaTabelleModel.NON_CONFIGURATA) == null) {
			HashMap<String, Key> keys = ccm.getKeys(ct.getToTablesHashMap().get(getSelectedTable()).getId());
			ct.getToTablesHashMap().get(getSelectedTable()).setKeys(keys);
			orig = (HashMap<?,?>)ct.getToTablesHashMap().get(getSelectedTable()).getKeys();
		}
		clone = (HashMap<?,?>)orig.clone();
		HashMap<String,Key> cloneHMKeys = new HashMap<String,Key>();		
		keySet = clone.keySet();
		it = keySet.iterator();
		while (it.hasNext()) {
			String myKey = (String)it.next();
			cloneHMKeys.put(myKey, (Key)((Key)clone.get(myKey)).clone());
		}
		setKeysHashMap(cloneHMKeys);
		//reimposto oldKeys
		oldKeys = new HashMap<String, Key>();
		keySet = keys.keySet();
		it = keySet.iterator();
		while (it.hasNext()) {
			String myKey = (String)it.next();
			oldKeys.put(myKey, (Key)keys.get(myKey).clone());
		}
	}

	public String[] getSelValFromColumns() {
		return selValFromColumns;
	}

	public void setSelValFromColumns(String[] selValFromColumns) {
		this.selValFromColumns = selValFromColumns;
	}

	public String[] getSelValToColumns() {
		return selValToColumns;
	}

	public void setSelValToColumns(String[] selValToColumns) {
		this.selValToColumns = selValToColumns;
	}

	public String getFocus() {
		//ridefinito perché non deve effettuare i controlli che esegue getFocus() della superclasse
		return focus;
	}

	public boolean isSavingDesc() {
		return isSavingDesc;
	}

	public void setSavingDesc(boolean isSavingDesc) {
		this.isSavingDesc = isSavingDesc;
	}

	public boolean isRenderDescription() {
		return renderDescription;
	}

	public void setRenderDescription(boolean renderDescription) {
		this.renderDescription = renderDescription;
	}

	public void setDescrizioneEstesa(String descrizioneEstesa) {
		this.descrizioneEstesa = descrizioneEstesa;
	}

	public String getTableDescription() {
		return tableDescription;
	}

	public void setTableDescription(String tableDescription) {
		this.tableDescription = tableDescription;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getOldTableDescription() {
		return oldTableDescription;
	}

	public void setOldTableDescription(String oldTableDescription) {
		this.oldTableDescription = oldTableDescription;
	}

	public String getOldTableName() {
		return oldTableName;
	}

	public void setOldTableName(String oldTableName) {
		this.oldTableName = oldTableName;
	}
}
