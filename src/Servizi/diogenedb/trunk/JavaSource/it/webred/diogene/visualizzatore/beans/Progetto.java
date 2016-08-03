package it.webred.diogene.visualizzatore.beans;

/**
 * Bean che contiene i dati per la gestione di un progetto (contenitore di classi al livello più esterno).
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class Progetto {
	
	/**
	 *	Identificativo del progetto.
	 */
	private Long id;
	/**
	 *	Nome del progetto.
	 */
	private String name;
	/**
	 * Valore per l'ordinamento in fase di visualizzazione.
	 */
	private Short sortOrder;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Short getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Short sortOrder) {
		this.sortOrder = sortOrder;
	}
	
}
