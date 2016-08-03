package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="titolaritaDTO")
public class TitolaritaDTO implements Serializable{

	private static final long serialVersionUID = 1229841628432284479L;
	
	private String foglio = "";
	private String numero = "";
	private String sezione = "";
	private String unimm = "";
	private String categoria = "";
	private String dataFinePos = "";
	private String titolo = "";
	private String percPos = "";

	public TitolaritaDTO() {

	}//-------------------------------------------------------------------------

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getUnimm() {
		return unimm;
	}

	public void setUnimm(String unimm) {
		this.unimm = unimm;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getDataFinePos() {
		return dataFinePos;
	}

	public void setDataFinePos(String dataFinePos) {
		this.dataFinePos = dataFinePos;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getPercPos() {
		return percPos;
	}

	public void setPercPos(String percPos) {
		this.percPos = percPos;
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
