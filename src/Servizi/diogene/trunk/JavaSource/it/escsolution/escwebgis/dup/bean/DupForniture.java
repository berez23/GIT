/*
 * Created on 13-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.dup.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Giulio Quaresima - WebRed
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DupForniture extends EscObject implements Serializable{
	
	private String idFornitura;
	private String codAmmin;
	private String dataInizio;
	private String dataFine;
	private String dataEstrCons;
	private String dataEstrCata ;
	private String cntScritti;
	private String cntNote;
	private String cntNoteReg; 
	private String cntNoteSca;
	private String cntImmoTratta;
	private String cntParticelle;
	private String cntFabbricati;
	
	
	
	
	
	public DupForniture(){
		   
		idFornitura="";
		codAmmin="";
		dataInizio="";
		dataFine="";
		dataEstrCons="";
		dataEstrCata="";
		cntScritti="";
		cntNote="";
		cntNoteReg=""; 
		cntNoteSca="";
		cntImmoTratta="";
		cntParticelle="";
		cntFabbricati="";
		}

	
	/* (non-Javadoc)
	 * @see it.escsolution.escwebgis.common.EscObject#getChiave()
	 */
	public String getChiave() {
		// TODO Auto-generated method stub
		return idFornitura;
	}
	
	/**
	 * @return Returns the codAmmin.
	 */
	public String getCodAmmin() {
		return codAmmin;
	}
	/**
	 * @param codAmmin The codAmmin to set.
	 */
	public void setCodAmmin(String codAmmin) {
		this.codAmmin = codAmmin;
	}
	/**
	 * @return Returns the dataEstrCata.
	 */
	public String getDataEstrCata() {
		return dataEstrCata;
	}
	/**
	 * @param dataEstrCata The dataEstrCata to set.
	 */
	public void setDataEstrCata(String dataEstrCata) {
		this.dataEstrCata = dataEstrCata;
	}
	/**
	 * @return Returns the dataEstrCons.
	 */
	public String getDataEstrCons() {
		return dataEstrCons;
	}
	/**
	 * @param dataEstrCons The dataEstrCons to set.
	 */
	public void setDataEstrCons(String dataEstrCons) {
		this.dataEstrCons = dataEstrCons;
	}
	/**
	 * @return Returns the dataFine.
	 */
	public String getDataFine() {
		return dataFine;
	}
	/**
	 * @param dataFine The dataFine to set.
	 */
	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}
	/**
	 * @return Returns the dataInizio.
	 */
	public String getDataInizio() {
		return dataInizio;
	}
	/**
	 * @param dataInizio The dataInizio to set.
	 */
	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}
	/**
	 * @return Returns the idFornitura.
	 */
	public String getIdFornitura() {
		return idFornitura;
	}
	/**
	 * @param idFornitura The idFornitura to set.
	 */
	public void setIdFornitura(String idFornitura) {
		this.idFornitura = idFornitura;
	}
	
	/**
	 * @return Returns the cntFabbricati.
	 */
	public String getCntFabbricati() {
		return cntFabbricati;
	}
	/**
	 * @param cntFabbricati The cntFabbricati to set.
	 */
	public void setCntFabbricati(String cntFabbricati) {
		this.cntFabbricati = cntFabbricati;
	}
	/**
	 * @return Returns the cntImmoTratta.
	 */
	public String getCntImmoTratta() {
		return cntImmoTratta;
	}
	/**
	 * @param cntImmoTratta The cntImmoTratta to set.
	 */
	public void setCntImmoTratta(String cntImmoTratta) {
		this.cntImmoTratta = cntImmoTratta;
	}
	/**
	 * @return Returns the cntNote.
	 */
	public String getCntNote() {
		return cntNote;
	}
	/**
	 * @param cntNote The cntNote to set.
	 */
	public void setCntNote(String cntNote) {
		this.cntNote = cntNote;
	}
	/**
	 * @return Returns the cntNoteReg.
	 */
	public String getCntNoteReg() {
		return cntNoteReg;
	}
	/**
	 * @param cntNoteReg The cntNoteReg to set.
	 */
	public void setCntNoteReg(String cntNoteReg) {
		this.cntNoteReg = cntNoteReg;
	}
	/**
	 * @return Returns the cntNoteSca.
	 */
	public String getCntNoteSca() {
		return cntNoteSca;
	}
	/**
	 * @param cntNoteSca The cntNoteSca to set.
	 */
	public void setCntNoteSca(String cntNoteSca) {
		this.cntNoteSca = cntNoteSca;
	}
	/**
	 * @return Returns the cntParticelle.
	 */
	public String getCntParticelle() {
		return cntParticelle;
	}
	/**
	 * @param cntParticelle The cntParticelle to set.
	 */
	public void setCntParticelle(String cntParticelle) {
		this.cntParticelle = cntParticelle;
	}
	/**
	 * @return Returns the cntScritti.
	 */
	public String getCntScritti() {
		return cntScritti;
	}
	/**
	 * @param cntScritti The cntScritti to set.
	 */
	public void setCntScritti(String cntScritti) {
		this.cntScritti = cntScritti;
	}
}