package it.webred.ct.data.model.diagnostiche;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class DocfaReportSoggPK implements Serializable {
	
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="FK_ID_REP")
	private String fkIdRep;
	
	@Column(name="PK_CUAA")
	private BigDecimal pkCuaa;
	
	@Temporal( TemporalType.DATE)
	@Column(name="DATA_FINE_POSSESSO")
	private Date dataFinePossesso;

    @Temporal( TemporalType.DATE)
	@Column(name="DATA_INIZIO_POSSESSO")
	private Date dataInizioPossesso;

	public void setFkIdRep(String fkIdRep) {
		this.fkIdRep = fkIdRep;
	}

	public String getFkIdRep() {
		return fkIdRep;
	}

	public void setPkCuaa(BigDecimal pkCuaa) {
		this.pkCuaa = pkCuaa;
	}

	public BigDecimal getPkCuaa() {
		return pkCuaa;
	}
	
	public Date getDataFinePossesso() {
		return this.dataFinePossesso;
	}

	public void setDataFinePossesso(Date dataFinePossesso) {
		this.dataFinePossesso = dataFinePossesso;
	}

	public Date getDataInizioPossesso() {
		return this.dataInizioPossesso;
	}

	public void setDataInizioPossesso(Date dataInizioPossesso) {
		this.dataInizioPossesso = dataInizioPossesso;
	}

}
