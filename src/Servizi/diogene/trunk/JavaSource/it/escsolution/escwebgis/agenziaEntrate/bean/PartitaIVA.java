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
public class PartitaIVA extends EscObject implements Serializable{
	
	private String idPartitaIVA;
	private String idContribuente;
	private String codEnt;
	private String partitaIVA;
	private String dataCessaPI;
	private String motiviCessaPI;
	private String confluenzaPI;
	       
	
	
	
	public PartitaIVA(){
		   
		idPartitaIVA="";
		idContribuente="";
		codEnt="";
		partitaIVA="";	
		dataCessaPI="";
		motiviCessaPI="";
		confluenzaPI="";
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
	 * @return Returns the confluenzaPI.
	 */
	public String getConfluenzaPI() {
		return confluenzaPI;
	}
	/**
	 * @param confluenzaPI The confluenzaPI to set.
	 */
	public void setConfluenzaPI(String confluenzaPI) {
		this.confluenzaPI = confluenzaPI;
	}
	/**
	 * @return Returns the dataCessaPI.
	 */
	public String getDataCessaPI() {
		return dataCessaPI;
	}
	/**
	 * @param dataCessaPI The dataCessaPI to set.
	 */
	public void setDataCessaPI(String dataCessaPI) {
		this.dataCessaPI = dataCessaPI;
	}
	/**
	 * @return Returns the fkIdContribuente.
	 */
	public String getidContribuente() {
		return idContribuente;
	}
	/**
	 * @param fkIdContribuente The fkIdContribuente to set.
	 */
	public void setidContribuente(String idContribuente) {
		this.idContribuente = idContribuente;
	}
	/**
	 * @return Returns the motiviCessaPI.
	 */
	public String getMotiviCessaPI() {
		return motiviCessaPI;
	}
	/**
	 * @param motiviCessaPI The motiviCessaPI to set.
	 */
	public void setMotiviCessaPI(String motiviCessaPI) {
		this.motiviCessaPI = motiviCessaPI;
	}
	/**
	 * @return Returns the partitaIVA.
	 */
	public String getPartitaIVA() {
		return partitaIVA;
	}
	/**
	 * @param partitaIVA The partitaIVA to set.
	 */
	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
	}
	/**
	 * @return Returns the pkIdPartitaIVA.
	 */
	public String getPkIdPartitaIVA() {
		return idPartitaIVA;
	}
	/**
	 * @param pkIdPartitaIVA The pkIdPartitaIVA to set.
	 */
	public void setIdPartitaIVA(String idPartitaIVA) {
		this.idPartitaIVA = idPartitaIVA;
	}
	}