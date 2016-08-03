package it.webred.gitland.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PRATICHE_ALLEGATI")
public class PraticaAllegato extends MasterItem{

	private static final long serialVersionUID = -2617316055709171129L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="pratica_allegato_seq")
	@SequenceGenerator(	name="pratica_allegato_seq", sequenceName="SEQ_GITLAND")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id = 0l;		

	@Column(name = "PERCORSO")
	private String percorso = "";
	
	@Column(name = "NOME_FILE")
	private String nomeFile = "";
	
	@Column(name = "DESCRIZIONE")
	private String descrizione = "";

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="CONTATORE_FK")
	private Pratica pratica  = null;

	
	public PraticaAllegato() {
	}//-------------------------------------------------------------------------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPercorso() {
		return percorso;
	}

	public void setPercorso(String percorso) {
		this.percorso = percorso;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Pratica getPratica() {
		return pratica;
	}

	public void setPratica(Pratica pratica) {
		this.pratica = pratica;
	}


}
