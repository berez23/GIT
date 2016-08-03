package it.webred.diogene.visualizzatore.beans;

/**
 * Bean che contiene i dati per la gestione di una classe di livello esterno, senza record inseriti in 
 * DV_FORMAT_CLASSE (classe contenitore).
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public class Classe {
	
	/**
	 * Identificativo della classe.
	 */
	private Long id;
	/**
	 * Nome della classe.
	 */
	private String name;
	/**
	 * Identificativo della user entity (chiave esterna in tabella DV_USER_ENTITY).
	 */
	private Long dvUserEntity;
	/**
	 * Valore per l'ordinamento in fase di visualizzazione.
	 */
	private Short sortOrder;
	/**
	 * Identificativo della classe che contiene la classe corrente.
	 */
	private Long dvClasse;
	/**
	 * Nome della classe che contiene la classe corrente.
	 */
	private String dvClasseName;
	/**
	 * Identificativo del progetto di cui fa parte la classe corrente.
	 */
	private Long dvProgetto;
	/**
	 * Nome del progetto di cui fa parte la classe corrente.
	 */
	private String dvProgettoName;
	
	public String getDvClasseName() {
		return dvClasseName;
	}

	public void setDvClasseName(String dvClasseName) {
		this.dvClasseName = dvClasseName;
	}

	public String getDvProgettoName() {
		return dvProgettoName;
	}

	public void setDvProgettoName(String dvProgettoName) {
		this.dvProgettoName = dvProgettoName;
	}

	public Long getDvClasse() {
		return dvClasse;
	}

	public void setDvClasse(Long dvClasse) {
		this.dvClasse = dvClasse;
	}

	public Long getDvProgetto() {
		return dvProgetto;
	}

	public void setDvProgetto(Long dvProgetto) {
		this.dvProgetto = dvProgetto;
	}

	public Long getDvUserEntity() {
		return dvUserEntity;
	}

	public void setDvUserEntity(Long dvUserEntity) {
		this.dvUserEntity = dvUserEntity;
	}

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
