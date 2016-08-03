package it.webred.ct.service.gestprep.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the S_GESTPREP_DEPOSITO database table.
 * 
 */
@Entity
@Table(name="S_GESTPREP_DEPOSITO")
public class GestPrepDeposito implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_DEP")
	private long idDep;

	@Column(name="DATA_DEP")
	private Date dataDep;

	@Column(name="IMPORTO_DEP")
	private BigDecimal importoDep;

	@Column(name="ID_SOGG")
	private Long idSoggetto;

    public GestPrepDeposito() {
    }

	public long getIdDep() {
		return this.idDep;
	}

	public void setIdDep(long idDep) {
		this.idDep = idDep;
	}

	public Date getDataDep() {
		return this.dataDep;
	}

	public void setDataDep(Date dataDep) {
		this.dataDep = dataDep;
	}

	public BigDecimal getImportoDep() {
		return this.importoDep;
	}

	public void setImportoDep(BigDecimal importoDep) {
		this.importoDep = importoDep;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}


	
	
}