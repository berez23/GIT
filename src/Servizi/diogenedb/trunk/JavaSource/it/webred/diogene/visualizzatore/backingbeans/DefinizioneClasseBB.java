package it.webred.diogene.visualizzatore.backingbeans;

import it.webred.diogene.metadata.beans.*;
import it.webred.diogene.visualizzatore.beans.*;
import it.webred.diogene.visualizzatore.model.DefinizioneEntitaModel;
import it.webred.diogene.visualizzatore.runtime.PageManager;
import it.webred.diogene.visualizzatore.runtime.tag.MenuRenderTag;

import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 * Backing bean utilizzato dalla popup di gestione classi (non contenitori).<p>
 * Contiene i metodi e i dati necessari per l'inserimento o la modifica di una classe, la sua associazione ad 
 * un'entità e la definizione, per la classe, di criteri di filtro e lista.<p>
 * L'accesso ai dati non viene direttamente effettuato in questa classe ma avviene tramite l'istanza di un oggetto di 
 * classe DefinizioneEntitaModel.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class DefinizioneClasseBB extends DefinizioneFolderBB {
	
	/**
	 * Il nome dell'entità associata alla classe.
	 */
	private String selectedEntity;
	/**
	 * Flag che indica se la combo box di selezione entità deve essere disabilitata.
	 */
	private boolean selectedEntityDisabled;
	/**
	 * HashMap che contiene la lista delle entità per la valorizzazione della combo box di selezione entità.
	 */
	private HashMap<String,Table> entities;
	/**
	 * DataModel per la costruzione della tabella utilizzata per l'inserimento dei criteri di filtro.
	 */
	private DataModel filterDM; 
	/**
	 * ArrayList di oggetti Filter che costituiscono i dati necessari per valorizzare il DataModel filterDM.
	 */
	private ArrayList<Filter> filterAL;
	/**
	 * ArrayList di SelectItem per valorizzare la lista di selezione delle colonne da aggiungere al filtro.
	 */
	private ArrayList<SelectItem> filterColumns;
	/**
	 * ArrayList di SelectItem che contiene l'elenco delle colonne per l'entità selezionata.
	 */
	private ArrayList<SelectItem> allColumns;
	/**
	 * Descrizione estesa della colonna selezionata nella lista delle colonne da aggiungere al filtro.
	 */
	private String selectedFilterColumn;
	/**
	 * Descrizione estesa della colonna rimossa dal filtro.
	 */
	private String removedFilterColumn;
	/**
	 * Descrizione estesa della colonna a cui viene aggiunto un valore fisso per il filtro.
	 */
	private String addedValueToFilterColumn;
	/**
	 * Descrizione estesa della colonna da cui viene rimosso un valore fisso per il filtro.
	 */
	private String removedValueFromFilterColumn;
	/**
	 * HashMap con chiavi String e valori ArrayList di GenericValue. Le chiavi sono le descrizioni estese delle colonne 
	 * aggiunte al filtro, le ArrayList rappresentano le liste degli operatori disponibili per ogni colonna.
	 */
	private HashMap<String, ArrayList<GenericValue>> typeOperators;	
	/**
	 * DataModel per la costruzione della tabella utilizzata per l'inserimento di colonne o javascript in lista.
	 */
	private DataModel listDM; 
	/**
	 * ArrayList di oggetti FcList che costituiscono i dati necessari per valorizzare il DataModel listDM.
	 */
	private ArrayList<FcList> listAL;
	/**
	 * Identificativo (index) della colonna o del javascript rimosso dalla lista.
	 */
	private int removedListRow;
	/**
	 * Identificativo (index) del javascript della lista a cui viene aggiunta una colonna parametro.
	 */
	private int addedParamToListJs;
	/**
	 * Identificativo (index) del javascript della lista da cui viene rimossa una colonna parametro.
	 */
	private int removedParamFromListJs;
	/**
	 * DataModel per la costruzione della tabella utilizzata per l'inserimento di tabelle o divisioni nel dettaglio.
	 */
	private DataModel detailDM; 
	/**
	 * ArrayList di oggetti DetailGroup che costituiscono i dati necessari per valorizzare il DataModel detailDM.
	 */
	private ArrayList<DetailGroup> detailAL;
	/**
	 * DataModel per la costruzione della tabella utilizzata per la gestione dei link.
	 */
	private DataModel linkDM;
	/**
	 * ArrayList di oggetti Link che costituiscono i dati necessari per valorizzare il DataModel linkDM.
	 */
	private ArrayList<Link> linkAL;
	/**
	 * Identificativo (index) della tabella o della divisione del dettaglio aggiornata o rimossa.
	 */
	private int updatedDetailTableOrBreak;
	/**
	 * Identificativo (index) della riga di una tabella rimossa dal dettaglio.
	 */
	private int removedDetailRow;
	/**
	 * Nome della classe di stile per il pannello filtro (per gestirne la visibilità: "visible" o "hidden")
	 */
	private String filterPanelStyle;
	/**
	 * Nome della classe di stile per il pannello lista (per gestirne la visibilità: "visible" o "hidden")
	 */
	private String listPanelStyle;
	/**
	 * Nome della classe di stile per il pannello dettaglio (per gestirne la visibilità: "visible" o "hidden")
	 */
	private String detailPanelStyle;
	/**
	 * Nome della classe di stile per il pannello link (per gestirne la visibilità: "visible" o "hidden")
	 */
	private String linkPanelStyle;
	/**
	 * Titolo del filtro.
	 */
	private String filterTitle;
	/**
	 * Titolo della lista.
	 */
	private String listTitle;
	/**
	 * Titolo del dettaglio.
	 */
	private String detailTitle;
	/**
	 * Campo composto dall'identificativo del link eventualmente deselezionato nella popup e da un flag che indica 
	 * come, in questo link, deve essere impostato il campo deletingAllKeys.
	 */
	private String linkDeletingAllKeys;
	

	public DefinizioneClasseBB() {
		super();
	}

	/**
	 * Metodo che inizializza la popup; se in modifica, preseleziona nella combo box relativa l'entità a cui 
	 * la classe è associata.
	 */
	protected void setPopup() {
		super.setPopup();
		selectedEntity = "";
		try {
			setEntitiesHashMap(dem.getEntities());
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare le entità"));
			setPopupError(true);
			e.printStackTrace();
		}
		setFilterColumns(new ArrayList<SelectItem>());
		setTypeOperators(new HashMap<String, ArrayList<GenericValue>>());
		selectedFilterColumn = "";
		removedFilterColumn = "";
		addedValueToFilterColumn = "";
		removedValueFromFilterColumn = "";
		filterDM = new ListDataModel();
		setFilterAL(new ArrayList<Filter>());
		listDM = new ListDataModel();
		setListAL(new ArrayList<FcList>());
		detailDM = new ListDataModel();
		setDetailAL(new ArrayList<DetailGroup>());
		linkDM = new ListDataModel();
		setLinkAL(new ArrayList<Link>());
		setFilterTitle("");
		setListTitle("");
		setDetailTitle("");
		removedListRow = -1;
		addedParamToListJs = -1;
		removedParamFromListJs = -1;
		updatedDetailTableOrBreak = -1;
		removedDetailRow = -1;
		linkDeletingAllKeys = "-1,false";
		filterPanelStyle = "visible";
		listPanelStyle = "hidden";
		detailPanelStyle = "hidden";
		linkPanelStyle = "hidden";
		selectedEntityDisabled = false;
		// se sono in modifica preseleziono l'entità per cui ci sono colonne in DV_FORMAT_CLASSE
		FacesContext context = FacesContext.getCurrentInstance();
		Map reqParMap = context.getExternalContext().getRequestParameterMap();
		if (!reqParMap.containsKey("mode")) return;
		String mode = (String) reqParMap.get("mode");
		if (mode.equals(UPDATE)) {
			Long id = reqParMap.get("id") == null || ((String)reqParMap.get("id")).equals("") ?
					null : 
					new Long((String)reqParMap.get("id"));
			try {
				HashMap<String, ? extends Object> selEntForCla = dem.getSelectedEntityAndFormatClasse(id);
				selectedEntity = (String)selEntForCla.get(DefinizioneEntitaModel.SELECTED_ENTITY);
				FormatClasse fc = (FormatClasse)selEntForCla.get(DefinizioneEntitaModel.FORMAT_CLASSE);
				setFilterAL(fc.getFilter()); //filtro
				setListAL(fc.getList()); //lista
				setDetailAL(fc.getDetail()); //dettaglio
				setLinkAL(fc.getLink()); //link
				setFilterTitle((String)selEntForCla.get(DefinizioneEntitaModel.FILTER_TITLE)); //titolo filtro
				setListTitle((String)selEntForCla.get(DefinizioneEntitaModel.LIST_TITLE)); //titolo lista
				setDetailTitle((String)selEntForCla.get(DefinizioneEntitaModel.DETAIL_TITLE)); //titolo dettaglio
				selectedEntityDisabled = dem.hasClassLinks(null, id, true) || dem.hasClassLinks(null, id, false);
			}catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile selezionare l'entità e caricare le colonne"));
				setPopupError(true);
				e.printStackTrace();
			}
			getColumnsFromChangedEntity(selectedEntity);
			matchFilterColumns();
		}
	}
	
	/**
	 * Effettua i controlli preliminari e chiama il metodo di DefinizioneEntitaModel che effettua il salvataggio dei 
	 * dati nel DB. Quindi aggiorna la struttura ad albero della pagina chiamante.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Salva" della popup.
	 * @throws AbortProcessingException Se si verifica una qualsiasi eccezione durante l'esecuzione del metodo.
	 */
	public void salva (ActionEvent event) throws AbortProcessingException {
		if (name == null || name.length() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Inserire un valore per il campo Nome"));
			setPopupError(true);
			return;
		}
		if (name != null && name.length() > 50) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile inserire un nome lungo più di 50 caratteri"));
			setPopupError(true);
			return;
		}
		if (getSelectedEntity() == null || getSelectedEntity().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Selezionare un'entità"));
			setPopupError(true);
			return;
		}
		String titlesMsg = checkTitles();
		if (titlesMsg != null && !titlesMsg.equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(titlesMsg));
			setPopupError(true);
			return;
		}
		if (!checkJs()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nella lista sono presenti javascript incompleti: impossibile procedere"));
			setPopupError(true);
			return;
		}
		if (!checkJsHeaders()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Non è possibile inserire due javascript che abbiano lo stesso nome colonna"));
			setPopupError(true);
			return;
		}
		if (!checkJsImageOrTest()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Non è possibile inserire un javascript che abbia valorizzati sia testo che URL immagine"));
			setPopupError(true);
			return;
		}
		for (Link link : linkAL) {
			if (link.isSelected() && (link.getLinkName() == null || link.getLinkName().equals(""))) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("E' necessario specificare un nome per tutti i link selezionati"));
				setPopupError(true);
				return;
			}
		}
		for (Link link : linkAL) {
			if (link.isSelected() && link.getLinkName() != null && link.getLinkName().length() > 100) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Non è possibile inserire link che abbiano un nome lungo più di 100 caratteri"));
				setPopupError(true);
				return;
			}
		}
		try {
			clazz.setName(name);
			if (!dem.isNameFree(clazz)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Una classe dello stesso livello con lo stesso nome è già stata inserita"));
				setPopupError(true);
				return;
			}
			FormatClasse fc = new FormatClasse();
			Long entityId = getEntitiesHashMap().get(getSelectedEntity()).getId();
			fc.setDvUserEntity(dem.getUserEntityIdFromEntityId(entityId)); //id user entity
			fc.setFilter(filterAL); //filtro
			fc.setList(listAL); //lista
			fc.setDetail(detailAL); //dettaglio
			fc.setLink(linkAL); //link
			fc.setFilterTitle(filterTitle); //titolo filtro
			fc.setListTitle(listTitle); //titolo lista
			fc.setDetailTitle(detailTitle); //titolo dettaglio
			dem.saveClazzAndFormat(clazz, fc);
			debb.setProjectsClasses(dem.getProjectsClasses(debb.getProjectsClasses(), debb.getAllProjectsClasses(), true));
			MenuRenderTag.svuotaCache();
			if (clazz.getId() != null)
				PageManager.svuotaCache(clazz.getId());
		} catch (Exception e) {
			String message = "Errore nel salvataggio dei dati: ";
			message += e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			e.printStackTrace();
			setPopupError(true);
			return;
		}
	}
	
	/**
	 * Legge l'evento di modifica della selezione dell'entità e chiama getColumnsFromChangedEntity() per la reimpostazione 
	 * dei criteri di filtro.
	 * @param event L'oggetto che rappresenta l'evento di modifica della selezione dell'entità nella combo box relativa.
	 */
	public void entityChanged(ValueChangeEvent event) {
		String newValStr = (String)event.getNewValue();
		filterDM = new ListDataModel();
		setFilterAL(new ArrayList<Filter>());
		listDM = new ListDataModel();
		setListAL(new ArrayList<FcList>());
		detailDM = new ListDataModel();
		setDetailAL(new ArrayList<DetailGroup>());
		linkDM = new ListDataModel();
		setLinkAL(new ArrayList<Link>());
		try {
			if (!newValStr.equals("")) {
				setLinkAL(dem.getLinkAL(null, clazz.getId(), getEntitiesHashMap().get(newValStr).getId()));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		getColumnsFromChangedEntity(newValStr);
	}
	
	/** 
	 * Alla modifica della selezione dell'entità nella combo box relativa, reimposta la lista delle colonne disponibili 
	 * per il filtro.
	 * @param selectedEntity Il nome dell'entità selezionata nella combo box relativa
	 */
	private void getColumnsFromChangedEntity(String selectedEntity) {
		try {
			if (selectedEntity != null && !selectedEntity.equals("")) {
				HashMap<String, ? extends Object> hm = dem.getColumnsForFilter(getEntitiesHashMap().get(selectedEntity).getId());
				ArrayList newCols = (ArrayList)hm.get(DefinizioneEntitaModel.COLUMNS);
				HashMap newOps = (HashMap)hm.get(DefinizioneEntitaModel.OPERATORS);
				setFilterColumns(getSelectItemArrayList(newCols));
				setAllColumns(new ArrayList<SelectItem>());
				//salvo l'elenco delle colonne presenti (e disponibili per la lista...) per l'entità selezionata
				for (SelectItem si : getFilterColumns()) {
					allColumns.add(new SelectItem(si.getValue().toString()));
				}
				setTypeOperators(getGenericValueHashMap(newOps));
			} else {
				setFilterColumns(new ArrayList<SelectItem>());
				setAllColumns(new ArrayList<SelectItem>());
				setTypeOperators(new HashMap<String, ArrayList<GenericValue>>());
			}
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare le colonne per l'entità selezionata"));
			e.printStackTrace();
			setPopupError(true);
		}
	}
	
	/**
	 * Controlla se sono stati inseriti i titoli di filtro, lista e dettaglio. Se sono stati inseriti tutti e tre 
	 * restituisce null, viceversa restituisce il messaggio di errore appropriato.
	 * @return Una String che rappresenta il messaggio di errore appropriato (null se non c'è errore).
	 */
	private String checkTitles() {
		String retVal = null;
		boolean[] errs = new boolean[3];
		String[] strs = new String[3];
		errs[0] = filterTitle == null || filterTitle.equals("");
		errs[1] = listTitle == null || listTitle.equals("");
		errs[2] = detailTitle == null || detailTitle.equals("");
		strs[0] = "del filtro";
		strs[1] = "della lista";
		strs[2] = "del dettaglio";
		int errCount = 0;
		for (boolean b : errs) {
			if (b)
				errCount++;
		}
		if (errCount > 0) {
			int errIndex = 0;
			if (errCount == 1) {
				retVal = "Deve essere inserito il titolo ";
				for (boolean b : errs) {
					if (b) {
						retVal += strs[errIndex];
						break;
					}
					errIndex++;
				}
			}else{
				retVal = "Devono essere inseriti i titoli ";
				int myErrCount = 0;
				for (boolean b : errs) {
					if (b) {
						myErrCount++;
						if (myErrCount > 1 && myErrCount < errCount)
							retVal += ", ";
						else if (myErrCount == errCount)
							retVal += " e ";
						retVal += strs[errIndex];
					}
					errIndex++;
				}
			}
		}
		return retVal;
	}
	
	/**
	 * Aggiunge la colonna selezionata nella lista relativa alla tabella che contiene le colonne disponibili per il 
	 * filtro.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Aggiungi" (colonna) della popup.
	 */
	public void addFilterColumn(ActionEvent event){
		if (selectedFilterColumn == null || selectedFilterColumn.equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Non è stata selezionata nessuna colonna dalla lista"));
			setPopupError(true);
			return;
		}
		Filter filt = new Filter();
		filt.setDcColumnName(selectedFilterColumn);
		ArrayList<GenericValue> algv = typeOperators.get(selectedFilterColumn);
		filt.setOperatorsGV(algv);
		//per default le checkbox sono tutte selezionate
		String[] selOps = new String[algv.size()];
		for (int i = 0; i < selOps.length; i++) {
			selOps[i] = algv.get(i).getValueDesc();
		}
		filt.setSelOperators(selOps);
		filt.setSelectedValueType(DefinizioneEntitaModel.VALORE_LIBERO);
		filt.setSqlCommand("");
		filt.setValues(new ArrayList<SelectItem>());
		filt.setSelectedValue("");
		filt.setValueId("");
		filt.setValueDesc("");
		
		filterAL.add(filt);
		matchFilterColumns();
	}
	
	/**
	 * Rimuove la colonna corrente dalla tabella che contiene le colonne disponibili per il filtro.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Elimina" nella tabella delle colonne 
	 * disponibili per il filtro.
	 */
	public void removeFilterColumn(ActionEvent event) {
		if (removedFilterColumn == null || removedFilterColumn.equals("")) {
			return;
		}
		filterColumns.add(new SelectItem(removedFilterColumn));
		for (Filter filt : filterAL) {
			if (filt.getDcColumnName().equalsIgnoreCase(removedFilterColumn)) {
				filterAL.remove(filt);
				break;
			}
		}
		matchFilterColumns();
		removedFilterColumn = "";
	}
	
	/**
	 * Effettuati i controlli preliminari, aggiunge un nuovo valore alla lista dei valori fissi disponibili per una 
	 * colonna del filtro.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Aggiungi" (valore fisso) nella 
	 * tabella delle colonne disponibili per il filtro.
	 */
	public void addValueToFilterColumn(ActionEvent event) {
		Filter myFilt = null;
		for (Filter filt : filterAL) {
			if (filt.getDcColumnName().equals(addedValueToFilterColumn)) {
				myFilt = filt;
				break;
			}
		}
		// controlli preliminari
		// se inserito id o descrizione null
		if (myFilt.getValueId() == null || myFilt.getValueId().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Identificativo del valore non inserito"));
			setPopupError(true);
			return;
		}
		if (myFilt.getValueDesc() == null || myFilt.getValueDesc().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Descrizione del valore non inserita"));
			setPopupError(true);
			return;
		}
		// se già inserito un elemento con lo stesso id o la stessa descrizione
		ArrayList<GenericValue> values = myFilt.getValuesGV();
		for (GenericValue gv : values) {
			if (gv.getIdAsString().equals(myFilt.getValueId())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Un valore con lo stesso identificativo è già stato inserito"));
				setPopupError(true);
				return;
			}
			if (gv.getValueDesc().equals(myFilt.getValueDesc())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Un valore con la stessa descrizione è già stato inserito"));
				setPopupError(true);
				return;
			}
		}
		//inserimento nuovo valore
		values.add(new GenericValue(myFilt.getValueId(), myFilt.getValueDesc()));
		myFilt.setValueId("");
		myFilt.setValueDesc("");
		addedValueToFilterColumn = "";
	}
	
	/**
	 * Elimina un valore dalla lista dei valori fissi disponibili per una colonna del filtro.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Togli selezionato" nella tabella 
	 * delle colonne disponibili per il filtro.
	 */
	public void removeValueFromFilterColumn(ActionEvent event) {
		Filter myFilt = null;
		for (Filter filt : filterAL) {
			if (filt.getDcColumnName().equals(removedValueFromFilterColumn)) {
				myFilt = filt;
				break; 
			}
		}
		if (myFilt.getSelectedValue() == null || myFilt.getSelectedValue().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Non è stato selezionato nessun valore"));
			setPopupError(true);
			return;
		}
		ArrayList<GenericValue> valuesGV = myFilt.getValuesGV();
		for (GenericValue gv : valuesGV) {
			String item = gv.getIdAsString() + Filter.VALUES_SEPARATOR + gv.getValueDesc(); 
			if (item.equals(myFilt.getSelectedValue())) {
				valuesGV.remove(gv);
				break;
			}
		}
		removedValueFromFilterColumn = "";
	}
	
	/**
	 * Aggiunge una colonna all'elenco degli elementi (colonne e javascript) disponibili per la lista.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Aggiungi campo" (colonna) della popup.
	 */
	public void addListColumn(ActionEvent event) {
		//controllo se è stata selezionata un'entità e se ci sono colonne disponibili
		if (getSelectedEntity() == null || getSelectedEntity().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Selezionare un'entità"));
			setPopupError(true);
			return;
		}
		if (allColumns == null || allColumns.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nessuna colonna disponibile per l'entità selezionata"));
			setPopupError(true);
			return;
		}
		//controllo se tutte le colonne sono già state aggiunte alla lista, in questo caso non procedo
		int columnCount = 0;
		for (FcList fcl : listAL) {
			if (!fcl.isJs()) {
				columnCount++;
			}
		}
		if (columnCount == allColumns.size()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sono già state aggiunte alla lista tutte le colonne"));
			setPopupError(true);
			return;
		}
		FcList newFcList = new FcList();
		newFcList.setIndex(-1); //lo reimposta checkListAL()
		newFcList.setColumns(getSelectItemArrayList(allColumns));
		newFcList.setSelectedColumn(""); //la reimposta checkListAL()
		newFcList.setJs(false);
		newFcList.setHeader("");
		newFcList.setScript("");
		newFcList.setImageurl("");
		newFcList.setTesturl("");
		newFcList.setParams(new ArrayList<SelectItem>());
		newFcList.setSelectedParam("");
		newFcList.setParamColumns(new ArrayList<SelectItem>());
		newFcList.setSelectedParamColumn("");
		listAL.add(newFcList);
		checkListAL();
	}
	
	/**
	 * Aggiunge un javascript all'elenco degli elementi (colonne e javascript) disponibili per la lista.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Aggiungi JS" della popup.
	 */
	public void addListJs(ActionEvent event) {
		//controllo se è stata selezionata un'entità e se ci sono colonne disponibili
		if (getSelectedEntity() == null || getSelectedEntity().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Selezionare un'entità"));
			setPopupError(true);
			return;
		}
		if (allColumns == null || allColumns.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nessuna colonna disponibile per l'entità selezionata"));
			setPopupError(true);
			return;
		}
		FcList newFcList = new FcList();
		newFcList.setIndex(-1); //lo reimposta checkListAL()
		newFcList.setColumns(new ArrayList<SelectItem>());
		newFcList.setSelectedColumn("");
		newFcList.setJs(true);
		newFcList.setHeader("");
		newFcList.setScript("");
		newFcList.setImageurl("");
		newFcList.setTesturl("");
		newFcList.setParams(new ArrayList<SelectItem>());
		newFcList.setSelectedParam("");
		newFcList.setParamColumns(getSelectItemArrayList(allColumns));
		newFcList.setSelectedParamColumn(""); //la reimposta checkListAL()
		listAL.add(newFcList);
		checkListAL();
	}
	
	/**
	 * Rimuove l'elemento corrente (colonna o javascript) dalla tabella che contiene gli elementi disponibili per la 
	 * lista.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Rimuovi" nella tabella degli elementi 
	 * disponibili per la lista.
	 */
	public void removeListRow(ActionEvent event) {
		listAL.remove(getRemovedListRow());
		checkListAL();
	}
	
	/**
	 * Aggiunge un nuovo parametro all'elenco dei parametri specificati per un javascript disponibile per la lista.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Aggiungi campo" in una delle celle 
	 * della tabella degli elementi (se javascript) disponibili per la lista.
	 */
	public void addParamToListJs(ActionEvent event) {
		FcList myFcList = listAL.get(getAddedParamToListJs());
		myFcList.getParams().add(new SelectItem(myFcList.getSelectedParamColumn()));
		myFcList.setSelectedParamColumn("");
		addedParamToListJs = -1;
		checkListAL();
	}
	
	/**
	 * Elimina il parametro selezionato dall'elenco dei parametri specificati per un javascript disponibile per la lista.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Togli selezionato" in una delle celle 
	 * della tabella degli elementi (se javascript) disponibili per la lista.
	 */
	public void removeParamFromListJs(ActionEvent event) {
		FcList myFcList = listAL.get(getRemovedParamFromListJs());
		ArrayList<SelectItem> myParams = myFcList.getParams();
		for (SelectItem si : myParams) {
			if (si.getValue().toString().equals(myFcList.getSelectedParam())) {
				myParams.remove(si);
				break;
			}
		}
		myFcList.setSelectedParam("");
		removedParamFromListJs = -1;
		checkListAL();
	}
	
	/**
	 * Aggiunge una tabella all'elenco degli elementi (tabelle e divisioni) disponibili per il dettaglio.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Aggiungi tabella" della popup.
	 */
	public void addDetailTable(ActionEvent event) {
		//controllo se è stata selezionata un'entità e se ci sono colonne disponibili
		if (getSelectedEntity() == null || getSelectedEntity().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Selezionare un'entità"));
			setPopupError(true);
			return;
		}
		if (allColumns == null || allColumns.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nessuna colonna disponibile per l'entità selezionata"));
			setPopupError(true);
			return;
		}
		DetailGroup dg = new DetailGroup();
		dg.setIndex(detailAL == null ? 0 : detailAL.size());
		dg.setTable(true);
		ArrayList<Detail> al = new ArrayList<Detail>();
		Detail d = new Detail();
		d.setIndex(-1); //lo reimposta checkDetailAL()
		d.setFirst(true);
		d.setTable(true);
		d.setTitle("");
		d.setSelectedColumn1("");
		d.setSelectedColumn2("");
		d.setSelectedColumn3("");
		al.add(d);
		dg.setDetailAL(al);
		detailAL.add(dg);
		checkDetailAL();
	}
	
	/**
	 * Aggiunge una divisione all'elenco degli elementi (tabelle e divisioni) disponibili per il dettaglio.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Aggiungi divisione" della popup.
	 */
	public void addDetailBreak(ActionEvent event) {
		//controllo se è stata selezionata un'entità e se ci sono colonne disponibili
		if (getSelectedEntity() == null || getSelectedEntity().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Selezionare un'entità"));
			setPopupError(true);
			return;
		}
		if (allColumns == null || allColumns.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nessuna colonna disponibile per l'entità selezionata"));
			setPopupError(true);
			return;
		}
		DetailGroup dg = new DetailGroup();
		dg.setIndex(detailAL == null ? 0 : detailAL.size());
		dg.setTable(false);
		ArrayList<Detail> al = new ArrayList<Detail>();
		Detail d = new Detail();
		d.setIndex(-1); //lo reimposta checkDetailAL()
		d.setFirst(false); // true solo per le tabelle
		d.setTable(false);
		d.setTitle("");
		d.setSelectedColumn1("");
		d.setSelectedColumn2("");
		d.setSelectedColumn3("");
		al.add(d);
		dg.setDetailAL(al);
		detailAL.add(dg);
		checkDetailAL();
	}
	
	/**
	 * Rimuove una tabella o una divisione dall'elenco degli elementi disponibili per il dettaglio.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Elimina tabella" o "Elimina divisione" 
	 * della popup.
	 */
	public void removeDetailTableOrBreak(ActionEvent event) {
		detailAL.remove(getUpdatedDetailTableOrBreak());
		updatedDetailTableOrBreak = -1;
		checkDetailAL();
	}
	
	/**
	 * Aggiunge una riga a una tabella disponibile per il dettaglio.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Aggiungi riga" della popup.
	 */
	public void addDetailRow(ActionEvent event) {
		DetailGroup dg = detailAL.get(getUpdatedDetailTableOrBreak());
		ArrayList<Detail> al = dg.getDetailAL();
		Detail d = new Detail();
		d.setIndex(-1); //lo reimposta checkDetailAL()
		d.setFirst(false);
		d.setTable(true);
		d.setTitle("");
		d.setSelectedColumn1("");
		d.setSelectedColumn2("");
		d.setSelectedColumn3("");
		al.add(d);
		updatedDetailTableOrBreak = -1;
		checkDetailAL();
	}
	
	/**
	 * Rimuove una riga da una tabella disponibile per il dettaglio.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Elimina riga" della popup.
	 */
	public void removeDetailRow(ActionEvent event) {
		DetailGroup dg = detailAL.get(getUpdatedDetailTableOrBreak());
		ArrayList<Detail> al = dg.getDetailAL();
		al.remove(getRemovedDetailRow());
		updatedDetailTableOrBreak = -1;
		removedDetailRow = -1;
		checkDetailAL();
	}
	
	/**
	 * Per il link deselezionato nella popup, imposta il valore del campo deletingAllKeys come specificato nella 
	 * confirm() relativa.<p>
	 * Questo permette l'eventuale cancellazione delle chiavi relative al link.
	 * @param event L'oggetto che rappresenta l'evento di modifica del valore del campo linkDeletingAllKeys di questa 
	 * classe, causato dall'evento di selezione della checkbox associata ad ogni link.
	 */
	public void selectedLink(ValueChangeEvent event) {
		//scompongo event.getNewValue() (linkDeletingAllKeys)
		String newVal = (String)event.getNewValue();
		StringTokenizer st = new StringTokenizer(newVal, ",");
		Long linkId = null;
		if (st.hasMoreTokens())
			 linkId = new Long(st.nextToken());
		boolean linkFlag = false;
		if (st.hasMoreTokens())
			linkFlag = new Boolean(st.nextToken()).booleanValue();
		if (linkId.longValue() != -1) {
			// linkId.longValue() è != -1 solo se sto deselezionando un link già inserito nel DB
			for (Link link : linkAL) {
				if (link.getLinkId() != null && link.getLinkId().doubleValue() == linkId.longValue()) {
					link.setDeletingAllKeys(linkFlag);
					break;
				}
			}
		}
	}

	public String getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(String selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

	/**
	 * Restituisce la lista delle entità (ordinata per descrizione) sotto forma di ArrayList di SelectItem, da utilizzarsi 
	 * per il caricamento della combo box relativa.
	 * @return ArrayList di SelectItem che rappresenta la lista delle entità, ordinata per descrizione.
	 */
	public ArrayList<SelectItem> getEntities() {
		if (entities == null) return null;
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		if (entities.size() == 0) {
			//inserisco solo l'item vuoto
			retVal.add(new SelectItem(""));
			return retVal;
		}
		Set<String> keys = entities.keySet();
		String[] keysStr = (String[])keys.toArray(new String[keys.size()]);
		Arrays.sort(keysStr);
		for (String keyStr : keysStr) {
			retVal.add(new SelectItem(keyStr));
		}
		//aggiungo l'item vuoto
		if (!((SelectItem)retVal.get(0)).equals(new SelectItem("")))
			retVal.add(0, new SelectItem(""));
		return retVal;
	}

	/** 
	 * Imposta entities con valori recuperati da un'ArrayList di SelectItem.
	 * @param entities Un'ArrayList di SelectItem
	 */
	public void setEntities(ArrayList<SelectItem> entities) {
		if (entities == null) {
			this.entities = null;
			return;
		}
		if (entities.size() == 0) {
			this.entities = new HashMap<String,Table>();
			return;
		}
		for (SelectItem si : entities) {
			Table tab = new Table();
			String value = si.getValue().toString();
			tab.setName(value);
			if (this.entities == null) this.entities = new HashMap<String,Table>();
			this.entities.put(value, tab);
		}
	}
	
	/** 
	 * Imposta entities con l'HashMap parametro.
	 * @param entities Un'HashMap di chiavi di tipo String e valori di tipo Table
	 */
	private void setEntitiesHashMap(HashMap<String, Table> entities) {
		this.entities = entities;
	}
	
	/** 
	 * Restituisce l'oggetto entities (HashMap di chiavi di tipo String e valori di tipo Table)
	 * @return L'oggetto entities (HashMap di chiavi di tipo String e valori di tipo Table)
	 */
	protected HashMap<String, Table> getEntitiesHashMap() {
		return entities;
	}
	
	public ArrayList<Filter> getFilterAL() {		
		//eventuale ordinamento alfabetico (per Filter.getDcColumnName())
		/*if (filterAL == null || filterAL.size() == 0) return filterAL;
		String[] filterALStr = new String[filterAL.size()];
		int index = 0;
		for (Filter filt : filterAL) {
			filterALStr[index] = filt.getDcColumnName();
			index++;
		}
		Arrays.sort(filterALStr);
		ArrayList<Filter> newFilterAL = new ArrayList<Filter>();
		for (String filterStr : filterALStr) {
			for (Filter filt : filterAL) {
				if (filt.getDcColumnName().equalsIgnoreCase(filterStr)) {
					newFilterAL.add(filt);
				}
				break;
			}
		}
		filterAL = newFilterAL;*/
		return filterAL;		
	}

	public void setFilterAL(ArrayList<Filter> filterAL) {
		this.filterAL = filterAL;
		filterDM.setWrappedData(this.filterAL);
	}

	public DataModel getFilterDM() {
		return filterDM;
	}

	public void setFilterDM(DataModel filterDM) {
		this.filterDM = filterDM;
	}
	
	public ArrayList<FcList> getListAL() {
		return listAL;
	}

	/**
	 * Imposta l'ArrayList listAL e il DataModel listDM.<p>
	 * Il nuovo valore per listAL viene controllato tramite chiamata al metodo checkListAL().
	 * @param listAL Il nuovo valore per l'oggetto listAL
	 */
	public void setListAL(ArrayList<FcList> listAL) {
		this.listAL = listAL;
		checkListAL();
		listDM.setWrappedData(this.listAL);
	}
	
	/**
	 * Restituisce il DataModel listDM, dopo aver controllato l'ArrayList listAL tramite chiamata al metodo checkListAL().
	 * @return Il DataModel listDM
	 */
	public DataModel getListDM() {
		checkListAL();
		return listDM;
	}

	public void setListDM(DataModel listDM) {
		this.listDM = listDM;
	}
	
	public ArrayList<DetailGroup> getDetailAL() {
		return detailAL;
	}
	
	/**
	 * Imposta l'ArrayList detailAL e il DataModel detailDM.
	 * Il nuovo valore per detailAL viene controllato tramite chiamata al metodo checkDetailAL().
	 * @param detailAL Il nuovo valore per l'oggetto detailAL
	 */
	public void setDetailAL(ArrayList<DetailGroup> detailAL) {
		this.detailAL = detailAL;
		checkDetailAL();
		detailDM.setWrappedData(this.detailAL);
	}
	
	public ArrayList<Link> getLinkAL() {
		return linkAL;
	}
	
	/**
	 * Imposta l'ArrayList linkAL e il DataModel linkDM.
	 * @param linkAL Il nuovo valore per l'oggetto linkAL
	 */
	public void setLinkAL(ArrayList<Link> linkAL) {
		this.linkAL = linkAL;
		linkDM.setWrappedData(this.linkAL);
	}

	/**
	 * Ordina alfabeticamente l'oggetto filterColumns e lo restituisce.
	 * @return ArrayList di SelectItem corrispondente all'oggetto filterColumns ordinato alfabeticamente
	 */
	public ArrayList<SelectItem> getFilterColumns() {
		// ordinamento alfabetico
		String[] filterColumnsArr = new String[filterColumns.size()];
		for (int i = 0; i < filterColumns.size(); i++) {
			filterColumnsArr[i] = filterColumns.get(i).getValue().toString();
		}			
		Arrays.sort(filterColumnsArr);
		ArrayList<SelectItem> filterColumnsOrd = new ArrayList<SelectItem>();
		for (String filterColumnsStr : filterColumnsArr) {
			for (SelectItem si : filterColumns) {
				if (filterColumnsStr.equals(si.getValue().toString())) {
					filterColumnsOrd.add(si);
					break;
				}					
			}
		}
		filterColumns = filterColumnsOrd;		
		return filterColumns;
	}

	public void setFilterColumns(ArrayList<SelectItem> filterColumns) {
		this.filterColumns = filterColumns;
	}
	
	public ArrayList<SelectItem> getAllColumns() {
		return allColumns;
	}

	public void setAllColumns(ArrayList<SelectItem> allColumns) {
		this.allColumns = allColumns;
	}

	public String getSelectedFilterColumn() {
		return selectedFilterColumn;
	}

	public void setSelectedFilterColumn(String selectedFilterColumn) {
		this.selectedFilterColumn = selectedFilterColumn;
	}

	/**
	 * Effettua il confronto tra la lista delle colonne disponibili per il filtro e quelle in esso già inserite.<p>
	 * Le colonne inserite nel filtro vengono eliminate dalla lista delle colonne disponibili.
	 */
	private void matchFilterColumns() {
		if (filterColumns != null) {
			for (int i = 0; i < filterColumns.size(); i++) {
				SelectItem sic = filterColumns.get(i);
				if (filterAL != null) {
					for (int j = 0; j < filterAL.size(); j++) {
						if (filterAL.get(j).getDcColumnName().equalsIgnoreCase(sic.getValue().toString())) {
							filterColumns.remove(i);
							i--;
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Verifica l'ArrayList listAL: in particolare, controlla i valori dei campi columns e paramColumns di ogni 
	 * oggetto FcList in essa contenuto, per evitare valori duplicati (colonne o colonne parametro di un javascript) 
	 * nelle liste relative.<p>
	 * Inoltre imposta il valore del campo index (usato come chiave) di ogni oggetto FcList.
	 */
	private void checkListAL() {
		if (listAL == null || listAL.size() == 0 || getAllColumns() == null || getAllColumns().size() == 0)
			return;
		int count = 0;
		for (FcList fcl : listAL) {
			fcl.setIndex(count);
			if (!fcl.isJs()) {
				fcl.setColumns(new ArrayList<SelectItem>());
				//verifico le ArrayList columns
				ArrayList<String> notSelectedColumns = new ArrayList<String>();
				ArrayList<String> selectedColumns = new ArrayList<String>();
				for (SelectItem si : allColumns) {
					notSelectedColumns.add(new String(si.getValue().toString()));
				}
				for (FcList checkFcl : listAL) {
					if (!checkFcl.isJs()) {
						selectedColumns.add(new String(checkFcl.getSelectedColumn()));
					}
				}
				for (int i = 0; i < notSelectedColumns.size(); i++) {
					String notSelCol = notSelectedColumns.get(i);
					for (String selCol : selectedColumns) {
						if (notSelCol.equals(selCol)) {
							notSelectedColumns.remove(notSelCol);
							i--;
							break;
						}
					}
				}
				//seleziono per default il primo elemento (se ho inserito una nuova colonna)
				if (fcl.getSelectedColumn() == null || fcl.getSelectedColumn().equals("")) {
					fcl.setSelectedColumn(new String(notSelectedColumns.get(0)));
					notSelectedColumns.remove(0);
					selectedColumns.add(new String(fcl.getSelectedColumn()));
				}
				//imposto l'ArrayList columns
				ArrayList<SelectItem> columns = fcl.getColumns();
				for (SelectItem si : allColumns) {
					String siVal = si.getValue().toString();
					if (siVal.equals(fcl.getSelectedColumn())) {
						columns.add(new SelectItem(siVal));
					}else{
						for (String notSelCol : notSelectedColumns) {
							if (siVal.equals(notSelCol)) {
								columns.add(new SelectItem(siVal));
								break;
							}
						}
					}
				}
			}else{
				fcl.setParamColumns(new ArrayList<SelectItem>());
				//verifico le ArrayList paramColumns
				ArrayList<SelectItem> params = fcl.getParams();
				ArrayList<String> notSelectedColumns = new ArrayList<String>();
				ArrayList<String> selectedColumns = new ArrayList<String>();
				for (SelectItem si : allColumns) {
					notSelectedColumns.add(new String(si.getValue().toString()));
				}
				for (SelectItem param : params) {
					selectedColumns.add(new String(param.getValue().toString()));
				}
				for (int i = 0; i < notSelectedColumns.size(); i++) {
					String notSelCol = notSelectedColumns.get(i);
					for (String selCol : selectedColumns) {
						if (notSelCol.equals(selCol)) {
							notSelectedColumns.remove(notSelCol);
							i--;
							break;
						}
					}
				}
				//seleziono il primo elemento
				fcl.setSelectedParamColumn(new String(notSelectedColumns.get(0)));
				//imposto l'ArrayList paramColumns
				ArrayList<SelectItem> paramColumns = fcl.getParamColumns();
				for (SelectItem si : allColumns) {
					String siVal = si.getValue().toString();
					for (String notSelCol : notSelectedColumns) {
						if (siVal.equals(notSelCol)) {
							paramColumns.add(new SelectItem(siVal));
							break;
						}
					}
				}
			}
			count++;
		}
	}
	
	/**
	 * Verifica l'ArrayList detailAL: in particolare, imposta il valore dei campi columns1, columns2 e columns3 
	 * (ArrayList di SelectItem) di ogni oggetto Detail che sia contenuto in una tabella.<p>
	 * Inoltre imposta il valore del campo index (usato come chiave) di ogni oggetto DetailGroup e Detail.
	 */
	private void checkDetailAL() {
		if (detailAL == null || detailAL.size() == 0 || getAllColumns() == null || getAllColumns().size() == 0)
			return;
		int groupCount = 0;
		for (DetailGroup dg : detailAL) {
			dg.setIndex(groupCount);
			ArrayList<Detail> groupDetailAL = dg.getDetailAL();
			if (groupDetailAL != null && groupDetailAL.size() > 0) {
				int rowCount = 0;
				for (Detail d : groupDetailAL) {
					d.setIndex(rowCount);
					d.setColumns1(new ArrayList<SelectItem>());
					d.setColumns2(new ArrayList<SelectItem>());
					d.setColumns3(new ArrayList<SelectItem>());
					if (dg.isTable() && rowCount > 0) {
						for (SelectItem si : allColumns) {
							String value = si.getValue().toString();
							d.getColumns1().add(new SelectItem(value));
							d.getColumns2().add(new SelectItem(value));
							d.getColumns3().add(new SelectItem(value));
						}
						d.getColumns1().add(0, new SelectItem(""));
						d.getColumns2().add(0, new SelectItem(""));
						d.getColumns3().add(0, new SelectItem(""));
					}
					rowCount++;
				}
			}
			groupCount++;
		}
	}
	
	/**
	 * Verifica se tutti i javascript inseriti nella lista sono correttamente valorizzati (se sono stati inseriti i 
	 * valori nei campi di testo relativi della popup).
	 * @return Un flag che indica se tutti i javascript inseriti nella lista sono correttamente valorizzati
	 */
	private boolean checkJs() {
		if (listAL == null || listAL.size() == 0)
			return true;
		for (FcList fcl : listAL) {
			if (fcl.isJs()) {
				if (fcl.getHeader() == null || fcl.getHeader().equals("")) return false;
				if (fcl.getScript() == null || fcl.getScript().equals("")) return false;
				if (fcl.getImageurl() == null || fcl.getImageurl().equals("")) {
					if (fcl.getTesturl() == null || fcl.getTesturl().equals("")) return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Verifica se i javascript inseriti nella lista hanno ognuno un header diverso (l'header ha funzione di chiave).
	 * @return Un flag che indica se i javascript inseriti nella lista hanno ognuno un header diverso
	 */
	private boolean checkJsHeaders() {
		if (listAL == null || listAL.size() == 0)
			return true;
		int count = 0;
		for (FcList fcl : listAL) {
			if (fcl.isJs()) {
				int checkCount = 0;
				for (FcList checkFcl : listAL) {
					if (checkFcl.isJs()) {
						if (count != checkCount && fcl.getHeader().equalsIgnoreCase(checkFcl.getHeader()))
							return false;
					}
					checkCount++;
				}
			}
			count++;
		}
		return true;
	}
	
	/**
	 * Verifica se i javascript inseriti nella lista hanno tutti valorizzato solo uno tra testo e URL immagine.
	 * @return Un flag che indica se i javascript inseriti nella lista hanno tutti valorizzato solo uno tra testo e 
	 * URL immagine.
	 */
	private boolean checkJsImageOrTest() {
		if (listAL == null || listAL.size() == 0)
			return true;
		for (FcList fcl : listAL) {
			if (fcl.getImageurl() != null && !fcl.getImageurl().equals("")) {
				if (fcl.getTesturl() != null && !fcl.getTesturl().equals("")) return false;
			}
		}
		return true;
	}

	public String getRemovedFilterColumn() {
		return removedFilterColumn;
	}

	public void setRemovedFilterColumn(String removedFilterColumn) {
		this.removedFilterColumn = removedFilterColumn;
	}
	
	/**
	 * Metodo di utilità per evitare le eccezioni di type safety nell'uso dei Generics di Java 5.0.<p>
	 * Data un'ArrayList (generica), restituisce la corrispondente ArrayList di SelectItem.<p>
	 * è utilizzato anche per la clonazione delle ArrayList di SelectItem.
	 * @param al ArrayList generica
	 * @return La corrispondente ArrayList di SelectItem
	 */
	private ArrayList<SelectItem> getSelectItemArrayList(ArrayList al) {
		//per evitare le eccezioni 
		if (al == null) return null;
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		if (al.size() == 0) return retVal;
		for (Object obj : al) {
			//si dà per scontato che il casting non dia eccezioni
			retVal.add((SelectItem)obj);
		}
		return retVal;
	}
	
	/**
	 * Metodo di utilità per evitare le eccezioni di type safety nell'uso dei Generics di Java 5.0.<p>
	 * Data un'HashMap (generica), restituisce la corrispondente HashMap di chiavi String e valori ArrayList di 
	 * GenericValue.
	 * @param hm HashMap generica
	 * @return La corrispondente HashMap di chiavi String e valori ArrayList di GenericValue
	 */
	private HashMap<String, ArrayList<GenericValue>> getGenericValueHashMap(HashMap hm) {
		//per evitare le eccezioni di type safety
		if (hm == null) return null;
		HashMap<String, ArrayList<GenericValue>> retVal = new HashMap<String, ArrayList<GenericValue>>();
		if (hm.size() == 0) return retVal;
		Set keySet = hm.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			//si dà per scontato che i casting non diano eccezioni
			String key = (String)it.next();
			ArrayList al = (ArrayList)hm.get(key);
			ArrayList<GenericValue> algv = new ArrayList<GenericValue>();
			for (Object obj : al) {
				//si dà per scontato che il casting non dia eccezioni
				algv.add((GenericValue)obj);
			}
			retVal.put(key, algv);
		}
		return retVal;
	}

	public HashMap<String, ArrayList<GenericValue>> getTypeOperators() {
		return typeOperators;
	}

	public void setTypeOperators(HashMap<String, ArrayList<GenericValue>> typeOperators) {
		this.typeOperators = typeOperators;
	}

	public String getAddedValueToFilterColumn() {
		return addedValueToFilterColumn;
	}

	public void setAddedValueToFilterColumn(String addedValueToFilterColumn) {
		this.addedValueToFilterColumn = addedValueToFilterColumn;
	}

	public String getRemovedValueFromFilterColumn() {
		return removedValueFromFilterColumn;
	}

	public void setRemovedValueFromFilterColumn(String removedValueFromFilterColumn) {
		this.removedValueFromFilterColumn = removedValueFromFilterColumn;
	}
	
	public int getAddedParamToListJs() {
		return addedParamToListJs;
	}

	public void setAddedParamToListJs(int addedParamToListJs) {
		this.addedParamToListJs = addedParamToListJs;
	}

	public int getRemovedListRow() {
		return removedListRow;
	}

	public void setRemovedListRow(int removedListRow) {
		this.removedListRow = removedListRow;
	}

	public int getRemovedParamFromListJs() {
		return removedParamFromListJs;
	}

	public void setRemovedParamFromListJs(int removedParamFromListJs) {
		this.removedParamFromListJs = removedParamFromListJs;
	}

	public String getDetailPanelStyle() {
		return detailPanelStyle;
	}

	public void setDetailPanelStyle(String detailPanelStyle) {
		this.detailPanelStyle = detailPanelStyle;
	}

	public String getFilterPanelStyle() {
		return filterPanelStyle;
	}

	public void setFilterPanelStyle(String filterPanelStyle) {
		this.filterPanelStyle = filterPanelStyle;
	}

	public String getListPanelStyle() {
		return listPanelStyle;
	}

	public void setListPanelStyle(String listPanelStyle) {
		this.listPanelStyle = listPanelStyle;
	}

	public String getDetailTitle() {
		return detailTitle;
	}

	public void setDetailTitle(String detailTitle) {
		this.detailTitle = detailTitle;
	}

	public String getFilterTitle() {
		return filterTitle;
	}

	public void setFilterTitle(String filterTitle) {
		this.filterTitle = filterTitle;
	}

	public String getListTitle() {
		return listTitle;
	}

	public void setListTitle(String listTitle) {
		this.listTitle = listTitle;
	}
	
	/**
	 * Restituisce il DataModel detailDM, dopo aver controllato l'ArrayList detailAL tramite chiamata al metodo checkDetailAL().
	 * @return Il DataModel detailDM
	 */
	public DataModel getDetailDM() {
		checkDetailAL();
		return detailDM;
	}

	public void setDetailDM(DataModel detailDM) {
		this.detailDM = detailDM;
	}
	
	public DataModel getLinkDM() {
		return linkDM;
	}

	public void setLinkDM(DataModel linkDM) {
		this.linkDM = linkDM;
	}

	public int getRemovedDetailRow() {
		return removedDetailRow;
	}

	public void setRemovedDetailRow(int removedDetailRow) {
		this.removedDetailRow = removedDetailRow;
	}

	public int getUpdatedDetailTableOrBreak() {
		return updatedDetailTableOrBreak;
	}

	public void setUpdatedDetailTableOrBreak(int updatedDetailTableOrBreak) {
		this.updatedDetailTableOrBreak = updatedDetailTableOrBreak;
	}

	public String getLinkPanelStyle() {
		return linkPanelStyle;
	}

	public void setLinkPanelStyle(String linkPanelStyle) {
		this.linkPanelStyle = linkPanelStyle;
	}

	public boolean isSelectedEntityDisabled() {
		return selectedEntityDisabled;
	}

	public void setSelectedEntityDisabled(boolean selectedEntityDisabled) {
		this.selectedEntityDisabled = selectedEntityDisabled;
	}

	public String getLinkDeletingAllKeys() {
		return linkDeletingAllKeys;
	}

	public void setLinkDeletingAllKeys(String linkDeletingAllKeys) {
		this.linkDeletingAllKeys = linkDeletingAllKeys;
	}
	
}
