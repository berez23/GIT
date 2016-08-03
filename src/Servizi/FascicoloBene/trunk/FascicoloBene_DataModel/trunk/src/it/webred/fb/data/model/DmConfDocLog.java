package it.webred.fb.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the DM_CONF_DOC_LOG database table.
 * 
 */
@Entity
@Table(name="DM_CONF_DOC_LOG")
@NamedQuery(name="DmConfDocLog.findAll", query="SELECT d FROM DmConfDocLog d")
public class DmConfDocLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DmConfDocLogPK id;

	@Column(name="N_ELABORATI")
	private BigDecimal nElaborati;

	@Column(name="N_NUOVI")
	private BigDecimal nNuovi;

	@Column(name="N_RIMOSSI")
	private BigDecimal nRimossi;

	@Lob
	@Column(name="TXT_LOG")
	private String txtLog;

	//bi-directional many-to-one association to DmConfDocDir
	@ManyToOne
	@JoinColumn(name="CODICE", insertable=false, updatable=false)
	private DmConfDocDir dmConfDocDir;

	public DmConfDocLog() {
	}

	public DmConfDocLogPK getId() {
		return this.id;
	}

	public void setId(DmConfDocLogPK id) {
		this.id = id;
	}

	
	public String getTxtLog() {
		return this.txtLog;
	}

	public void setTxtLog(String txtLog) {
		this.txtLog = txtLog;
	}

	public DmConfDocDir getDmConfDocDir() {
		return this.dmConfDocDir;
	}

	public void setDmConfDocDir(DmConfDocDir dmConfDocDir) {
		this.dmConfDocDir = dmConfDocDir;
	}

	public BigDecimal getnElaborati() {
		return nElaborati;
	}

	public void setnElaborati(BigDecimal nElaborati) {
		this.nElaborati = nElaborati;
	}

	public BigDecimal getnNuovi() {
		return nNuovi;
	}

	public void setnNuovi(BigDecimal nNuovi) {
		this.nNuovi = nNuovi;
	}

	public BigDecimal getnRimossi() {
		return nRimossi;
	}

	public void setnRimossi(BigDecimal nRimossi) {
		this.nRimossi = nRimossi;
	}

}