package it.webred.ct.data.model.segnalazionequalificata;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the S_OF_PRAT_ALLEGATO database table.
 * 
 */
@Entity
@Table(name="S_OF_PRAT_ALLEGATO")
public class SOfPratAllegato implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;

    @Temporal( TemporalType.DATE)
	@Column(name="DATA_INSERIMENTO")
	private Date dataInserimento;

	@Column(name="FK_PRATICA")
	private java.math.BigDecimal fkPratica;

	@Column(name="NOME_FILE")
	private String nomeFile;

    public SOfPratAllegato() {
    }

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDataInserimento() {
		return this.dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public java.math.BigDecimal getFkPratica() {
		return this.fkPratica;
	}

	public void setFkPratica(java.math.BigDecimal fkPratica) {
		this.fkPratica = fkPratica;
	}

	public String getNomeFile() {
		return this.nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

}