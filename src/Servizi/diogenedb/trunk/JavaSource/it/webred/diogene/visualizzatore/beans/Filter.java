package it.webred.diogene.visualizzatore.beans;

import it.webred.diogene.visualizzatore.model.DefinizioneEntitaModel;

import java.util.*;

import javax.faces.model.SelectItem;

/**
 * Bean che contiene i dati per la gestione dei parametri di filtro per una classe (definizione del valore per il 
 * campo FILTER, di tipo XMLTYPE, della tabella DV_FORMAT_CLASSE).<p>
 * Ogni colonna aggiunta al filtro definirà una parte del file XML con un proprio bean di classe Filter.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class Filter {
	
	/**
	 * Identificativo della colonna aggiunta al filtro.
	 */
	private Long dcColumn;
	/**
	 * Descrizione estesa della colonna aggiunta al filtro.
	 */
	private String dcColumnName;
	/**
	 * Array di oggetti String che indica la selezione effettuata nella lista di CheckBox degli operatori.
	 */
	private String[] selOperators;
	/**
	 * Array di oggetti String che rappresenta la selezione precedentemente effettuata nella lista di CheckBox degli operatori.<p>
	 * Utilizzato per controlli sulla modifica della selezione nella lista.
	 */
	private String[] oldSelOperators;
	/**
	 * ArrayList di GenericValue, per la definizione della lista di CheckBox degli operatori.
	 */
	private ArrayList<GenericValue> operators;
	/**
	 * ArrayList di SelectItem, per la definizione della combo box di selezione dell'operatore di default.
	 */
	private ArrayList<SelectItem> operatorsForDefault;
	/**
	 * String che rappresenta l'elemento scelto nella combo box di selezione dell'operatore di default.
	 */
	private String selectedOperatorForDefault;
	/**
	 * ArrayList di SelectItem, per la definizione della combo box di selezione del tipo di valori. Si tratta di 
	 * una lista fissa, definita staticamente nella classe DefinizioneEntitaModel.
	 */
	private ArrayList<SelectItem> valueTypes = DefinizioneEntitaModel.getValueTypes();
	/**
	 * String che rappresenta l'elemento scelto nella combo box di selezione del tipo di valori.
	 */
	private String selectedValueType;
	/**
	 * String che rappresenta l'elemento precedentemente scelto nella combo box di selezione del tipo di valori.<p>
	 * Utilizzato per controlli sulla modifica della selezione nella combo box.
	 */
	private String oldSelectedValueType = "";
	/**
	 * Flag che indica se nella cella "Valori" deve essere visibile la check box "Scelta obbligatoria".
	 */
	private boolean renderCheckbox;
	/**
	 * Flag che indica se nella cella "Valori" deve essere visibile la text area per l'inserimento del valore come 
	 * stringa SQL.
	 */
	private boolean renderTextarea;
	/**
	 * Flag che indica se nella cella "Valori" devono essere visibili la lista contenente i valori fissi (GenericValue) 
	 * nonché i campi di testo ed i pulsanti associati (per inserimento e cancellazione di valori).
	 */
	private boolean renderList;
	/**
	 * Flag che indica se è selezionata la check box "Scelta obbligatoria".
	 */
	private boolean mandatory;
	/**
	 * Valore inserito come stringa SQL nella text area apposita.
	 */
	private String sqlCommand;
	/**
	 * ArrayList di GenericValue, per la definizione della lista contenente i valori fissi.
	 */
	private ArrayList<GenericValue> values;
	/**
	 * String che rappresenta l'elemento scelto nella lista contenente i valori fissi.
	 */
	private String selectedValue;
	/**
	 * Identificativo del GenericValue da aggiungere alla lista contenente i valori fissi.
	 */
	private String valueId;
	/**
	 * Valore (descrizione) del GenericValue da aggiungere alla lista contenente i valori fissi.
	 */
	private String valueDesc;	
	/**
	 * Separatore tra identificativo e valore per i singoli GenericValue inclusi nella lista contenente i valori fissi.
	 */
	public static String VALUES_SEPARATOR = " - ";
	
	public Long getDcColumn() {
		return dcColumn;
	}
	
	public void setDcColumn(Long dcColumn) {
		this.dcColumn = dcColumn;
	}
	
	public String getDcColumnName() {
		return dcColumnName;
	}
	
	public void setDcColumnName(String dcColumnName) {
		this.dcColumnName = dcColumnName;
	}
	
	/** 
	 * Restituisce la lista degli operatori sotto forma di ArrayList di SelectItem.
	 * @return Un'ArrayList di SelectItem, utilizzata per la creazione della lista di check box per la selezione degli 
	 * operatori.
	 */
	public ArrayList<SelectItem> getOperators() {
		if (operators == null)
			return null;
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		for (GenericValue gv : operators) {
			retVal.add(new SelectItem(gv.getValueDesc()));
		}
		return retVal;
	}
	
	/**
	 * Imposta la lista degli operatori secondo un parametro ArrayList di SelectItem
	 * @param operators Un'ArrayList di SelectItem
	 */
	public void setOperators(ArrayList<SelectItem> operators) {
		//all'atto pratico può essere usato solo per cancellare la lista
		if (operators == null) {
			this.operators = null;
		}else{
			this.operators = new ArrayList<GenericValue>();
			for (SelectItem si : operators) {
				this.operators.add(new GenericValue("", si.getValue().toString()));
			}
		}
	}
	
	/**
	 * Restituisce l'oggetto operators (ArrayList di GenericValue).
	 * @return L'oggetto operators (ArrayList di GenericValue)
	 */
	public ArrayList<GenericValue> getOperatorsGV() {
		return operators; 
	}
	
	/** 
	 * Imposta operators con l'ArrayList di GenericValue parametro.
	 * @param operators Un'ArrayList di GenericValue
	 */
	public void setOperatorsGV(ArrayList<GenericValue> operators) {
		this.operators = operators;
	}	
	
	/**
	 * Se modificata la selezione degli operatori, chiama il metodo che valorizza la lista degli operatori da 
	 * includere nella combo box per la selezione dell'operatore di default. Quindi restituisce la selezione nella 
	 * lista di check box degli operatori.
	 * @return Array di String che rappresenta la selezione nella lista di check box degli operatori.
	 */
	public String[] getSelOperators() {
		if (selOperators == null) {
			oldSelOperators = null;
			return selOperators;
		}
		boolean equals = oldSelOperators == null ? false : selOperators.length == oldSelOperators.length;
		if (equals) {
			for (int i = 0; i < oldSelOperators.length; i++) {
				if (!oldSelOperators[i].equals(selOperators[i])) {
					equals = false;
					break;
				}
			}
		}
		if (!equals)
			setOperatorsForDefaultFromSelected();
		oldSelOperators = new String[selOperators.length];
		for (int i = 0; i < oldSelOperators.length; i++) {
			oldSelOperators[i] = selOperators[i];
		}
		return selOperators;
	}
	
	public void setSelOperators(String[] selOperators) {
		this.selOperators = selOperators;
	}
	
	/**
	 * Data una String che rappresenta il valore di un GenericValue, scorre la lista degli operatori e restituisce 
	 * l'identificativo del GenericValue che ha quel valore, come Object (o null se non è trovata corrispondenza).
	 * @param desc Il valore di un GenericValue compreso nella lista degli operatori.
	 * @return L'identificativo dell'operatore che ha quel valore (come Object).
	 */
	public Object getOperatorIdFromDesc(String desc) {
		for (GenericValue gv : operators) {
			if (gv.getValueDesc().equalsIgnoreCase(desc))
				return gv.getId();
		}
		return null;
	}
	
	/**
	 * Data una String che rappresenta il valore di un GenericValue, scorre la lista degli operatori e restituisce 
	 * l'identificativo del GenericValue che ha quel valore, come String (o null se non è trovata corrispondenza).
	 * @param desc Il valore di un GenericValue compreso nella lista degli operatori.
	 * @return L'identificativo dell'operatore che ha quel valore (come String).
	 */
	public String getOperatorIdFromDescAsString(String desc) {
		for (GenericValue gv : operators) {
			if (gv.getValueDesc().equalsIgnoreCase(desc))
				return gv.getIdAsString();
		}
		return null;
	}
	
	/**
	 * Data una String che rappresenta il valore di un GenericValue, scorre la lista degli operatori e restituisce 
	 * l'identificativo del GenericValue che ha quel valore, come Long (o null se non è trovata corrispondenza).
	 * @param desc Il valore di un GenericValue compreso nella lista degli operatori.
	 * @return L'identificativo dell'operatore che ha quel valore (come Long).
	 */
	public Long getOperatorIdFromDescAsLong(String desc) {
		for (GenericValue gv : operators) {
			if (gv.getValueDesc().equalsIgnoreCase(desc))
				return gv.getIdAsLong();
		}
		return null;
	}

	public ArrayList<SelectItem> getValueTypes() {
		return valueTypes;
	}

	public void setValueTypes(ArrayList<SelectItem> valueTypes) {
		this.valueTypes = valueTypes;
	}

	/**
	 * Restituisce il valore del campo selectedValueType. Se questo è diverso dal valore di oldSelectedValueType 
	 * (cioè se è modificata la selezione) chiama setRenderValues() per la modifica della visualizzazione dei campi 
	 * nella cella "Valori".
	 * @return Il valore del campo selectedValueType
	 */
	public String getSelectedValueType() {
		if (selectedValueType == null) selectedValueType = "";
		if (oldSelectedValueType == null) oldSelectedValueType = "";
		if (!selectedValueType.equals(oldSelectedValueType))
			setRenderValues();
		oldSelectedValueType = selectedValueType;
		return selectedValueType;
	}

	public void setSelectedValueType(String selectedValueType) {
		this.selectedValueType = selectedValueType;
	}
	public boolean isRenderCheckbox() {
		return renderCheckbox;
	}
	public void setRenderCheckbox(boolean renderCheckbox) {
		this.renderCheckbox = renderCheckbox;
	}
	public boolean isRenderList() {
		return renderList;
	}
	public void setRenderList(boolean renderList) {
		this.renderList = renderList;
	}
	public boolean isRenderTextarea() {
		return renderTextarea;
	}
	public void setRenderTextarea(boolean renderTextarea) {
		this.renderTextarea = renderTextarea;
	}
	
	/** 
	 * Restituisce la lista dei valori fissi sotto forma di ArrayList di SelectItem, previo ordinamento per descrizione.<p>
	 * La lista visualizza identificativo e valore (descrizione), separati da VALUES_SEPARATOR.
	 * @return Un'ArrayList di SelectItem, utilizzata per la creazione della lista dei valori fissi
	 */
	public ArrayList<SelectItem> getValues() {
		if (values == null)
			return null;
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		//ordinamento alfabetico per descrizione
		String[] descsToOrder = new String[values.size()];
		for (int i = 0; i < values.size(); i++) {
			descsToOrder[i] = values.get(i).getValueDesc();
		}
		Arrays.sort(descsToOrder);
		//lista con id + descrizione
		for (String desc : descsToOrder) {
			for (GenericValue gv : values) {
				if (gv.getValueDesc().equals(desc)) {
					String item = gv.getIdAsString() + VALUES_SEPARATOR + gv.getValueDesc();
					retVal.add(new SelectItem(item));
					break;
				}
			}
		}
		return retVal;
	}
	
	/**
	 * Imposta la lista dei valori fissi secondo un parametro ArrayList di SelectItem
	 * @param values Un'ArrayList di SelectItem
	 */
	public void setValues(ArrayList<SelectItem> values) {
		//all'atto pratico può essere usato solo per cancellare la lista
		if (values == null) {
			this.values = null;
		}else{
			this.values = new ArrayList<GenericValue>();
			for (SelectItem si : values) {
				this.values.add(new GenericValue("", si.getValue().toString()));
			}
		}
	}
	
	/**
	 * Restituisce l'oggetto values (ArrayList di GenericValue).
	 * @return L'oggetto values (ArrayList di GenericValue)
	 */
	public ArrayList<GenericValue> getValuesGV() {
		return values; 
	}
	
	/** 
	 * Imposta values con l'ArrayList di GenericValue parametro.
	 * @param values Un'ArrayList di GenericValue
	 */
	public void setValuesGV(ArrayList<GenericValue> values) {
		this.values = values;
	}
	
	/**
	 * Alla modifica della selezione nella combo box tipo valori, reimposta la visualizzazione dei campi inclusi 
	 * nella cella "Valori" cancellandone il contenuto (chiamata a resetFields()).
	 */
	private void setRenderValues() {
		if (selectedValueType.equals(DefinizioneEntitaModel.SQL)) {
			setRenderCheckbox(true);
			setRenderTextarea(true);
			setRenderList(false);
		} else if (selectedValueType.equals(DefinizioneEntitaModel.VALORI_FISSI)) {
			setRenderCheckbox(true);
			setRenderTextarea(false);
			setRenderList(true);
		} else if (selectedValueType.equals(DefinizioneEntitaModel.VALORE_LIBERO)) {
			setRenderCheckbox(false);
			setRenderTextarea(false);
			setRenderList(false);
		}
		resetFields();
	}
	
	/**
	 * Cancella il contenuto dei campi inclusi nella cella "Valori".
	 */
	private void resetFields() {
		setMandatory(false);
		setSqlCommand("");
		setValues(new ArrayList<SelectItem>());
		setSelectedValue("");
		setValueId("");
		setValueDesc("");
	}
	
	/**
	 * Alla modifica della selezione nella lista di check box degli operatori, reimposta la combo box per la selezione 
	 * dell'operatore di default e predispone la selezione del suo primo elemento.
	 */
	private void setOperatorsForDefaultFromSelected() {
		operatorsForDefault = new ArrayList<SelectItem>();
		if (selOperators == null || selOperators.length == 0) {
			//solo item vuoto
			operatorsForDefault.add(new SelectItem(""));
			selectedOperatorForDefault = "";
			return;
		}
		for (String selOp : selOperators) {
			operatorsForDefault.add(new SelectItem(selOp));
		}
		//seleziono il primo elemento
		selectedOperatorForDefault = operatorsForDefault.get(0).getValue().toString();
	}
	
	public ArrayList<SelectItem> getOperatorsForDefault() {
		return operatorsForDefault;
	}
	public void setOperatorsForDefault(ArrayList<SelectItem> operatorsForDefault) {
		this.operatorsForDefault = operatorsForDefault;
	}
	public String getSelectedOperatorForDefault() {
		return selectedOperatorForDefault;
	}
	public void setSelectedOperatorForDefault(String selectedOperatorForDefault) {
		this.selectedOperatorForDefault = selectedOperatorForDefault;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public String getSqlCommand() {
		return sqlCommand;
	}
	public void setSqlCommand(String sqlCommand) {
		this.sqlCommand = sqlCommand;
	}
	public String getValueDesc() {
		return valueDesc;
	}
	public void setValueDesc(String valueDesc) {
		this.valueDesc = valueDesc;
	}
	public String getValueId() {
		return valueId;
	}
	public void setValueId(String valueId) {
		this.valueId = valueId;
	}
	public String getSelectedValue() {
		return selectedValue;
	}
	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}
	public String getOldSelectedValueType() {
		return oldSelectedValueType;
	}
	public void setOldSelectedValueType(String oldSelectedValueType) {
		this.oldSelectedValueType = oldSelectedValueType;
	}
	public String[] getOldSelOperators() {
		return oldSelOperators;
	}
	public void setOldSelOperators(String[] oldSelOperators) {
		this.oldSelOperators = oldSelOperators;
	}
	
}
