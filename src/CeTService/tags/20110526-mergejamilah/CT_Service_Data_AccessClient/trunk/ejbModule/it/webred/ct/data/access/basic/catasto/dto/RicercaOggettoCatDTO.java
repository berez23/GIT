package it.webred.ct.data.access.basic.catasto.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RicercaOggettoCatDTO extends CatastoBaseDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codEnte;
	private String idUiu;
	private String sezione;
	private String foglio;
	private String particella;
	private String unimm;
	private Date dtVal;
	private String codCategoria;
	private CatastoSearchCriteria criteria;
	private int startm;
	private int numberRecord;
	private String via;
	private BigDecimal pkIdStra;
	private String civico;
	
	
	public RicercaOggettoCatDTO() {
		super();
	}
	public RicercaOggettoCatDTO(String codEnte, String foglio, String particella, String unimm, Date dtVal, String idUiu) {
		super();
		this.codEnte=codEnte;
		this.foglio = foglio;
		this.particella = particella;
		this.unimm = unimm;
		this.dtVal  = dtVal;
		this.idUiu = idUiu;
	}
	public RicercaOggettoCatDTO(String codEnte, String foglio, String particella, String unimm, Date dtVal) {
		super();
		this.codEnte=codEnte;
		this.foglio = foglio;
		this.particella = particella;
		this.unimm = unimm;
		this.dtVal  = dtVal;
	}
	public RicercaOggettoCatDTO(String codEnte, String idUiu) {
		super();
		this.codEnte = codEnte;
		this.idUiu = idUiu;
	}
	public RicercaOggettoCatDTO(CatastoSearchCriteria criteria) {
		super();
		this.criteria = criteria;
	}
	public String getCodEnte() {
		return codEnte;
	}
	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}
	public String getIdUiu() {
		return idUiu;
	}
	public void setIdUiu(String idUiu) {
		this.idUiu = idUiu;
	}
	public String getFoglio() {
		return foglio;
	}
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public String getUnimm() {
		return unimm;
	}
	public void setUnimm(String unimm) {
		this.unimm = unimm;
	}
	public Date getDtVal() {
		return dtVal;
	}
	public void setDtVal(Date dtVal) {
		this.dtVal = dtVal;
	}
	public String getCodCategoria() {
		return codCategoria;
	}
	public void setCodCategoria(String codCategoria) {
		this.codCategoria = codCategoria;
	}	
	public CatastoSearchCriteria getCriteria() {
		return criteria;
	}
	public void setCriteria(CatastoSearchCriteria criteria) {
		this.criteria = criteria;
	}
	public int getStartm() {
		return startm;
	}
	public void setStartm(int startm) {
		this.startm = startm;
	}
	public int getNumberRecord() {
		return numberRecord;
	}
	public void setNumberRecord(int numberRecord) {
		this.numberRecord = numberRecord;
	}
	public String getVia() {
		return via;
	}
	public void setVia(String via) {
		this.via = via;
	}
	public BigDecimal getPkIdStra() {
		return pkIdStra;
	}
	public void setPkIdStra(BigDecimal pkIdStra) {
		this.pkIdStra = pkIdStra;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	
}
