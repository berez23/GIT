package it.escsolution.escwebgis.indagineCivico.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.Hashtable;



public class Anagrafe extends EscObject implements Serializable {
	
	private String id;
	private String idExt;
	private String codAnagrafe;
	private String tipoSoggetto;
	private String famiglia;
	private String tipoParentela;
	private String sezElettorali;
	private String codFiscale;
	private String cognome;
	private String nome;
	private String dataNascita;
	private String sesso;
	private String comuneNascita;
	private String cittadinanza;
	private String codiceNazionale;
	
	public Anagrafe() {
		id ="";
		idExt="";;
		codAnagrafe="";
		tipoSoggetto="";
		famiglia="";
		tipoParentela="";;
		sezElettorali="";
		codFiscale="";
		cognome="";
		nome="";
		dataNascita="";
		sesso="";
		comuneNascita="";
		cittadinanza="";
		codiceNazionale="";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdExt() {
		return idExt;
	}
	public void setIdExt(String idExt) {
		this.idExt = idExt;
	}
	public String getCodAnagrafe() {
		return codAnagrafe;
	}
	public void setCodAnagrafe(String codAnagrafe) {
		this.codAnagrafe = codAnagrafe;
	}
	public String getTipoSoggetto() {
		return tipoSoggetto;
	}
	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}
	public String getFamiglia() {
		return famiglia;
	}
	public void setFamiglia(String famiglia) {
		this.famiglia = famiglia;
	}
	public String getSezElettorali() {
		return sezElettorali;
	}
	public void setSezElettorali(String sezElettorali) {
		this.sezElettorali = sezElettorali;
	}
	public String getCodFiscale() {
		return codFiscale;
	}
	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getSesso() {
		return sesso;
	}
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	public String getComuneNascita() {
		return comuneNascita;
	}
	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}
	public String getCittadinanza() {
		return cittadinanza;
	}
	public void setCittadinanza(String cittadinanza) {
		this.cittadinanza = cittadinanza;
	}
	public String getCodiceNazionale() {
		return codiceNazionale;
	}
	public void setCodiceNazionale(String codiceNazionale) {
		this.codiceNazionale = codiceNazionale;
	}
	public String getTipoParentela() {
		return tipoParentela;
	}
	public void setTipoParentela(String tipoParentela) {
		this.tipoParentela = tipoParentela;
	}
	

}
