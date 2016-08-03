package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="unitaTerreDTO")
public class UnitaTerreDTO implements Serializable{

	private static final long serialVersionUID = -3566193345880741781L;
	
	private String descrizione = "";
	/* 
	* Nella query la sezione è in join con la siticomu quindi va bene come parametro 
	* di ricerca ma non è presente come informazione nella Sititrkc
	*/
//	private String sezione = "";
	private String foglio = "";
	private String numero = "";
	private String subalterno = "";
//	private String dataInizioVal = "";		//coincide con la data fine precedente
	private String dataFineVal = "";
	private String classe = "";
	private String qualita = "";
	private String rendita = "";
	private String redditoDominicale = "";
	private String redditoAgrario = "";
	
	private ArrayList<PersonaDTO> alPersone = null;

	public UnitaTerreDTO() {

	}//-------------------------------------------------------------------------


	public String getDescrizione() {
		return descrizione;
	}


	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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


//	public String getDataInizioVal() {
//		return dataInizioVal;
//	}
//
//
//	public void setDataInizioVal(String dataInizioVal) {
//		this.dataInizioVal = dataInizioVal;
//	}


	public String getDataFineVal() {
		return dataFineVal;
	}


	public void setDataFineVal(String dataFineVal) {
		this.dataFineVal = dataFineVal;
	}


	public String getClasse() {
		return classe;
	}


	public void setClasse(String classe) {
		this.classe = classe;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


//	public String getSezione() {
//		return sezione;
//	}
//
//
//	public void setSezione(String sezione) {
//		this.sezione = sezione;
//	}


	public String getSubalterno() {
		return subalterno;
	}


	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

	public String getQualita() {
		return qualita;
	}


	public void setQualita(String qualita) {
		this.qualita = qualita;
	}


	public String getRendita() {
		return rendita;
	}


	public void setRendita(String rendita) {
		this.rendita = rendita;
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


	public ArrayList<PersonaDTO> getAlPersone() {
		return alPersone;
	}


	public void setAlPersone(ArrayList<PersonaDTO> alPersone) {
		this.alPersone = alPersone;
	}

	
	

}
