/*
 * Created on 13-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.condomini.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Condomini extends EscObject implements Serializable{
	
	private String codctb;
	private String denominazione;
	private String codvia;
	private String indirizzo;
	private String civico;
	private String espciv;
	private String classetar;
	private String riduzione;
	private String superf;
	private String caricoarr;
	private String prog;
	private String cessato;
	private String sospeso;
	private String codiceFiscale;
	private String partitaIva;
	private String objectid;
	private String passo;
	private String civico_cal;
	
	
	public Condomini(){
		 codctb="";
		 denominazione="";
		 codvia="";
		 indirizzo="";
		 civico="";
		 espciv="";
		 classetar="";
		 riduzione="";
		 superf="";
		 caricoarr="";
		 prog="";
		 cessato="";
		 sospeso="";
		 codiceFiscale="";
		 partitaIva="";
		 objectid="";
		 passo="";
		 civico_cal="";
	
	}

	
	/**
	 * @return Returns the caricoarr.
	 */
	public String getCaricoarr() {
		return caricoarr;
	}
	/**
	 * @param caricoarr The caricoarr to set.
	 */
	public void setCaricoarr(String caricoarr) {
		this.caricoarr = caricoarr;
	}
	/**
	 * @return Returns the cessato.
	 */
	public String getCessato() {
		return cessato;
	}
	/**
	 * @param cessato The cessato to set.
	 */
	public void setCessato(String cessato) {
		this.cessato = cessato;
	}
	/**
	 * @return Returns the civico.
	 */
	public String getCivico() {
		return civico;
	}
	/**
	 * @param civico The civico to set.
	 */
	public void setCivico(String civico) {
		this.civico = civico;
	}
	/**
	 * @return Returns the civico_cal.
	 */
	public String getCivico_cal() {
		return civico_cal;
	}
	/**
	 * @param civico_cal The civico_cal to set.
	 */
	public void setCivico_cal(String civico_cal) {
		this.civico_cal = civico_cal;
	}
	/**
	 * @return Returns the classetar.
	 */
	public String getClassetar() {
		return classetar;
	}
	/**
	 * @param classetar The classetar to set.
	 */
	public void setClassetar(String classetar) {
		this.classetar = classetar;
	}
	/**
	 * @return Returns the codctb.
	 */
	public String getCodctb() {
		return codctb;
	}
	/**
	 * @param codctb The codctb to set.
	 */
	public void setCodctb(String codctb) {
		this.codctb = codctb;
	}
	/**
	 * @return Returns the codiceFiscale.
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	/**
	 * @param codiceFiscale The codiceFiscale to set.
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	/**
	 * @return Returns the codvia.
	 */
	public String getCodvia() {
		return codvia;
	}
	/**
	 * @param codvia The codvia to set.
	 */
	public void setCodvia(String codvia) {
		this.codvia = codvia;
	}
	/**
	 * @return Returns the denominazione.
	 */
	public String getDenominazione() {
		return denominazione;
	}
	/**
	 * @param denominazione The denominazione to set.
	 */
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	/**
	 * @return Returns the espciv.
	 */
	public String getEspciv() {
		return espciv;
	}
	/**
	 * @param espciv The espciv to set.
	 */
	public void setEspciv(String espciv) {
		this.espciv = espciv;
	}
	/**
	 * @return Returns the indirizzo.
	 */
	public String getIndirizzo() {
		return indirizzo;
	}
	/**
	 * @param indirizzo The indirizzo to set.
	 */
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	/**
	 * @return Returns the objectid.
	 */
	public String getObjectid() {
		return objectid;
	}
	/**
	 * @param objectid The objectid to set.
	 */
	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}
	/**
	 * @return Returns the partitaIva.
	 */
	public String getPartitaIva() {
		return partitaIva;
	}
	/**
	 * @param partitaIva The partitaIva to set.
	 */
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
	/**
	 * @return Returns the passo.
	 */
	public String getPasso() {
		return passo;
	}
	/**
	 * @param passo The passo to set.
	 */
	public void setPasso(String passo) {
		this.passo = passo;
	}
	/**
	 * @return Returns the prog.
	 */
	public String getProg() {
		return prog;
	}
	/**
	 * @param prog The prog to set.
	 */
	public void setProg(String prog) {
		this.prog = prog;
	}
	/**
	 * @return Returns the riduzione.
	 */
	public String getRiduzione() {
		return riduzione;
	}
	/**
	 * @param riduzione The riduzione to set.
	 */
	public void setRiduzione(String riduzione) {
		this.riduzione = riduzione;
	}
	/**
	 * @return Returns the sospeso.
	 */
	public String getSospeso() {
		return sospeso;
	}
	/**
	 * @param sospeso The sospeso to set.
	 */
	public void setSospeso(String sospeso) {
		this.sospeso = sospeso;
	}
	/**
	 * @return Returns the superf.
	 */
	public String getSuperf() {
		return superf;
	}
	/**
	 * @param superf The superf to set.
	 */
	public void setSuperf(String superf) {
		this.superf = superf;
	}
	
	public String getChiave(){
			String key =codctb.equals("-")?"-1":codctb;
	 			   key+="@"+denominazione+"@"+codvia+"@"+indirizzo+"@"; 
	 			   key+=civico.equals("-")?"-1":civico;
	 		return key;			
	 		
			
		}
	
	
}
