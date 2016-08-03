package it.webred.diogene.visualizzatore.beans;


/**
 * Bean che contiene i dati necessari, in fase di inserimento o di modifica di una classe, per l'inserimento, la 
 * modifica o la cancellazione dei link tra la classe e altre voci di menu.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class Link {
	/**
	 * Identificativo del progetto.
	 */
	private Long projectId;
	/**
	 * Nome del progetto.
	 */
	private String projectName;
	/**
	 * Identificativo della classe.
	 */
	private Long classId;
	/**
	 * Nome della classe.
	 */
	private String className;
	/**
	 * Identificativo del link.
	 */
	private Long linkId;
	/**
	 * Nome del link.
	 */
	private String linkName;
	/**
	 * Identificativo della DC_ENTITY_REL.
	 */
	private Long dcEntityRelId;
	/**
	 * Flag che indica se il link è selezionato o meno nella lista relativa della popup di gestione classi.
	 */
	private boolean selected;
	/**
	 * Flag che indica se in caso di cancellazione del link devono essere cancellate anche tutte le chiavi relative 
	 * al link.
	 */
	private boolean deletingAllKeys;
	
	public Long getClassId() {
		return classId;
	}
	public void setClassId(Long classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Long getDcEntityRelId() {
		return dcEntityRelId;
	}
	public void setDcEntityRelId(Long dcEntityRelId) {
		this.dcEntityRelId = dcEntityRelId;
	}
	public Long getLinkId() {
		return linkId;
	}
	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public boolean isDeletingAllKeys() {
		return deletingAllKeys;
	}
	public void setDeletingAllKeys(boolean deletingAllKeys) {
		this.deletingAllKeys = deletingAllKeys;
	}
}
