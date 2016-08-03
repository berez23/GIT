/*
 * Created on 27-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.tributi.bean;


import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Contribuente extends EscObject implements Serializable{
	
	private String cognome;
	private String nome;
	private String sesso;
	private String dataNascita;
	private String piano;
	private String scala;
	private String interno;
	private String desComuneNascita;
	private String denominazione;
	private String civico;
	private String codiceSoggetto;
	private String indirizzo;
	private String residenza;
	private String codContribuente;
	private String anagrafe;
	private String comune;
	private String siatel;
	private String comuneSiatel;
	private String dataCaricamento;
	private String codFiscale;
	private String partitaIVA;
	private String provenienza;
	
	private String dicPosseduto;
	private String quotaPossesso;
	private String renditaCatastale;
	private Integer 
		denAnno, 
		denRiferimento,
		denNumero,
		numRiga,
		denMesiPossesso,
		denTipo;
	
	public String getChiave() {
		return codContribuente;
	}

	public Contribuente(){
		quotaPossesso ="";
		dicPosseduto ="";
		
		cognome = "";
		nome = "";
		sesso = "";
		dataNascita = "";
		piano = "";
		scala = "";
		interno = "";
		desComuneNascita = "";
		denominazione = "";
		civico = "";
		codiceSoggetto = "";
		indirizzo = "";
		residenza = "";
		codContribuente = "";
		anagrafe = "";
		comune = "";
		siatel = "";
		comuneSiatel = "";
		dataCaricamento = "";
		codFiscale="";
		partitaIVA="";
		provenienza="";
	}
	
	/**
	 * @return
	 */
	public String getAnagrafe() {
		return anagrafe;
	}

	/**
	 * @return
	 */
	public String getCivico() {
		return civico;
	}

	/**
	 * @return
	 */
	public String getCodContribuente() {
		return codContribuente;
	}

	/**
	 * @return
	 */
	public String getCodiceSoggetto() {
		return codiceSoggetto;
	}

	/**
	 * @return
	 */
	public String getCognome() {
		return cognome;
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
	public String getDataCaricamento() {
		return dataCaricamento;
	}

	/**
	 * @return
	 */
	public String getDataNascita() {
		return dataNascita;
	}

	/**
	 * @return
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * @return
	 */
	public String getDesComuneNascita() {
		return desComuneNascita;
	}

	/**
	 * @return
	 */
	public String getIndirizzo() {
		return indirizzo;
	}

	/**
	 * @return
	 */
	public String getInterno() {
		return interno;
	}

	/**
	 * @return
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return
	 */
	public String getPiano() {
		return piano;
	}

	/**
	 * @return
	 */
	public String getResidenza() {
		return residenza;
	}

	/**
	 * @return
	 */
	public String getScala() {
		return scala;
	}

	/**
	 * @return
	 */
	public String getSesso() {
		return sesso;
	}

	/**
	 * @param string
	 */
	public void setAnagrafe(String string) {
		anagrafe = string;
	}

	/**
	 * @param string
	 */
	public void setCivico(String string) {
		civico = string;
	}

	/**
	 * @param string
	 */
	public void setCodContribuente(String string) {
		codContribuente = string;
	}

	/**
	 * @param string
	 */
	public void setCodiceSoggetto(String string) {
		codiceSoggetto = string;
	}

	/**
	 * @param string
	 */
	public void setCognome(String string) {
		cognome = string;
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
	public void setDataCaricamento(String string) {
		dataCaricamento = string;
	}

	/**
	 * @param string
	 */
	public void setDataNascita(String string) {
		dataNascita = string;
	}

	/**
	 * @param string
	 */
	public void setDenominazione(String string) {
		denominazione = string;
	}

	/**
	 * @param string
	 */
	public void setDesComuneNascita(String string) {
		desComuneNascita = string;
	}

	/**
	 * @param string
	 */
	public void setIndirizzo(String string) {
		indirizzo = string;
	}

	/**
	 * @param string
	 */
	public void setInterno(String string) {
		interno = string;
	}

	/**
	 * @param string
	 */
	public void setNome(String string) {
		nome = string;
	}

	/**
	 * @param string
	 */
	public void setPiano(String string) {
		piano = string;
	}

	/**
	 * @param string
	 */
	public void setResidenza(String string) {
		residenza = string;
	}

	/**
	 * @param string
	 */
	public void setScala(String string) {
		scala = string;
	}

	/**
	 * @param string
	 */
	public void setSesso(String string) {
		sesso = string;
	}

	/**
	 * @return
	 */
	public String getCodFiscale() {
		return codFiscale;
	}

	/**
	 * @return
	 */
	public String getPartitaIVA() {
		return partitaIVA;
	}

	/**
	 * @param string
	 */
	public void setCodFiscale(String string) {
		codFiscale = string;
	}

	/**
	 * @param string
	 */
	public void setPartitaIVA(String string) {
		partitaIVA = string;
	}

	public String getProvenienza() {
		return provenienza;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	public Integer getDenAnno() {
		return denAnno;
	}
	public Integer getDenMesiPossesso() {
		return denMesiPossesso;
	}
	public Integer getDenNumero() {
		return denNumero;
	}
	public Integer getDenRiferimento() {
		return denRiferimento;
	}
	public Integer getDenTipo() {
		return denTipo;
	}
	public String getDicPosseduto() {
		return dicPosseduto;
	}
	public Integer getNumRiga() {
		return numRiga;
	}
	public String getQuotaPossesso() {
		return quotaPossesso;
	}
	public void setDenAnno(Integer denAnno) {
		this.denAnno = denAnno;
	}
	public void setDenMesiPossesso(Integer denMesiPossesso) {
		this.denMesiPossesso = denMesiPossesso;
	}
	public void setDenNumero(Integer denNumero) {
		this.denNumero = denNumero;
	}
	public void setDenRiferimento(Integer denRiferimento) {
		this.denRiferimento = denRiferimento;
	}
	public void setDenTipo(Integer denTipo) {
		this.denTipo = denTipo;
	}
	public void setDicPosseduto(String dicPosseduto) {
		this.dicPosseduto = dicPosseduto;
	}
	public void setNumRiga(Integer numRiga) {
		this.numRiga = numRiga;
	}
	public void setQuotaPossesso(String quotaPossesso) {
		this.quotaPossesso = quotaPossesso;
	}

	public String getRenditaCatastale() {
		return renditaCatastale;
	}

	public void setRenditaCatastale(String renditaCatastale) {
		this.renditaCatastale = renditaCatastale;
	}

	public String getSiatel() {
		return siatel;
	}

	public void setSiatel(String siatel) {
		this.siatel = siatel;
	}

	public String getComuneSiatel() {
		return comuneSiatel;
	}

	public void setComuneSiatel(String comuneSiatel) {
		this.comuneSiatel = comuneSiatel;
	}
	
}
