package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="terreniMuiDTO")
public class TerreniMuiDTO implements Serializable{

	private static final long serialVersionUID = 6083976671312888598L;
	
	private String foglio = "";
	private String numero = "";
	private String sezione = "";
	private String subalterno = "";
	private String idCatastale = "";
	private String codEsito = "";
	private String edificabilita = "";
	private String natura = "";
	private String qualita = "";
	private String classe = "";
	private String ettari = "";
	private String are = "";
	private String centiare = "";
	private String redditoDominicale = "";
	private String redditoAgrario = "";
	private String partita = "";

	public TerreniMuiDTO() {

	}//-------------------------------------------------------------------------

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getSubalterno() {
		return subalterno;
	}

	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

	public String getIdCatastale() {
		return idCatastale;
	}

	public void setIdCatastale(String idCatastale) {
		this.idCatastale = idCatastale;
	}

	public String getCodEsito() {
		return codEsito;
	}

	public void setCodEsito(String codEsito) {
		this.codEsito = codEsito;
	}

	public String getEdificabilita() {
		return edificabilita;
	}

	public void setEdificabilita(String edificabilita) {
		this.edificabilita = edificabilita;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getQualita() {
		return qualita;
	}

	public void setQualita(String qualita) {
		this.qualita = qualita;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getEttari() {
		return ettari;
	}

	public void setEttari(String ettari) {
		this.ettari = ettari;
	}

	public String getAre() {
		return are;
	}

	public void setAre(String are) {
		this.are = are;
	}

	public String getCentiare() {
		return centiare;
	}

	public void setCentiare(String centiare) {
		this.centiare = centiare;
	}

	public String getRedditoDominicale() {
		return redditoDominicale;
	}

	public void setRedditoDominicale(String redditoDominicale) {
		this.redditoDominicale = redditoDominicale;
	}

	public String getRedditoAgrario() {
		return redditoAgrario;
	}

	public void setRedditoAgrario(String redditoAgrario) {
		this.redditoAgrario = redditoAgrario;
	}

	public String getPartita() {
		return partita;
	}

	public void setPartita(String partita) {
		this.partita = partita;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	
	
}
