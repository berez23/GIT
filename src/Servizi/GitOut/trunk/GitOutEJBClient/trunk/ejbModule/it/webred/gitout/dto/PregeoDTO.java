package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="pregeoDTO")
public class PregeoDTO implements Serializable{
	
	private static final long serialVersionUID = 4510335205300639795L;
	
	private String descrizione = "";
	private String codicePregeo = "";
	private String dataPregeo = "";
	private String denominazione = "";
	private String tecnico = "";
	private String foglio = "";
	private String particella = "";

	public PregeoDTO() {
	}//-------------------------------------------------------------------------

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodicePregeo() {
		return codicePregeo;
	}

	public void setCodicePregeo(String codicePregeo) {
		this.codicePregeo = codicePregeo;
	}

	public String getDataPregeo() {
		return dataPregeo;
	}

	public void setDataPregeo(String dataPregeo) {
		this.dataPregeo = dataPregeo;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getTecnico() {
		return tecnico;
	}

	public void setTecnico(String tecnico) {
		this.tecnico = tecnico;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
