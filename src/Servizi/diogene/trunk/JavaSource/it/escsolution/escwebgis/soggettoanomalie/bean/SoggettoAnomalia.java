/*
 * Created on 13-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.soggettoanomalie.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Giulio Quaresima - WebRed
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SoggettoAnomalia extends EscObject implements Serializable{
	
	private String pk_idinternosgtscarti;
	private String regola;
	private String data_caricamento;
	private String codent;
	private String cod_fisc;
	private String part_iva;
	private String tipo_persona;
	private String denominazione;
	private String cognome;
	private String nome;
	private String sesso;
	private String nasdata;
	private String nasluogo;
	private String nascodcom;
	private String fk_db;
	private String fk_chiave;
	private String descrizione;
	private String descrDB;
	
	
	
	
	
	public SoggettoAnomalia(){
		   
		pk_idinternosgtscarti="";
		regola="";
		data_caricamento="";
		codent="";
		cod_fisc="";
		part_iva="";
		tipo_persona="";
		denominazione="";
		cognome="";
		nome="";
		sesso="";
		nasdata="";
		nasluogo="";
		nascodcom="";
		fk_db="";
		fk_chiave="";
		descrizione="";
		descrDB="";
		}





	public String getCod_fisc() {
		return cod_fisc;
	}





	public void setCod_fisc(String cod_fisc) {
		this.cod_fisc = cod_fisc;
	}





	public String getCodent() {
		return codent;
	}





	public void setCodent(String codent) {
		this.codent = codent;
	}





	public String getCognome() {
		return cognome;
	}





	public void setCognome(String cognome) {
		this.cognome = cognome;
	}





	public String getData_caricamento() {
		return data_caricamento;
	}





	public void setData_caricamento(String data_caricamento) {
		this.data_caricamento = data_caricamento;
	}





	public String getDenominazione() {
		return denominazione;
	}





	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}





	public String getDescrizione() {
		return descrizione;
	}





	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}





	public String getFk_chiave() {
		return fk_chiave;
	}





	public void setFk_chiave(String fk_chiave) {
		this.fk_chiave = fk_chiave;
	}





	public String getFk_db() {
		return fk_db;
	}





	public void setFk_db(String fk_db) {
		this.fk_db = fk_db;
	}





	public String getNascodcom() {
		return nascodcom;
	}





	public void setNascodcom(String nascodcom) {
		this.nascodcom = nascodcom;
	}





	public String getNasdata() {
		return nasdata;
	}





	public void setNasdata(String nasdata) {
		this.nasdata = nasdata;
	}





	public String getNasluogo() {
		return nasluogo;
	}





	public void setNasluogo(String nasluogo) {
		this.nasluogo = nasluogo;
	}





	public String getNome() {
		return nome;
	}





	public void setNome(String nome) {
		this.nome = nome;
	}





	public String getPart_iva() {
		return part_iva;
	}





	public void setPart_iva(String part_iva) {
		this.part_iva = part_iva;
	}





	public String getPk_idinternosgtscarti() {
		return pk_idinternosgtscarti;
	}





	public void setPk_idinternosgtscarti(String pk_idinternosgtscarti) {
		this.pk_idinternosgtscarti = pk_idinternosgtscarti;
	}





	public String getRegola() {
		return regola;
	}





	public void setRegola(String regola) {
		this.regola = regola;
	}





	public String getSesso() {
		return sesso;
	}





	public void setSesso(String sesso) {
		this.sesso = sesso;
	}





	public String getTipo_persona() {
		return tipo_persona;
	}





	public void setTipo_persona(String tipo_persona) {
		this.tipo_persona = tipo_persona;
	}





	public String getDescrDB() {
		return descrDB;
	}





	public void setDescrDB(String descrDB) {
		this.descrDB = descrDB;
	}

	

}