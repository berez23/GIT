package it.webred.ct.service.gestprep.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the S_GESTPREP_TARIFFA_VIS database table.
 * 
 */
@Entity
@Table(name="S_GESTPREP_TARIFFA_VIS")
public class GestPrepTariffaVisura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_TAR")
	private long idTar;

    @Temporal( TemporalType.DATE)
	@Column(name="DATA_FINE_VAL")
	private Date dataFineVal;

    @Temporal( TemporalType.DATE)
	@Column(name="DATA_INIZIO_VAL")
	private Date dataInizioVal;

	private BigDecimal tariffa;

	@Column(name="ID_VIS")
	private Long idVisura;

    public GestPrepTariffaVisura() {
    }

	public long getIdTar() {
		return this.idTar;
	}

	public void setIdTar(long idTar) {
		this.idTar = idTar;
	}

	public Date getDataFineVal() {
		return this.dataFineVal;
	}

	public void setDataFineVal(Date dataFineVal) {
		this.dataFineVal = dataFineVal;
	}

	public Date getDataInizioVal() {
		return this.dataInizioVal;
	}

	public void setDataInizioVal(Date dataInizioVal) {
		this.dataInizioVal = dataInizioVal;
	}

	public BigDecimal getTariffa() {
		return this.tariffa;
	}

	public void setTariffa(BigDecimal tariffa) {
		this.tariffa = tariffa;
	}

	public Long getIdVisura() {
		return idVisura;
	}

	public void setIdVisura(Long idVisura) {
		this.idVisura = idVisura;
	}


	
}