package it.webred.diogene.visualizzatore.beans;

import java.util.*;

import javax.faces.model.SelectItem;

/**
 * Bean che contiene i dati per la gestione della visualizzazione della lista per una classe (definizione del valore 
 * per il campo LIST, di tipo XMLTYPE, della tabella DV_FORMAT_CLASSE).<p>
 * Ogni colonna (oppure ogni javascript) aggiunta al filtro definirà una parte del file XML con un proprio bean di 
 * classe FcList.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class FcList {
	
	/**
	 * Numerico che rappresenta la posizione del bean nell'ArrayList listAL di DefinizioneClasseBB.<p>
	 * è utilizzato come chiave, non essendovi altri campi che sono presenti sempre (tutti dipendono dal valore 
	 * di js).
	 */
	private int index;
	/**
	 * ArrayList di SelectItem, per la definizione della combo box di selezione colonne (se js = false).
	 */
	private ArrayList<SelectItem> columns;
	/**
	 * String che rappresenta l'elemento scelto nella combo box di selezione colonne (se js = false).
	 */
	private String selectedColumn;
	/**
	 * Flag che indica se il bean contiene i dati di una colonna (se false) o di un javascript (se true)
	 */
	private boolean js;
	/**
	 * Valore del campo header (se js = true)
	 */
	private String header;
	/**
	 * Valore del campo script (se js = true)
	 */
	private String script;
	/**
	 * Valore del campo imageurl (se js = true)
	 */
	private String imageurl;
	/**
	 * Valore del campo testurl (se js = true)
	 */
	private String testurl;
	/**
	 * ArrayList di SelectItem, per la definizione della lista dei parametri (se js = true).
	 */
	private ArrayList<SelectItem> params;
	/**
	 * String che rappresenta l'elemento scelto nella lista dei parametri.
	 */
	private String selectedParam;
	/**
	 * ArrayList di SelectItem, per la definizione della combo box di selezione colonne parametro (se js = true).
	 */
	private ArrayList<SelectItem> paramColumns;
	/**
	 * String che rappresenta l'elemento scelto nella combo box di selezione colonne parametro (se js = true).
	 */
	private String selectedParamColumn;
	
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public ArrayList<SelectItem> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<SelectItem> columns) {
		this.columns = columns;
	}

	public String getSelectedColumn() {
		return selectedColumn;
	}

	public void setSelectedColumn(String selectedColumn) {
		this.selectedColumn = selectedColumn;
	}

	public String getHeader() {
		return header;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public String getImageurl() {
		return imageurl;
	}
	
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	
	public boolean isJs() {
		return js;
	}
	
	public void setJs(boolean js) {
		this.js = js;
	}
	
	public String getScript() {
		return script;
	}
	
	public void setScript(String script) {
		this.script = script;
	}
	
	public String getTesturl() {
		return testurl;
	}

	public void setTesturl(String testurl) {
		this.testurl = testurl;
	}

	public ArrayList<SelectItem> getParams() {
		return params;
	}

	public void setParams(ArrayList<SelectItem> params) {
		this.params = params;
	}

	public String getSelectedParam() {
		return selectedParam;
	}

	public void setSelectedParam(String selectedParam) {
		this.selectedParam = selectedParam;
	}

	public ArrayList<SelectItem> getParamColumns() {
		return paramColumns;
	}

	public void setParamColumns(ArrayList<SelectItem> paramColumns) {
		this.paramColumns = paramColumns;
	}

	public String getSelectedParamColumn() {
		return selectedParamColumn;
	}

	public void setSelectedParamColumn(String selectedParamColumn) {
		this.selectedParamColumn = selectedParamColumn;
	}

}
