package it.webred.gitland.data.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "AUDIT_ENTITA_ELIMINATE")
public class AuditEntitaEliminate extends MasterItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="audit_entita_eliminate_seq")
	@SequenceGenerator(	name="audit_entita_eliminate_seq", sequenceName="SEQ_GITLAND")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id = 0l;
	
	@Column(name = "UTENTE")
	private String utente = "";
	
	@Column(name = "DATA")
	private Date data = null;
	
	@Column(name = "ID_ENTITA")
	private Long idEntita = 0l;
	
	@Column(name = "ENTITA")
	private String entita = "";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Long getIdEntita() {
		return idEntita;
	}

	public void setIdEntita(Long idEntita) {
		this.idEntita = idEntita;
	}

	public String getEntita() {
		return entita;
	}

	public void setEntita(String entita) {
		this.entita = entita;
	}

}
