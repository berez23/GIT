package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="personaDTO")
public class PersonaDTO implements Serializable{
	
	private static final long serialVersionUID = -1358763152320127818L;
	
	private String descrizione = "";

	private String ragiSoci;
	private String nome;
	private String dataInizio;
	private String dataFine;
	private String percPoss;
	private String descTipo;
	private String titolo;
	private String tipoTitolo;
	private String codiFisc;
	private String codiPiva;
	private String sesso;
	private String flagPersFisica;
	
//	private String tipImm;
	private String comuNasc;
//	private String dataMorte;
	private String dataNasc;
//	private String dataRegistrazione;
//	private String naturaGiuridica;
//	private String dataInizioPossesso;
//	private String dataFinePossesso;
//	private String resiComu;
	

	public PersonaDTO() {

	}//-------------------------------------------------------------------------

	public String getDescTipo() {
		return descTipo;
	}

	public void setDescTipo(String descTipo) {
		this.descTipo = descTipo;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getTipoTitolo() {
		return tipoTitolo;
	}

	public void setTipoTitolo(String tipoTitolo) {
		this.tipoTitolo = tipoTitolo;
	}

	public String getPercPoss() {
		return percPoss;
	}

	public void setPercPoss(String percPoss) {
		this.percPoss = percPoss;
	}

	public String getCodiFisc() {
		return codiFisc;
	}

	public void setCodiFisc(String codiFisc) {
		this.codiFisc = codiFisc;
	}

	public String getCodiPiva() {
		return codiPiva;
	}

	public void setCodiPiva(String codiPiva) {
		this.codiPiva = codiPiva;
	}


	public String getDataFine() {
		return dataFine;
	}

	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}


	public String getFlagPersFisica() {
		return flagPersFisica;
	}

	public void setFlagPersFisica(String flagPersFisica) {
		this.flagPersFisica = flagPersFisica;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRagiSoci() {
		return ragiSoci;
	}

	public void setRagiSoci(String ragiSoci) {
		this.ragiSoci = ragiSoci;
	}


	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getComuNasc() {
		return comuNasc;
	}

	public void setComuNasc(String comuNasc) {
		this.comuNasc = comuNasc;
	}

	public String getDataNasc() {
		return dataNasc;
	}

	public void setDataNasc(String dataNasc) {
		this.dataNasc = dataNasc;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


}
