package it.webred.ct.data.access.basic.catasto.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import it.webred.ct.data.model.catasto.Sititrkc;

public class TerrenoPerSoggDTO implements Serializable {
	
	private long foglio;
	private String particella;
	private String subalterno;
	private String partita;
	private BigDecimal superficie; //SITITRKC.AREA_PART
	private BigDecimal qualita; //SITITRKC.QUAL_CAT
	private String classe; 	//SITITRKC.CLASSE_TERRENO
	private BigDecimal redditoDominicale;
	private BigDecimal redditoAgrario;	
	private Date dataFinVal;
	private String sezione;
	private Date dtIniPos;
	private Date dtFinPos;
	private String titolo;
	
	
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public long getFoglio() {
		return foglio;
	}
	public void setFoglio(long foglio) {
		this.foglio = foglio;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public String getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}
	public String getPartita() {
		return partita;
	}
	public void setPartita(String partita) {
		this.partita = partita;
	}
	
	
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	
	public Date getDataFinVal() {
		return dataFinVal;
	}
	public void setDataFinVal(Date dataFinVal) {
		this.dataFinVal = dataFinVal;
	}
		
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public Date getDtIniPos() {
		return dtIniPos;
	}
	public void setDtIniPos(Date dtIniPos) {
		this.dtIniPos = dtIniPos;
	}
	public Date getDtFinPos() {
		return dtFinPos;
	}
	public void setDtFinPos(Date dtFinPos) {
		this.dtFinPos = dtFinPos;
	}
	public BigDecimal getSuperficie() {
		return superficie;
	}
	public void setSuperficie(BigDecimal superficie) {
		this.superficie = superficie;
	}
	public BigDecimal getQualita() {
		return qualita;
	}
	public void setQualita(BigDecimal qualita) {
		this.qualita = qualita;
	}
	public BigDecimal getRedditoDominicale() {
		return redditoDominicale;
	}
	public void setRedditoDominicale(BigDecimal redditoDominicale) {
		this.redditoDominicale = redditoDominicale;
	}
	public BigDecimal getRedditoAgrario() {
		return redditoAgrario;
	}
	public void setRedditoAgrario(BigDecimal redditoAgrario) {
		this.redditoAgrario = redditoAgrario;
	}
	
}
