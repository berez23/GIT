/*
 * Created on 9-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.catasto.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Terreno implements Serializable {
	private String sezione;
	private String particella;
	private String particellaCatasto;
	private String foglio;
	private String numero;
	private String subalterno;
	private String partita;
	private String comune;
	private String ettari;
	private String are;
	private String centiare;
	private String superficie;
	private String qualita;
	private String classe; 	
	private String rDominicale;
	private String rAgrario;	
	private String descrClasse;
	private String descrQualita;
	private String codente;	
	private String titolo;
	private String dataFine;
	private String dataInizio;
	private String dataFinePos;
	private BigDecimal percPoss;
	private String annotazione;
	
	private BigDecimal ideMutaIni;
	private BigDecimal ideMutaFine;
	
	long recordAttuale;
	long recordTotali;
	
	private String latitudine;
	private String longitudine;
	private boolean evidenza;
	private String stato;
	
	/**
	 * 
	 */
	public Terreno() {
		sezione = "";
	//	particella = "";
		foglio = "";
		numero = "";
		subalterno = "";
		partita = "";
		comune = "";
		ettari = "";
		are = "";
		centiare = "";
		superficie = "";
		qualita = "";
		classe = ""; 
		rDominicale = "";
		rAgrario = "";
		particellaCatasto="";
		descrClasse="";
		descrQualita="";
		titolo = "";
		dataFine ="";
		dataInizio ="";
		dataFinePos ="";
		annotazione="";
	}
	public Terreno(String cod,String  des) {
		foglio = cod;
		numero = des;
	}

	/**
	 * @return
	 */
	public String getChiave() {
			return particella;
		}
	
	public String getAre() {
		return are;
	}

	/**
	 * @return
	 */
	public String getCentiare() {
		return centiare;
	}

	/**
	 * @return
	 */
	public String getClasse() {
		return classe;
	}

	/**
	 * @return
	 */
	public String getComune() {
		return comune;
	}

	/**
	 * @return
	 */
	public String getEttari() {
		return ettari;
	}

	/**
	 * @return
	 */
	public String getFoglio() {
		return foglio;
	}

	/**
	 * @return
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @return
	 */
	public String getPartita() {
		return partita;
	}

	/**
	 * @return
	 */
	public String getQualita() {
		return qualita;
	}

	/**
	 * @return
	 */
	public String getRAgrario() {
		return rAgrario;
	}

	/**
	 * @return
	 */
	public String getRDominicale() {
		return rDominicale;
	}

	/**
	 * @return
	 */
	public String getSubalterno() {
		return subalterno;
	}

	/**
	 * @return
	 */
	public String getSuperficie() {
		return superficie;
	}

	/**
	 * @param string
	 */
	public void setAre(String string) {
		are = string;
	}

	/**
	 * @param string
	 */
	public void setCentiare(String string) {
		centiare = string;
	}

	/**
	 * @param string
	 */
	public void setClasse(String string) {
		classe = string;
	}

	/**
	 * @param string
	 */
	public void setComune(String string) {
		comune = string;
	}

	/**
	 * @param string
	 */
	public void setEttari(String string) {
		ettari = string;
	}

	/**
	 * @param string
	 */
	public void setFoglio(String string) {
		foglio = string;
	}

	/**
	 * @param string
	 */
	public void setNumero(String string) {
		numero = string;
	}

	public BigDecimal getIdeMutaIni() {
		return ideMutaIni;
	}
	public void setIdeMutaIni(BigDecimal ideMutaIni) {
		this.ideMutaIni = ideMutaIni;
	}
	public BigDecimal getIdeMutaFine() {
		return ideMutaFine;
	}
	public void setIdeMutaFine(BigDecimal ideMutaFine) {
		this.ideMutaFine = ideMutaFine;
	}
	/**
	 * @param string
	 */
	public void setPartita(String string) {
		partita = string;
	}

	/**
	 * @param string
	 */
	public void setQualita(String string) {
		qualita = string;
	}

	/**
	 * @param string
	 */
	public void setRAgrario(String string) {
		rAgrario = string;
	}

	/**
	 * @param string
	 */
	public void setRDominicale(String string) {
		rDominicale = string;
	}

	/**
	 * @param string
	 */
	public void setSubalterno(String string) {
		subalterno = string;
	}

	/**
	 * @param string
	 */
	public void setSuperficie(String string) {
		superficie = string;
	}
	
	/**
	 * @return
	 */
	public String getSezione() {
		return sezione;
	}
	
	/**
	 * @param string
	 */
	public void setSezione(String string) {
		sezione = string;
	}


	public String getParticella() {
		return particella;
	}


	public void setParticella(String string) {
		particella = string;
	}

	/**
	 * @return
	 */
	public long getRecordAttuale() {
		return recordAttuale;
	}

	/**
	 * @return
	 */
	public long getRecordTotali() {
		return recordTotali;
	}

	/**
	 * @param l
	 */
	public void setRecordAttuale(long l) {
		recordAttuale = l;
	}

	/**
	 * @param l
	 */
	public void setRecordTotali(long l) {
		recordTotali = l;
	}

	/**
	 * @return
	 */
	public String getParticellaCatasto() {
		return particellaCatasto;
	}

	/**
	 * @param string
	 */
	public void setParticellaCatasto(String string) {
		particellaCatasto = string;
	}

	/**
	 * @return Returns the descrClasse.
	 */
	public String getDescrClasse() {
		return descrClasse;
	}
	/**
	 * @return Returns the descrQualita.
	 */
	public String getDescrQualita() {
		return descrQualita;
	}
	/**
	 * @param descrClasse The descrClasse to set.
	 */
	public void setDescrClasse(String descrClasse) {
		this.descrClasse = descrClasse;
	}
	/**
	 * @param descrQualita The descrQualita to set.
	 */
	public void setDescrQualita(String descrQualita) {
		this.descrQualita = descrQualita;
	}
	public String getCodente()
	{
		return codente;
	}
	public void setCodente(String codente)
	{
		this.codente = codente;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getDataFine() {
		return dataFine;
	}
	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}
	public String getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}
	public String getDataFinePos() {
		return dataFinePos;
	}
	public void setDataFinePos(String dataFinePos) {
		this.dataFinePos = dataFinePos;
	}
	public String getLatitudine() {
		return latitudine;
	}
	public void setLatitudine(String latitudine) {
		this.latitudine = latitudine;
	}
	public String getLongitudine() {
		return longitudine;
	}
	public void setLongitudine(String longitudine) {
		this.longitudine = longitudine;
	}
	public void setEvidenza(boolean evidenza) {
		this.evidenza = evidenza;
	}
	public boolean isEvidenza() {
		return evidenza;
	}
	public void setStato(String stato) {
		this.stato = stato;
		
	}
	
	public BigDecimal getPercPoss() {
		return percPoss;
	}
	public void setPercPoss(BigDecimal percPoss) {
		this.percPoss = percPoss;
	}
	public String getAnnotazione() {
		return annotazione;
	}
	public void setAnnotazione(String annotazione) {
		this.annotazione = annotazione!=null ? annotazione :"";
	}
	
	public String getStato(){
		String s ="";
		//String dataFineVal= ter.getDataFine();
		
		if (this.dataFine == null)
			return s = "-";
	
		s = this.dataFine.equals("31/12/9999") ? "ATTUALE" : "CESSATO IL " + dataFine ;
		
		return s;
	}
	
}
