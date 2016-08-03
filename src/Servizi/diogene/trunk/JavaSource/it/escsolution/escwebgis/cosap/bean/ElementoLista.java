/*
 * Created on 13-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.cosap.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Giulio Quaresima - WebRed
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ElementoLista extends EscObject implements Serializable
{
	
	private String descrizoneContribuente;
	private String codiceContribuente;

	private String codiceFiscale;
	private String partitaIva;
	
	public String getChiave(){ 
		return codiceContribuente;
	}
	
	public String getDescrizoneContribuente() {
		return descrizoneContribuente;
	}
	public void setDescrizoneContribuente(String descrizoneContribuente) {
		this.descrizoneContribuente = descrizoneContribuente;
	}
	public String getCodiceContribuente() {
		return codiceContribuente;
	}
	public void setCodiceContribuente(String codiceContribuente) {
		this.codiceContribuente = codiceContribuente;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getPartitaIva() {
		return partitaIva;
	}
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}


	
}
