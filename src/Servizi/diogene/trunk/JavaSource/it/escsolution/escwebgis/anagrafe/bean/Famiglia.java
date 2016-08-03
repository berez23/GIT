/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.anagrafe.bean;
import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Famiglia extends EscObject implements Serializable{

	private String codFamiglia;
	private String comune;
	private String tipoFamiglia;
	private String denominazione;
	private String codFiscale;
	private String strada;
	private String civico;
	private String scala;
	private String interno;
	private String piano;
	private String indirizzo;
	private String dataCaricamento;
	private String descrCivico;
	
	
		public Famiglia(){
			
			codFamiglia="";
			comune="";
			tipoFamiglia="";
			denominazione="";
			codFiscale="";
			strada="";
			civico="";
			scala="";
			interno="";
			piano="";
			indirizzo="";
			dataCaricamento="";
			descrCivico="";
			
			
		}
	
		
	/**
	 * @return
	 */
	
	public String getChiave() {
			return codFamiglia;
		}
		
	public String getCivico() {
		return civico;
	}

	/**
	 * @return
	 */
	public String getCodFamiglia() {
		return codFamiglia;
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
	public String getDataCaricamento() {
		return dataCaricamento;
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
	public String getDescrCivico() {
		return descrCivico;
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
	public String getPiano() {
		return piano;
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
	public String getStrada() {
		return strada;
	}

	/**
	 * @return
	 */
	public String getTipoFamiglia() {
		return tipoFamiglia;
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
	public void setCodFamiglia(String string) {
		codFamiglia = string;
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
	public void setDataCaricamento(String string) {
		dataCaricamento = string;
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
	public void setDescrCivico(String string) {
		descrCivico = string;
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
	public void setPiano(String string) {
		piano = string;
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
	public void setStrada(String string) {
		strada = string;
	}

	/**
	 * @param string
	 */
	public void setTipoFamiglia(String string) {
		tipoFamiglia = string;
	}

	
	

}
