/*
 * Created on 13-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.agenziaEntrate.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Giulio Quaresima - WebRed
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RapLegale extends EscObject implements Serializable{
	
	private String codEnt;
	private String idRap;
	private String idContribuente;	
	private String dataInizioCarRap;
	private String codiceFiscaleRap;
	private String dataFineCarRap;
	private String tipoCarRap;
	private String fonteRap;
	
	public RapLegale(){
		   
		codEnt="";
		idRap="";
		idContribuente="";	
		dataInizioCarRap="";
		codiceFiscaleRap="";
		dataFineCarRap="";
		tipoCarRap="";
		fonteRap="";
	}

	

	/**
	 * @return Returns the codEnt.
	 */
	public String getCodEnt() {
		return codEnt;
	}
	/**
	 * @param codEnt The codEnt to set.
	 */
	public void setCodEnt(String codEnt) {
		this.codEnt = codEnt;
	}
	/**
	 * @return Returns the codiceFiscaleRap.
	 */
	public String getCodiceFiscaleRap() {
		return codiceFiscaleRap;
	}
	/**
	 * @param codiceFiscaleRap The codiceFiscaleRap to set.
	 */
	public void setCodiceFiscaleRap(String codiceFiscaleRap) {
		this.codiceFiscaleRap = codiceFiscaleRap;
	}
	/**
	 * @return Returns the dataFineCarRap.
	 */
	public String getDataFineCarRap() {
		return dataFineCarRap;
	}
	/**
	 * @param dataFineCarRap The dataFineCarRap to set.
	 */
	public void setDataFineCarRap(String dataFineCarRap) {
		this.dataFineCarRap = dataFineCarRap;
	}
	/**
	 * @return Returns the dataInizioCarRap.
	 */
	public String getDataInizioCarRap() {
		return dataInizioCarRap;
	}
	/**
	 * @param dataInizioCarRap The dataInizioCarRap to set.
	 */
	public void setDataInizioCarRap(String dataInizioCarRap) {
		this.dataInizioCarRap = dataInizioCarRap;
	}
	/**
	 * @return Returns the idContribuente.
	 */
	public String getIdContribuente() {
		return idContribuente;
	}
	/**
	 * @param idContribuente The idContribuente to set.
	 */
	public void setIdContribuente(String idContribuente) {
		this.idContribuente = idContribuente;
	}
	/**
	 * @return Returns the idRap.
	 */
	public String getIdRap() {
		return idRap;
	}
	/**
	 * @param idRap The idRap to set.
	 */
	public void setIdRap(String idRap) {
		this.idRap = idRap;
	}
	/**
	 * @return Returns the tipoCarRap.
	 */
	public String getTipoCarRap() {
		return tipoCarRap;
	}
	/**
	 * @param tipoCarRap The tipoCarRap to set.
	 */
	public void setTipoCarRap(String tipoCarRap) {
		this.tipoCarRap = tipoCarRap;
	}
	
	
	/**
	 * @return Returns the fonteRap.
	 */
	public String getFonteRap() {
		return fonteRap;
	}
	/**
	 * @param fonteRap The fonteRap to set.
	 */
	public void setFonteRap(String fonteRap) {
		this.fonteRap = fonteRap;
	}
	}