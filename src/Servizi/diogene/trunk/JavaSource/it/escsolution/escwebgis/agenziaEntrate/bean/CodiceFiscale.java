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
public class CodiceFiscale extends EscObject implements Serializable{
	
	private String idCodiFisc;
	private String idContribuente;
	private String codEnt;
	private String codiceFiscale;
	       
	
	
	
	public CodiceFiscale(){
		   
		idCodiFisc="";
		idContribuente="";
		codEnt="";
		codiceFiscale="";	
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
	 * @return Returns the fkIdContribuente.
	 */
	public String getFkIdContribuente() {
		return idContribuente;
	}
	/**
	 * @param fkIdContribuente The fkIdContribuente to set.
	 */
	public void setIdContribuente(String idContribuente) {
		this.idContribuente = idContribuente;
	}
	/**
	 * @return Returns the pkIdCodiFisc.
	 */
	public String getIdCodiFisc() {
		return idCodiFisc;
	}
	/**
	 * @param pkIdCodiFisc The pkIdCodiFisc to set.
	 */
	public void setIdCodiFisc(String IdCodiFisc) {
		this.idCodiFisc = IdCodiFisc;
	}
	}