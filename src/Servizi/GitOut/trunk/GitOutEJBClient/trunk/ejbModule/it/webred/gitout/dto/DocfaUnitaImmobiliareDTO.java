package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="docfaUnitaImmobiliareDTO")
public class DocfaUnitaImmobiliareDTO implements Serializable{

	private static final long serialVersionUID = 6556430616137093135L;
	
	private String foglio = "";
	private String numero = "";
	private String tipoOp = "";
	private String subalterno = "";
	private String indirizzo = "";

	public DocfaUnitaImmobiliareDTO() {
	}//-------------------------------------------------------------------------
	
	public String getSubalterno() {
		return subalterno;
	}

	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String getTipoOp() {
		return tipoOp;
	}



	public void setTipoOp(String tipoOp) {
		this.tipoOp = tipoOp;
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
