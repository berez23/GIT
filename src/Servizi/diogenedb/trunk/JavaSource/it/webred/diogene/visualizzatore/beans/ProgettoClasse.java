package it.webred.diogene.visualizzatore.beans;

/**
 * Bean che contiene i dati per la visualizzazione, in un diagramma ad albero, della struttura gerarchica 
 * di progetti e classi.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class ProgettoClasse {

	/**
	 * Identificativo del progetto o della classe.
	 */
	private Long id;
	/**
	 * Percorso dell'icona associata alla descrizione di progetto o classe (+ e - per contenitore aperto e chiuso, 
	 * altrimenti nessuna icona) nel diagramma ad albero.
	 */
	private String iconPath;
	/**
	 * Nome del progetto o della classe.
	 */
	private String name;
	/**
	 * Indica tramite specifiche CSS l'indentazione dell'elemento dell'albero.
	 */
	private String indentStyle;
	/**
	 * Livello: se è uguale a -1 si tratta di un progetto, altrimenti di una classe del livello corrispondente
	 */
	private Short level;
	/**
	 * Valore per l'ordinamento nel diagramma ad albero, all'interno del livello corrispondente.
	 */
	private Short sortOrder;
	/**
	 * Flag che indica se deve essere visualizzata l'icona "Muovi su" per la modifica dell'ordinamento.
	 */
	private boolean upRendered;
	/**
	 * Flag che indica se deve essere visualizzata l'icona "Muovi già" per la modifica dell'ordinamento.
	 */
	private boolean downRendered;
	/**
	 * Flag che indica se deve essere visualizzata l'icona "Nuova classe" per l'inserimento di una nuova classe.
	 */
	private boolean newRendered;
	/**
	 * Flag che indica se deve essere visualizzata l'icona "Cancella" per la cancellazione di un progetto o di una 
	 * classe.
	 */
	private boolean deleteRendered;
	/**
	 * Flag che indica se il nodo dell'albero (se contenitore) deve essere o meno espanso.
	 */
	private boolean expanded;
	/**
	 * Nome della popup che deve essere aperta all'azione sui link di inserimento classe (possono essere inserite 
	 * classi contenitore o classi terminali).
	 */
	private String popupName;
	
	public boolean isExpanded() {
		return expanded;
	}
	
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
	public Short getLevel() {
		return level;
	}
	
	public void setLevel(Short level) {
		this.level = level;
		int indentValue = ((level + 1) * 50) + 20;
		this.indentStyle = "width:" + indentValue + "px;height:18px;";
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getIndentStyle() {
		return indentStyle;
	}

	public void setIndentStyle(String indentStyle) {
		this.indentStyle = indentStyle;
	}

	public boolean isDeleteRendered() {
		return deleteRendered;
	}

	public void setDeleteRendered(boolean deleteRendered) {
		this.deleteRendered = deleteRendered;
	}

	public boolean isDownRendered() {
		return downRendered;
	}

	public void setDownRendered(boolean downRendered) {
		this.downRendered = downRendered;
	}

	public Short getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Short sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean isUpRendered() {
		return upRendered;
	}

	public void setUpRendered(boolean upRendered) {
		this.upRendered = upRendered;
	}

	public boolean isNewRendered() {
		return newRendered;
	}

	public void setNewRendered(boolean newRendered) {
		this.newRendered = newRendered;
	}

	public String getPopupName() {
		return popupName;
	}

	public void setPopupName(String popupName) {
		this.popupName = popupName;
	}
	
}
