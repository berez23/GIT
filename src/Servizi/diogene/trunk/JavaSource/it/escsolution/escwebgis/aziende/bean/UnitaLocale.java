/*
 * Created on 7-nov-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.escsolution.escwebgis.aziende.bean;

import it.escsolution.escwebgis.common.EscObject;

/**
 * @author Giulio Quaresima - WebRed
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UnitaLocale extends EscObject {
	private String provincia;
	private String comune;
	private String localita;
	private String sedime;
	private String nomeVia;
	private String civico;
	private String descrCategoria;
	private String descrAttitita;
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
	 * @return Returns the descrAttitita.
	 */
	public String getDescrAttitita() {
		return descrAttitita;
	}
	/**
	 * @param descrAttitita The descrAttitita to set.
	 */
	public void setDescrAttitita(String descrAttitita) {
		this.descrAttitita = descrAttitita;
	}
	/**
	 * @return Returns the descrCategoria.
	 */
	public String getDescrCategoria() {
		return descrCategoria;
	}
	/**
	 * @param descrCategoria The descrCategoria to set.
	 */
	public void setDescrCategoria(String descrCategoria) {
		this.descrCategoria = descrCategoria;
	}
	/**
	 * @return Returns the comune.
	 */
	public String getComune() {
		return comune;
	}
	/**
	 * @param comune The comune to set.
	 */
	public void setComune(String comune) {
		this.comune = comune;
	}
	/**
	 * @return Returns the localita.
	 */
	public String getLocalita() {
		return localita;
	}
	/**
	 * @param localita The localita to set.
	 */
	public void setLocalita(String localita) {
		this.localita = localita;
	}
	/**
	 * @return Returns the nomeVia.
	 */
	public String getNomeVia() {
		return nomeVia;
	}
	/**
	 * @param nomeVia The nomeVia to set.
	 */
	public void setNomeVia(String nomeVia) {
		this.nomeVia = nomeVia;
	}
	/**
	 * @return Returns the provincia.
	 */
	public String getProvincia() {
		return provincia;
	}
	/**
	 * @param provincia The provincia to set.
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	/**
	 * @return Returns the sedime.
	 */
	public String getSedime() {
		return sedime;
	}
	/**
	 * @param sedime The sedime to set.
	 */
	public void setSedime(String sedime) {
		this.sedime = sedime;
	}
}
