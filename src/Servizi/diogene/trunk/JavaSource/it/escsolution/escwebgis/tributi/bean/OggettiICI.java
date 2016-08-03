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
public class OggettiICI extends EscObject  implements Serializable{
	
	private String anagrafe;
	private String comune, codEnte;
	private String foglioCatasto;
	private String particellaCatasto;
	private String parCatastali;
	private String strada;
	private String superficie;
	private String vani;
	private String categoria;
	private String classe;
	private String rendita;
	private String titolo;
	private String quota;
	private String codContribuente;
	private String dataInizio;
	private String dataFine;
	private String partitaIva;
	private String codFiscale;
	private String subalterno;
	private String chiave;
	private String immd;
	private String provenienza;
	private String dicPosseduto;
	private String quotaPossesso;
	private String indirizzo;
	private String indirizzoCatastale;
	private String indirizzoViarioRif;
	private Integer 
		denAnno, 
		denRiferimento,
		denNumero,
		numRiga,
		denMesiPossesso,
		denTipo;
	private boolean evidenzia;
	private String partita;
	
	private String abitazionePrincipale;
	
	private String latitudine;
	private String longitudine;
	
	public String getProvenienza() {
		return provenienza;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	/**
		 * 
		 */
		public OggettiICI() {
			anagrafe = "";
			comune = "";
			foglioCatasto = "";
			comune = "";
			particellaCatasto = "";
			parCatastali = "";
			strada = "";
			superficie = "";
			vani = "";
			categoria = "";
			classe = "";
			rendita = "";
			titolo = "";
			quota= "";
			codContribuente = "";
			dataInizio= "";
			dataFine= "";
			partitaIva = "";
			codFiscale = "";
			subalterno="";
			immd ="";
			indirizzo="";
		}
		/**
		 * @return
		 */
	
	
	
	
	

	/**
	 * @return
	 */
	public String getAnagrafe() {
		return anagrafe;
	}

	/**
	 * @return
	 */
	public String getCategoria() {
		return categoria;
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
	public String getCodContribuente() {
		return codContribuente;
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
	public String getComune() {
		return comune;
	}

	/**
	 * @return
	 */
	public String getDataFine() {
		return dataFine;
	}

	/**
	 * @return
	 */
	public String getDataInizio() {
		return dataInizio;
	}

	/**
	 * @return
	 */
	public String getFoglioCatasto() {
		return foglioCatasto;
	}

	/**
	 * @return
	 */
	public String getParCatastali() {
		return parCatastali;
	}

	/**
	 * @return
	 */
	public String getParticellaCatasto() {
		return particellaCatasto;
	}

	/**
	 * @return
	 */
	public String getPartitaIva() {
		return partitaIva;
	}

	/**
	 * @return
	 */
	public String getQuota() {
		return quota;
	}

	/**
	 * @return
	 */
	public String getRendita() {
		return rendita;
	}

	/**
	 * @return
	 */
	public String getStrada() {
		return strada;
	}

	/**
	 * @return
	 */
	public String getSuperficie() {
		return superficie;
	}

	/**
	 * @return
	 */
	public String getTitolo() {
		return titolo;
	}

	/**
	 * @return
	 */
	public String getVani() {
		return vani;
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
	public void setCategoria(String string) {
		categoria = string;
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
	public void setCodContribuente(String string) {
		codContribuente = string;
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
	public void setComune(String string) {
		comune = string;
	}

	/**
	 * @param string
	 */
	public void setDataFine(String string) {
		dataFine = string;
	}

	/**
	 * @param string
	 */
	public void setDataInizio(String string) {
		dataInizio = string;
	}

	/**
	 * @param string
	 */
	public void setFoglioCatasto(String string) {
		foglioCatasto = string;
	}

	/**
	 * @param string
	 */
	public void setParCatastali(String string) {
		parCatastali = string;
	}

	/**
	 * @param string
	 */
	public void setParticellaCatasto(String string) {
		particellaCatasto = string;
	}

	/**
	 * @param string
	 */
	public void setPartitaIva(String string) {
		partitaIva = string;
	}

	/**
	 * @param string
	 */
	public void setQuota(String string) {
		quota = string;
	}

	/**
	 * @param string
	 */
	public void setRendita(String string) {
		rendita = string;
	}

	/**
	 * @param string
	 */
	public void setStrada(String string) {
		strada = string;
	}

	/**
	 * @param string
	 */
	public void setSuperficie(String string) {
		superficie = string;
	}

	/**
	 * @param string
	 */
	public void setTitolo(String string) {
		titolo = string;
	}

	/**
	 * @param string
	 */
	public void setVani(String string) {
		vani = string;
	}

	/**
	 * @return
	 */
	public String getSubalterno() {
		return subalterno;
	}

	/**
	 * @param string
	 */
	public void setSubalterno(String string) {
		subalterno = string;
	}

	/**
	 * @return
	 */
	public String getChiave() {
		//return chiave;
		// ERA UNA CAVOLATA 
		//return ""+immd+"|"+categoria;
		return chiave;
	}

	/**
	 * @param string
	 */
	public void setChiave(String string) {
		chiave = string;
	}
	public Integer getDenAnno()
	{
		return denAnno;
	}
	public void setDenAnno(Integer denAnno)
	{
		this.denAnno = denAnno;
	}
	public Integer getDenMesiPossesso()
	{
		return denMesiPossesso;
	}
	public void setDenMesiPossesso(Integer denMesiPossesso)
	{
		this.denMesiPossesso = denMesiPossesso;
	}
	public Integer getDenNumero()
	{
		return denNumero;
	}
	public void setDenNumero(Integer denNumero)
	{
		this.denNumero = denNumero;
	}
	public Integer getDenRiferimento()
	{
		return denRiferimento;
	}
	public void setDenRiferimento(Integer denRiferimento)
	{
		this.denRiferimento = denRiferimento;
	}
	public String getDicPosseduto()
	{
		return dicPosseduto;
	}
	public void setDicPosseduto(String dicPosseduto)
	{
		this.dicPosseduto = dicPosseduto;
	}
	public Integer getNumRiga()
	{
		return numRiga;
	}
	public void setNumRiga(Integer numRiga)
	{
		this.numRiga = numRiga;
	}
	public String getQuotaPossesso()
	{
		return quotaPossesso;
	}
	public void setQuotaPossesso(String quotaPossesso)
	{
		this.quotaPossesso = quotaPossesso;
	}
	public String getCodEnte()
	{
		return codEnte;
	}
	public void setCodEnte(String codEnte)
	{
		this.codEnte = codEnte;
	}
	public Integer getDenTipo()
	{
		return denTipo;
	}
	public void setDenTipo(Integer denTipo)
	{
		this.denTipo = denTipo;
	}
	public String getImmd() {
		return immd;
	}
	public void setImmd(String immd) {
		this.immd = immd;
	}
	public String getIndirizzo()
	{
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo)
	{
		this.indirizzo = indirizzo;
	}
	public String getIndirizzoCatastale()
	{
		return indirizzoCatastale;
	}
	public void setIndirizzoCatastale(String indirizzoCatastale)
	{
		this.indirizzoCatastale = indirizzoCatastale;
	}
	public String getIndirizzoViarioRif()
	{
		return indirizzoViarioRif;
	}
	public void setIndirizzoViarioRif(String indirizzoViarioRif)
	{
		this.indirizzoViarioRif = indirizzoViarioRif;
	}
	public boolean isEvidenzia() {
		return evidenzia;
	}
	public void setEvidenzia(boolean evidenzia) {
		this.evidenzia = evidenzia;
	}
	public String getPartita() {
		return partita;
	}
	public void setPartita(String partita) {
		this.partita = partita;
	}
	public String getAbitazionePrincipale() {
		return abitazionePrincipale;
	}
	public void setAbitazionePrincipale(String abitazionePrincipale) {
		this.abitazionePrincipale = abitazionePrincipale;
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
	

}
