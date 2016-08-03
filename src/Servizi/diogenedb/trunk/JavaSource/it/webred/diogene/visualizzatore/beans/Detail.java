package it.webred.diogene.visualizzatore.beans;

import java.util.ArrayList;

import javax.faces.model.SelectItem;

/**
 * Bean che contiene i dati di una riga di una tabella, o una divisione, definita per il dettaglio nella popup di 
 * gestione classe.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class Detail {

	/**
	 * Numerico che rappresenta la posizione del bean nell'ArrayList detailAL dell'oggetto DetailGroup contenitore.<p>
	 * è utilizzato come chiave, non essendovi altri campi che sono presenti sempre.
	 */
	private int index;
	/**
	 * Flag che indica se il bean, qualora incluso in una tabella, rappresenta o meno la prima riga (quella contenente il 
	 * titolo della tabella).
	 */
	private boolean first;
	/**
	 * Flag che indica se il bean è incluso in una tabella (se true) o in una divisione (se false).
	 */
	private boolean table;
	/**
	 * Titolo della tabella (se il bean è incluso in una tabella e ne rappresenta la prima riga).
	 */
	private String title;
	/**
	 * ArrayList di SelectItem, per la definizione della combo box di selezione colonne contenuta nella prima colonna 
	 * (se il bean è incluso in una tabella e non ne rappresenta la prima riga).
	 */
	private ArrayList<SelectItem> columns1;
	/**
	 * String che rappresenta l'elemento scelto nella combo box di selezione colonne contenuta nella prima colonna 
	 * (se il bean è incluso in una tabella e non ne rappresenta la prima riga).
	 */
	private String selectedColumn1;
	/**
	 * ArrayList di SelectItem, per la definizione della combo box di selezione colonne contenuta nella seconda colonna 
	 * (se il bean è incluso in una tabella e non ne rappresenta la prima riga).
	 */
	private ArrayList<SelectItem> columns2;
	/**
	 * String che rappresenta l'elemento scelto nella combo box di selezione colonne contenuta nella seconda colonna 
	 * (se il bean è incluso in una tabella e non ne rappresenta la prima riga).
	 */
	private String selectedColumn2;
	/**
	 * ArrayList di SelectItem, per la definizione della combo box di selezione colonne contenuta nella terza colonna 
	 * (se il bean è incluso in una tabella e non ne rappresenta la prima riga).
	 */
	private ArrayList<SelectItem> columns3;
	/**
	 * String che rappresenta l'elemento scelto nella combo box di selezione colonne contenuta nella terza colonna 
	 * (se il bean è incluso in una tabella e non ne rappresenta la prima riga).
	 */
	private String selectedColumn3;
	

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ArrayList<SelectItem> getColumns1() {
		return columns1;
	}

	public void setColumns1(ArrayList<SelectItem> columns1) {
		this.columns1 = columns1;
	}

	public ArrayList<SelectItem> getColumns2() {
		return columns2;
	}

	public void setColumns2(ArrayList<SelectItem> columns2) {
		this.columns2 = columns2;
	}

	public ArrayList<SelectItem> getColumns3() {
		return columns3;
	}

	public void setColumns3(ArrayList<SelectItem> columns3) {
		this.columns3 = columns3;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public String getSelectedColumn1() {
		return selectedColumn1;
	}

	public void setSelectedColumn1(String selectedColumn1) {
		this.selectedColumn1 = selectedColumn1;
	}

	public String getSelectedColumn2() {
		return selectedColumn2;
	}

	public void setSelectedColumn2(String selectedColumn2) {
		this.selectedColumn2 = selectedColumn2;
	}

	public String getSelectedColumn3() {
		return selectedColumn3;
	}

	public void setSelectedColumn3(String selectedColumn3) {
		this.selectedColumn3 = selectedColumn3;
	}

	public boolean isTable() {
		return table;
	}

	public void setTable(boolean table) {
		this.table = table;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
		
}
