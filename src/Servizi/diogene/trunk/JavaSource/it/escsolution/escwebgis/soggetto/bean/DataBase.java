/*
 * Created on 13-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.soggetto.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.Vector;
/**
 * @author Giulio Quaresima - WebRed
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DataBase extends EscObject implements Serializable{
	
	private String idDB;
	private String nome;
	private String descrizione;
	private Vector elementi;  
	private String urlDB;
	private String codProcedura;
	
	
	
	public DataBase(){
		idDB = "";
		nome = "";
		descrizione = "";		
		elementi = new Vector();
		urlDB = "";
		codProcedura = "";
	}

	public String getNome() {
		return nome;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public String getIdDB() {
		return idDB;
	}
	public Vector getElementi(){
		return elementi;
	}
	
	public void setNome(String string) {
		nome = string;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public void setIdDB(String idDB) {
		this.idDB = idDB;
	}
	
	public void setElementi(Vector elems){
		elementi = elems;
	}
	public String getUrlDB() {
		return urlDB;
	}
	public void setUrlDB(String urlDB) {
		this.urlDB = urlDB;
	}

	public String getCodProcedura() {
		return codProcedura;
	}
	public void setCodProcedura(String codProcedura) {
		this.codProcedura = codProcedura;
	}
}
