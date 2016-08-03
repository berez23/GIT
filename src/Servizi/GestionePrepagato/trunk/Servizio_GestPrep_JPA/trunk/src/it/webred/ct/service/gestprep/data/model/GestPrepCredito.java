package it.webred.ct.service.gestprep.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the S_GESTPREP_CREDITO database table.
 * 
 */
@Entity
@Table(name="S_GESTPREP_CREDITO")
public class GestPrepCredito implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_CREDITO")
	private long idCredito;

	private BigDecimal credito;

    @Temporal( TemporalType.DATE)
	@Column(name="DATA_OP")
	private Date dataOp;

	@Column(name="ID_SOGGETTO")
	private Long idSoggetto;

    public GestPrepCredito() {
    }

	public long getIdCredito() {
		return this.idCredito;
	}

	public void setIdCredito(long idCredito) {
		this.idCredito = idCredito;
	}

	public BigDecimal getCredito() {
		return this.credito;
	}

	public void setCredito(BigDecimal credito) {
		this.credito = credito;
	}

	public Date getDataOp() {
		return this.dataOp;
	}

	public void setDataOp(Date dataOp) {
		this.dataOp = dataOp;
	}

	public Long getIdSoggetto() {
		return this.idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

}