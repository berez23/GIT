/*
 * Created on 18-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.common;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PulsantiNavigazione implements Serializable {
	private boolean lista;
	private boolean primo;
	private boolean ricerca;
	private boolean back;
	private boolean multipagina;
	private boolean multirecord;
	private boolean mappa;
	private boolean link;
	private String ext;
	
	private boolean stampa;


	/**
	 * 
	 */

	public void chiamataRicerca(){
		lista = false;
		primo = false;
		ricerca = false;
		back = false;
		multipagina = false;
		multirecord = true;
		mappa = false;
		stampa = false;
	}

	public void chiamataInternaDettaglio(){
		lista = true;
		primo = false;
		ricerca = true;
		back = true;
		multipagina = false;
		multirecord = true;
		stampa = true;
	}
	public void chiamataEsternaDettaglio(){
		lista = false;
		primo = false;
		ricerca = true;
		back = true;
		multipagina = false;
		multirecord = false;
		stampa = true;
	}
	public void chiamataInternaLista(){
		lista = false;
		primo = true;
		ricerca = true;
		back = true;
		multipagina = true;
		multirecord = false;
		mappa = false;
		stampa = true;
	}
	public void chiamataEsternaLista(){
		lista = false;
		primo = false;
		ricerca = true;
		back = true;
		multipagina = true;
		multirecord = false;
		mappa = false;
		stampa = true;
	}
	public PulsantiNavigazione() {
		super();
		lista = true;
		primo = true;
		ricerca = true;
		back = true;
		multipagina = true;
		multirecord = true;
		stampa = true;
	}


	public void chiamataPart(){
			lista = false;
			primo = false;
			ricerca = true;
			back = true;
			multipagina = false;
			multirecord = false;
			stampa = true;
		}
	/**
	 * @return
	 */
	public boolean isBack() {
		return back;
	}

	/**
	 * @return
	 */
	public boolean isLista() {
		return lista;
	}

	/**
	 * @return
	 */
	public boolean isMultipagina() {
		return multipagina;
	}

	/**
	 * @return
	 */
	public boolean isMultirecord() {
		return multirecord;
	}

	/**
	 * @return
	 */
	public boolean isPrimo() {
		return primo;
	}

	/**
	 * @return
	 */
	public boolean isRicerca() {
		return ricerca;
	}

	/**
	 * @param b
	 */
	public void setBack(boolean b) {
		back = b;
	}

	/**
	 * @param b
	 */
	public void setLista(boolean b) {
		lista = b;
	}

	/**
	 * @param b
	 */
	public void setMultipagina(boolean b) {
		multipagina = b;
	}

	/**
	 * @param b
	 */
	public void setMultirecord(boolean b) {
		multirecord = b;
	}

	/**
	 * @param b
	 */
	public void setPrimo(boolean b) {
		primo = b;
	}

	/**
	 * @param b
	 */
	public void setRicerca(boolean b) {
		ricerca = b;
	}

	/**
	 * @return
	 */
	public String getExt() {
		return ext;
	}

	/**
	 * @return
	 */
	public boolean isLink() {
		return link;
	}

	/**
	 * @param string
	 */
	public void setExt(String string) {
		ext = string;
	}

	/**
	 * @param b
	 */
	public void setLink(boolean b) {
		link = b;
	}

	/**
	 * @return
	 */
	public boolean isMappa() {
		return mappa;
	}

	/**
	 * @param b
	 */
	public void setMappa(boolean b) {
		mappa = b;
	}

	public boolean isStampa() {
		return stampa;
	}

	public void setStampa(boolean stampa) {
		this.stampa = stampa;
	}


}
