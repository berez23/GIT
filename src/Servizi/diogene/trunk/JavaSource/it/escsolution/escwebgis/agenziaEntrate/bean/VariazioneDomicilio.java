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
public class VariazioneDomicilio extends EscObject implements Serializable{
	
	private String idVMod;
	private String idContribuente;
	private String codEnt;
	private String dataInizioValVDom;
	private String dataFineValVDom;
	private String comuneVDom;
	private String indCivVDom;
	private String provinciaVDom;
	private String capVDom;
	private String fonteSDom;       
	
		
	public VariazioneDomicilio(){
		   
		idVMod="";
		idContribuente="";
		codEnt="";
		dataInizioValVDom="";	
		dataFineValVDom="";
		comuneVDom="";
		indCivVDom="";
		provinciaVDom="";
		capVDom="";
		fonteSDom ="";
	}

	
	/**
	 * @return Returns the capVDom.
	 */
	public String getCapVDom() {
		return capVDom;
	}
	/**
	 * @param capVDom The capVDom to set.
	 */
	public void setCapVDom(String capVDom) {
		this.capVDom = capVDom;
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
	 * @return Returns the comuneVDom.
	 */
	public String getComuneVDom() {
		return comuneVDom;
	}
	/**
	 * @param comuneVDom The comuneVDom to set.
	 */
	public void setComuneVDom(String comuneVDom) {
		this.comuneVDom = comuneVDom;
	}
	/**
	 * @return Returns the dataFineValVDom.
	 */
	public String getDataFineValVDom() {
		return dataFineValVDom;
	}
	/**
	 * @param dataFineValVDom The dataFineValVDom to set.
	 */
	public void setDataFineValVDom(String dataFineValVDom) {
		this.dataFineValVDom = dataFineValVDom;
	}
	/**
	 * @return Returns the dataInizioValVDom.
	 */
	public String getDataInizioValVDom() {
		return dataInizioValVDom;
	}
	/**
	 * @param dataInizioValVDom The dataInizioValVDom to set.
	 */
	public void setDataInizioValVDom(String dataInizioValVDom) {
		this.dataInizioValVDom = dataInizioValVDom;
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
	 * @return Returns the idVMod.
	 */
	public String getIdVMod() {
		return idVMod;
	}
	/**
	 * @param idVMod The idVMod to set.
	 */
	public void setIdVMod(String idVMod) {
		this.idVMod = idVMod;
	}
	/**
	 * @return Returns the indCivVDom.
	 */
	public String getIndCivVDom() {
		return indCivVDom;
	}
	/**
	 * @param indCivVDom The indCivVDom to set.
	 */
	public void setIndCivVDom(String indCivVDom) {
		this.indCivVDom = indCivVDom;
	}
	/**
	 * @return Returns the provinciaVDom.
	 */
	public String getProvinciaVDom() {
		return provinciaVDom;
	}
	/**
	 * @param provinciaVDom The provinciaVDom to set.
	 */
	public void setProvinciaVDom(String provinciaVDom) {
		this.provinciaVDom = provinciaVDom;
	}
	
	
	/**
	 * @return Returns the fonteSDom.
	 */
	public String getFonteSDom() {
		return fonteSDom;
	}
	/**
	 * @param fonteSDom The fonteSDom to set.
	 */
	public void setFonteSDom(String fonteSDom) {
		this.fonteSDom = fonteSDom;
	}
	}