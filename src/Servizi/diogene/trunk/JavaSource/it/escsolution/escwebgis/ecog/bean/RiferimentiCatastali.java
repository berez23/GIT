/*
 * Created on 21-nov-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.escsolution.escwebgis.ecog.bean;

import it.escsolution.escwebgis.common.EscObject;

/**
 * @author Utente
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RiferimentiCatastali extends EscObject {
	int foglio;
	String Particella;
	String unimm;
	
	private String latitudine;
	private String longitudine;
	
	/**
	 * @return Returns the foglio.
	 */
	public int getFoglio() {
		return foglio;
	}
	/**
	 * @param foglio The foglio to set.
	 */
	public void setFoglio(int foglio) {
		this.foglio = foglio;
	}
	/**
	 * @return Returns the particella.
	 */
	public String getParticella() {
		return Particella;
	}
	/**
	 * @param particella The particella to set.
	 */
	public void setParticella(String particella) {
		Particella = particella;
	}
	/**
	 * @return Returns the unimm.
	 */
	public String getUnimm() {
		return unimm;
	}
	/**
	 * @param unimm The unimm to set.
	 */
	public void setUnimm(String unimm) {
		this.unimm = unimm;
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
