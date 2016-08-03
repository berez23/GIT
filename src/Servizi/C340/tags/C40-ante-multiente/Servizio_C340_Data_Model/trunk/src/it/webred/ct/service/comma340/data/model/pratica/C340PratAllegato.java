package it.webred.ct.service.comma340.data.model.pratica;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the S_C340_PRAT_ALLEGATO database table.
 * 
 */
@Entity
@Table(name="S_C340_PRAT_ALLEGATO")
public class C340PratAllegato implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;

	@Column(name="ID_PRATICA")
	private Long idPratica;

	@Column(name="NOME_FILE")
	private String nomeFile;
	
	@Temporal( TemporalType.DATE)
	@Column(name="DATA_CREAZIONE")
	private Date dataCreazione;

    public C340PratAllegato() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdPratica() {
		return idPratica;
	}

	public void setIdPratica(Long idPratica) {
		this.idPratica = idPratica;
	}

	public String getNomeFile() {
		return this.nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

}