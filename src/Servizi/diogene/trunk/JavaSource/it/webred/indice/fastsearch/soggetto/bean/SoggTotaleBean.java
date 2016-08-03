package it.webred.indice.fastsearch.soggetto.bean;

import it.webred.indice.fastsearch.bean.TotaleBean;

import java.io.Serializable;
import java.util.List;

public class SoggTotaleBean extends TotaleBean {
	
	private String nome;
	private String cognome;
	private String fkSoggetto;
	private String dataNascita;
	private String denominaz;
	private String piva;
	private String codFisc;
	
	private List<List<SoggTotaleBean>> soggettiAssociati;
	
	private String tipoPersona;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getFkSoggetto() {
		return fkSoggetto;
	}
	public void setFkSoggetto(String fkSoggetto) {
		this.fkSoggetto = fkSoggetto;
	}
	public String getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getDenominaz() {
		return denominaz;
	}
	public void setDenominaz(String denominaz) {
		this.denominaz = denominaz;
	}
	public String getPiva() {
		return piva;
	}
	public void setPiva(String piva) {
		this.piva = piva;
	}
	public String getCodFisc() {
		return codFisc;
	}
	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	public List<List<SoggTotaleBean>> getSoggettiAssociati() {
		return soggettiAssociati;
	}
	public void setSoggettiAssociati(List<List<SoggTotaleBean>> soggettiAssociati) {
		this.soggettiAssociati = soggettiAssociati;
	}
		
}
